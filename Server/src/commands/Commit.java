package commands;

import commandFactory.UserConfig;
import dataProvider.IDataProvider;
import javafx.util.Pair;
import logger.ILogger;
import logger.Logger;
import packet.CommitPacket;
import packet.ErrorPacket;
import packet.IPacket;

public class Commit implements ICommand {

    private CommitPacket commitPacket;

    public Commit(IPacket packet) {
        if (packet instanceof CommitPacket)
            this.commitPacket = (CommitPacket) packet;
    }

    @Override
    public Pair<IPacket, byte[]> execute(IDataProvider dataProvider, byte[] data) {
        if (commitPacket == null) return new Pair<>(new ErrorPacket(1, "BED. Received a bad package"), null);
        UserConfig userConfig = UserConfig.getInstance();
        String userRep = userConfig.getUserRepository(commitPacket.getUserName());
        if (userConfig.getUserRepository(commitPacket.getUserName()) != null) {
            try {
                if (commitPacket.getActualFiles() != null) {
                    dataProvider.addNewVersion(userRep, data, commitPacket.getActualFiles());
                    ILogger logger = new Logger(userRep);
                    logger.writeNewLog(commitPacket.getUserName() + ": commit");
                    return new Pair<>(new CommitPacket(commitPacket.getUserName(), 0, commitPacket.getActualFiles()), null);
                } else return new Pair<>(new ErrorPacket(12, "No actual files in commit packet"), null);
            } catch (Exception exception) {
                return new Pair<>(new ErrorPacket(
                        13, "Cannot execute command 'Commit': " + exception.getLocalizedMessage()), null);
            }
        } else return new Pair<>(new ErrorPacket(5, "Make command clone at first"), null);
    }
}
