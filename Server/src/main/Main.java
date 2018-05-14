package main;

import dataProvider.FolderProvider;
import versionSystem.StandardVersion;
import webServer.Server;

import java.io.*;


public class Main {
    public static void main(String[] args) {
        new Server("127.0.0.1", 40010).Run();
        System.exit(0);



        /*

        try {
            FolderProvider folderProvider = new FolderProvider();
            folderProvider.createRepository("govno", new StandardVersion());
            File[] files = folderProvider.getActualVersion("govno");
            System.out.println("OK");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        */
        /*
        AddPacket addPacket = new AddPacket("Andrey", "pisa");
        ClonePacket clonePacket = new ClonePacket("Andrey", "aa", "pisos", null, null);
        UpdatePacket updatePacket = new UpdatePacket("Andrey", null);
        LogPacket logPacket = new LogPacket("Andrey", null);
        CommitPacket commitPacket = new CommitPacket("Andrey", new FilePacket[0], new String[] {"aka", "pop"});
        RevertPacket revertPacket = new RevertPacket("Andrey", "1.3", null, null);
        ICommand command0 = CommandFactory.createCommand(addPacket);
        IPacket packet0 = command0.execute(new FolderProvider());
        ICommand command = CommandFactory.createCommand(clonePacket);
        IPacket packet1 = command.execute(new FolderProvider());
        ICommand command1 = CommandFactory.createCommand(updatePacket);
        IPacket packet2 = command1.execute(new FolderProvider());
        ICommand command2 = CommandFactory.createCommand(logPacket);
        IPacket packet3 = command2.execute(new FolderProvider());
        ICommand command3 = CommandFactory.createCommand(commitPacket);
        IPacket packet4 = command3.execute(new FolderProvider());
        ICommand command4 = CommandFactory.createCommand(revertPacket);
        IPacket packet5 = command4.execute(new FolderProvider());
        System.out.println("OK");
        */
    }
}
