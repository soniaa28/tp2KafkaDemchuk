package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_email", referencedColumnName = "email")
    private Client client;

    public Commande() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public Client getClient() { return client; }

    public void setClient(Client client) { this.client = client; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}