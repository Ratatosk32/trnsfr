package com.domain.api;

import com.domain.dao.DaoFactory;
import com.domain.exception.TransferException;
import com.domain.model.Transaction;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.OK;

@Path("/transaction")
@Produces(MediaType.APPLICATION_JSON)
public class TransactionApi {

	private final DaoFactory daoFactory = DaoFactory.newInstance();
	
	/** Transfers between accounts. */
	@POST
	public Response transfer(Transaction transaction) throws TransferException {
		int updateCount = daoFactory.getAccountDao().transferAccountBalance(transaction);
		if (updateCount == 2) {
			return Response.status(OK).build();
		} else {
			throw new TransferException("Transfer failed");
		}
	}

}
