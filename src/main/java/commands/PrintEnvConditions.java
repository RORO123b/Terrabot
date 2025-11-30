package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import map.Cell;
import map.Map;
import map.Robot;

public final class PrintEnvConditions implements Command {
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
            map.updateEntities(command, lastTimestamp);
            Cell currentCell = map.getCell(robot.getX(), robot.getY());
            ObjectNode envOutput = mapper.createObjectNode();

            if (currentCell.getSoil() != null) {
                envOutput.set("soil", CommandHelper.createSoilNode(currentCell.getSoil()));
            }
            if (currentCell.getPlant() != null) {
                envOutput.set("plants",
                        CommandHelper.createPlantNode(currentCell.getPlant()));
            }
            if (currentCell.getAnimal() != null) {
                envOutput.set("animals",
                        CommandHelper.createAnimalNode(currentCell.getAnimal()));
            }
            if (currentCell.getWater() != null) {
                envOutput.set("water", CommandHelper.createWaterNode(currentCell.getWater()));
            }
            if (currentCell.getAir() != null) {
                envOutput.set("air", CommandHelper.createAirNode(currentCell.getAir()));
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
        return commandNode;
    }
}
