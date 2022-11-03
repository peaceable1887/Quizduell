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

            {"name":"john", "password":"password"}

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

          eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJxdWl6ZHVlbGxfYXV0aHNlcnZlciIsInN1YiI6IjFkYjczOWZlLTYwNTQtNDU2Ny1iNDQzLTJlOGUzODgyMjA2OCIsImV4cCI6MTY2NzQxNTMxOSwiaWF0IjoxNjY3NDExNzE5LCJzY29wZSI6IiJ9.lT1O1QhQ0_kTIYbypLIrG94-Jw4Wo2hgIoYg0NrVCedHhfvsn0yihlU3v7JUnwD8Nk0i6_01f360h-ug61jlsxubJQLTCQ6Sq7U0k57sjYJA1oiyewdqhzPk5IIAbuwnU-sudRJenHxKTuZE_3MmWJf1YjByYwXs_Aydmts4Rp81XeMtUj0hbTtk7PtQPA6xlOZBuCmIfvX1FaNMSOh3qHacoAvdgIEv6ERiaYe1pgHDJusY-1DUHSTE2RZ4rNDHhrIKhijwsroYxUTeczzJ4-2O0EQjC5tik5mwIcufO2dvSUBtG-lyVgr2dI1AgY_O7A-JBFrZ-PxrsIlvZxsvGg

  - response status

            200 OK

- Endpunkt zum Testen des JWT

  - Endpunkt:

        <host>:/api/auth/v1/

  - request method
    - GET
  - request header
    - bearer token (JWT)

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
                "players": [
                    {
                        "userId": "7cb353a2-c35d-4560-958a-a6ec6ceae50b"
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
            "players": [
                {
                    "userId": "7cb353a2-c35d-4560-958a-a6ec6ceae50b"
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
                "players": [
                    {
                        "userId": "7cb353a2-c35d-4560-958a-a6ec6ceae50b"
                    }
                ]
            },
            {
                "id": "cc884343-578b-4c95-9ef3-541d311c8682",
                "name": "lobbyname",
                "players": [
                    {
                        "userId": "7cb353a2-c35d-4560-958a-a6ec6ceae50b"
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
            "players": [
                {
                    "userId": "7cb353a2-c35d-4560-958a-a6ec6ceae50b"
                },
                {
                    "userId": "1db739fe-6054-4567-b443-2e8e38822068"
                }
            ]
        }

  - response status

        200 OK

### Quiz

- \<dummy>
  - Endpunkt
  - request method
  - request header
  - request body
  - response body
  - response status
