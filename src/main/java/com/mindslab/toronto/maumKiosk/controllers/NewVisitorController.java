package com.mindslab.toronto.maumKiosk.controllers;

import com.mindslab.toronto.maumKiosk.commons.Slack_Service;
import com.mindslab.toronto.maumKiosk.runner.api_engine.tts.TTS_Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Base64;
import java.util.Date;

@Slf4j
@Controller
@RequestMapping("/newVisitor")
public class NewVisitorController {
    @Autowired
    private Slack_Service slack_service;
    
    @Autowired
    private TTS_Service tts_service;
    
    @RequestMapping("/")
    public String newVisitor (@ModelAttribute("newVisitorName") String newVisitorName, Model model) {
        String voiceSrc = "data:audio/wav;base64, " + Base64.getEncoder().encodeToString(tts_service.getApiTts("Please tell us your reason for visiting us today.", "baseline_eng"));
        model.addAttribute("voiceSrc", voiceSrc);
        model.addAttribute("textBoxVisible", true);
        model.addAttribute("successMessage", false);
        model.addAttribute("name", newVisitorName);
        model.addAttribute("datetime", new Date());
        return "view/newVisitor";
    }
    
    @RequestMapping(value="/slackMessage", method= RequestMethod.POST)
    public String slackMessage (@RequestParam(value="name") String newVisitorName, @RequestParam(value="message") String message, Model model) {
        slack_service.messageBuilder(newVisitorName, message);
        System.out.println("Message sent to Slack!");
        String voiceSrc = "data:audio/wav;base64, " + Base64.getEncoder().encodeToString(tts_service.getApiTts("Thank you. Your message has been sent to our representative. Please wait a moment.", "baseline_eng"));
        model.addAttribute("voiceSrc", voiceSrc);
        model.addAttribute("textBoxVisible", false);
        model.addAttribute("successMessage", true);
        model.addAttribute("message", message);
        model.addAttribute("name", newVisitorName);
        model.addAttribute("datetime", new Date());
        return "view/newVisitor";
    }
    
    @RequestMapping("/redirectB")
    public RedirectView toWelcome () {
        return new RedirectView("/");
    }
}
