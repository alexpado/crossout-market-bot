package fr.alexpado.bots.cmb.configurations.interfaces;

public interface IDiscordConfiguration {

    String getClientId();

    String getClientSecret();

    String getGrantType();

    String getRedirectUri();

    String getScope();

    String getToken();

    String getPrefix();

    boolean isEnabled();

}
