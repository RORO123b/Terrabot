package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import map.Map;
import map.Robot;

import java.util.List;

public final class PrintKnowledgeBase implements Command {
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
            map.updateEntities(robot, command, lastTimestamp);
        }
        if (simulationActive && map != null && robot != null && !robot.getIsCharging()) {
            ArrayNode knowledgeOutput = mapper.createArrayNode();
            for (java.util.Map.Entry<String, List<String>> entry
                    : robot.getKnowledgeBase().entrySet()) {
                ObjectNode topicNode = mapper.createObjectNode();
                topicNode.put("topic", entry.getKey());

                ArrayNode factsArray = mapper.createArrayNode();
                for (String fact : entry.getValue()) {
                    factsArray.add(fact);
                }
                topicNode.set("facts", factsArray);

                knowledgeOutput.add(topicNode);
            }
            commandNode.set("output", knowledgeOutput);
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
