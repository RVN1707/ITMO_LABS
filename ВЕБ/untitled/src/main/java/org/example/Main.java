package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Main {
    private static final int HTTP_BAD_REQUEST = 400;
    private static final int HTTP_INTERNAL_ERROR = 500;

    // Коллекция для хранения всех результатов
    private static final List<ResultData> allResults = new CopyOnWriteArrayList<>();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        // Игнорируем null поля в JSON
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static void main(String[] args) throws JsonProcessingException {
        System.setProperty("FCGI_PORT", "24047");
        FCGIInterface fcgi = new FCGIInterface();

        while (fcgi.FCGIaccept() >= 0) {
            long startTime = System.currentTimeMillis();
            PointResponse response;

            try {
                String jsonInput = readJsonInput();

                // Парсим общий запрос
                BaseRequest baseRequest = objectMapper.readValue(jsonInput, BaseRequest.class);

                if ("clear".equals(baseRequest.getAction())) {
                    handleClearRequest();
                } else if ("get_data".equals(baseRequest.getAction())) {
                    handleGetDataRequest();
                } else if (baseRequest.getX() != null && baseRequest.getY() != null && baseRequest.getR() != null) {
                    // Запрос на проверку точки
                    PointRequest request = new PointRequest(baseRequest.getX(), baseRequest.getY(), baseRequest.getR());

                    // Валидация
                    PointValidator.ValidationResult validation = PointValidator.validate(request);
                    if (!validation.isValid()) {
                        response = PointResponse.error(validation.getErrorMessage());
                        sendErrorResponse(HTTP_BAD_REQUEST, response);
                        continue;
                    }

                    // Проверка попадания
                    boolean hit = TargetChecker.checkHit(request.getX(), request.getY(), request.getR());
                    long executionTime = System.currentTimeMillis() - startTime;
                    String currentTime = getCurrentTime();

                    // Создание нового результата
                    ResultData newResult = new ResultData(
                            request.getX(), request.getY(), request.getR(),
                            hit, executionTime, currentTime, hit ? "✅" : "❌"
                    );

                    allResults.add(newResult);

                    // Формирование ответа
                    response = PointResponse.success(allResults, newResult, executionTime);
                    sendSuccessResponse(response);
                } else {
                    // 400 ошибка
                    response = PointResponse.error("Неверный формат запроса. Ожидается: {\"x\":число,\"y\":число,\"r\":число} OR {\"action\":\"clear\"} OR {\"action\":\"get_data\"}");
                    sendErrorResponse(HTTP_BAD_REQUEST, response);
                }

            } catch (JsonProcessingException e) {
                response = PointResponse.error("Неверный формат JSON: " + e.getMessage());
                sendErrorResponse(HTTP_BAD_REQUEST, response);
            } catch (Exception e) {
                response = PointResponse.error("Внутренняя ошибка сервера");
                sendErrorResponse(HTTP_INTERNAL_ERROR, response);
            }
        }
    }

    private static void handleClearRequest() throws JsonProcessingException {
        allResults.clear();
        PointResponse response = PointResponse.success(allResults, null, 0L);
        sendSuccessResponse(response);
    }

    private static void handleGetDataRequest() throws JsonProcessingException {
        PointResponse response = PointResponse.success(allResults, null, 0L);
        sendSuccessResponse(response);
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

    private static void sendSuccessResponse(PointResponse response) throws JsonProcessingException {
        String jsonResponse = objectMapper.writeValueAsString(response);
        System.out.println("Content-type: application/json");
        System.out.println();
        System.out.println(jsonResponse);
        System.out.flush();
    }

    private static void sendErrorResponse(int httpStatus, PointResponse response) throws JsonProcessingException {
        String jsonResponse = objectMapper.writeValueAsString(response);
        System.out.println("Content-type: application/json");
        System.out.println("Status: " + httpStatus);
        System.out.println();
        System.out.println(jsonResponse);
        System.out.flush();
    }
}