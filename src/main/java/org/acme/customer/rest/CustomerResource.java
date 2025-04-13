package org.acme.customer.rest;
import org.acme.customer.model.Customer;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

import java.net.URI;


@Path("/customer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {
    // private static final Logger LOGGER = LoggerFactory.getLogger(CustomerResource.class);

    @GET
    public Response get(@QueryParam("firstname") String firstname) {
        if (firstname != null)
            return Response.ok(Customer.findByFirstName(firstname)).build();
        return Response.ok(Customer.listAll()).build();
    }

    @GET
    @Path("/id/{id}")
    public Response getById(@PathParam("id") String userId) {
        Customer customer = Customer.findByCustomerId(userId);
        if (customer != null)
            return Response.status(Status.OK).entity(Customer.findByCustomerId(userId)).build();
        return Response.status(Status.NOT_FOUND).build();
    }

    @POST
    @Transactional
    public Response create(Customer customer) {
        customer.id = null;
        customer.persist();
        if (customer.isPersistent()) {
            return Response.created(URI.create("/customer/id/" + customer.id)).build();
        }

        return Response.status(Status.BAD_REQUEST).build();
    }

    @PUT
    @Path("/id/{id}")
    @Transactional
    public Response updateById(@PathParam("id") String id, Customer newCustomer) {
        Customer customer = Customer.findById(id);
        if (customer != null){
            customer.firstName = newCustomer.firstName;
            customer.lastName = newCustomer.lastName;
            customer.email = newCustomer.email;
            customer.phone = newCustomer.phone;
            return Response.status(Status.NO_CONTENT).entity(customer).build();
        }
        return Response.status(Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/id/{id}")
    @Transactional
    public Response deleteById(@PathParam("id") String id){
        Customer customer = Customer.findById(id);
        if (customer != null) {
            customer.delete();
            return Response.status(Status.NO_CONTENT).build();
        }
        return Response.status(Status.NOT_FOUND).build();
    }
}
