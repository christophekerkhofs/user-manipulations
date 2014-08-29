package com.cloudhammer.user_manipulations.dao.user;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;

import com.cloudhammer.user_manipulations.entities.exceptions.UserNotFoundException;
import com.cloudhammer.user_manipulations.entities.users.User;

/**
 * The DAO implementation for the User operations
 */
public class UserDaoImpl implements UserDao {
	private EntityManager entityManager;

	public UserDaoImpl() {
		entityManager = HsqlDbUtils.getEntityManagerFactory("MANIPULATION_PERSISTENCE").createEntityManager();
	}

	/**
	 * @see com.cloudhammer.user_manipulations.dao.user.UserDao#get(String)
	 */
	@Override
	public User get(String id) throws UserNotFoundException {
		verifyParameter(id);

		User user =  entityManager.find(User.class, id);
		if (user == null) {
			throw new UserNotFoundException(id);
		}
		return user;
	}

	private void verifyParameter(Object parameter) {
		boolean throwError = false;
		if (parameter == null) {
			throwError = true;
		} else if (parameter instanceof String && StringUtils.isEmpty((String) parameter)) {
			throwError = true;
		}

		if (throwError) {
			throw new IllegalArgumentException("The provided parameter cannot be empty");
		}
	}

	/**
	 * @see com.cloudhammer.user_manipulations.dao.user.UserDao#save(com.cloudhammer.user_manipulations.entities.users.User)
	 */
	@Override
	public User save(User user) {
		verifyParameter(user);

		User saved = (User) entityManager.merge(user);

		return saved;
	}

	/**
	 * @see com.cloudhammer.user_manipulations.dao.user.UserDao#delete(com.cloudhammer.user_manipulations.entities.users.User)
	 */
	@Override
	public void delete(User user) {
		verifyParameter(user);

		entityManager.remove(user);
	}

	/**
	 * @see com.cloudhammer.user_manipulations.dao.user.UserDao#findByName(String)
	 */
	@Override
	public List<User> findByName(String name) {
		verifyParameter(name);

		String queryString = "SELECT u FROM User u WHERE u.name = :name";
		Query query = entityManager.createQuery(queryString);
		query.setParameter("name", name);

		return query.getResultList();
	}

	/**
	 * @see com.cloudhammer.user_manipulations.dao.user.UserDao#findByEmail(String)
	 */
	@Override
	public User findByEmail(String email) {
		verifyParameter(email);

		String queryString = "SELECT u FROM User u WHERE u.email = :email";
		Query query = entityManager.createQuery(queryString);
		query.setParameter("email", email);

		return (User) query.getSingleResult();
	}

	@Override
	public List<User> list() {
		String queryString = "SELECT u FROM User u";
		Query query = entityManager.createQuery(queryString);

		return query.getResultList();
	}
}
