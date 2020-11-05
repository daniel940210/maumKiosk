package com.mindslab.toronto.maumKiosk.controllers;

import com.mindslab.toronto.maumKiosk.runner.api_engine.tts.TTS_Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;

@Slf4j
@Controller
@RequestMapping("/input")
public class InputController {
    @Autowired
    private TTS_Service tts_service;
    
    @RequestMapping("/")
    public String footerInfo (@ModelAttribute("uploadedVid") final MultipartFile uploadedVid, Model model) {
        String voiceSrc = "data:audio/wav;base64, " + Base64.getEncoder().encodeToString(tts_service.getApiTts("Press record your face to capture your face for face recognition." +
                                                                                                                   "When complete, press submit to start the sign-in process.", "baseline_eng"));
        model.addAttribute("voiceSrc", voiceSrc);
        model.addAttribute("uploadedVid", uploadedVid);
        model.addAttribute("datetime", new Date());
        return "view/input";
    }
    @RequestMapping("/redirectB")
    public RedirectView toWelcome () {
        return new RedirectView("/");
    }
    
    @RequestMapping(value="/save", method=RequestMethod.POST)
    public String redirect (@ModelAttribute("vidBase64") final String videoSrc, @ModelAttribute("ftResult") final String ftResult, RedirectAttributes redirectAttr) {
        redirectAttr.addFlashAttribute("videoSrc", videoSrc);
        return "redirect:/result/";
    }
}
