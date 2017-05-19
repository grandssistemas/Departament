package br.com.codein.department.domain.model.department;

import br.com.codein.buddycharacteristic.domain.characteristic.Characteristic;
import br.com.codein.department.domain.model.department.enums.VariationType;
import br.com.codein.mobiagecore.domain.model.storage.StorageFile;
import io.gumga.domain.GumgaModel;
import io.gumga.domain.GumgaMultitenancy;
import io.gumga.domain.GumgaMultitenancyPolicy;
import io.gumga.domain.domains.GumgaImage;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Columns;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by gelatti on 21/02/17.
 */
@Entity
@GumgaMultitenancy(policy = GumgaMultitenancyPolicy.ORGANIZATIONAL)
@Audited
@SequenceGenerator(name = GumgaModel.SEQ_NAME, sequenceName = "SEQ_DEPARTMENT")
public class Department extends GumgaModel<Long> implements Serializable {
    @Version
    @ApiModelProperty(hidden = true)
    private Integer version;
    @NotNull(message = "Name should be informed.")
    @ApiModelProperty(position = 1, value = "O nome do Departamento.", required = true)
    private String name;
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    @ApiModelProperty(hidden = true)
    private Set<Category> categories;
    @ManyToMany
    @ApiModelProperty(position = 3, value = "As caracteristicas que todos os filhos desse departamento irão ter.")
    private Set<Characteristic> characteristics = new HashSet<>();
    @ElementCollection
    @Column(name = "name_mount")
    @ApiModelProperty(hidden = true)
    private List<String> nameMount;
    @ApiModelProperty(value = "Nome do tipo de variação que o departamento segue.", position = 5)
    private VariationType variation;
    @ApiModelProperty(value = "ID usado para integração com outros softwares", position = 14)
    private Long integrationId;
    @ApiModelProperty(value = "Determina se o departamento esta ativo ou não", position = 15)
    private Boolean active = Boolean.TRUE;
    @OneToOne
    @ApiModelProperty(value = "Imagem do departamento", position = 11)
    private StorageFile file;

    @Size(max = 3, message = "skuId cant have more than 3 characters.")
    @ApiModelProperty(value = "Identificado que será utilizado para montar o SKU do produto.", position = 16)
    private String skuId;

    public Department() {
        this.categories = new HashSet<>();
        this.characteristics = new HashSet<>();
        this.nameMount = new ArrayList<>();
    }

    public Department(String name) {
        this.name = name;

    }

    public Department(String name, Set<Category> categories) {
        this.name = name;

        this.categories = categories;
    }


    public Department(String name, Set<Category> categories, String skuId) {
        this.name = name;
        this.skuId = skuId;
        this.categories = categories;
    }

    public Department(String name, Set<Category> categories, Set<Characteristic> characteristics) {
        this.name = name;
        this.categories = categories;
        this.characteristics = characteristics;

    }

    public Department(String name, StorageFile file, Set<Category> categories, Set<Characteristic> characteristics,
                      List<String> nameMount, VariationType variation, Long integrationId, Boolean active) {
        this.name = name;
        this.file = file;
        this.categories = categories;
        this.characteristics = characteristics;
        this.nameMount = nameMount;
        this.variation = variation;
        this.integrationId = integrationId;
        this.active = active;

    }

    public Department(Long id, String name,
                      Set<Category> categories,
                      Set<Characteristic> characteristics,
                      List<String> nameMount,
                      VariationType variation,
                      Boolean active,
                      StorageFile file) {
        this.id = id;
        this.name = name;
        this.file = file;
        this.categories = categories;
        this.characteristics = characteristics;
        this.nameMount = nameMount;
        this.variation = variation;
        this.active = active;
    }

    public Department(Long id, String name,
                      Set<Category> categories,
                      Set<Characteristic> characteristics,
                      List<String> nameMount,
                      VariationType variation,
                      Boolean active,
                      StorageFile file,
                      String skuId) {
        this.id = id;
        this.name = name;
        this.file = file;
        this.categories = categories;
        this.characteristics = characteristics;
        this.nameMount = nameMount;
        this.variation = variation;
        this.active = active;
        this.skuId = skuId;
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

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public Set<Characteristic> getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(Set<Characteristic> characteristics) {
        this.characteristics = characteristics;
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

    public VariationType getVariation() {
        return variation;
    }

    public void setVariation(VariationType variation) {
        this.variation = variation;
    }

    public void setOnChildrens() {
        if (this.categories != null) {
            this.categories.forEach(category -> category.setDepartment(this));
        }
    }

    public StorageFile getFile() {
        return file;
    }

    public void setFile(StorageFile file) {
        this.file = file;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }
}
