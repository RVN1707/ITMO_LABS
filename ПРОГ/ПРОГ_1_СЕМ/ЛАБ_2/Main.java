import Pokemons.*;
import ru.ifmo.se.pokemon.Battle;
import ru.ifmo.se.pokemon.Pokemon;

public class Main {
    public static void main(String[] args) {
        Battle b = new Battle();
        Pokemon p1 = new Cresselia("", 1);
        Pokemon p2 = new Clauncher("", 1);
        Pokemon p3 = new Clawitzer("", 1);
        Pokemon p4 = new Tynamo("", 1);
        Pokemon p5 = new Elektrik("", 1);
        Pokemon p6 = new Electross("", 1);
        b.addAlly(p1);
        b.addAlly(p2);
        b.addAlly(p3);
        b.addFoe(p4);
        b.addFoe(p5);
        b.addFoe(p6);
        b.go();
    }
}