package Attacks;

import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.SpecialMove;
import ru.ifmo.se.pokemon.Stat;
import ru.ifmo.se.pokemon.Type;

public class DragonPulse extends SpecialMove {
    public DragonPulse() {
        this.type = Type.DRAGON;
        this.power = 85.0;
        this.accuracy = 100.0;
        this.priority = 10;
    }

    protected void applyOppDamage(Pokemon p, double damage) {
        p.setMod(Stat.HP, (int)Math.round(damage));
    }

    protected String describe() {
        return "применил Dragon Pulse";
    }
}