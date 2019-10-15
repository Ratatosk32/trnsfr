package com.domain.dao;

/** Provides basic infrastructure methods for creation factory impl instance and data population. */
public abstract class DaoFactory {
	/** Returns Account dao instance. */
	public abstract AccountDao getAccountDao();
	/** Fulfils a test data. */
	public abstract void prepareData();
	/** Create a dao instance. */
	public static DaoFactory newInstance() {
      return new DaoFactoryImpl();
	}
}
