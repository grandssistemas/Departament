package br.com.codein.department.application.utils;

import br.com.codein.buddycharacteristic.domain.characteristic.Characteristic;
import br.com.codein.department.application.service.DepartmentService;
import br.com.codein.department.domain.model.department.Category;
import br.com.codein.department.domain.model.department.Department;
import br.com.codein.department.domain.model.department.ProductType;
import br.com.codein.department.domain.model.department.enums.ControlType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by rafael on 05/08/15.
 */
@Component
public class DepartmentCrudUtil {
    @Autowired
    private DepartmentService departmentService;

    public static Department departmentOnlyName(){
        return new Department("Department only name", true);
    }
    public static Department departmentWithPattern(){
        return new Department("Department only name","TAMANHO;COR", true);
    }
    public static Department departmentWithCharacs(){
        Set<Characteristic> values = new HashSet<>();
        values.add(CharacteristicUtil.characteristicLogic());
        values.add(CharacteristicUtil.charactersticTamanho());
        return new Department("Department characteristics",values, true);
    }

    @Transactional
    public ProductType getNewProductType(){
        Department department = new Department("Telefonia", true);
        department.setCategories(new HashSet<>());
        Category category = new Category("Telefonia movel",department, true);
        category.setProductTypes(new HashSet<>());
        department.getCategories().add(category);
        ProductType pt = new ProductType("Telefone",false, true);
        pt.setCategory(category);
        pt.setCharacteristics(new ArrayList<>());
        pt.setIsGrid(false);
        pt.setControlTypeProduct(ControlType.AMOUNT);
        category.getProductTypes().add(pt);
        Department dep = pt.getCategory().getDepartment();
        departmentService.save(dep);

        return pt;
    }

    @Transactional
    public ProductType getGasProductType(){
        Department department = new Department("Liquído", true);
        department.setCategories(new HashSet<>());
        Category category = new Category("Inflamável",department, true);
        category.setProductTypes(new HashSet<>());
        department.getCategories().add(category);
        ProductType pt = new ProductType("Combustível",false, true);
        pt.setCategory(category);
        pt.setCharacteristics(new ArrayList<>());
        pt.setIsGrid(false);
        pt.setControlTypeProduct(ControlType.GASTYPE);
        category.getProductTypes().add(pt);
        Department dep = pt.getCategory().getDepartment();
        departmentService.save(dep);

        return pt;
    }
}
