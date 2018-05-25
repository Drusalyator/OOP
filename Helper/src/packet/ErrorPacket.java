package packet;

public class ErrorPacket implements IPacket {

    private int errorType;
    private String errorInfo;

    public ErrorPacket() {
    }

    public ErrorPacket(int errorType, String errorInfo) {
        this.errorType = errorType;
        this.errorInfo = errorInfo;
    }

    public int getErrorType() {
        return errorType;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    @Override
    public int getDataLength() {
        return 0;
    }
}
