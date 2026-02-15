#include <stdio.h>
#include <math.h>

typedef struct Point {
    int x;
    int y;
} Point;

// Функция для вычисления расстояния между двумя точками
double distance_between_points(Point p1, Point p2) {
    int dx = p2.x - p1.x;
    int dy = p2.y - p1.y;
    return sqrt(dx*dx + dy*dy);
}

// Основная функция для обработки массива пар точек
void calculate_distances(Point* pairs, double* results, int num_pairs) {
    for (int i = 0; i < num_pairs; i++) {
        Point p1 = pairs[i*2];
        Point p2 = pairs[i*2 + 1];  
        results[i] = distance_between_points(p1, p2);
    }
}