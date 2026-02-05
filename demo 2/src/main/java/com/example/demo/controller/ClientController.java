package com.example.demo.controller;

import com.example.demo.entity.Client;
import com.example.demo.service.ClientService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

@Controller
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/")
    public RedirectView root() {
        return new RedirectView("/store/home");
    }

    @GetMapping("/store/home")

    public ModelAndView home(HttpSession session) {
        String email = (String) session.getAttribute("clientEmail");
        Client client = (email == null) ? null : clientService.findByEmail(email);

        ModelAndView mv = new ModelAndView("home");
        mv.addObject("client", client); // можно null, это нормально
        return mv;
    }

    @GetMapping("/store/register")
    public ModelAndView registerPage(@RequestParam(required = false) String error) {
        return new ModelAndView("register", Map.of("error", error == null ? "" : error));
    }

    @PostMapping("/store/register")
    public RedirectView register(@RequestParam String email,
                                 @RequestParam String password,
                                 @RequestParam String nom,
                                 @RequestParam String prenom) {
        if (clientService.existsByEmail(email)) {
            return new RedirectView("/store/register?error=email_exists");
        }
        clientService.register(email, password, nom, prenom);
        return new RedirectView("/store/login");
    }

    @GetMapping("/store/login")
    public ModelAndView loginPage(@RequestParam(required = false) String error) {
        return new ModelAndView("login", Map.of("error", error == null ? "" : error));
    }

    @PostMapping("/store/login")
    public RedirectView login(@RequestParam String email,
                              @RequestParam String password,
                              HttpSession session) {
        return clientService.login(email, password)
                .map(c -> {
                    session.setAttribute("clientEmail", c.getEmail());
                    return new RedirectView("/store/home");
                })
                .orElseGet(() -> new RedirectView("/store/login?error=bad_credentials"));
    }

    @PostMapping("/store/logout")
    public RedirectView logout(HttpSession session) {
        session.invalidate();
        return new RedirectView("/store/home");
    }
}