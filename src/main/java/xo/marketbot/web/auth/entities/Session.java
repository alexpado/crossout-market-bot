package xo.marketbot.web.auth.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xo.marketbot.entities.discord.UserEntity;
import xo.marketbot.web.auth.entities.responses.TokenResponse;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.servlet.ServletRequest;
import java.time.LocalDateTime;

@Entity
public class Session {

    private static final Logger LOGGER = LoggerFactory.getLogger(Session.class);

    @Id
    @Column(length = 64)
    private String        code;
    @OneToOne
    private UserEntity    user;
    @JsonIgnore
    private String        ipAddress;
    @JsonIgnore
    private String        token;
    @JsonIgnore
    private String        refreshToken;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;
    private LocalDateTime lastUsedAt;

    public Session() {}

    public Session(ServletRequest request, UserEntity user, TokenResponse tokenResponse, String code) {

        this.code      = code;
        this.user      = user;
        this.ipAddress = request.getRemoteAddr();

        this.token        = tokenResponse.getAccessToken();
        this.refreshToken = tokenResponse.getRefreshToken();

        this.expiresAt  = LocalDateTime.now().plusSeconds(tokenResponse.getExpiresIn());
        this.createdAt  = LocalDateTime.now();
        this.lastUsedAt = LocalDateTime.now();
    }

    /**
     * Retrieve this {@link Session}'s short code used to first authenticate with Discord.
     *
     * @return The code.
     */
    public String getCode() {

        return this.code;
    }

    /**
     * Define this {@link Session}'s short code used to first authenticate with Discord.
     *
     * @param code
     *         The code.
     */
    public void setCode(String code) {

        this.code = code;
    }

    /**
     * Retrieve this {@link Session}'s {@link UserEntity}.
     *
     * @return The {@link UserEntity} owning this {@link Session}.
     */
    public UserEntity getUser() {

        return this.user;
    }

    /**
     * Define this {@link Session}'s {@link UserEntity}
     *
     * @param user
     *         The {@link UserEntity} owning this {@link Session}.
     */
    public void setUser(UserEntity user) {

        if (!this.user.getId().equals(user.getId())) {
            LOGGER.warn("A session owner switch occurred ! If it wasn't done on purpose, this is a major security issue.");
        }

        this.user = user;
    }

    /**
     * Retrieve this {@link Session}'s ip address.
     *
     * @return An IP Address.
     */
    public String getIpAddress() {

        return this.ipAddress;
    }

    /**
     * Define this {@link Session}'s ip address.
     *
     * @param ipAddress
     *         An IP Address.
     */
    public void setIpAddress(String ipAddress) {

        if (!this.ipAddress.equals(ipAddress)) {
            LOGGER.warn("A session IP switch occurred ! If it wasn't done on purpose, this is a major security issue.");
        }

        this.ipAddress = ipAddress;
    }

    /**
     * Retrieve this {@link Session}'s token.
     *
     * @return The token.
     */
    public String getToken() {

        return this.token;
    }

    /**
     * Define this {@link Session}'s token.
     *
     * @param token
     *         The token.
     */
    public void setToken(String token) {

        this.token = token;
    }

    /**
     * Retrieve this {@link Session}'s refresh token.
     *
     * @return The refresh token.
     */
    public String getRefreshToken() {

        return this.refreshToken;
    }

    /**
     * Define this {@link Session}'s refresh token.
     *
     * @param refreshToken
     *         The refresh token.
     */
    public void setRefreshToken(String refreshToken) {

        this.refreshToken = refreshToken;
    }

    /**
     * Retrieve this {@link Session} expiration date.
     *
     * @return The expiration date.
     */
    public LocalDateTime getExpiresAt() {

        return this.expiresAt;
    }

    /**
     * Define this {@link Session}'s expiration date.
     *
     * @param expiresAt
     *         The expiration date.
     */
    public void setExpiresAt(LocalDateTime expiresAt) {

        this.expiresAt = expiresAt;
    }

    /**
     * Retrieve this {@link Session}'s creation date.
     *
     * @return The creation date.
     */
    public LocalDateTime getCreatedAt() {

        return this.createdAt;
    }

    /**
     * Define this {@link Session}'s creation date.
     *
     * @param createdAt
     *         The creation date.
     */
    public void setCreatedAt(LocalDateTime createdAt) {

        this.createdAt = createdAt;
    }

    /**
     * Retrieve this {@link Session}'s last usage date.
     *
     * @return The last usage date.
     */
    public LocalDateTime getLastUsedAt() {

        return this.lastUsedAt;
    }

    /**
     * Define this {@link Session}'s last usage date.
     *
     * @param lastUsedAt
     *         The last usage date.
     */
    public void setLastUsedAt(LocalDateTime lastUsedAt) {

        this.lastUsedAt = lastUsedAt;
    }

}
