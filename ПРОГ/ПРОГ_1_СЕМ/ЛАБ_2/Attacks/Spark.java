package Attacks;

import ru.ifmo.se.pokemon.Effect;
import ru.ifmo.se.pokemon.PhysicalMove;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Stat;
import ru.ifmo.se.pokemon.Type;

public class Spark extends PhysicalMove {
    public Spark() {
        this.type = Type.ELECTRIC;
        this.power = 65.0;
        this.accuracy = 100.0;
        this.priority = 20;
    }

    protected void applyOppDamage(Pokemon p, double damage) {
        p.setMod(Stat.HP, (int)Math.round(damage));
    }

    protected void applyOppEffects(Pokemon p) {
        if (Math.random() <= 0.3) {
            Effect.paralyze(p);
        }

    }

    protected String describe() {
        return "применил Attacks.Spark";
    }
}