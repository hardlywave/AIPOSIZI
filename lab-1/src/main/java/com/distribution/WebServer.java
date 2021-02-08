package com.distribution;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static com.distribution.Constant.*;

public class WebServer {

    private static Logger LOGGER;

    static {
        try (FileInputStream ins = new FileInputStream(LOG_CONFIG_FILE_PATH)) {
            LogManager.getLogManager().readConfiguration(ins);
            LOGGER = Logger.getLogger(WebServer.class.getName());
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
    }


    private static String[] parts;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            LOGGER.info("Server started on port 8080");

            while (true) {
                Socket socket = serverSocket.accept();
                LOGGER.info("Client connected");

                new Thread(() -> handleRequest(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleRequest(Socket socket) {
        try (BufferedReader input = new BufferedReader(
                new InputStreamReader(
                        socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter output = new PrintWriter(socket.getOutputStream())
        ) {
            readRequest(input);
            writeResponse(output);
            LOGGER.info("Client disconnected");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readRequest(BufferedReader input) throws IOException {
        while (!input.ready()) ;

        String firstLine = input.readLine();
        parts = firstLine.split(" ");
        System.out.println(firstLine);
        while (input.ready()) {
            System.out.println(input.readLine());
        }
    }

    private static void writeResponse(PrintWriter output) throws IOException {
        switch (parts[0]) {
            case GET: {
                getRequestProcessing(output);
                break;
            }
            case POST: {
                postRequestProcessing(output);
                break;
            }
            case OPTIONS: {
                optionsRequestProcessing(output);
                break;
            }
            default: {
                output.println("HTTP/1.1 501 NOT_IMPLEMENTED");
                output.println("Content-Type: text/html; charset=utf-8");
                output.println();
                output.println("<h1>Not Implemented</h1>");
                output.flush();
                LOGGER.warning("501 NOT IMPLEMENTED");
            }
        }
        Path path = Paths.get(RESOURCES_PATH, parts[1]);
        if (!Files.exists(path)) {
            output.println("HTTP/1.1 404 NOT_FOUND");
            output.println("Content-Type: text/html; charset=utf-8");
            output.println();
            output.println("<h1>File not found!</h1>");
            output.flush();
            LOGGER.warning("404 NOT FOUND");
            return;
        }

        output.println("HTTP/1.1 200 OK");
        output.println("Content-Type: text/html; charset=utf-8");
        output.println();

        Files.newBufferedReader(path).transferTo(output);
        LOGGER.info("200 OK");
    }

    private static void getRequestProcessing(PrintWriter output) {

    }

    private static void postRequestProcessing(PrintWriter output) {

    }

    //    > OPTIONS /api/strings/ HTTP/1.1
//            > Host: localhost:8080
//            > User-Agent: curl/7.52.1
//            > Accept: */*
//>
//< HTTP/1.1 200
//< Allow: GET,HEAD
//< Content-Length: 0
//< Date: Tue, 01 May 2018 16:40:21 GMT
    private static void optionsRequestProcessing(PrintWriter output) {

    }

    private static void write(PrintWriter output, Path path, String result) throws IOException {
        output.println("HTTP/1.1 " + result);
        output.println("Content-Type: text/html; charset=utf-8");
        output.println();

        Files.newBufferedReader(path).transferTo(output);
        LOGGER.info(result);
    }
}