# basic.README #
A simple implementation of Connect-N gamming backend server. Some of salient features include:

* An N-connected board with size KXP, all of which can be configured via setup.
* The game is implemented to support Multi-player including a (dumb!) version of AI the player can play against to.
* Inherently, as the game works based on player and game ids, the game supports multisession as well!

# System reqirements
The system is completely built on top of the Java, so the entire dependencies are also related to it.
### Build configuration
The recommended configurations for building from the source:
* Oracle Java SDK >= 1.8.*
* Maven 3.2.*
* git 2.4.*

##### Downloading and Building

```
$ git clone https://bitbucket.org/tckb/connect4-glueck-games.git
$ cd connect4-glueck-games
$ mvn clean package
```
The above command will download the required dependencies and will create the jar file of the application in ```target``` folder incuding the documentation of the source.

##### Source documentation
While packaging, maven creates the java documentation of the project as well.
```
$ cd connect4-glueck-games
$ open target/apidocs/index.html
```
Incase of using windows, use ```start``` instead of _open_.

##### Runnning the application
The application has an inbuilt embedded server, so suffice to have only JRE (>=1.8.*).
```
$ cd connect4-glueck-games
$ java -jar target/connect4-<version>.jar
```
By default, the server runs on port ```8090``` . To change any project related configuration please see [application.properties] [app-props.link]. For more detailed configuration, please consult [spring-boot-application.properties](https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html)

##### Additional requirements
The project uses embedded mongodb for storing application related information, if you are curious to see the data yourself, you must install a mongodb-_compliant_ client. By default, the mongodb runs on port ```2003``` (see [property][app-props.link] file).

For eg:
```
mongo 127.0.0.1:2003/test
show collections
current_boards → 0.000MB / 0.008MB
expired_boards → 0.000MB / 0.008MB
```
Continued more @ [Rest.README](./README-rest.md).

[app-props.link]: ./src/main/resources/application.properties