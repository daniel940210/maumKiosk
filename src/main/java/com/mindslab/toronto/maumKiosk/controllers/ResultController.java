package com.mindslab.toronto.maumKiosk.controllers;

import com.mindslab.toronto.maumKiosk.commons.*;
import com.mindslab.toronto.maumKiosk.runner.api_engine.faceTracking.FaceTracking_Service;
import com.mindslab.toronto.maumKiosk.runner.api_engine.face_recog.FaceRecog_Service;
import com.mindslab.toronto.maumKiosk.runner.api_engine.tts.TTS_Service;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.awt.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Controller
@RequestMapping("/result")
public class ResultController {
    @Autowired
    private Base64ToFile_Service base64ToFile;
    //     Input: String (base64-encoded src of video or image)
    //     Output: MultipartFile (of captured video, for Face Tracking input)
    
    @Autowired
    private FaceTracking_Service ftService;
    //     Input: MultipartFile (video)
    //     Output: String (in JSON response format)
    
    @Autowired
    private FaceRecog_Service frService;
    //     Input: MultipartFile (image)
    //     Output: String (in JSON response format)
    
    @Autowired
    private FTtoImageSrc_Service ftToImageSrc;
    //     Input: String (of Face Tracking response, in JSON format)
    //     Output: String (base64-encoded src of first-tracked face image)
    
    @Autowired
    private ImageSrcToFR_Service imageSrcToFR;
    //     Input: String (base64-encoded src of image from Face Tracking response)
    //     Output: Void (Image file saved locally)
    
    @Autowired
    private Slack_Service slack_service;
    
    @Autowired
    private TTS_Service tts_service;
    
    @RequestMapping("/")
    public String footerInfo (@ModelAttribute("videoSrc") String videoSrc, Model model) throws IOException {
//        System.out.println(videoSrc);
//        System.out.println("Converting video into appropriate format...");
//        base64ToFile.convert(videoSrc);

        // 현재 JavaScript에서는 mp4 형식의 비디오를 캡쳐하는 기능을 서포트 하지 않기때문에,
        // 일단 따로 캡쳐한 mp4 영상을 가져와서 face tracking에 보내준다.
        System.out.println("Capture sent to Face Tracking API...");
        String ftResult = ftService.getApiFaceTracking();

        // Face tracking이 성공적일시 응답문에 첨부된 image data URI를 추출해온다.
        System.out.println("Face Tracking successful! Creating result image...");
        String imageSrc = ftToImageSrc.convert(ftResult);

        // 추출한 image data URI를 jpg 파일로 저장한다.
        System.out.println("Image sent to Face Recognition API...");
        imageSrcToFR.convert(imageSrc);

        // 저장한 jpg 파일을 face recognition에 전송한다.
        System.out.println("Face Recognition successful! Creating response...");
        String frResult = frService.recogFace("maumKiosk");
        JSONObject frJSON = new JSONObject(frResult);
        String userName;
        boolean isRecognized;
        String voiceSrc;

        // 얼굴인식 실패시, 신규 방문자 절차로 넘어간다. (방문자 이름, 방문 이유 기입 유도)
        if (frJSON.getJSONObject("result").getString("id").equals("__no__match__")) {
            userName = "New Visitor";
            isRecognized = false;
            voiceSrc = "data:audio/wav;base64, " + Base64.getEncoder().encodeToString(tts_service.getApiTts("Welcome, new visitor. Please type in your name, and then press submit.", "baseline_eng"));
        } else {
            // 얼굴인식 성공시, 슬랙으로 방문자 Face ID와 함께 메세지를 보낸다.
            userName = frJSON.getJSONObject("result").getString("id");
            isRecognized = true;
            slack_service.knownUser(userName);
            System.out.println("Message sent to Slack!");
            voiceSrc = "data:audio/wav;base64, " + Base64.getEncoder().encodeToString(tts_service.getApiTts("Welcome, " + userName + " We notified your arrival to our representative.", "baseline_eng"));
        }
        model.addAttribute("voiceSrc", voiceSrc);
        model.addAttribute("name", userName);
        model.addAttribute("isRecognized", isRecognized);
        model.addAttribute("videoSrc", videoSrc);
        model.addAttribute("imageSrc", "data:image/jpeg;base64, " + imageSrc);
        model.addAttribute("datetime", new Date());
        return "view/result";
    }
    
    @RequestMapping(value="/newVisitor", method= RequestMethod.POST)
    public String newUser (@RequestParam(value="newVisitorName") String newVisitorName, RedirectAttributes redirectAttr) {
        redirectAttr.addFlashAttribute("newVisitorName", newVisitorName);
        return "redirect:/newVisitor/";
    }
    
    @RequestMapping("/redirectB")
    public RedirectView toInput () {
        return new RedirectView("/input/");
    }
    @RequestMapping("/redirectF")
    public RedirectView toWelcome () {
        return new RedirectView("/");
    }
}
