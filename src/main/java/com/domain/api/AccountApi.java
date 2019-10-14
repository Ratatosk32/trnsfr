package com.domain.api;

import com.domain.dao.DaoFactory;
import com.domain.exception.TransferException;
import com.domain.model.Account;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;

import static com.domain.utils.FormatterUtils.setHalfEvenRoundingMode;
import static java.math.BigDecimal.ZERO;

/** Account Service API. */
@Path("/account")
@Produces(MediaType.APPLICATION_JSON)
public class AccountApi {
	
    private final DaoFactory daoFactory = DaoFactory.newInstance();

    @GET
    @Path("/{accountId}/balance")
    public BigDecimal getBalance(@PathParam("accountId") long accountId) throws TransferException {
        final Account account = daoFactory.getAccountDao().getAccountById(accountId);
        if(account == null){
            throw new RuntimeException("Invalid account");
        }

        return account.getBalance();
    }

    @PUT
    @Path("/{accountId}/deposit/{amount}")
    public Account deposit(@PathParam("accountId") long accountId,@PathParam("amount") BigDecimal amount)
            throws TransferException {
        checkAmount(amount);

        daoFactory.getAccountDao().updateAccountBalance(accountId, setHalfEvenRoundingMode(amount));
        return daoFactory.getAccountDao().getAccountById(accountId);
    }

    @PUT
    @Path("/{accountId}/withdraw/{amount}")
    public Account withdraw(
            @PathParam("accountId") long accountId, @PathParam("amount") BigDecimal amount) throws TransferException {
        checkAmount(amount);

        daoFactory.getAccountDao().updateAccountBalance(accountId, setHalfEvenRoundingMode(amount.negate()));
        return daoFactory.getAccountDao().getAccountById(accountId);
    }

    private static void checkAmount(BigDecimal amount) {
        if (amount.compareTo(ZERO) <= 0) {
            throw new RuntimeException("Invalid amount");
        }
    }
}
