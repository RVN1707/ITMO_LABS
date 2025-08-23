package Attacks;

import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.SpecialMove;
import ru.ifmo.se.pokemon.Stat;
import ru.ifmo.se.pokemon.Type;

public class Moonblast extends SpecialMove {
    public Moonblast() {
        this.type = Type.FAIRY;
        this.power = 95.0;
        this.accuracy = 100.0;
        this.priority = 15;
    }

    protected void applyOppDamage(Pokemon p, double damage) {
        p.setMod(Stat.HP, (int)Math.round(damage));
    }

    protected void applyOppEffects(Pokemon p) {
        if (Math.random() <= 0.3) {
            p.setMod(Stat.SPECIAL_ATTACK, -1);
        }

    }

    protected String describe() {
        return "применил Attacks.Moonblast";
    }
}
