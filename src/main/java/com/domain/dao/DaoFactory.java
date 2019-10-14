package com.domain.dao;

public abstract class DaoFactory {

	public abstract AccountDao getAccountDao();

	public abstract void prepareData();

	public static DaoFactory newInstance() {
      return new DaoFactoryImpl();
	}
}
