package com.mindslab.toronto.maumKiosk.runner.api_engine.stt;

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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Objects;

@Slf4j
@Service
public class STT_Service {
    final String API_SERVER = "https://api.maum.ai";
    
    @Value("${upload.dir}") String UPLOAD_DIR;
    @Value("${api.id}") String API_ID;
    @Value("${api.key}") String API_KEY;
    
    String getApiStt(MultipartFile file, String lang, String level, String sampling) {
        try {
            String url = API_SERVER + "/api/stt/";
            
            HttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost(url);
            
            /* 음원 데이타를 Multipart로 등록하기 위한 준비 */
            String uploadPath = UPLOAD_DIR + "/stt";
            File varFile = new File(uploadPath);
            if (!varFile.exists()) {
                varFile.mkdirs();
            }
            varFile = new File((uploadPath + "/" + Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf("\\") + 1)));
            file.transferTo(varFile);
            FileBody fileBody = new FileBody(varFile);
            
            /* Multipart 등록 */
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.addPart("file", fileBody);
            builder.addPart("lang", new StringBody(lang, ContentType.MULTIPART_FORM_DATA));
            builder.addPart("level", new StringBody(level, ContentType.MULTIPART_FORM_DATA));
            builder.addPart("sampling", new StringBody(sampling, ContentType.MULTIPART_FORM_DATA));
            builder.addPart("ID", new StringBody(API_ID, ContentType.MULTIPART_FORM_DATA));
            builder.addPart("key", new StringBody(API_KEY, ContentType.MULTIPART_FORM_DATA));
            builder.addPart("cmd", new StringBody("runFileStt", ContentType.MULTIPART_FORM_DATA));
            HttpEntity entity = builder.build();
            post.setEntity(entity);
            
            /* STT API 실행 */
            HttpResponse response = client.execute(post);
            
            int responseCode = response.getStatusLine().getStatusCode();
            /* 정상 응답 수신 */
            String result = EntityUtils.toString(response.getEntity(), "UTF-8");
            if (responseCode == 200) {
                log.info("# EngineApiService.getApiStt >> succ >> result >> {}", result);
                return result;
            }
            /* 에러 응답 수신 */
            else {
                log.info("# EngineApiService.getApiStt >> fail >> code = {} >> result >> {}", responseCode, result);
                
                System.out.println("Response Result : " + result.replace('+', ' '));
                System.out.println("API 호출 에러 발생 : 에러코드 = " + responseCode);
                return "{ \"status\": \"error\" }";
            }
            
        } catch (Exception e) {
            log.info("# EngineApiService.getApiStt >> fail >> exception >> {}", e.getMessage());
            e.printStackTrace();
            return "{ \"status\": \"error\" }";
        }
    }
}
