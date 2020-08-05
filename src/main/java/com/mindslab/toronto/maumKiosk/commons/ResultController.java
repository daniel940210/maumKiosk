package com.mindslab.toronto.maumKiosk.commons;

import com.mindslab.toronto.maumKiosk.runner.api_engine.faceTracking.FaceTracking_Service;
import com.mindslab.toronto.maumKiosk.test.FTtoFR_ImageSrc;
import com.mindslab.toronto.maumKiosk.test.FTtoFR_Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Date;

@Slf4j
@Controller
@RequestMapping("/result")
public class ResultController {
    @Autowired
    private FaceTracking_Service ftService;
    
    @Autowired
    private FTtoFR_Service conversion;
    
    @Autowired
    private FTtoFR_ImageSrc imageSrc;
    
    @RequestMapping("/")
    public String footerInfo (@ModelAttribute("video") String video, Model model) {
        model.addAttribute("vidPreview", video);
        System.out.println(video);
//        String image = imageSrc.ftResultToImageSrc(ftService.getApiFaceTracking(file));
//        model.addAttribute("imageSrc", "data:image/jpeg;base64, " + image);
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
