package org.example.jakarthaeedemo.resource;

import jakarta.ejb.EJB;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.jakarthaeedemo.dto.TransactionDTO;
import org.example.jakarthaeedemo.ejb.TransactionService;
import org.example.jakarthaeedemo.ejb.CustomerService;
import org.example.jakarthaeedemo.entity.Transaction;

import java.net.URI;
import java.util.List;

@Path("/customers/{customerId}/transactions")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TransactionResource {

    @EJB
    private TransactionService transactionService;

    @EJB
    private CustomerService customerService;

    @GET
    public List<TransactionDTO> list(@PathParam("customerId") long customerId) {
        customerService.findById(customerId).orElseThrow(() -> new NotFoundException("Customer not found"));
        return transactionService.findAllForCustomer(customerId)
                .stream()
                .map(t -> new TransactionDTO(t.getId(), t.getAmount()))
                .toList();
    }

    @GET @Path("{id}")
    public TransactionDTO get(@PathParam("customerId") long customerId, @PathParam("id") long id) {
        var tx = transactionService.findById(id).orElseThrow(() -> new NotFoundException("Transaction not found"));
        if (!tx.getCustomer().getId().equals(customerId)) throw new BadRequestException("Transaction does not belong to this customer");
        return new TransactionDTO(tx.getId(), tx.getAmount());
    }

    @POST
    public Response create(@PathParam("customerId") long customerId, @Valid TransactionDTO dto) {
        var customer = customerService.findById(customerId).orElseThrow(() -> new NotFoundException("Customer not found"));
        var tx = new Transaction();
        tx.setAmount(dto.amount());
        tx.setCustomer(customer);
        var saved = transactionService.create(tx);
        return Response.created(URI.create("/api/customers/" + customerId + "/transactions/" + saved.getId()))
                .entity(new TransactionDTO(saved.getId(), saved.getAmount()))
                .build();
    }

    @PUT @Path("{id}")
    public TransactionDTO update(@PathParam("customerId") long customerId, @PathParam("id") long id, @Valid TransactionDTO dto) {
        var tx = new Transaction();
        tx.setAmount(dto.amount());
        return new TransactionDTO(transactionService.update(id, tx).getId(), dto.amount());
    }

    @DELETE @Path("{id}")
    public Response delete(@PathParam("customerId") long customerId, @PathParam("id") long id) {
        transactionService.delete(id);
        return Response.noContent().build();
    }
}
