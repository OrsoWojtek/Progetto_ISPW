package com.example.progetto_ispw.model.sessione;

import com.example.progetto_ispw.constants.SessionID;

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
    public synchronized Session createSession(SessionID id) {
        String sessionId = id.getValue();
        if (!activeSessions.containsKey(sessionId)) {
            Session session = new Session(sessionId);
            activeSessions.put(sessionId, session);
        }
        return activeSessions.get(sessionId);
    }

    public synchronized Session getSession(SessionID id) {
        return activeSessions.get(id.getValue());
    }

    public synchronized void invalidateSession(SessionID sessionId) {
        activeSessions.remove(sessionId.getValue());
    }
    public synchronized void invalidateSessions() {
        activeSessions.clear();
    }
}
