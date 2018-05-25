package commands;

import dataProvider.IDataProvider;
import javafx.util.Pair;
import packet.IPacket;

public interface ICommand {

    Pair<IPacket, byte[]> execute(IDataProvider dataProvider, byte[] data);
}
