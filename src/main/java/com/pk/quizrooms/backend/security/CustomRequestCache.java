package com.pk.quizrooms.backend.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

public class CustomRequestCache extends HttpSessionRequestCache {

    @Override
    public void saveRequest(HttpServletRequest request, HttpServletResponse response) {
        if (!SecurityUtils.isFrameWorkInternalRequest(request)) {
            super.saveRequest(request, response);
        }

    }
}

