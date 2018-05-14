package commands;

import dataProvider.IDataProvider;
import packet.ErrorPacket;
import packet.IPacket;

public class Error implements ICommand {

    private ErrorPacket errorPacket;

    public Error(IPacket packet) {
        if (packet instanceof ErrorPacket)
            this.errorPacket = (ErrorPacket) packet;
    }

    @Override
    public IPacket execute(IDataProvider dataProvider) {
        return new ErrorPacket(10, errorPacket.getErrorInfo());
    }
}
