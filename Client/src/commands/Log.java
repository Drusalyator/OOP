package commands;

import packet.IPacket;
import packet.LogPacket;

public class Log implements ICommand{

    private LogPacket logPacket;

    public Log (IPacket packet) {
        if (packet instanceof LogPacket)
            this.logPacket = (LogPacket) packet;
    }

    @Override
    public void execute() {
        System.out.println(logPacket.getLog());
    }
}
