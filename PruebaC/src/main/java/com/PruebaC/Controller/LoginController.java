package com.PruebaC.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.PruebaC.Model.UserModel;
import com.PruebaC.Security.AESUtil;
import com.PruebaC.Service.UserService;



@RestController
@RequestMapping(path = "/login")
@CrossOrigin

public class LoginController {
	@Autowired
	private AESUtil aesUtil;
	

	@Autowired
    private UserService service;

	@PostMapping
	public ResponseEntity<?> login(@RequestBody UserModel credentials) {
	    UserModel user = service.buscarPorTaxId(credentials.getTax_id());
	    if (user == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
	    }
	    if (!aesUtil.matches(credentials.getPassword(), user.getPassword())) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Icorrect password");
	    }
	    return ResponseEntity.ok("Success!");
	}

	}
	

	
	

