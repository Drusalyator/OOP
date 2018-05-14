package packet;

public class FilePacket implements IPacket {

    private String fileName;
    private byte[] fileData;
    private String hash;

    public FilePacket() {
    }

    public FilePacket(String fileName, byte[] fileData, String hash) {
        this.fileName = fileName;
        this.fileData = fileData;
    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public String getHash() {
        return hash;
    }
}
