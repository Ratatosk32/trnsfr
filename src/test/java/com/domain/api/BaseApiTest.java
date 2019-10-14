package com.domain.api;

import com.domain.Application;
import com.domain.dao.DaoFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.HttpClient;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;


public abstract class BaseApiTest {

    private static final int MAX_REQ = 100;

    private static Server server = null;
    private static PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
    private static DaoFactory daoFactory = DaoFactory.newInstance();

    static HttpClient client ;

    ObjectMapper mapper = new ObjectMapper();
    URIBuilder builder = new URIBuilder().setScheme("http").setHost("localhost:8888");

    @BeforeClass
    public static void setup() throws Exception {
        daoFactory.prepareData();

        if (server == null) {
            server = Application.createNewServer();
            server.start();
        }

        connManager.setDefaultMaxPerRoute(MAX_REQ);
        connManager.setMaxTotal(MAX_REQ);

        client = HttpClients.custom().setConnectionManager(connManager).setConnectionManagerShared(true).build();
    }

    @AfterClass
    public static void closeClient() {
        HttpClientUtils.closeQuietly(client);
    }
}
