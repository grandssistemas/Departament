package br.com.codein.department.application.department;

import br.com.codein.department.application.AbstractTest;
import br.com.codein.department.application.service.DepartmentService;
import br.com.codein.department.application.utils.DepartmentCrudUtil;
import br.com.codein.department.domain.model.department.Department;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by rafael on 14/07/15.
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
    public void validIsPatternTypeCountIsRight(){
        department.setPatterns("TAMANHO;COR");
        department.setCharacteristics(new HashSet<>());
        department.setCategories(new HashSet<>());
        assertNotNull(departmentService.save(department).getId());
        department.setPatterns("TAMANHO");
        try{
            departmentService.save(department);
        }catch (RuntimeException e){
            assertEquals(e.getMessage(), "In Department patterns count isn't right");
        }
        department.setPatterns(null);
        assertNotNull(departmentService.save(department));
    }

    @Test
    public void validIsPatternTypeIsRight(){
        department.setPatterns("TAMANHO;COR");
        assertNotNull(departmentService.save(department).getId());
        department.setPatterns("TAMANHO;Aasdasd");
        try{
            departmentService.save(department);
        }catch (RuntimeException e){
            assertEquals(e.getMessage(),"In Department patterns types aren't right");
        }
    }

}