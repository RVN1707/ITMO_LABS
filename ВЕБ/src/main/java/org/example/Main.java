package org.example;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
    public static void main(String[] args) {
        System.setProperty("FCGI_PORT", "24047");
        FCGIInterface fcgi = new FCGIInterface();

        while (fcgi.FCGIaccept() >= 0) {
            long startTime = System.currentTimeMillis();

            try {
                String jsonInput = readJsonInput();
                double x = parseDoubleFromJson(jsonInput, "x");
                double y = parseDoubleFromJson(jsonInput, "y");
                double r = parseDoubleFromJson(jsonInput, "r");

                boolean hit = checkTargetHit(x, y, r);
                long executionTime = System.currentTimeMillis() - startTime; // Время выполнения
                String currentTime = getCurrentTime(); // Текущее время

                // Формируем ответ
                String response = String.format(
                        "{" +
                                "\"success\": true," +
                                "\"result\": {" +
                                "  \"x\": %.2f," +
                                "  \"y\": %.2f," +
                                "  \"r\": %.2f," +
                                "  \"hit\": %b," +
                                "  \"execution_time\": %d," +
                                "  \"current_time\": \"%s\"," +
                                "  \"hit_icon\": \"%s\"" +
                                "}" +
                                "}",
                        x, y, r, hit, executionTime, currentTime, hit ? "✅" : "❌"
                );

                System.out.println("Content-type: application/json");
                System.out.println();
                System.out.println(response);

            } catch (Exception e) {
                long executionTime = System.currentTimeMillis() - startTime;

                String errorResponse = String.format(
                        "{" +
                                "\"success\": false," +
                                "\"error\": \"%s\"," +
                                "\"execution_time\": %d" +
                                "}",
                        e.getMessage(), executionTime
                );

                System.err.println("Error: " + e.getMessage());
                System.out.println("Content-type: application/json");
                System.out.println();
                System.out.println(errorResponse);
            }
        }
    }

    private static String getCurrentTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }

    private static String readJsonInput() throws IOException {
        String contentLengthStr = System.getProperty("CONTENT_LENGTH");
        if (contentLengthStr == null || contentLengthStr.isEmpty()) {
            return "{}";
        }

        int contentLength = Integer.parseInt(contentLengthStr);
        byte[] buffer = new byte[contentLength];
        int bytesRead = System.in.read(buffer);
        return new String(buffer, 0, bytesRead, "UTF-8");
    }

    private static double parseDoubleFromJson(String json, String key) {
        int keyIndex = json.indexOf("\"" + key + "\"");
        if (keyIndex == -1) {
            throw new RuntimeException("Key '" + key + "' not found in JSON");
        }

        int valueStart = json.indexOf(":", keyIndex) + 1;
        int valueEnd = json.indexOf(",", valueStart);
        if (valueEnd == -1) {
            valueEnd = json.indexOf("}", valueStart);
        }

        if (valueEnd == -1) {
            throw new RuntimeException("Invalid JSON format");
        }

        String valueStr = json.substring(valueStart, valueEnd).trim();
        return Double.parseDouble(valueStr);
    }

    private static boolean checkTargetHit(double x, double y, double r) {
        if (x >= -r/2 && x <= 0 && y >= 0 && y <= r) {
            return true;
        }

        if (x >= 0 && x <= r/2 && y <= 0 && y >= -r && y >= -2*x - r) {
            return true;
        }

        if (x <= 0 && y <= 0 && (x*x + y*y) <= (r/2)*(r/2)) {
            return true;
        }

        return false;
    }
}