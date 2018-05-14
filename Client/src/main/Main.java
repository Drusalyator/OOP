package main;

import client.Client;
import parser.CommandParser;

public class Main {

    public static void main(String[] args) throws CommandParser.ParseError{
        new Client("127.0.0.1", 40010).run();
    }
}