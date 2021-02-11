package com.distribution;

import com.distribution.enums.HttpCode;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.time.Instant;
import java.util.Objects;
import java.util.StringTokenizer;

import static com.distribution.enums.HttpCode.*;
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
            createResponse(NOT_FOUND, new CreatorHTML(NOT_FOUND));
        } catch (IOException ioe) {
            createResponse(INTERNAL_SERVER_ERROR, new CreatorHTML(INTERNAL_SERVER_ERROR));
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
        StringBuilder builder = new StringBuilder();
        while (true) {
            String s = in.readLine();
            builder.append(s);
            if (s == null || s.trim().length() == 0) {
                break;
            }
        }
        out.println(format("HTTP/1.1 %s %s", OK.getCode(), OK.getDescription()));
        out.println("Server: HTTP Server");
        out.println(format("Date: %s", Instant.now()));
        out.println("Access-Control-Allow-Origin: localhost");
        out.println("Access-Control-Allow-Methods: GET, POST, OPTIONS");
        out.println("Content-Type: text/html; charset=utf-8");
        out.println();
        out.println(builder.toString());
    }

    private void processOptions() throws IOException {
        log.info("OPTIONS request was accepted");
        createResponse(OK, null);
    }

    private void methodNotAllowed(String method) throws IOException {
        createResponse(NOT_IMPLEMENTED, new CreatorHTML(NOT_IMPLEMENTED));
        log.warn("Unknown method: " + method);
    }

    private void createResponse(HttpCode code, CreatorHTML html) {
        out.println(format("HTTP/1.1 %s %s", code.getCode(), code.getDescription()));
        out.println("Server: HTTP Server");
        out.println(format("Date: %s", Instant.now()));
        out.println("Access-Control-Allow-Origin: localhost");
        out.println("Access-Control-Allow-Methods: GET, POST, OPTIONS");
        out.println("Content-Type: text/html; charset=utf-8");
        out.println();
        if (!Objects.isNull(html)) {
            if ("".equals(html.getFileHTML())) {
                Download.download(html.getFolder(), dataOut);
            } else {
                out.println(html.getFileHTML());
            }
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
