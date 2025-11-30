package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Water;
import entities.air.Air;
import entities.air.DesertAir;
import entities.air.MountainAir;
import entities.air.PolarAir;
import entities.air.TemperateAir;
import entities.air.TropicalAir;
import entities.animals.Animal;
import entities.plants.Plant;
import entities.soils.DesertSoil;
import entities.soils.ForestSoil;
import entities.soils.GrasslandSoil;
import entities.soils.Soil;
import entities.soils.SwampSoil;
import entities.soils.TundraSoil;

public final class CommandHelper {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private CommandHelper() {
    }

    /**
     * Creates a JSON node for soil entity.
     * @param soil The soil entity
     * @return ObjectNode representation
     */
    public static ObjectNode createSoilNode(final Soil soil) {
        ObjectNode node = MAPPER.createObjectNode();
        node.put("type", soil.getType());
        node.put("name", soil.getName());
        node.put("mass", soil.getMass());
        node.put("nitrogen", soil.getNitrogen());
        node.put("waterRetention", soil.getWaterRetention());
        node.put("soilpH", soil.getSoilpH());
        node.put("organicMatter", soil.getOrganicMatter());
        node.put("soilQuality", soil.calculateQuality());

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

    /**
     * Creates a JSON node for air entity.
     * @param air The air entity
     * @return ObjectNode representation
     */
    public static ObjectNode createAirNode(final Air air) {
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
                node.put("desertStorm", ((DesertAir) air).isDesertStorm());
                break;
            case "MountainAir":
                node.put("altitude", ((MountainAir) air).getAltitude());
                break;
            default:
                break;
        }

        return node;
    }

    /**
     * Creates a JSON node for plant entity.
     * @param plant The plant entity
     * @return ObjectNode representation
     */
    public static ObjectNode createPlantNode(final Plant plant) {
        ObjectNode node = MAPPER.createObjectNode();
        node.put("type", plant.getType());
        node.put("name", plant.getName());
        node.put("mass", plant.getMass());
        return node;
    }

    /**
     * Creates a JSON node for animal entity.
     * @param animal The animal entity
     * @return ObjectNode representation
     */
    public static ObjectNode createAnimalNode(final Animal animal) {
        ObjectNode node = MAPPER.createObjectNode();
        node.put("type", animal.getType());
        node.put("name", animal.getName());
        node.put("mass", animal.getMass());
        return node;
    }

    /**
     * Creates a JSON node for water entity.
     * @param water The water entity
     * @return ObjectNode representation
     */
    public static ObjectNode createWaterNode(final Water water) {
        ObjectNode node = MAPPER.createObjectNode();
        node.put("type", water.getType());
        node.put("name", water.getName());
        node.put("mass", water.getMass());
        return node;
    }
}
