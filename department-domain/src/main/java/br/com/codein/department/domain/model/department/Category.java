package br.com.codein.department.domain.model.department;

import br.com.codein.buddycharacteristic.domain.characteristic.Characteristic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.gumga.domain.GumgaModel;
import io.gumga.domain.GumgaMultitenancy;
import io.gumga.domain.GumgaMultitenancyPolicy;
import io.gumga.domain.domains.GumgaImage;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Columns;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.InheritanceType.SINGLE_TABLE;

/**
 * Created by gelatti on 21/02/17.
 */
@Entity
@GumgaMultitenancy(policy = GumgaMultitenancyPolicy.ORGANIZATIONAL)
@Table(name="Category")
@Inheritance(strategy=SINGLE_TABLE)
@SequenceGenerator(name = GumgaModel.SEQ_NAME, sequenceName = "SEQ_CATEGORY")
@DiscriminatorColumn
@DiscriminatorValue(value = "category")
@Audited
public class Category extends GumgaModel<Long> implements Serializable {
    @Version
    @ApiModelProperty(hidden = true)
    private Integer version;
    @NotNull(message = "Name should be informed")
    @ApiModelProperty(value = "Nome da categoria", position = 1, required = true)
    private String name;
    @ApiModelProperty(value = "Descrição da categoria", position = 2)
    private String description;
    @JsonIgnore
    @ManyToOne
    @ApiModelProperty(value = "Especifica a qual departamento pertence esta categoria", position = 3)
    private Department department;
    @JsonIgnore
    @ManyToOne
    @ApiModelProperty(value = "Especifica qual a categoria pai desta categoria caso ela possua categorias filhas.", position = 4)
    private Category category;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "category")
    @ApiModelProperty(value = "Set de categorias filhas desta categoria", position = 5)
    private Set<Category> categories;
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @ApiModelProperty(value = "Set de tipo de produtos referentes a esta categoria", position = 6)
    private Set<ProductType> productTypes;
    @Columns(columns = {
            @Column(name = "image_name"),
            @Column(name = "image_size"),
            @Column(name = "image_type"),
            @Column(name = "image_bytes",length = 50*1024*1024)
    })
    @ApiModelProperty(value = "Salva dados da imagem da categoria", position = 7)
    private GumgaImage image;
    @ManyToMany
    @ApiModelProperty(value = "Especifica um set de caracteristicas referentes a esta categoria", position = 8)
    private Set<Characteristic> characteristics = new HashSet<>();

    @ElementCollection
    @Column(name="name_mount")
    @ApiModelProperty(hidden = true)
    private List<String> nameMount;
    @ApiModelProperty(value = "ID usado para integração com outros softwares", position = 9)
    private Long integrationId;
    @ApiModelProperty(value = "Determina se a categoria esta ativa ou não", position = 10)
    private Boolean active;

    public Category() {
    }

    public Category(String name, Boolean active) {
        this.name = name;
        this.active = active;
    }

    public Category(String name, Department department, Boolean active) {
        this.name = name;
        this.department = department;
        this.active = active;
    }

    public Category(String name, Category category, Boolean active) {
        this.name = name;
        this.category = category;
        this.active = active;
    }

    public Category(String name, Department department, Set<Characteristic> characteristics, Boolean active) {
        this.name = name;
        this.department = department;
        this.characteristics = characteristics;
        this.active = active;
    }

    public Category(String name, String description, Department department, Set<Category> categories, Set<Characteristic> characteristics, Boolean active) {
        this.name = name;
        this.description = description;
        this.department = department;
        this.categories = categories;
        this.characteristics = characteristics;
        this.active = active;
    }
    public Category(String name, String description, Set<Category> categories, Set<Characteristic> characteristics, Boolean active) {
        this.name = name;
        this.description = description;
        this.categories = categories;
        this.characteristics = characteristics;
        this.active = active;
    }

    public Category(String name, String description, Set<Characteristic> characteristics, Boolean active) {
        this.name = name;
        this.description = description;
        this.characteristics = characteristics;
        this.active = active;
    }

    public Category(String description, String name, Set<Category> categories, Set<ProductType> productTypes, Set<Characteristic> characteristics, Boolean active) {
        this.description = description;
        this.name = name;
        this.categories = categories;
        this.productTypes = productTypes;
        this.characteristics = characteristics;
        this.active = active;
    }

    public Category(Long id,String name, String description, Set<Category> categories, Set<ProductType> productTypes, Set<Characteristic> characteristics, Boolean active) {
        this.description = description;
        this.name = name;
        this.categories = categories;
        this.productTypes = productTypes;
        this.characteristics = characteristics;
        this.id = id;
        this.active = active;
    }

    public Category(Long id,String name, String description, Set<Category> categories, Set<ProductType> productTypes, Set<Characteristic> characteristics, Department department, Boolean active) {
        this.description = description;
        this.name = name;
        this.categories = categories;
        this.productTypes = productTypes;
        this.characteristics = characteristics;
        this.id = id;
        this.department =department;
        this.active = active;
    }

    public Category(Long id,String name, String description, Set<Category> categories, Set<ProductType> productTypes, Set<Characteristic> characteristics, Department department,List<String> nameMount, Boolean active) {
        this.description = description;
        this.name = name;
        this.categories = categories;
        this.productTypes = productTypes;
        this.characteristics = characteristics;
        this.id = id;
        this.department =department;
        this.nameMount = nameMount;
        this.active = active;
    }

    public Category(Long id, String name, String description, Set<Category> categories, Set<ProductType> productTypes, Set<Characteristic> characteristics, Category category, Boolean active) {
        this.description = description;
        this.name = name;
        this.categories = categories;
        this.productTypes = productTypes;
        this.characteristics = characteristics;
        this.id = id;
        this.category = category;
        this.active = active;
    }

    public Category(Long id, String name, String description, Set<Category> categories, Set<ProductType> productTypes, Set<Characteristic> characteristics, Category category,List<String> nameMount, Boolean active) {
        this.description = description;
        this.name = name;
        this.categories = categories;
        this.productTypes = productTypes;
        this.characteristics = characteristics;
        this.id = id;
        this.category = category;
        this.nameMount = nameMount;
        this.active = active;
    }

    public Category(String name, HashSet<ProductType> productTypes, boolean active) {
        this.name = name;
        this.productTypes = productTypes;
        this.active = active;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public Set<ProductType> getProductTypes() {
        return productTypes;
    }

    public void setProductTypes(Set<ProductType> productTypes) {
        this.productTypes = productTypes;
    }

    public GumgaImage getImage() {
        return image;
    }

    public void setImage(GumgaImage image) {
        this.image = image;
    }

    public Set<Characteristic> getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(Set<Characteristic> characteristics) {
        this.characteristics = characteristics;
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
