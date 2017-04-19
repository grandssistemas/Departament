package br.com.codein.department.application.service;

import br.com.codein.department.domain.model.department.Department;
import br.com.codein.department.domain.model.department.ProductType;
import br.com.codein.department.domain.model.exception.ValidationException;
import br.com.codein.buddycharacteristic.application.service.characteristic.AssociativeCharacteristicService;
import br.com.codein.buddycharacteristic.domain.characteristic.AssociativeCharacteristic;
import br.com.codein.buddycharacteristic.domain.characteristic.Characteristic;
import br.com.codein.buddycharacteristic.domain.characteristic.enums.ValueTypeCharacteristic;
import br.com.codein.department.application.repository.ProductTypeRepository;
import io.gumga.application.GumgaService;
import io.gumga.core.QueryObject;
import io.gumga.core.SearchResult;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gelatti on 21/02/17.
 */
@Service
public class ProductTypeService extends GumgaService<ProductType, Long> {

    private ProductTypeRepository repository;


    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private AssociativeCharacteristicService associativeCharacteristicService;

    @Override
    @Transactional
    public ProductType save(ProductType resource) {
        if (!isGridPatternRight(resource)) {
            throw new ValidationException("The gridPattern is not right for the grid configuration in this product type");
        }
        if(!checkCharacteristicContain(resource)){
            throw new ValidationException("The father characteristics are no contained in characteristics");
        }
        if (resource.getIsGrid()) {
//            if (!departmentService.isPatternTypesCountRight(resource.getGridPattern())) {
//                throw new ValidationException("In ProductType patterns count isn't right");
//            } else if (!departmentService.isPatternTypesRight(resource.getGridPattern())) {
//                throw new ValidationException("In ProductType patterns types aren't right");
//            } else
                if (!isGridCharacteristicRight(resource)) {
                throw new ValidationException("The grid characteristics values is not in the right quantity");
            } else if (!isGridValuesTypeRight(resource)) {
                throw new ValidationException("The grid characteristics values types are not matching with the gridPattern");
            }else if(gridCharacteristicCount(resource) < 1 && gridCharacteristicCount(resource) > 2){
                throw new ValidationException("This product type grid characteristic count is not right");
            }
        }else{
            if(gridCharacteristicCount(resource) > 0){
                throw new ValidationException("This product type should not have grid characteristic");
            }
        }
        super.save(resource);
        return resource;
    }

    @Override
    public void beforeSave(ProductType entity) {
        if (entity.getActive()) {
            entity.getCategory().setActive(entity.getActive());
            entity.getCategory().getDepartment().setActive(entity.getActive());
        }
        super.beforeSave(entity);
    }

    @Transactional
    private List<AssociativeCharacteristic> initializeOptions(ProductType resource){
        List<AssociativeCharacteristic> oplist = new ArrayList<>();
        for (AssociativeCharacteristic op : resource.getCharacteristics()) {
            oplist.add(associativeCharacteristicService.save(op));
        }
        return oplist;
    }

    @Autowired
    public ProductTypeService(ProductTypeRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Transactional
    public ProductType loadProductTypeFat(Long id) {
        ProductType obj = repository.findOne(id);
        Hibernate.initialize(obj.getCharacteristics());
        Hibernate.initialize(obj.getNameMount());
        for (AssociativeCharacteristic c : obj.getCharacteristics()) {
            if (c.getCharacteristic() != null) {
                Hibernate.initialize(c.getCharacteristic().getValues());
            }
        }
        return obj;
    }

    public Boolean isGridValuesTypeRight(ProductType productType) {
        Characteristic col = null;
        Characteristic row = null;

        for(AssociativeCharacteristic cpt:productType.getCharacteristics()){
            if(cpt.getGridCount() == 1){
                col = cpt.getCharacteristic();
            }else if(cpt.getGridCount() == 2){
                row = cpt.getCharacteristic();
            }
        }
//        String[] arr = productType.getGridPattern().split(";");
//        Boolean rowIsRight = row != null && row.getTipoDeValorCaracteristica() == ValueTypeCharacteristic.getByName(arr[0]);
//        Boolean colIsRight = col != null && col.getTipoDeValorCaracteristica() == ValueTypeCharacteristic.getByName(arr[1]);
        return false;
    }

    private Boolean isGridCharacteristicRight(ProductType productType) {
        Characteristic col = null;
        Characteristic row = null;
        for(AssociativeCharacteristic cpt:productType.getCharacteristics()){
            if(cpt.getGridCount() == 1){
                col = cpt.getCharacteristic();
            }else if(cpt.getGridCount() == 2){
                row = cpt.getCharacteristic();
            }
        }
        Boolean haveRow = row != null;
        Boolean haveCol = col != null;
        return haveCol && haveRow;
    }

    private Boolean isGridPatternRight(ProductType resource){
//        return (resource.getIsGrid() && resource.getGridPattern() != null) || (!resource.getIsGrid() && resource.getGridPattern() == null);
        return false;
    }

    private Integer gridCharacteristicCount(ProductType resource){
        int count = 0 ;
        for(AssociativeCharacteristic cpt:resource.getCharacteristics()){
            if(cpt.getIsGrid()){
                count++;
            }
        }
        return count;
    }

    private Boolean checkCharacteristicContain(ProductType resource) {
        if(resource.getCategory() != null){
            List<Characteristic> list = new ArrayList<>(resource.getCategory().getCharacteristics());
            for(int i = list.size() - 1;i>=0;i--){
                Characteristic characteristic = list.get(i);
                if(characteristic.getTipoDeValorCaracteristica() == ValueTypeCharacteristic.TAMANHO){
                    list.remove(characteristic);
                }
            }
            int count = 0;
            for(Characteristic c:list){
                for(AssociativeCharacteristic cpt:resource.getCharacteristics()){
                    if(c.equals(cpt.getCharacteristic())){
                        count++;
                    }
                }
            }
            return count == list.size();
        }
        throw new ValidationException("Category should have a father");
    }

    public ProductType recoveryByName(String name) {
        QueryObject qo = new QueryObject();
        qo.setAq("obj.name = '"+name+"'");
        SearchResult<ProductType> result = repository.search(qo);
        if(result.getValues().isEmpty()){
            return null;
        }
        return result.getValues().get(0);
    }

    public void initializeProductType(ProductType resource){
        Hibernate.initialize(resource);
        Hibernate.initialize(resource.getCharacteristics());
        if(resource.getCharacteristics() != null){
            for(AssociativeCharacteristic c:resource.getCharacteristics()){
                associativeCharacteristicService.initializeAssociativeCharacteristic(c);
            }
        }
        Hibernate.initialize(resource.getNameMount());
    }

    public Boolean isEditable(Long id) {
        ProductType productType = loadProductTypeFat(id);
        List<ProductType> productTypes = new ArrayList<>();
        productTypes.add(productType);
        return departmentService.isEditable(productTypes);
    }

    public SearchResult<ProductType> getAll() {
        List<ProductType> all = repository.findAll();
        return new SearchResult<>(new QueryObject(),all.size(),all);
    }

    public List<ProductType> getAllProducts(){
        return repository.findAll();
    }

    /**
     * Função para encontrar um tipo de produto pelo id de integração
     * @param id ID de integração
     * @return O tipo de produto encontrado
     */
    @Transactional
    public ProductType findByIntegrationId(Long id) {
        QueryObject qo = new QueryObject();
        qo.setAq("obj.integrationId = "+id);
        SearchResult<ProductType> result = repository.search(qo);
        if(result.getValues().isEmpty()){
            return null;
        }
        return result.getValues().get(0);
    }

    @org.springframework.transaction.annotation.Transactional
    public List<ProductType> findAll(){
        return repository.findAllWithTenancy().getValues();
    }
}

