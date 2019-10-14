package com.domain.api;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;

import static com.domain.utils.FormatterUtils.formatToBigDecimal;
import static org.junit.Assert.assertEquals;

public class AccountApiTest extends BaseApiTest {

    @Test
    public void testGetAccountBalance() throws Exception {
        URI uri = builder.setPath("/account/1/balance").build();
        HttpGet request = new HttpGet(uri);

        HttpResponse response = client.execute(request);

        assertEquals(HttpServletResponse.SC_OK, response.getStatusLine().getStatusCode());
        assertEquals(
                new BigDecimal(EntityUtils.toString(response.getEntity())).setScale(4, RoundingMode.HALF_EVEN),
                formatToBigDecimal(50));
    }
}
