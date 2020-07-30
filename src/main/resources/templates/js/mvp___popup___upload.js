
var uploaded_file;

$(document).ready(function() {

    // 재업로드
    $('.reset_upload_btn').on('click', function(){
        let uploadBtnId = '#' + $(this).val() + 'InputFile';
        $(uploadBtnId).click();
    });


    // 입력완료
    $('.file_confirm_btn').on('click',function(){
        let inputType = $(this).val();
        if(inputType === 'audio'){
            $('#audio_input_box').fadeOut(200);
            if(callbackAudiaRecord != null) callbackAudiaRecord(inputType, uploaded_file);
        }
        else if(inputType === 'image'){
            $('#video_input_box').fadeOut(200);
            if(callbackCamera != null) callbackCamera(inputType, uploaded_file);
        }else if(inputType === 'video'){
            $('#videoUpload_input_box').fadeOut(200);
            if(callbackVideo != null) callbackVideo(inputType, uploaded_file);
        }
        uploaded_file = null;
    });


});


function onClick_FileUpload(inputType, inputObj){
    uploaded_file = inputObj.get(0).files[0];

    if(uploaded_file === null || uploaded_file === ""){
        console.log("파일 없음");
        return false;
    }else{
        $('.upload_box').hide();
        $('.upload_desc').hide();

        let url = URL.createObjectURL(uploaded_file);

        if(inputType === 'audio'){

            let audio = document.getElementById("AudioPlayer");
            audio.src = url;
            $('.record_Box').hide();
            $('.record_desc').hide();
            $('.play_Box').fadeIn();
            $('#Audio_record_resultBtn').css('display', 'none');
            $('#Audio_upload_resultBtn').css('display','inline-block');

        }else if(inputType === 'image'){

            if(videoTracks) videoTracks.forEach(function(track) {track.stop()}); // 촬영 중지

            let image = document.getElementById('uploadedImg');
            image.src = url;

            $('#uploadedImg').show();
            $('#video_input_box .video_start').hide();
            $('#video_input_box .video_desc').hide();
            $('#video_input_box .border_line').hide();
            // video = $('#video').detach();
            $('#video').hide();

            $('#Image_capture_resultBtn').css('display','none');
            $('#Image_upload_resultBtn').css('display','inline-block');

        }else if(inputType === 'video'){
            let videoElem = document.getElementById('videoUpload_video');
            videoElem.src = url;

            $('#videoUpload_input_box .video_box').show();
            $('#videoUpload_input_box .upload_box').hide();
            $('#videoUpload_input_box .video_desc').hide();

            $('#videoUpload_input_box .btn').show();
        }

        inputObj.val(null);
    }
}