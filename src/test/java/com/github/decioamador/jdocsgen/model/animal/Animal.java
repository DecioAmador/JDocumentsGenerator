package com.github.decioamador.jdocsgen.model.animal;

import java.util.Calendar;
import java.util.Set;

public abstract class Animal {

    private String kingdom;
    private String specie;
    private double weight;
    private Calendar birthdate;
    private Set<Transport> transport;

    public Animal(final String kingdom, final String specie, final double weight, final Set<Transport> transport,
            final Calendar birthdate) {
        this.kingdom = kingdom;
        this.specie = specie;
        this.weight = weight;
        this.transport = transport;
        this.birthdate = birthdate;
    }

    public String getKingdom() {
        return kingdom;
    }

    public void setKingdom(final String kingdom) {
        this.kingdom = kingdom;
    }

    public String getSpecie() {
        return specie;
    }

    public void setSpecie(final String specie) {
        this.specie = specie;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(final double weight) {
        this.weight = weight;
    }

    public Set<Transport> getTransport() {
        return transport;
    }

    public void setTransport(final Set<Transport> transport) {
        this.transport = transport;
    }

    public Calendar getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(final Calendar birthdate) {
        this.birthdate = birthdate;
    }

}
