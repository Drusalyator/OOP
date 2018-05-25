package commands;

import packet.ErrorPacket;
import packet.IPacket;

public class Error implements ICommand {

    private ErrorPacket errorPacket;

    public Error(IPacket packet) {
        if (packet instanceof ErrorPacket)
            this.errorPacket = (ErrorPacket) packet;
    }

    @Override
    public void execute(byte[] data) {
        System.out.println(" > Something wrong");
        System.out.println("   Error type: " + errorPacket.getErrorType());
        System.out.println("   Error info: " + errorPacket.getErrorInfo());
    }
}
