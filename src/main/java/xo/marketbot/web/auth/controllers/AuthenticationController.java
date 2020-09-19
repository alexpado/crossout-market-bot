package xo.marketbot.web.auth.controllers;

import fr.alexpado.lib.rest.exceptions.RestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import xo.marketbot.entities.discord.User;
import xo.marketbot.repositories.UserEntityRepository;
import xo.marketbot.web.auth.AuthManager;
import xo.marketbot.web.auth.entities.Session;
import xo.marketbot.web.auth.entities.responses.TokenResponse;
import xo.marketbot.web.auth.entities.responses.UserResponse;
import xo.marketbot.web.auth.repositories.SessionRepository;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);

    private final AuthManager          manager;
    private final SessionRepository    sessionRepository;
    private final UserEntityRepository userRepository;

    public AuthenticationController(AuthManager manager, SessionRepository sessionRepository, UserEntityRepository userRepository) {

        this.manager           = manager;
        this.sessionRepository = sessionRepository;
        this.userRepository    = userRepository;
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Session authenticate(HttpServletRequest request, @RequestBody Map<String, String> body) {

        if (!body.containsKey("code")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        try {
            String            code            = body.get("code");
            Optional<Session> optionalSession = this.sessionRepository.findById(code);

            TokenResponse tokenResponse;

            if (optionalSession.isPresent()) {
                tokenResponse = new TokenResponse(optionalSession.get());
            } else {
                tokenResponse = this.manager.tryAuthAction(code).complete();
            }

            UserResponse   userResponse = this.manager.validateTokenAction(tokenResponse.getAccessToken()).complete();
            Optional<User> optionalUser = this.userRepository.findById(userResponse.getId());
            User           user;
            Session        session;

            if (optionalUser.isPresent()) {
                user = optionalUser.get();
                user.merge(userResponse);
            } else {
                user = userResponse.toUser();
            }

            this.userRepository.save(user);


            if (optionalSession.isPresent()) {
                session = optionalSession.get();
                session.setUser(user);
            } else {
                session = new Session(request, user, tokenResponse, code);
            }

            return this.sessionRepository.save(session);

        } catch (RestException restException) {
            LOGGER.error("Unable to login: The server did not responded as expected.");
            LOGGER.debug(new String(restException.getResponseBody(), StandardCharsets.UTF_8));
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, restException.getMessage());
        } catch (Exception e) {
            LOGGER.error("Unable to login: An error occurred.", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
