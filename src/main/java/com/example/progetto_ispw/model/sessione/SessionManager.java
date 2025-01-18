package com.example.progetto_ispw.model.sessione;

import java.util.HashMap;
import java.util.Map;

//----CLASSE SINGLETON PER LA GESTIONE DELLE SESSIONI APERTE----
public class SessionManager {
    private static volatile SessionManager instance;
    private Map<String, Session> activeSessions;

    protected SessionManager() {
        this.activeSessions = new HashMap<>();
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            synchronized (SessionManager.class) {
                if (instance == null) {
                    instance = new SessionManager();
                }
            }
        }
        return instance;
    }
    public synchronized Session createSession(String sessionId) {
        if (!activeSessions.containsKey(sessionId)) {
            Session session = new Session(sessionId);
            activeSessions.put(sessionId, session);
            return session;
        }
        return activeSessions.get(sessionId);
    }

    public synchronized Session getSession(String sessionId) {
        return activeSessions.get(sessionId);
    }

    public synchronized void invalidateSession(String sessionId) {
        activeSessions.remove(sessionId);
    }
    public synchronized void invalidateSessions() {
        activeSessions.clear();
    }
}
