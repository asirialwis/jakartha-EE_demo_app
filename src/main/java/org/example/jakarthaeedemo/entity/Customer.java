package org.example.jakarthaeedemo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers",
        uniqueConstraints = @UniqueConstraint(name = "uk_customer_email", columnNames = "email"))
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 120)
    private String name;

    @NotBlank @Email
    @Column(nullable = false, length = 180)
    private String email;

    @Column(name = "created_at", nullable = false, columnDefinition = "timestamp")
    private OffsetDateTime createdAt;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        createdAt = OffsetDateTime.now();
    }

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    public List<Transaction> getTransactions() { return transactions; }
    public void setTransactions(List<Transaction> transactions) { this.transactions = transactions; }

    public void addTransaction(Transaction t) {
        transactions.add(t);
        t.setCustomer(this);
    }

    public void removeTransaction(Transaction t) {
        transactions.remove(t);
        t.setCustomer(null);
    }
}
