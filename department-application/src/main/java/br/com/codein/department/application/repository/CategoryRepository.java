package br.com.codein.department.application.repository;

import br.com.codein.department.domain.model.department.Category;
import io.gumga.domain.repository.GumgaCrudRepository;

/**
 * Created by gelatti on 21/02/17.
 */
public interface CategoryRepository extends GumgaCrudRepository<Category, Long> {

}