# WebSockets

## Lobby

- Endpunkt zum Verbinden

      ws://<host>/lobby-websocket

- Endpunkt zum Abonnieren für neue Lobbies

      ws://<host>/topic/new-lobby

  - Message on Subscribe: Json Array mit allen Lobbies

  - Message

        {
            "id": "cc884343-578b-4c95-9ef3-541d311c8682",
            "name": "TestLobby",
            "players": [
                {
                    "userId": "7cb353a2-c35d-4560-958a-a6ec6ceae50b"
                }
            ]
        }

- Endpunkt zum Abonnieren für Änderungen in einer Lobby

      ws://<host>/topic/lobby/<lobby-UUID>

  - Message on Subscribe: aktueller State der abonnierten Lobby

  - Message

        {
            "id": "cc884343-578b-4c95-9ef3-541d311c8682",
            "name": "TestLobby",
            "players": [
                {
                    "userId": "7cb353a2-c35d-4560-958a-a6ec6ceae50b"
                    "status": "ready"
                },
                {
                    "userId": "1db739fe-6054-4567-b443-2e8e38822068"
                    "status": "wait"
                }
            ]
        }

- Endpunkt zum Mitteilen, dass der Spieler bereit ist, oder nicht mehr bereit ist

      ws://<host>/app/lobby/<lobby-UUID>/status

  - Message

        {
            "playerId": <UUID>
            "status": "ready"
        }

        {
            "playerId": <UUID>
            "status": "wait"
        }

- Endpunkt zum Abonnieren, der über den Start oder Abbruch eines Spiels informiert

      ws://<host>/topic/lobby/<lobby-UUID>/start

  - Message

        {
            "status": "start"
        }

        {
            "status": "abort"
        }

## Quiz

- Endpunkt zum Verbinden

      ws://<host>/quiz-websocket

- Endpunkt zum Senden einer Antwort

      ws://<host>/app/quiz/<quiz-UUID>/answer

  - Message

        {
            "answer": "2"
        }

- Endpunkt zum Abonnieren für Änderungen im Spiel

      ws://<host>/topic/quiz/<quiz-UUID>

  - Message

        {
            "id": "cc884343-578b-4c95-9ef3-541d311c8682",
            "round": "2",
            "timeLeft" "10",
            "players": [
                {
                    "userId": "7cb353a2-c35d-4560-958a-a6ec6ceae50b"
                    "status": "ready"
                },
                {
                    "userId": "1db739fe-6054-4567-b443-2e8e38822068"
                    "status": "wait"
                }
            ]
        }
