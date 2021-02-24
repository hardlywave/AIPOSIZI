package com.distribution.readers;

import java.io.IOException;
import java.io.InputStream;

public class FileReader implements Reader {

    @Override
    public byte[] read(InputStream inputStream) throws IOException {
        var fileData = new byte[inputStream.available()];
        try (inputStream) {
            inputStream.read(fileData);
        }
        return fileData;
    }
}