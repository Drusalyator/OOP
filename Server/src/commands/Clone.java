package commands;

import commandFactory.UserConfig;
import dataProvider.IDataProvider;
import logger.ILogger;
import logger.Logger;
import packet.ClonePacket;
import packet.ErrorPacket;
import packet.FilePacket;
import packet.IPacket;
import utils.Helper;

import java.io.File;

public class Clone implements ICommand {

    private ClonePacket clonePacket;

    public Clone(IPacket packet) {
        if (packet instanceof ClonePacket)
            this.clonePacket = (ClonePacket) packet;
    }

    @Override
    public IPacket execute(IDataProvider dataProvider) {
        if (clonePacket == null) return new ErrorPacket(1, "BED. Received a bad package");
        try {
            File[] files = dataProvider.getActualVersion(clonePacket.getRepoName());
            FilePacket[] filePackets = Helper.getFilePacket(files);
            UserConfig userConfig = UserConfig.getInstance();
            userConfig.addNewUsers(clonePacket.getUserName(), clonePacket.getRepoName());
            ILogger logger = new Logger(clonePacket.getRepoName());
            logger.writeNewLog(clonePacket.getUserName() + ": clone");
            return new ClonePacket(clonePacket.getUserName(), clonePacket.getPath(),
                    clonePacket.getRepoName(), clonePacket.getFlag(), filePackets);
        } catch (Exception exception) {
            return new ErrorPacket(3, "Cannot execute command clone" + exception.getLocalizedMessage());
        }
    }
}
