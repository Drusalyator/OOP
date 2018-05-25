package main;

import commands.Add;
import commands.Clone;
import commands.Commit;
import commands.ICommand;
import dataProvider.FolderProvider;
import javafx.util.Pair;
import packet.AddPacket;
import packet.ClonePacket;
import packet.CommitPacket;
import packet.IPacket;
import utils.Helper;
import webServer.Server;

import java.io.File;
import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException{

        /*

        File[] files = new File[2];
        File file1 = new File("Hate.mp3");
        File file2 = new File("Magic.mp3");
        files[0] = file1;
        files[1] = file2;

        byte[] bytes = Helper.makeArchive(files);

        ICommand add2 = new Clone(new ClonePacket("a", "ada", "pisos", null, 0));
        Pair<IPacket, byte[]> data = add2.execute(new FolderProvider(), null);

        ICommand add = new Commit(new CommitPacket("a", bytes.length, new String[] {"Hate.mp3", "Magic.mp3"}));
        Pair<IPacket, byte[]> data2 = add.execute(new FolderProvider(), bytes);

        System.out.print(1);

        */

        new Server("127.0.0.1", 40010).Run();
        System.exit(0);
    }
}
