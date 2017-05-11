package br.com.codein.department.api.department;

import br.com.codein.department.application.service.DepartmentService;
import br.com.codein.department.domain.model.department.Department;
import br.com.codein.department.domain.model.exception.ValidationException;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import io.gumga.annotations.GumgaSwagger;
import io.gumga.application.GumgaService;
import io.gumga.application.GumgaTempFileService;
import io.gumga.core.QueryObject;
import io.gumga.core.SearchResult;
import io.gumga.domain.GumgaObjectAndRevision;
import io.gumga.domain.domains.GumgaImage;
import io.gumga.presentation.GumgaAPI;
import io.gumga.presentation.RestResponse;
import io.gumga.presentation.api.CSVGeneratorAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by gelatti on 21/02/17.
 */
@Api(value = "/department", description = "Operation about departments")
@RestController
@RequestMapping("/api/department")
public class DepartmentAPI extends GumgaAPI<Department, Long> implements CSVGeneratorAPI {

    @Autowired
    private GumgaTempFileService gumgaTempFileService;

    @Autowired
    public DepartmentAPI(GumgaService<Department, Long> service) {
        super(service);
    }

    @Override
//    @ApiImplicitParams(@ApiImplicitParam(dataType = "file", name = "avatar", paramType = "body"))
    @ApiOperation(value = "Pesquisa de departamentos",notes = "Retorna a lista departamentos conforme a query fornecida")
    @RequestMapping(method = RequestMethod.GET)
    public SearchResult<Department> pesquisa(QueryObject query) {
        return super.pesquisa(query);
    }

    @Override
    @ApiOperation(value = "Listagem das versões antigas")
    @RequestMapping(value = "listoldversions/{id}", method = RequestMethod.GET)
    public List<GumgaObjectAndRevision> listOldVersions(@PathVariable Long id) {
        return super.listOldVersions(id);
    }

    @Override
    @Transactional
    @ApiOperation(value = "Obtem por id")
    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    @GumgaSwagger
    public Department load(@PathVariable Long id) {
        return ((DepartmentService) service).loadDepartmentFat(id);
    }

    @Override
    @ApiOperation(value = "Obtem estado inicial da classe")
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public Department initialState() {
        return super.initialState();
    }

    @Override
    public GumgaService getGumgaService() {
        return ((DepartmentService) service);
    }

    @RequestMapping(value = "/all",method = RequestMethod.GET)
    public SearchResult<Department> getAll(){
        return ((DepartmentService) service).getAll();
    }

    @RequestMapping(value = "/array", method = RequestMethod.POST)
    public List<Department> saveArray(@RequestBody List<Department> departments) {
        return ((DepartmentService)service).saveArray(departments);
    }

    @Override
    public RestResponse<Department> delete(@PathVariable Long id) {
        try {
            return super.delete(id);
        } catch (Exception e) {
            throw new ValidationException("twd03;;That department are already in use, cannot delete.");
        }
    }

    @Override
    public RestResponse<Department> save(@RequestBody @Valid Department model, BindingResult result) {
        return super.save(model, result);
    }
}
