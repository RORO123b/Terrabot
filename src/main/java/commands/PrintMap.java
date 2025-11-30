package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import map.Cell;
import map.Map;
import map.Robot;

public class PrintMap implements Command {
    @Override
    public ObjectNode executeCommand(final ObjectMapper mapper, final CommandInput command,
                                    final Map map, final Robot robot,
                                    final boolean simulationActive, final int lastTimestamp) {
        ObjectNode commandNode = mapper.createObjectNode();
        commandNode.put("command", command.getCommand());

        if (robot != null) {
            robot.checkBatteryCharging(command.getTimestamp());
        }

        if (simulationActive && map != null) {
            map.checkWeatherFinished(command.getTimestamp());
        }

        if (simulationActive && map != null && robot != null && !robot.getIsCharging()) {
            map.updateEntities(robot, command, lastTimestamp);
            ArrayNode mapOutput = mapper.createArrayNode();

            for (int y = 0; y < map.getHeight(); y++) {
                for (int x = 0; x < map.getWidth(); x++) {
                    Cell cell = map.getCell(x, y);
                    ObjectNode cellNode = mapper.createObjectNode();

                    ArrayNode sectionArray = mapper.createArrayNode();
                    sectionArray.add(x);
                    sectionArray.add(y);
                    cellNode.set("section", sectionArray);

                    cellNode.put("totalNrOfObjects", cell.totalObjects());
                    cellNode.put("airQuality", cell.getAirQuality());
                    cellNode.put("soilQuality", cell.getSoilQuality());

                    mapOutput.add(cellNode);
                }
            }
            commandNode.set("output", mapOutput);
        } else if (robot != null && robot.getIsCharging()) {
            commandNode.put("message",
                    "ERROR: Robot still charging. Cannot perform action");
        } else {
            commandNode.put("message",
                    "ERROR: Simulation not started. Cannot perform action");
        }
        commandNode.put("timestamp", command.getTimestamp());
        return commandNode;
    }
}
