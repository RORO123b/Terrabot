package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import fileio.InputLoader;
import fileio.CommandInput;
import fileio.SimulationInput;
import fileio.TerritorySectionParamsInput;
import fileio.SoilInput;
import fileio.PlantInput;
import fileio.AnimalInput;
import fileio.AirInput;
import fileio.WaterInput;
import fileio.PairInput;

import entities.soils.Soil;
import entities.soils.ForestSoil;
import entities.soils.SwampSoil;
import entities.soils.GrasslandSoil;
import entities.soils.TundraSoil;
import entities.soils.DesertSoil;

import entities.plants.Plant;
import entities.plants.FloweringPlants;
import entities.plants.Ferns;
import entities.plants.Mosses;
import entities.plants.Algae;
import entities.plants.GymnospermsPlants;

import entities.animals.Animal;
import entities.animals.Herbivore;
import entities.animals.Carnivore;
import entities.animals.Detritivore;
import entities.animals.Omnivore;
import entities.animals.Parasite;

import entities.Water;

import entities.air.Air;
import entities.air.TemperateAir;
import entities.air.TropicalAir;
import entities.air.PolarAir;
import entities.air.DesertAir;
import entities.air.MountainAir;

import map.Map;
import map.Cell;
import map.Robot;

import java.io.File;
import java.io.IOException;

/**
 * The entry point to this homework. It runs the checker that tests your implementation.
 */
public final class Main {

    private Main() {
    }

    private static final ObjectMapper MAPPER = new ObjectMapper();
    public static final ObjectWriter WRITER = MAPPER.writer().withDefaultPrettyPrinter();

    /**
     * @param inputPath input file path
     * @param outputPath output file path
     * @throws IOException when files cannot be loaded.
     */
    public static void action(final String inputPath,
                              final String outputPath) throws IOException {

        InputLoader inputLoader = new InputLoader(inputPath);
        ArrayNode output = MAPPER.createArrayNode();

        Map map = null;
        Robot robot = null;
        boolean simulationActive = false;
        int currentSimulationIndex = -1;

        for (CommandInput command : inputLoader.getCommands()) {
            ObjectNode commandNode = MAPPER.createObjectNode();
            commandNode.put("command", command.getCommand());

            switch (command.getCommand()) {
                case "startSimulation":
                    currentSimulationIndex++;
                    if (currentSimulationIndex < inputLoader.getSimulations().size()) {
                        SimulationInput sim = inputLoader
                                .getSimulations()
                                .get(currentSimulationIndex);

                        String[] dims = sim.getTerritoryDim().split("x");
                        int width = Integer.parseInt(dims[0]);
                        int height = Integer.parseInt(dims[1]);

                        map = new Map(height, width);
                        robot = new Robot(sim.getEnergyPoints());

                        initializeMap(map, sim.getTerritorySectionParams());

                        simulationActive = true;
                        commandNode.put("message", "Simulation has started.");
                    }
                    commandNode.put("timestamp", command.getTimestamp());
                    break;

                case "printEnvConditions":
                    Robot.checkBatteryCharging(command.getTimestamp());
                    if (simulationActive && map != null && robot != null
                            && !robot.getIsCharging()) {
                        Cell currentCell = map.getCell(robot.getX(), robot.getY());
                        ObjectNode envOutput = MAPPER.createObjectNode();

                        if (currentCell.getSoil() != null) {
                            envOutput.set("soil", createSoilNode(currentCell.getSoil()));
                        }
                        if (currentCell.getPlant() != null) {
                            envOutput.set("plants",
                                    createPlantNode(currentCell.getPlant()));
                        }
                        if (currentCell.getAnimal() != null) {
                            envOutput.set("animals", createAnimalNode(currentCell.getAnimal()));
                        }
                        if (currentCell.getWater() != null) {
                            envOutput.set("water", createWaterNode(currentCell.getWater()));
                        }
                        if (currentCell.getAir() != null) {
                            envOutput.set("air", createAirNode(currentCell.getAir()));
                        }

                        commandNode.set("output", envOutput);
                    } else if (robot != null && robot.getIsCharging()) {
                        commandNode.put("message",
                                "ERROR: Robot still charging. Cannot perform action");
                    } else {
                        commandNode.put("message",
                                "ERROR: Simulation not started. Cannot perform action");
                    }
                    commandNode.put("timestamp", command.getTimestamp());
                    break;

                case "printMap":
                    Robot.checkBatteryCharging(command.getTimestamp());
                    if (simulationActive && map != null && robot != null
                            && !robot.getIsCharging()) {
                        ArrayNode mapOutput = MAPPER.createArrayNode();

                        for (int y = 0; y < map.getHeight(); y++) {
                            for (int x = 0; x < map.getWidth(); x++) {
                                Cell cell = map.getCell(x, y);
                                ObjectNode cellNode = MAPPER.createObjectNode();

                                ArrayNode sectionArray = MAPPER.createArrayNode();
                                sectionArray.add(x);
                                sectionArray.add(y);
                                cellNode.set("section", sectionArray);

                                cellNode.put("totalNrOfObjects", cell.totalObjects());
                                cellNode.put("airQuality", cell.getAirQuality());
                                cellNode.put("soilQuality", cell.getSoilQuality());

                                mapOutput.add(cellNode);
                            }
                        }
                        Robot.checkBatteryCharging(command.getTimestamp());

                        commandNode.set("output", mapOutput);
                    } else if (robot != null && robot.getIsCharging()) {
                        commandNode.put("message",
                                "ERROR: Robot still charging. Cannot perform action");
                    } else {
                        commandNode.put("message",
                                "ERROR: Simulation not started. Cannot perform action");
                    }
                    commandNode.put("timestamp", command.getTimestamp());
                    break;

                case "endSimulation":
                    simulationActive = false;
                    commandNode.put("message", "Simulation has ended.");
                    commandNode.put("timestamp", command.getTimestamp());
                    break;

                case "moveRobot":
                    Robot.checkBatteryCharging(command.getTimestamp());
                    if (simulationActive && map != null && robot != null
                            && !robot.getIsCharging()) {
                        String moveResult = robot.moveRobot(map);
                        commandNode.put("message", moveResult);
                    } else if (robot != null && robot.getIsCharging()) {
                        commandNode.put("message",
                                "ERROR: Robot still charging. Cannot perform action");
                    } else {
                        commandNode.put("message",
                                "ERROR: Simulation not started. Cannot perform action");
                    }
                    commandNode.put("timestamp", command.getTimestamp());
                    break;

                case "rechargeBattery":
                    Robot.checkBatteryCharging(command.getTimestamp());
                    if (simulationActive && map != null && robot != null
                            && !robot.getIsCharging()) {
                        robot.rechargeBattery(command.getTimeToCharge(), command.getTimestamp());
                        commandNode.put("message", "Robot battery is charging.");
                    } else if (robot != null && robot.getIsCharging()) {
                        commandNode.put("message",
                                "ERROR: Robot still charging. Cannot perform action");
                    } else {
                        commandNode.put("message",
                                "ERROR: Simulation not started. Cannot perform action");
                    }
                    commandNode.put("timestamp", command.getTimestamp());
                break;

                case "getEnergyStatus":
                    Robot.checkBatteryCharging(command.getTimestamp());
                    if (simulationActive && map != null && robot != null
                            && !robot.getIsCharging()) {
                        commandNode.put("message",
                                "TerraBot has " + robot.getEnergyPoints() + " energy points left.");
                    } else if (robot != null && robot.getIsCharging()) {
                        commandNode.put("message",
                                "ERROR: Robot still charging. Cannot perform action");
                    } else {
                        commandNode.put("message",
                                "ERROR: Simulation not started. Cannot perform action");
                    }
                    commandNode.put("timestamp", command.getTimestamp());
                    break;
                default:
                    commandNode.put("timestamp", command.getTimestamp());
                    break;

            }

            output.add(commandNode);
        }

        File outputFile = new File(outputPath);
        outputFile.getParentFile().mkdirs();
        WRITER.writeValue(outputFile, output);
    }

    private static void initializeMap(final Map map, final TerritorySectionParamsInput params) {

        if (params.getSoil() != null) {
            for (SoilInput soilInput : params.getSoil()) {
                if (soilInput.getSections() != null) {
                    for (PairInput section : soilInput.getSections()) {
                        Soil soil = createSoil(soilInput);
                        if (soil != null) {
                            Cell cell = map.getCell(section.getX(), section.getY());
                            if (cell != null) {
                                cell.setSoil(soil);
                            }
                        }
                    }
                }
            }
        }

        if (params.getAir() != null) {
            for (AirInput airInput : params.getAir()) {
                if (airInput.getSections() != null) {
                    for (PairInput section : airInput.getSections()) {
                        Air air = createAir(airInput);
                        if (air != null) {
                            Cell cell = map.getCell(section.getX(), section.getY());
                            if (cell != null) {
                                cell.setAir(air);
                            }
                        }
                    }
                }
            }
        }

        if (params.getPlants() != null) {
            for (PlantInput plantInput : params.getPlants()) {
                if (plantInput.getSections() != null) {
                    for (PairInput section : plantInput.getSections()) {
                        Plant plant = createPlant(plantInput);
                        if (plant != null) {
                            Cell cell = map.getCell(section.getX(), section.getY());
                            if (cell != null) {
                                cell.setPlant(plant);
                            }
                        }
                    }
                }
            }
        }

        if (params.getAnimals() != null) {
            for (AnimalInput animalInput : params.getAnimals()) {
                if (animalInput.getSections() != null) {
                    for (PairInput section : animalInput.getSections()) {
                        Animal animal = createAnimal(animalInput);
                        if (animal != null) {
                            Cell cell = map.getCell(section.getX(), section.getY());
                            if (cell != null) {
                                cell.setAnimal(animal);
                            }
                        }
                    }
                }
            }
        }

        if (params.getWater() != null) {
            for (WaterInput waterInput : params.getWater()) {
                if (waterInput.getSections() != null) {
                    for (PairInput section : waterInput.getSections()) {
                        Water water = createWater(waterInput);
                        if (water != null) {
                            Cell cell = map.getCell(section.getX(), section.getY());
                            if (cell != null) {
                                cell.setWater(water);
                            }
                        }
                    }
                }
            }
        }
    }

    private static Soil createSoil(final SoilInput input) {
        Soil soil = null;
        switch (input.getType()) {
            case "ForestSoil":
                soil = new ForestSoil();
                if (input.getLeafLitter() != 0) {
                    ((ForestSoil) soil).setLeafLitter(input.getLeafLitter());
                }
                break;
            case "SwampSoil":
                soil = new SwampSoil();
                if (input.getWaterLogging() != 0) {
                    ((SwampSoil) soil).setWaterLogging(input.getWaterLogging());
                }
                break;
            case "GrasslandSoil":
                soil = new GrasslandSoil();
                if (input.getRootDensity() != 0) {
                    ((GrasslandSoil) soil).setRootDensity(input.getRootDensity());
                }
                break;
            case "TundraSoil":
                soil = new TundraSoil();
                if (input.getPermafrostDepth() != 0) {
                    ((TundraSoil) soil).setPermafrostDepth(input.getPermafrostDepth());
                }
                break;
            case "DesertSoil":
                soil = new DesertSoil();
                if (input.getSalinity() != 0) {
                    ((DesertSoil) soil).setSalinity(input.getSalinity());
                }
                break;
            default:
                break;
        }

        if (soil != null) {
            soil.setType(input.getType());
            soil.setName(input.getName());
            soil.setMass(input.getMass());
            soil.setNitrogen(input.getNitrogen());
            soil.setWaterRetention(input.getWaterRetention());
            soil.setSoilpH(input.getSoilpH());
            soil.setOrganicMatter(input.getOrganicMatter());
            soil.calculateFinalResult();
        }

        return soil;
    }

    private static Air createAir(final AirInput input) {
        Air air = null;
        switch (input.getType()) {
            case "TemperateAir":
                air = new TemperateAir();
                ((TemperateAir) air).setPollenLevel(input.getPollenLevel());
                break;
            case "TropicalAir":
                air = new TropicalAir();
                ((TropicalAir) air).setCo2Level(input.getCo2Level());
                break;
            case "PolarAir":
                air = new PolarAir();
                ((PolarAir) air).setIceCrystalConcentration(input.getIceCrystalConcentration());
                break;
            case "DesertAir":
                air = new DesertAir();
                ((DesertAir) air).setDustParticles(input.getDustParticles());
                break;
            case "MountainAir":
                air = new MountainAir();
                ((MountainAir) air).setAltitude(input.getAltitude());
                break;
            default:
                break;
        }

        if (air != null) {
            air.setType(input.getType());
            air.setName(input.getName());
            air.setMass(input.getMass());
            air.setHumidity(input.getHumidity());
            air.setTemperature(input.getTemperature());
            air.setOxygenLevel(input.getOxygenLevel());
            air.setAirQuality();
            air.calculateToxicityAQ();
        }

        return air;
    }

    private static Plant createPlant(final PlantInput input) {
        Plant plant = null;
        switch (input.getType()) {
            case "FloweringPlants":
                plant = new FloweringPlants(input.getName());
                break;
            case "Ferns":
                plant = new Ferns(input.getName());
                break;
            case "Mosses":
                plant = new Mosses(input.getName());
                break;
            case "Algae":
                plant = new Algae(input.getName());
                break;
            case "GymnospermsPlants":
                plant = new GymnospermsPlants(input.getName());
                break;
            default:
                break;
        }

        if (plant != null) {
            plant.setType(input.getType());
            plant.setMass(input.getMass());
        }

        return plant;
    }

    private static Animal createAnimal(final AnimalInput input) {
        Animal animal = null;
        switch (input.getType()) {
            case "Herbivores":
                animal = new Herbivore(input.getName(), (int) input.getMass());
                animal.setMass(input.getMass());
                break;
            case "Carnivores":
                animal = new Carnivore(input.getName(), (int) input.getMass());
                animal.setMass(input.getMass());
                break;
            case "Omnivores":
                animal = new Omnivore(input.getName(), (int) input.getMass());
                animal.setMass(input.getMass());
                break;
            case "Detritivores":
                animal = new Detritivore(input.getName(), (int) input.getMass());
                animal.setMass(input.getMass());
                break;
            case "Parasites":
                animal = new Parasite(input.getName(), (int) input.getMass());
                animal.setMass(input.getMass());
                break;
            default:
                break;
        }

        if (animal != null) {
            animal.setType(input.getType());
        }

        return animal;
    }

    private static Water createWater(final WaterInput input) {
        Water water = new Water();
        water.setType(input.getType());
        water.setName(input.getName());
        water.setMass(input.getMass());
        return water;
    }

    private static ObjectNode createSoilNode(final Soil soil) {
        ObjectNode node = MAPPER.createObjectNode();
        node.put("type", soil.getType());
        node.put("name", soil.getName());
        node.put("mass", soil.getMass());
        node.put("nitrogen", soil.getNitrogen());
        node.put("waterRetention", soil.getWaterRetention());
        node.put("soilpH", soil.getSoilpH());
        node.put("organicMatter", soil.getOrganicMatter());
        node.put("soilQuality", soil.getFinalResult());

        switch (soil.getType()) {
            case "ForestSoil":
                node.put("leafLitter", ((ForestSoil) soil).getLeafLitter());
                break;
            case "SwampSoil":
                node.put("waterLogging", ((SwampSoil) soil).getWaterLogging());
                break;
            case "GrasslandSoil":
                node.put("rootDensity", ((GrasslandSoil) soil).getRootDensity());
                break;
            case "TundraSoil":
                node.put("permafrostDepth", ((TundraSoil) soil).getPermafrostDepth());
                break;
            case "DesertSoil":
                node.put("salinity", ((DesertSoil) soil).getSalinity());
                break;
            default:
                break;
        }

        return node;
    }

    private static ObjectNode createAirNode(final Air air) {
        ObjectNode node = MAPPER.createObjectNode();
        node.put("type", air.getType());
        node.put("name", air.getName());
        node.put("mass", air.getMass());
        node.put("humidity", air.getHumidity());
        node.put("temperature", air.getTemperature());
        node.put("oxygenLevel", air.getOxygenLevel());
        node.put("airQuality", air.getAirQuality());

        switch (air.getType()) {
            case "TemperateAir":
                node.put("pollenLevel", ((TemperateAir) air).getPollenLevel());
                break;
            case "TropicalAir":
                node.put("co2Level", ((TropicalAir) air).getCo2Level());
                break;
            case "PolarAir":
                node.put("iceCrystalConcentration", ((PolarAir) air).getIceCrystalConcentration());
                break;
            case "DesertAir":
                node.put("dustParticles", ((DesertAir) air).getDustParticles());
                break;
            case "MountainAir":
                node.put("altitude", ((MountainAir) air).getAltitude());
                break;
            default:
                break;
        }

        return node;
    }

    private static ObjectNode createPlantNode(final Plant plant) {
        ObjectNode node = MAPPER.createObjectNode();
        node.put("type", plant.getType());
        node.put("name", plant.getName());
        node.put("mass", plant.getMass());
        return node;
    }

    private static ObjectNode createAnimalNode(final Animal animal) {
        ObjectNode node = MAPPER.createObjectNode();
        node.put("type", animal.getType());
        node.put("name", animal.getName());
        node.put("mass", animal.getMass());
        return node;
    }

    private static ObjectNode createWaterNode(final Water water) {
        ObjectNode node = MAPPER.createObjectNode();
        node.put("type", water.getType());
        node.put("name", water.getName());
        node.put("mass", water.getMass());
        return node;
    }
}

