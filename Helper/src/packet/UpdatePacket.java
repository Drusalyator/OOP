package packet;

public class UpdatePacket implements IPacket {

    private String userName;
    private int dataLength;

    public UpdatePacket() {
    }

    public UpdatePacket(String userName, int dataLength) {
        this.userName = userName;
        this.dataLength = dataLength;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public int getDataLength() {
        return dataLength;
    }
}
