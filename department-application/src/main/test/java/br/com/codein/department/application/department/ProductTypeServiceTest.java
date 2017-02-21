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
import br.com.codein.department.domain.model.department.Category;
import br.com.codein.department.domain.model.department.ProductType;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.ArrayList;
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
        productType = new ProductType("ProductType", true, true);
        productType2 = new ProductType("ProductType2", true, true);
        productType3 = new ProductType("ProductType3", true, true);
        productType4 = new ProductType("ProductType4", true, true);
    }

    @Test
    @Transactional
    public void testIsGridValuesTypeRight(){
        productType.setCategory(categoryService.save(CategoryUtil.categoryWithDepartment()));
        productType.setCharacteristics(characList1);
        productType.setGridPattern("LOGICO;LOGICO");
        productType.setIsGrid(true);
        assertNotNull(productTypeService.save(productType).getId());
        productType2.setIsGrid(true);
        Category c = CategoryUtil.categoryWithDepartment();
        departmentService.save(c.getDepartment());
        categoryService.save(c);
        productType2.setCategory(c);
        productType2.setCharacteristics(characList2);
        productType2.setGridPattern("LOGICO;LOGICO");
        productTypeService.save(productType);
        try{
        }catch (RuntimeException e){
            assertEquals(e.getMessage(),"The grid characteristics values is not in the right quantity");
        }
    }

    @Test
    @Transactional
    public void testIsGridCharacteristicRight(){
        productType.setCategory(categoryService.save(CategoryUtil.categoryWithDepartment()));
        productType.setCharacteristics(characList1);
        productType.setIsGrid(true);
        productType.setGridPattern("LOGICO;LOGICO");
        assertNotNull(productTypeService.save(productType).getId());
        productType2.setIsGrid(true);
        productType2.setCategory(categoryService.save(CategoryUtil.categoryWithDepartment()));
        AssociativeCharacteristic tamanho = AssociativeCharacteristicUtils.tamanhoGrid1();
        tamanho.setCharacteristic(characteristicService.save(tamanho.getCharacteristic()));
        characList2.add(tamanho);
        productType2.setCharacteristics(characList2);
        productType2.setGridPattern("LOGICO;LOGICO");
        try{
            productTypeService.save(productType2);
        }catch (RuntimeException e){
            assertEquals(e.getMessage(),"The grid characteristics values types are not matching with the gridPattern");
        }
    }

    @Test
    @Transactional
    public void testIsGridPatternRight(){
        productType.setCategory(categoryService.save(CategoryUtil.categoryWithDepartment()));
        productType.setCharacteristics(characList1);
        productType.setGridPattern("LOGICO;LOGICO");
        productType.setIsGrid(true);
        assertNotNull(productTypeService.save(productType).getId());

        AssociativeCharacteristic characProdLogic1= AssociativeCharacteristicUtils.logicGrid2();
        characProdLogic1.setCharacteristic(characteristicService.save(characProdLogic1.getCharacteristic()));
        characList2.add(characProdLogic1);
        productType2.setIsGrid(true);
        productType2.setCategory(categoryService.save(CategoryUtil.categoryWithDepartment()));
        characList2.add(AssociativeCharacteristicUtils.tamanhoGrid1());
        productType2.setCharacteristics(characList2);
        productType2.setGridPattern("LOGICO;LOGICO");
        productType2.setIsGrid(false);
        try{
            productTypeService.save(productType);
        }catch (RuntimeException e){
            assertEquals(e.getMessage(),"The gridPattern is not right for the grid configuration in this product type");
        }

        productType3.setCategory(categoryService.save(CategoryUtil.categoryWithDepartment()));
        productType3.setCharacteristics(characList);
        productType3.setIsGrid(false);
        assertNotNull(productTypeService.save(productType3).getId());
        productType3.setCategory(categoryService.save(CategoryUtil.categoryWithDepartment()));
        characList2.add(AssociativeCharacteristicUtils.tamanhoGrid1());
        productType3.setCharacteristics(characList2);
        productType3.setIsGrid(true);
        try{
            productTypeService.save(productType);
        }catch (RuntimeException e){
            assertEquals(e.getMessage(),"The gridPattern is not right for the grid configuration in this product type");
        }
    }


    @Test
    @Transactional
    public void testgridCharacteristicCount(){
        productType.setCategory(categoryService.save(CategoryUtil.categoryWithDepartment()));
        productType.setCharacteristics(characList1);
        productType.setIsGrid(true);
        productType.setGridPattern("LOGICO;LOGICO");
        assertNotNull(productTypeService.save(productType).getId());
        productType2.setCategory(categoryService.save(CategoryUtil.categoryWithDepartment()));
        productType2.setIsGrid(true);
        characList2.add(AssociativeCharacteristicUtils.tamanhoGrid1());
        productType2.setCharacteristics(characList2);
        productType2.setGridPattern("LOGICO;LOGICO");
        try{
            productTypeService.save(productType);
        }catch (RuntimeException e){
            assertEquals(e.getMessage(),"This product type grid characteristic count is not right");
        }

        productType3.setCategory(categoryService.save(CategoryUtil.categoryWithDepartment()));
        productType3.setCharacteristics(characList);
        productType3.setIsGrid(false);
        assertNotNull(productTypeService.save(productType3).getId());
        productType3.setCategory(categoryService.save(CategoryUtil.categoryWithDepartment()));
        productType3.setCharacteristics(characList2);
        productType3.setIsGrid(false);
        try{
            productTypeService.save(productType);
        }catch (RuntimeException e){
            assertEquals(e.getMessage(),"This product type should not have grid characteristic");
        }
    }

    @Test
    @Transactional
    public void testIsPatternTypesCountRight(){
        productType.setCategory(categoryService.save(CategoryUtil.categoryWithDepartment()));
        productType.setCharacteristics(characList1);
        productType.setIsGrid(true);
        productType.setGridPattern("LOGICO;LOGICO");
        assertNotNull(productTypeService.save(productType).getId());
        productType2.setIsGrid(true);
        productType2.setCategory(categoryService.save(CategoryUtil.categoryWithDepartment()));
        characList2.add(AssociativeCharacteristicUtils.tamanhoGrid1());
        productType2.setCharacteristics(characList2);
        productType2.setGridPattern("LOGICO;LOGICO;TAMANHO");
        try{
            productTypeService.save(productType);
        }catch (RuntimeException e){
            assertEquals(e.getMessage(),"In ProductType patterns count isn't right");
        }
    }


    @Test
    @Transactional
    public void testIsPatternTypesRight(){
        productType.setCategory(categoryService.save(CategoryUtil.categoryWithDepartment()));
        productType.setCharacteristics(characList1);
        productType.setIsGrid(true);
        productType.setGridPattern("LOGICO;LOGICO");
        assertNotNull(productTypeService.save(productType).getId());
        productType2.setIsGrid(true);
        productType2.setCategory(categoryService.save(CategoryUtil.categoryWithDepartment()));
        characList2.add(AssociativeCharacteristicUtils.tamanhoGrid1());
        productType2.setCharacteristics(characList2);
        productType2.setGridPattern("LOGICO;TEXTO");
        try{
            productTypeService.save(productType);
        }catch (RuntimeException e){
            assertEquals(e.getMessage(),"In ProductType patterns types aren't right");
        }
    }

    @Test
    @Transactional
    public void testCheckCharacteristicContain(){
        productType.setCategory(categoryService.save(CategoryUtil.categoryWithDepartment()));
        productType.setCharacteristics(characList1);
        productType.setIsGrid(true);
        productType.setGridPattern("LOGICO;LOGICO");
        assertNotNull(productTypeService.save(productType).getId());
        productType2.setIsGrid(true);
        productType2.setCategory(categoryService.save(CategoryUtil.categoryWithDepartment()));
        characList2.add(AssociativeCharacteristicUtils.tamanhoGrid1());
        productType2.setCharacteristics(characList2);
        productType2.setGridPattern("LOGICO;TEXTO");
        try{
            productTypeService.save(productType);
        }catch (RuntimeException e){
            assertEquals(e.getMessage(),"The father characteristics are no contained in characteristics");
        }
    }
}