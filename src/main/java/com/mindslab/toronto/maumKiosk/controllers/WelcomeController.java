package com.mindslab.toronto.maumKiosk.controllers;

import com.mindslab.toronto.maumKiosk.runner.api_engine.tts.TTS_Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Controller
@RequestMapping("/")
public class WelcomeController {
    @Autowired
    private TTS_Service tts_service;
    
    @RequestMapping("/")
    public String footerInfo (Model model) {
        String voiceSrc = "data:audio/wav;base64, " + Base64.getEncoder().encodeToString(tts_service.getApiTts("Welcome. Press Proceed to sign-in.", "baseline_eng"));
//        System.out.println(voiceSrc);
        model.addAttribute("voiceSrc", voiceSrc);
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
