package br.com.codein.department.domain.model.department.enums;

/**
 * Created by gelatti on 21/02/17.
 */
public enum ControlType {
    AMOUNT("Quantidade (Un)", "Un"),
    WEIGHT("Peso (Kg)", "Kg"),
    GASTYPE("Combustível", "Comb");

    private final String label;
    private final String abbreviation;

    ControlType(String label, String abbreviation) {
        this.label = label;
        this.abbreviation = abbreviation;
    }

    public String getName(){
        return this.name();
    }

    public String getLabel() {
        return label;
    }

    public String getAbbreviation () {
        return this.abbreviation;
    }
}
