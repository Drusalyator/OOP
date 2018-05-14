package commands;

import commandFactory.UserConfig;
import dataProvider.IDataProvider;
import logger.ILogger;
import logger.Logger;
import packet.ErrorPacket;
import packet.FilePacket;
import packet.IPacket;
import packet.UpdatePacket;
import utils.Helper;

import java.io.File;

public class Update implements ICommand{

    private UpdatePacket updatePacket;

    public Update(IPacket packet) {
        if (packet instanceof UpdatePacket)
            this.updatePacket = (UpdatePacket) packet;
    }

    @Override
    public IPacket execute(IDataProvider dataProvider) {
        if (updatePacket == null) return new ErrorPacket(1, "BED. Received a bad package");
        UserConfig userConfig = UserConfig.getInstance();
        String userRep = userConfig.getUserRepository(updatePacket.getUserName());
        if (userConfig.getUserRepository(updatePacket.getUserName()) != null) {
            try {
                File[] files = dataProvider.getActualVersion(userRep);
                FilePacket[] filePackets = Helper.getFilePacket(files);
                ILogger logger = new Logger(userRep);
                logger.writeNewLog(updatePacket.getUserName() + ": add");
                return new UpdatePacket(updatePacket.getUserName(), filePackets);
            } catch (Exception exception) {
                return new ErrorPacket(6, "Cannot execute command 'Update': " + exception.getLocalizedMessage());
            }
        } else return new ErrorPacket(5, "Make command clone at first");
    }
}
