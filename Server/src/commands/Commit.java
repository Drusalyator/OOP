package commands;

import commandFactory.UserConfig;
import dataProvider.IDataProvider;
import logger.ILogger;
import logger.Logger;
import packet.CommitPacket;
import packet.ErrorPacket;
import packet.FilePacket;
import packet.IPacket;

public class Commit implements ICommand {

    private CommitPacket commitPacket;

    public Commit(IPacket packet) {
        if (packet instanceof CommitPacket)
            this.commitPacket = (CommitPacket) packet;
    }

    @Override
    public IPacket execute(IDataProvider dataProvider) {
        if (commitPacket == null) return new ErrorPacket(1, "BED. Received a bad package");
        UserConfig userConfig = UserConfig.getInstance();
        String userRep = userConfig.getUserRepository(commitPacket.getUserName());
        if (userConfig.getUserRepository(commitPacket.getUserName()) != null) {
            try {
                if (commitPacket.getActualFiles() != null) {
                    dataProvider.addNewVersion(userRep, commitPacket.getFiles(), commitPacket.getActualFiles());
                    ILogger logger = new Logger(userRep);
                    logger.writeNewLog(commitPacket.getUserName() + ": commit " +
                            commitPacket.getFiles().length + " files");
                    return new CommitPacket(commitPacket.getUserName(), new FilePacket[0], commitPacket.getActualFiles());
                } else return new ErrorPacket(12, "No actual files in commit packet");
            } catch (Exception exception) {
                return new ErrorPacket(13, "Cannot execute command 'Commit': " + exception.getLocalizedMessage());
            }
        } else return new ErrorPacket(5, "Make command clone at first");
    }
}
