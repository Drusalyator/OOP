package commands;

import commandFactory.UserConfig;
import dataProvider.IDataProvider;
import javafx.util.Pair;
import logger.ILogger;
import logger.Logger;
import packet.ErrorPacket;
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
    public Pair<IPacket, byte[]> execute(IDataProvider dataProvider, byte[] data) {
        if (revertPacket == null) return new Pair<>(new ErrorPacket(1, "BED. Received a bad package"), null);
        UserConfig userConfig = UserConfig.getInstance();
        String userRep = userConfig.getUserRepository(revertPacket.getUserName());
        if (userConfig.getUserRepository(revertPacket.getUserName()) != null) {
            try {
                ILogger logger = new Logger(userRep);
                if (revertPacket.getVersion() != null) {
                    File[] files = dataProvider.getNeededVersion(userRep, revertPacket.getVersion());
                    byte[] fileData = Helper.makeArchive(files);
                    logger.writeNewLog(revertPacket.getUserName() + ": revert " + revertPacket.getVersion());
                    return new Pair<>(new RevertPacket(revertPacket.getUserName(), revertPacket.getVersion(),
                            revertPacket.getFlag(), fileData.length), fileData);
                } else {
                    File[] files = dataProvider.getActualVersion(userRep);
                    byte[] fileData = Helper.makeArchive(files);
                    logger.writeNewLog(revertPacket.getUserName() + ": revert");
                    return new Pair<>(new RevertPacket(revertPacket.getUserName(), revertPacket.getVersion(),
                            revertPacket.getFlag(), fileData.length), fileData);
                }
            } catch (Exception exception) {
                return new Pair<>(new ErrorPacket
                        (16, "Cannot execute command 'Revert': " + exception.getLocalizedMessage()), null);
            }
        } else return new Pair<>(new ErrorPacket(5, "Make command clone at first"), null);
    }
}
