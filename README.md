# ChineseCheckers
### About application
Application to play chinese checkers online with friends using ip connection.  

Project constists of two modules: Client and Server.  
Server is hosting game and defines number of players that will play.  
When game starts, server is responsible for all in-game events.  
Client is connecting player to server and sending events that user makes on his application window.  
For more information remember to check project documentation.  

### How to run application
1. Server Application  
To start server - build server module.  
! Remember to pass number of players that will be playing (2,3,4 or 6) as argument of command calling MainServer.  
Running server uses ip of host and random port. It is printed in console to inform host, which ip should players use to connect.

2. Client Application
To start client - build client module.  
When client starts, asks for ip to connect.  
Provide proper ip of running server and then wait for others to connect.  
When all players will join - game starts. Have fun!  

## Next phase of project
### Additions planned
a. Spring connection to database containing past games  
b. Option to run Client and watch game from past, not play new one
c. Website version of client to play in browser

### Possible future additions
a. Compiling modules into two packages with executables.  
b. Defining players by nicknames not ids

## Made by Krzysztof Tałałaj and Martyna Dziamara
