package com.github.decioamador.jdocsgen.model.animal;

import java.util.Calendar;
import java.util.EnumSet;

public class Violetear extends Animal {

    public Violetear(final double weight, final Calendar birthdate) {
        super("Animalia", "Colibri delphinae", weight, EnumSet.of(Transport.AIR), birthdate);
    }

}
