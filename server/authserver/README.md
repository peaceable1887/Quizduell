# AuthServer

Micro-Service zum Registrieren, Einloggen, Bearbeiten des Profils. Stellt nach erfolgreicher Authentication ein JWT zur Verfügung, um mit den anderen Micro-Services zu interagieren.

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

        200 OK

- User-Details updaten

  - Endpunkt

        <host>:/api/auth/v1/update

  - request method
    - POST
  - request header
    - bearer token (JWT)
  - request body

        {
          "name": "<new-name>"
          "mail": "<new-mail>"
          "password": "<new-password>"
        }

  - response body

        {
          "name": "john",
          "mail": "john@test.de",
          "userId": "7cb353a2-c35d-4560-958a-a6ec6ceae50b"
        }

  - respones status
    200 OK

- Profilbild posten

  - Endpunkt

        <host>:/api/auth/v1/image

  - request method
    - POST
  - request header
    - bearer token (JWT)
  - requst boden

        {
          "file": "<picture>"
        }

  - respones status

        200 OK
