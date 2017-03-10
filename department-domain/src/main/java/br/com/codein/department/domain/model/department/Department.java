package br.com.codein.department.domain.model.department;

import br.com.codein.buddycharacteristic.domain.characteristic.Characteristic;
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
    @ApiModelProperty(position = 1, value="O nome do Departamento.", required = true)
    private String name;
    @Columns(columns = {
            @Column(name = "image_name"),
            @Column(name = "image_size"),
            @Column(name = "image_type"),
            @Column(name = "image_bytes", length = 50 * 1024 * 1024)
    })
    @ApiModelProperty(position = 2, value="A imagem de descrição do departamento.")
    private GumgaImage image;
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    @ApiModelProperty(hidden = true)
    private Set<Category> categories;
    @ManyToMany
    @ApiModelProperty(position = 3, value="As caracteristicas que todos os filhos desse departamento irão ter.")
    private Set<Characteristic> characteristics = new HashSet<>();
    @ElementCollection
    @Column(name="name_mount")
    @ApiModelProperty(hidden = true)
    private List<String> nameMount;
    //Os tipos são separados por ";"
    @ApiModelProperty(value = "Guarda 1 ou 2 valores separados por ';' que podem ser: MULTISELECAO, COR, TAMANHO, LOGICO", position = 4)
    private String patterns;
    @ApiModelProperty(value = "Nome do tipo de variação que o departamento segue.", position = 5)
    private String variation;
    @ApiModelProperty(value = "ID usado para integração com outros softwares", position = 14)
    private Long integrationId;
    @ApiModelProperty(value = "Determina se o departamento esta ativo ou não", position = 15)
    private Boolean active;

    public Department() {
    }

    public Department(String name, Boolean active,Set<Category> categories, GumgaImage image) {
        this.name = name;
        this.categories = categories;
        this.active = active;
        this.image = image;
    }

    public Department(String name, Boolean active, GumgaImage image) {
        this.name = name;
        this.active = active;
        this.image = image;
    }

    public Department(String name, Set<Characteristic> characteristics, Boolean active, GumgaImage image) {
        this.name = name;
        this.characteristics = characteristics;
        this.active = active;
        this.image = image;
    }
    public Department(Long id, String name, Set<Characteristic> characteristics, Boolean active, GumgaImage image) {
        this.name = name;
        this.characteristics = characteristics;
        this.id = id;
        this.active = active;
        this.image = image;
    }

    public Department(String name, String patterns, Boolean active, GumgaImage image) {
        this.name = name;
        this.patterns = patterns;
        this.active = active;
        this.image = image;
    }

    public Department(String name, Set<Category> categories, Set<Characteristic> characteristics, Boolean active, GumgaImage image) {
        this.name = name;
        this.categories = categories;
        this.active = active;
        this.image = image;
    }

    public Department(Long id, String name, Set<Category> categories, Set<Characteristic> characteristics, Boolean active, GumgaImage image) {
        this.name = name;
        this.categories = categories;
        this.characteristics = characteristics;
        this.id = id;
        this.active = active;
        this.image = image;
    }

    public Department(Long id, String name, Set<Category> categories, Set<Characteristic> characteristics, List<String> nameMount, String patterns, String variation, Boolean active, GumgaImage image) {
        this.name = name;
        this.categories = categories;
        this.characteristics = characteristics;
        this.id = id;
        this.nameMount = nameMount;
        this.patterns = patterns;
        this.variation = variation;
        this.active = active;
        this.image = image;
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

    public GumgaImage getImage() {
        return image;
    }

    public void setImage(GumgaImage image) {
        this.image = image;
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

    public String getPatterns() {
        return patterns;
    }

    public void setPatterns(String patterns) {
        this.patterns = patterns;
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
