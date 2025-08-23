package Pokemons;

import Attacks.ChargeBeam;
import Attacks.ThunderWave;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Tynamo extends Pokemon {
    public Tynamo(String name, int level) {
        super(name, level);
        this.setStats(180.0, 103.0, 76.0, 85.0, 76.0, 112.0);
        this.setType(Type.ELECTRIC);
        this.setMove(new ThunderWave(), new ChargeBeam());
    }
}

