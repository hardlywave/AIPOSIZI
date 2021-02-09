package com.distribution;

public class Constant {
    public static final String LOG_CONFIG_FILE_PATH = System.getProperty("user.dir") + "/log/log.config";
    public static final String DIRECTORY_PATH = System.getProperty("user.dir") + "/directory";
    public static final String RESOURCES_PATH = System.getProperty("user.dir") + "/src/main/resources";
    public static final String CODE_404_PATH = RESOURCES_PATH + "/code404.html";
    public static final String CODE_501_PATH = RESOURCES_PATH + "/code501.html";

    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String OPTIONS = "OPTIONS";


    public static final String CODE_200 = "200 OK";
    public static final String CODE_403 = "403 Forbidden";
    public static final String CODE_404 = "404 Not found";
    public static final String CODE_501 = "501 Not implemented";

}
