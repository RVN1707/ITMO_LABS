import math

def f(x):
    """Исходная функция f(x) = 1/3*x^3 - 5x + x*ln(x)"""
    return (1/3) * x**3 - 5*x + x * math.log(x)

def f_prime(x):
    """Производная функции f'(x) = x^2 - 5 + ln(x) + 1 = x^2 - 4 + ln(x)"""
    return x**2 - 4 + math.log(x)

def midpoint_method(a, b, epsilon, max_iterations=100):

    k = 0
    
    print(f"\n{'Итерация':^8} | {'a':^10} | {'b':^10} | {'x':^10} | {"f'(x)":^12} | {'|f\'(x)| ≤ ε?':^15}")
    print("-" * 80)
    
    while k < max_iterations:
        # Вычисляем середину отрезка
        x = (a + b) / 2
        
        # Вычисляем производную в точке x
        f_deriv = f_prime(x)
        
        # Проверка критерия останова
        is_epsilon_reached = abs(f_deriv) <= epsilon
        
        # Выводим информацию об итерации
        print(f"{k:^8} | {a:^10.6f} | {b:^10.6f} | {x:^10.6f} | {f_deriv:^12.6f} | {str(is_epsilon_reached):^15}")
        
        # Критерий останова
        if is_epsilon_reached:
            print("-" * 80)
            print(f"Критерий останова выполнен: |f'(x)| = {abs(f_deriv):.6f} ≤ {epsilon}")
            break
        
        # Обновляем границы в зависимости от знака производной
        if f_deriv > 0:
            b = x  # Минимум слева
        else:  # f_deriv < 0
            a = x  # Минимум справа
        
        k += 1
    
    # Финальная точка
    x_min = (a + b) / 2
    f_min = f(x_min)
    
    return x_min, f_min, k

# Решение задачи
a, b = 1.5, 2.0
epsilon = 0.02

print("=" * 80)
print("МЕТОД СРЕДНЕЙ ТОЧКИ (МЕТОД 1-ГО ПОРЯДКА)")
print("=" * 80)
print(f"Функция: f(x) = 1/3*x^3 - 5x + x*ln(x)")
print(f"Начальный интервал: [{a}, {b}]")
print(f"Точность ε = {epsilon}")
print("=" * 80)

x_min, f_min, iterations = midpoint_method(a, b, epsilon)

print(f"\nРЕЗУЛЬТАТ:")
print(f"Точка минимума x* = {x_min:.6f}")
print(f"Значение функции f(x*) = {f_min:.6f}")
print(f"Число итераций: {iterations}")

# Проверка выполнения условия
f_prime_at_min = f_prime(x_min)
print(f"\nПРОВЕРКА:")
print(f"Производная в точке минимума f'(x*) = {f_prime_at_min:.6f}")
print(f"|f'(x*)| = {abs(f_prime_at_min):.6f} ≤ {epsilon}: {abs(f_prime_at_min) <= epsilon}")
