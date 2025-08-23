package org.example.FileParsers;

import java.util.Map;

public abstract class FileParser {
    protected Map<String, String> map;
    public void setMap(Map<String, String> map) {
        this.map = map;
    }
}
