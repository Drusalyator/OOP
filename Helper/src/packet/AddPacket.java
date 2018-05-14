package packet;

public class AddPacket implements IPacket {

    private String userName;
    private String repositoryName;

    public AddPacket() {
    }

    public AddPacket(String userName, String repositoryName) {
        this.userName = userName;
        this.repositoryName = repositoryName;
    }

    public String getUserName() {
        return userName;
    }

    public String getRepositoryName() {
        return repositoryName;
    }
}
