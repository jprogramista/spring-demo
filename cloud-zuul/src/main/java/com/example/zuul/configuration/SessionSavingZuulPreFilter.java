package com.example.zuul.configuration;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Objects.nonNull;

@Component
public class SessionSavingZuulPreFilter extends ZuulFilter {

    private static final Logger logger = LoggerFactory.getLogger(SessionSavingZuulPreFilter.class);

    private AtomicInteger counter = new AtomicInteger(0);

    @Autowired
    private SessionRepository repository;

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {

        int current = counter.getAndIncrement();

        RequestContext context = RequestContext.getCurrentContext();
        HttpSession httpSession = context.getRequest().getSession();
        logger.info("[" + current + "] Session ID from context.getRequest().getSession(): " + context.getRequest().getSession().getId());
        Session session = repository.findById(httpSession.getId());
        logger.info("[" + current + "] Session ID from repository.findById(httpSession.getId()): " + session.getId());
        String sessionId = getSessionId();
        logger.info("[" + current + "] getSessionId(): " + sessionId);
        context.addZuulRequestHeader("Cookie", "SESSION=" + sessionId);

        String servletPath = context.getRequest().getServletPath();
        logger.info("[" + current + "] Path Zuul: " + servletPath);

        Cookie[] cookies = context.getRequest().getCookies();
        if (nonNull(cookies)) {
            for (Cookie cookie : cookies) {
                logger.info("[" + current + "] Cookie Zuul: " + cookie.getName() + " = " + cookie.getValue());
            }
        }
        return null;
    }

    private String getSessionId() {
        Optional<Cookie> cookieOptional = Optional.empty();
        RequestContext context = RequestContext.getCurrentContext();
        Cookie[] cookies = context.getRequest().getCookies();
        if (nonNull(cookies)) {
            cookieOptional = Arrays.stream(cookies)
                    .filter(cookie -> StringUtils.equals("SESSION", cookie.getName()))
                    .findFirst();

        }
        if (cookieOptional.isPresent()) {
            return cookieOptional.get().getValue();
        }
        return context.getRequest().getSession().getId();
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }
}
