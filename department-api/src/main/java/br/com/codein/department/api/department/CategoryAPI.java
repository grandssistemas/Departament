package br.com.codein.department.api.department;

import br.com.codein.department.application.service.CategoryService;
import br.com.codein.department.application.service.DepartmentService;
import br.com.codein.department.application.service.ProductTypeService;
import br.com.codein.department.domain.model.department.Category;
import br.com.codein.department.domain.model.department.Department;
import br.com.codein.department.domain.model.department.ProductType;
import br.com.codein.department.domain.model.exception.ParamWrongException;
import br.com.codein.department.domain.model.exception.ValidationException;
import br.com.codein.department.gateway.dto.department.CategoryDTO;
import br.com.codein.department.gateway.dto.department.CategoryType;
import br.com.codein.department.gateway.dto.department.ProductEspecificationDTO;
import br.com.codein.department.gateway.dto.department.VariationTypeDTO;
import br.com.codein.department.gateway.translator.CategoryTranslator;
import io.gumga.annotations.GumgaSwagger;
import io.gumga.application.GumgaService;
import io.gumga.application.GumgaTempFileService;
import io.gumga.core.QueryObject;
import io.gumga.core.SearchResult;
import io.gumga.domain.domains.GumgaImage;
import io.gumga.presentation.GumgaAPI;
import io.gumga.presentation.RestResponse;
import io.gumga.presentation.api.CSVGeneratorAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private GumgaTempFileService gumgaTempFileService;


    @Autowired
    private CategoryTranslator translator;

    @Autowired
    public CategoryAPI(GumgaService<Category, Long> service) {
        super(service);
    }

    @Override
    @GumgaSwagger
    @Transactional(readOnly = true)
    public Category load(@PathVariable Long id) {
        return ((CategoryService)service).loadCategoriaFat(id);
    }

    @Transactional(readOnly = true)
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
    @RequestMapping(value = "/tree/saveall", method = RequestMethod.POST, consumes = "application/json")
    @GumgaSwagger
    public RestResponse<List<CategoryDTO>> saveAll(@RequestBody List<CategoryDTO> arvore) {
        for (CategoryDTO category : arvore){
            switch (category.categoryType){
                case DEPARTMENT:
                    Department department = (Department) translator.to(category, null);
                    departmentService.save(department);
                    break;
                case CATEGORY:
                    categoryService.save((Category) translator.to(category, null));
                    break;
                case PRODUCTTYPE:
                    productTypeService.save((ProductType) translator.to(category, null));
                    break;
            }
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

    @Transactional(readOnly = true)
    @RequestMapping(value = "/tree/{id}", method = RequestMethod.GET)
    public RestResponse<CategoryDTO> loadFork(@PathVariable("id") Long id) {
        return new RestResponse<>(translator.fork(productTypeService.loadProductTypeFat(id)), "Sucesso");
    }

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
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
    @Transactional
    public RestResponse<Category> delete(@PathVariable Long id) {
        try {
            return super.delete(id);
        } catch (Exception e) {
            throw new ValidationException("twd02;;That category are already in uses, cannot delete.");
        }
    }

    @Override
    public GumgaService getGumgaService() {
        return (CategoryService) service;
    }

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
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

    @Override
    public RestResponse<Category> save(@RequestBody @Valid Category model, BindingResult result) {
        return super.save(model, result);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/image")
    public String logoUpload(@RequestParam MultipartFile image) throws IOException {
        GumgaImage gi = new GumgaImage();
        gi.setBytes(image.getBytes());
        gi.setMimeType(image.getContentType());
        gi.setName(image.getName());
        gi.setSize(image.getSize());
        return gumgaTempFileService.create(gi);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/image/{fileName}")
    public String logoDelete(@PathVariable String fileName) {
        return gumgaTempFileService.delete(fileName);
    }

    @Transactional(readOnly = true)
    @RequestMapping(method = RequestMethod.GET, value = "/image/{fileName}")
    public GumgaImage logoGet(@PathVariable(value = "fileName") String fileName) {
        return (GumgaImage) gumgaTempFileService.find(fileName);
    }

    @RequestMapping(value = "/variationtype", method = RequestMethod.GET)
    public RestResponse<List<VariationTypeDTO>> getVariationType() {
        List<VariationTypeDTO> typeDTOS = VariationTypeDTO.getValues();
        return new RestResponse<>(typeDTOS,"sucesso");
    }

    @RequestMapping(value = "/productespecification", method = RequestMethod.GET)
    public RestResponse<List<ProductEspecificationDTO>> getProductEspecification() {
        List<ProductEspecificationDTO> especificationDTOS = ProductEspecificationDTO.getValues();
        return new RestResponse<>(especificationDTOS,"sucesso");
    }

    @Transactional(readOnly = true)
    @RequestMapping(value = "/search/{type}/{name}", method = RequestMethod.GET)
    public RestResponse<List<CategoryDTO>> searchByNameByType(@PathVariable("type") CategoryType type, @PathVariable("name") String name){
        List<CategoryDTO> result = new ArrayList<>();
        QueryObject qo = new QueryObject();
        qo.setAq(String.format("lower(obj.name) = lower(\'%s\')",name));
        switch (type) {
            case DEPARTMENT:
                List<Department> dep = departmentService.pesquisa(qo).getValues();
                result = dep.stream().map(department -> translator.from(department)).collect(Collectors.toList());
                break;
            case CATEGORY:
                List<Category> cat = categoryService.pesquisa(qo).getValues();
                result = cat.stream().map(category -> translator.from(category)).collect(Collectors.toList());
                break;
            case PRODUCTTYPE:
                List<ProductType> pt  = productTypeService.pesquisa(qo).getValues();
                result = pt.stream().map(productType -> translator.from(productType)).collect(Collectors.toList());
                break;
        }
        return new RestResponse<>(result, "Sucesso");
    }

    @Transactional(readOnly = true)
    @RequestMapping(value = "/searchid/{type}/{id}", method = RequestMethod.GET)
    public RestResponse<List<CategoryDTO>> searchByIdByType(@PathVariable("type") CategoryType type, @PathVariable("id") String id){
        List<CategoryDTO> result = new ArrayList<>();
        QueryObject qo = new QueryObject();
        qo.setAq(String.format("obj.id = %s",id));
        switch (type) {
            case DEPARTMENT:
                List<Department> dep = departmentService.pesquisa(qo).getValues();
                result = dep.stream().map(department -> translator.from(department)).collect(Collectors.toList());
                break;
            case CATEGORY:
                List<Category> cat = categoryService.pesquisa(qo).getValues();
                result = cat.stream().map(category -> translator.from(category)).collect(Collectors.toList());
                break;
            case PRODUCTTYPE:
                List<ProductType> pt  = productTypeService.pesquisa(qo).getValues();
                result = pt.stream().map(productType -> translator.from(productType)).collect(Collectors.toList());
                break;
        }
        return new RestResponse<>(result, "Sucesso");
    }
}
