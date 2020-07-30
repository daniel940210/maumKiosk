
/* ===================================================================================================================== */
// 문자열 관련
/* ===================================================================================================================== */

/*
** printf() 기능 제공, {#} 형식으로...
*/
function formatString() {
    var expression = arguments[0];
    for(var xx = 1; xx < arguments.length; xx++) {
        var pattern = "{" + (xx - 1) + "}";
        expression = expression.replaceAll(pattern, arguments[xx]);
    }

    return expression;
}

String.prototype.replaceAll = function(org, dest) {
    return this.split(org).join(dest);
}


/* ===================================================================================================================== */
// 미디어 관련
/* ===================================================================================================================== */

//---------- Browser 기능 지원 확인 -----------//
function hasGetUserMedia() {
    return !!(navigator.getUserMedia || navigator.webkitGetUserMedia ||
        navigator.mozGetUserMedia || navigator.msGetUserMedia);
}


/* ===================================================================================================================== */
// Flow & 엔진
/* ===================================================================================================================== */

/*
** Flow 노드에 등록된 그룹 파라메타를 일반 파라메타 형식으로 변환한다.
*/
function getParamListFromFlowNode(flow_node, param_list) {
    for(var xx = 0; xx < flow_node.paramList.length; xx++) {
        // console.log("===> ",  flow_node.paramList[xx].name, " : ",  flow_node.paramList[xx].value);
        if(isGroupParam(flow_node.engineId, flow_node.paramList[xx].name)) getParamListFromFlowNode_GroupParam(param_list, flow_node.engineId, flow_node.paramList[xx].name, flow_node.paramList[xx].value);
        else {
            param_list.push(new Param(flow_node.paramList[xx].name, flow_node.paramList[xx].value));
        }
    }
}

function isGroupParam(engine_id, param_name) {
    for(var xx = 0; xx < engineList[engine_id].groupParamList.length; xx++) {
        if(param_name == engineList[engine_id].groupParamList[xx].name) return true;
        // console.log("GroupParam ===> ",  engineList[engine_id].groupParamList[xx].name, " : ",  engineList[engine_id].groupParamList[xx].params);
    }

    return false;
}

/*
** 그룹 파라메타를 일반 파라메타로 변환.
*/
function getParamListFromFlowNode_GroupParam(param_list, engine_id, param_name, param_value) {
    for(var xx = 0; xx < engineList[engine_id].groupParamList.length; xx++) {
        if(param_name == engineList[engine_id].groupParamList[xx].name) {

            for(var yy = 0; yy < engineList[engine_id].groupParamList[xx].valueList.length; yy++) {
                if(param_value == engineList[engine_id].groupParamList[xx].valueList[yy].name) {

                    var param_name_list = engineList[engine_id].groupParamList[xx].params.split(",");
                    var param_value_list = engineList[engine_id].groupParamList[xx].valueList[yy].data.split(",");

                    for(var zz = 0; zz < param_name_list.length; zz++) {
                        console.log("===> ", param_name_list[zz], " : ", param_value_list[zz]);
                        param_list.push(new Param(param_name_list[zz], param_value_list[zz]));
                    }
                    break;
                }
            }

            break;
        }
    }
}



/* ===================================================================================================================== */
// 타임 관련
/* ===================================================================================================================== */

function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
    // $$.delay(ms);
}


/* ===================================================================================================================== */
// 자바스크립트
/* ===================================================================================================================== */


function loadJavascript(url, callback, charset) {
    var head = document.getElementsByTagName('head')[0];
    var script = document.createElement('script');
    script.type = 'text/javascript';
    script.charset = charset;
    var loaded = false;
    script.onreadystatechange = function () {
        if (this.readyState == 'loaded' || this.readyState == 'complete') {
            if (loaded) {
                return;
            }
            loaded = true;
            callback();
        }
    }
    script.onload = function () {
        callback();
    }
    script.src = url;
    head.appendChild(script);
}