package com.mindslab.toronto.maumKiosk.test;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

@Slf4j
@Service
public class FTtoFR_ImageSrc {
    public String ftResultToImageSrc (String jsonOutput) {
        JSONObject jo = new JSONObject(jsonOutput);
        return jo.getString("frame_1");
    }
}
