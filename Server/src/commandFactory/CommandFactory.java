package commandFactory;

import commands.*;
import commands.Error;
import packet.IPacket;

public class CommandFactory {

    public static ICommand createCommand(IPacket packet) {
        switch (packet.getClass().getName()) {
            case "packet.AddPacket":
                return new Add(packet);
            case "packet.ClonePacket":
                return new Clone(packet);
            case "packet.UpdatePacket":
                return new Update(packet);
            case "packet.CommitPacket":
                return new Commit(packet);
            case "packet.RevertPacket":
                return new Revert(packet);
            case "packet.LogPacket":
                return new Log(packet);
            default:
                return new Error(packet);
        }
    }
}
