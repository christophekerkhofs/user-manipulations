package com.cloudhammer.spring.controllers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cloudhammer.spring.validators.UserValidator;
import com.cloudhammer.user_manipulations.dao.user.UserDao;
import com.cloudhammer.user_manipulations.entities.exceptions.UserNotFoundException;
import com.cloudhammer.user_manipulations.entities.users.User;

/**
 * Controller for all REST actions
 *
 * @author Christophe Kerkhofs
 */
@Controller
@RequestMapping(value = "/users")
public class UserManipulationsController {
	@Autowired
	public UserDao userDao;
	@Autowired
	public UserValidator validator;

	/**
	 * Save a new user
	 *
	 * @param user
	 * 		the user to save
	 * @return the saved user
	 */
	@RequestMapping(value = "/user/{userId}", method = RequestMethod.POST)
	@ResponseBody
	public User post(@RequestBody User user) {
		Validate.isTrue(validator.isValid(user), "The given user is not valid");
		user.setPassword(hashPassword(user.getPassword()));
		return userDao.save(user);
	}

	private String hashPassword(String unhashed) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(unhashed.getBytes());
			byte[] bytes = md.digest();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			//Should not occur
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
	@ResponseBody
	public User get(@PathVariable(value = "userId") String userId) {
		try {
			return userDao.get(userId);
		} catch (UserNotFoundException e) {
			throw new IllegalArgumentException("There is no user for id " + userId);
		}
	}

	/**
	 * Save an existing user
	 *
	 * @param user
	 * 		the user to save
	 * @return the saved user
	 */
	@RequestMapping(value = "/user/{userId}", method = RequestMethod.PUT)
	@ResponseBody
	public User update(@PathVariable(value = "userId") String userId, @RequestBody User user) {
		Validate.isTrue(userId.equals(user.getId()), "The given user has an invalid id: " + user.getId());
		Validate.isTrue(validator.isValid(user), "The given user is not valid");

		user.setPassword(hashPassword(user.getPassword()));
		return userDao.save(user);
	}

	/**
	 * Delete a specific user
	 *
	 * @param userId
	 * 		the id of the user.
	 */
	@RequestMapping(value = "/user/{userId}", method = RequestMethod.DELETE)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable(value = "userId") String userId) {
		try {
			User user = userDao.get(userId);
			userDao.delete(user);
		} catch (UserNotFoundException e) {
			throw new IllegalArgumentException("There is no user for id " + userId);
		}
	}

	@RequestMapping(value = "/findByName/name={name}", method = RequestMethod.GET)
	@ResponseBody
	public List<User> findByName(@PathVariable(value = "name") String name) {
		return userDao.findByName(name);
	}

	@RequestMapping(value = "/findByEmail/email={email}", method = RequestMethod.GET)
	@ResponseBody
	public User findByEmail(@PathVariable(value = "email") String email) {
		return userDao.findByEmail(email);
	}

	@RequestMapping(value = "/pwdcheck/email={email}&pwd={password}", method = RequestMethod.GET)
	@ResponseBody
	public User passwordCheck(@PathVariable(value = "email") String email, @PathVariable(value = "password") String password) {
		User user = userDao.findByEmail(email);
		Validate.isTrue(user.getPassword().equals(password), "The given email address does not match with the password");
		return user;
	}
}
