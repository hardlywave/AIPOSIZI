package com.distribution;

import com.distribution.config.Config;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class WebServer {
    private static Logger LOGGER;

    static {
        try (FileInputStream ins = new FileInputStream(Config.LOG_CONFIG_FILE_PATH)) {
            LogManager.getLogManager().readConfiguration(ins);
            LOGGER = Logger.getLogger(WebServer.class.getName());
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
    }

    public static void main(String[] args) throws Throwable {
        ServerSocket serverSocket = new ServerSocket(8080);
        LOGGER.info("Server started on port 8080");
        while (true) {
            Socket socket = serverSocket.accept();
            LOGGER.info("Client accepted");
            new Thread(new SocketProcessor(socket)).start();
        }
    }

    private static class SocketProcessor implements Runnable {

        private final Socket socket;
        private final InputStream input;
        private final OutputStream output;
        private String request;

        private SocketProcessor(Socket s) throws Throwable {
            this.socket = s;
            this.input = s.getInputStream();
            this.output = s.getOutputStream();
        }

        public void run() {
            try {
                readInputHeaders();
                writeResponse("<html><body><h1>Hello from Habrahabr</h1></body></html>");
            } catch (Throwable t) {
                /*do nothing*/
            } finally {
                try {
                    socket.close();
                } catch (Throwable t) {
                    /*do nothing*/
                }
            }
            LOGGER.info("Client processing finished");
        }

        private void writeResponse(String s) throws Throwable {
            String response = "HTTP/1.1 200 OK\r\n" +
                    "Server: YarServer/2009-09-09\r\n" +
                    "Content-Type: text/html\r\n" +
                    "Content-Length: " + s.length() + "\r\n" +
                    "Connection: close\r\n\r\n";
            String result = response + s;
            output.write(result.getBytes());
            output.flush();
        }

        private void readInputHeaders() throws Throwable {
            BufferedReader br = new BufferedReader(new InputStreamReader(input));
            StringBuilder builder = new StringBuilder();
            while (true) {
                String s = br.readLine();
                if (s == null || s.trim().length() == 0) {
                    break;
                }
                builder.append(s);
            }
            request = builder.toString();
        }
    }
}
