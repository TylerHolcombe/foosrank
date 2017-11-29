package com.foosrank.security.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
	@Autowired
	private UserService userService;

	@RequestMapping(value = "/api/users", method = RequestMethod.GET)
	public List<UserDto> findAll() {
		return userService.getUsers();
	}

}
