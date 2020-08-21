package com.mindslab.toronto.maumKiosk.runner.api_engine.face_recog;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
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

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Objects;

@Slf4j
@Service
public class FaceRecog_Service {

    final String API_SERVER = "https://api.maum.ai";
    
//    @Value("${api.id}") String API_ID;
//    @Value("${api.key}") String API_KEY;
//    @Value("${upload.dir}") String UPLOAD_DIR;
    String UPLOAD_DIR = "c:/upload";
    String API_ID = "swhange91d6f0f28ab";
    String API_KEY = "78ab9ae227d149539d96ed553ae448a4";
    
    public String getFace(String dbId) {
        try {
            String url = API_SERVER + "/insight/app/getFaceList";
            
            HttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost(url);
            
            /* Multipart 등록 */
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.addPart("apiId", new StringBody(API_ID, ContentType.MULTIPART_FORM_DATA));
            builder.addPart("apiKey", new StringBody(API_KEY, ContentType.MULTIPART_FORM_DATA));
            builder.addPart("dbId", new StringBody(dbId, ContentType.TEXT_PLAIN.withCharset("UTF-8")));
            HttpEntity entity = builder.build();
            post.setEntity(entity);
            
            /* getFace API 실행 */
            HttpResponse response = client.execute(post);
            
            int responseCode = response.getStatusLine().getStatusCode();
            
            /* 정상 응답 수신 */
            String result = EntityUtils.toString(response.getEntity(), "UTF-8");
            
            if (responseCode == 200) {
                log.info("# EngineApiService.getFace >> succ >> result >> {}", result);
                return result;
            }
            /* 에러 응답 수신 */
            else {
                log.info("# EngineApiService.getFace >> fail >> code = {} >> result >> {}", responseCode, result);
                
                System.out.println("Response Result : " + result.replace('+', ' '));
                System.out.println("API 호출 에러 발생 : 에러코드 = " + responseCode);
                return "{ \"status\": \"error\" }";
            }
            
        } catch (Exception e) {
            log.info("# EngineApiService.getFace >> fail >> exception >> {}", e.getMessage());
            e.printStackTrace();
            return "{ \"status\": \"error\" }";
        }
    }
    
    public String setFace(String faceId, String dbId) {
        try {
            String url = API_SERVER + "/insight/app/setFace";
            
            HttpClient client = HttpClients.createDefault();
            HttpPut put = new HttpPut(url);
            
            /* 이미지 데이타를 Multipart로 등록하기 위한 준비 */
//            String uploadPath = UPLOAD_DIR + "/faceRecog";
//            File varFile = new File(uploadPath);
//
//            if (!varFile.exists()) {
//                varFile.mkdirs();
//            }
//
//            varFile = new File((uploadPath + "/" + Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf("\\") + 1)));
//            file.transferTo(varFile);
//            FileBody fileBody = new FileBody(varFile);
            
            File file = new File ("C:/Users/Daniel Whang/Desktop/maumKiosk/src/main/resources/templates/video/tracked.jpg");
            FileBody fileBody = new FileBody(file);
            
            /* Multipart 등록 */
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.addPart("apiId", new StringBody(API_ID, ContentType.MULTIPART_FORM_DATA));
            builder.addPart("apiKey", new StringBody(API_KEY, ContentType.MULTIPART_FORM_DATA));
            builder.addPart("dbId", new StringBody(dbId, ContentType.TEXT_PLAIN.withCharset("UTF-8")));
            builder.addPart("faceId", new StringBody(faceId, ContentType.TEXT_PLAIN.withCharset("UTF-8")));
            builder.addPart("file", fileBody);
            HttpEntity entity = builder.build();
            put.setEntity(entity);
            
            /* setFace API 실행 */
            HttpResponse response = client.execute(put);
            
            int responseCode = response.getStatusLine().getStatusCode();
            
            /* 정상 응답 수신 */
            if (responseCode == 200) {
                String result = EntityUtils.toString(response.getEntity(), "UTF-8");
                log.info("# EngineApiService.setFace >> succ >> result >> {}", result);
//                varFile.delete();
                return result;
            }
            /* 에러 응답 수신 */
            else {
                String result = EntityUtils.toString(response.getEntity(), "UTF-8");
                log.info("# EngineApiService.setFace >> fail >> code = {} >> result >> {}", responseCode, result);
                
                System.out.println("Response Result : " + result.replace('+', ' '));
                System.out.println("API 호출 에러 발생 : 에러코드 = " + responseCode);
//                varFile.delete();
                return "{ \"status\": \"error\" }";
            }
            
        } catch (Exception e) {
            log.info("# EngineApiService.setFace >> fail >> exception >> {}", e.getMessage());
            e.printStackTrace();
            return "{ \"status\": \"error\" }";
        }
    }
    
    public String deleteFace(String faceId, String dbId) {
        String apiUrl = API_SERVER + "/insight/app/deleteFace";
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "3FS4PKI7YH9S";
        
        try {
            URL connectURL = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) connectURL.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Connection", "Keep-Alive"); // connection을 위함
            conn.setRequestProperty("Content-type", "multipart/form-data;boundary = " + boundary);
            conn.setConnectTimeout(15000);
    
            /* DataOutputStream 등록 */
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            
            StringBuilder sb = new StringBuilder();
            sb.append(twoHyphens).append(boundary).append(lineEnd);
            sb.append("Content-Disposition: form-data; name=\"apiId\"").append(lineEnd);
            sb.append(lineEnd);
            sb.append(API_ID).append(lineEnd);
            
            sb.append(twoHyphens).append(boundary).append(lineEnd);
            sb.append("Content-Disposition: form-data; name=\"apiKey\"").append(lineEnd);
            sb.append(lineEnd);
            sb.append(API_KEY).append(lineEnd);
            
            sb.append(twoHyphens).append(boundary).append(lineEnd);
            sb.append("Content-Disposition: form-data; name=\"faceId\"").append(lineEnd);
            sb.append(lineEnd);
            sb.append(faceId).append(lineEnd);
            
            sb.append(twoHyphens).append(boundary).append(lineEnd);
            sb.append("Content-Disposition: form-data; name=\"dbId\"").append(lineEnd);
            sb.append(lineEnd);
            sb.append(dbId).append(lineEnd);
            
            sb.append(twoHyphens).append(boundary).append(twoHyphens).append(lineEnd);
            
            dos.writeUTF(sb.toString());
            dos.flush();
    
            /* deleteFace API 실행 */
            int responseCode = conn.getResponseCode();
            StringBuilder outResult = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"), 8);
            String inputLine;
            while((inputLine = br.readLine()) != null) {
                try {
                    outResult.append(URLDecoder.decode(inputLine, "UTF-8"));
                }catch (Exception e) {
                    outResult.append(inputLine);
                }
            }
            /* 정상 응답 수신 */
            if (responseCode == 200) {
                log.info("# EngineApiService.deleteFace >> succ >> result >> {}", outResult);
                br.close();
                return outResult.toString();
            }
            /* 에러 응답 수신 */
            else {
                String result = outResult.toString();
                
                log.info("# EngineApiService.deleteFace >> fail >> code = {} >> result >> {}" , responseCode, result);
                return "{ \"status\": \"error\" }";
            }
        } catch (Exception e) {
            log.info("# EngineApiService.deleteFace >> fail >> exception >> {}", e.getMessage());
            e.printStackTrace();
            return "{ \"status\": \"error\" }";
        }
    }
    
    public String recogFace(String dbId) {
        try {
            String url = API_SERVER + "/insight/app/recogFace";
            
            HttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost(url);
            
            /* 이미지 데이타를 Multipart로 등록하기 위한 준비 */
//            String uploadPath = UPLOAD_DIR+ "/faceRecog";
//            File varFile = new File(uploadPath);
//
//            if(!varFile.exists()) {
//                varFile.mkdirs();
//            }
//
//            varFile = new File((uploadPath + "/" + Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf("\\" + 1))));
//            file.transferTo(varFile);
//            FileBody fileBody = new FileBody(varFile);
    
            File file = new File ("C:/Users/Daniel Whang/Desktop/maumKiosk/src/main/resources/templates/video/tracked.jpg");
            FileBody fileBody = new FileBody(file);
            
            /* Multipart 등록 */
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.addPart("apiId", new StringBody(API_ID, ContentType.MULTIPART_FORM_DATA));
            builder.addPart("apiKey", new StringBody(API_KEY, ContentType.MULTIPART_FORM_DATA));
            builder.addPart("dbId", new StringBody(dbId, ContentType.MULTIPART_FORM_DATA));
            builder.addPart("file", fileBody);
            HttpEntity entity = builder.build();
            post.setEntity(entity);
            
            /* recogFace API 실행 */
            HttpResponse response = client.execute(post);
            
            int responseCode = response.getStatusLine().getStatusCode();
            
            /* 정상 수신 응답 */
            String result = EntityUtils.toString(response.getEntity(), "UTF-8");
            if (responseCode == 200) {
                log.info("# EngineApiService.recogFace >> succ >> result >> {}", "너무 길어서 안찍음");
//                varFile.delete();
                return result;
            }
            /* 에러 응답 수신 */
            else {
                log.info("# EngineApiService.recogFace >> fail >> code = {} >> result >> {}", responseCode, result);
                
                System.out.println("Response Result : " + result.replace('+', ' '));
                System.out.println("API 호출 에러 발생 : 에러코드 = " + responseCode);
//                varFile.delete();
                return "{ \"status\": \"error\" }";
            }
        } catch (Exception e) {
            log.info("# EngineApiService.recogFace >> fail >> exception >> {}", e.getMessage());
            e.printStackTrace();
            return "{ \"status\": \"error\" }";
        }
    }
}
