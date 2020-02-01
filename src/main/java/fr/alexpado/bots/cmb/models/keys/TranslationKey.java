package fr.alexpado.bots.cmb.models.keys;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TranslationKey implements Serializable {

    private String translationKey;
    private String language;

}
