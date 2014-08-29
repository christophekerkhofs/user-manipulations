package com.cloudhammer.user_manipulations.dao.user;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.cloudhammer.user_manipulations.entities.exceptions.UserNotFoundException;
import com.cloudhammer.user_manipulations.entities.users.User;

public class UserDaoImplTest extends AbstractDaoTestCase {
	private UserDaoImpl userDao;

	@Before
	public void setUp() throws Exception {
		super.setUp();
		userDao = new UserDaoImpl();
	}

	@Test
	public void testSuccessGet() throws Exception {
		User actual = userDao.get("u1");
		User expected = createUser1();

		Assert.assertEquals(expected, actual);
	}

	private User createUser1() {
		User entity = new User();
		entity.setId("u1");
		entity.setName("John Doe");
		entity.setEmail("john.doe@local.com");
		entity.setPassword("password1");
		return entity;
	}

	private User createUser2() {
		User entity = new User();
		entity.setId("u2");
		entity.setName("John Doe");
		entity.setEmail("john.doe2@local.com");
		entity.setPassword("password2");
		return entity;
	}

	@Test(expected = UserNotFoundException.class)
	public void testFailGetNotFound() throws Exception {
		userDao.get("u3");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFailGetEmptyParameter() throws Exception {
		userDao.get("");
	}

	@Test
	public void testSuccessSaveNew() {
		User actual = userDao.save(createUser3());
		User expected = createUser3();
		expected.setId(actual.getId());

		Assert.assertEquals(expected, actual);
	}

	private User createUser3() {
		User entity = new User();
		entity.setId("u4");
		entity.setName("Jane Doe");
		entity.setEmail("jane.doe@local.com");
		entity.setPassword("herpassword");
		return entity;
	}

	@Test
	public void testSuccessSaveExisting() throws Exception {
		User expected = createUser2();
		expected.setPassword("hispassword");
		userDao.save(expected);
		User actual = userDao.get(createUser2().getId());

		Assert.assertEquals(expected, actual);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFailSaveEmptyParameter() throws Exception {
		userDao.save(null);
	}

	@Test(expected = UserNotFoundException.class)
	public void testSuccessDelete() throws Exception {
		User user = userDao.get("u4");
		Assert.assertNotNull(user);

		userDao.delete(user);

		userDao.get("u4");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFailDeleteEmptyParameter() throws Exception {
		userDao.delete(null);
	}

	@Test
	public void testSuccessList() {
		List<User> actual = userDao.list();
		Assert.assertEquals(3, actual.size());
	}
}
