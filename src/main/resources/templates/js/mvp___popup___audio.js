var is_audio_recording = false;
var audio_recorder;
var audio_context;
var audio_stream;
var callbackAudiaRecord;
var sampling_rate;

$(document).ready(function() {
    // recordButton = document.getElementById('record');
    // stopButton = document.getElementById('stop');

    // get audio stream from user's mic

    $('.demoRecord').on('click', onClick_AudioRecord);
    $('#audio_input_box .play_btn').on('click', onClick_AudioPlay);

    $('#audioInputFile').on('change', function(){
        stopAndClearRecording();
        onClick_FileUpload('audio', $(this));
    });

    $('#Button_AudioRecord_Reset').on('click', onClick_AudioRecord_Reset);
    $('#Button_AudioRecord_Complete').on('click', onClick_AudioRecord_Complete);

    $('#audio_input_box .ico_close').on('click', function () {
        $('#audio_input_box').fadeOut(300);

        if(is_audio_recording) is_audio_recording = false;
        stopAndClearRecording();

        // 업로드 파일 비워주기
        uploaded_file = null;

        $('body').css({
            'overflow': '',
        });

        if(callbackAudiaRecord != null) callbackAudiaRecord(null, null);
    });
});

/* ===================================================================================================================== */
// 사용자 호출
/* ===================================================================================================================== */

function showPopup_AudioRecord(sampling, callback) {
    sampling_rate = sampling;
    callbackAudiaRecord = callback;

    // UI 초기화
    setReadyUI_AudioRecord();

    $('#audio_input_box').show();

    setReadyAudioAPI(audioInputCallback);
}


/* ===================================================================================================================== */
// 버튼 클릭
/* ===================================================================================================================== */

/*
** 레코딩 시작, 멈춤 버튼 클릭
*/
function onClick_AudioRecord() {
    /* 레코딩 중이면, 레코딩 중단. */
    if(is_audio_recording) {
        setPlayReadyUI_AudioRecord();
        stopRecording();
        exportRecordedAudio();
        is_audio_recording = false;
    }
    /* 레코딩 전이면, 레코딩 시작 */
    else {
        is_audio_recording = true;
        setRecordingUI_AudioRecord();
        startRecording();
    }
}

/*
** 녹음된 오디오 재생 버튼 클릭
*/
function onClick_AudioPlay() {
    playAudio();
}

/*
** 다시 녹음 버튼 클릭
*/
function onClick_AudioRecord_Reset() {
    setReadyUI_AudioRecord();
    setReadyAudioAPI(audioInputCallback);
}

/*
** 녹음 완료 버튼 클릭
*/
function onClick_AudioRecord_Complete() {

    $('#audio_input_box').fadeOut(200);

    /* 사용자 콜백으로 음원 데이타 전달 */
    let AudioFormat = "audio/wav";
    audio_recorder && audio_recorder.exportMonoWAV(function (blob) {
        if(callbackAudiaRecord != null) callbackAudiaRecord('audio', blob);
        audio_recorder.clear();
    }, (AudioFormat || "audio/wav"), sampling_rate);
}



/* ===================================================================================================================== */
// UI 설정
/* ===================================================================================================================== */

/*
** 녹음 전 초기 UI
*/
function setReadyUI_AudioRecord() {
    $('.play_Box').hide();
    $('#Layer_ResultButton').hide();

    $('.demoRecord em').removeClass('fa-stop-circle');
    $('.demoRecord em').addClass('fa-microphone');
    $('.demoRecord span strong').text('녹음');
    $('.demoRecord').prop('disabled', false);
    $('.demoRecord').removeClass('disabled');

    $('.record_Box').show();
    // $('.record_desc').html('음성을 녹음하거나 <br>음성 파일을 업로드해주세요.');
    $('.record_desc').html(messages.popup_audio.record_desc1);
    $('.record_desc').show();
    $('#audio_input_box .upload_box').show();
    $('#audio_input_box .btn').hide();
    $('.holeBox').hide();
}

/*
** 녹음중인 UI
*/
function setRecordingUI_AudioRecord() {
    $('#audio_input_box .upload_box').hide();
    $('.demoRecord em').removeClass('fa-microphone');
    $('.demoRecord em').addClass('fa-stop-circle');
    $('.demoRecord span strong').text(messages.common.complete);
    $('.record_desc').html(messages.popup_audio.record_desc2);
    $('.holeBox').show();
}

/*
** 녹음 완료 UI
*/
function setPlayReadyUI_AudioRecord() {
    $('.record_Box').hide();
    $('.record_desc').hide();
    $('.play_Box').fadeIn();
    $('#Audio_record_resultBtn').css('display','inline-block');
    $('#Audio_upload_resultBtn').css('display', 'none');

}


/* ===================================================================================================================== */
// 오디오 동작
/* ===================================================================================================================== */

function setReadyAudioAPI(callback){
    try {
        // webkit shim
        window.AudioContext = window.AudioContext || window.webkitAudioContext;
        navigator.getUserMedia = navigator.getUserMedia || navigator.webkitGetUserMedia;
        window.URL = window.URL || window.webkitURL;

        audio_context = new AudioContext;
        console.log('navigator.getUserMedia ' + (navigator.getUserMedia ? 'available.' : 'not present!'));
    } catch (e) {
        callback();
    }

    navigator.getUserMedia({audio: true}, onRecordingReady, function(e) {
        console.log('No live audio input: ' + e);
        //alert(  '마이크를 사용할 수 없습니다.\n' +
        //    '마이크를 연결하시거나, 아래의 링크로 수동으로 권한 설정을 해주세요.\n' +
        //    'https://support.google.com/chrome/answer/2693767?co=GENIE.Platform%3DDesktop&hl=ko');
        callback();
    });
}

function audioInputCallback(){
    $('.demoRecord').prop('disabled', true);
    $('.demoRecord').addClass('disabled');
    $('.record_desc').html(messages.popup_audio.recrd_desc_micError);
}


function onRecordingReady(stream) {
    audio_stream = stream;
    var input = audio_context.createMediaStreamSource(stream);
    audio_recorder = new Recorder(input);
}

function startRecording() {
    audio_recorder && audio_recorder.record();
}

function stopAndClearRecording(){
    if(audio_stream){
        stopRecording();
        clearAudio();
    }
}

function stopRecording() {
    audio_recorder && audio_recorder.stop();
    audio_stream.getAudioTracks()[0].stop();
}

function clearAudio(){
    audio_recorder.clear();
}


function exportRecordedAudio(){

    audio_recorder && audio_recorder.exportMonoWAV(function (blob) {
        var url = URL.createObjectURL(blob);

        var audio = document.getElementById("AudioPlayer");
        audio.src = url;

    }, "audio/wav", 16000)
}

function playAudio() {
    $('#AudioPlayer')[0].pause();
    $('#AudioPlayer')[0].load();
    $('#AudioPlayer')[0].oncanplaythrough = $('#AudioPlayer')[0].play();
}

function pauseAudio(){
    $('#AudioPlayer')[0].pause();
}
