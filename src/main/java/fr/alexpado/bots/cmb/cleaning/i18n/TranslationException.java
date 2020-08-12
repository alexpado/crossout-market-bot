package fr.alexpado.bots.cmb.cleaning.i18n;

public class TranslationException extends RuntimeException {

    private final Throwable causedBy;

    public TranslationException(Throwable causedBy) {

        this.causedBy = causedBy;
    }

    public Throwable getCausedBy() {

        return causedBy;
    }
}
