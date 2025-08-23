package Pokemons;

import Attacks.DragonPulse;
import ru.ifmo.se.pokemon.Type;

public class Clawitzer extends Clauncher {
    public Clawitzer(String name, int level) {
        super(name, level);
        this.setStats(252.0, 135.0, 162.0, 220.0, 164.0, 110.0);
        this.setType(Type.WATER);
        this.setMove( new DragonPulse());
    }
}

