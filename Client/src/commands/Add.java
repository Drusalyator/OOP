package commands;

import packet.AddPacket;
import packet.IPacket;

public class Add implements ICommand{

    private AddPacket addPacket;

    public Add(IPacket packet) {
        if (packet instanceof AddPacket)
            this.addPacket = (AddPacket) packet;
    }

    @Override
    public void execute() {
        System.out.println(" > Repository with name : " + addPacket.getRepositoryName() + " was created");
    }

}
