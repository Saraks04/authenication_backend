package com.buzzup.authentication.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity

/* The class "User" will be acting as the data model for the UserAuth Table in the database. */

public class User {
	@Id
	private String emailId;
	private String password;
	private String userRole="customer";

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "User{" + "emailId='" + emailId + '\'' + ", password='" + password + '\'' +'}';
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = "customer";
	}
}