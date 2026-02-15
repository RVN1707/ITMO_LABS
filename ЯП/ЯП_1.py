import ctypes
import os
import random

class Point(ctypes.Structure):
    _fields_ = [("x", ctypes.c_int), ("y", ctypes.c_int)] 

def generate_points_file(filename="points.txt", num_pairs=1000):
    """Генерирует файл с парами точек"""
    with open(filename, 'w') as f:
        for i in range(num_pairs):
            x1, y1 = random.randint(-100, 100), random.randint(-100, 100)
            x2, y2 = random.randint(-100, 100), random.randint(-100, 100)
            f.write(f"{x1},{y1} {x2},{y2}\n")
    print(f"Сгенерирован файл {filename} с {num_pairs} парами точек")

def read_points_from_file(filename="points.txt"):
    """Читает точки из файла и возвращает массив структур Point"""
    points = []
    with open(filename, 'r') as f:
        for line in f:
            line = line.strip()
            if not line:
                continue
                
            # Разделяем на две точки
            point1_str, point2_str = line.split()
            
            # Парсим первую точку
            x1, y1 = map(int, point1_str.split(','))
            points.append(Point(x=x1, y=y1))
            
            # Парсим вторую точку
            x2, y2 = map(int, point2_str.split(','))
            points.append(Point(x=x2, y=y2))
    
    return points

if __name__ == "__main__":
    print("Start program")
    
    # Генерируем файл с точками
    generate_points_file("points.txt", 1000)
    
    # Читаем точки из файла
    points_array = read_points_from_file("points.txt")
    num_pairs = len(points_array) // 2
    print(f"Прочитано {num_pairs} пар точек")
    
    lib_name = "point_lib.dll" if os.name == 'nt' else "point_lib.so"
    lib_path = os.path.join(os.path.dirname(os.path.abspath(__file__)), lib_name)

    c_lib = ctypes.CDLL(lib_path)
    print(f"Библиотека успешно загружена: {lib_path}")

    

    #  Подготавливаем массивы для C-функции
   
    points_ctypes = (Point * len(points_array))(*points_array)
    
    # Массив для результатов (расстояний)
    results_array = (ctypes.c_double * num_pairs)()
    
    # Вызываем C-функцию для вычисления расстояний
    print("Вычисление расстояний...")
    c_lib.calculate_distances(points_ctypes, results_array, num_pairs)
    
    # Выводим результаты
    print("\nПервые 10 результатов:")
    for i in range(min(10, num_pairs)):
        p1 = points_array[i*2]
        p2 = points_array[i*2 + 1]
        distance = results_array[i]
        print(f"Пара {i+1}: ({p1.x},{p1.y}) - ({p2.x},{p2.y}) = {distance:.2f}")
    
    # Дополнительная статистика
    if num_pairs > 0:
        max_dist = max(results_array)
        min_dist = min(results_array)
        avg_dist = sum(results_array) / num_pairs
        print(f"\nСтатистика:")
        print(f"Минимальное расстояние: {min_dist:.2f}")
        print(f"Максимальное расстояние: {max_dist:.2f}")
        print(f"Среднее расстояние: {avg_dist:.2f}")
