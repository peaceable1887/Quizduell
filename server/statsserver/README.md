# StatsServer

## API Dokumentation

### Stats-Server

- Spieler-Statistik holen

  - Endpunkt

        <host>:/api/stats/v1/get

  - request method
    - GET
  - request header
    - bearer token (jwt)
  - response body

        {
          "id":"cc884343-578b-4c95-9ef3-541d311c8682",
          "playerId":"7cb353a2-c35d-4560-958a-a6ec6ceae50b",
          "gameCount":"10",
          "gameWonCount":"4",
          "gameLossCount":"2",
          "gameDrawCount":"4"
        }
