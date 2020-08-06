package com.mindslab.toronto.maumKiosk.commons;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@Service
public class Base64ToFile_Service {
    public MultipartFile convert (String base64) throws UnsupportedEncodingException {
        String data = base64.split("base64,")[1];
        byte[] decoded = Base64.getDecoder().decode(data.getBytes(StandardCharsets.UTF_8));
        return new MockMultipartFile("video.mp4",decoded);
    }
}
