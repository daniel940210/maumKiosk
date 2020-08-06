package com.mindslab.toronto.maumKiosk.controllers;

import com.mindslab.toronto.maumKiosk.commons.Base64ToFile_Service;
import com.mindslab.toronto.maumKiosk.runner.api_engine.faceTracking.FaceTracking_Service;
import com.mindslab.toronto.maumKiosk.commons.FTtoImageSrc_Service;
import com.mindslab.toronto.maumKiosk.commons.FTtoFR_Service;
import com.mindslab.toronto.maumKiosk.runner.api_engine.face_recog.FaceRecog_Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@Slf4j
@Controller
@RequestMapping("/result")
public class ResultController {
    @Autowired
    private Base64ToFile_Service base64ToFile;
    //     Input: String (base64-encoded src of video or image)
    //     Output: MultipartFile (of captured video, for Face Tracking input)
    
    @Autowired
    private FaceTracking_Service ftService;
    //     Input: MultipartFile (video)
    //     Output: String (in JSON response format)
    
    @Autowired
    private FaceRecog_Service frService;
    //     Input: MultipartFile (image)
    //     Output: String (in
    
    @Autowired
    private FTtoImageSrc_Service ftToImageSrc;
    //     Input: String (of Face Tracking response, in JSON format)
    //     Output: String (base64-encoded src of first-tracked face image)
    
    @RequestMapping("/")
    public String footerInfo (@ModelAttribute("video") String videoSrc, @ModelAttribute("ftResult") String ftResultJSON, Model model) throws UnsupportedEncodingException {
//        String[] stringBin = videoSrc.split(";;");
//        videoSrc = stringBin[0] + ";" + stringBin[1];
        System.out.println(ftResultJSON);
//        System.out.println("Capture sent to Face Tracking API...");
        model.addAttribute("videoSrc", videoSrc);
        String imageSrc = "data:image/jpeg;base64, " + ftToImageSrc.convert(ftResultJSON);
        System.out.println(imageSrc);
        model.addAttribute("imageSrc", imageSrc);
//        String ftResult = ftService.getApiFaceTracking(base64ToFile.convert(videoSrc));
//        String imageSrc = fTtoImageSrc.convert(ftResult);
//        System.out.println(imageSrc);
        
//        model.addAttribute("imageSrc", "data:image/jpeg;base64, " + imageSrc);
        model.addAttribute("datetime", new Date());
        return "view/result";
    }
    @RequestMapping("/redirectB")
    public RedirectView toInput () {
        return new RedirectView("/input/");
    }
    @RequestMapping("/redirectF")
    public RedirectView toWelcome () {
        return new RedirectView("/");
    }
}
