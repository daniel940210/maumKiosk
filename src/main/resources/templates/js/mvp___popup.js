

$(document).ready(function() {
    setPopupCallback_InputText();
    // setPopupCallback_InputAudio();

    $('#api_input').on('input keyup paste change', function () {
        let $inputCompleteBtn = $('#text_input_box .blue_btn');
        if ($(this).val() !== "") {
            disableBtn($inputCompleteBtn,false);
            // $inputCompleteBtn.prop('disabled', false).css({'background':'#2877f9', 'cursor':'pointer'});
        } else {
            disableBtn($inputCompleteBtn,true);
            // $inputCompleteBtn.prop('disabled', true).css({'background':'#c8c8c8', 'cursor':'default'});
        }
    });


    $('#text_input_box .ico_close').on('click', function () {
        $('#text_input_box').fadeOut(300);

        $('body').css({
            'overflow': '',
        });

        $('#api_input').val("");

        if(callback_InputText != null) callback_InputText(null, null);
    });

});

/* ===================================================================================================================== */
// 텍스트 입력 팝업
/* ===================================================================================================================== */

var callback_InputText = null;
function setPopupCallback_InputText() {
    $('#text_input_box .btn button').on('click', function () {

        if($('#api_input').val().trim() === ""){
            disableBtn($(this),true);
            // $(this).prop('disabled', true).css({'background':'#c8c8c8', 'cursor':'default'});
            $('#api_input').val('').focus();
        }
        else{
            if(callback_InputText != null) {
                callback_InputText('text', $('#api_input').val());
            }
            $('#api_input').val("");
            $('#text_input_box').fadeOut(200);
        }
    });
}

function showPopup_InputText(title, hint, callback) {
    if(title != null) ;

    if(hint != null) $('#api_input').attr('placeholder', hint);
    else  $('#api_input').attr('placeholder', messages.mvp_popup.api_input_placeholder);

    callback_InputText = callback;
    $('#text_input_box').show();
    $('#api_input').change();
}

/* ===================================================================================================================== */
// 메세지 팝업
/* ===================================================================================================================== */

function showPopup_Message(content) {
    $('#Popup_Message').fadeIn();
    $('#Popup_Message_Content').text(content);
    $('#Button_Ok').on('click', function () {
        $('#Popup_Message').fadeOut();
    })
}



/* ===================================================================================================================== */
// 팝업 공통 function
/* ===================================================================================================================== */

function disableBtn($Btn, disable){
    if(disable){ // disable 시키기
        $Btn.addClass('disable');
        $Btn.attr('disabled', true);
    }else{
        $Btn.removeClass('disable');
        $Btn.attr('disabled', false);
    }
}


