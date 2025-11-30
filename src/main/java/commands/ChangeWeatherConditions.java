package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import map.Map;
import map.Robot;

public final class ChangeWeatherConditions implements Command {
    @Override
    public ObjectNode executeCommand(final ObjectMapper mapper, final CommandInput command,
                                    final Map map, final Robot robot,
                                    final boolean simulationActive, final int lastTimestamp) {
        ObjectNode commandNode = mapper.createObjectNode();
        commandNode.put("command", command.getCommand());

        if (simulationActive && map != null) {
            map.checkWeatherFinished(command.getTimestamp());
            map.updateEntities(robot, command, lastTimestamp);
            String type = command.getType();
            double value = 0;
            String season = null;
            String airType = null;

            switch (type) {
                case "desertStorm":
                    airType = "DesertAir";
                    map.changeWeather(airType, true, command.getTimestamp());
                    break;
                case "peopleHiking":
                    airType = "MountainAir";
                    value = command.getNumberOfHikers();
                    map.changeWeather(airType, value, command.getTimestamp());
                    break;
                case "newSeason":
                    airType = "TemperateAir";
                    season = command.getSeason();
                    map.changeWeather(airType, season, command.getTimestamp());
                    break;
                case "polarStorm":
                    airType = "PolarAir";
                    value = command.getWindSpeed();
                    map.changeWeather(airType, value, command.getTimestamp());
                    break;
                case "rainfall":
                    airType = "TropicalAir";
                    value = command.getRainfall();
                    map.changeWeather(airType, value, command.getTimestamp());
                    break;
                default:
                    break;
            }
            if (map.hasTypeOfWeather(airType)) {
                commandNode.put("message", "The weather has changed.");
            } else {
                commandNode.put("message",
                        "ERROR: The weather change does not affect the environment. "
                        + "Cannot perform action");
            }
        } else {
            commandNode.put("message",
                    "ERROR: Simulation not started. Cannot perform action");
        }
        commandNode.put("timestamp", command.getTimestamp());
        return commandNode;
    }
}
