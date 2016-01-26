# rest.README #
The default context path is ```/```

## Rest API documentation ##
An overview of the rest api is as follows:

api path  | method | description
------------- | -------------| -------------
/setup  | POST | Used for initial setup of the game *Must be the first call!*
/start?[multi_player=```multi_player:boolean``` |GET| Used for starting up the the game. *As an added advantage, the player will get the chance to make the first move.*
/join?session=```sessionId:String```| GET | Used for joining a game initiated by another player. *The player must use ```multi_player=true``` for allowing other players to join.*
/place_chip/```column:Int```/playerRef=```player_ref:String```&boardSessionId=```sessionId;String```| GET| Used for placing a chip on the board.
/stats | GET| Dumps the current stats of the game including current and expired game.


#### Rest-params



# Architecture 
![Alt text](./src/main/resources/arch.png)







