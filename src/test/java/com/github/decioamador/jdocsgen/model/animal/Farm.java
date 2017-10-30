package com.github.decioamador.jdocsgen.model.animal;

import java.time.LocalDateTime;
import java.util.List;

public class Farm {

    private Pet[] pets;
    private List<Animal> animals;
    private LocalDateTime founded;

    public Pet[] getPets() {
        return pets;
    }

    public void setPets(final Pet[] pets) {
        this.pets = pets;
    }

    public List<Animal> getAnimals() {
        return animals;
    }

    public void setAnimals(final List<Animal> animals) {
        this.animals = animals;
    }

    public LocalDateTime getFounded() {
        return founded;
    }

    public void setFounded(final LocalDateTime founded) {
        this.founded = founded;
    }

}
