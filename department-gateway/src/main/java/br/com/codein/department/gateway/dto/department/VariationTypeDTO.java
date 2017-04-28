package br.com.codein.department.gateway.dto.department;

import br.com.codein.department.domain.model.department.enums.VariationType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by luizaugusto on 28/04/17.
 */
public class VariationTypeDTO {

    private String key;
    private String label;

    public VariationTypeDTO(VariationType variationType){
        this.key = variationType.getName();
        this.label = variationType.getLabel();
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

    public static List<VariationTypeDTO> getValues(){
        List<VariationTypeDTO> variationTypeDTOs = new ArrayList<>();
        Arrays.asList(VariationType.values()).forEach(item->{
            variationTypeDTOs.add(new VariationTypeDTO(item));
        });
        return variationTypeDTOs;
    }
}
