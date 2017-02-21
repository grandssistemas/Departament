package br.com.codein.department.application.utils;

import br.com.codein.buddycharacteristic.domain.characteristic.Characteristic;
import br.com.codein.buddycharacteristic.domain.characteristic.OptionValueCharacteristic;
import br.com.codein.buddycharacteristic.domain.characteristic.enums.CharacteristicOrigin;
import br.com.codein.buddycharacteristic.domain.characteristic.enums.ValueTypeCharacteristic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rafael on 05/08/15.
 */
public class CharacteristicUtil {
    public static Characteristic characteristicLogic(){
        return new Characteristic("Logic", ValueTypeCharacteristic.LOGICO, CharacteristicOrigin.PRODUCT);
    }

    public static Characteristic characteristicText(){
        return new Characteristic("Text", ValueTypeCharacteristic.TEXTO, CharacteristicOrigin.PRODUCT);
    }

    public static Characteristic characteristicSelectionWithValues(){
        return new Characteristic("Selection", ValueTypeCharacteristic.SELECAO, charactersticValues(), CharacteristicOrigin.PRODUCT);
    }

    public static Characteristic characteristicSelection(){
        return new Characteristic("Selection", ValueTypeCharacteristic.SELECAO, CharacteristicOrigin.PRODUCT);
    }

    public static Characteristic characteristic(){
        return new Characteristic("Selection", ValueTypeCharacteristic.SELECAO, CharacteristicOrigin.PRODUCT);
    }

    public static List<OptionValueCharacteristic> charactersticValues(){
        List<OptionValueCharacteristic> values = new ArrayList<>();

        values.add(new OptionValueCharacteristic("Value"));
        values.add(new OptionValueCharacteristic("Value"));
        values.add(new OptionValueCharacteristic("Value"));

        return values;
    }

    public static Characteristic charactersticTamanho() {
        return new Characteristic("Tamanho", ValueTypeCharacteristic.TAMANHO,charactersticValues(), CharacteristicOrigin.PRODUCT);
    }
}
