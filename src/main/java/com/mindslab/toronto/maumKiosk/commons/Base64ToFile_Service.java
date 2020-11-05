package com.mindslab.toronto.maumKiosk.commons;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@Service
public class Base64ToFile_Service {
    public void convert (String base64) {
        String data = base64.split("base64,")[1];
        byte[] decoded = Base64.getDecoder().decode(data.getBytes(StandardCharsets.UTF_8));
        try {
            FileOutputStream out = new FileOutputStream("video/captured.webm");
            out.write(decoded);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        String command = "cmd /c start cmd.exe /K \"dir && cd C:/Users/Daniel Whang/Desktop/maumKiosk/src/main/resources/templates/video/ && ffmpeg -i captured.webm -c:v libx264 captured.mp4 && exit";
        try {
            Runtime.getRuntime().exec(command);
//            command = "ffmpeg -i captured.webm -c:v libx264 captured.mp4";
//            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
//        String inputFilename = "C:/Users/Daniel Whang/Desktop/maumKiosk/src/main/resources/templates/video/captured.webm";
//        String outputFilename = "C:/Users/Daniel Whang/Desktop/maumKiosk/src/main/resources/templates/video/captured.mp4";
//        IMediaReader mediaReader =
//            ToolFactory.makeReader(inputFilename);
//        IMediaWriter mediaWriter =
//            ToolFactory.makeWriter(outputFilename, mediaReader);
//        mediaReader.addListener(mediaWriter);
//        IMediaViewer mediaViewer = ToolFactory.makeViewer(true);
//        mediaReader.addListener(mediaViewer);
//        while (mediaReader.readPacket() == null);
    }
}
