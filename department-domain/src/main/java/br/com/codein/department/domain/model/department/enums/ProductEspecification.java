package br.com.codein.department.domain.model.department.enums;

public enum ProductEspecification {
    GAS, WEAPON, CAR, MEDICINE,COMMON;

    public static ProductEspecification findByName(String name){
        for (ProductEspecification s : ProductEspecification.values()) {
            if (name.equalsIgnoreCase(s.name()))
                return s;
        }
        return null;
    }
}