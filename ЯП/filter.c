#include <stdlib.h>

typedef struct {
    double x;
    double y;
} Point;

typedef int (*FilterFunc)(Point);

void filter(Point* points, int count, FilterFunc predicate, Point** out_points, int* out_count) {
    Point* temp = (Point*)malloc(count * sizeof(Point));
    int result_count = 0;

    for (int i = 0; i < count; i++) {
        if (predicate(points[i])) {
            temp[result_count++] = points[i];
        }
    }

    *out_points = (Point*)malloc(result_count * sizeof(Point));
    for (int i = 0; i < result_count; i++) {
        (*out_points)[i] = temp[i];
    }

    *out_count = result_count;
    free(temp);
}

void free_points(Point* points) {
    free(points);
}
