package Attacks;

import ru.ifmo.se.pokemon.Messages;
import ru.ifmo.se.pokemon.PhysicalMove;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Stat;
import ru.ifmo.se.pokemon.Type;

public class PsychoCut extends PhysicalMove {
    public PsychoCut() {
        this.type = Type.PSYCHIC;
        this.power = 70.0;
        this.accuracy = 100.0;
        this.priority = 20;
    }

    protected double calcCriticalHit(Pokemon att, Pokemon def) {
        if (att.getStat(Stat.SPEED) / 170.0 > Math.random()) {
            System.out.println(Messages.getBundle("critical"));
            return 2.0;
        } else {
            return 1.0;
        }
    }

    protected void applyOppDamage(Pokemon p, double damage) {
        p.setMod(Stat.HP, (int)Math.round(damage));
    }

    protected String describe() {
        return "применил Psycho Cut";
    }
}
