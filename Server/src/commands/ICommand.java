package commands;

import dataProvider.IDataProvider;
import packet.IPacket;

public interface ICommand {

    IPacket execute(IDataProvider dataProvider);

    class CommandError extends Exception {
        public CommandError(String message) {
            super(message);
        }
    }
}
