# TerraBot

## Project Structure

The project consists of 4 packages containing multiple classes and subpackages:
- main -> contains the main class
- commands -> contains the necessary commands for solving the assignment
- entities -> contains all entity classes
  - animals -> contains all animal classes and subclasses
  - air -> contains all air classes and subclasses
  - plants -> contains all plant classes and subclasses
  - soil -> contains all soil classes and subclasses
  - Entity -> super class
  - Water -> subclass of entity
- map -> contains all classes related to the map (map, cell, robot)

## Solution Implementation

### Main
  - for each command I created an anonymous class that contains the respective command and I call it to produce JSON output
  - what's also notable is the start simulation part where I initialize the map, the robot and increment the simulation index

### Commands
  - I decided to create an interface called Command which is implemented by each command to be executed
  - I created a CommandHelper class that helps me create nodes for JSON output
  
  - #### StartSimulation
    - I initialize the map object by setting its dimensions and adding the corresponding entities to each cell
    - in the createAnimal, createPlant, createSoil methods I use switch case on type to create the corresponding type
  
  - #### EndSimulation
    - I display the appropriate message and set the active simulation boolean to false
  
  - #### ChangeWeatherConditions
    - I use a switch case to determine the type of weather change
    - I created the changeWeather method in the Map class which I **overload** to be able to change the weather of the appropriate cells
  
  - #### MoveRobot
    - I created the moveRobot method in the Robot class to perform the movement
    - I use 2 direction vectors to find the cell the robot will move to
  
  - #### RechargeBattery
    - I created the rechargeBattery method in the Robot class which adds energy and sets a timestamp until when it should charge
    - I check at each command if the robot is still charging
  
  - #### ScanObject
    - in robot I scan the object, determine what I scanned and add it to inventory
    - in map I add the scanned entity to one of the 3 ArrayLists (scannedPlants, scannedWaters, scannedAnimals) and set the timestamp at which it should be updated
    - updateEntities:
      - it is updated also for intermediate timestamps that don't appear in input (using lastTimestamp)
      - for plants it searches for the plant that should grow and I update the cell fields
      - similar for water
      - I search for the animal, update the cell with organicMatter and feed it then move it
      - the feeding algorithm is divided into 2 subcases: for carnivores and parasites, the rest of the animals
      - it is implemented according to the requirements, and to remove the eaten entity I will set that entity to null
      - the algorithm for animal movement uses 2 direction vectors to determine the appropriate cell
      - they are updated based on the nextUpdate field within each entity type
  
  - #### LearnFact
    - I iterate through the robot's inventory and add to knowledgeBase
    - for the knowledgeBase implementation I use a LinkedHashMap which has component as key and a list of subjects (strings) as value
    
  - #### ImproveEnvironment
    - the robot checks if it has in knowledgeBase and inventory what it needs to improve the cell it's on and does it
  
  - #### getEnergyStatus
    - returns how much energy the robot has

  - #### PrintEnvConditions
    - puts the necessary fields in output, using CommandHelper
  
  - #### PrintMap
    - similar but I iterate through the map
  
  - #### PrintKnowledgeBase
    - I iterate through knowledgeBase and display
