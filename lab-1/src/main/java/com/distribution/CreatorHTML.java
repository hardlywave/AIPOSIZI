package com.distribution;

import com.distribution.enums.HttpCode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class CreatorHTML {

    private String fileHTML = "";
    private String path;
    private File folder;
    private final String DIRECTORY_PATH = System.getProperty("user.dir") + "/directory";

    public CreatorHTML(String path) throws FileNotFoundException {
        this.path = path;
        File file = new File(DIRECTORY_PATH + path);
        if (!file.exists() && !file.isDirectory()) {
            throw new FileNotFoundException();
        }
        folder = new File(DIRECTORY_PATH + path);
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles == null) {
            System.out.println("It is file!");
            return;
        }
        List<File> files = Arrays.stream(listOfFiles).filter(File::isFile).collect(Collectors.toList());
        List<File> directories = Arrays.stream(listOfFiles).filter(x -> !x.isFile()).collect(Collectors.toList());
        createFileHTML(files, directories);
    }

    public CreatorHTML(HttpCode code) {
        createStartFile();
        fileHTML = fileHTML + createTag(1,code.getDescription());
        createEndFile();
    }

    private void createFileHTML(List<File> files, List<File> directories) {
        createStartFile();
        fileHTML = fileHTML + createTag(1, path);
        createTags(false, directories);
        createTags(true, files);
        createEndFile();
    }

    private void createStartFile() {
        fileHTML = "<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n<meta charset=\"UTF-8\">\n";
    }

    private void createEndFile() {
        fileHTML = fileHTML + "</head>\n" + "</html>";
    }

    private void createTags(boolean checkFiles, List<File> list) {
        if (list.isEmpty()) return;
        StringBuilder builder = new StringBuilder(fileHTML);
        builder.append(createTag(2, (checkFiles) ? "Download files:" : "Open directory"));
        for (File file : list) {
            builder.append((!checkFiles) ? createTagWithLink(file) : createTagWithLinkDownload(file));
        }
        fileHTML = builder.toString();
    }

    private String createTag(int size, String message) {
        return format("<h%d>%s</h%d>\n", size, message, size);
    }

    private String createTagWithLink(File file) {
        String path = file.getPath().substring(DIRECTORY_PATH.length());
        return format("<p><a href=\"http://localhost:8080%s\"> download %s</a></p>\n", path, file.getName());
    }

    private String createTagWithLinkDownload(File file) {
        String path = file.getPath().substring(DIRECTORY_PATH.length());
        return format("<p><a href=\"http://localhost:8080%s\" download> download %s</a></p>\n", path, file.getName());
    }

    public String getFileHTML() {
        return fileHTML;
    }

    public File getFolder() {
        return folder;
    }
}