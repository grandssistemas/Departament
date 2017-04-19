package br.com.codein.department.domain.model.department.enums;

/**
 * Created by gelatti on 21/02/17.
 */
public enum TypeLabeling {

    COMMON("Comum"),
    BALANCE_UNIT("Balança Unitário"),
    BALANCE_WEIGHT("Balança Peso");

    private final String label;

    TypeLabeling(String label) {
        this.label = label;
    }

    public String getName(){
        return this.name();
    }

    public String getLabel() {
        return label;
    }
}
