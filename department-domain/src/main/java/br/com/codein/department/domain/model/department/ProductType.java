package br.com.codein.department.domain.model.department;

import br.com.codein.buddycharacteristic.domain.characteristic.AssociativeCharacteristic;
import br.com.codein.buddycharacteristic.domain.characteristic.Characteristic;
import br.com.codein.department.domain.model.department.enums.ControlType;
import br.com.codein.department.domain.model.department.enums.TypeLabeling;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private List<AssociativeCharacteristic> characteristics;
    @NotNull(message = "A ProductType should have a father")
    @JsonIgnoreProperties(value = {"productTypes"})
    @ManyToOne
    @ApiModelProperty(value = "Categoria a qual pertence este tipo de produto", position = 3, required = true)
    private Category category;
    @NotNull(message = "Grid variable should be defined")
    @ApiModelProperty(value = "Especifica se este tipo de produto é de produto em grade", position = 4, required = true)
    private Boolean isGrid = false;
    @ElementCollection
    @Column(name="name_mount")
    @ApiModelProperty(hidden = true)
    private List<String> nameMount;
    @ApiModelProperty(value = "Nome do tipo de variação que o tipo de produto segue.", position = 5)
    private String variation;
    @ApiModelProperty(value = "Guarda 1 ou 2 valores separados por ';' que podem ser: MULTISELECAO, COR, TAMANHO, LOGICO", position = 6)
    private String gridPattern;
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(value = "Especifíca o tipo de controle de estoque", position = 7)
    private ControlType controlTypeProduct;
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(value = "Tipo de etiqueta que pode ser: balance ou common", position = 8)
    private TypeLabeling typeLabeling;
    @ApiModelProperty(value = "ID usado para integração com outros softwares", position = 9)
    private Long integrationId;
    @ApiModelProperty(value = "Determina se o tipo de produto esta ativo ou não", position = 10)
    private Boolean active;
    @Columns(columns = {
            @Column(name = "image_name"),
            @Column(name = "image_size"),
            @Column(name = "image_type"),
            @Column(name = "image_bytes",length = 50*1024*1024)
    })
    @ApiModelProperty(value = "Salva dados da imagem da categoria", position = 11)
    private GumgaImage image;

    public ProductType() {
    }

    public ProductType(String name, Boolean active, GumgaImage image) {
        this.name = name;
        this.active = active;
        this.image = image;
    }

    public ProductType(String name, List<AssociativeCharacteristic> characteristics, Boolean isGrid, Boolean active, GumgaImage image) {
        this.name = name;
        this.characteristics = characteristics;
        this.isGrid = isGrid;
        this.active = active;
        this.image = image;
    }

    public ProductType(Long id,String name, List<AssociativeCharacteristic> characteristics, Boolean isGrid,
                       Category category, Boolean active, GumgaImage image) {
        this.name = name;
        this.characteristics = characteristics;
        this.isGrid = isGrid;
        this.id = id;
        this.category = category;
        this.active = active;
        this.image = image;
    }

    public ProductType(Long id, String name, List<AssociativeCharacteristic> characteristics,
                       Boolean isGrid, Category category, List<String> nameMount, String gridPattern,
                       String variation, ControlType controlTypeProduct, TypeLabeling typeLabeling, Boolean active, GumgaImage image) {
        this.name = name;
        this.characteristics = characteristics;
        this.isGrid = isGrid;
        this.id = id;
        this.category = category;
        this.nameMount = nameMount;
        this.gridPattern = gridPattern;
        this.variation = variation;
        this.controlTypeProduct = controlTypeProduct;
        this.typeLabeling = typeLabeling;
        this.active = active;
        this.image = image;
    }

    public ProductType(String name, Boolean isGrid, Boolean active, GumgaImage image) {
        this.name = name;
        this.isGrid = isGrid;
        this.active = active;
        this.image = image;
    }

    public ProductType(String name, List<AssociativeCharacteristic> characteristics, Boolean active, GumgaImage image) {
        this.name = name;
        this.characteristics = characteristics;
        this.active = active;
        this.image = image;
    }

    public ProductType(String name, List<AssociativeCharacteristic> characteristics, Category category,
                       Boolean isGrid, Boolean active, GumgaImage image) {
        this.name = name;
        this.characteristics = characteristics;
        this.category = category;
        this.isGrid = isGrid;
        this.active = active;
        this.image = image;
    }

    public ProductType(String name, boolean isGrid, List<String> nameMount, ControlType controlTypeProduct, TypeLabeling typeLabeling, boolean active, GumgaImage image) {
        this.name = name;
        this.isGrid = isGrid;
        this.nameMount = nameMount;
        this.controlTypeProduct = controlTypeProduct;
        this.typeLabeling = typeLabeling;
        this.active = active;
        this.setCharacteristics(new ArrayList<>());
        this.image = image;
    }


    public ProductType(String name, boolean isGrid, List<String> nameMount, ControlType controlTypeProduct, TypeLabeling typeLabeling, boolean active, GumgaImage image,Characteristic row, Characteristic col) {
        this.name = name;
        this.isGrid = isGrid;
        this.nameMount = nameMount;
        this.controlTypeProduct = controlTypeProduct;
        this.typeLabeling = typeLabeling;
        this.active = active;
        this.image = image;
        List<AssociativeCharacteristic> list = new ArrayList<>();
        list.add(new AssociativeCharacteristic(false,row,true,1));
        list.add(new AssociativeCharacteristic(false,col,true,2));
        this.setCharacteristics(list);
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

    public String getGridPattern() {
        return gridPattern;
    }

    public void setGridPattern(String gridPattern) {
        this.gridPattern = gridPattern;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getVariation() {
        return variation;
    }

    public void setVariation(String variation) {
        this.variation = variation;
    }

    public ControlType getControlTypeProduct() {
        return controlTypeProduct;
    }

    public void setControlTypeProduct(ControlType controlTypeProduct) {
        this.controlTypeProduct = controlTypeProduct;
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
        for(AssociativeCharacteristic cpt:characteristics){
            if(cpt.getGridCount().equals(value)){
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
}
