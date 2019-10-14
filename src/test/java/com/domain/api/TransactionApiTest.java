package com.domain.api;

import com.domain.model.Account;
import com.domain.model.Transaction;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.math.BigDecimal;
import java.net.URI;

import static com.domain.utils.FormatterUtils.formatToBigDecimal;
import static org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;

public class TransactionApiTest extends BaseApiTest {
    @Test
    public void testDeposit() throws Exception {
        HttpPut request = new HttpPut(builder.setPath("/account/1/deposit/100").build());
        request.setHeader("Content-type", "application/json");

        HttpResponse response = client.execute(request);

        assertEquals(SC_OK, response.getStatusLine().getStatusCode());
        String jsonString = EntityUtils.toString(response.getEntity());
        Account afterDeposit = mapper.readValue(jsonString, Account.class);
        assertEquals(afterDeposit.getBalance(), formatToBigDecimal(150));
    }

    @Test
    public void testWithDrawSufficientFund() throws Exception {
        HttpPut request = new HttpPut(builder.setPath("/account/2/withdraw/100").build());
        request.setHeader("Content-type", "application/json");

        HttpResponse response = client.execute(request);

        assertEquals(SC_OK, response.getStatusLine().getStatusCode());
        assertEquals(
                mapper.readValue(EntityUtils.toString(response.getEntity()), Account.class).getBalance(),
                formatToBigDecimal(50));
    }

    @Test
    public void testTransactionNotEnoughFund() throws Exception {
        URI uri = builder.setPath("/transaction").build();
        BigDecimal amount = formatToBigDecimal(100000);
        Transaction transaction = new Transaction(amount, 3L, 4L);
        String jsonInString = mapper.writeValueAsString(transaction);
        StringEntity entity = new StringEntity(jsonInString);
        HttpPost request = new HttpPost(uri);
        request.setHeader("Content-type", "application/json");
        request.setEntity(entity);

        HttpResponse response = client.execute(request);

        assertEquals(SC_INTERNAL_SERVER_ERROR,  response.getStatusLine().getStatusCode());
    }
}
