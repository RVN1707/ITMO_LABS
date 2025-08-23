package Attacks;

import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.SpecialMove;
import ru.ifmo.se.pokemon.Stat;
import ru.ifmo.se.pokemon.Type;

public class BubbleBeam extends SpecialMove {
    public BubbleBeam() {
        this.type = Type.WATER;
        this.power = 65.0;
        this.accuracy = 100.0;
        this.priority = 20;
    }

    protected void applyOppDamage(Pokemon p, double damage) {
        p.setMod(Stat.HP, (int)Math.round(damage));
    }

    protected void applyOppEffects(Pokemon p) {
        if (Math.random() <= 0.1) {
            p.setMod(Stat.SPEED, -1);
        }

    }

    protected String describe() {
        return "применил Bubble Beam";
    }
}
