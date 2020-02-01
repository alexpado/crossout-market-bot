package fr.alexpado.bots.cmb.models;

import fr.alexpado.bots.cmb.models.keys.TranslationKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@Getter
@IdClass(TranslationKey.class)
@Setter
public class Translation {

    @Id
    @Column(length = 100)
    private String translationKey;

    @Id
    @Column(length = 3)
    private String language;

    private String text;

}
