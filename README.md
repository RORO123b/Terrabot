# Tema 1 POO  - TerraBot

## Structura temei

Tema este alcatuita din 4 pachete care contin mai multe clase si subpachete:
- main -> contine clasa main
- commands -> contine comenzile necesare pentru rezolvarea temei
- entities -> contine toate clasele de entitati
  - animals -> contine toate clasele si subclasele animalelor
  - air -> contine toate clasele si subclasele aerului
  - plants -> contine toate clasele si subclasele plantelor
  - soil -> contine toate clasele si subclasele pamantului
  - Entity -> super clasa
  - Water -> subclasa la entity
- map -> contine toate clasele care tin de harta (harta, celula, robot)

## Rezolvarea temei

### Main
  - pentru fiecare comanda am creat o clasa anonima cu care contine comanda respectiva si o apelez pentru a face JSON output
  - ce mai este notabil este partea de start simulation in care initializez harta, robotul si incrementez indexul simularii

### Commands
  - am considerat sa creez o interfata numita Command care o implementeaza fiecare comanda pentru a putea fi executata
  - am creat o clasa CommandHelper care ma ajuta sa creez nodurile pentru JSON output
  
  - #### StartSimulation
    - initializez obiectul map punandu-i dimensiunile si adaugand in fiecare celula entitatile corespunzatoare
    - in metodele createAnimal, createPlant, createSoil folosesc switch case pe type pentru a crea tipul corespunzator
  
  - #### EndSimulation
    - afisez mesajul potrivit si fac boolean-ul de simulare activa fals
  
  - #### ChangeWeatherConditions
    - folosesc de un switch case pentru determinarea tipului de schimbare a vremii
    - am creat metoda changeWeather in cadrul clasei Map caruia ii dau **overload** pentru a putea schimba vremea celulelor potrivite
  
  - #### MoveRobot
    - am creat metoda moveRobot in cadrul clasei Robot pentru a face miscarea
    - ma folosesc de 2 vectori de directie pentru a gasi celula pe care robotul se va muta
  
  - #### RechargeBattery
    - am creat metoda rechargeBattery in cadrul clasei Robot care adauga energia si pune un timestamp pana la cat sa se incarce
    - verific la fiecare comanda daca robotul se mai incarca
  
  - #### ScanObject
    - in robot scanez obiectul, determin ce am scanat si il adaug in inventar
    - in map adaug entitatea scanata in una dintre cele 3 ArrayList (scannedPlants, scannedWaters, scannedAnimals) si pun timestamp-ul la care trebuie updatat
    - updateEntities:
      - el este updatat si pentru timestampurile intermediare care nu apar in input (folosind lastTimestamp)
      - pentru plante cauta planta ce trebuie sa creasca si updatez campurile celulei
      - similar apa
      - caut animalul, updatez celula cu organicMatter si il hranesc si apoi il mut
      - algoritmul pentru hranire este impartit in 2 subcazuri: pentru carnivori si paraziti, restul animalelor
      - el este implementat conform cerintei, iar pentru eliminarea entitatii mancate voi seta acea entitate cu null
      - algoritmul pentru miscarea animalului se foloseste de 2 vectori de directie pentru a determina celula potrivita
      - ele sunt updatate in functie de campul nextUpdate din cadrul fiecarui tip de entitate
  
  - #### LearnFact
    - iterez prin inventarul robotului si adaug in knowledgeBase
    - pentru implementarea knowledgeBaseului ma folosesc de un LinkedHashMap care are ca cheie component si ca valoare o lista de subiecte (stringuri)
    
  - #### ImproveEnvironment
    - robotul verifica daca are in knowledgeBase si inventar ce-i trebuie pentru a imbunatati celula pe care se afla si o face
  
  - #### getEnergyStatus
    - returneaza cata energie are robotul

  - #### PrintEnvConditions
    - pune in output campurile necesare, folosind CommandHelper
  
  - #### PrintMap
    - similar doar ca iterez prin harta
  
  - #### PrintKnowledgeBase
    - iterez prin knowledgeBase si afisez

## Folosirea LLM-urilor
- am utilizat LLM-urile pentru numirea unor variabile globale (nu eram prea inspirat la denumire) si a unor clase (Command si CommandHelper)
- la unul dintre teste uitasem sa calculez calitatea apei cand creez apa in constructor si a gasit el greseala

