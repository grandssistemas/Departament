package br.com.codein.department.api.department;

import br.com.codein.department.application.service.ProductTypeService;
import br.com.codein.department.domain.model.department.ProductType;
import io.gumga.annotations.GumgaSwagger;
import io.gumga.application.GumgaService;
import io.gumga.core.QueryObject;
import io.gumga.core.SearchResult;
import io.gumga.presentation.GumgaAPI;
import io.gumga.presentation.api.CSVGeneratorAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by gelatti on 21/02/17.
 */
@RestController
@RequestMapping("/api/producttype")
public class ProductTypeAPI extends GumgaAPI<ProductType, Long> implements CSVGeneratorAPI {

    @Autowired
    public ProductTypeAPI(GumgaService<ProductType, Long> service) {
        super(service);
    }

    @Override
    @GumgaSwagger
    public ProductType load(@PathVariable Long id) {
        return ((ProductTypeService)service).loadProductTypeFat(id);
    }

    @Override
    public GumgaService getGumgaService() {
        return (ProductTypeService) service;
    }


    @RequestMapping(value = "/all",method = RequestMethod.GET)
    public SearchResult<ProductType> getAll(){
        return ((ProductTypeService) service).getAll();
    }

    @RequestMapping(value = "/getactive", method = RequestMethod.GET)
    public SearchResult<ProductType> getAllActive (QueryObject query) {
        return ((ProductTypeService)service).pesquisa(query);
    }
}