package com.pk.quizrooms.backend.security;


import com.vaadin.flow.server.HandlerHelper;
import com.vaadin.flow.shared.ApplicationConstants;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.stream.Stream;

final public class SecurityUtils {

    static boolean isFrameWorkInternalRequest(HttpServletRequest request) {

        final String parameterValue=request.getParameter(ApplicationConstants.REQUEST_TYPE_PARAMETER);
        return parameterValue!=null&& Stream.of(HandlerHelper.RequestType.values()).anyMatch(r->r.getIdentifier().equals(parameterValue));
    }

    static boolean isUserLoggedIn()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication!=null && authentication.isAuthenticated();
    }


}
