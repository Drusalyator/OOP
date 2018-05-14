package commands;

import commandFactory.UserConfig;
import dataProvider.IDataProvider;
import logger.ILogger;
import logger.Logger;
import packet.ErrorPacket;
import packet.FilePacket;
import packet.IPacket;
import packet.RevertPacket;
import utils.Helper;

import java.io.File;

public class Revert implements ICommand {

    private RevertPacket revertPacket;

    public Revert(IPacket packet) {
        if (packet instanceof RevertPacket)
            this.revertPacket = (RevertPacket) packet;
    }

    @Override
    public IPacket execute(IDataProvider dataProvider) {
        if (revertPacket == null) return new ErrorPacket(1, "BED. Received a bad package");
        UserConfig userConfig = UserConfig.getInstance();
        String userRep = userConfig.getUserRepository(revertPacket.getUserName());
        if (userConfig.getUserRepository(revertPacket.getUserName()) != null) {
            try {
                ILogger logger = new Logger(userRep);
                if (revertPacket.getVersion() != null) {
                    File[] files = dataProvider.getNeededVersion(userRep, revertPacket.getVersion());
                    FilePacket[] filePackets = Helper.getFilePacket(files);
                    logger.writeNewLog(revertPacket.getUserName() + ": revert " + revertPacket.getVersion());
                    return new RevertPacket(revertPacket.getUserName(), revertPacket.getVersion(),
                            revertPacket.getFlag(), filePackets);
                } else {
                    File[] files = dataProvider.getActualVersion(userRep);
                    FilePacket[] filePackets = Helper.getFilePacket(files);
                    logger.writeNewLog(revertPacket.getUserName() + ": revert");
                    return new RevertPacket(revertPacket.getUserName(), revertPacket.getVersion(),
                            revertPacket.getFlag(), filePackets);
                }
            } catch (Exception exception) {
                return new ErrorPacket(16, "Cannot execute command 'Revert': " + exception.getLocalizedMessage());
            }
        } else return new ErrorPacket(5, "Make command clone at first");
    }
}
