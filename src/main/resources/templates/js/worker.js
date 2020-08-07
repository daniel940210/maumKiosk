importScripts('ffmpeg.js');

function print(text) {
    postMessage({
        'type' : 'stdout',
        'data' : text
    });
}

onmessage = function(event) {
    let module = {
        files: event.data.files || [],
        arguments: event.data.arguments || [],
        print: print,
        printErr: print
    };
    postMessage({
        'type' : 'start',
        'data' : module.arguments
    });
    let result = ffmpeg_run(module);
    postMessage({
        'type' : 'done',
        'data' : result
    });
};

postMessage({
    'type' : 'ready'
});