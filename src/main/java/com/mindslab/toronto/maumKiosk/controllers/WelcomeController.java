package com.mindslab.toronto.maumKiosk.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Date;

@Slf4j
@Controller
@RequestMapping("/")
public class WelcomeController {
    @RequestMapping("/")
    public String footerInfo (Model model) {
        model.addAttribute("datetime", new Date());
        return "view/welcome";
    }
    @RequestMapping("/redirect")
    public RedirectView redirectView () {
        return new RedirectView("/input/");
    }
}
