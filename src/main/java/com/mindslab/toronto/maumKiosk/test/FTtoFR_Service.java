package com.mindslab.toronto.maumKiosk.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.json.JSONObject;
import java.util.Base64;

@Slf4j
@Service
public class FTtoFR_Service {
    public MultipartFile ftResultToFrInput (String jsonOutput) {
        JSONObject jo = new JSONObject(jsonOutput);
        byte[] decoded = Base64.getDecoder().decode(jo.getString("frame_1"));
        return new MockMultipartFile("temp.jpg", decoded);
    }
}
