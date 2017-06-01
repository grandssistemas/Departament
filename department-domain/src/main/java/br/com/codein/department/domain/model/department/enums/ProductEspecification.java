package br.com.codein.department.domain.model.department.enums;

public enum ProductEspecification {
    GAS("Combustível"),
    COMMON("Comum");
//    WEAPON("Armamento"),
//    CAR("Veículo"),
//    MEDICINE("Medicamento");

    private final String label;

    ProductEspecification(String label){
        this.label = label;
    }

    public String getName(){
        return this.name();
    }

    public String getLabel() {
        return label;
    }

    public static ProductEspecification findByName(String name){
        for (ProductEspecification s : ProductEspecification.values()) {
            if (name.equalsIgnoreCase(s.name()))
                return s;
        }
        return null;
    }
}