package br.com.codein.department.gateway.dto.department;

import br.com.codein.department.domain.model.department.enums.ProductEspecification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by luizaugusto on 02/05/17.
 */
public class ProductEspecificationDTO {

    private String key;
    private String label;

    public ProductEspecificationDTO(ProductEspecification productEspecification){
        this.key = productEspecification.getName();
        this.label = productEspecification.getLabel();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public static List<ProductEspecificationDTO> getValues(){
        List<ProductEspecificationDTO> productEspecificationDTOS = new ArrayList<>();
        Arrays.asList(ProductEspecification.values()).forEach(item->{
            productEspecificationDTOS.add(new ProductEspecificationDTO(item));
        });
        return productEspecificationDTOS;
    }
}
