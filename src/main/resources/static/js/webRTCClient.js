'use strict';

var videoSelf = document.getElementById('videoSelf');
var videoCaller = document.getElementById('videoCaller');
var rtcButton = document.getElementById('rtcButton');
var cancelButton = document.getElementById('cancelButton');
var vidCon = document.getElementById('web-rtc-page');

var connectionStatus = 'VD_STILL';
var offerSignal;
var simplePeer;

function sendOrAcceptInvitation(isInitiator, offer) {

    navigator.mediaDevices.getUserMedia({ video: true, audio: true }).then((mediaStream) => {
        vidCon.style.display = 'flex';
        if ('srcObject' in videoCaller) {
            videoSelf.srcObject = mediaStream;
        } else {
            videoSelf.src = window.URL.createObjectURL(mediaStream) // for older browsers
        }
        videoSelf.play();

        var sp = new SimplePeer({
            trickle: false,
            initiator: isInitiator,
            stream: mediaStream,
        });

        if (isInitiator) {
            connectionStatus = 'VD_OFFERING'
            videoCaller.classList.add("loader");
        }
        else offer && sp.signal(offer);

        sp.on("signal", (data) => {
            const message = {
                sender: username,
                content: JSON.stringify(data)
            }
            if (data.type === 'offer') {
                message.type = 'VD_OFFERING'
                stompClient.send("/app/send/" + chatname, {}, JSON.stringify(message))
            } else if (data.type === 'answer') {
                message.type = 'VD_ANSWER'
                stompClient.send("/app/send/" + chatname, {}, JSON.stringify(message))
            }
        })
        sp.on("connect", () => {
            connectionStatus = 'VD_CONNECTED'
            videoCaller.classList.remove("loader");
        });
        sp.on("stream", (stream) => {
            if ('srcObject' in videoCaller) {
                videoCaller.srcObject = stream
            } else {
                videoCaller.src = window.URL.createObjectURL(stream) // for older browsers
            }
            videoCaller.play();
        });
        simplePeer = sp;
    });
};

function cancelRTC() {
    connectionStatus = 'VD_STILL'
    rtcButton.innerHTML = 'Call';
    cancelButton.style.display = "none";
    stompClient.send("/app/send/" + chatname, {}, JSON.stringify({
        sender: username,
        type: 'VD_CANCEL'
    }))
}

function webRTCaction() {
    switch (connectionStatus) {
        case 'VD_STILL':
            rtcButton.innerHTML = 'Cancel';
            sendOrAcceptInvitation(true);
            break;
        case 'VD_RECEIVING':
            rtcButton.innerHTML = 'Close';
            cancelButton.style.display = "none";
            sendOrAcceptInvitation(false, offerSignal)
            break;
        case 'VD_CONNECTED':
        case 'VD_OFFERING':
            rtcButton.innerHTML = 'Call';
            cancelButton.style.display = "none";
            simplePeer.destroy()
            connectionStatus = 'VD_STILL'
            videoCaller.srcObject = null;
            videoSelf.srcObject = null;
            vidCon.style.display = 'none';
            stompClient.send("/app/send/" + chatname, {}, JSON.stringify({
                sender: username,
                type: 'VD_CANCEL'
            }))
            break;
        default:
            break;
    }
}