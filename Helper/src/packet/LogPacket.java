package packet;

public class LogPacket implements IPacket{

    private String userName;
    private String log;

    public LogPacket() {
    }

    public LogPacket(String userName, String log) {
        this.userName = userName;
        this.log = log;
    }

    public String getUserName() {
        return userName;
    }

    public String getLog() {
        return log;
    }
}
