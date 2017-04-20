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


    public static ControlType getByAbbreviation(String abbreviation){
        for(int i = 0; i < ControlType.values().length;i++){
            if(ControlType.values()[i].equalsAbbreviationNoCase(abbreviation)){
                return ControlType.values()[i];
            }
        }
        return null;
    }

    private boolean equalsAbbreviationNoCase(String abbreviation) {
        return this.abbreviation.toUpperCase().equals(abbreviation.toUpperCase());
    }
}
