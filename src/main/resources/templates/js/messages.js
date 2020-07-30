var messages_ko = {
    common: {
        complete: '완료',
        play: '재생',
        pause: ' 중지',
        exec: ' 실행',
    },
    mvp_proj: {
        emptylist: '<li class=\"dataNone\">등록된 프로젝트가 없습니다. 프로젝트 추가를 원하실 경우 상단 프로젝트 등록 버튼을 클릭해 주세요.</li>'
    },
    mvp_popup: {
        api_input_placeholder: "문장을 입력해주세요."
    },
    mvp_exec: {
        popup_selectModel: '모델 선택을 완료해 주세요',
        popup_selectFlowNode: "Flow 노드 등록 후, 실행하세요.",
        ai_styling_inputHint: '원하는 이미지&스타일을 텍스트로 입력해 주세요.\n영문만 지원합니다.',
        popup_input_error: "엔진 입력 데이터 오류"

    },
    mvp_flow: {
        record_desc: '촬영 버튼을 눌러 사진을 촬영해 주세요.',
        button_exec: '실행',
        popup_missingFlowNode: '저장할 Flow 노드가 존재하지 않습니다.',
        popup_missingFlow: '저장된 Flow가 없습니다.',
        next_page: "다음페이지",
        saved_files: "저장 파일 목록",
        server_connectionError: "서버와의 연결이 원활하지 않습니다. 잠시 후 다시 시도해 주세요 :)",
        selection_complete: "선택 완료",
        update: "수정",
        model_request: "모델학습 요청하기",
        engine_noParamError: "<p>해당 API의 경우,<br>선택 parameter가 없습니다</p>",
        result_value: "결과값",
        input_value: "입력값",
        appLink_button_minutes: "maum 회의록 바로가기",
        appLink_button_fast: "FAST 대화형 AI 바로가기",
        audio_playing: "음원 재생",
        download_file: "파일 다운로드",
        view_result: "결과 보기",
        anom_det_start: "이상행동 시작 시점",
        second: "초",
        popup_selectModel: '모델 선택을 완료해 주세요',
    },
    mvp_exec_api: {
        error_missing_input: "Failed<br> *Input 값을 확인해주세요.",
        error_fileSize_exceed: "파일의 크기가 2MB를 초과합니다. 다시 시도해 주세요."
    },
    popup_audio: {
        record_desc1: '음성을 녹음하거나 <br>음성 파일을 업로드해주세요.',
        record_desc2: '음성을 듣고 있습니다.<br> 완료 버튼을 누르면, 음성이 저장 됩니다.',
        recrd_desc_micError: "마이크를 사용할 수 없습니다. <br>파일 업로드를 통해 입력값을 넣어주세요."
    },
    popup_camera: {
        video_desc1: '사진을 촬영하거나 이미지 파일을 업로드해주세요.',
        video_desc_browserError: '현재 브라우저에 대한 기능을 지원하지 않습니다. <br>파일 업로드를 통해 입력값을 넣어주세요.',
        video_desc_cameraError: '카메라를 사용할 수 없습니다. <br>파일 업로드를 통해 입력값을 넣어주세요.',
    },
    popup_engEdu: {
        inputValue: '입력값',
    },
    engine: {
        tts: "음성생성",
        stt: "음성인식",
        denoise: "음성정제",
        voice_filter: "음성필터",
        mrc: "AI독해",
        gpt: "문장생성",
        itf: "의도분류",
        nlu: "자연어이해",
        xdc: "텍스트분류",
        hmd: "패턴분류",
        esr: "ESR",
        ai_styling: "AI스타일링",
        text_rem: "텍스트제거",
        lic_plate_rec: "차량번호인식",
        stt_eng_edu: "영어교육 STT",
        pron_scoring: "발음평가",
        phonics_assess: "파닉스평가",
        chatbot: "챗봇",
        text_conv: "텍스트변환",
        pose_rec: "인물포즈인식",
        subt_extr: "이미지 자막 인식",
        voice_rec: "음성인증",
        voice_rec_set: "음성인증(등록)",
        voice_rec_del: "음성인증(삭제)",
        voice_rec_view: "음성인증(조회)",
        voice_rec_rec: "음성인증(식별)",
        face_rec: "얼굴인증",
        face_rec_set: "얼굴인증(등록)",
        face_rec_del: "얼굴인증(삭제)",
        face_rec_view: "얼굴인증(조회)",
        face_rec_rec: "얼굴인증(식별)",
        face_track: "얼굴추적",
        hotel_conc_chatbot: "호텔컨시어지챗봇",
        anom_det: "이상행동 감지"
    },
    home: {
        error_sendMail: "메일발송 요청 실패하였습니다.",
        error_valid_email: "이메일 형식을 확인해 주세요!",
        error_missing_input: "입력사항을 모두 입력해 주세요.",
        ask: "문의하기",
        ask_custome_model: '나만의 모델 만들기 문의'
    }
};

var messages_en = {
    common: {
        complete: 'Done',
        play: 'Play',
        pause: ' Pause',
        exec: ' Run',
    },
    mvp_proj: {
        emptylist: '<li class=\"dataNone\">There are no projects saved. To add a new project, please click the <br>“+ New Project” button above.</li>'
    },
    mvp_popup: {
        api_input_placeholder: "Please insert text."
    },
    mvp_exec: {
        popup_selectModel: 'Please select a model',
        popup_selectFlowNode: "After registering the flow, execute",
        ai_styling_inputHint: 'Please type in your desired image and style',
        popup_input_error: "Engine Input Error"

    },
    mvp_flow: {
        record_desc: 'Please click the record button to capture image',
        popup_missingFlowNode: 'Flow node does not exist',
        popup_missingFlow: 'There are no flow nodes saved',
        next_page: "Next",
        saved_files: "Saved Files",
        server_connectionError: "There is a problem with the server connection. Please try again later :)",
        selection_complete: "Select",
        update: "Change",
        model_request: "Custom Model Request",
        engine_noParamError: "<p>There are no parameters available<br>for the chosen API</p>",
        result_value: "Result",
        input_value: "Input",
        appLink_button_minutes: "Go to maum Minutes",
        appLink_button_fast: "Go to FAST AI",
        audio_playing: "Play Audio",
        download_file: "Download File",
        view_result: "View Results",
        anom_det_start: "Anomaly Detection Starting Point",
        second: "sec",
        popup_selectModel: 'Please select a model',
    },
    mvp_exec_api: {
        error_missing_input: "Failed<br> *Please check the input value.",
        error_fileSize_exceed: "The file size exceeds 2MB. Please try again."
    },
    popup_audio: {
        record_desc1: 'Please record <br> or upload an audio file.',
        record_desc2: 'Audio is being recorded.<br> Click done to save audio file',
        recrd_desc_micError: "There are issues with the microphone. <br>Please upload an audio file for input."
    },
    popup_camera: {
        video_desc1: 'Please capture or upload an image',
        video_desc_browserError: 'The current browser does not support the funtionality. <br>Please upload a video file for input.',
        video_desc_cameraError: 'There are issues with the camera. <br>Please upload a video file for input.',
    },
    popup_engEdu: {
        inputValue: 'Input',
    },
    engine: {
        tts: "TTS",
        stt: "STT",
        denoise: "Denoise",
        voice_filter: "Voice Filter",
        mrc: "MRC",
        gpt: "GPT",
        itf: "ITF",
        nlu: "NLU",
        xdc: "XDC",
        hmd: "HMD",
        esr: "ESR",
        ai_styling: "AI Styling",
        text_rem: "Text Removal",
        lic_plate_rec: "License Plate Recognition",
        stt_eng_edu: "STT For English Education",
        pron_scoring: "Pronounciation Scoring",
        phonics_assess: "Phonics Assessment",
        chatbot: "Chatbot",
        text_conv: "Text Conversion",
        pose_rec: "Pose Recognition",
        subt_extr: "Subtitle Extraction",
        voice_rec: "Voice Recognition",
        voice_rec_set: "Voice Recognition (Set)",
        voice_rec_del: "Voice Recognition (Delete)",
        voice_rec_view: "Voice Recognition (View)",
        voice_rec_rec: "Voice Recognition (Recognize)",
        face_rec: "Face Recognition",
        face_rec_set: "Face Recognition (Set)",
        face_rec_del: "Face Recognition (Delete)",
        face_rec_view: "Face Recognition (View)",
        face_rec_rec: "Face Recognition (Recognize)",
        face_track: "Face Tracking",
        hotel_conc_chatbot: "Hotel Concierge Chatbot",
        anom_det: "Anomaly Detection"
    },
    home: {
        error_sendMail: "Failed to send email",
        error_valid_email: "Please enter in a valid email address",
        error_missing_input: "Please fill out all fields",
        ask: "Ask Now",
        ask_custome_model: 'Ask to create custom models'
    }
};


