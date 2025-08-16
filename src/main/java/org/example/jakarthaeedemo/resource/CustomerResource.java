package org.example.jakarthaeedemo.resource;

import jakarta.ejb.EJB;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.jakarthaeedemo.dto.CustomerDTO;
import org.example.jakarthaeedemo.ejb.CustomerService;
import org.example.jakarthaeedemo.entity.Customer;

import java.net.URI;
import java.util.List;

@Path("/customers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CustomerResource {

    @EJB
    private CustomerService service;

    @GET
    public List<CustomerDTO> list(@QueryParam("offset") @DefaultValue("0") int offset,
                                  @QueryParam("limit")  @DefaultValue("50") int limit) {
        return service.findAll(offset, limit).stream()
                .map(c -> new CustomerDTO(c.getId(), c.getName(), c.getEmail()))
                .toList();
    }

    @GET @Path("{id}")
    public CustomerDTO get(@PathParam("id") long id) {
        var customer = service.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found"));
        return new CustomerDTO(customer.getId(), customer.getName(), customer.getEmail());
    }

    @POST
    public Response create(@Valid CustomerDTO dto) {
        var c = new Customer();
        c.setName(dto.name());
        c.setEmail(dto.email());
        var saved = service.create(c);
        return Response.created(URI.create("/api/customers/" + saved.getId()))
                .entity(new CustomerDTO(saved.getId(), saved.getName(), saved.getEmail()))
                .build();
    }

    @PUT @Path("{id}")
    public CustomerDTO update(@PathParam("id") long id, @Valid CustomerDTO dto) {
        var payload = new Customer();
        payload.setName(dto.name());
        payload.setEmail(dto.email());
        var updated = service.update(id, payload);
        return new CustomerDTO(updated.getId(), updated.getName(), updated.getEmail());
    }

    @DELETE @Path("{id}")
    public Response delete(@PathParam("id") long id) {
        service.delete(id);
        return Response.noContent().build();
    }
}
