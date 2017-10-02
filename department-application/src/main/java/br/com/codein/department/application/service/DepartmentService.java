package br.com.codein.department.application.service;

import br.com.codein.buddycharacteristic.application.service.characteristic.AssociativeCharacteristicService;
import br.com.codein.buddycharacteristic.application.service.characteristic.CharacteristicService;
import br.com.codein.buddycharacteristic.domain.characteristic.AssociativeCharacteristic;
import br.com.codein.buddycharacteristic.domain.characteristic.Characteristic;
import br.com.codein.buddycharacteristic.domain.characteristic.enums.ValueTypeCharacteristic;
import br.com.codein.department.application.repository.DepartmentRepository;
import br.com.codein.department.domain.model.department.Category;
import br.com.codein.department.domain.model.department.Department;
import br.com.codein.department.domain.model.department.ProductType;
import br.com.codein.department.domain.model.exception.ValidationException;
import br.com.codein.mobiagecore.application.service.storage.StorageFileService;
import br.com.codein.mobiagecore.domain.model.storage.StorageFile;
import io.gumga.application.GumgaService;
import io.gumga.application.GumgaTempFileService;
import io.gumga.core.GumgaThreadScope;
import io.gumga.core.QueryObject;
import io.gumga.core.SearchResult;
import io.gumga.domain.domains.GumgaImage;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static br.com.codein.department.domain.model.department.QDepartment.department;

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
    @Autowired
    private AssociativeCharacteristicService associativeCharacteristicService;
    @Autowired
    private StorageFileService storageFileService;
    @Autowired
    private ProductTypeService productTypeService;


    @Override
    @Transactional
    public Department save(Department resource) {
        validateDepartment(resource);
        initializeDepartment(resource);
        if (resource.getCategories() != null) {
            Set<Category> categories = resource.getCategories().stream().map(category -> {
                if (category.getDepartment() == null){
                    category.setDepartment(resource);
                }
                if (category.getFile() != null) {
                    storageFileService.save(category.getFile());
                }
                if (category.getProductTypes() != null) {
                    Set<ProductType> productTypes = category.getProductTypes().stream().map(productType -> {
                        if (productType.getCategory() == null){
                            productType.setCategory(category);
                        }
                        if (productType.getFile() != null) {
                            storageFileService.save(productType.getFile());
                        }
                        productType.getCharacteristics().forEach(ac -> {
                            if(ac.getId() == null) {
                               associativeCharacteristicService.save(ac);
                            }
                        });
                        productTypeService.validateProductType(productType);
                        return productType;
                    }).collect(Collectors.toSet());
                    category.setProductTypes(productTypes);
                }
                categoryService.validateCategory(category);
                return category;
            }).collect(Collectors.toSet());

            resource.setCategories(categories);
        }
        if (resource.getFile() != null) {
            storageFileService.save(resource.getFile());
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

    @Transactional(readOnly = true)
    public void validateDepartment(Department resource){
        if(departmentIsAlreadyRegistered(resource)){
            throw new ValidationException("dep001;;Department already registered");
        }
    }

    private boolean departmentIsAlreadyRegistered(Department resource) {
        String hql = "lower('"+resource.getName()+"') = lower(obj.name)";
        if(resource.getId() != null){
            hql+=" AND obj.id != "+resource.getId();
        }
        QueryObject qo = new QueryObject();
        qo.setAq(hql);
        return !this.pesquisa(qo).getValues().isEmpty();
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
        QueryObject qo = new QueryObject();
        qo.setAq("upper(obj.name) = '" + name.toUpperCase() + "'");
        SearchResult<Department> result = pesquisa(qo);
        if(result.getValues().isEmpty()){
            return null;
        }
        return result.getValues().get(0);
    }

    @Transactional(readOnly = true)
    public Department loadDepartmentFat(Long id) {
        Department obj = repository.findOne(id);
        Hibernate.initialize(obj.getCharacteristics());
        Hibernate.initialize(obj.getCategories());
        Hibernate.initialize(obj.getNameMount());

        return obj;
    }

    @Transactional
    public Department loadDepartmentFatWithCategories(Long id) {
        Department obj = view(id);
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
    public Department loadFat(Department obj) {
        Hibernate.initialize(obj.getCategories());
        for (Category category : obj.getCategories()) {
            categoryService.initializeCategory(category);
        }
        return obj;
    }

    @Transactional
    public Department loadDepartmentFatWithCategoriesAndProductType(Long id) {
        return loadFat(this.loadDepartmentFatWithCategories(id));
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

    @Transactional(readOnly = true)
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
        String hql = "SELECT obj FROM Department obj WHERE (obj.oi IS NULL OR obj.oi LIKE '" + oi + "%')";
        List<Department> toReturn = repository.search(hql, new HashMap<>()).getValues();
//        List<Department> toReturn = repository.findAllWithTenancy().getValues();
        for (Department dep : toReturn) {
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
     *
     * @param id ID de integração
     * @return O departamento encontrado
     */
    @Transactional
    public Department findByIntegrationId(Long id) {

        QueryObject qo = new QueryObject();
        qo.setAq("obj.integrationId = " + id);
        SearchResult<Department> result = repository.search(qo);
        if (result.getValues().isEmpty()) {
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

    /**
     * Função que retornar todos departmentos cadastrados
     *
     * @return Os departamento encontrados
     */
    public SearchResult<Department> getAll() {
        return repository.findAllWithTenancy();
    }

    /**
     * Função que salva um array de departamentos
     *
     * @param departments Array de departamentos a ser salvo
     * @return Os departamentos salvos
     */
    @Transactional
    public List<Department> saveArray(List<Department> departments) {
        List<Department> savedList = new ArrayList<>();
        Set<Characteristic> characterSet = new HashSet<>();
        Set<Characteristic> characteristicSet = new HashSet<>();
        List<AssociativeCharacteristic> associativeCharacteristicList = new ArrayList<>();
        for (Department department : departments) {
            department.setVersion(null);
            department.getCharacteristics().forEach(characteristic -> {
                characteristicSet.add(createFindCharacteristic(characteristic));
            });
            department.setCharacteristics(characteristicSet);
            department.getCategories().forEach(category -> {
                category.setVersion(null);
                category.setDepartment(department);
                category.getCharacteristics().forEach(character -> {
                    characterSet.add(createFindCharacteristic(character));
                });
                category.setCharacteristics(characterSet);
                category.getProductTypes().forEach(pt -> {
                    pt.setCategory(category);
                    pt.setVersion(null);
                    pt.getCharacteristics().forEach(associativeCharacteristic -> {
                        associativeCharacteristic.setCharacteristic(createFindCharacteristic(associativeCharacteristic.getCharacteristic()));
                        AssociativeCharacteristic associativeCharacteristic1 = associativeCharacteristicService.save(
                                new AssociativeCharacteristic(associativeCharacteristic.getCharacteristic(),
                                        associativeCharacteristic.getGridCount()));
                        associativeCharacteristicList.add(associativeCharacteristic1);
                    });
                    pt.setCharacteristics(associativeCharacteristicList);
                });
            });
            save(department);
            savedList.add(department);
        }
        return savedList;
    }


    private Characteristic createFindCharacteristic (Characteristic characteristic) {
        SearchResult<Characteristic> characterSearchResult = characteristicService.recoveryByNameWithTenancy(characteristic.getName());
        if (characterSearchResult.getValues().isEmpty()) {
            Characteristic newCharacter = new Characteristic(characteristic.getName(),
                    characteristic.getCharacteristicValueType(), characteristic.getValues(),
                    characteristic.getOrigin());
            Characteristic charac = characteristicService.save(newCharacter);
            return charac;
        } else {
            return characterSearchResult.getValues().get(0);
        }
    }

    /**
     * Função que retorna um array com todos os filhos e caracteristicas
     *
     * @param departments Array de departamentos a ser carregado
     * @return Os departamentos carregados
     */
    public List<Department> getFatArray(List<Department> departments) {
        List<Department> departmentList = new ArrayList<>();
        for (Department department : departments) {
            departmentList.add(loadDepartmentFatWithCategoriesAndProductType(department.getId()));
        }
        return departmentList;
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    @Transactional
    public List<Department> findAll(){
        return repository.findAllWithTenancy().getValues();
    }

}