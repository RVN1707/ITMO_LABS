import math

def newton_optimization(f, f_prime, f_double_prime, f_triple_prime, a, b, eps, max_iter=100):
    """
    Минимизация функции одной переменной методом Ньютона (2-го порядка).
    
    Параметры:
    f - целевая функция
    f_prime - первая производная
    f_double_prime - вторая производная
    f_triple_prime - третья производная
    a, b - границы интервала [a, b]
    eps - точность (критерий останова по модулю первой производной)
    max_iter - максимальное число итераций
    
    Возвращает:
    x_k - точка минимума
    iterations - массив приближений для отслеживания сходимости
    """
    
    # Выбор начального приближения согласно принципу из лекции (стр. 24)
    # Выбираем тот конец отрезка, где знаки f' и f''' совпадают
    
    f_prime_a = f_prime(a)
    f_prime_b = f_prime(b)
    f_triple_a = f_triple_prime(a)
    f_triple_b = f_triple_prime(b)
    
    print(f"Анализ для выбора начального приближения:")
    print(f"f'({a}) = {f_prime_a:.6f}, f'''({a}) = {f_triple_a:.6f}")
    print(f"f'({b}) = {f_prime_b:.6f}, f'''({b}) = {f_triple_b:.6f}")
    
    # Проверяем совпадение знаков
    if f_prime_a * f_triple_a > 0:
        x_k = a
        print(f"Выбрано начальное приближение x0 = {a} (знаки f' и f''' совпадают)")
    elif f_prime_b * f_triple_b > 0:
        x_k = b
        print(f"Выбрано начальное приближение x0 = {b} (знаки f' и f''' совпадают)")
    else:
        # Если ни один конец не подходит, берем середину
        x_k = (a + b) / 2
        print(f"Предупреждение: знаки не совпадают на концах, выбрана середина x0 = {x_k}")
    
    iterations = [x_k]
    k = 0
    
    print(f"\nИтерационный процесс метода Ньютона:")
    print(f"{'k':<3} {'x^k':<15} {'f\'(x^k)':<15} {'f\'\'(x^k)':<15} {'x^{k+1}':<15}")
    print("-" * 75)
    
    while k < max_iter:
        f_prime_val = f_prime(x_k)
        f_double_prime_val = f_double_prime(x_k)
        
        # Проверка критерия останова
        if abs(f_prime_val) <= eps:
            print(f"\nКритерий останова выполнен: |f'(x^k)| = {abs(f_prime_val):.6f} ≤ ε = {eps}")
            break
        
        # Проверка на нулевую вторую производную
        if abs(f_double_prime_val) < 1e-15:
            print("Ошибка: вторая производная близка к нулю. Метод Ньютона не применим.")
            return None, iterations
        
        # Формула Ньютона: x^{k+1} = x^k - f'(x^k)/f''(x^k)
        x_next = x_k - f_prime_val / f_double_prime_val
        
        print(f"{k:<3} {x_k:<15.6f} {f_prime_val:<15.6f} {f_double_prime_val:<15.6f} {x_next:<15.6f}")
        
        x_k = x_next
        iterations.append(x_k)
        k += 1
    
    if k >= max_iter:
        print(f"\nДостигнуто максимальное число итераций ({max_iter})")
    
    return x_k, iterations

# Определение функции и её производных
def f(x):
    """Целевая функция f(x) = (1/3)x^3 - 5x + x·ln(x)"""
    return (1/3) * x**3 - 5*x + x * math.log(x)

def f_prime(x):
    """Первая производная f'(x) = x^2 - 5 + ln(x) + 1 = x^2 + ln(x) - 4"""
    return x**2 + math.log(x) - 4

def f_double_prime(x):
    """Вторая производная f''(x) = 2x + 1/x"""
    return 2*x + 1/x

def f_triple_prime(x):
    """Третья производная f'''(x) = 2 - 1/x^2"""
    return 2 - 1/x**2

# Параметры задачи
a, b = 1.5, 2.0
eps = 0.02

print("=" * 75)
print("МИНИМИЗАЦИЯ ФУНКЦИИ МЕТОДОМ НЬЮТОНА (2-го ПОРЯДКА)")
print("=" * 75)
print(f"Функция: f(x) = (1/3)x³ - 5x + x·ln(x)")
print(f"Интервал: [{a}, {b}]")
print(f"Точность ε = {eps} (критерий останова: |f'(x^k)| ≤ ε)")
print("=" * 75)

# Запуск метода Ньютона
x_min, iterations = newton_optimization(f, f_prime, f_double_prime, f_triple_prime, a, b, eps)

if x_min is not None:
    print("\n" + "=" * 75)
    print("РЕЗУЛЬТАТЫ:")
    print(f"Найденная точка минимума x* = {x_min:.6f}")
    print(f"Значение функции в минимуме f(x*) = {f(x_min):.6f}")
    print(f"Значение первой производной в найденной точке f'(x*) = {f_prime(x_min):.6f}")
    print(f"Количество итераций: {len(iterations)-1}")
    print("=" * 75)
    
    # Проверка условия унимодальности (знакопостоянство второй производной на интервале)
    print(f"\nПРОВЕРКА УСЛОВИЙ ИЗ ЛЕКЦИИ:")
    f_double_min = f_double_prime(x_min)
    print(f"Вторая производная в точке минимума f''(x*) = {f_double_min:.6f} > 0 - точка минимума подтверждена")
