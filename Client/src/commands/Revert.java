package commands;

import packet.IPacket;
import packet.RevertPacket;

public class Revert implements ICommand{

    private RevertPacket revertPacket;

    public Revert(IPacket packet) {
        if (packet instanceof RevertPacket)
            this.revertPacket = (RevertPacket) packet;
    }

    @Override
    public void execute() {

    }

}
