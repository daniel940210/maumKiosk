
var callbackVideo; // 콜백


$(document).ready(function() {

    // 파일 업로드
    $('#videoInputFile').on('change', function(){
        onClick_FileUpload('video', $(this));
    });

    // 입력 완료
    $('#videoConfirm').on('click', onClick_VideoUpload_Complete);

    $('#videoUpload_input_box .ico_close').on('click', function () {
        $('#videoUpload_input_box').fadeOut(300);
        $('#videoInputFile').val(null);
        if(callbackVideo != null) callbackVideo(null, null);
    });

});

/* ===================================================================================================================== */
// 사용자 호출
/* ===================================================================================================================== */

function showPopup_VideoUpload(callback) {
    callbackVideo = callback;

    videoUpload_initUI();
    $('#videoUpload_input_box').fadeIn(200);

}

/* ===================================================================================================================== */
// 버튼 클릭
/* ===================================================================================================================== */

function onClick_VideoUpload_Complete(){
    $('#videoUpload_video').stop();
}

/* ===================================================================================================================== */
// UI 설정
/* ===================================================================================================================== */

function videoUpload_initUI() {

    $('#videoUpload_input_box .video_box').hide();
    $('#videoUpload_input_box .upload_box').show();
    $('#videoUpload_input_box .video_desc').show();

    $('#videoUpload_input_box .btn').hide();
}

