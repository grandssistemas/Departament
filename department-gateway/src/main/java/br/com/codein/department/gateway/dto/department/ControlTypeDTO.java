package br.com.codein.department.gateway.dto.department;

import br.com.codein.department.domain.model.department.enums.ControlType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by gelatti on 21/02/17.
 */
public class ControlTypeDTO {

    private String key;
    private String label;

    public ControlTypeDTO(ControlType controlType){
        this.key = controlType.getName();
        this.label = controlType.getLabel();
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

    public static List<ControlTypeDTO> getValues(){
        List<ControlTypeDTO> controlTypeDTOs = new ArrayList<>();
        Arrays.asList(ControlType.values()).forEach(item->{
            controlTypeDTOs.add(new ControlTypeDTO(item));
        });
        return controlTypeDTOs;
    }

}
