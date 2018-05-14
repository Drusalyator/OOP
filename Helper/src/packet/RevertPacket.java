package packet;

public class RevertPacket implements IPacket{

    private String userName;
    private String version;
    private String flag;
    private FilePacket[] files;

    public RevertPacket() {
    }

    public RevertPacket(String userName, String version, String flag, FilePacket[] files) {
        this.userName = userName;
        this.version = version;
        this.flag = flag;
        this.files = files;
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

    public FilePacket[] getFiles() {
        return files;
    }
}
