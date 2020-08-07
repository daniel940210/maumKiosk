package com.mindslab.toronto.maumKiosk.commons;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Slf4j
@Service
public class ImageSrcToFR_Service {
    
    public void convert (String imageSrc) throws IOException {
        InputStream stream = new ByteArrayInputStream(Base64.getDecoder().decode(imageSrc.getBytes()));
        BufferedImage image = ImageIO.read(stream);
        File file = new File("C:/Users/Daniel Whang/Desktop/maumKiosk/src/main/resources/templates/video/tracked.jpg");
        try {
            File dir = new File(file.getParentFile(), file.getName());
            ImageIO.write(image, "jpg", dir);
            System.out.println("Image saved to: " + dir.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
