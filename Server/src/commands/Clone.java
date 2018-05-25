package commands;

import commandFactory.UserConfig;
import dataProvider.IDataProvider;
import javafx.util.Pair;
import logger.ILogger;
import logger.Logger;
import packet.ClonePacket;
import packet.ErrorPacket;
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
    public Pair<IPacket, byte[]> execute(IDataProvider dataProvider, byte[] data) {
        if (clonePacket == null) return new Pair<>(new ErrorPacket(1, "BED. Received a bad package"), null);
        try {
            File[] files = dataProvider.getActualVersion(clonePacket.getRepoName());
            byte[] archive = Helper.makeArchive(files);

            UserConfig userConfig = UserConfig.getInstance();
            userConfig.addNewUsers(clonePacket.getUserName(), clonePacket.getRepoName());

            ILogger logger = new Logger(clonePacket.getRepoName());
            logger.writeNewLog(clonePacket.getUserName() + ": clone");

            return new Pair<>(new ClonePacket(clonePacket.getUserName(), clonePacket.getPath(),
                    clonePacket.getRepoName(), clonePacket.getFlag(), archive.length), archive);
        } catch (Exception exception) {
            return new Pair<>(new ErrorPacket(
                    3, "Cannot execute command clone" + exception.getLocalizedMessage()), null);
        }
    }
}
