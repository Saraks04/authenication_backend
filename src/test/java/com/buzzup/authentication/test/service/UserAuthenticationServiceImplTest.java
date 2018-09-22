package com.buzzup.authentication.test.service;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.buzzup.authentication.exception.UserAlreadyExistsException;
import com.buzzup.authentication.exception.UserNotFoundException;
import com.buzzup.authentication.service.UserAuthenticationServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.buzzup.authentication.model.User;
import com.buzzup.authentication.repository.UserAuthenticationRepository;

public class UserAuthenticationServiceImplTest 
{
	
	    @Mock
	   private UserAuthenticationRepository userRepository;
	    User user;

	    @InjectMocks
	    UserAuthenticationServiceImpl authenticateService;

	    List<User> userList = null;
	    Optional<User> options;

	    @Before
	    public void setUp() {
	        MockitoAnnotations.initMocks(this);

	        user = new User();
	        user.setEmailId("Johan@gmail.com");
	        user.setPassword("johnpass");
	        user.setUserRole("customer");
	        userList = new ArrayList<>();
	        userList.add(user);

	        options = Optional.of(user);

	    }

	    @Test
	    public void registerUserSuccess() throws UserAlreadyExistsException {
	        when(userRepository.save((User) any())).thenReturn(user);
	        boolean userSaved = authenticateService.saveUser(user);
	        assertEquals(true, userSaved);

	    }

	    public void registerUserFailure() throws UserAlreadyExistsException{
	        when(userRepository.save((User) any())).thenReturn(null);
	        boolean userSaved = authenticateService.saveUser(user);
	        assertEquals(false, userSaved);

	    }


	    @Test
	    public void getUserByIdAndpassword() throws UserNotFoundException {

	        //when(userRepository.findById(user.getEmailId())).thenReturn(options);
	        when(userRepository.findByEmailIdAndPassword("Johan@gmail.com","johnpass")).thenReturn(user);
	        User fetchedUser = authenticateService.findByEmailIdAndPassword(user.getEmailId(), user.getPassword());

	        assertEquals(user, fetchedUser);

	    }

}
