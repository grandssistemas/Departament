package br.com.codein.department.application.department;

import br.com.codein.department.application.AbstractTest;
import br.com.codein.department.application.service.CategoryService;
import br.com.codein.department.application.utils.CategoryUtil;
import br.com.codein.department.application.utils.DepartmentCrudUtil;
import br.com.codein.department.domain.model.department.Category;
import br.com.codein.department.domain.model.department.Department;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by rafael on 10/07/15.
 */
public class CategoryServiceTest extends AbstractTest {


    @Autowired
    private CategoryService categoryService;

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
    public void testHaveFather(){
        assertNotNull(categoryService.save(categoryWithDep).getId());
        categoryWithDep.setDepartment(null);
        try{
            categoryService.save(categoryWithDep);
        }catch (RuntimeException e){
            assertEquals(e.getMessage(),"Category should have a father");
        }

        assertNotNull(categoryService.save(categoryWithCat).getId());
        categoryWithCat.setCategory(null);
        try{
            categoryService.save(categoryWithCat);
        }catch (RuntimeException e){
            assertEquals(e.getMessage(),"Category should have a father");
        }
    }

    @Test
    @Transactional
    public void testFatherCountIsRight(){
        assertNotNull(categoryService.save(categoryWithDep).getId());
        categoryWithDep.setCategory(CategoryUtil.category());
        try{
            categoryService.save(categoryWithDep);
        }catch (RuntimeException e){
            assertEquals(e.getMessage(),"Category father quantity is not right");
        }
    }

    @Test
    @Transactional
    public void testCheckCharacteristicValid(){
        categoryWithDep.setDepartment(department);
        categoryWithDep.setCharacteristics(department.getCharacteristics());
        assertNotNull(categoryService.save(categoryWithDep).getId());
        categoryWithDep.setCharacteristics(new HashSet<>());
        try{
            categoryService.save(categoryWithDep);
        }catch (RuntimeException e){
            assertEquals(e.getMessage(),"Category should have father's characteristics");
        }
    }
}