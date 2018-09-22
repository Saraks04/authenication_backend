package com.buzzup.authentication.test.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.buzzup.authentication.controller.UserAuthenticationController;
import com.buzzup.authentication.exception.UserAlreadyExistsException;
import com.buzzup.authentication.model.User;
import com.buzzup.authentication.service.UserAuthenticationService;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.ArgumentMatchers.any;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;

import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.test.web.servlet.MockMvc;


//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.any;
//import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(UserAuthenticationController.class)

public class UserAuthenticationControllerTest 
{
	    @Autowired
	    private MockMvc mockMvc;
	    
	    @MockBean
	    private User user;
	    
	    @MockBean
	    private UserAuthenticationService authenticateService;
	    
	    @InjectMocks
	    private UserAuthenticationController authenticateController;

	    @Before
	    public void setUp() {
	        MockitoAnnotations.initMocks(this);
	        mockMvc = MockMvcBuilders.standaloneSetup(authenticateController).build();
	        user = new User();
	        user.setEmailId("Johan@gmail.com");
	        user.setPassword("1234");
	        user.setUserRole("customer");
	    }

	    @Test
	    public void registerUserSuccess() throws Exception {

	        when(authenticateService.saveUser(user)).thenReturn(true);
	        mockMvc.perform(post("/api/v1/auth/register")
	                .contentType(MediaType.APPLICATION_JSON).content(jsonToString(user)))
	                .andExpect(status().isCreated()).andDo(MockMvcResultHandlers.print());

	    }

	    @Test
	    public void registerUserFailure() throws Exception {

	        when(authenticateService.saveUser(any())).thenThrow(UserAlreadyExistsException.class);
	        mockMvc.perform(post("/api/v1/auth/register")
	                .contentType(MediaType.APPLICATION_JSON).content(jsonToString(user)))
	                .andExpect(status().isConflict()).andDo(MockMvcResultHandlers.print());

	    }


	    @Test
	    public void testLoginUser() throws Exception {
	        String userId = "Johan@gmail.com";
	        String password = "1234";
	        when(authenticateService.saveUser(user)).thenReturn(true);
	        when(authenticateService.findByEmailIdAndPassword(userId, password)).thenReturn(user);
	        mockMvc.perform(post("/api/login").contentType(MediaType.APPLICATION_JSON).content(jsonToString(user)))
	                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
	    }
	    // Parsing String format data into JSON format
	    private static String jsonToString(final Object obj) throws JsonProcessingException {
	        String result;
	        try {
	            final ObjectMapper mapper = new ObjectMapper();
	            final String jsonContent = mapper.writeValueAsString(obj);
	            result = jsonContent;
	        } catch (JsonProcessingException e) {
	            result = "Json processing error";
	        }
	        return result;
	    }
	 }
	 



