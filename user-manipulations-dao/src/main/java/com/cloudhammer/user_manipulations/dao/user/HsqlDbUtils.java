package com.cloudhammer.user_manipulations.dao.user;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Utility class to initialize the EntityManagerFactory
 *
 * @author Christophe Kerkhofs
 */
public final class HsqlDbUtils {
	private static EntityManagerFactory entityManagerFactory;

	public static synchronized EntityManagerFactory getEntityManagerFactory(String persistenceUnitName) {
		if (entityManagerFactory == null) {
			entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
		}
		return entityManagerFactory;
	}

	public static synchronized void closeFactory() {
		entityManagerFactory.close();
	}
}
