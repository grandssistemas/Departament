package br.com.codein.department.application.service;

import br.com.codein.department.domain.model.department.Category;
import br.com.codein.department.domain.model.department.Department;
import br.com.codein.department.domain.model.department.ProductType;
import br.com.codein.department.domain.model.exception.ValidationException;
import br.com.codein.buddycharacteristic.application.service.characteristic.CharacteristicService;
import br.com.codein.buddycharacteristic.domain.characteristic.AssociativeCharacteristic;
import br.com.codein.buddycharacteristic.domain.characteristic.Characteristic;
import br.com.codein.buddycharacteristic.domain.characteristic.enums.ValueTypeCharacteristic;
import br.com.codein.department.application.repository.CategoryRepository;
import io.gumga.application.GumgaService;
import io.gumga.core.QueryObject;
import io.gumga.core.SearchResult;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gelatti on 21/02/17.
 */
@Service
public class CategoryService extends GumgaService<Category, Long> {

    private CategoryRepository repository;

    @Autowired
    private ProductTypeService productTypeService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private CharacteristicService characteristicService;

    @Autowired
    public CategoryService(CategoryRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Transactional
    public Category loadCategoriaFat(Long id) {
        Category obj = repository.findOne(id);
        Hibernate.initialize(obj.getCategories());
        Hibernate.initialize(obj.getProductTypes());
        for (ProductType pt : obj.getProductTypes()) {
            Hibernate.initialize(pt.getCharacteristics());
        }
        Hibernate.initialize(obj.getCharacteristics());
        Hibernate.initialize(obj.getNameMount());
        return obj;
    }

    public Category recoveryByName(String name) {
        return repository.findByName(name);
    }

    public SearchResult<Category> recupera(String father, String hql) {
        HashMap<String, Object> parametros = new HashMap<>();
        parametros.put("name", "%" + father + "%");
        SearchResult<Category> search = repository.search(hql, parametros);
        return search;
    }

    @Transactional
    public ResponseEntity getFatherOptions(String param) {
        if (param == null) {
            param = new String();
        }

        String query = "from Category ct where ct.name like :name)";
        SearchResult<Category> recupera = this.recupera(param, query);

        String queryd = "from Department dt where dt.name like :name)";
        SearchResult<Department> recuperad = departmentService.recupera(param, queryd);

        int ps = recuperad.getPageSize() + recupera.getPageSize();
        Long count = recuperad.getCount() + recupera.getCount();

        List<Object> list = new ArrayList<>();
        list.addAll(recupera.getValues());
        list.addAll(recuperad.getValues());

        SearchResult<Object> sr = new SearchResult<Object>(0, ps, count, list);
        return new ResponseEntity(sr, HttpStatus.MULTI_STATUS.OK);
    }

    public Category loadCategoryFatWithCategories(Long id) {
        Category obj = repository.findById(id);
        if (obj != null) {
            Hibernate.initialize(obj.getCharacteristics());
            Hibernate.initialize(obj.getCategories());
            Hibernate.initialize(obj.getProductTypes());
            Hibernate.initialize(obj.getNameMount());
            for (Category category : obj.getCategories()) {
                Hibernate.initialize(category.getCharacteristics());
                Hibernate.initialize(category.getNameMount());
                for (Characteristic charac : category.getCharacteristics()) {
                    Hibernate.initialize(charac.getValues());
                }
            }
            for (ProductType productType : obj.getProductTypes()) {
                Hibernate.initialize(productType.getCharacteristics());
                Hibernate.initialize(productType.getNameMount());
                for (AssociativeCharacteristic charac : productType.getCharacteristics()) {
                    Hibernate.initialize(charac.getCharacteristic().getValues());
                }
            }
        }
        return obj;
    }

    //TODO ARRUMAR A REGRA DE NEGOCIO PARA COMPARAR A CARACTERISTICA DE TAMANHO DO PAI
    private Boolean checkCharacteristicContain(Category category) {
        if (category.getDepartment() != null) {
            List<Characteristic> list = new ArrayList<>(category.getDepartment().getCharacteristics());
            for (int i = list.size() - 1; i >= 0; i--) {
                Characteristic characteristic = list.get(i);
                if (characteristic.getTipoDeValorCaracteristica() == ValueTypeCharacteristic.TAMANHO) {
                    list.remove(characteristic);
                }
            }
            return category.getCharacteristics().containsAll(list);
        } else if (category.getCategory() != null) {
            return category.getCharacteristics().containsAll(category.getCategory().getCharacteristics());
        }
        throw new ValidationException("Category should have a father");
    }

    private Boolean haveFather(Category category) {
        return (category.getDepartment() != null || category.getCategory() != null);
    }

    @Override
    @Transactional
    public Category save(Category resource) {
        validateCategory(resource);
        super.save(resource);
        return resource;
    }

    public void validateCategory(Category resource){
        if (!haveFather(resource)) {
            throw new ValidationException("Category should have a father");
        } else if (!checkFatherCountIsRight(resource)) {
            throw new ValidationException("Category father quantity is not right");
        } else if (!checkCharacteristicContain(resource)) {
            throw new ValidationException("Category should have father's characteristics");
        }
    }

    @Override
    @Transactional
    public void beforeSave(Category entity) {
        if (!entity.getActive()) {
            entity.getProductTypes().forEach(pt -> pt.setActive(entity.getActive()));
        } else {
            if (entity.getDepartment() != null) {
                entity.getDepartment().setActive(entity.getActive());
            }
        }
        super.beforeSave(entity);
    }

    private Boolean checkFatherCountIsRight(Category resource) {
        return !(resource.getCategory() != null && resource.getDepartment() != null);
    }

    public void initializeCategory(Category resource) {
        Hibernate.initialize(resource);
        Hibernate.initialize(resource.getCategories());
        if (resource.getCategories() != null) {
            for (Category c : resource.getCategories()) {
                initializeCategory(c);
            }
        }
        Hibernate.initialize(resource.getCharacteristics());
        if(resource.getCharacteristics() != null){
            for(Characteristic c: resource.getCharacteristics()){
                characteristicService.initializeCharacteristic(c);
            }
        }
        Hibernate.initialize(resource.getProductTypes());
        if(resource.getProductTypes() != null){
            for(ProductType p: resource.getProductTypes()){
                productTypeService.initializeProductType(p);
            }
        }
        Hibernate.initialize(resource.getNameMount());

    }

    public List<ProductType> getProductTypes(Collection<Category> categories){
        List<ProductType> productTypes = new ArrayList<>();
        for(Category c:categories){
            if(c.getCategories() != null){
                productTypes.addAll(getProductTypes(c.getCategories()));
            }
            if(c.getProductTypes() != null){
                productTypes.addAll(c.getProductTypes());
            }
        }
        return productTypes;
    }

    public Boolean isEditable(Long id) {
        Category category = loadCategoryFatWithCategories(id);
        initializeCategory(category);
        List<ProductType> productTypes = new ArrayList<>();
        if(category.getProductTypes() != null){
            productTypes.addAll(category.getProductTypes());
        }
        productTypes.addAll(getProductTypes(category.getCategories()));
        return departmentService.isEditable(productTypes);
    }

    /**
     * @return List<Category> com os filhos já carregados [categories,productTypes]
     */
    @Transactional
    public List<Category> loadAllWithFirstChildrens() {
        List<Category> toReturn = repository.findAllWithTenancy().getValues();
        for(Category cat :toReturn){
            Hibernate.initialize(cat.getCategories());
            Hibernate.initialize(cat.getProductTypes());
        }
        return toReturn;
    }

    @Transactional
    public Category getFatherWithChildrens(Long id) {
        Category category = productTypeService.view(id).getCategory();
        Hibernate.initialize(category.getCategories());
        Hibernate.initialize(category.getProductTypes());
        return category;
    }

    /**
     * Função para encontrar uma categoria pelo id de integração
     * @param id ID de integração
     * @return A categoria encontrado
     */
    @Transactional
    public Category findByIntegrationId(Long id) {
        QueryObject qo = new QueryObject();
        qo.setAq("obj.integrationId = "+id);
        SearchResult<Category> result = repository.search(qo);
        if(result.getValues().isEmpty()){
            return null;
        }
        return result.getValues().get(0);
    }

    @Transactional
    public List<Category> findAll(){
        return repository.findAllWithTenancy().getValues();
    }
}


