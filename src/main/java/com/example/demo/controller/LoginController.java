package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String registerPage(Model model) {

        model.addAttribute("user", new User());

        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user) {

        userService.registerUser(user);

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage() {

        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        jakarta.servlet.http.HttpSession session,
                        Model model) {

        User user = userService.login(email, password);

        if (user != null) {

            session.setAttribute("loggedUser", user);

            return "redirect:/";
        }

        model.addAttribute("error", "Invalid Email or Password");

        return "login";
    }
    @GetMapping("/logout")
    public String logout(jakarta.servlet.http.HttpSession session) {

        session.invalidate();

        return "redirect:/login";
    }

}