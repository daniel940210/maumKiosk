package com.mindslab.toronto.maumKiosk.commons;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

@Slf4j
@Controller
@RequestMapping("/input")
public class InputController {
    @RequestMapping("/")
    public String footerInfo (Model model) {
        model.addAttribute("datetime", new Date());
        return "view/input";
    }
    @RequestMapping("/redirectB")
    public RedirectView toWelcome () {
        return new RedirectView("/");
    }
/*    @GetMapping("/")
    public String inputForm(Model model) {
        model.addAttribute("vidInput", new VidInput());
        return "view/input";
    }*/
/*    @PostMapping("/result/")
    public String toResult (@ModelAttribute VidInput vidInput, Model model) {
        model.addAttribute("vidInput", vidInput);
        return "view/result";
    }*/
    
/*    @RequestMapping("/redirectF")
    public RedirectView toResult () {
        return new RedirectView("/result/");
    }*/
    
    @RequestMapping(value="/save", method=RequestMethod.POST)
    public String redirect (@ModelAttribute("vidBase64") final String video, RedirectAttributes redirectAttr) {
        redirectAttr.addFlashAttribute("video", video);
        /*System.out.println(video);*/
        return "redirect:/result/";
    }
}
