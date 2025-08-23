package Pokemons;

import Attacks.Spark;
import ru.ifmo.se.pokemon.Type;

public class Elektrik extends Tynamo {
    public Elektrik(String name, int level) {
        super(name, level);
        this.setStats(240.0, 157.0, 130.0, 139.0, 130.0, 76.0);
        this.setType(Type.ELECTRIC);
        this.setMove(new Spark());
    }
}

