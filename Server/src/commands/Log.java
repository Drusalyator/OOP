package commands;

import commandFactory.UserConfig;
import dataProvider.IDataProvider;
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
    public IPacket execute (IDataProvider dataProvider) {
        if (logPacket == null) return new ErrorPacket(1, "BED. Received a bad package");
        UserConfig userConfig = UserConfig.getInstance();
        String userRep = userConfig.getUserRepository(logPacket.getUserName());
        if (userConfig.getUserRepository(logPacket.getUserName()) != null) {
            try {
                ILogger logger = new Logger(userRep);
                logger.writeNewLog(logPacket.getUserName() + ": log");
                String log = logger.getLog();
                return new LogPacket(logPacket.getUserName(), log);
            } catch (Exception exception) {
                return new ErrorPacket(11, "Cannot execute command 'Clone': " + exception.getLocalizedMessage());
            }
        } else return new ErrorPacket(5, "Make command clone at first");
    }
}
