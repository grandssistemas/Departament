package br.com.codein.department.gateway.dto.department;

import br.com.codein.buddycharacteristic.domain.characteristic.AssociativeCharacteristic;
import br.com.codein.buddycharacteristic.domain.characteristic.Characteristic;
import br.com.codein.department.domain.model.department.enums.ProductEspecification;
import br.com.codein.department.domain.model.department.enums.TypeLabeling;
import br.com.codein.department.domain.model.department.enums.VariationType;
import br.com.codein.mobiagecore.domain.model.storage.StorageFile;
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
    public StorageFile file;
    public ProductEspecification especification;
    public Long integrationId;
    public String skuId;

    public CategoryDTO() {
    }

    public CategoryDTO(String name, List<AssociativeCharacteristic> characteristicsPT, Boolean isGrid,
                       List<CategoryDTO> childrens, Set<Characteristic> characteristics, String description,
                       CategoryType categoryType, List<String> nameMount, Boolean active, StorageFile file,
                       ProductEspecification especification, Long integrationId) {
        this.name = name;
        this.characteristicsPT = characteristicsPT;
        this.isGrid = isGrid;
        this.childrens = childrens;
        this.characteristics = characteristics;
        this.description = description;
        this.categoryType = categoryType;
        this.nameMount = nameMount;
        this.active = active;
        this.file = file;
        this.especification = especification;
        this.integrationId = integrationId;
    }
}