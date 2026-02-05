package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Client {
    @Id
    private String email;
    private String password;
    private String prenom;
    private String nom;

    public Client(){
    }

    public Client(String email, String password, String prenom, String nom){
        this.email=email;
        this.password=password;
        this.prenom=prenom;
        this.nom=nom;

    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
}
