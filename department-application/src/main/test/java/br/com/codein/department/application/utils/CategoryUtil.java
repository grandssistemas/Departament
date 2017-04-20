package br.com.codein.department.application.utils;


import br.com.codein.department.domain.model.department.Category;

/**
 * Created by rafael on 05/08/15.
 */
public class CategoryUtil {

    public static Category categoryWithDepartment(){
        return new Category("Category", DepartmentCrudUtil.departmentOnlyName());
    }
    public static Category category(){
        return new Category("Category");
    }
    public static Category categoryWithCategory(){
        return new Category("Category", category());
    }

}
