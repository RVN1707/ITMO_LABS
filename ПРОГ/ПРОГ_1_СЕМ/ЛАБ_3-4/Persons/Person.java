package Persons;
import Records.Luck;
import Transpot.Carpet;
import Enums.Corner;

public class Person extends WaysToSliding implements PersonsMood{
    private String name;

    public Person(String name){
        this.name=name;
    }
    public String getName() {
        return name;
    }
    public void NeedGoFirst(Person name1, Corner c, Carpet d){
        Push(name, name1.name);
        NeedGo(c,d);
    }
    public void NeedGoSecond(Corner c, Carpet d){
        NeedGo(c,d);
    }
    private void NeedGo(Corner c, Carpet d){
        Start(name);
        Finish(name, Process(name, c, d));
    }

    private void Push(String name1, String name2){
        System.out.println(name1 +" толкаясь идет к желобу. " + name2 + " в шоке от такого поведения");
    }

    @Override
    public void Start(String name){
        System.out.println(name +" собирается скатиться по желобу");
    }

    @Override
    public String Process(String name, Corner c, Carpet d) {
        Luck luck = new Luck("лузер","победитель");
        if (d.isSlippedoff()) {
            BadSliding(name);
            d.Experiense();
            return luck.Luck1();
        } else if (c==Corner.SMOOTH) {
            GoodSliding(c, name);
            d.Experiense();
            return luck.Luck1();
        } else {
            ExtremeSliding(c, name, d);
            d.Experiense();
            return luck.Luck1();
        }
    }

    @Override
    public void Finish(String name, String a){
        System.out.println(name +" завершает своё скатывание");
        System.out.println(name+" "+a+" по жизни");
    }

    @Override
    public String toString() {
        return "Person{name='" + name + "'}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // Если ссылки на один и тот же объект
        if (obj == null || getClass() != obj.getClass()) return false; // Проверка на совместимость классов
        Person person = (Person) obj; // Приведение типа
        return name != null ? name.equals(person.name) : person.name == null; // Сравнение имён
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0; // Генерация хэш-кода на основе имени
    }
}
