package org.example;

import java.util.List;

public class PointResponse {
    private final boolean success;
    private final List<ResultData> results;
    private final ResultData newResult;
    private final String error;
    private final Long execution_time;

    public PointResponse(boolean success, List<ResultData> results, ResultData newResult, String error, Long executionTime) {
        this.success = success;
        this.results = results;
        this.newResult = newResult;
        this.error = error;
        this.execution_time = executionTime;
    }

    public static PointResponse success(List<ResultData> allResults, ResultData newResult, Long executionTime) {
        return new PointResponse(true, allResults, newResult, null, executionTime);
    }

    public static PointResponse error(String error) {
        return new PointResponse(false, null, null, error, null);
    }
}