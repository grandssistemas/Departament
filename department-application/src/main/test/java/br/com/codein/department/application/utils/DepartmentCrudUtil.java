package br.com.codein.department.application.utils;

import br.com.codein.buddycharacteristic.domain.characteristic.Characteristic;
import br.com.codein.department.application.service.DepartmentService;
import br.com.codein.department.domain.model.department.Category;
import br.com.codein.department.domain.model.department.Department;
import br.com.codein.department.domain.model.department.ProductType;
import br.com.codein.department.domain.model.department.enums.ProductEspecification;
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
        return new Department("Department only name");
    }
    public static Department departmentWithPattern(){
        return new Department("Department only name");
    }
    public static Department departmentWithCharacs(){
        Set<Characteristic> values = new HashSet<>();
        values.add(CharacteristicUtil.characteristicLogic());
        values.add(CharacteristicUtil.charactersticTamanho());
        return new Department("Department characteristics",null,values);
    }

    @Transactional
    public ProductType getNewProductType(){
        Department department = new Department("Telefonia");
        department.setCategories(new HashSet<>());
        Category category = new Category("Telefonia movel",department);
        category.setProductTypes(new HashSet<>());
        department.getCategories().add(category);
        ProductType pt = new ProductType("Telefone",false);
        pt.setCategory(category);
        pt.setCharacteristics(new ArrayList<>());
        pt.setIsGrid(false);
        category.getProductTypes().add(pt);
        Department dep = pt.getCategory().getDepartment();
        departmentService.save(dep);

        return pt;
    }

    @Transactional
    public ProductType getGasProductType(){
        Department department = new Department("Liquído");
        department.setCategories(new HashSet<>());
        Category category = new Category("Inflamável",department);
        category.setProductTypes(new HashSet<>());
        department.getCategories().add(category);
        ProductType pt = new ProductType("Combustível",false);
        pt.setCategory(category);
        pt.setCharacteristics(new ArrayList<>());
        pt.setIsGrid(false);
        pt.setEspecification(ProductEspecification.GAS);
        category.getProductTypes().add(pt);
        Department dep = pt.getCategory().getDepartment();
        departmentService.save(dep);

        return pt;
    }
}
