package commands;

import commandFactory.UserConfig;
import dataProvider.IDataProvider;
import javafx.util.Pair;
import logger.ILogger;
import logger.Logger;
import packet.ErrorPacket;
import packet.IPacket;
import packet.LogPacket;

public class Log implements ICommand{
    private LogPacket logPacket;

    public Log (IPacket packet) {
        if (packet instanceof LogPacket)
            this.logPacket = (LogPacket) packet;
    }

    @Override
    public Pair<IPacket, byte[]> execute (IDataProvider dataProvider, byte[] data) {
        if (logPacket == null) return new Pair<>(new ErrorPacket(1, "BED. Received a bad package"), null);
        UserConfig userConfig = UserConfig.getInstance();
        String userRep = userConfig.getUserRepository(logPacket.getUserName());
        if (userConfig.getUserRepository(logPacket.getUserName()) != null) {
            try {
                ILogger logger = new Logger(userRep);
                logger.writeNewLog(logPacket.getUserName() + ": log");
                String log = logger.getLog();
                return new Pair<>(new LogPacket(logPacket.getUserName(), log), null);
            } catch (Exception exception) {
                return new Pair<>(new ErrorPacket(
                        11, "Cannot execute command 'Clone': " + exception.getLocalizedMessage()), null);
            }
        } else return new Pair<>(new ErrorPacket(5, "Make command clone at first"), null);
    }
}
