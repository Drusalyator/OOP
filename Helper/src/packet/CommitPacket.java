package packet;

public class CommitPacket implements IPacket {

    private String userName;
    private int dataLength;
    private String[] actualFiles;

    public CommitPacket() {
    }

    public CommitPacket(String userName, int dataLength, String[] actualFiles) {
        this.userName = userName;
        this.dataLength = dataLength;
        this.actualFiles = actualFiles;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public int getDataLength() {
        return dataLength;
    }

    public String[] getActualFiles() {
        return actualFiles;
    }
}
