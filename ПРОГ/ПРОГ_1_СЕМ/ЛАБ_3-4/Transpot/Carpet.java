package Transpot;
public class Carpet implements CarpetsMethods{
    private boolean HaveUser;
    private String username;

    public Carpet(String username){
        this.HaveUser=true;
        this.username=username;
    }
    @Override
    public void Experiense(){
        if (getHaveUser()){
            System.out.println(username+" смог остаться на ковре.");
        }else System.out.println(username+" не смог остаться на ковре.");
    }

    @Override
    public boolean getHaveUser(){
        return this.HaveUser;
    }

    @Override
    public boolean isSlippedoff(){

        if (Math.random() > 0.5) {
            System.out.println("Коврик соскальзывает вниз, а "+ username+" за ним");
             HaveUser=false;
            return true;
        } else {
            System.out.println(username+" сумел сесть на коврик");
            return false;
        }
    }
    @Override
    public void ExtremeSlippedoff(){
        System.out.println("Но коврик неожиданно соскальзывает вниз от "+username);
        HaveUser=false;
    }

    @Override
    public String toString() {
        return "Carpet{" + "HaveUser=" + HaveUser + ", username='" + username + '\'' + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // Проверка на идентичность объектов
        if (obj == null || getClass() != obj.getClass()) return false; // Проверка на null и совпадение классов
        Carpet carpet = (Carpet) obj; // Приведение объекта к типу Carpet
        return HaveUser == carpet.HaveUser && // Сравнение полей
                (username != null ? username.equals(carpet.username) : carpet.username == null);
    }

    @Override
    public int hashCode() {
        int result = (HaveUser ? 1 : 0); // Преобразуем boolean в int
        result = 31 * result + (username != null ? username.hashCode() : 0); // Вычисление hashCode с учетом username
        return result;
    }
}