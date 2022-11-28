import SockJS from "sockjs-client";
import Stomp from "stompjs";

const token =
  "Bearer eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJxdWl6ZHVlbGxfYXV0aHNlcnZlciIsInN1YiI6Ijk2YmRiMTUzLWMwYWMtNGE2ZS04YzE3LThhNzEwODNlMTFmMyIsImV4cCI6MTY2OTY2MTU0NiwiaWF0IjoxNjY5NjU3OTQ2LCJzY29wZSI6IiJ9.AFh1W7dNTzerSllc4L_yLTPfEPRXX_rdeJDLhwlTts_VFkH4V6aloIvftsJwqSvgfHmK0LJyealHu914KMmlcS4WVCQ3VrT-7sm4pHK8pdwduC6NLRZ7rBqzHvThjpdprKyWAkNvEZVB2uWirDX1hcDjWLN28OxF6LCfGbVST6uhjjLnWaa-6BCdlRZiRlHFCBkPUQYV6OMOrgfhi-ZD21qVhhXenul2NchH0m94pA_i60xA4Xwpc-CAhQULiq1zQo4_a15eD7Y6MOvPhQBnUAlJFModBrMJXxoHTapd6Fl8-1LfAT84ehaqc1gC_PXTbGSiI2HnZrETUCcW-mhX-A";
const msg = "Hello World";
const httpURL = "https://test.burmeister.hamburg/lobby-websocket";
//const httpURL = "http://localhost:8080/lobby-websocket";
console.log(msg);

const socket = new SockJS(httpURL);
const stompClient = Stomp.over(socket);
let sub_first_msg_new_lobby = true;

stompClient.connect({ Authorization: token }, function (frame) {
  console.log("Connected: " + frame);
  stompClient.subscribe("/topic/new-lobby", function (message) {
    let json = JSON.parse(message.body);
    showLobbies(json);
  });

  stompClient.subscribe("/topic/test", function (message) {
    showTest(message.body);
  });

  stompClient.send("/app/test", {}, "test");
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

function showTest(msg) {
  console.log(msg);
}
