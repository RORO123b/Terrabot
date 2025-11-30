package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.AirInput;
import fileio.AnimalInput;
import fileio.CommandInput;
import fileio.PairInput;
import fileio.PlantInput;
import fileio.SimulationInput;
import fileio.SoilInput;
import fileio.TerritorySectionParamsInput;
import fileio.WaterInput;
import lombok.Getter;
import lombok.Setter;
import map.Cell;
import map.Map;
import map.Robot;
import entities.Water;
import entities.air.Air;
import entities.air.DesertAir;
import entities.air.MountainAir;
import entities.air.PolarAir;
import entities.air.TemperateAir;
import entities.air.TropicalAir;
import entities.animals.Animal;
import entities.animals.Carnivore;
import entities.animals.Detritivore;
import entities.animals.Herbivore;
import entities.animals.Omnivore;
import entities.animals.Parasite;
import entities.plants.Algae;
import entities.plants.Ferns;
import entities.plants.FloweringPlants;
import entities.plants.GymnospermsPlants;
import entities.plants.Mosses;
import entities.plants.Plant;
import entities.soils.DesertSoil;
import entities.soils.ForestSoil;
import entities.soils.GrasslandSoil;
import entities.soils.Soil;
import entities.soils.SwampSoil;
import entities.soils.TundraSoil;

@Getter
@Setter

public class StartSimulation implements Command {
    private SimulationInput simulation;
    private Map map;
    private Robot robot;

    public StartSimulation(final SimulationInput simulation) {
        this.simulation = simulation;
    }

    @Override
    public final ObjectNode executeCommand(final ObjectMapper mapper, final CommandInput command,
                                    final Map mapParam, final Robot robotParam,
                                    final boolean simulationActive, final int lastTimestamp) {
        ObjectNode commandNode = mapper.createObjectNode();
        commandNode.put("command", command.getCommand());

        if (simulationActive) {
            commandNode.put("message",
                    "ERROR: Simulation already started. Cannot perform action");
        } else if (simulation != null) {
            String[] dims = simulation.getTerritoryDim().split("x");
            int width = Integer.parseInt(dims[0]);
            int height = Integer.parseInt(dims[1]);

            this.map = new Map(height, width);
            this.robot = new Robot(simulation.getEnergyPoints());

            initializeMap(this.map, simulation.getTerritorySectionParams());
            commandNode.put("message", "Simulation has started.");
        }
        commandNode.put("timestamp", command.getTimestamp());
        return commandNode;
    }

    private void initializeMap(final Map gameMap, final TerritorySectionParamsInput params) {
        if (params.getSoil() != null) {
            for (SoilInput soilInput : params.getSoil()) {
                if (soilInput.getSections() != null) {
                    for (PairInput section : soilInput.getSections()) {
                        Soil soil = createSoil(soilInput);
                        if (soil != null) {
                            Cell cell = gameMap.getCell(section.getX(), section.getY());
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
                            Cell cell = gameMap.getCell(section.getX(), section.getY());
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
                            Cell cell = gameMap.getCell(section.getX(), section.getY());
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
                            Cell cell = gameMap.getCell(section.getX(), section.getY());
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
                        Water water = new Water(waterInput);
                        if (water != null) {
                            Cell cell = gameMap.getCell(section.getX(), section.getY());
                            if (cell != null) {
                                cell.setWater(water);
                            }
                        }
                    }
                }
            }
        }
    }

    private Soil createSoil(final SoilInput input) {
        Soil soil = null;
        switch (input.getType()) {
            case "ForestSoil":
                soil = new ForestSoil(input.getType(), input.getName(), input.getMass(),
                        input.getNitrogen(), input.getWaterRetention(), input.getSoilpH(),
                        input.getOrganicMatter(), input.getLeafLitter());
                break;
            case "SwampSoil":
                soil = new SwampSoil(input.getType(), input.getName(), input.getMass(),
                        input.getNitrogen(), input.getWaterRetention(), input.getSoilpH(),
                        input.getOrganicMatter(), input.getWaterLogging());
                break;
            case "GrasslandSoil":
                soil = new GrasslandSoil(input.getType(), input.getName(), input.getMass(),
                        input.getNitrogen(), input.getWaterRetention(), input.getSoilpH(),
                        input.getOrganicMatter(), input.getRootDensity());
                break;
            case "TundraSoil":
                soil = new TundraSoil(input.getType(), input.getName(), input.getMass(),
                        input.getNitrogen(), input.getWaterRetention(), input.getSoilpH(),
                        input.getOrganicMatter(), input.getPermafrostDepth());
                break;
            case "DesertSoil":
                soil = new DesertSoil(input.getType(), input.getName(), input.getMass(),
                        input.getNitrogen(), input.getWaterRetention(), input.getSoilpH(),
                        input.getOrganicMatter(), input.getSalinity());
                break;
            default:
                break;
        }

        return soil;
    }

    private Air createAir(final AirInput input) {
        Air air = null;
        switch (input.getType()) {
            case "TemperateAir":
                air = new TemperateAir(input.getType(), input.getName(), input.getMass(),
                        input.getHumidity(), input.getTemperature(), input.getOxygenLevel(),
                        input.getPollenLevel());
                break;
            case "TropicalAir":
                air = new TropicalAir(input.getType(), input.getName(), input.getMass(),
                        input.getHumidity(), input.getTemperature(), input.getOxygenLevel(),
                        input.getCo2Level());
                break;
            case "PolarAir":
                air = new PolarAir(input.getType(), input.getName(), input.getMass(),
                        input.getHumidity(), input.getTemperature(), input.getOxygenLevel(),
                        input.getIceCrystalConcentration());
                break;
            case "DesertAir":
                air = new DesertAir(input.getType(), input.getName(), input.getMass(),
                        input.getHumidity(), input.getTemperature(), input.getOxygenLevel(),
                        input.getDustParticles());
                break;
            case "MountainAir":
                air = new MountainAir(input.getType(), input.getName(), input.getMass(),
                        input.getHumidity(), input.getTemperature(), input.getOxygenLevel(),
                        input.getAltitude());
                break;
            default:
                break;
        }

        return air;
    }

    private Plant createPlant(final PlantInput input) {
        Plant plant = null;
        switch (input.getType()) {
            case "FloweringPlants":
                plant = new FloweringPlants(input.getName(), input.getMass());
                break;
            case "Ferns":
                plant = new Ferns(input.getName(), input.getMass());
                break;
            case "Mosses":
                plant = new Mosses(input.getName(), input.getMass());
                break;
            case "Algae":
                plant = new Algae(input.getName(), input.getMass());
                break;
            case "GymnospermsPlants":
                plant = new GymnospermsPlants(input.getName(), input.getMass());
                break;
            default:
                break;
        }

        return plant;
    }

    private Animal createAnimal(final AnimalInput input) {
        Animal animal = null;
        switch (input.getType()) {
            case "Herbivores":
                animal = new Herbivore(input.getName(), input.getMass());
                break;
            case "Carnivores":
                animal = new Carnivore(input.getName(), input.getMass());
                break;
            case "Omnivores":
                animal = new Omnivore(input.getName(), input.getMass());
                break;
            case "Detritivores":
                animal = new Detritivore(input.getName(), input.getMass());
                break;
            case "Parasites":
                animal = new Parasite(input.getName(), input.getMass());
                break;
            default:
                break;
        }

        return animal;
    }
}
