package br.com.codein.department.application.department;

import br.com.codein.buddycharacteristic.application.service.characteristic.AssociativeCharacteristicService;
import br.com.codein.buddycharacteristic.application.service.characteristic.CharacteristicService;
import br.com.codein.buddycharacteristic.domain.characteristic.AssociativeCharacteristic;
import br.com.codein.department.application.AbstractTest;
import br.com.codein.department.application.service.CategoryService;
import br.com.codein.department.application.service.DepartmentService;
import br.com.codein.department.application.service.ProductTypeService;
import br.com.codein.department.application.utils.AssociativeCharacteristicUtils;
import br.com.codein.department.application.utils.CategoryUtil;
import br.com.codein.department.application.utils.ProductTypeUtil;
import br.com.codein.department.domain.model.department.Category;
import br.com.codein.department.domain.model.department.ProductType;
import br.com.codein.department.domain.model.exception.ValidationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by rafael on 14/07/15.
 */
public class ProductTypeServiceTest extends AbstractTest {

    @Autowired
    private ProductTypeService productTypeService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private CharacteristicService characteristicService;
    @Autowired
    private AssociativeCharacteristicService associativeCharacteristicService;

    ProductType productType;
    ProductType productType2;
    ProductType productType3;
    ProductType productType4;

    List<AssociativeCharacteristic> characList;
    List<AssociativeCharacteristic> characList1;
    List<AssociativeCharacteristic> characList2;

    @Before
    @Transactional
    public void setUp(){
        AssociativeCharacteristic characProdLogic = AssociativeCharacteristicUtils.logicGrid1();
        characteristicService.save(characProdLogic.getCharacteristic());
        associativeCharacteristicService.save(characProdLogic);

        AssociativeCharacteristic characProdLogic1= AssociativeCharacteristicUtils.logicGrid2();
        characteristicService.save(characProdLogic1.getCharacteristic());
        associativeCharacteristicService.save(characProdLogic1);

        AssociativeCharacteristic characProdLogic2 = AssociativeCharacteristicUtils.logicGrid2();
        characteristicService.save(characProdLogic2.getCharacteristic());
        associativeCharacteristicService.save(characProdLogic2);

        characList = new ArrayList<>();

        characList1 = new ArrayList<>();
        characList1.add(characProdLogic);
        characList1.add(characProdLogic1);

        characList2 = new ArrayList<>();
        characList2.add(characProdLogic2);
        productType = new ProductType("ProductType", true);
        productType2 = new ProductType("ProductType2", true);
        productType3 = new ProductType("ProductType3", true);
        productType4 = new ProductType("ProductType4", true);
    }

    @Test
    @Transactional
    public void testIsGridCharacteriticCountRight(){
        Category category = CategoryUtil.categoryWithDepartment();
        assertNotNull(departmentService.save(category.getDepartment()).getId());
        assertNotNull(categoryService.save(category).getId());

        productType.setCategory(category);
        productType.setCharacteristics(characList1);
        productType.setIsGrid(true);
        assertNotNull(productTypeService.save(productType).getId());
        productType2.setIsGrid(true);
        productType2.setCategory(category);
        productType2.setCharacteristics(characList2);
        try{
            productTypeService.save(productType2);
            Assert.fail();
        }catch (Exception e){
            assertEquals(ValidationException.class, e.getClass());
            assertEquals("characvalue02;;The grid characteristics values is not in the right quantity", e.getMessage());
        }
    }

    @Test
    @Transactional
    public void testIsGridCharacteristicRight(){
        Category category = CategoryUtil.categoryWithDepartment();

        assertNotNull(departmentService.save(category.getDepartment()).getId());
        assertNotNull(categoryService.save(category).getId());

        productType.setCategory(category);
        productType.setCharacteristics(characList1);
        productType.setIsGrid(true);
        assertNotNull(productTypeService.save(productType).getId());
        productType2.setIsGrid(true);
        productType2.setCategory(category);
        AssociativeCharacteristic tamanho = AssociativeCharacteristicUtils.textoGrade();
        tamanho.setCharacteristic(characteristicService.save(tamanho.getCharacteristic()));
        characList2.add(tamanho);
        productType2.setCharacteristics(characList2);
        try{
            productTypeService.save(productType2);
            Assert.fail();
        }catch (Exception e){
            assertEquals(ValidationException.class, e.getClass());
            assertEquals("The grid characteristics values types are not matching the valid grid type values", e.getMessage());
        }
    }


    @Test
    @Transactional
    public void testgridCharacteristicCount(){
        Category category = CategoryUtil.categoryWithDepartment();

        assertNotNull(departmentService.save(category.getDepartment()).getId());
        assertNotNull(categoryService.save(category).getId());

        productType.setCategory(category);
        productType.setCharacteristics(characList1);
        productType.setIsGrid(true);
        assertNotNull(productTypeService.save(productType).getId());
        productType2.setCategory(category);
        productType2.setIsGrid(true);
        characList2.add(AssociativeCharacteristicUtils.tamanhoGrid1());
        characList2.add(AssociativeCharacteristicUtils.tamanhoGrid3());
        productType2.setCharacteristics(characList2);
        try{
            productTypeService.save(productType2);
            Assert.fail();
        }catch (Exception e){
            assertEquals(ValidationException.class, e.getClass());
            assertEquals("This product type grid characteristic count is not right", e.getMessage());
        }
    }

    @Test
    public void testShouldNotExistCharacteristic(){
        Category category = CategoryUtil.categoryWithDepartment();

        assertNotNull(departmentService.save(category.getDepartment()).getId());
        assertNotNull(categoryService.save(category).getId());

        productType3.setCategory(category);
        productType3.setCharacteristics(characList);
        productType3.setIsGrid(false);
        assertNotNull(productTypeService.save(productType3).getId());
        productType3.setCharacteristics(characList2);
        productType3.setIsGrid(false);
        try{
            productTypeService.save(productType3);
            Assert.fail();
        }catch (Exception e){
            assertEquals(ValidationException.class, e.getClass());
            assertEquals("This product type should not have grid characteristic", e.getMessage());
        }
    }

    @Test
    @Transactional
    public void testCheckCharacteristicContain(){
        Category category = CategoryUtil.categoryWithDepartment();

        assertNotNull(departmentService.save(category.getDepartment()).getId());
        assertNotNull(categoryService.save(category).getId());

        productType.setCategory(category);
        productType.setCharacteristics(characList1);
        productType.setIsGrid(true);
        assertNotNull(productTypeService.save(productType).getId());
        characList2.add(AssociativeCharacteristicUtils.tamanhoGrid1());
        productType.getCategory().setCharacteristics(new HashSet<>());
        productType.getCategory().getCharacteristics().add(characList2.get(0).getCharacteristic());

        try{
            productTypeService.save(productType);
            Assert.fail();
        }catch (Exception e){
            assertEquals(ValidationException.class, e.getClass());
            assertEquals("The father characteristics are no contained in characteristics", e.getMessage());
        }
    }

    @Test
    public void testValidateDuplicateNameProductType(){
        ProductType productType = ProductTypeUtil.productType();
        departmentService.save(productType.getCategory().getDepartment());
        categoryService.save(productType.getCategory());
        assertNotNull(productTypeService.save(productType).getId());
        ProductType newProductType = new ProductType("ProductType");
        newProductType.setCategory(productType.getCategory());
        try{
            productTypeService.save(newProductType);
            Assert.fail();
        }catch (Exception e){
            assertEquals(ValidationException.class, e.getClass());
            assertEquals("prodtype001;;ProductType already registered", e.getMessage());
        }
    }

}