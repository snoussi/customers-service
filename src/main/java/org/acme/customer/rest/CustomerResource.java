package org.acme.customer.rest;
import org.acme.customer.model.Customer;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

import java.net.URI;


@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Customer Service API", description = "This is a REST service to manager customer information" )

public class CustomerResource {
    // private static final Logger LOGGER = LoggerFactory.getLogger(CustomerResource.class);

    @GET
    @Operation(summary = "List customers, optionally filtered by first name")
    @APIResponse(responseCode = "200", description = "Customers found")
    public Response get(@QueryParam("firstname") String firstname) {
        if (firstname != null)
            return Response.ok(Customer.findByFirstName(firstname)).build();
        return Response.ok(Customer.listAll()).build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get a customer by its id")
    @APIResponse(responseCode = "200", description = "Customer found")
    @APIResponse(responseCode = "404", description = "Customer not found")
    public Response getById(@PathParam("id") String userId) {
        Customer customer = Customer.findByCustomerId(userId);
        if (customer != null)
            return Response.status(Status.OK).entity(Customer.findByCustomerId(userId)).build();
        return Response.status(Status.NOT_FOUND).build();
    }

    @POST
    @Transactional
    @Operation(summary = "Create a new customer")
    @APIResponse(responseCode = "201", description = "Customer created")
    @APIResponse(responseCode = "422", description = "Invalid customer payload supplied: id was invalidly set")
    @APIResponse(responseCode = "417", description = "Customer could not be created")
    public Response create(Customer customer) {
        if (customer.id != null) {
            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }
        customer.persist();
        if (customer.isPersistent()) {
            return Response.created(URI.create("/customers/" + customer.id)).build();
        }
        return Response.status(Status.EXPECTATION_FAILED).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Update a customer by its id")
    @APIResponse(responseCode = "204", description = "Customer updated")
    @APIResponse(responseCode = "404", description = "Customer not found")
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
    @Path("/{id}")  
    @Operation(summary = "Delete a customer by its id")
    @APIResponse(responseCode = "204", description = "Customer deleted")
    @APIResponse(responseCode = "404", description = "Customer not found")
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
