package com.example.demo.service;

import com.example.demo.entity.Client;
import com.example.demo.entity.Commande;
import com.example.demo.repository.CommandeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommandeService {

    private final CommandeRepository commandeRepository;

    public CommandeService(CommandeRepository commandeRepository) {
        this.commandeRepository = commandeRepository;
    }

    public List<Commande> listForClientEmail(String email) {
        return commandeRepository.findByClientEmailOrderByCreatedAtDesc(email);
    }

    public void createForClient(Client client) {
        Commande c = new Commande();
        c.setClient(client);
        commandeRepository.save(c);
    }

    public Commande findById(Long id) {
        return commandeRepository.findById(id).orElse(null);
    }
    public Commande findForClient(Long id, String email) {
        return commandeRepository.findByIdAndClientEmail(id, email).orElse(null);
    }
}