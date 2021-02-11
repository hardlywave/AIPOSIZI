package com.distribution;

import java.io.File;
import java.io.OutputStream;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Download {
    public static void download(File file, OutputStream out) {
        try {
            //Для понимания, какой файл нужно скачать, выесняет на что заканчивается адресс сервера и создает название файла
            //
            String fileName = file.getName();
            out.write("HTTP/1.1 200 OK\r\n".getBytes());
            out.write("Accept-Ranges: bytes\r\n".getBytes());
            out.write(("Content-Length: " + file.length() + "\r\n").getBytes());
            out.write("Content-Type: application/octet-stream\r\n".getBytes());
            out.write(("Content-Disposition: attachment; filename=\"" + fileName + "\"\r\n").getBytes());
            out.write("\r\n".getBytes()); // Added as Joy Rê proposed, make it work!
            Files.copy(Paths.get(file.getPath()), out);
            System.out.println("File upload completed!");
            out.close();
        } catch (SocketException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
