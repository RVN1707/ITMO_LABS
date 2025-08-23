import Transpot.Carpet;
import Persons.*;
import Enums.Corner;
import Gutter.Gutter;
import exceptions.NotGutter;

public class Main {
    public static void main(String[] args)  {

        System.out.println("\nДА НАЧНЁТСЯ ИСТОРИЯ!\n");

        try {
            Gutter gutter = new Gutter();
            Corner corner = gutter.getCorner();
            Person Pestrenky = new Person("Пёстренький");
            Carpet PC = new Carpet(Pestrenky.getName());
            Person Neznaika = new Person("Незнайка");
            Carpet NC = new Carpet(Neznaika.getName());
            Pestrenky.NeedGoFirst( Neznaika, corner, PC);
            System.out.println("");
            Neznaika.NeedGoSecond(corner, NC);

        } catch (NotGutter e) {
            System.out.println(e);
            System.exit(0);
        }

        System.out.println("\nКОНЕЦ ИСТОРИИ!");
    }
}
