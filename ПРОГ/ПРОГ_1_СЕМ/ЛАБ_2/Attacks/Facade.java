package Attacks;

import ru.ifmo.se.pokemon.PhysicalMove;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Stat;
import ru.ifmo.se.pokemon.Status;
import ru.ifmo.se.pokemon.Type;

public class Facade extends PhysicalMove {
    public Facade() {
        this.type = Type.NORMAL;
        this.power = 70.0;
        this.accuracy = 100.0;
        this.priority = 20;
    }

    protected void applyOppDamage(Pokemon p, double damage) {
        Status status = p.getCondition();
        if (status.equals(Status.BURN) | status.equals(Status.PARALYZE) | status.equals(Status.POISON)) {
            p.setMod(Stat.HP, (int)Math.round(damage) * 2);
        }

        p.setMod(Stat.HP, (int)Math.round(damage));
    }

    protected String describe() {
        return "применил Attacks.Facade";
    }
}
