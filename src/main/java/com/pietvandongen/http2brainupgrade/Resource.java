package com.pietvandongen.http2brainupgrade;

public class Resource {

    private final String path;
    private final String fileName;

    public static Resource create(String path, String fileName) {
        return new Resource(path, fileName);
    }

    private Resource(String path, String fileName) {
        this.path = path;
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public String getFileName() {
        return fileName;
    }
}
