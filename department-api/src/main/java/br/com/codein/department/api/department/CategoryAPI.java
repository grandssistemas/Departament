package br.com.codein.department.api.department;

import br.com.codein.department.application.service.CategoryService;
import br.com.codein.department.application.service.DepartmentService;
import br.com.codein.department.application.service.ProductTypeService;
import br.com.codein.department.domain.model.department.Category;
import br.com.codein.department.domain.model.department.Department;
import br.com.codein.department.domain.model.department.ProductType;
import br.com.codein.department.domain.model.exception.ParamWrongException;
import br.com.codein.department.gateway.dto.department.CategoryDTO;
import br.com.codein.department.gateway.dto.department.CategoryType;
import br.com.codein.department.gateway.translator.CategoryTranslator;
import io.gumga.annotations.GumgaSwagger;
import io.gumga.application.GumgaService;
import io.gumga.core.QueryObject;
import io.gumga.core.SearchResult;
import io.gumga.presentation.GumgaAPI;
import io.gumga.presentation.RestResponse;
import io.gumga.presentation.api.CSVGeneratorAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gelatti on 21/02/17.
 */
@RestController
@RequestMapping("/api/category")
public class CategoryAPI extends GumgaAPI<Category, Long> implements CSVGeneratorAPI {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private ProductTypeService productTypeService;


    @Autowired
    private CategoryTranslator translator;

    @Autowired
    public CategoryAPI(GumgaService<Category, Long> service) {
        super(service);
    }

    @Override
    @GumgaSwagger
    public Category load(@PathVariable Long id) {
        return ((CategoryService)service).loadCategoriaFat(id);
    }

    @RequestMapping(value = "/searchfather",  method = RequestMethod.GET)
    public ResponseEntity searchFather(String param){

        return categoryService.getFatherOptions(param);
    }

    @Transactional
    @RequestMapping(value = "/tree/save", method = RequestMethod.POST, consumes = "application/json")
    @GumgaSwagger
    public RestResponse<CategoryDTO> save(@RequestBody CategoryDTO arvore) {

        switch (arvore.categoryType){
            case DEPARTMENT:
                Department department = (Department) translator.to(arvore, null);
                departmentService.save(department);
                break;
            case CATEGORY:
                categoryService.save((Category) translator.to(arvore, null));
                break;
            case PRODUCTTYPE:
                productTypeService.save((ProductType) translator.to(arvore, null));
                break;
        }
        return new RestResponse<>(arvore, "Sucesso");
    }

    @Transactional
    @RequestMapping(value = "/editable", method = RequestMethod.GET, consumes = "application/json")
    public RestResponse<Boolean> editable(@RequestBody CategoryDTO nodo) {
        Boolean toReturn = false;
        switch (nodo.categoryType){
            case DEPARTMENT:
                toReturn = departmentService.isEditable(nodo.id);
                break;
            case CATEGORY:
                categoryService.save((Category) translator.to(nodo, null));
                break;
            case PRODUCTTYPE:
                productTypeService.save((ProductType) translator.to(nodo, null));
                break;
        }
        return new RestResponse<>(toReturn, "Sucesso");
    }

    @Transactional
    @RequestMapping(value = "/tree/{id}", method = RequestMethod.GET)
    public RestResponse<CategoryDTO> loadFork(@PathVariable("id") Long id) {
        return new RestResponse<>(translator.fork(productTypeService.loadProductTypeFat(id)), "Sucesso");
    }

    @Transactional
    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public RestResponse<List<CategoryDTO>> loadTree() {
        //TODO ARRUMAR PARA TRAZER TODOS OS DEPARTAMENTOS
        CategoryDTO dto;
        SearchResult<Department> treeFathers = departmentService.getAll();
        List<CategoryDTO> dtos = new ArrayList<>();
        List<Department> departmentList = treeFathers.getValues();
        departmentService.initializeCharacteristics(departmentList);
        for(Department department:departmentList){
            dto = translator.from(department);
            dto.editable = departmentService.isEditable(department.getId());
            dto.childrens = null;
            dtos.add(dto);
        }
        return new RestResponse<>(dtos, "Sucesso");
    }

    @Transactional
    @RequestMapping(value = "/tree/childrens/{type}/{id}", method = RequestMethod.GET)
    public RestResponse<List<CategoryDTO>> loadChildrens(@PathVariable("id") Long id, @PathVariable("type")CategoryType type) {
        List<CategoryDTO> dto = new ArrayList<>();
        switch (type) {
            case DEPARTMENT:
                Department department = departmentService.loadDepartmentFatWithCategories(id);
                dto = translator.getChildrens(department);
                for(CategoryDTO ct:dto){
                    if(ct.categoryType == CategoryType.CATEGORY){
                        ct.editable = categoryService.isEditable(ct.id);
                    }else{
                        ct.editable = productTypeService.isEditable(ct.id);
                    }
                }
                break;
            case CATEGORY:
                Category category = categoryService.loadCategoryFatWithCategories(id);
                dto = translator.getChildrens(category);
                for(CategoryDTO ct:dto){
                    if(ct.categoryType == CategoryType.CATEGORY){
                        ct.editable = categoryService.isEditable(ct.id);
                    }else{
                        ct.editable = productTypeService.isEditable(ct.id);
                    }
                }
                break;
        }
        return new RestResponse<>(dto, "Sucesso");
    }

    @Override
    @GumgaSwagger
    public RestResponse<Category> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    public GumgaService getGumgaService() {
        return (CategoryService) service;
    }

    @Transactional
    @RequestMapping(value = "/tree/level/{type}", method = RequestMethod.GET)
    public RestResponse<List<CategoryDTO>> loadLevel(@PathVariable("type")CategoryType type) {
        List<CategoryDTO> dto = new ArrayList<>();
        switch (type) {
            case CATEGORY:
                List<Department> toConvertDep = departmentService.loadAllWithFirstChildrens();
                for(Department dep: toConvertDep){
                    dto.add(translator.getWithLazyChildrens(dep));
                }
                break;
            case PRODUCTTYPE:
                List<Category> toConvertCat = categoryService.loadAllWithFirstChildrens();
                for(Category cat: toConvertCat){
                    dto.add(translator.getWithLazyChildrens(cat));
                }
                break;
            default:
                throw new ParamWrongException("twd01;;CategoryType 'DEPARTMENT' can not be use to search here");
        }
        return new RestResponse<>(dto, "Sucesso");
    }

    @Transactional
    @RequestMapping(value = "/tree/fatherlevel/{type}/{id}", method = RequestMethod.GET)
    public RestResponse<CategoryDTO> loadFatherLevel(@PathVariable("type")CategoryType type, @PathVariable("id") Long id) {
        CategoryDTO dto;
        switch (type) {
            case CATEGORY:
                Department dep = departmentService.getFatherWithChildrens(id);
                dto = translator.getWithLazyChildrens(dep);
                break;
            case PRODUCTTYPE:
                Category cat = categoryService.getFatherWithChildrens(id);
                dto = translator.getWithLazyChildrens(cat);
                break;
            default:
                throw new ParamWrongException("twd01;;CategoryType 'DEPARTMENT' can not be use to search here");
        }
        return new RestResponse<>(dto, "Sucesso");
    }
}
