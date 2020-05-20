package fr.alexpado.bots.cmb.modules.discord.utils;


import fr.alexpado.bots.cmb.modules.discord.models.DiscordUser;
import fr.alexpado.bots.cmb.modules.discord.models.Session;
import fr.alexpado.bots.cmb.modules.discord.repositories.SessionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class SessionQuery {

    private final SessionRepository repository;

    public SessionQuery(SessionRepository repository) {
        this.repository = repository;
    }

    public Session getSession(HttpServletRequest request, HttpStatus statusIfNotFound) {
        String bearer = request.getHeader("Authorization");
        if (bearer == null) {
            throw new ResponseStatusException(statusIfNotFound);
        }

        String code = request.getHeader("Authorization").replace("Bearer", "").trim();
        Session session = this.repository.findByCode(code).orElseThrow(() -> new ResponseStatusException(statusIfNotFound, "No session opened with this code."));
        if (session.getIpAddress().equals(request.getRemoteAddr())) {
            session.setLastUse(System.currentTimeMillis());
            return this.repository.save(session);
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "IP Mismatch");
    }

    public Session getSession(HttpServletRequest request) {
        return this.getSession(request, HttpStatus.UNAUTHORIZED);
    }

    public Session getSession(String code, DiscordUser user) {
        return this.repository.findByCodeAndDiscordUser(code, user).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public List<Session> getSessions(DiscordUser user) {
        List<Session> sessions = this.repository.findAllByDiscordUser(user);
        if (sessions.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        return sessions;
    }

    public Session createSession(DiscordUser user, String code, String ipAddress) {
        Session session = new Session();

        session.setCode(code);
        session.setDiscordUser(user);
        session.setIpAddress(ipAddress);

        return this.repository.save(session);
    }

}
