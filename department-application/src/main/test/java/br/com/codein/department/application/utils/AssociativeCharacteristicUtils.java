package br.com.codein.department.application.utils;


import br.com.codein.buddycharacteristic.domain.characteristic.AssociativeCharacteristic;

/**
 * Created by rafael on 05/08/15.
 */
public class AssociativeCharacteristicUtils {

    public static AssociativeCharacteristic logicGrid1(){
        return new AssociativeCharacteristic(false, CharacteristicUtil.characteristicLogic(),true,1);
    }

    public static AssociativeCharacteristic logicGrid2(){
        return new AssociativeCharacteristic(false, CharacteristicUtil.characteristicLogic(),true,2);
    }

    public static AssociativeCharacteristic tamanhoGrid1(){
        return new AssociativeCharacteristic(false, CharacteristicUtil.characteristicLogic(),true,1);
    }

    public static AssociativeCharacteristic logic(){
        return new AssociativeCharacteristic(false, CharacteristicUtil.characteristicLogic(),false,0);
    }

    public static AssociativeCharacteristic tamanho(){
        return new AssociativeCharacteristic(false, CharacteristicUtil.charactersticTamanho(),false,0);
    }

    public static AssociativeCharacteristic tamanhoGrid2(){
        return new AssociativeCharacteristic(false, CharacteristicUtil.characteristicLogic(),true,2);
    }

}
