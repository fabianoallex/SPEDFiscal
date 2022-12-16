package sped.core;

public class Context {
    private final Definitions definitions;

    public Context(Definitions definitions) {
        this.definitions = definitions;
    }

    public Definitions getDefinitions() {
        return definitions;
    }
}
