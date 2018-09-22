package com.buzzup.authentication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.buzzup.authentication.model.User;

@Repository
public interface UserAuthenticationRepository extends JpaRepository<User , String>
{
	
	public User findByEmailIdAndPassword(String emailId, String password);
	
}
