package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import fileio.InputLoader;
import fileio.CommandInput;
import fileio.SimulationInput;

import map.Map;
import map.Robot;

import commands.StartSimulation;
import commands.PrintEnvConditions;
import commands.PrintMap;
import commands.EndSimulation;
import commands.MoveRobot;
import commands.RechargeBattery;
import commands.GetEnergyStatus;
import commands.ChangeWeatherConditions;
import commands.ScanObject;
import commands.LearnFact;
import commands.PrintKnowledgeBase;
import commands.ImproveEnvironment;

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
        int lastTimestamp = 0;

        for (CommandInput command : inputLoader.getCommands()) {
            ObjectNode commandNode;

            switch (command.getCommand()) {
                case "startSimulation":
                    currentSimulationIndex++;
                    SimulationInput sim = null;
                    if (currentSimulationIndex < inputLoader.getSimulations().size()) {
                        sim = inputLoader.getSimulations().get(currentSimulationIndex);
                    }
                    StartSimulation startCmd = new StartSimulation(sim);
                    commandNode = startCmd.executeCommand(MAPPER, command,
                            map, robot, simulationActive, lastTimestamp);
                    if (!simulationActive && sim != null) {
                        map = startCmd.getMap();
                        robot = startCmd.getRobot();
                        simulationActive = true;
                    }
                    lastTimestamp = command.getTimestamp();
                    break;

                case "printEnvConditions":
                    commandNode = new PrintEnvConditions().executeCommand(MAPPER, command,
                            map, robot, simulationActive, lastTimestamp);
                    lastTimestamp = command.getTimestamp();
                    break;

                case "printMap":
                    commandNode = new PrintMap().executeCommand(MAPPER, command,
                            map, robot, simulationActive, lastTimestamp);
                    lastTimestamp = command.getTimestamp();
                    break;

                case "endSimulation":
                    commandNode = new EndSimulation().executeCommand(MAPPER, command,
                            map, robot, simulationActive, lastTimestamp);
                    if (simulationActive) {
                        simulationActive = false;
                    }
                    lastTimestamp = command.getTimestamp();
                    break;

                case "moveRobot":
                    commandNode = new MoveRobot().executeCommand(MAPPER, command,
                            map, robot, simulationActive, lastTimestamp);
                    lastTimestamp = command.getTimestamp();
                    break;

                case "rechargeBattery":
                    commandNode = new RechargeBattery().executeCommand(MAPPER, command,
                            map, robot, simulationActive, lastTimestamp);
                    lastTimestamp = command.getTimestamp();
                    break;

                case "getEnergyStatus":
                    commandNode = new GetEnergyStatus().executeCommand(MAPPER, command,
                            map, robot, simulationActive, lastTimestamp);
                    lastTimestamp = command.getTimestamp();
                    break;

                case "changeWeatherConditions":
                    commandNode = new ChangeWeatherConditions().executeCommand(MAPPER, command,
                            map, robot, simulationActive, lastTimestamp);
                    lastTimestamp = command.getTimestamp();
                    break;

                case "scanObject":
                    commandNode = new ScanObject().executeCommand(MAPPER, command,
                            map, robot, simulationActive, lastTimestamp);
                    lastTimestamp = command.getTimestamp();
                    break;

                case "learnFact":
                    commandNode = new LearnFact().executeCommand(MAPPER, command,
                            map, robot, simulationActive, lastTimestamp);
                    lastTimestamp = command.getTimestamp();
                    break;

                case "printKnowledgeBase":
                    commandNode = new PrintKnowledgeBase().executeCommand(MAPPER, command,
                            map, robot, simulationActive, lastTimestamp);
                    lastTimestamp = command.getTimestamp();
                    break;

                case "improveEnvironment":
                    commandNode = new ImproveEnvironment().executeCommand(MAPPER, command,
                            map, robot, simulationActive, lastTimestamp);
                    lastTimestamp = command.getTimestamp();
                    break;

                default:
                    commandNode = MAPPER.createObjectNode();
                    commandNode.put("command", command.getCommand());
                    commandNode.put("timestamp", command.getTimestamp());
                    break;
            }

            output.add(commandNode);
        }

        File outputFile = new File(outputPath);
        outputFile.getParentFile().mkdirs();
        WRITER.writeValue(outputFile, output);
    }
}
