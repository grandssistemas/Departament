package br.com.codein.department.domain.model.department;

import br.com.codein.buddycharacteristic.domain.characteristic.AssociativeCharacteristic;
import br.com.codein.buddycharacteristic.domain.characteristic.Characteristic;
import br.com.codein.department.domain.model.department.enums.ProductEspecification;
import br.com.codein.department.domain.model.department.enums.TypeLabeling;
import br.com.codein.department.domain.model.department.enums.VariationType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.gumga.domain.GumgaModel;
import io.gumga.domain.GumgaMultitenancy;
import io.gumga.domain.GumgaMultitenancyPolicy;
import io.gumga.domain.domains.GumgaImage;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Columns;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gelatti on 21/02/17.
 */
@GumgaMultitenancy(policy = GumgaMultitenancyPolicy.ORGANIZATIONAL)
@Entity
@Audited
@SequenceGenerator(name = GumgaModel.SEQ_NAME, sequenceName = "SEQ_PRODUCTTYPE")
public class ProductType extends GumgaModel<Long> implements Serializable {
    @Version
    @ApiModelProperty(hidden = true)
    private Integer version;
    @NotBlank(message = "Should have a name")
    @ApiModelProperty(value = "Nome do tipo de produto", position = 1, required = true)
    private String name;
    @OneToMany(orphanRemoval = true)
    @ApiModelProperty(value = "Lista de caracteristicas do tipo de produto.", position = 2)
    private List<AssociativeCharacteristic> characteristics = new ArrayList<>();
    @NotNull(message = "A ProductType should have a father")
    @JsonIgnoreProperties(value = {"productTypes"})
    @ManyToOne
    @ApiModelProperty(value = "Categoria a qual pertence este tipo de produto", position = 3, required = true)
    private Category category;
    @NotNull(message = "Grid variable should be defined")
    @ApiModelProperty(value = "Especifica se este tipo de produto é de produto em grade", position = 4, required = true)
    private Boolean isGrid = false;
    @ElementCollection
    @Column(name = "name_mount")
    @ApiModelProperty(hidden = true)
    private List<String> nameMount;
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(value = "Tipo de variação que o tipo de produto segue.", position = 5)
    private VariationType variation;
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(value = "Tipo de etiqueta que pode ser: balance ou common", position = 8)
    private TypeLabeling typeLabeling;
    @ApiModelProperty(value = "ID usado para integração com outros softwares", position = 9)
    private Long integrationId;
    @ApiModelProperty(value = "Determina se o tipo de produto esta ativo ou não", position = 10)
    private Boolean active = Boolean.TRUE;
    @Columns(columns = {
            @Column(name = "image_name"),
            @Column(name = "image_size"),
            @Column(name = "image_type"),
            @Column(name = "image_bytes", length = 50 * 1024 * 1024)
    })
    @ApiModelProperty(value = "Salva dados da imagem da categoria", position = 11)
    private GumgaImage image;

    private ProductEspecification especification;

    public ProductType() {
    }

    public ProductType(String name) {
        this.name = name;
    }

    public ProductType(String name, Boolean isGrid) {
        this.name = name;
        this.isGrid = isGrid;
    }

    public ProductType(String name, List<AssociativeCharacteristic> characteristics, Category category, Boolean isGrid, List<String> nameMount, VariationType variation, TypeLabeling typeLabeling, Long integrationId, Boolean active, GumgaImage image, ProductEspecification especification) {
        this.name = name;
        this.characteristics = characteristics;
        this.category = category;
        this.isGrid = isGrid;
        this.nameMount = nameMount;
        this.variation = variation;
        this.typeLabeling = typeLabeling;
        this.integrationId = integrationId;
        this.active = active;
        this.image = image;
        this.especification = especification;
    }

    public ProductType(Long id, String name,
                       List<AssociativeCharacteristic> characteristicsPT,
                       Boolean isGrid, Category father, List<String> nameMount, VariationType variation,
                       TypeLabeling typeLabeling, Boolean active, GumgaImage image) {
        this.id = id;
        this.name = name;
        this.isGrid = isGrid;
        this.nameMount = nameMount;
        this.variation = variation;
        this.typeLabeling = typeLabeling;
        this.active = active;
        this.image = image;
        this.category = father;
        this.characteristics = characteristicsPT;
    }

    public ProductType(String name, List<String> nameMount, TypeLabeling typeLabeling, Boolean isGrid, Characteristic row, Characteristic col){
        this.name = name;
        this.isGrid = isGrid;
        this.nameMount = nameMount;
        this.typeLabeling = typeLabeling;
        List<AssociativeCharacteristic> list = new ArrayList<>();
        list.add(new AssociativeCharacteristic(row,1));
        list.add(new AssociativeCharacteristic(col,2));
        this.setCharacteristics(list);
    }
    public ProductType(String name, List<String> nameMount, TypeLabeling typeLabeling, Boolean isGrid){
        this.name = name;
        this.isGrid = isGrid;
        this.nameMount = nameMount;
        this.typeLabeling = typeLabeling;
    }

    public Long getIntegrationId() {
        return integrationId;
    }

    public void setIntegrationId(Long integrationId) {
        this.integrationId = integrationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AssociativeCharacteristic> getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(List<AssociativeCharacteristic> characteristics) {
        this.characteristics = characteristics;
    }

    public Boolean getIsGrid() {
        return isGrid;
    }

    public void setIsGrid(Boolean isGrid) {
        this.isGrid = isGrid;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<String> getNameMount() {
        return nameMount;
    }

    public void setNameMount(List<String> nameMount) {
        this.nameMount = nameMount;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public TypeLabeling getTypeLabeling() {
        return typeLabeling;
    }

    public void setTypeLabeling(TypeLabeling typeLabeling) {
        this.typeLabeling = typeLabeling;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    private Characteristic getGrid(Integer value) {
        Characteristic characteristic = null;
        for (AssociativeCharacteristic cpt : characteristics) {
            if (cpt.getGridCount().equals(value)) {
                characteristic = cpt.getCharacteristic();
            }
        }
        return characteristic;
    }

    public Characteristic getGrid1() {
        return getGrid(1);
    }

    public Characteristic getGrid2() {
        return getGrid(2);
    }

    public GumgaImage getImage() {
        return image;
    }

    public void setImage(GumgaImage image) {
        this.image = image;
    }

    public ProductEspecification getEspecification() {
        return especification;
    }

    public void setEspecification(ProductEspecification especification) {
        this.especification = especification;
    }

    public VariationType getVariation() {
        return variation;
    }

    public void setVariation(VariationType variation) {
        this.variation = variation;
    }
}
