package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import map.Map;
import map.Robot;

public final class ImproveEnvironment implements Command {
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
            map.updateEntities(command, lastTimestamp);
        }
        String improvementType = command.getImprovementType();
        String type = command.getType();
        String name = command.getName();

        if (simulationActive && map != null && robot != null && !robot.getIsCharging()) {
            String result = robot.improveEnvironment(improvementType, type, name, map);
            commandNode.put("message", result);
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
