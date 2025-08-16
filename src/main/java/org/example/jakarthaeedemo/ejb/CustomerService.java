package org.example.jakarthaeedemo.ejb;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.example.jakarthaeedemo.entity.Customer;

import java.util.List;
import java.util.Optional;

@Stateless
public class CustomerService {

    @PersistenceContext(unitName = "demoPU")
    private EntityManager em;

    public Customer create(@Valid @NotNull Customer c) {
        em.persist(c);
        return c;
    }

    public Optional<Customer> findById(@NotNull Long id) {
        Customer c = em.find(Customer.class, id);
        return Optional.ofNullable(c);
    }

    public List<Customer> findAll(int offset, int limit) {
        return em.createQuery("SELECT c FROM Customer c ORDER BY c.id", Customer.class)
                .setFirstResult(Math.max(offset, 0))
                .setMaxResults(limit <= 0 ? 50 : limit)
                .getResultList();
    }

    public Customer update(@NotNull Long id, @Valid @NotNull Customer payload) {
        Customer existing = em.find(Customer.class, id);
        if (existing == null) {
            throw new jakarta.ws.rs.NotFoundException("Customer not found");
        }
        existing.setName(payload.getName());
        existing.setEmail(payload.getEmail());
        return em.merge(existing);
    }

    public void delete(@NotNull Long id) {
        Customer existing = em.find(Customer.class, id);
        if (existing != null) {
            em.remove(existing);
        } else {
            throw new jakarta.ws.rs.NotFoundException("Customer not found");
        }
    }
}
