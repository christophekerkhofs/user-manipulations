package com.cloudhammer.spring.validators;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;

import com.cloudhammer.user_manipulations.entities.users.User;

/**
 * Validate the User object
 *
 * @author Christophe Kerkhofs
 */
@Component
public class UserValidator {

	/**
	 * Verify whether or not the given User object is valid
	 *
	 * @param user
	 * 		the user to verify
	 * @return true if found valid, otherwise false.
	 */
	public boolean isValid(User user) {
		boolean valid = true;
		if (StringUtils.isEmpty(user.getName())) {
			valid = false;
		}
		if (StringUtils.isEmpty(user.getEmail())) {
			valid = false;
		} else if (!EmailValidator.getInstance().isValid(user.getEmail())) {
			valid = false;
		}
		return valid;
	}
}
