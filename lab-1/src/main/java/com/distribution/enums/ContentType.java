package com.distribution.enums;

import com.distribution.readers.FileReader;
import com.distribution.readers.ImageReader;
import com.distribution.readers.Reader;

import java.util.Arrays;

public enum ContentType {
    PLAIN("text/plain", "txt", new FileReader()),
    HTML("text/html", "html", new FileReader()),
    CSS("text/css", "css", new FileReader()),
    JS("application/javascript", "js", new FileReader()),
    PDF("application/pdf", "pdf", new FileReader()),
    SVG("image/svg+xml", "svg", new FileReader()),
    PNG("image/png", "png", new ImageReader()),
    JPEG("image/jpeg", "jpeg", new ImageReader()),
    GIF("image/gif", "gif", new ImageReader());

    private final String text;
    private final String extension;
    private final Reader reader;


    ContentType(String text, String extension, Reader reader) {
        this.text = text;
        this.extension = extension;
        this.reader = reader;
    }

    public String getText() {
        return text;
    }

    public String getExtension() {
        return extension;
    }

    public Reader getReader() {
        return reader;
    }

    public static ContentType findByFileName(String fileName) {
        var extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        return Arrays.stream(ContentType.values())
                .filter(x -> x.getExtension().equalsIgnoreCase(extension))
                .findFirst()
                .orElse(PLAIN);
    }
}
