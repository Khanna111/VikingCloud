# Getting Started

### Compiling and Building the Application
Running it for the first time will be slower as dependencies would need to be downloaded. Please run the following command in the root of the project.
```
./gradlew clean assemble
```

### To Run the Application
```
 java -jar build/libs/horsetrack-0.0.1-SNAPSHOT.jar
```

#####  Console Output on Application Startup
```
$ java -jar build/libs/horsetrack-0.0.1-SNAPSHOT.jar             

Inventory:
$1,10
$5,10
$10,10
$20,10
$100,10
Horses:
$1,That Darn Gray Cat,5,Won
$2,Fort Utopia,10,Lost
$3,Count Sheep,9,Lost
$4,Ms Traitour,4,Lost
$5,Real Princess,3,Lost
$6,Pa Kettle,5,Lost
$7,Gin Stinger,6,Lost

```

### Salient Points
- DDD principals are utilized in architecting the solution.
  * The application is demarcated into "Application", "Domain" and "Infrastructure" layers.
  * Horse and Inventory data is initialized from source files. Moving it into a repository (database) requires changes to only the "Infrastructure" layer and writing an Respository Adapter implementation.
  * Similarly utilizing RPC to invoke the application requires a change again to only the "Infrastructure" layer and writing an RPC adapter such as a REST Controller.
  * Design principle such as SOLID are incorporated. 
- Gradle is used to build the application.
- Log file is `horsetrack.log` in the root folder.

### Discussion
- At the appropriate time, we can talk about the benefits of such Architecture and Design Principles.
- Further refinements and updates such as utilizing a database, concurrent user inputs can be introduced.
