package Attacks;

import ru.ifmo.se.pokemon.Effect;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.SpecialMove;
import ru.ifmo.se.pokemon.Stat;
import ru.ifmo.se.pokemon.Type;

public class ChargeBeam extends SpecialMove {
    public ChargeBeam() {
        this.type = Type.ELECTRIC;
        this.power = 50.0;
        this.accuracy = 90.0;
        this.priority = 10;
    }

    protected void applyOppDamage(Pokemon p, double damage) {
        p.setMod(Stat.HP, (int)Math.round(damage));
    }

    protected void applyOppEffects(Pokemon p) {
        if (Math.random() >= 0.3) {
            p.addEffect((new Effect()).stat(Stat.SPECIAL_ATTACK, 1));
        }

    }

    protected String describe() {
        return "применил Charge Beam";
    }
}

