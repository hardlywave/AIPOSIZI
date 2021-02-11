package com.distribution;

import com.distribution.enums.HttpCode;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.StringTokenizer;

import static com.distribution.Constant.CODE_501_PATH;
import static com.distribution.enums.HttpCode.NOT_FOUND;
import static com.distribution.enums.HttpCode.NOT_IMPLEMENTED;
import static java.lang.String.format;

public class HttpServer implements Runnable {

    private final Socket socket;
    private static final Logger log = LogManager.getLogger(HttpServer.class);

    private BufferedReader in;
    private PrintWriter out;
    private BufferedOutputStream dataOut;

    public HttpServer(Socket accept) {
        socket = accept;
    }

    @Override
    public void run() {
        test();
    }

    private void test() {
        String fileRequested = "";
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
            dataOut = new BufferedOutputStream(socket.getOutputStream());

            String input = in.readLine();
            log.info("Input is: " + input);

            StringTokenizer parse;
            try {
                parse = new StringTokenizer(input);
            } catch (NullPointerException e) {
                return;
            }

            String method = parse.nextToken();
            log.info("Request method: " + method);
            fileRequested = parse.nextToken();

            switch (method) {
                case "GET":
                    processGet(fileRequested);
                    break;
                case "POST":
                    processPost();
                    break;
                case "OPTIONS":
                    processOptions();
                    break;
                default:
                    methodNotAllowed(method);
                    break;
            }
            log.info("File " + fileRequested + "returned");
        } catch (FileNotFoundException fnfe) {
            log.warn(NOT_FOUND);
            log.warn("File: " + fileRequested + "not found, load");
        } catch (IOException ioe) {
            log.error("Server error: " + ioe.getMessage());
        } finally {
            try {
                in.close();
                out.close();
                dataOut.close();
                socket.close();
            } catch (Exception e) {
                log.error("Error closing stream : " + e.getMessage());
            }
            log.info("Connection closed");
        }
    }

    private void processGet(String fileRequested) throws IOException {
        log.info("GET request was accepted");

        createResponse(HttpCode.OK, new CreatorHTML(fileRequested));
    }

    private void processPost() throws IOException {

    }

    private void processOptions() throws IOException {

    }

    private void methodNotAllowed(String method) throws IOException {
        log.warn("Unknown method: " + method);
        createBadResponse(NOT_IMPLEMENTED, Paths.get(CODE_501_PATH));
        //
    }

    private void createBadResponse(HttpCode code, Path path) throws IOException {
        out.println(format("HTTP/1.1 %s %s", code.getCode(), code.getDescription()));
        out.println("Server: HTTP Server");
        out.println(format("Date: %s", Instant.now()));
        out.println("Access-Control-Allow-Origin: localhost");
        out.println("Access-Control-Allow-Methods: GET, POST, OPTIONS");
        out.println("Content-Type: text/html; charset=utf-8");
        out.println();

        Files.newBufferedReader(path).transferTo(out);
        out.flush();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException ignored) {
            log.info("Thread error.");
        }
        log.info("Creating header of response with code " + code.getCode());
    }

    private void createResponse(HttpCode code, CreatorHTML html) {
        out.println(format("HTTP/1.1 %s %s", code.getCode(), code.getDescription()));
        out.println("Server: HTTP Server");
        out.println(format("Date: %s", Instant.now()));
        out.println("Access-Control-Allow-Origin: localhost");
        out.println("Access-Control-Allow-Methods: GET, POST, OPTIONS");
        out.println("Content-Type: text/html; charset=utf-8");
        out.println();
        if ("".equals(html.getFileHTML())) {
            Download.download(html.getFolder(), dataOut);
        } else {
            out.println(html.getFileHTML());
        }
        out.flush();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException ignored) {
            log.info("Thread error.");
        }
        log.info("Creating header of response with code " + code.getCode());
    }
}
