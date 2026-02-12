package com.example.demo.controller;
import com.example.demo.entity.Client;
import com.example.demo.entity.Commande;
import com.example.demo.service.ClientService;
import com.example.demo.service.CommandeService;
import com.example.demo.service.LigneService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import com.example.demo.kafka.KafkaProducer;
import java.util.List;
import com.example.demo.kafka.OrderSubmittedEvent;
@Controller
public class CommandeController {

    private final ClientService clientService;
    private final CommandeService commandeService;
    private final LigneService ligneService;
    private final KafkaProducer kafkaProducer;
    public CommandeController(ClientService clientService,
                              CommandeService commandeService,
                              LigneService ligneService ,
                              KafkaProducer kafkaProducer) {
        this.clientService = clientService;
        this.commandeService = commandeService;
        this.ligneService = ligneService;
        this.kafkaProducer = kafkaProducer;
    }

    private Client requireClient(HttpSession session) {
        String email = (String) session.getAttribute("clientEmail");
        if (email == null) return null;
        return clientService.findByEmail(email);
    }

    // список команд (для залогиненного клиента)
    @GetMapping("/store/commandes")
    public ModelAndView list(HttpSession session) {
        Client client = requireClient(session);
        if (client == null) return new ModelAndView("redirect:/store/login");

        List<Commande> commandes = commandeService.listForClientEmail(client.getEmail());

        ModelAndView mv = new ModelAndView("commandes");
        mv.addObject("client", client);
        mv.addObject("commandes", commandes);
        return mv;
    }

    // создать новую команду
    @PostMapping("/store/commandes/create")
    public RedirectView create(HttpSession session) {
        Client client = requireClient(session);
        if (client == null) return new RedirectView("/store/login");

        commandeService.createForClient(client);
        return new RedirectView("/store/commandes");
    }
    @GetMapping("/store/commandes/{id}")
    public ModelAndView show(@PathVariable Long id, HttpSession session) {
        Client client = requireClient(session);
        if (client == null) return new ModelAndView("redirect:/store/login");

        Commande commande = commandeService.findForClient(id, client.getEmail());
        if (commande == null) return new ModelAndView("redirect:/store/commandes");

        ModelAndView mv = new ModelAndView("commande"); // => templates/commande.html
        mv.addObject("client", client);
        mv.addObject("commande", commande);
        mv.addObject("lignes", ligneService.listByCommande(id));
        return mv;
    }

    @GetMapping("/store/commandes/{id}/print")
    public ModelAndView print(@PathVariable Long id, HttpSession session) {
        Client client = requireClient(session);
        if (client == null) return new ModelAndView("redirect:/store/login");

        Commande commande = commandeService.findForClient(id, client.getEmail());
        if (commande == null) return new ModelAndView("redirect:/store/commandes");

        var lignes = ligneService.listByCommande(id);

        double total = lignes.stream()
                .mapToDouble(l -> l.getQuantite() * l.getPrixUnitaire())
                .sum();

        ModelAndView mv = new ModelAndView("print");
        mv.addObject("client", client);
        mv.addObject("commande", commande);
        mv.addObject("lignes", lignes);
        mv.addObject("total", total);
        return mv;
    }
    @PostMapping("/store/commandes/{id}/submit")
    public RedirectView submit(@PathVariable Long id, HttpSession session) {
        Client client = requireClient(session);
        if (client == null) return new RedirectView("/store/login");

        Commande commande = commandeService.findForClient(id, client.getEmail());
        if (commande == null) return new RedirectView("/store/commandes");

        var lignes = ligneService.listByCommande(id);

        OrderSubmittedEvent event = new OrderSubmittedEvent();
        event.commandeId = id;
        event.clientEmail = client.getEmail();
        event.lignes = lignes.stream()
                .map(l -> new OrderSubmittedEvent.LigneDto(l.getLibelle(), l.getQuantite()))
                .toList();

        kafkaProducer.sendOrderSubmitted(event);

        return new RedirectView("/store/commandes/" + id);
    }
}