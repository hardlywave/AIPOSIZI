package com.distribution.readers;

import java.io.IOException;
import java.io.InputStream;

public interface Reader {
    byte[] read(InputStream inputStream) throws IOException;
}