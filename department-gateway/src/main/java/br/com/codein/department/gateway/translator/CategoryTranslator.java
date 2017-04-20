package br.com.codein.department.gateway.translator;

import br.com.codein.buddycharacteristic.application.service.characteristic.AssociativeCharacteristicService;
import br.com.codein.buddycharacteristic.domain.characteristic.AssociativeCharacteristic;
import br.com.codein.buddycharacteristic.domain.characteristic.Characteristic;
import br.com.codein.department.application.service.CategoryService;
import br.com.codein.department.application.service.DepartmentService;
import br.com.codein.department.domain.model.department.Category;
import br.com.codein.department.domain.model.department.Department;
import br.com.codein.department.domain.model.department.ProductType;
import br.com.codein.department.gateway.dto.department.CategoryDTO;
import br.com.codein.department.gateway.dto.department.CategoryType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by gelatti on 21/02/17.
 */
@Component
public class CategoryTranslator {

    @Autowired //TODO TESTAR+
    private CategoryService categoryService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private AssociativeCharacteristicService associativeCharacteristicService;


    @Transactional
    public Object to(CategoryDTO dto, Object father) {
        Object toReturn = new Object();
        Set<Category> categories = new HashSet<>();
        Set<ProductType> productTypes = new HashSet<>();

        switch (dto.categoryType) {
            case DEPARTMENT:
                toReturn = new Department(dto.id, dto.name, categories, dto.characteristics, dto.nameMount, dto.patterns, dto.variation, dto.active, dto.image);
                ((Department) toReturn).setVersion(dto.version);
                if (dto.childrens.size() == 0 && dto.id != null) {
                    Department dep = departmentService.loadDepartmentFat(dto.id);
                    for (Category cat : dep.getCategories()) {
                        dto.childrens.add(from(cat));
                    }
                }
                for (CategoryDTO children : dto.childrens) {
                    categories.add((Category) to(children, toReturn));
                }
                break;
            case CATEGORY:
                if (father.getClass() == Category.class) {
                    for(Characteristic characteristic: ((Category)father).getCharacteristics()){
                        if(!dto.characteristics.contains(characteristic)) dto.characteristics.add(characteristic);
                    }
                    toReturn = new Category(dto.id, dto.name, dto.description, categories, productTypes, dto.characteristics, (Category) father, dto.nameMount, dto.active, dto.image);
                } else {
                    for(Characteristic characteristic: ((Department)father).getCharacteristics()){
                        if(!dto.characteristics.contains(characteristic)) dto.characteristics.add(characteristic);
                    }
                    toReturn = new Category(dto.id, dto.name, dto.description, categories, productTypes, dto.characteristics, (Department) father, dto.nameMount, dto.active, dto.image);
                }
                ((Category) toReturn).setVersion(dto.version);

                if (dto.childrens.size() == 0 && dto.id != null) {
                    Category dep = categoryService.loadCategoriaFat(dto.id);
                    for (Category cat : dep.getCategories()) {
                        dto.childrens.add(from(cat));
                    }
                    for (ProductType prod : dep.getProductTypes()) {
                        dto.childrens.add(from(prod));
                    }
                }

                for (CategoryDTO children : dto.childrens) {
                    if (children.categoryType == CategoryType.CATEGORY) {
                        categories.add((Category) to(children, toReturn));
                    } else if (children.categoryType == CategoryType.PRODUCTTYPE) {
                        productTypes.add((ProductType) to(children, toReturn));
                    }
                }
                break;
            case PRODUCTTYPE:
                for(Characteristic characteristic: ((Category)father).getCharacteristics()){
                    if (dto.characteristicsPT.stream().filter(data -> data.getCharacteristic().equals(characteristic)).count() == 0){
                        AssociativeCharacteristic cpt = new AssociativeCharacteristic();
                        cpt.setCharacteristic(characteristic);
                        cpt.setGridCount(0);
                        cpt.setIsGrid(false);
                        cpt.setIsRequired(false);
                        cpt.setHaveRequired(false);
                        associativeCharacteristicService.save(cpt);
                        if(dto.nameMount.contains(cpt.getCharacteristic().getName())){
                            dto.nameMount.add(cpt.getCharacteristic().getName());
                        }
                        dto.characteristicsPT.add(cpt);

                    }
                }
                toReturn = new ProductType(dto.id, dto.name, dto.characteristicsPT, dto.isGrid, (Category) father,
                        dto.nameMount, dto.patterns, dto.variation, dto.controlTypeProduct, dto.typeLabeling, dto.active, dto.image);
                ((ProductType) toReturn).setVersion(dto.version);
                break;
        }

        return toReturn;
    }

    //Pega a arvore
    public CategoryDTO from(Department department) {
        CategoryDTO dto = new CategoryDTO();
        dto.id = department.getId();
        dto.version = department.getVersion();
        dto.name = department.getName();
        dto.variation = department.getVariation();
        dto.characteristics = department.getCharacteristics();
        dto.childrens = new ArrayList<>();
        for (Category category : department.getCategories()) {
            dto.childrens.add(from(category));
        }
        dto.categoryType = CategoryType.DEPARTMENT;
        dto.nameMount = department.getNameMount();
        dto.patterns = department.getPatterns();
        dto.active = department.getActive();
        dto.image = department.getImage();
        return dto;
    }

    public CategoryDTO from(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.id = category.getId();
        dto.version = category.getVersion();
        dto.name = category.getName();
        dto.description = category.getDescription();
        dto.characteristics = category.getCharacteristics();
        dto.childrens = new ArrayList<>();
        for (Category ct : category.getCategories()) {
            dto.childrens.add(from(ct));
        }
        for (ProductType pt : category.getProductTypes()) {
            dto.childrens.add(from(pt));
        }
        dto.categoryType = CategoryType.CATEGORY;
        dto.nameMount = category.getNameMount();
        dto.active = category.getActive();
        dto.image = category.getImage();
        return dto;
    }

    public CategoryDTO from(ProductType productType) {
        CategoryDTO dto = new CategoryDTO();
        dto.id = productType.getId();
        dto.version = productType.getVersion();
        dto.name = productType.getName();
        dto.variation = productType.getVariation();
        dto.characteristicsPT = productType.getCharacteristics();
        dto.isGrid = productType.getIsGrid();
        dto.categoryType = CategoryType.PRODUCTTYPE;
        dto.nameMount = productType.getNameMount();
        dto.patterns = productType.getGridPattern();
        dto.controlTypeProduct = productType.getControlTypeProduct();
        dto.typeLabeling = productType.getTypeLabeling();
        dto.active = productType.getActive();
        dto.image = productType.getImage();
        return dto;
    }

    //Pega sem caracteristica
    public CategoryDTO fromWithNoCharacteristic(Department department) {
        CategoryDTO dto = new CategoryDTO();
        dto.id = department.getId();
        dto.version = department.getVersion();
        dto.name = department.getName();
        dto.variation = department.getVariation();

        dto.childrens = new ArrayList<>();
        for (Category category : department.getCategories()) {
            dto.childrens.add(from(category));
        }
        dto.categoryType = CategoryType.DEPARTMENT;
        dto.patterns = department.getPatterns();
        dto.active = department.getActive();
        dto.image = department.getImage();
        return dto;
    }

    public CategoryDTO fromWithNoCharacteristic(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.id = category.getId();
        dto.version = category.getVersion();
        dto.name = category.getName();
        dto.description = category.getDescription();
        dto.childrens = new ArrayList<>();
        for (Category ct : category.getCategories()) {
            dto.childrens.add(from(ct));
        }
        for (ProductType pt : category.getProductTypes()) {
            dto.childrens.add(from(pt));
        }
        dto.categoryType = CategoryType.CATEGORY;
        dto.active = category.getActive();
        dto.image = category.getImage();
        return dto;
    }

    public CategoryDTO fromWithNoCharacteristic(ProductType productType) {
        CategoryDTO dto = new CategoryDTO();
        dto.id = productType.getId();
        dto.version = productType.getVersion();
        dto.variation = productType.getVariation();

        dto.name = productType.getName();
        dto.isGrid = productType.getIsGrid();
        dto.categoryType = CategoryType.PRODUCTTYPE;
        dto.patterns = productType.getGridPattern();
        dto.controlTypeProduct = productType.getControlTypeProduct();
        dto.typeLabeling = productType.getTypeLabeling();
        dto.active = productType.getActive();
        dto.image = productType.getImage();
        return dto;
    }

    //Faz o galho da recursão
    public CategoryDTO fork(ProductType obj) {
        CategoryDTO dto;
        CategoryDTO dtoAux;
        CategoryDTO dtoObj;
        dto = fork(obj.getCategory());
        if (dto.childrens.size() > 0) {
            dtoAux = dto.childrens.get(0);
        } else {
            dtoAux = dto;
        }
        while (dtoAux.childrens.size() > 0) {
            dtoAux = dtoAux.childrens.get(0);
        }
        dtoObj = fromWithNoCharacteristic(obj);
        dtoObj.childrens = new ArrayList<>();
        dtoAux.childrens.add(dtoObj);
        dto.characteristicsPT = null;
        dto.nameMount = obj.getNameMount();
        dto.variation = obj.getVariation();

        dto.patterns = obj.getGridPattern();
        dto.controlTypeProduct = obj.getControlTypeProduct();
        dto.typeLabeling = obj.getTypeLabeling();
        dto.active = obj.getActive();
        dto.image = obj.getImage();
        return dto;
    }

    public CategoryDTO fork(Category obj) {
        CategoryDTO dto;
        CategoryDTO dtoAux;
        CategoryDTO dtoObj;
        if (obj.getDepartment() != null) {
            dto = fork(obj.getDepartment());
        } else {
            dto = fork(obj.getCategory());
        }
        if (dto.childrens.size() > 0) {
            dtoAux = dto.childrens.get(0);
        } else {
            dtoAux = dto;
        }
        while (dtoAux.childrens.size() > 0) {
            dtoAux = dtoAux.childrens.get(0);
        }
        dtoObj = fromWithNoCharacteristic(obj);
        dtoObj.childrens = new ArrayList<>();
        dtoAux.childrens.add(dtoObj);
        dto.characteristics = null;
        dto.nameMount = null;
        dto.active = obj.getActive();
        dto.image = obj.getImage();
        return dto;
    }

    public CategoryDTO fork(Department obj) {
        CategoryDTO dto = fromWithNoCharacteristic(obj);
        dto.childrens = new ArrayList<>();
        dto.characteristics = null;
        dto.nameMount = null;
        dto.patterns = obj.getPatterns();
        dto.variation = obj.getVariation();
        dto.active = obj.getActive();
        dto.image = obj.getImage();
        return dto;
    }

    //    Pega so o nó sem os filhos
    public CategoryDTO fromWithoutChildrens(Department department) {
        CategoryDTO dto = new CategoryDTO();
        dto.id = department.getId();
        dto.version = department.getVersion();
        dto.name = department.getName();
        dto.variation = department.getVariation();
        dto.characteristics = department.getCharacteristics();
        dto.categoryType = CategoryType.DEPARTMENT;
        dto.nameMount = department.getNameMount();
        dto.active = department.getActive();
        dto.image = department.getImage();
        return dto;
    }

    public CategoryDTO fromWithoutChildrens(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.id = category.getId();
        dto.version = category.getVersion();
        dto.name = category.getName();
        dto.description = category.getDescription();
        dto.characteristics = category.getCharacteristics();
        dto.categoryType = CategoryType.CATEGORY;
        dto.nameMount = category.getNameMount();
        dto.active = category.getActive();
        dto.image = category.getImage();
        return dto;
    }

    public CategoryDTO fromWithoutChildrens(ProductType productType) {
        CategoryDTO dto = from(productType);
        dto.characteristicsPT = productType.getCharacteristics();
        dto.nameMount = productType.getNameMount();
        dto.variation = productType.getVariation();
        dto.patterns = productType.getGridPattern();
        dto.controlTypeProduct = productType.getControlTypeProduct();
        dto.typeLabeling = productType.getTypeLabeling();
        dto.active = productType.getActive();
        dto.image = productType.getImage();
        return dto;
    }

    /**
     * @param category valor a ser convertido para o DTO
     * @return Valor convertido para DTO apernas com o id,name,categoryType
     */
    private CategoryDTO fromWithoutCharacteristics(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.id = category.getId();
        dto.name = category.getName();
        dto.categoryType = CategoryType.CATEGORY;
        dto.active = category.getActive();
        dto.image = category.getImage();
        return dto;
    }

    /**
     * @param productType valor a ser convertido para o DTO
     * @return Valor convertido para DTO apernas com o id,name,categoryType
     */
    private CategoryDTO fromWithoutCharacteristics(ProductType productType) {
        CategoryDTO dto = new CategoryDTO();
        dto.id = productType.getId();
        dto.name = productType.getName();
        dto.categoryType = CategoryType.PRODUCTTYPE;
        dto.active = productType.getActive();
        dto.image = productType.getImage();
        return dto;
    }

    //Pega o primeiro nivel de filhos
    public List<CategoryDTO> getChildrens(Department department) {
        List<CategoryDTO> childrens = new ArrayList<>();
        if (department.getCategories() != null) {
            for (Category cat : department.getCategories()) {
                childrens.add(fromWithoutChildrens(cat));
            }
            return childrens;
        }
        return new ArrayList<>();
    }

    public List<CategoryDTO> getChildrens(Category category) {
        List<CategoryDTO> childrens = new ArrayList<>();
        for (Category cat : category.getCategories()) {
            childrens.add(fromWithoutChildrens(cat));
        }
        for (ProductType productType : category.getProductTypes()) {
            childrens.add(fromWithoutChildrens(productType));
        }
        return childrens;
    }

    /**
     * @param department departamento que será modificado os filhos
     * @return CategoryDTO já contendo os filhos convertidos para o DTO
     */
    //Pega o primeiro nivel de filhos sem caracteristicas
    @Transactional
    public CategoryDTO getWithLazyChildrens(Department department) {
        CategoryDTO dto = new CategoryDTO();
        dto.id = department.getId();
        dto.name = department.getName();
        dto.categoryType = CategoryType.DEPARTMENT;
        dto.active = department.getActive();
        dto.image = department.getImage();
        List<CategoryDTO> childrens = new ArrayList<>();
        if (department.getCategories() != null) {
            for (Category cat : department.getCategories()) {
                childrens.add(fromWithoutCharacteristics(cat));
            }
        }
        dto.childrens = childrens;
        return dto;
    }


    /**
     * @param category departamento que será modificado os filhos
     * @return CategoryDTO já com os filhos convertidos para o DTO
     */
    @Transactional
    public CategoryDTO getWithLazyChildrens(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.id = category.getId();
        dto.name = category.getName();
        dto.categoryType = CategoryType.CATEGORY;
        dto.active = category.getActive();
        dto.image = category.getImage();
        List<CategoryDTO> childrens = new ArrayList<>();
        for (Category cat : category.getCategories()) {
            childrens.add(fromWithoutCharacteristics(cat));
        }
        for (ProductType productType : category.getProductTypes()) {
            childrens.add(fromWithoutCharacteristics(productType));
        }
        dto.childrens = childrens;
        return dto;
    }



}
