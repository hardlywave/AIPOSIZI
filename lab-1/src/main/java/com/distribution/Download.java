package com.distribution;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.distribution.Constant.DIRECTORY_PATH;
import static com.distribution.Constant.RESOURCES_PATH;

public class Download {
    private final int BUFFER_SIZE = 4096;
    private Socket socket;
    private byte[] buf = new byte[BUFFER_SIZE];
    private OutputStream out;
    private InputStream is;
    int numberRead = 0;


    public Download(Socket socket) throws IOException {
        this.socket = socket;
        try {
            out = socket.getOutputStream();
            is = socket.getInputStream();
            numberRead = is.read(buf, 0, BUFFER_SIZE);
            System.out.println("read " + numberRead);


            if (numberRead < 0)
                return;


            byte[] readBuf = new byte[numberRead];
            System.arraycopy(buf, 0, readBuf, 0, numberRead);


            //Для понимания, какой файл нужно скачать, выесняет на что заканчивается адресс сервера и создает название файла
            //
            String header = new String(readBuf);
            System.out.println(header);
            String fileName = header.split("\r\n")[0].split(" ")[1].substring(1);
            System.out.println(socket.getInetAddress().getHostAddress()+" asked for file: "+fileName);

            File f = new File(DIRECTORY_PATH + fileName);

            out.write("HTTP/1.1 200 OK\r\n".getBytes());
            out.write("Accept-Ranges: bytes\r\n".getBytes());
            out.write(("Content-Length: " + f.length() + "\r\n").getBytes());
            out.write("Content-Type: application/octet-stream\r\n".getBytes());
            out.write(("Content-Disposition: attachment; filename=\"" + fileName + "\"\r\n").getBytes());
            out.write("\r\n".getBytes()); // Added as Joy Rê proposed, make it work!
            Files.copy(Paths.get(fileName), out);
            System.out.println("File upload completed!");
            out.close();
        }catch(SocketException e) {
            System.out.println(e.getMessage());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
