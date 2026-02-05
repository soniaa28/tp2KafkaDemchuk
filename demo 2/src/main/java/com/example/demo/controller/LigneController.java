package com.example.demo.controller;

import com.example.demo.entity.Client;
import com.example.demo.entity.Commande;
import com.example.demo.service.ClientService;
import com.example.demo.service.CommandeService;
import com.example.demo.service.LigneService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class LigneController {

    private final ClientService clientService;
    private final CommandeService commandeService;
    private final LigneService ligneService;

    public LigneController(ClientService clientService,
                           CommandeService commandeService,
                           LigneService ligneService) {
        this.clientService = clientService;
        this.commandeService = commandeService;
        this.ligneService = ligneService;
    }

    private Client requireClient(HttpSession session) {
        String email = (String) session.getAttribute("clientEmail");
        return (email == null) ? null : clientService.findByEmail(email);
    }

    @PostMapping("/store/commandes/{cid}/lignes/add")
    public RedirectView add(@PathVariable Long cid,
                            String libelle,
                            Integer quantite,
                            Double prixUnitaire,
                            HttpSession session) {

        Client client = requireClient(session);
        if (client == null) return new RedirectView("/store/login");

        Commande commande = commandeService.findForClient(cid, client.getEmail());
        if (commande == null) return new RedirectView("/store/commandes");

        ligneService.add(
                commande,
                libelle,
                (quantite == null ? 1 : quantite),
                (prixUnitaire == null ? 0.0 : prixUnitaire)
        );

        return new RedirectView("/store/commandes/" + cid);
    }

    @PostMapping("/store/commandes/{cid}/lignes/{lid}/delete")
    public RedirectView delete(@PathVariable Long cid,
                               @PathVariable Long lid,
                               HttpSession session) {
        Client client = requireClient(session);
        if (client == null) return new RedirectView("/store/login");

        // проверяем что эта commande принадлежит этому client
        Commande commande = commandeService.findForClient(cid, client.getEmail());
        if (commande == null) return new RedirectView("/store/commandes");

        ligneService.delete(cid, lid);
        return new RedirectView("/store/commandes/" + cid);
    }
}