package commands;

import packet.CommitPacket;
import packet.IPacket;

public class Commit implements ICommand{

    private CommitPacket commitPacket;

    public Commit(IPacket packet) {
        if (packet instanceof CommitPacket)
            this.commitPacket = (CommitPacket) packet;
    }

    @Override
    public void execute() {
        System.out.println(" > File(s) was commit is: " + commitPacket.getUserName());
    }
}
