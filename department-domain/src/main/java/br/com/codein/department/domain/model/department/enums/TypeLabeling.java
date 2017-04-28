package br.com.codein.department.domain.model.department.enums;

/**
 * Created by gelatti on 21/02/17.
 */
public enum TypeLabeling {

    COMMON("Comum"),
    BALANCE("Balança");

    private final String label;

    TypeLabeling(String label) {
        this.label = label;
    }

    public String getName() {
        return this.name();
    }

    public String getLabel() {
        return label;
    }

    public static TypeLabeling findByName(String name) {
        switch (name) {
            case "COMMON":
                return COMMON;
            case "BALANCE_UNIT":
                return BALANCE_UNIT;
            case "BALANCE_WEIGHT":
                return BALANCE_WEIGHT;
            default:
                return null;
        }
    }
}
