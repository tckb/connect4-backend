## Rest API documentation ##
An overview of the rest api is as follows:

api path  | method | description
------------- | -------------| -------------
/setup  | POST | Content Cell
/start?[multi_player=```true|false```] |GET| Content Cell
/join?session=```sessionId```| GET | Content Cell
/place_chip/{column}/playerRef=```player_ref```&boardSessionId=```sessionId```| GET| Content Cell
/stats | GET| Content Cell



# Architecture 
![Alt text](./src/main/resources/arch.png)


