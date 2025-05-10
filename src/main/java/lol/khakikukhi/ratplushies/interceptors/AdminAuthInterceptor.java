package lol.khakikukhi.ratplushies.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lol.khakikukhi.ratplushies.constants.Messages;
import lol.khakikukhi.ratplushies.constants.UserRoles;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(Messages.WebError.NO_SESSION.getMessage());
            return false;
        }

        if (session.getAttribute("userId") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(Messages.WebError.NOT_AUTHENTICATED.getMessage());
            return false;
        }

        Object roleAttr = session.getAttribute("userRole");
        if (!(roleAttr instanceof UserRoles.Role role) || role != UserRoles.Role.ADMIN) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write(Messages.WebError.NOT_AUTHORISED.getMessage());
            return false;
        }

        return true;
    }


}
