
var video;
var videoTracks; // 비디오 촬영 중지를 위한 변수
var snapshotCanvas;
var callbackCamera; // 콜백


$(document).ready(function() {

    $('#imageInputFile').on('change', function(){
        onClick_FileUpload('image', $(this));
    });

    // 촬영
    $('#video_input_box .video_start').on('click', function(){
        onClick_ImageCapture($('#snapshotCanvas'), setImageCaptureUI);
    });

    // 재촬영
    $('#reCapture').on('click', onClick_reCapture);

    // 입력완료
    $('#imgConfirm').on('click',onClick_ImageCapture_Complete);


    $('#video_input_box .ico_close').on('click', function () {
        $('#video_input_box').fadeOut(300);

        // 촬영중이면 중지
        if(videoTracks) videoTracks.forEach(function(track) {track.stop()});

        if(callbackCamera != null) callbackCamera(null, null);
    });

});

/* ===================================================================================================================== */
// 사용자 호출
/* ===================================================================================================================== */

function showPopup_CameraCapture(callback) {
    video = document.getElementById('video');
    callbackCamera = callback;

    initUI();
    $('#video_input_box').fadeIn(function(){
        if (!hasGetUserMedia()) {
            cameraErrCallback("can't get userMedia");
            $('#video_input_box .video_desc').html(messages.popup_camera.video_desc_browserError);
        }else{
            startCamera(cameraErrCallback);
        }
    });

}

/* ===================================================================================================================== */
// 버튼 클릭
/* ===================================================================================================================== */

function onClick_ImageCapture(canvasObj, captureCallback){

    snapshotCanvas = canvasObj.get(0);

    let context = snapshotCanvas.getContext('2d');
    context.drawImage(video, 0, 0, snapshotCanvas.width, snapshotCanvas.height); // Draw the video frame to the canvas.

    videoTracks.forEach(function(track) {track.stop()}); // 촬영 중지
    captureCallback();
}

/* 재촬영 */
function onClick_reCapture(){
    initUI();
    startCamera(cameraErrCallback);
}

/* 입력 완료 */
function onClick_ImageCapture_Complete(){

    let imageUrl = snapshotCanvas.toDataURL('image/jpeg', 1.0);
    let blobBin = atob(imageUrl.split(',')[1]);	// base64 데이터 디코딩
    let array = [];
    for (let i = 0; i < blobBin.length; i++) {
        array.push(blobBin.charCodeAt(i));
    }
    let image_data = new Blob([new Uint8Array(array)], {type: 'image/png'});	// Blob 생성

    videoTracks.forEach(function(track) {track.stop()}); // 촬영 중지

    $('#video_input_box').fadeOut(300);
    if(callbackCamera != null) callbackCamera('image',image_data);

}


/* ===================================================================================================================== */
// UI 설정
/* ===================================================================================================================== */

function initUI() {
    // 사진 UI
    $('#video_input_box .border_line').show();
    $('#uploadedImg').hide();
    // $('#snapshotCanvas').before(video).hide(); // canvas앞에 video를 붙이고, canvas hide
    $('#snapshotCanvas').hide();
    $('#video').show();

    // 촬영 버튼
    $('#video_input_box .video_start').prop('disabled', false);
    $('#video_input_box .video_start').removeClass('disabled');
    $('#video_input_box .video_start').show();

    $('#video_input_box .video_desc').show();
    $('#video_input_box .video_desc').html(messages.popup_camera.video_desc1);
    $('#video_input_box .upload_box').show();

    $('#video_input_box .btn').hide();
    video = document.getElementById('video');
}

/* 촬영 클릭 */
function setImageCaptureUI(){
    $('#video_input_box .upload_box').hide();
    $('#snapshotCanvas').show();
    $('#video_input_box .video_start').hide();
    $('#video_input_box .video_desc').hide();
    $('#video_input_box .border_line').hide();

    $('#Image_capture_resultBtn').css('display','inline-block');
    $('#Image_upload_resultBtn').css('display','none');

    // video = $('#video').detach();
    $('#video').hide();
}

/* ===================================================================================================================== */
// 카메라 동작
/* ===================================================================================================================== */

// Put video listeners into place
function startCamera(ErrCallback) {

    if(navigator.getUserMedia) { // Standard
        navigator.getUserMedia({video: true}, function(stream) {
            video.srcObject = stream;
            videoTracks = stream.getVideoTracks();
        }, ErrCallback);
    } else if(navigator.webkitGetUserMedia) { // WebKit-prefixed
        navigator.webkitGetUserMedia({video: true}, function(stream){
            video.src = window.webkitURL.createObjectURL(stream);
            videoTracks = stream.getVideoTracks();
        }, ErrCallback);
    }
}

function cameraErrCallback(error){
    $('#video_input_box .video_start').prop('disabled', true);
    $('#video_input_box .video_start').addClass('disabled');
    $('#video_input_box .video_desc').html(messages.popup_camera.video_desc_cameraError);
    console.log("Video capture error: ", error);
}

