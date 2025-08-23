package Pokemons;

import Attacks.Facade;
import ru.ifmo.se.pokemon.Type;

public class Electross extends Elektrik {
    public Electross(String name, int level) {
        super(name, level);
        this.setStats(280.0, 211.0, 148.0, 193.0, 148.0, 94.0);
        this.setType(Type.ELECTRIC);
        this.setMove(new Facade());
    }
}
