package com.distribution;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class HTML {

    public static void main(String[] args) {
        HTML html = new HTML("/test");
        System.out.println(html.getFileHTML());
    }

    private String fileHTML;
    private final String path;

    public HTML(String path) {
        this.path = path;
        File folder = new File(Constant.DIRECTORY_PATH + path);
        if (folder.isFile()) {
            System.out.println("It is file!");
            return;
        }
        File[] listOfFiles = folder.listFiles();
        assert listOfFiles != null;
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

    private void createTags(boolean checkFiles, List<File> list){
        if(list.isEmpty()) return;
        StringBuilder builder = new StringBuilder(fileHTML);
        builder.append(createTag(2, (checkFiles) ? "Open files:" : "Open directory"));
        for (File file : list) {
            builder.append(createTagWithLink(file));
        }
        fileHTML = builder.toString();
    }

    // name?
    private String createTag(int size, String message) {
        return format("<h%d>%s</h%d>\n", size, message, size);
    }

    private String createTagWithLink(File file) {
        String path = file.getPath().substring(0, Constant.DIRECTORY_PATH.length()) + "/" + file.getName();
        return format("<p><a href=\"http://localhost:8080%s\"> Open %s</a></p>\n", path, file.getName());
    }

    public String getFileHTML() {
        return fileHTML;
    }
}