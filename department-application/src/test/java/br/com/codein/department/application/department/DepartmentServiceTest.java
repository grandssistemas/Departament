package br.com.codein.department.application.department;

import br.com.codein.department.application.AbstractTest;
import br.com.codein.department.application.service.DepartmentService;
import br.com.codein.department.application.utils.CategoryUtil;
import br.com.codein.department.application.utils.DepartmentCrudUtil;
import br.com.codein.department.domain.model.department.Department;
import br.com.codein.department.domain.model.exception.ValidationException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Created by marcio on 18/09/17.
 */
public class DepartmentServiceTest extends AbstractTest {

    @Autowired
    private DepartmentService departmentService;

    Department department;

    @Before
    public void setUp(){
        department = DepartmentCrudUtil.departmentOnlyName();
    }

    @Test
    public void testValidateDuplicateNameDepartment(){
        assertNotNull(departmentService.save(department).getId());
        Department newDepartment = DepartmentCrudUtil.departmentOnlyName();
        try{
            departmentService.save(newDepartment);
            fail();
        }catch (Exception e){
            assertEquals(ValidationException.class, e.getClass());
            assertEquals("dep001;;Department already registered", e.getMessage());
        }
    }

}
