package com.buzzup.authentication.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.buzzup.authentication.exception.UserAlreadyExistsException;
import com.buzzup.authentication.exception.UserNotFoundException;

import com.buzzup.authentication.model.User;
import com.buzzup.authentication.repository.UserAuthenticationRepository;

@Service
public class UserAuthenticationServiceImpl implements UserAuthenticationService {

	private UserAuthenticationRepository authenticationRepository;
	
	@Autowired
	public UserAuthenticationServiceImpl(UserAuthenticationRepository authenticationRepository) {
		super();
		this.authenticationRepository = authenticationRepository;
	}

	@Override
	public User findByEmailIdAndPassword(String emailId, String password) throws UserNotFoundException {
		User user = authenticationRepository.findByEmailIdAndPassword(emailId, password);
		if (user == null) {
			throw new UserNotFoundException("EmailId and Password mismatch");
		}
		return user;
	}

	@Override
	public boolean saveUser(User user) throws UserAlreadyExistsException {
		Optional<User> fetchedUser = authenticationRepository.findById(user.getEmailId());
		if (fetchedUser.isPresent()) {
			throw new UserAlreadyExistsException("User with EmailId already exists");
		}
		authenticationRepository.save(user);
		return true;
	}

}
