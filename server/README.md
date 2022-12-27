# Ordner für die Server-Komponenten

## API Dokumentation

### Authentication

- Registrieren

  - Endpunkt:

        <host>:/api/auth/v1/register

  - request method

    - POST

  - request header

            -

  - request body

            {"name":"john", "mail":"john@test.de", "password":"password"}

  - response body

            -

  - response status

            201 Created

- Login/ JWT anfordern

  - Endpunkt:

        <host>:/api/auth/v1/token

  - request method

    - GET

  - request header
    - basic auth
      - username
      - password
  - request body

          -

  - response body (JWT Beispiel)

          {
            "token": "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJxdWl6ZHVlbGxfYXV0aHNlcnZlciIsInN1YiI6IjFkYjczOWZlLTYwNTQtNDU2Ny1iNDQzLTJlOGUzODgyMjA2OCIsImV4cCI6MTY2NzQxNTMxOSwiaWF0IjoxNjY3NDExNzE5LCJzY29wZSI6IiJ9.lT1O1QhQ0_kTIYbypLIrG94-Jw4Wo2hgIoYg0NrVCedHhfvsn0yihlU3v7JUnwD8Nk0i6_01f360h-ug61jlsxubJQLTCQ6Sq7U0k57sjYJA1oiyewdqhzPk5IIAbuwnU-sudRJenHxKTuZE_3MmWJf1YjByYwXs_Aydmts4Rp81XeMtUj0hbTtk7PtQPA6xlOZBuCmIfvX1FaNMSOh3qHacoAvdgIEv6ERiaYe1pgHDJusY-1DUHSTE2RZ4rNDHhrIKhijwsroYxUTeczzJ4-2O0EQjC5tik5mwIcufO2dvSUBtG-lyVgr2dI1AgY_O7A-JBFrZ-PxrsIlvZxsvGg",
            "userId": "7cb353a2-c35d-4560-958a-a6ec6ceae50b",
            "name": "john"
          }

  - response status

            200 OK

- Endpunkt zum Testen des JWT

  - Endpunkt:

        <host>:/api/auth/v1/

  - request method
    - GET
  - request header
    - bearer token (JWT)

- User-Details abrufen

  - Endpunkt

        <host>:/api/auth/v1/details

  - request method
    - GET
  - request header
    - bearer token (JWT)
  - response body

        {
          "name": "john",
          "mail": "john@test.de",
          "userId": "7cb353a2-c35d-4560-958a-a6ec6ceae50b"
        }

  - response status
    - 200 OK

### Lobby

- Lobby erstellen

  - Endpunkt

          <host>:/api/lobby/v1/create

  - request method

    - POST

  - request header
    - bearer token (JWT)
  - request body

            { "name":"lobbyname" }

  - response body

        {
            "id": "3d182c84-15e3-4eb9-8590-841e272388aa",
            "name": "lobbyname",
            "status": "WAIT",
            "players": [
                {
                    "userId": "7cb353a2-c35d-4560-958a-a6ec6ceae50b",
                    "name": "john",
                    "status": "wait"
                }
            ]
        }

  - response status

            201 Created

- Einzelne Lobby holen

  - Endpunkt

          <host>:/api/lobby/v1/get

  - request method
    - GET
  - request header
    - bearer token (JWT)
  - request body

        { "lobbyId":"cc884343-578b-4c95-9ef3-541d311c8682" }

  - response body

        {
            "id": "cc884343-578b-4c95-9ef3-541d311c8682",
            "name": "lobbyname",
            "status": "WAIT",
            "players": [
                {
                    "userId": "7cb353a2-c35d-4560-958a-a6ec6ceae50b",
                    "name": "john",
                    "status": "wait"
                }
            ]
        }

  - response status

        200 OK

- Alle Lobbies holen

  - Endpunkt

        <host>:/api/lobby/v1/all

  - request method
    - GET
  - request header
    - bearer token (JWT)
  - request body

        -

  - response body

        [
            {
                "id": "3d182c84-15e3-4eb9-8590-841e272388aa",
                "name": "lobbyname",
                "status": "WAIT",
                "players": [
                    {
                        "userId": "7cb353a2-c35d-4560-958a-a6ec6ceae50b",
                        "name": "john",
                        "status": "wait"
                    }
                ]
            },
            {
                "id": "cc884343-578b-4c95-9ef3-541d311c8682",
                "name": "lobbyname",
                "status": "WAIT",
                "players": [
                    {
                        "userId": "7cb353a2-c35d-4560-958a-a6ec6ceae50b",
                        "name": "john",
                        "status": "wait"
                    }
                ]
            }
        ]

  - response status

        200 OK

- Mit Lobby verbinden

  - Endpunkt

        <host>:/api/lobby/v1/connect

  - request method
    - POST
  - request header
    - bearer token (JWT)
  - request body

        { "lobbyId":"cc884343-578b-4c95-9ef3-541d311c8682" }

  - response body

        {
            "id": "cc884343-578b-4c95-9ef3-541d311c8682",
            "name": "TestLobby",
            "status": "WAIT",
            "players": [
                {
                    "userId": "7cb353a2-c35d-4560-958a-a6ec6ceae50b",
                    "name": "john",
                    "status": "wait"
                },
                {
                    "userId": "1db739fe-6054-4567-b443-2e8e38822068",
                    "name": "jane",
                    "status": "wait"
                }
            ]
        }

  - response status

        200 OK

- Disconnecten von Lobby

  - Endpunkt

        <host>:/api/lobby/v1/disconnect

  - request method
    - POST
  - request header
    - bearer token (JWT)
  - request body

        { "lobbyId":"cc884343-578b-4c95-9ef3-541d311c8682" }

  - response body

        -

  - response status

        200 OK

### Lobby STOMP Websockets

#### Im Header wird der JWT zur Authentication benötigt.

Beispiel:

    { Authorization: "Bearer <token>" }

#### Endpunkte

- Endpunkt zum Verbinden

      ws://<host>/lobby-websocket

- Endpunkt zum Abonnieren für neue Lobbies

      /topic/new-lobby

  - Message

        {
            "id": "cc884343-578b-4c95-9ef3-541d311c8682",
            "name": "TestLobby",
            "status": "WAIT",
            "players": [
                {
                    "userId": "7cb353a2-c35d-4560-958a-a6ec6ceae50b",
                    "name": "john",
                    "status": "wait"
                }
            ]
        }

- Endpunkt zum Abonnieren für Änderungen in einer Lobby

      /topic/lobby/<lobby-UUID>

  - Message

        {
            "id": "cc884343-578b-4c95-9ef3-541d311c8682",
            "name": "TestLobby",
            "status": "WAIT",
            "players": [
                {
                    "userId": "7cb353a2-c35d-4560-958a-a6ec6ceae50b",
                    "name": "john",
                    "status": "ready"
                },
                {
                    "userId": "1db739fe-6054-4567-b443-2e8e38822068",
                    "name": "jane",
                    "status": "wait"
                }
            ]
        }

- Endpunkt zum Senden für Status-Update des Spielers

      /app/lobby/<lobby-UUID>/status-player

  - Message

        {
          "status": "ready"
        }

        {
          "status": "wait"
        }

- Endpunkt zum Abonnieren, der über Start, Countdown und Abbruch informiert

      /topic/lobby/<lobby-UUID>/start-lobby

  - Message

        {
          "status": "start",
          "countdown": "3",
          "token": ""
        }

        {
          "status": "start",
          "countdown": "0",
          "token": "<JWT>"
        }

        {
          "status": "abort",
          "countdown": "3",
          "token": ""
        }

- Endpunkt zum Abonnieren für Lobbies die gelöscht werden

      /topic/lobby/delete-lobby

  - Message

        {
          "cc884343-578b-4c95-9ef3-541d311c8682"
        }

### Stats-Server

- Spieler-Statistik holen

  - Endpunkt

        <host>:/api/auth/v1/get

  request method

  - GET
    request header
  - bearer token (jwt)
    request body

        {
          "id":"cc884343-578b-4c95-9ef3-541d311c8682",
          "playerId":"7cb353a2-c35d-4560-958a-a6ec6ceae50b",
          "gameCount":"10",
          "gameWonCount":"4",
          "gameLossCount":"2",
          "gameDrawCount":"4"
        }

### Quiz

- Mit Quiz verbinden

  - Endpunkt

        <host>:/api/quiz/v1/connect

  - request method
    - POST
  - request header
    - bearer token (JWT)
  - request body

        {
          "gameToken":"<JWT>",
          "lobbyId":"cc884343-578b-4c95-9ef3-541d311c8682",
          "playerId":"7cb353a2-c35d-4560-958a-a6ec6ceae50b"
        }

  - response body

        {
            "id": "cb4aedc0-4a1c-44f0-a07c-d68d10151267",
            "lobbyId": "cc884343-578b-4c95-9ef3-541d311c8682",
            "players": [
                {
                    "userId": "7cb353a2-c35d-4560-958a-a6ec6ceae50b",
                    "name": "john"
                },
                {
                    "userId": "1db739fe-6054-4567-b443-2e8e38822068",
                    "name": "jane"
                }
            ]
        }

  - response status

        200 OK

- Quiz abbrechen

  - Endpunkt

        <host>:/api/quiz/v1/cancel

  - request method
    - POST
  - request header
    - bearer token (JWT)
  - request body

        { "lobbyId":"cc884343-578b-4c95-9ef3-541d311c8682" }

  - response body

        {
          "true"
        }

  - response status

        200 OK

- Einzelnes Quiz holen

  - Endpunkt

          <host>:/api/quiz/v1/get

  - request method
    - GET
  - request header
    - bearer token (JWT)
  - request body

        { "lobbyId":"cc884343-578b-4c95-9ef3-541d311c8682" }

  - response body

        {
            "id": "cb4aedc0-4a1c-44f0-a07c-d68d10151267",
            "lobbyId": "cc884343-578b-4c95-9ef3-541d311c8682",
            "players": [
                {
                    "userId": "7cb353a2-c35d-4560-958a-a6ec6ceae50b",
                    "name": "john"
                },
               {
                    "userId": "1db739fe-6054-4567-b443-2e8e38822068",
                    "name": "jane"
                }
            ]
        }

  - response status

        200 OK

- Quiz Session holen

  - Enpunkt

          <host>:/api/quiz/v1/get-session

  - request method
    -get
  - request header
    -bearer token (JWT)
  - request body

        { }

  - response body

        {
            "quizId": "1789d84d-14e4-4c5e-a4b9-4666ebf1c9db",
            "lobbyId": "19133db7-d7e1-415f-b689-449ca609175a",
            "quizStatus": "STARTED",
            "playerList": [
                  {
                    "userId": "764ef3e2-5ef6-4976-afa0-b5309fe2f66c",
                    "name": "john",
                    "status": "ready"
                  },
                  {
                    "userId": "e2b7e3e6-d635-477b-980d-88cfb60ef14d",
                    "name": "john",
                    "status": "ready"
                  }
              ],
              "roundList": [
                {
                  "roundStatus": "OPEN",
                  "maxRounds": 6,
                  "currentRound": 1,
                  "playerList": [
                      {
                        "playerId": "764ef3e2-5ef6-4976-afa0-b5309fe2f66c",
                        "name": "john",
                        "playerRoundStatus": "GUESS",
                        "chosenAnswer": 0
                      },
                      {
                        "playerId": "e2b7e3e6-d635-477b-980d-88cfb60ef14d",
                        "name": "john",
                        "playerRoundStatus": "GUESS",
                        "chosenAnswer": 0
                      }
                  ],
                  "categoryName": null,
                  "questionText": "Wie heiÃt der korrekte Datentyp fÃ¼r eine Zeichenkette?",
                  "answerOne": "string",
                  "answerTwo": "String",
                  "answerThree": "str",
                  "answerFour": "charchain",
                  "correctAnswer": 0
                }
            ]
        }

### Quiz STOMP Websockets

#### Im Header wird der JWT zur Authentication benötigt.

Beispiel:

    { Authorization: "Bearer <token>" }

#### Endpunkte

- Endpunkt zum Verbinden

      ws://<host>/quiz-websocket

- Endpunkt zum Abonnieren für Änderungen an einem Quiz

      /topic/quiz/<lobby-UUID>

  - Message

        {
            "id": "cb4aedc0-4a1c-44f0-a07c-d68d10151267",
            "lobbyId": "cc884343-578b-4c95-9ef3-541d311c8682",
            "players": [
                {
                    "userId": "7cb353a2-c35d-4560-958a-a6ec6ceae50b",
                    "name": "john",
                    "status": "ready"
                },
                {
                    "userId": "1db739fe-6054-4567-b443-2e8e38822068",
                    "name": "jane",
                    "status": "wait"
                }
            ]
        }

- Endpunkt zum Senden für Status-Update des Spielers

      /app/quiz/<lobby-UUID>/status-player

  - Message

        {
          "status": "ready"
        }

        {
          "status": "wait"
        }

- Endpunkt zum Abonnieren, der über Start, Countdown und Abbruch informiert

      /topic/quiz/<lobby-UUID>/start-quiz

  - Message

        {
          "status": "start",
          "countdown": "3",
        }

        {
          "status": "start",
          "countdown": "0",
        }

        {
          "status": "abort",
          "countdown": "3",
        }

- Endpunkt zum Abonnieren für Updates einer QuizSession

      /topic/quiz/session/<lobby-UUID>

  - Message

        {
            "roundStatus": "OPEN",
            "maxRounds": 6,
            "currentRound": 1,
            "playerList": [
                {
                "playerId": "771ecdf4-58ac-4e1e-a082-58c1589e081a",
                "name": "john",
                "playerRoundStatus": "GUESS",
                "chosenAnswer": 0
                },
                {
                "playerId": "684d0be6-1888-485b-af53-89b81b6dae22",
                "name": "jane",
                "playerRoundStatus": "GUESS",
                "chosenAnswer": 0
                }
            ],
            "categoryName": null,
            "questionText": "testText",
            "answerOne": "antwort1",
            "answerTwo": "antwort2",
            "answerThree": "antwort3",
            "answerFour": "antwort4",
            "correctAnswer": 0
        }

- Endpunkt zum Abonnieren für den Countdown einer Runde

      /topic/quiz/session/<lobby-UUID>/round-countdown

  - Message

        {
          "20"
        }

        {
          "19"
        }

- Endpunkt zum Senden einer Antwort auf eine Quizfrage

      /app/quiz/session/<lobby-UUID>/answer

  - Message

        {
          "1"
        }
