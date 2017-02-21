package br.com.codein.department.api.department;

import br.com.codein.department.application.service.DepartmentService;
import br.com.codein.department.domain.model.department.Department;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import io.gumga.annotations.GumgaSwagger;
import io.gumga.application.GumgaService;
import io.gumga.core.QueryObject;
import io.gumga.core.SearchResult;
import io.gumga.domain.GumgaObjectAndRevision;
import io.gumga.presentation.GumgaAPI;
import io.gumga.presentation.api.CSVGeneratorAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by gelatti on 21/02/17.
 */
@Api(value = "/department", description = "Operation about departments")
@RestController
@RequestMapping("/api/department")
public class DepartmentAPI extends GumgaAPI<Department, Long> implements CSVGeneratorAPI {

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
}
