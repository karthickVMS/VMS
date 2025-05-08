package com.akt.vms.specification;
import org.springframework.data.jpa.domain.Specification;

import com.akt.vms.entity.Customer;



public class CustomerSpecification {

    public static Specification<Customer> hasName(String name) {
        return (root, query, builder) ->
            name == null ? null : builder.like(root.get("customerName"), "%" + name + "%");
    }

    public static Specification<Customer> hasEmail(String email) {
        return (root, query, builder) ->
            email == null ? null : builder.equal(root.get("customerEmail"), email);
    }

    public static Specification<Customer> isFromCity(String city) {
        return (root, query, builder) ->
            city == null ? null : builder.equal(root.get("city"), city);
    }
}
