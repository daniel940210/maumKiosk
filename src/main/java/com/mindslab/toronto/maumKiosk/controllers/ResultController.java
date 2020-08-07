package com.mindslab.toronto.maumKiosk.controllers;

import com.mindslab.toronto.maumKiosk.commons.*;
import com.mindslab.toronto.maumKiosk.runner.api_engine.faceTracking.FaceTracking_Service;
import com.mindslab.toronto.maumKiosk.runner.api_engine.face_recog.FaceRecog_Service;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.awt.*;
import java.io.IOException;
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
    //     Output: String (in JSON response format)
    
    @Autowired
    private FTtoImageSrc_Service ftToImageSrc;
    //     Input: String (of Face Tracking response, in JSON format)
    //     Output: String (base64-encoded src of first-tracked face image)
    
    @Autowired
    private ImageSrcToFR_Service imageSrcToFR;
    //     Input: String (base64-encoded src of image from Face Tracking response)
    //     Output: Void (Image file saved locally)
    
    @RequestMapping("/")
    public String footerInfo (@ModelAttribute("uploadedVid") MultipartFile uploadedVid, @ModelAttribute("video") String videoSrc, Model model) throws IOException {
        System.out.println("Capture sent to Face Tracking API...");
        String ftResult = ftService.getApiFaceTracking(uploadedVid);
//        System.out.println(ftResult);
        System.out.println("Face Tracking successful! Creating result image...");
        String imageSrc = ftToImageSrc.convert(ftResult);
        System.out.println("Image sent to Face Recognition API...");
        imageSrcToFR.convert(imageSrc);
        System.out.println("Face Recognition successful! Creating response...");
        String frResult = frService.recogFace("maumKiosk");
//        System.out.println(frResult);
        JSONObject frJSON = new JSONObject(frResult);
        if (frJSON.getJSONObject("result").getString("id").equals("__no__match__")) {
            model.addAttribute("name", "New Visitor");
        } else {
            model.addAttribute("name", frJSON.getJSONObject("result").getString("id"));
        }
        model.addAttribute("videoSrc", videoSrc);
        model.addAttribute("imageSrc", "data:image/jpeg;base64, " + imageSrc);
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
