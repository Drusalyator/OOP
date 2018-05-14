package commands;

import dataProvider.FolderProvider;
import dataProvider.IDataProvider;
import logger.ILogger;
import logger.Logger;
import packet.AddPacket;
import packet.ErrorPacket;
import packet.IPacket;
import versionSystem.StandardVersion;

public class Add implements ICommand {

    private AddPacket addPacket;

    public Add(IPacket packet) {
        if (packet instanceof AddPacket)
            this.addPacket = (AddPacket) packet;
    }

    @Override
    public IPacket execute(IDataProvider dataProvider) {
        if (addPacket == null) return new ErrorPacket(1, "BED. Received a bad package");
        try {
            dataProvider.createRepository(addPacket.getRepositoryName(), new StandardVersion());
            ILogger logger = new Logger(addPacket.getRepositoryName());
            logger.writeNewLog(addPacket.getUserName() + ": add");
            return new AddPacket(addPacket.getUserName(), addPacket.getRepositoryName());
        } catch (IDataProvider.ProviderException exception) {
            return new ErrorPacket(2, "BED. Cannot create a repository" + exception.getLocalizedMessage());
        }
    }
}
