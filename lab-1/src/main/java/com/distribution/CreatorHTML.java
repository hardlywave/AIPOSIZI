package com.distribution;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class CreatorHTML {

    private String fileHTML = "";
    private final String path;
    private final File folder;

    public CreatorHTML(String path) {
        this.path = path;
        folder = new File(Constant.DIRECTORY_PATH + path);
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles == null) {
            System.out.println("It is file!");
            return;
        }
        List<File> files = Arrays.stream(listOfFiles).filter(File::isFile).collect(Collectors.toList());
        List<File> directories = Arrays.stream(listOfFiles).filter(x -> !x.isFile()).collect(Collectors.toList());
        createFileHTML(files, directories);
    }

    private void createFileHTML(List<File> files, List<File> directories) {
        createStartFile();
        createTags(false, directories);
        createTags(true, files);
        createEndFile();
    }

    private void createStartFile() {
        fileHTML = "<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n<meta charset=\"UTF-8\">\n" + createTag(1, path);
    }

    private void createEndFile() {
        fileHTML = fileHTML + "</head>\n" + "</html>";
    }

    private void createTags(boolean checkFiles, List<File> list) {
        if (list.isEmpty()) return;
        StringBuilder builder = new StringBuilder(fileHTML);
        builder.append(createTag(2, (checkFiles) ? "Download files:" : "Download directory"));
        for (File file : list) {
            builder.append((!checkFiles) ? createTagWithLink(file) : createTagWithLinkDownload(file));
        }
        fileHTML = builder.toString();
    }

    private String createTag(int size, String message) {
        return format("<h%d>%s</h%d>\n", size, message, size);
    }

    private String createTagWithLink(File file) {
        String path = file.getPath().substring(Constant.DIRECTORY_PATH.length());
        return format("<p><a href=\"http://localhost:8080%s\"> download %s</a></p>\n", path, file.getName());
    }

    private String createTagWithLinkDownload(File file) {
        String path = file.getPath().substring(Constant.DIRECTORY_PATH.length());
        return format("<p><a href=\"http://localhost:8080%s\" download> download %s</a></p>\n", path, file.getName());
    }

    public String getFileHTML() {
        return fileHTML;
    }

    public File getFolder() {
        return folder;
    }
}