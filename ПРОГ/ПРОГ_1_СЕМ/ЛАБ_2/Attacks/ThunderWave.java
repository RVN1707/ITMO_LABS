package Attacks;

import ru.ifmo.se.pokemon.Effect;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.SpecialMove;
import ru.ifmo.se.pokemon.Stat;
import ru.ifmo.se.pokemon.Type;

public class ThunderWave extends SpecialMove {
    public ThunderWave() {
        this.type = Type.ELECTRIC;
        this.power = 0.0;
        this.accuracy = 90.0;
        this.priority = 20;
    }

    protected void applyOppEffects(Pokemon p) {
        Effect.paralyze(p);
        if (Math.random() <= 0.25) {
            p.setMod(Stat.ATTACK, 0);
        }

        p.setMod(Stat.SPEED, (int)(-1.0 * p.getStat(Stat.SPEED) / 2.0));
    }

    protected String describe() {
        return "применил Thunder Wave";
    }
}
