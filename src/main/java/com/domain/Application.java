package com.domain;

import com.domain.dao.DaoFactory;
import com.domain.api.AccountApi;
import com.domain.api.ServiceExceptionMapper;
import com.domain.api.TransactionApi;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

public class Application {

	private static final String BASE_PATH = "jersey.config.server.provider.classnames";
	private static final String CLASSES = AccountApi.class.getCanonicalName() + "," +
			ServiceExceptionMapper.class.getCanonicalName() + "," +
			TransactionApi.class.getCanonicalName();

	public static void main(String[] args) throws Exception {
		DaoFactory.newInstance().prepareData();

		Server server = createNewServer();
		try {
			server.start();
			server.join();
		} finally {
			server.destroy();
		}
	}

	public static Server createNewServer() {
		Server server = new Server(8888);
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		server.setHandler(context);
		ServletHolder servletHolder = context.addServlet(ServletContainer.class, "/*");
		servletHolder.setInitParameter(BASE_PATH, CLASSES);
		return server;
	}
}
