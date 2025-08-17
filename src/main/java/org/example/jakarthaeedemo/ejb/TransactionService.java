package org.example.jakarthaeedemo.ejb;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.example.jakarthaeedemo.entity.Transaction;


import java.util.List;
import java.util.Optional;

@Stateless
public class TransactionService {

    @PersistenceContext(unitName = "demoPU")
    private EntityManager em;

    public Transaction create(@Valid @NotNull Transaction t) {
        em.persist(t);
        return t;
    }

    public Optional<Transaction> findById(@NotNull Long id) {
        return Optional.ofNullable(em.find(Transaction.class, id));
    }

    public List<Transaction> findAllForCustomer(@NotNull Long customerId) {
        return em.createQuery("SELECT t FROM Transaction t WHERE t.customer.id = :cid ORDER BY t.createdAt DESC", Transaction.class)
                .setParameter("cid", customerId)
                .getResultList();
    }

    public Transaction update(@NotNull Long id, @Valid @NotNull Transaction payload) {
        Transaction existing = em.find(Transaction.class, id);
        if (existing == null) throw new jakarta.ws.rs.NotFoundException("Transaction not found");
        existing.setAmount(payload.getAmount());
        return em.merge(existing);
    }

    public void delete(@NotNull Long id) {
        Transaction existing = em.find(Transaction.class, id);
        if (existing != null) em.remove(existing);
        else throw new jakarta.ws.rs.NotFoundException("Transaction not found");
    }
}
