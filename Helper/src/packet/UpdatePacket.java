package packet;

public class UpdatePacket implements IPacket {

    private String userName;
    private FilePacket[] files;

    public UpdatePacket() {
    }

    public UpdatePacket(String userName, FilePacket[] files) {
        this.userName = userName;
        this.files = files;
    }

    public String getUserName() {
        return userName;
    }

    public FilePacket[] getFiles() {
        return files;
    }
}
