package sped.core;

public class FieldNotFoundException extends Exception {
    public FieldNotFoundException(String fieldClassName, String fieldName, String registerName) {
        super(String.format("%s '%s' não encontrado no Registro '%s'.", fieldClassName, fieldName, registerName));
    }
}
