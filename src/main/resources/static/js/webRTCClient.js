var webSocketConnection = new WebSocket('ws://' + window.location.host + '/videochat');

var videoSelf = document.getElementById('video');
var videoCaller;
var connectionStatus;
var offerSignal;
var simplePeer;

webSocketConnection.onmessage(function (message) {
    const payload = JSON.parse(message.data);
    if (payload.type === "offer") {
        offerSignal = payload;
        connectionStatus = 'VD_RECEIVING';
    } else if (payload.type === "answer") {
        simplePeer = payload;
    }
})

function sendOrAcceptInvitation(isInitiator, offer) {
    navigator.mediaDevices.getUserMedia({ video: true, audio: false }).then((mediaStream) => {
        const video = videoSelf.current;
        video.srcObject = mediaStream;
        video.play();

        sp = new SimplePeer({
            trickle: false,
            initiator: isInitiator,
            stream: mediaStream,
        });

        if (isInitiator) connectionStatus = 'VD_OFFERING';
        else offer && sp.signal(offer);

        sp.on("signal", (data) => webSocketConnection.send(JSON.stringify(data)));
        sp.on("connect", () => connectionStatus = 'VD_CONNECTED');
        sp.on("stream", (stream) => {
            const video = videoCaller.current;
            video.srcObject = stream;
            video.play();
        });
        simplePeer = sp;
    });
};