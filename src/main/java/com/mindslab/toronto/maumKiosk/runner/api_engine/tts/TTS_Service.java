package com.mindslab.toronto.maumKiosk.runner.api_engine.tts;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@Slf4j
@Service
public class TTS_Service {
    final String API_SERVER = "https://api.maum.ai";
    
    String API_ID = "swhange91d6f0f28ab";
    String API_KEY = "78ab9ae227d149539d96ed553ae448a4";
    
    public byte[] getApiTts (String text, String voiceName) {
        try {
            String url = API_SERVER + "/tts/stream";
            
            HttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost(url);
            
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.addPart("text", new StringBody(text, ContentType.TEXT_PLAIN.withCharset("UTF-8")));
            builder.addPart("voiceName", new StringBody(voiceName, ContentType.TEXT_PLAIN.withCharset("UTF-8")));
            builder.addPart("apiId", new StringBody(API_ID, ContentType.TEXT_PLAIN.withCharset("UTF-8")));
            builder.addPart("apiKey", new StringBody(API_KEY, ContentType.TEXT_PLAIN.withCharset("UTF-8")));
            
            HttpEntity entity = builder.build();
            
            post.setEntity(entity);
            HttpResponse response = client.execute(post);
            
            int responseCode = response.getStatusLine().getStatusCode();
            
            log.info("Response Code : " + response.getEntity());
            
            HttpHeaders headers = new HttpHeaders();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            /* 정상 응답 수신 */
            if (responseCode == 200) {
                response.getEntity().writeTo(baos);
                return baos.toByteArray();
//                return new ResponseEntity<>(imageArray, headers, HttpStatus.OK);
            }
            /* 에러 응답 수신 */
            else {
                log.info("# EngineApiService.getApiTts >> fail >> code = {} >> result >> {}", responseCode, EntityUtils.toString(response.getEntity(), "UTF-8"));
//                return null;
                return null;
            }
        } catch (Exception e) {
            log.info("# EngineApiService.getApiTts >> fail >> exception >> {}", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
