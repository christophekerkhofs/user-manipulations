package com.cloudhammer.user_manipulations.dao.user;

import java.util.List;

import com.cloudhammer.user_manipulations.entities.exceptions.UserNotFoundException;
import com.cloudhammer.user_manipulations.entities.users.User;

/**
 * Interface for the DAO operations for the User object
 */
public interface UserDao {

	/**
	 * Get the User object with the specified id.
	 *
	 * @param id
	 * 		the id of the user.
	 * @return the User object.
	 * @throws com.cloudhammer.user_manipulations.entities.exceptions.UserNotFoundException
	 * 		when the user could not be found.
	 * @throws java.lang.IllegalArgumentException
	 * 		when id is empty.
	 */
	User get(String id) throws UserNotFoundException;

	/**
	 * Save the User.
	 *
	 * @param user
	 * 		the User object to save.
	 * @return the saved User object.
	 * @throws java.lang.IllegalArgumentException
	 * 		when user is empty.
	 */
	User save(User user);

	/**
	 * Delete the User.
	 *
	 * @param user
	 * 		the User to delete.
	 * @throws java.lang.IllegalArgumentException
	 * 		when user is empty.
	 */
	void delete(User user);

	/**
	 * Find all users with a specific name.
	 *
	 * @param name
	 * 		the name of the user.
	 * @return the list of users.  When no user is found, an empty list is returned.
	 * @throws java.lang.IllegalArgumentException
	 * 		when name is empty.
	 */
	List<User> findByName(String name);

	/**
	 * Find a user with a specific email address.
	 *
	 * @param email
	 * 		the email address.
	 * @return the user, or <code>null</code> when no user is found.
	 * @throws java.lang.IllegalArgumentException
	 * 		when email is empty, or invalid.
	 */
	User findByEmail(String email);

	/**
	 * List all users
	 *
	 * @return the list of all available users.
	 */
	List<User> list();
}
