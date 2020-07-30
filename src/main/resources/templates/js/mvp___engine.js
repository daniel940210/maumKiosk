
/* ===================================================================================================================== */
// 엔진 초기화
/* ===================================================================================================================== */

function initEngine() {
   loadEngineGroupList();
}

/* 엔진 그룹 목록 로딩 및 등록 */
function loadEngineGroupList() {
    $.ajax({
        type:"GET",
        url:"/mvp/api/EngineGroups?lang=" +lang_code,
        success: function(result){
            console.log("엔진 그룹 조회 성공");
            console.dir(result);

            engineGrpList = result;
            for(let xx = 0; xx < engineGrpList.length; xx++) {
                drawEngineGroup(engineGrpList[xx]);
                loadEngineListByGroup(engineGrpList[xx]);
            }

        },
        error: function(xhr, status, error) {
            alert(error);
            console.log(xhr.toString());
            console.log(status.toString());
            console.log(error.toString());
        }
    });
}

/* 지정 그룹의 엔진 목록 로딩 및 등록 */
function loadEngineListByGroup(engineGroup) {
    $.ajax({
        type:"GET",
        url:"/mvp/api/EngineGroups/"+ engineGroup.groupId + "?lang=" +lang_code,

        success: function(result){
            console.group("groupId : " + engineGroup.groupId);
            console.dir(result);
            console.groupEnd();

            for(let xx = 0; xx < result.length; xx++) addEngine(result[xx]);
        },
        error: function(xhr, status, error) {
            alert(error);
        }
    });
}




/* ===================================================================================================================== */
// 엔진 그룹 관리
/* ===================================================================================================================== */


/*
** 엔진 그룹 UI 생성
*/
function drawEngineGroup(engineGroup) {
    var id = 'EngineGroup_' + engineGroup.groupId;

    // UI 추가
    var ui_format =
        "<div id='House_{0}' class='api_box api_speak'>\n" +
        "   <p><span> {1} <span></p>\n" +
        "   <ul id='Layer_{0}' class='speak'>\n" +
        "   </ul>\n" +
        "</div>\n";
    $('.enginGroups').append( formatString(ui_format, id, engineGroup.name) );
    // $('.enginGroups').append( formatString(ui_format, id, (lang_code === 'ko') ? engineGroup.name : engineGroup.nameEn) );

    // 속성 설정
    $('#House_' + id + ' p').css('color', '#'+engineGroup.color);
    // $('#House_' + id).css('max-width', (engineGroup.maxCols*45+18)+'px');
    $('#House_' + id).css('max-width', (engineGroup.maxCols*(40+10)+13)+'px');
}




/* ================================================================== */
// 엔진 관리
/* ================================================================== */


/* 엔진 목록에 엔진 추가 */
function addEngine(engine){
    engineList[engine.engineId] = engine;
    drawEngineInGroupLayer(engine);
    resetEngineStatus(null);
}

/* 엔진 그룹 레이어에 엔진 추가 */
function drawEngineInGroupLayer(engine) {
    var parentId = 'EngineGroup_' + engine.groupId;
    var engineId = 'Engine_' + engine.engineId;

    // UI 추가
    var ui_format =
        "<li '>\n" +
        "   <button id='{0}' class='button' value='' title='{2}' disabled>" +
        "       <img src='{1}' >\n" +
        "   </button>\n" +
        "</li>\n"
    $('#Layer_' + parentId).append( formatString(ui_format, engineId, engine.iconPath, engine.desc) );
    // $('#Layer_' + parentId).append( formatString(ui_format, engineId, engine.iconPath, (lang_code === 'ko') ? engine.desc : engine.descEn) );

    // 속성 설정
    $('#' + engineId).val(engine.engineId);
    $('#' + engineId).on('click',onClick_Engine);
    if(engine.active == 0) $('#' + engineId).parent().addClass('disable');
}

/*
** 엔진 목록 UI에서 엔진 클릭 이벤트 처리
*/
function onClick_Engine() {
    console.group("엔진 List");
    console.dir(engineList);
    console.groupEnd();
    $('#Button_MVPExec').addClass('disable').prop('disabled', true);
    addFlowNode( engineList[ $(this).val() ] );
    // resetEngineStatus(engineList[ $(this).val() ]);
}


/*
** 현재 이용 가능한 엔진만 활성화 한다.
*/
function resetEngineStatus(engine) {

    /* 설정 초기화 기능 동작 시, 사용 가능 엔진 reset */
    if (engine == null) {
        for (selectEngine in engineList) {
            if (engineList[selectEngine].active == 0)
                setEngineStatus(engineList[selectEngine], false);
            else
                setEngineStatus(engineList[selectEngine], true);
        }
    } else {

        for (selectEngine in engineList) {
            if (engineList[selectEngine].active == 0) setEngineStatus(engineList[selectEngine], false);

            /* 현재 등록된 FlowNode가 존재하면 연결 가능한 엔진만 등록 가능하도록 설정 */
            else if ($('.cont_api_select').length > 0) {
                if (engineList[selectEngine].inputType != engine.outputType) {
                    setEngineStatus(engineList[selectEngine], 0);
                }
                /* 현재 엔진의 output이 multi일 경우, 다음 엔진의 input이 multi인 것은 비활성화 */
                else if( engine.outputType == 'multi' && engineList[selectEngine].inputType == 'multi' ) {
                    setEngineStatus(engineList[selectEngine], 0);
                }
                else {
                    if(checkUnlinkableEngine(engine, engineList[selectEngine]) == true) setEngineStatus(engineList[selectEngine], 0);
                    else setEngineStatus(engineList[selectEngine], 1);
                }
            }
        }
    }
}

function setEngineStatus(engine, status) {
    if (status == 1) {
        $('#Engine_' + engine.engineId).parent().removeClass('disable');
        $('#Engine_' + engine.engineId).prop('disabled', false);
    }
    else {
        $('#Engine_' + engine.engineId).parent().addClass('disable');
        $('#Engine_' + engine.engineId).prop('disabled', true);
    }
}

/*
** 현재 엔진과 연결이 불가능한 엔진을 체크한다.
*/
function checkUnlinkableEngine(engine, linkingEngine) {
    if(engine.name == messages.engine.tts) {
        if(linkingEngine.name == messages.engine.denoise) return true;
        else if(linkingEngine.name == '화자분리') return true;
    }

    return false;
}