package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import map.Map;
import map.Robot;

public final class ScanObject implements Command {
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
        String color = command.getColor();
        String smell = command.getSmell();
        String sound = command.getSound();

        if (simulationActive && map != null && robot != null && !robot.getIsCharging()) {
            String scanResult = robot.scanObject(color, smell, sound, map, command);
            commandNode.put("message", scanResult);
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
