package fileWorker;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.FileInputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;


public class Md5Executor implements IExecutable{

    @Override
    public String process(File file) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            DigestInputStream digestInputStream = new DigestInputStream(new FileInputStream(file), messageDigest);
            while (digestInputStream.read() >= 0) ;
            byte[] hash = messageDigest.digest();
            return DatatypeConverter.printHexBinary(hash);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }
}

