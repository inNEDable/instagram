package com.example.instagramproject.util;

import com.example.instagramproject.exceptions.UnauthorizedAccessException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Component
public class SessionManager {

    private static final String LOGGED = "logged";
    private static final String LOGGED_FROM = "loggedFrom";
    public static final String USER_ID = "userID";

    public void login(Long id, HttpSession session, HttpServletRequest request) {
        session.setAttribute(LOGGED_FROM, request.getRemoteAddr());
        session.setAttribute(LOGGED, 1);
        session.setAttribute(USER_ID, id);
        session.setMaxInactiveInterval(-1);
    }

    public void authorizeSession (Long providedUserID, HttpSession session, HttpServletRequest request) {
        if (session.isNew()) throw new UnauthorizedAccessException("This is NEW session. Cannot execute request!");

        String loggedFrom = (String) session.getAttribute(LOGGED_FROM);
        if (loggedFrom == null || !loggedFrom.equals(request.getRemoteAddr())) throw new UnauthorizedAccessException("Session hijacking attempt");

        if (session.getAttribute(LOGGED) == null || (Integer)session.getAttribute(LOGGED) != 1)
            throw new UnauthorizedAccessException("User must be logged first!");

        if (providedUserID != null){
            Long userIdFromSession = (Long) session.getAttribute(USER_ID);
            if (!providedUserID.equals(userIdFromSession)) throw new UnauthorizedAccessException("User trying to manipulate foreign profile");
        }

    }

    public void logOut(HttpSession session){
        session.setAttribute(LOGGED, 0);
    }

    public boolean isLogged(HttpSession session) {
       if (session.isNew()
               || session.getAttribute(LOGGED) == null
               || (Integer)session.getAttribute(LOGGED) != 1 )
           return false;
       return true;
    }

    public Long getUserID(HttpServletRequest request) {
        return (Long) request.getSession().getAttribute(USER_ID);
    }
}
