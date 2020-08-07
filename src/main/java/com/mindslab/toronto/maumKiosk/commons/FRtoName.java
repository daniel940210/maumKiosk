package com.mindslab.toronto.maumKiosk.commons;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@Service
public class FRtoName {
    public String convert (String jsonOutput) {
        JSONObject jo = new JSONObject(jsonOutput);
        return jo.getJSONObject("result").getString("id");
    }
}
