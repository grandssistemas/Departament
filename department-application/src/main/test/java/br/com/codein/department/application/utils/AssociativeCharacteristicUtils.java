package br.com.codein.department.application.utils;


import br.com.codein.buddycharacteristic.domain.characteristic.AssociativeCharacteristic;

/**
 * Created by rafael on 05/08/15.
 */
public class AssociativeCharacteristicUtils {

    public static AssociativeCharacteristic logicGrid1(){
        return new AssociativeCharacteristic(CharacteristicUtil.characteristicLogic(),1);
    }

    public static AssociativeCharacteristic logicGrid2(){
        return new AssociativeCharacteristic(CharacteristicUtil.characteristicLogic(),2);
    }

    public static AssociativeCharacteristic tamanhoGrid1(){
        return new AssociativeCharacteristic(CharacteristicUtil.characteristicLogic(),1);
    }

    public static AssociativeCharacteristic textoGrade(){
        return new AssociativeCharacteristic(CharacteristicUtil.characteristicText(),1);
    }

    public static AssociativeCharacteristic logic(){
        return new AssociativeCharacteristic(CharacteristicUtil.characteristicLogic(),0);
    }

    public static AssociativeCharacteristic tamanho(){
        return new AssociativeCharacteristic(CharacteristicUtil.charactersticTamanho(),0);
    }

    public static AssociativeCharacteristic tamanhoGrid2(){
        return new AssociativeCharacteristic(CharacteristicUtil.characteristicLogic(),2);
    }
    public static AssociativeCharacteristic tamanhoGrid3(){
        return new AssociativeCharacteristic(CharacteristicUtil.characteristicLogic(),3);
    }

}
