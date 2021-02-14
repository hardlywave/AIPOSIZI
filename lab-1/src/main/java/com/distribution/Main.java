package com.distribution;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;


public class Main {
    public static String DIRECTORY_PATH = System.getProperty("user.dir") + "/directory";
    public static String FORBIDDEN = "/home/alex/Pictures";
//    public static String DIRECTORY_PATH = "/home/alex";
    private static final Logger log = LogManager.getLogger(Main.class);
    private static final int PORT = 8080;

    public static void main(String[] args) {
        try {
            if (args.length > 0) {
                if (Files.exists(Paths.get(args[0]))) {
                    DIRECTORY_PATH = args[0];
                }
            }
            var serverConnect = new ServerSocket(PORT);
            PropertyConfigurator.configure("log4j.properties");
            log.info("Server started. Listening for connections on port: " + PORT);
            log.info("Root directory: " + DIRECTORY_PATH);
            while (true) {
                HttpServer server = new HttpServer(serverConnect.accept());
                log.info("Connection is opened. " + Instant.now());
                Thread thread = new Thread(server);
                thread.start();
            }
        } catch (IOException e) {
            log.error("Server Connection error : " + e.getMessage());
        }
    }
}