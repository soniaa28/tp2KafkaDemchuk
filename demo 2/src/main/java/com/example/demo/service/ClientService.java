package com.example.demo.service;


import com.example.demo.entity.Client;
import com.example.demo.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public boolean existsByEmail(String email) {
        return clientRepository.existsById(email);
    }

    public void register(String email, String password, String nom, String prenom) {
        Client client = new Client(email, password, nom, prenom);
        clientRepository.save(client);
    }

    public Client findByEmail(String email) {
        return clientRepository.findById(email).orElse(null);
    }

    public Optional<Client> login(String email, String password) {
        return clientRepository.findById(email)
                .filter(c -> c.getPassword() != null && c.getPassword().equals(password));
    }

}
