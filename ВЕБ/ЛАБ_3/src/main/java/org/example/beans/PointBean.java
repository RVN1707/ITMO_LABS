package org.example.beans;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.example.entities.PointResult;
import org.example.utils.AreaChecker;
import org.example.services.PointRepository;
import org.example.utils.Validator;
import java.io.Serializable;
import java.util.List;

@Named("pointBean")
@SessionScoped
public class PointBean implements Serializable {

    private Double x;
    private Double y;
    private Double r = 1.0;

    @Inject
    private PointRepository pointRepository;

    @Inject
    private AreaChecker areaChecker;

    @Inject
    private Validator validator;

    public String checkPoint() {
        System.out.println("checkPoint вызван с: x=" + x + ", y=" + y + ", r=" + r);

        if (!validator.isValid(x, y, r)) {
            return "";
        }

        // Вычисляем время выполнения и проверяем точку
        long startTime = System.nanoTime();
        boolean hit = areaChecker.checkPoint(x, y, r);
        long execTime = (System.nanoTime() - startTime) / 1000;

        // Сохраняем
        PointResult result = new PointResult(x, y, r, hit, execTime);
        pointRepository.save(result);

        return "main";
    }

    // Получение всех результатов
    public List<PointResult> getResults() {
        return pointRepository.findAll();
    }

    public String clearHistory() {
        pointRepository.deleteAll();
        return "main";
    }

    public Double getX() {
        return x;
    }
    public void setX(Double x) {
        this.x = x;
    }
    public Double getY() {
        return y;
    }
    public void setY(Double y) {
        this.y = y;
    }
    public Double getR() {
        return r;
    }
    public void setR(Double r) {
        this.r = r;
    }
}