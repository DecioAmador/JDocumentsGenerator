package com.github.decioamador.jdocsgen.model.animal;

import java.util.Calendar;
import java.util.EnumSet;

public class Cow extends Animal {

    public Cow(final double weight, final Calendar birthdate) {
        super("Animalia", "Bos taurus", weight, EnumSet.of(Transport.TERRESTRIAL), birthdate);
    }

}
