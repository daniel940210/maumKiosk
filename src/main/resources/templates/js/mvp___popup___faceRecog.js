
var callbackFaceRecog = null; // 콜백
var fr_inputType = "";
var fr_image = null;


$(document).ready(function() {

    // 파일 업로드
    $('#frInputFile').on('change', function(){
        onClick_FR_fileUpload($(this));
    });

    // 촬영
    $('#face_input_box .record_btn').on('click', function(){
        show_FR_imageCapture_UI();
        startCamera(FR_cameraErr_callback);
    });

    // 보기
    $('#face_input_box .face_check_btn').on('click', function(){
        onClick_FR_checkImg($(this));
    });

    // 실제 촬영
    $('#face_input_box .video_start').on('click', function(){
        onClick_ImageCapture($('#fr_snapshotCanvas'), onClick_FR_ImageCapture_callback);
    });

    // 삭제
    $('.face_delete').on('click', onClick_FR_deleteImage);

    // 돌아가기
    $('#fr_return').on('click', show_FR_afterInput_UI);

    // faceId 입력
    $('#frInputText').on('input paste', FR_inputCompleteBtn_disable);

    // 입력 완료
    $('#fr_input_complete').on('click', onClick_FR_inputComplete);

    // close
    $('#face_input_box .ico_close').on('click', function () {
        $('#face_input_box').fadeOut(300);

        // 촬영중이면 중지
        if(videoTracks) videoTracks.forEach(function(track) {track.stop()});

        clear_FR_inputData();

        $('body').css({
            'overflow': '',
        });

        if(callbackFaceRecog != null) callbackFaceRecog(null, null);
    });

});


/* ===================================================================================================================== */
// 사용자 호출
/* ===================================================================================================================== */

function showPopup_FaceRecog(fr_engine, callback) {

    video = document.getElementById('fr_video');
    if (!hasGetUserMedia()) {
        console.log("카메라 사용 불가");
        // cameraErrCallback("can't get userMedia");
        // $('.video_desc').html("현재 브라우저에 대한 기능을 지원하지 않습니다. <br>파일 업로드를 통해 입력값을 넣어주세요.");

        disableBtn($('#face_input_box .record_btn'),true);
    }else{
        console.log("카메라 가능?");
        disableBtn($('#face_input_box .record_btn'),false);
        startCamera(FR_cameraErr_callback);
    }

    callbackFaceRecog = callback;
    fr_inputType = fr_engine.inputType;

    switch (fr_inputType) {
        case 'none':
            callbackFaceRecog('none', 'none'); break;
        case 'text':
            show_FR_textPopup(); break;
        case 'image':
            show_FR_imagePopup(); break;
        case 'multi':
            show_FR_multiPopup(); break;
        default:
            console.log("======= Voice Recog : inputType 오류 ==========");
            callbackFaceRecog(null, null);
    }

}


/* ===================================================================================================================== */
// 버튼 클릭
/* ===================================================================================================================== */

function onClick_FR_inputComplete(){

    let faceId = $('#frInputText').val();
    let $Btn = $('#fr_input_complete');

    if(fr_inputType === 'image'){
        if(!presentImageData(fr_image)){
            console.log("파일 없음");
            disableBtn($Btn,true);
            return;
        }else{
            callbackFaceRecog('image', fr_image);
        }

    }else if(fr_inputType === 'text'){
        if(faceId.trim() === ""){
            console.log("텍스트 없음");
            disableBtn($Btn,true);
            return;
        }else{
            callbackFaceRecog('text', faceId);
        }

    }else if(fr_inputType === 'multi'){
        if(!presentImageData(fr_image) || faceId.trim() === ""){
            console.log("input 없음");
            disableBtn($Btn,true);
            return;
        }else{
            let multiObj = {
                "image" : fr_image,
                "faceId" : faceId
            };
            callbackFaceRecog('multi', multiObj);
        }

    }

    $('#face_input_box').fadeOut(300);
    clear_FR_inputData();

}


function onClick_FR_fileUpload(inputObj){
    fr_image = inputObj.get(0).files[0];

    if(!presentImageData(fr_image)){
        return false;
    }else{
        let url = URL.createObjectURL(fr_image);
        $('#face_input_box .face_check_btn').val(url);
        show_FR_afterInput_UI();
        FR_inputCompleteBtn_disable();
    }
}

// 촬영 callback
function onClick_FR_ImageCapture_callback(){
    let imageUrl = snapshotCanvas.toDataURL('image/jpeg', 1.0);
    let blobBin = atob(imageUrl.split(',')[1]);	// base64 데이터 디코딩
    let array = [];
    for (let i = 0; i < blobBin.length; i++) {
        array.push(blobBin.charCodeAt(i));
    }
    fr_image = new Blob([new Uint8Array(array)], {type: 'image/png'});	// Blob 생성

    videoTracks.forEach(function(track) {track.stop()}); // 촬영 중지

    $('#face_input_box .face_check_btn').val(imageUrl);
    show_FR_afterInput_UI();
    FR_inputCompleteBtn_disable();
}



function onClick_FR_checkImg(btn){
    $('#fr_uploadedImg').attr('src', btn.val());
    show_FR_fileCheck_UI();
}

function onClick_FR_deleteImage(){

    fr_image = null;
    $('#face_input_box .face_check_btn').val(null);
    $('#frInputFile').val(null);

    show_FR_beforeInput_UI();
    disableBtn($('#fr_input_complete'),true);
}


/* ===================================================================================================================== */
// UI 설정
/* ===================================================================================================================== */

function init_FR_Popup_UI(){
    $('#face_input_box .record_btn').show();
    $('#face_input_box .upload_box').show();
    $('.face_check_btn').hide();
    $('.face_delete').hide();
    $('#fr_image').show();
    $('#fr_text').show();
    $('#face_input_box .input_box').show();


    $('#face_input_box .recording').hide();
    $('#face_input_box .file_check_box').hide();

    $('#fr_input_complete').show();
    $('#fr_return').hide();
    $('#face_input_box .btn').show();
    disableBtn($('#fr_input_complete'),true);
}

function show_FR_textPopup(){
    init_FR_Popup_UI();
    $('#fr_image').hide();
    $('#face_input_box').fadeIn(300);
}

function show_FR_imagePopup(){
    init_FR_Popup_UI();
    $('#fr_text').hide();
    $('#face_input_box').fadeIn(300);
}

function show_FR_multiPopup(){
    init_FR_Popup_UI();
    $('#face_input_box').fadeIn(300);
}




function show_FR_imageCapture_UI(){
    $('#face_input_box .input_box').hide();
    $('#face_input_box .btn').hide();

    $('#face_input_box .recording').show();
}

function show_FR_fileCheck_UI(){
    $('#face_input_box .ico_close').hide();
    $('#face_input_box .input_box').hide();
    $('#face_input_box .btn #fr_input_complete').hide();

    $('#face_input_box .btn #fr_return').show();
    $('#face_input_box .file_check_box').show();
    $('#face_input_box .btn').show();

}

function show_FR_beforeInput_UI(){
    $('#face_input_box .record_btn').show();
    $('#face_input_box .upload_box').show();
    $('#face_input_box .face_check_btn').hide();
    $('#face_input_box .face_delete').hide();
    $('#face_input_box .input_box').show();

    $('#face_input_box .recording').hide();
    $('#face_input_box .file_check_box').hide();

    $('#face_input_box .btn #fr_input_complete').show();
    $('#face_input_box .btn #fr_return').hide();
    $('#face_input_box .btn').show();
}

function show_FR_afterInput_UI(){
    $('#face_input_box .ico_close').show();
    $('#face_input_box .record_btn').hide();
    $('#face_input_box .upload_box').hide();
    $('#face_input_box .face_check_btn').show();
    $('#face_input_box .face_delete').show();
    $('#face_input_box .input_box').show();

    $('#face_input_box .recording').hide();
    $('#face_input_box .file_check_box').hide();

    $('#face_input_box .btn #fr_input_complete').show();
    $('#face_input_box .btn #fr_return').hide();
    $('#face_input_box .btn').show();
}

function FR_inputCompleteBtn_disable(){
    let frIntputText = $('#frInputText').val().trim();
    let $Btn =$('#fr_input_complete');

    if(fr_inputType === 'image'){
        presentBlobData(fr_image) ? disableBtn($Btn,false) : disableBtn($Btn,true);
    }else if(fr_inputType === 'text'){
        frIntputText !== "" ? disableBtn($Btn,false) : disableBtn($Btn,true);
    }else if(fr_inputType === 'multi'){
        frIntputText !== "" && presentBlobData(fr_image) ? disableBtn($Btn,false) : disableBtn($Btn,true);
    }
}

/* ===================================================================================================================== */
// 공통 function
/* ===================================================================================================================== */
function clear_FR_inputData(){
    fr_image = null;
    $('#face_input_box .face_check_btn').val(null);
    $('#frInputFile').val(null);
    $('#frInputText').val("");
}


function FR_cameraErr_callback(){
    disableBtn($('#face_input_box .record_btn'),true);
}

// image data가 정상적으로 있으면 true
function presentImageData(image_data){
    return !(image_data === '' || image_data === null || image_data === undefined);
}
