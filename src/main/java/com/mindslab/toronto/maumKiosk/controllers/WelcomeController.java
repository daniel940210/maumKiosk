package com.mindslab.toronto.maumKiosk.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
    
//    @RequestMapping(value="/submit", method= RequestMethod.POST)
//    public String redirect (@ModelAttribute("submitFile") final MultipartFile file, RedirectAttributes redirectAttr) {
//        redirectAttr.addFlashAttribute("uploadedVid", file);
//        return "redirect:/input/";
//    }
}
