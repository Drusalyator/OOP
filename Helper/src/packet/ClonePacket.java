package packet;

public class ClonePacket implements IPacket {

    private String userName;
    private String path;
    private String repoName;
    private String flag;
    private FilePacket[] files;

    public ClonePacket() {
    }

    public ClonePacket(String userName, String path, String repoName, String flag, FilePacket[] files) {
        this.userName = userName;
        this.path = path;
        this.repoName = repoName;
        this.flag = flag;
        this.files = files;
    }

    public String getUserName() {
        return userName;
    }

    public String getPath() {
        return path;
    }

    public String getRepoName() {
        return repoName;
    }

    public String getFlag() {
        return flag;
    }

    public FilePacket[] getFiles() {
        return files;
    }
}
