package fr.alexpado.bots.cmb.throwables;

public class TranslationException extends Exception {

    public TranslationException(int needed, int retrieved) {
        super("Missing " + (needed - retrieved) + " translation(s).");
    }
    
}
