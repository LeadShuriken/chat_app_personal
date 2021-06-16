'use strict';

var chatnamePage = document.querySelector('#chatname-page');
var chatPage = document.querySelector('#chat-page');
var chatnameForm = document.querySelector('#chatnameForm');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');

var stompClient = null;
var chatname = null;
var username = null;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

function connect(event) {
    username = document.querySelector('#userName').value.trim();
    chatname = document.querySelector('#chatName').value.trim();

    if (chatname && username) {
        chatnamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');

        document.getElementById("copytextarea").value = window.location.origin + '/join?id=' + chatname;

        var socket = new SockJS('/wss');
        stompClient = Stomp.over(socket);

        stompClient.connect({ chat: chatname }, onConnected, onError);
    }
    event.preventDefault();
}

function copyToClipboard(text) {
    if (window.clipboardData && window.clipboardData.setData) {
        // Internet Explorer-specific code path to prevent textarea being shown while dialog is visible.
        return window.clipboardData.setData("Text", text);

    }
    else if (document.queryCommandSupported && document.queryCommandSupported("copy")) {
        var textarea = document.createElement("textarea");
        textarea.textContent = text;
        textarea.style.position = "fixed";  // Prevent scrolling to bottom of page in Microsoft Edge.
        document.body.appendChild(textarea);
        textarea.select();
        try {
            return document.execCommand("copy");  // Security exception may be thrown by some browsers.
        }
        catch (ex) {
            console.warn("Copy to clipboard failed.", ex);
            return false;
        }
        finally {
            document.body.removeChild(textarea);
        }
    }
}

function onConnected() {
    // Subscribe to the Public Topic
    stompClient.subscribe('/chat/' + chatname, onMessageReceived);

    // Tell your chatname to the server
    stompClient.send("/app/register/" + chatname,
        {}, JSON.stringify({ sender: username, type: 'JOIN' })
    )

    connectingElement.classList.add('hidden');
}

function onError(error) {
    connectingElement.textContent = 'No open websockets here.';
    connectingElement.style.color = 'red';
}

function send(event) {
    var messageContent = messageInput.value.trim();

    if (messageContent && stompClient) {
        var chatMessage = {
            sender: username,
            content: messageInput.value,
            type: 'CHAT'
        };

        stompClient.send("/app/send/" + chatname, {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}

function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);

    var messageElement = document.createElement('li');

    if (message.type === 'JOIN') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' joined!';
    } else if (message.type === 'LEAVE') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' left!';
    } else {
        messageElement.classList.add('chat-message');

        var avatarElement = document.createElement('i');
        var avatarText = document.createTextNode(message.sender[0]);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(message.sender);

        messageElement.appendChild(avatarElement);

        var chatnameElement = document.createElement('span');
        var chatnameText = document.createTextNode(message.sender);
        chatnameElement.appendChild(chatnameText);
        messageElement.appendChild(chatnameElement);
    }

    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}


function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }

    var index = Math.abs(hash % colors.length);
    return colors[index];
}

messageForm.addEventListener('submit', send, true)
chatnameForm.addEventListener('submit', connect, true)

document.querySelector("#copy").onclick = function () {
    copyToClipboard(document.getElementById("copytextarea").value);
};