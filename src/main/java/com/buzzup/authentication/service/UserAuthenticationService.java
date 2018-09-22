package com.buzzup.authentication.service;

import com.buzzup.authentication.exception.UserAlreadyExistsException;
import com.buzzup.authentication.exception.UserNotFoundException;
import com.buzzup.authentication.model.User;

public interface UserAuthenticationService {

	public User findByEmailIdAndPassword(String emailId, String password) throws UserNotFoundException;

	boolean saveUser(User user) throws UserAlreadyExistsException;

}