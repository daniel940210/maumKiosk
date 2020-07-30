package com.mindslab.toronto.maumKiosk.runner.api_engine.faceTracking;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class FaceTracking_Service {
    final String API_SERVER = "https://api.maum.ai";
    
    @Value("${upload.dir}") String UPLOAD_DIR;
    @Value("${api.id}") String API_ID;
    @Value("${api.key}") String API_KEY;
    
    public String getApiFaceTracking(MultipartFile file) {
        Map<Integer, ResponseEntity> map = new HashMap<>();
        
        try {
            String url = API_SERVER + "/Vca/faceTracking";
            
            HttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost(url);
            
            /* 이미지 데이타를 Multipart로 등록하기 위한 준비 */
            String uploadPath = UPLOAD_DIR + "/faceTracking";
            File varFile = new File(uploadPath);
            
            if(!varFile.exists()) {
                varFile.mkdirs();
            }
            
            varFile = new File((uploadPath + "/" + Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf("\\") + 1)));
            file.transferTo(varFile);
            FileBody fileBody = new FileBody(varFile);
    
            /* Multipart 등록 */
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.addPart("video", fileBody);
            builder.addPart("apiId", new StringBody(API_ID, ContentType.MULTIPART_FORM_DATA));
            builder.addPart("apiKey", new StringBody(API_KEY, ContentType.MULTIPART_FORM_DATA));
    
            /* FaceTracking API 실행 */
            HttpEntity entity = builder.build();
            post.setEntity(entity);
            HttpResponse response = client.execute(post);
            
            int responseCode = response.getStatusLine().getStatusCode();
            /* 정상 응답 수신 */
            String result = EntityUtils.toString(response.getEntity(), "UTF-8");
            
            if (responseCode == 200) {
                log.info("# EngineApiService.getApiFaceTracking >> succ >> result >> {}", "너무 길어서 안 찍음");
                varFile.delete();
                return result;
            }
            /* 에러 응답 수신 */
            else {
                log.info("# EngineApiService.getApiFaceTRacking >> fail >> code = {} >> result >> {}", responseCode, result);
                varFile.delete();
                return "{ \"status\": \"error\" }";
            }
            
        } catch (Exception e) {
            log.info("# EngineApiService.getApiFaceTracking >> fail >> exception >> {}", e.getMessage());
            e.printStackTrace();
            return "{ \"status\": \"error\" }";
        }
    }
}
