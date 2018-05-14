package packet;

public class CommitPacket implements IPacket {

    private String userName;
    private FilePacket[] files;
    private String[] actualFiles;

    public CommitPacket() {
    }

    public CommitPacket(String userName, FilePacket[] files, String[] actualFiles) {
        this.userName = userName;
        this.files = files;
        this.actualFiles = actualFiles;
    }

    public String getUserName() {
        return userName;
    }

    public FilePacket[] getFiles() {
        return files;
    }

    public String[] getActualFiles() {
        return actualFiles;
    }
}
