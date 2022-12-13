package sped.core;

public abstract class Validation {
    private final String name;

    Validation(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract void validate();
}

