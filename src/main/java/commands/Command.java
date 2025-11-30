package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import map.Map;
import map.Robot;

public interface Command {
    /**
     * Executes the command and returns the result as an ObjectNode.
     *
     * @param mapper The ObjectMapper for creating JSON nodes
     * @param command The command input
     * @param map The game map
     * @param robot The robot
     * @param simulationActive Whether simulation is active
     * @param lastTimestamp The last timestamp
     * @return The command result as ObjectNode
     */
    ObjectNode executeCommand(ObjectMapper mapper, CommandInput command,
                            Map map, Robot robot, boolean simulationActive,
                            int lastTimestamp);
}
