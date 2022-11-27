import SockJS from "sockjs-client";
import Stomp from "stompjs";

const token =
  "Bearer eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJxdWl6ZHVlbGxfYXV0aHNlcnZlciIsInN1YiI6IjM2MDkwNTU3LWMzYjgtNDI1Mi1hM2NkLWRlMWZlNmI4NmEzYyIsImV4cCI6MTY2OTU0NzkxMCwiaWF0IjoxNjY5NTQ0MzEwLCJzY29wZSI6IiJ9.A00v7PmHPse3SI0jS83h3nMw1oX4ApumNgzwivW-7MtyFhlmN3LdAZFDALI1oVKbOblrQUxO0PlxfuzYrrcF_iwQbL-Xb6K_3yjQoPnlspbeCliw_oNERQPaZMcUeN0g8LrhKacTYB_PTbYl5HsFaFXeSG9SfqH0xe5W9VwJnBSLUpnlaPtQETT35Kj8l58I9FgmZldnjlHHzbaVAytFx0tB9cPwoPJJ_3Xs6JuIvto_inKG8F33QC7VWLwsl2t8J1idbQB5u1wbV3Vq7CyIHOlTsYvjp-Ui4A_Fho2fqqjWpfeSYsc8_TuPjff5dRUAynCgZoMJ6lo0_yLF0kz-IQ";
const msg = "Hello World";
// const httpURL = "https://test.burmeister.hamburg/lobby-websocket";
const httpURL = "http://localhost:8080/lobby-websocket";
console.log(msg);

const socket = new SockJS(httpURL);
const stompClient = Stomp.over(socket);
let sub_first_msg_new_lobby = true;

stompClient.onConnect = function (frame) {
  console.log("connected!");
};

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
