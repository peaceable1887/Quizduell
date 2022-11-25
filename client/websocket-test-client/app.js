import SockJS from "sockjs-client";
import Stomp from "stompjs";

const msg = "Hello World";
const wsURL = "ws://test.burmeister.hamburg/lobby-websocket";
const httpURL = "https://test.burmeister.hamburg/lobby-websocket";
console.log(msg);

const socket = new SockJS(httpURL);
const stompClient = Stomp.over(socket);
let sub_first_msg_new_lobby = true;

stompClient.onConnect = function (frame) {
  console.log("connected!");
};

stompClient.onStompError = function (frame) {
  // Will be invoked in case of error encountered at Broker
  // Bad login/passcode typically will cause an error
  // Complaint brokers will set `message` header with a brief message. Body may contain details.
  // Compliant brokers will terminate the connection after any error
  console.log("Broker reported error: " + frame.headers["message"]);
  console.log("Additional details: " + frame.body);
};

stompClient.connect({}, function (frame) {
  console.log("Connected: " + frame);
  stompClient.subscribe("/topic/new-lobby", function (message) {
    let json = JSON.parse(message.body);
    showLobbies(json);
  });
});

function showLobbies(lobbyJson) {
  if (sub_first_msg_new_lobby) {
    sub_first_msg_new_lobby = false;
    for (let i = 0; i < lobbyJson.length; i++) {
      console.log(lobbyJson[i]);
    }
  } else {
    console.log("---- NEW LOBBY ----");
    console.log(lobbyJson);
  }
}
