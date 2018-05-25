package commands;

import dataProvider.IDataProvider;
import javafx.util.Pair;
import packet.ErrorPacket;
import packet.IPacket;

public class Error implements ICommand {

    private ErrorPacket errorPacket;

    public Error(IPacket packet) {
        if (packet instanceof ErrorPacket)
            this.errorPacket = (ErrorPacket) packet;
    }

    @Override
    public Pair<IPacket, byte[]> execute(IDataProvider dataProvider, byte[] data) {
        return new Pair<>(new ErrorPacket(10, errorPacket.getErrorInfo()), null);
    }
}
