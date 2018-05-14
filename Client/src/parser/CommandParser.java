package parser;

import commandFactory.CommandFactory;
import fileWorker.Md5Executor;
import packet.*;
import utils.Helper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Properties;

public class CommandParser {

    public static IPacket parseCommand(String stringForParse, String userName) throws ParseError {
        ArrayList<String> arguments = getFinishArguments(stringForParse);
        if (arguments.size() > 0) {
            switch (arguments.get(0)) {
                case "add":
                    if (arguments.size() != 2) throw new ParseError("Incorrect command");
                    else return new AddPacket(userName, arguments.get(1));
                case "clone":
                    if (arguments.size() > 4 || arguments.size() < 3) throw new ParseError("Incorrect command");
                    if (arguments.size() == 3)
                        return new ClonePacket(userName, arguments.get(1), arguments.get(2), null, null);
                    else {
                        if (!Objects.equals(arguments.get(3), ".")) throw new ParseError("Unsupported flag");
                        return new ClonePacket(userName, arguments.get(1), arguments.get(2), arguments.get(3), null);
                    }
                case "update":
                    if (arguments.size() > 1) throw new ParseError("Incorrect command");
                    else return new UpdatePacket(userName, null);
                case "commit":
                    if (arguments.size() > 1) throw new ParseError("Incorrect command");
                    else return makeCommitPacket(userName);
                case "revert":
                    if (arguments.size() > 3) throw new ParseError("Incorrect command");
                    if (arguments.size() == 1) return new RevertPacket(userName, null, null, null);
                    else if (arguments.size() == 2)
                        try {
                            Double.parseDouble(arguments.get(1));
                            return new RevertPacket(userName, arguments.get(1), null, null);
                        } catch (Exception exception) {
                            throw new ParseError("Incorrect command");
                        }
                    else return new RevertPacket(userName, arguments.get(1), arguments.get(2), null);
                case "log":
                    if (arguments.size() > 1) throw new ParseError("Incorrect command");
                    else return new LogPacket(userName, null);
                default:
                    throw new ParseError("Incorrect command");
            }
        } else throw new ParseError("Incorrect command");
    }

    private static CommitPacket makeCommitPacket(String userName) throws ParseError {
        Properties userConfig;
        try {
            userConfig = CommandFactory.loadConfigFile("user.conf");
        } catch (IOException exception) {
            throw new ParseError("Cannot load conf file");
        }

        String localRep = userConfig.getProperty("LocalRep");

        Properties repConfig;
        try {
            repConfig = CommandFactory.loadConfigFile(localRep + "//rep.conf");
        } catch (IOException exception) {
            throw new ParseError("Cannot load conf file");
        }

        ArrayList<File> filesToSend = new ArrayList<>();

        File userRep = new File(localRep);
        File[] actualFile = userRep.listFiles((dir, name) -> !Objects.equals(name, "rep.conf"));
        if (actualFile == null) throw new ParseError("No changes");
        Md5Executor executor = new Md5Executor();
        for (File file: actualFile) {
            String hash = executor.process(file);
            if (repConfig.getProperty(file.getName(), null) == null ||
                    !Objects.equals(hash, repConfig.getProperty(file.getName(), null))) {
                filesToSend.add(file);
            }
        }

        FilePacket[] toSend;
        try {
            toSend = Helper.getFilePacket(filesToSend.toArray(new File[filesToSend.size()]));
        } catch (IOException exception) {
            throw new ParseError("Cannot collect files");
        }

        if (toSend == null) toSend = new FilePacket[0];

        String[] actFiles = userRep.list((dir, name) -> !Objects.equals(name, "rep.conf"));
        if (actFiles == null) actFiles = new String[0];

        return new CommitPacket(userName, toSend, actFiles);
    }

    private static ArrayList<String> getFinishArguments(String string) {
        String[] splitString = string.toLowerCase().split(" ");
        ArrayList<String> arguments = new ArrayList<>();

        for (String str : splitString) {
            if (!Objects.equals(str, " ") && !Objects.equals(str, ""))
                arguments.add(str);
        }

        return arguments;
    }

    public static class ParseError extends Exception {
        public ParseError(String message) {
            super(message);
        }
    }
}
