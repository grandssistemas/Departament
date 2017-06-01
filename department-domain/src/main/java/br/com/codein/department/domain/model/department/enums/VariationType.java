package br.com.codein.department.domain.model.department.enums;

/**
 * Created by luizaugusto on 19/04/17.
 */
public enum VariationType {
    CLOTHING("Moda"),
    DRINKS("Bebidas"),
    OTHER("Outros");

    private final String label;

    VariationType(String label){
        this.label = label;
    }

    public String getName(){
        return this.name();
    }

    public String getLabel() {
        return label;
    }
}