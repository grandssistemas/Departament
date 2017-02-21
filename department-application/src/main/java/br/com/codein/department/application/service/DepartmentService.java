package br.com.codein.department.application.service;

import br.com.codein.department.domain.model.department.Category;
import br.com.codein.department.domain.model.department.Department;
import br.com.codein.department.domain.model.department.ProductType;
import br.com.codein.department.domain.model.exception.ValidationException;
import br.com.codein.buddycharacteristic.application.service.characteristic.CharacteristicService;
import br.com.codein.buddycharacteristic.domain.characteristic.Characteristic;
import br.com.codein.buddycharacteristic.domain.characteristic.enums.ValueTypeCharacteristic;
import br.com.codein.department.application.repository.DepartmentRepository;
import io.gumga.application.GumgaService;
import io.gumga.core.GumgaThreadScope;
import io.gumga.core.QueryObject;
import io.gumga.core.SearchResult;
import io.gumga.domain.GumgaMultitenancy;
import io.gumga.domain.GumgaMultitenancyPolicy;
import io.gumga.domain.repository.GumgaMultitenancyUtil;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gelatti on 21/02/17.
 */
@Service
public class DepartmentService extends GumgaService<Department, Long> {

    private DepartmentRepository repository;

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CharacteristicService characteristicService;


    @Override
    @Transactional
    public Department save(Department resource) {
        initializeDepartment(resource);
        if (resource.getPatterns() != null) {
            if (!this.isPatternTypesCountRight(resource.getPatterns())) {
                throw new ValidationException("In Department patterns count isn't right");
            } else if (!this.isPatternTypesRight(resource.getPatterns())) {
                throw new ValidationException("In Department patterns types aren't right");
            }
        }
        super.save(resource);
        return resource;
    }

    @Override
    @Transactional
    public void beforeSave(Department entity) {
        if (!entity.getActive()) {
            for (Category category : entity.getCategories()) {
                category.setActive(entity.getActive());
                category.getProductTypes().forEach(pt -> pt.setActive(entity.getActive()));
            }
        }

        super.beforeSave(entity);
    }

    public Boolean isPatternTypesCountRight(String patterns) {
        if (patterns != null && patterns != "") {
            String arr[];
            arr = patterns.split(";");
            if (arr.length != 2 && arr.length != 0) {
                return false;
            }
        }
        return true;
    }

    public Boolean isPatternTypesRight(String patterns) {
        if (patterns != null) {
            String arr[];
            arr = patterns.split(";");
            for (int i = 0; i < arr.length; i++) {
                if (ValueTypeCharacteristic.getByName(arr[i]) != ValueTypeCharacteristic.LOGICO
                        && ValueTypeCharacteristic.getByName(arr[i]) != ValueTypeCharacteristic.COR
                        && ValueTypeCharacteristic.getByName(arr[i]) != ValueTypeCharacteristic.TAMANHO
                        && ValueTypeCharacteristic.getByName(arr[i]) != ValueTypeCharacteristic.MULTISELECAO) {
                    return false;
                }
            }
        }
        return true;

    }

    @Autowired
    public DepartmentService(DepartmentRepository repository) {
        super(repository);
        this.repository = repository;
    }

    public Department recoveryByName(String name) {
        return repository.findByName(name);
    }

    public Department loadDepartmentFat(Long id) {
        Department obj = repository.findOne(id);
        Hibernate.initialize(obj.getCharacteristics());
        Hibernate.initialize(obj.getCategories());
        Hibernate.initialize(obj.getNameMount());

        return obj;
    }

    @Transactional
    public Department loadDepartmentFatWithCategories(Long id) {
        Department obj = repository.findById(id);
        if (obj != null) {
            Hibernate.initialize(obj.getCharacteristics());
            Hibernate.initialize(obj.getCategories());
            Hibernate.initialize(obj.getNameMount());
            for (Category category : obj.getCategories()) {
                Hibernate.initialize(category.getCharacteristics());
                Hibernate.initialize(category.getNameMount());
                for (Characteristic charac : category.getCharacteristics()) {
                    Hibernate.initialize(charac.getValues());
                }
            }
        }
        return obj;
    }

    @Transactional
    public Department loadDepartmentFatWithCategoriesAndProductType(Long id) {
        Department obj = this.loadDepartmentFatWithCategories(id);
        for (Category category : obj.getCategories()) {
            categoryService.initializeCategory(category);
        }
        return obj;
    }

    public SearchResult<Department> recupera(String father, String hql) {
        HashMap<String, Object> parametros = new HashMap<>();
        parametros.put("name", "%" + father + "%");
        SearchResult<Department> search = repository.search(hql, parametros);
        return search;
    }

    @Transactional
    public void initializeCharacteristics(List<Department> list) {
        for (Department department : list) {
            Hibernate.initialize(department.getCharacteristics());
            Hibernate.initialize(department.getNameMount());
            for (Characteristic charac : department.getCharacteristics()) {
                Hibernate.initialize(charac.getValues());
            }
        }
    }

    public void initializeDepartment(Department resource) {
        Hibernate.initialize(resource);
        Hibernate.initialize(resource.getCharacteristics());
        if (resource.getCharacteristics() != null) {
            for (Characteristic c : resource.getCharacteristics()) {
                characteristicService.initializeCharacteristic(c);
            }
        }
        Hibernate.initialize(resource.getCategories());
        if (resource.getCategories() != null) {
            for (Category c : resource.getCategories()) {
                categoryService.initializeCategory(c);
            }
        }
        Hibernate.initialize(resource.getNameMount());
    }

    public Boolean isEditable(Long id) {
        Department department = this.loadDepartmentFatWithCategoriesAndProductType(id);

        List<ProductType> productTypes = categoryService.getProductTypes(department.getCategories());
        return isEditable(productTypes);
    }

    /**
     * @return List<Department> com os filhos já carregados [categories]
     */
    @Transactional
    public List<Department> loadAllWithFirstChildrens() {
        String oi = GumgaThreadScope.organizationCode.get();
        String hql = "SELECT obj FROM Department obj WHERE (obj.oi IS NULL OR obj.oi LIKE '"+oi+"%')";
        List<Department> toReturn = repository.search(hql,new HashMap<>()).getValues();
//        List<Department> toReturn = repository.findAllWithTenancy().getValues();
        for(Department dep:toReturn){
            Hibernate.initialize(dep.getCategories());
        }
        return toReturn;
    }


    @Transactional
    public Department getFatherWithChildrens(Long id) {
        Department department = categoryService.view(id).getDepartment();
        Hibernate.initialize(department.getCategories());
        return department;
    }

    /**
     * Função para encontrar um departamento pelo id de integração
     * @param id ID de integração
     * @return O departamento encontrado
     */
    @Transactional
    public Department findByIntegrationId(Long id) {

        QueryObject qo = new QueryObject();
        qo.setAq("obj.integrationId = "+id);
        SearchResult<Department> result = repository.search(qo);
        if(result.getValues().isEmpty()){
            return null;
        }
        return result.getValues().get(0);
    }

    public Boolean isEditable(List<ProductType> productTypes) {
        String multitenancyPattern = GumgaThreadScope.organizationCode.get().substring(0, GumgaThreadScope.organizationCode.get().indexOf(".") + 1);
        if (productTypes.isEmpty())
            return true;
        Map<String, Object> params = new HashMap<>();
        params.put("values", productTypes);
        params.put("oi", multitenancyPattern);
        return repository.search("SELECT p FROM Product p WHERE p.productType in (:values) and (p.oi is null or p.oi like concat(:oi,'%'))", params)
                .getValues()
                .isEmpty();
    }

    public SearchResult<Department> getAll() {
        return repository.findAllWithTenancy();
    }
}