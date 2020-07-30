package com.mindslab.toronto.maumKiosk.runner.api_engine.face_recog;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Controller
@RequestMapping(value = "/mvp/runner/api")
public class FaceRecog_Controller {
    
    @Autowired
    private FaceRecog_Service service;
    
    @RequestMapping(value = "/faceRecog/get", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getFace(@RequestParam(value = "dbId") String dbId) {
        return service.getFace(dbId);
    }
    
    @RequestMapping("/faceRecog/set")
    @ResponseBody
    public String setFace(@RequestParam("file") MultipartFile file, @RequestParam(value = "faceId") String faceId, @RequestParam(value = "dbId") String dbId) {
        return service.setFace(file, faceId, dbId);
    }
    
    @RequestMapping("faceRecog/delete")
    @ResponseBody
    public String deleteFace(@RequestParam(value = "faceId") String faceId, @RequestParam(value = "dbId") String dbId) {
        return service.deleteFace(faceId, dbId);
    }
    
    @RequestMapping(value = "faceRecog/recog", produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String recogFace(@RequestParam("file") MultipartFile file, @RequestParam(value = "dbId") String dbId) {
        return service.recogFace(file, dbId);
    }
}
