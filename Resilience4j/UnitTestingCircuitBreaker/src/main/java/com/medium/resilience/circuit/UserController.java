package com.medium.resilience.circuit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
	@Autowired
	private UserService userService;

	@GetMapping("/user/{userId}")
	public String getName(@PathVariable Integer userId) {
		return userService.getName(userId);
	}
}