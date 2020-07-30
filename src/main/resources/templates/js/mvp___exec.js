
var default_input_text;
var mvp_request = null;
var reqAbort = false;

$(document).ready(function() {
    $('#Button_MVPExec').on("click", function () {

        let out_leng = $(".output").length;
        let inout_set = $(".inout_set").length;
        if (inout_set > 0 && out_leng != inout_set){
            showPopup_Message(messages.mvp_exec.popup_selectModel);
            return;
        }

        if($(this).html() === messages.common.pause){
            reqAbort = true;
            controlFlow();
        }else{
            if(workingFlow.nodeList.length == 0) {
                showPopup_Message(messages.mvp_exec.popup_selectFlowNode);
                return;
            }

            $('.api_show').css('opacity','0.7');
            $('.output_show').css('opacity','0.7');
            $('#FlowNode_Input_0').css('opacity', '1');
            $('.api_set:last-child .output_show a').remove();

            console.log('==================================================');
            console.log("시나리오를 실행합니다. (", workingFlow.name, ")");
            console.log('--------------------------------------------------');
            runFlow();
        }

        $(this).blur(); // focus out 시키기
    });

    // makeFlow_Sample1();
    // makeFlow_Sample2();
    console.dir(workingFlow);
});


/* ===================================================================================================================== */
// Flow 실행 제어
/* ===================================================================================================================== */

var currentWorkingIndex;

function runFlow() {
    beforeProc_MVPExec();

    currentWorkingIndex = 0;
    console.dir(workingFlow);

    let engineName = engineList[workingFlow.nodeList[0].engineId].name;
    let engineInputType = engineList[workingFlow.nodeList[0].engineId].inputType;

    if(engineName.indexOf(messages.engine.voice_rec) !== -1){
        showPopup_VoiceRecog(engineList[workingFlow.nodeList[0].engineId], popupCallback);
        return;
    }else if(engineName.indexOf(messages.engine.face_rec) !== -1){
        showPopup_FaceRecog(engineList[workingFlow.nodeList[0].engineId], popupCallback);
        return;
    }else if(engineName.indexOf(messages.engine.stt_eng_edu) !== -1 || engineName.indexOf(messages.engine.pron_scoring) !== -1 || engineName.indexOf(messages.engine.phonics_assess) !== -1){
        showPopup_EngEdu(engineList[workingFlow.nodeList[0].engineId], popupCallback);
        return;
    } else if (engineName.indexOf(messages.engine.voice_filter) !== -1) {
        showPopup_VoiceFilter(engineList[workingFlow.nodeList[0].engineId], popupCallback);
        return;
    }else if(engineName.indexOf(messages.engine.mrc) !== -1) {
        showPopup_MRC(engineList[workingFlow.nodeList[0].engineId], popupCallback);
        return;
    }

    if(engineInputType === 'text') {
        let input_hint = null;
        if(engineName === messages.engine.ai_styling) {
            input_hint = messages.mvp_exec.ai_styling_inputHint
        }
        showPopup_InputText(null, input_hint, popupCallback);
    }
    else if(engineInputType === 'audio') {
        showPopup_AudioRecord(getSamplingRate(workingFlow.nodeList[0]), popupCallback);
    }else if(engineInputType === 'image') {
        showPopup_CameraCapture(popupCallback);
    }else if(engineInputType === 'video'){
        showPopup_VideoUpload(popupCallback);
    }else {
        console.log("### 실패 >> 알 수 없는 입력 유형 >> ", engineInputType);
    }

}

function popupCallback(input_type, data){
    if (data == null) {
        console.log('--------------------------------------------------');
        console.log("시나리오가 취소 되었습니다. (", workingFlow.name, ")");
        console.log('==================================================');

        afterProc_MVPExec();
        return;
    }

    controlFlow(input_type, data);
}


async function controlFlow(input_type, input_data) {
    console.log('--------------------------------------------------');
    console.log(input_type);
    console.log(input_data);
    console.log('--------------------------------------------------');

    if(reqAbort === true){
        console.log('--------------------------------------------------');
        console.log("시나리오를 중지합니다. (", workingFlow.name , ")");
        console.log('==================================================');

        mvp_request.abort();
        progressFlowNode(currentWorkingIndex-1, false);
        ///////////////////////////// 미디어 멈추기
        currentWorkingIndex = workingFlow.nodeList.length;
        reqAbort = false;
        afterProc_MVPExec();
        return;
    }

    console.log("currentWorkingIndex = ", currentWorkingIndex);

    if(currentWorkingIndex > 0 && input_type == 'text') displayTextOutput(currentWorkingIndex-1, input_data);
    if(currentWorkingIndex > 0 && input_type == 'audio') displayAudioOutput(currentWorkingIndex-1, input_data);
    if(currentWorkingIndex > 0 && input_type == 'json') displayTextOutput(currentWorkingIndex-1, JSON.stringify(input_data, null, 4));
    if(currentWorkingIndex > 0 && input_type == 'image') displayImageOutput(currentWorkingIndex-1, input_data);
    if(currentWorkingIndex > 0 && input_type == 'multi') displayMultiOutput(currentWorkingIndex-1, input_data);

    if(currentWorkingIndex > 0 && currentWorkingIndex < workingFlow.nodeList.length) await sleep(800);
    progressFlowNode(currentWorkingIndex-1, false);

    if(currentWorkingIndex >= workingFlow.nodeList.length) {
        console.log('--------------------------------------------------');
        console.log("시나리오가 종료 되었습니다. (", workingFlow.name, ")");
        console.log('==================================================');

        afterProc_MVPExec();
        return;
    }

    if(currentWorkingIndex == 0 && input_type == 'text') displayTextInput(currentWorkingIndex, input_data);
    if(currentWorkingIndex == 0 && input_type == 'audio') displayAudioInput(currentWorkingIndex, input_data);
    if(currentWorkingIndex == 0 && input_type == 'image') displayImageInput(currentWorkingIndex, input_data);
    // if(currentWorkingIndex == 0 && input_type == 'none') displayNoneInput(currentWorkingIndex);
    if(currentWorkingIndex == 0 && input_type == 'multi') displayMultiInput(currentWorkingIndex, input_data); // TODO : multi 엔진 추가되면 수정 필요

    progressFlowNode(currentWorkingIndex, true);
    runFlowNode(input_data, workingFlow.nodeList[currentWorkingIndex]);
    currentWorkingIndex++;
}


function runFlowNode(input_data, flow_node) {
    var engine = engineList[flow_node.engineId];
    callApi(input_data, flow_node, engine);
}

function callApi(input_data, flow_node, engine) {

    if(input_data){

        if(engine.inputType === "text" && input_data.trim() === ""){
            showInputErrPopup(engine.name, input_data);
            return;
        }


        api_func_link[engine.name](input_data, flow_node, engine);

    }
    else{
        showInputErrPopup(engine.name, input_data);
    }



    // if(engine.name == '문장생성') callApi_GPT(input_data, flow_node, engine);
    // else if(engine.name == '음성생성') callApi_TTS(input_data, flow_node, engine);
    // else if(engine.name == '챗봇') callApi_Chat(input_data, flow_node, engine);
    // else if(engine.name == '음성인식') callApi_STT(input_data, flow_node, engine);
    // else if(engine.name == '얼굴인증(인증)') callApi_FR(input_data, flow_node, engine);
    // else if(engine.name == '자연어이해') callApi_NLU(input_data, flow_node, engine);
    // else if(engine.name == '텍스트변환') callApi_TextConv(input_data, flow_node, engine);
}


function showInputErrPopup(engineName, input_data){
    let title = "<span style='color:#2575f9;'>" + engineName + "</span>" + messages.mvp_exec.popup_input_error;
    let msg = "Data type : " + typeof(input_data) +
            "<br> Data : " + input_data;

    $('#Engine_InputErr_Popup h2').html(title);
    $('#Engine_InputErr_Popup_Contents').html(msg);

    $('#Engine_InputErr_Popup').show();

    progressFlowNode(currentWorkingIndex, false);
    currentWorkingIndex = workingFlow.nodeList.length;
    controlFlow(null, null);
}



/* ===================================================================================================================== */
// UI 작업
/* ===================================================================================================================== */

/*
** MVP 실행 전, 사전 작업
*/
function beforeProc_MVPExec() {
    // MVP 실행 비활성화
    // $('#Button_MVPExec').hide();
    $('#Button_MVPExec').html(messages.common.pause);

    // $('.mvp_bg').css('display', 'block');
    // $('#Button_MVPExec').css('display', 'none');

    for(let xx = 0; xx < workingFlow.nodeList.length; xx++) {
        if(xx == 0) initInputUI(xx, engineList[workingFlow.nodeList[xx].engineId].inputType);
        initOutputUI(xx, engineList[workingFlow.nodeList[xx].engineId].outputType);
    }
}

/*
** MVP 실행 후, 사후 작업
*/
function afterProc_MVPExec() {
    //console.log( $('.inout_set').eq( $('.inout_set').length-1 ).children('.api_show').children('.api_bot').attr('id') );

    var last_engine_id = $('.inout_set').eq( $('.inout_set').length-1 ).children('.api_show').children('.api_bot').attr('id');

    // 마지막 엔진의 output이 audio나 image일 경우 다운로드 버튼 활성화
    if( engineList[last_engine_id].responseType == 'audio' || engineList[last_engine_id].responseType == 'image' )
        $('.inout_set').eq( $('.inout_set').length-1 ).children('.output_show').children('.output').children('.btn_dwn').css('display', 'block');


    // MVP 실행 버튼 활성화
    // $('#Button_MVPExec').show();
    $('#Button_MVPExec').html("<em class=\"fas fa-project-diagram\"></em>"+messages.common.exec);

}

/* ===================================================================================================================== */
// AUDIO의 샘플링 값 조회
/* ===================================================================================================================== */

function getSamplingRate(flow_node) {
    // 음성 인식인 경우엔, sampling 파라메타를 확인한다.
    if(engineList[flow_node.engineId].name == messages.engine.stt) {
        return getSamplingRate_STT(flow_node);
    }

    return 16000;
}

function getSamplingRate_STT(flow_node) {
    var param_list = new Array();
    getParamListFromFlowNode(flow_node, param_list);

    for(var xx = 0; xx < param_list.length; xx++) {
        if(param_list[xx].name == 'sampling') return param_list[xx].value;
    }

    return 16000;
}


