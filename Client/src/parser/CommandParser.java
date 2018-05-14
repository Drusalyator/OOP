package parser;

import packet.*;

import java.util.ArrayList;
import java.util.Objects;

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
                    else return new ClonePacket(userName, arguments.get(1), arguments.get(2), arguments.get(3), null);
                case "update":
                    if (arguments.size() > 1) throw new ParseError("Incorrect command");
                    else return new UpdatePacket(userName, null);
                case "commit":
                    if (arguments.size() > 1) throw new ParseError("Incorrect command");
                    else return new CommitPacket(userName, new FilePacket[0], new String[0]);
                    //TODO
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
