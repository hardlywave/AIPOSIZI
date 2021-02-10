package com.distribution;

import java.io.*;
import java.net.*;
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
    private static Socket socket;
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            LOGGER.info("Server started on port 8080");

            while (true) {
                socket = serverSocket.accept();
                LOGGER.info("Client connected");
                //Короче в отдельном файле я сделал так, чтобы файл PDF скачивался, я его сюда сейчас запущу
                new Thread(() -> handleRequest()).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleRequest() {
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
                write(output, Paths.get(CODE_501_PATH), CODE_501);
                LOGGER.warning(CODE_501);
            }
        }

        Path path = Paths.get(RESOURCES_PATH, parts[1]);
        if (!Files.exists(path)) {
            write(output, Paths.get(CODE_404_PATH), CODE_404);
            LOGGER.warning(CODE_404);
            return;
        }
        write(output, path, CODE_200);
        LOGGER.info(CODE_200);
    }

    private static void getRequestProcessing(PrintWriter output) throws IOException {
        String mainPage = "/HelloWorld.html";
        if(parts[1].equals("/")){
            parts[1] = mainPage;
            write(output, Paths.get(RESOURCES_PATH + mainPage), CODE_200);
        }else {
            switch (parts[1]) {
                case "/picture.jpg": {
                    write(output, Paths.get(RESOURCES_PATH + "/picture.jpg"), CODE_200);
                    break;
                }
                case "/downloadArchive": {

                    break;
                }
                case "/downloadPDF": {
                    new DownloadPDFTest(socket);
//                    write(output, Paths.get(DIRECTORY_PATH + "/labPBZ.pdf"), CODE_200);
                    break;
                } default:{
                    write(output, Paths.get(CODE_404_PATH), CODE_404);
                    LOGGER.warning(CODE_404);
                }
            }
        }
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