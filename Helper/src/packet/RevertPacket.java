package packet;

public class RevertPacket implements IPacket{

    private String userName;
    private String version;
    private String flag;
    private int dataLength;

    public RevertPacket() {
    }

    public RevertPacket(String userName, String version, String flag, int dataLength) {
        this.userName = userName;
        this.version = version;
        this.flag = flag;
        this.dataLength = dataLength;
    }

    public String getUserName() {
        return userName;
    }

    public String getVersion() {
        return version;
    }

    public String getFlag() {
        return flag;
    }

    @Override
    public int getDataLength() {
        return dataLength;
    }
}
