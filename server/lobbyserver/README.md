# LobbyServer

## API Dokumentation

### Lobby

- Lobby erstellen

  - Endpunkt

          <host>:/api/lobby/v1/create

  - request method

    - POST

  - request header
    - bearer token (JWT)
  - request body

            {
              "name":"lobbyname",
              "password": "" (optional wenn kein Passwort dann leer "")
            }

  - response body

        {
            "id": "3d182c84-15e3-4eb9-8590-841e272388aa",
            "name": "lobbyname",
            "status": "WAIT",
            "hasPassword": "false",
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
  - URL Parameter

        ?lobbyId=<UUID>

  - response body

        {
            "id": "cc884343-578b-4c95-9ef3-541d311c8682",
            "name": "lobbyname",
            "status": "WAIT",
            "hasPassword": "false",
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
                "hasPassword": "false",
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
                "hasPassword": "true",
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

        {
          "lobbyId":"cc884343-578b-4c95-9ef3-541d311c8682",
          "password": "<password>" (wenn kein Passwort dann leer "")
        }

  - response body

        {
            "id": "cc884343-578b-4c95-9ef3-541d311c8682",
            "name": "TestLobby",
            "status": "WAIT",
            "hasPassword": "false",
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
