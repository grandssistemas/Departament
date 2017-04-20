package br.com.codein.department.gateway.dto.department;

import br.com.codein.buddycharacteristic.domain.characteristic.AssociativeCharacteristic;
import br.com.codein.buddycharacteristic.domain.characteristic.Characteristic;
import br.com.codein.department.domain.model.department.enums.TypeLabeling;
import br.com.codein.department.domain.model.department.enums.VariationType;
import io.gumga.domain.domains.GumgaImage;

import java.util.List;
import java.util.Set;

/**
 * Created by gumga on 21/02/17.
 */
public class CategoryDTO {

    public Long id;
    public String name;
    public List<AssociativeCharacteristic> characteristicsPT;
    public Boolean isGrid;
    public Boolean editable;
    public List<CategoryDTO> childrens;
    public Set<Characteristic> characteristics;
    public String description;
    public CategoryType categoryType;
    public List<String> nameMount;
    public VariationType variation;
    public Integer version;
    public TypeLabeling typeLabeling;
    public Boolean active;
    public GumgaImage image;

    public CategoryDTO() {
    }

    public CategoryDTO(String name, List<AssociativeCharacteristic> characteristicsPT, Boolean isGrid,
                       List<CategoryDTO> childrens, Set<Characteristic> characteristics, String description,
                       CategoryType categoryType, List<String> nameMount, Boolean active, GumgaImage image) {
        this.name = name;
        this.characteristicsPT = characteristicsPT;
        this.isGrid = isGrid;
        this.childrens = childrens;
        this.characteristics = characteristics;
        this.description = description;
        this.categoryType = categoryType;
        this.nameMount = nameMount;
        this.active = active;
        this.image = image;
    }
}
