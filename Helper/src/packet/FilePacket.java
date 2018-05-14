package packet;

public class FilePacket implements IPacket {

    private String fileName;
    private byte[] fileData;

    public FilePacket() {
    }

    public FilePacket(String fileName, byte[] fileData) {
        this.fileName = fileName;
        this.fileData = fileData;
    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getFileData() {
        return fileData;
    }
}
