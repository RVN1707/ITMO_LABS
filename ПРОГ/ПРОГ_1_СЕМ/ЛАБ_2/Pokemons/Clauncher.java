package Pokemons;

import Attacks.BubbleBeam;
import Attacks.Facade;
import Attacks.FlashCannon;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Clauncher extends Pokemon {
    public Clauncher(String name, int level) {
        super(name, level);
        this.setStats(210.0, 99.0, 116.0, 108.0, 117.0, 83.0);
        this.setType(Type.WATER);
        this.setMove(new FlashCannon(), new BubbleBeam(), new Facade());
    }
}
