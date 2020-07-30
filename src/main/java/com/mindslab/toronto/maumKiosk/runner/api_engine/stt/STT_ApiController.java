package com.mindslab.toronto.maumKiosk.runner.api_engine.stt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Controller
@RequestMapping(value="/mvp/runner/api")
public class STT_ApiController {
    
    @Autowired
    private STT_Service service;
    
    @PostMapping("/stt")
    @ResponseBody
    public String getApiStt(@RequestParam("file") MultipartFile file,
                            @RequestParam(value = "lang") String lang,
                            @RequestParam(value = "level") String level,
                            @RequestParam(value = "sampling") String sampling) {
        String result = service.getApiStt(file,lang,level,sampling);
        return result;
    }
}
