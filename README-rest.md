## Rest API documentation ##
An overview of the rest api is as follows:

api path  | method | description
------------- | -------------| -------------
/setup  | POST | Content Cell
/start?[multi_player=] |GET| Content Cell
/join?session= | GET | Content Cell
/place_chip/{column}/playerRef=&boardSessionId=| GET| Content Cell
/stats | GET| Content Cell



# Architecture 
![Alt text](./src/main/resources/arch.png)


