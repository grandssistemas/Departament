package br.com.codein.department.api.department;

import br.com.codein.department.application.service.ProductTypeService;
import br.com.codein.department.domain.model.department.ProductType;
import br.com.codein.department.domain.model.exception.ValidationException;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by gelatti on 21/02/17.
 */
@RestController
@RequestMapping("/api/producttype")
public class ProductTypeAPI extends GumgaAPI<ProductType, Long> implements CSVGeneratorAPI {

    @Autowired
    private GumgaTempFileService gumgaTempFileService;

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

    @Override
    public RestResponse<ProductType> delete(@PathVariable Long id) {
        try {
            return super.delete(id);
        } catch (Exception e) {
            throw new ValidationException("twd04;;That product type are already in use, cannot delete.");
        }
    }

    @Override
    public RestResponse<ProductType> save(@RequestBody @Valid ProductType model, BindingResult result) {
        model.setImage((GumgaImage) gumgaTempFileService.find(model.getImage().getName()));
        return super.save(model, result);
    }
}