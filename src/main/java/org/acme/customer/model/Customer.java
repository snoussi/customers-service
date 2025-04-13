package org.acme.customer.model;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

import java.util.List;

@Entity
public class Customer extends PanacheEntity {

    public String firstName;
    public String lastName;
    public String email;
    public String phone;

    public static List<Customer> findByFirstName(String firstName) {
        return find("upper(firstName)", firstName.toUpperCase()).list();
    }

    public static List<Customer> findByLastNames(String lastName) {
        return find("upper(lastName)", lastName.toUpperCase()).list();
    }

    public static Customer findByCustomerId(String id) {
        return find("id", id).firstResult();
    }

    public static Customer findByEmail(String email) {
        return find("email", email).firstResult();
    }
}
