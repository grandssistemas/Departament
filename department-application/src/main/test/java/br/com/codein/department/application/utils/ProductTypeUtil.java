package br.com.codein.department.application.utils;

import br.com.codein.buddycharacteristic.domain.characteristic.AssociativeCharacteristic;
import br.com.codein.department.domain.model.department.ProductType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rafael on 07/08/15.
 */
public class ProductTypeUtil {
    public static ProductType productType(){
        ProductType pt = new ProductType("ProductType",false);
        pt.setCategory(CategoryUtil.categoryWithDepartment());
        List<AssociativeCharacteristic> list = new ArrayList<>();
        list.add(AssociativeCharacteristicUtils.logic());
        list.add(AssociativeCharacteristicUtils.tamanho());
        pt.setCharacteristics(list);
        List<String> list1 = new ArrayList<>();
        list1.add("Tipo de Produto");
        list1.add("Logic");
        list1.add("Tamanho");
        pt.setNameMount(list1);
        pt.setIsGrid(false);
        return pt;
    }

}