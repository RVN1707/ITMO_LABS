public class My_1_lab { //Все кратко и ясно, самое оптимальное решение без лишней фигни
    //А если не прописывать циклы for три раза в switch-case, то мы каждую итерацию вложенного цикла будем смотреть условие, чему равен s[i]

    public static void main(String[] args) {

        double[] x = new double[10]; //Объявляем массив x и приступаем к его заполнению
        for (int i = 0; i < x.length; i++) {
            x[i]= -2.0 + 6.0 * Math.random();
        }

        short[] s = new short[16]; //Объявляем массив s и приступаем к его заполнению параллельно с вычислением значений для нашего двумерного массива в цикле
        short variableForS = 4;

        for (int i = 0; i < s.length; i++) { //Проход по строкам

            s[i] = variableForS;
            variableForS++;

            switch (s[i]) {

                case 5:

                    for (int j = 0; j < x.length; j++) { //Проход по столбцам
                        System.out.print(Math.round(Math.pow(Math.pow(Math.cos(x[j]), Math.exp(x[j]) / 2) + 1, 3) * 100) / 100.0 + "  ");
                    }
                    break;

                case 8, 9, 10, 11, 12, 14, 15, 19:

                    for (int j = 0; j < x.length; j++) { //Проход по столбцам
                        System.out.print(Math.round(Math.pow(Math.cos(Math.cbrt(x[j])), (Math.pow((Math.pow(x[j] * 2, 2) / 2), 3)) / 2) * 100) / 100.0 + "  ");
                    }
                    break;

                default:

                    for (int j = 0; j < x.length; j++) { //Проход по столбцам
                        System.out.print(Math.round(Math.pow(0.75, 3) * Math.pow(Math.sin(x[j]) * (Math.pow(x[j], x[j] / 2) + 1), 2) * 100) / 100.0 + "  ");
                    }
            }

            System.out.println(); //Переход на следующую строку (для форматированного вывода)
        }
    }
}