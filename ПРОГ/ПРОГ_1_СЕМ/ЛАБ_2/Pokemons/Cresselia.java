package Pokemons;

import Attacks.ChargeBeam;
import Attacks.Moonblast;
import Attacks.Psychic;
import Attacks.PsychoCut;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Cresselia extends Pokemon {
    public Cresselia(String name, int level) {
        super(name, level);
        this.setStats(350.0, 130.0, 202.0, 139.0, 220.0, 157.0);
        this.setType(Type.PSYCHIC);
        this.setMove(new ChargeBeam(), new PsychoCut(), new Psychic(), new Moonblast());
    }
}