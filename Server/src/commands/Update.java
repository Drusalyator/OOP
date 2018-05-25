package commands;

import commandFactory.UserConfig;
import dataProvider.IDataProvider;
import javafx.util.Pair;
import logger.ILogger;
import logger.Logger;
import packet.ErrorPacket;
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
    public Pair<IPacket, byte[]> execute(IDataProvider dataProvider, byte[] data) {
        if (updatePacket == null) return new Pair<>(new ErrorPacket(1, "BED. Received a bad package"), null);
        UserConfig userConfig = UserConfig.getInstance();
        String userRep = userConfig.getUserRepository(updatePacket.getUserName());
        if (userConfig.getUserRepository(updatePacket.getUserName()) != null) {
            try {
                File[] files = dataProvider.getActualVersion(userRep);
                byte[] fileData = Helper.makeArchive(files);
                ILogger logger = new Logger(userRep);
                logger.writeNewLog(updatePacket.getUserName() + ": add");
                return new Pair<>(new UpdatePacket(updatePacket.getUserName(), fileData.length), fileData);
            } catch (Exception exception) {
                return new Pair<>(new ErrorPacket
                        (6, "Cannot execute command 'Update': " + exception.getLocalizedMessage()), null);
            }
        } else return new Pair<>(new ErrorPacket(5, "Make command clone at first"), null);
    }
}
