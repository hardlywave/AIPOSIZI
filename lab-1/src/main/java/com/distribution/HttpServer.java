package com.distribution;

import com.distribution.enums.ContentType;
import com.distribution.enums.HttpCode;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.time.Instant;
import java.util.Objects;
import java.util.StringTokenizer;

import static com.distribution.Main.DIRECTORY_PATH;
import static com.distribution.enums.ContentType.findByFileName;
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
                    processPost(fileRequested);
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
        CreatorHTML html = new CreatorHTML(fileRequested);
        if ((DIRECTORY_PATH + fileRequested).contains(Main.FORBIDDEN)) {
            createResponse(FORBIDDEN, new CreatorHTML(FORBIDDEN));
            return;
        }
        if ("".equals(html.getFileHTML())) {
            InputStream inputStream = findFile(fileRequested);
            ContentType content = findByFileName(fileRequested);
            byte[] data = content.getReader().read(inputStream);
            createResponse(OK, content, data.length, data);
            log.info(format("File %s of type %s returned", fileRequested, content.getText()));
        } else {
            createResponse(OK, html);
        }
    }

    private InputStream findFile(String fileName) throws FileNotFoundException {
        File file = new File(DIRECTORY_PATH + fileName);
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        return new FileInputStream(file.getPath());
    }

    private void processPost(String path) throws IOException {
        log.info("POST request was accepted");
        File file = new File(DIRECTORY_PATH + path);
        if (file.getPath().contains(Main.FORBIDDEN)) {
            createResponse(FORBIDDEN, new CreatorHTML(FORBIDDEN));
            return;
        }
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        if (!getFileExtension(file).equals("txt")) {
            createResponse(INTERNAL_SERVER_ERROR, new CreatorHTML(INTERNAL_SERVER_ERROR));
            return;
        }
        FileInputStream inputStream = new FileInputStream(file.getPath());
        var data = new byte[inputStream.available()];
        inputStream.read(data);

        out.println(format("HTTP/1.1 %s %s", OK.getCode(), OK.getDescription()));
        out.println("Server: HTTP Server");
        out.println(format("Date: %s", Instant.now()));
        out.println("Access-Control-Allow-Origin: localhost");
        out.println("Access-Control-Allow-Methods: GET, POST, OPTIONS");
        out.println("Content-Type: text/plain");
        out.println();
        out.flush();

        dataOut.write(data, 0, data.length);
        dataOut.flush();
    }

    private static String getFileExtension(File file) {
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        else return "";
    }

    private void processOptions() throws IOException {
        log.info("OPTIONS request was accepted");
        createResponse(OK, null);
    }

    private void methodNotAllowed(String method) throws IOException {
        createResponse(NOT_IMPLEMENTED, new CreatorHTML(NOT_IMPLEMENTED));
        log.warn("Unknown method: " + method);
    }

    private void createResponse(HttpCode code, ContentType content, int fileLength, byte[] fileData)
            throws IOException {
        out.println(format("HTTP/1.1 %s %s", code.getCode(), code.getDescription()));
        out.println("Server: HTTP Server");
        out.println(format("Date: %s", Instant.now()));
        out.println(format("Content-type: %s", content.getText()));
        out.println(format("Content-length: %s", fileLength));
        out.println("Access-Control-Allow-Origin: localhost");
        out.println("Access-Control-Allow-Methods: GET, POST, OPTIONS");
        out.println();
        out.flush();

        dataOut.write(fileData, 0, fileLength);
        dataOut.flush();
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
//                Download.download(html.getFolder(), dataOut);
            } else {
                out.println(html.getFileHTML());
            }
        }
        out.flush();
        log.info("Creating header of response with code " + code.getCode());
    }
}