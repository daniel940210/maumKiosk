package com.mindslab.toronto.maumKiosk.runner.api_engine.faceTracking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Controller
@RequestMapping(value="/mvp/runner/api")
public class FaceTracking_Controller {
    
    @Autowired
    private FaceTracking_Service service;
    
    @RequestMapping(value = "/faceTracking")
    @ResponseBody
    public String getApiFaceTracking(@RequestParam MultipartFile file) {
        return service.getApiFaceTracking();
    }
}
