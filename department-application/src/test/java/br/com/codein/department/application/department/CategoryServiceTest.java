package br.com.codein.department.application.department;

import br.com.codein.department.application.AbstractTest;
import br.com.codein.department.application.service.CategoryService;
import br.com.codein.department.application.service.DepartmentService;
import br.com.codein.department.application.utils.CategoryUtil;
import br.com.codein.department.application.utils.DepartmentCrudUtil;
import br.com.codein.department.domain.model.department.Category;
import br.com.codein.department.domain.model.department.Department;
import br.com.codein.department.domain.model.exception.ValidationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Created by rafael on 10/07/15.
 */
public class CategoryServiceTest extends AbstractTest {


    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DepartmentService departmentService;

    Department department;
    Category categoryWithDep;
    Category categoryWithCat;

    @Before
    @Transactional
    public void setUp(){
        department = DepartmentCrudUtil.departmentWithCharacs();
        categoryWithDep = CategoryUtil.categoryWithDepartment();
        categoryWithCat = CategoryUtil.categoryWithCategory();
    }

    @Test
    @Transactional
    public void testHaveFatherDep(){
        assertNotNull(categoryService.save(categoryWithDep).getId());
        categoryWithDep.setDepartment(null);
        try{
            categoryService.save(categoryWithDep);
            fail();
        }catch (Exception e){
            assertEquals(ValidationException.class, e.getClass());
            assertEquals("Category should have a father", e.getMessage());
        }
    }
    @Test
    @Transactional
    public void testHaveFatherCat(){
        assertNotNull(categoryService.save(categoryWithCat).getId());
        categoryWithCat.setCategory(null);
        try{
            categoryService.save(categoryWithCat);
            fail();
        }catch (Exception e){
            assertEquals(ValidationException.class, e.getClass());
            assertEquals("Category should have a father", e.getMessage());
        }
    }

    @Test
    @Transactional
    public void testFatherCountIsRight(){
        assertNotNull(categoryService.save(categoryWithDep).getId());
        categoryWithDep.setCategory(CategoryUtil.category());
        try{
            categoryService.save(categoryWithDep);
            fail();
        }catch (Exception e){
            assertEquals(ValidationException.class, e.getClass());
            assertEquals("Category father quantity is not right", e.getMessage());
        }
    }

    @Test
    public void testCheckCharacteristicValid(){
        categoryWithDep.setDepartment(department);
        categoryWithDep.setCharacteristics(department.getCharacteristics());
        assertNotNull(categoryService.save(categoryWithDep).getId());
        categoryWithDep.setCharacteristics(new HashSet<>());
        try{
            categoryService.save(categoryWithDep);
            fail();
        }catch (Exception e){
            assertEquals(ValidationException.class, e.getClass());
            assertEquals("Category should have father's characteristics", e.getMessage());
        }
    }

    @Test
    public void testValidateDuplicateNameCategory(){
        Department departmentOnlyName = DepartmentCrudUtil.departmentOnlyName();
        departmentService.save(departmentOnlyName);
        categoryWithDep.setDepartment(departmentOnlyName);
        assertNotNull(categoryService.save(categoryWithDep).getId());
        Category newCategory = new Category("Category", departmentOnlyName);

        try{
            categoryService.save(newCategory);
            fail();
        }catch (Exception e){
            assertEquals(ValidationException.class, e.getClass());
            assertEquals("cat001;;Category already registered", e.getMessage());
        }
    }

}