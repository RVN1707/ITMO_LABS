package org.example.Parsers.FileParsers;

import java.util.Map;

public abstract class FileParser {
    public Map<String, String> map;
    public void setMap(Map<String, String> map) {
        this.map = map;
    }
}
