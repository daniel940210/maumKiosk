package com.mindslab.toronto.maumKiosk.runner.api_engine.tts;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequestMapping(value="/mvp/runner/api")
public class TTS_Controller {
    
    @Autowired
    private TTS_Service service;
    
    @RequestMapping("/tts")
    @ResponseBody
    public byte[] getApiTts(@RequestParam(value = "text") String text,
                                            @RequestParam(value = "voicename") String voiceName) {
        return service.getApiTts(text, voiceName);
    }
}
