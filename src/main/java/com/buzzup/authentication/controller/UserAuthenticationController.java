package com.buzzup.authentication.controller;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.buzzup.authentication.exception.UserAlreadyExistsException;
import com.buzzup.authentication.exception.UserIdAndPasswordMismatchException;
import com.buzzup.authentication.exception.UserNotFoundException;
import com.buzzup.authentication.exception.UserNullException;
import com.buzzup.authentication.model.User;
import com.buzzup.authentication.security.SecurityTokenGenerator;
import com.buzzup.authentication.service.UserAuthenticationService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/* As the application is made of RESTful web services, this class is annotated with @RestController annotation.
 */

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class UserAuthenticationController {
	private UserAuthenticationService authenticationService;
	
	@Autowired
	public UserAuthenticationController(UserAuthenticationService authenticationService) {
		super();
		this.authenticationService = authenticationService;
	}


	/*
	 * This handler method is used to create a user and it calls the corresponding
	 * UserService methods to store the user information in a database.
	 */
	
	
	@PostMapping("/v1/auth/register")
	//@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody User user) {
		ResponseEntity<?> responseEntity = null;
		try {
			authenticationService.saveUser(user);
			responseEntity = new ResponseEntity<>(user, HttpStatus.CREATED);
		} catch (UserAlreadyExistsException exception) {
			responseEntity = new ResponseEntity<String>("{ \"message\": \"" + exception.getMessage() + "\"}",
					HttpStatus.CONFLICT);
		}
		return responseEntity;
	}
	

	/*
	 * This handler method is used to authenticate the user based on the credentials
	 * provided(emailId,password). Once authentication is done, a JWT token is
	 * returned to the client.
	 */
	
	
	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody User loginDetails) {
		try {
			String userId = loginDetails.getEmailId();
			String password = loginDetails.getPassword();
			if (userId == null || password == null) {
				throw new UserNullException("Userid and Password cannot be empty");
			}
			User user = authenticationService.findByEmailIdAndPassword(userId, password);
			if (user == null) {
				throw new UserNotFoundException("User with given Id does not exists");
			}
			String fetchedPassword = user.getPassword();
			if (!password.equals(fetchedPassword)) {
				throw new UserIdAndPasswordMismatchException(
						"Invalid login credential, Please check username and password ");
			}
			// generating token
			SecurityTokenGenerator securityTokenGenrator = (User userDetails) -> {
				String jwtToken = "";
				jwtToken = Jwts.builder().setId(user.getEmailId()).setSubject(user.getUserRole()).setIssuedAt(new Date())
						.signWith(SignatureAlgorithm.HS256, "secretkey").compact();
				Map<String, String> map1 = new HashMap<>();
				map1.put("token", jwtToken);
				map1.put("message", "User successfully logged in");
				return map1;
			};
			Map<String, String> map = securityTokenGenrator.generateToken(user);
			return new ResponseEntity<>(map, HttpStatus.OK);
		} catch (UserNullException | UserNotFoundException | UserIdAndPasswordMismatchException exception) {
			return new ResponseEntity<>("{ \"message\": \"" + exception.getMessage() + "\"}", HttpStatus.UNAUTHORIZED);
		}
	}
}
