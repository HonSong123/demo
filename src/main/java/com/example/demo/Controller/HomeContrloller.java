package com.example.demo.Controller;

import java.net.http.HttpResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class HomeContrloller {
    @RequestMapping("/")
    public String index(HttpServletResponse response) {
        response.setHeader("Content-Security-Policy", "script-src 'self' https://ajax.googleapis.com https://www.gstatic.com https://accounts.google.com https://apis.google.com https://cdnjs.cloudflare.com 'unsafe-inline' 'unsafe-eval' blob: data:");
        return "redirect:/base.html";
    }

    @RequestMapping("/login")
    public String login() {
        return "redirect:/login2.html";
    }

    @RequestMapping("/profit")
    public String profit() {
        return "redirect:/profit.html";
    }

    @RequestMapping("/forgotpass")
    public String forgotpass() {
        return "redirect:/forgotpass.html";
    }


    @RequestMapping("/checkout")
    public String checkout() {
        return "redirect:/check-out.html";
    }
}
