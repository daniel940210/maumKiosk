package com.mindslab.toronto.maumKiosk.commons;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Date;

@Slf4j
@Controller
@RequestMapping("/result")
public class ResultController {
    @RequestMapping("/")
    public String footerInfo (Model model) {
        model.addAttribute("datetime", new Date());
        return "view/result";
    }
    @RequestMapping("/redirect")
    public RedirectView redirectView () {
        return new RedirectView("/");
    }
}
