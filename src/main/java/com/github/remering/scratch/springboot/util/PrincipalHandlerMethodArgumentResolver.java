package com.github.remering.scratch.springboot.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.remering.scratch.springboot.bean.AccountPrincipal;
import com.github.remering.scratch.springboot.service.JwtService;
import lombok.AllArgsConstructor;
import lombok.val;
import lombok.var;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.io.IOException;
import java.util.Base64;


@Component
@AllArgsConstructor
public class PrincipalHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return AccountPrincipal.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public AccountPrincipal resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws IOException {
        var authorizationHeader = webRequest.getHeader(HttpHeaders.AUTHORIZATION.toLowerCase());
        if (authorizationHeader != null && authorizationHeader.toLowerCase().startsWith("bearer ")) {
            val token = authorizationHeader.substring("bearer ".length());
            val decodedJwt = jwtService.verifyToken(token);
            if (decodedJwt != null) {
                val principal = objectMapper.readValue(Base64.getDecoder().decode(decodedJwt.getPayload()), AccountPrincipal.class);
                if (principal != null) return principal;
            }
        }
        throw HttpClientErrorException.create(HttpStatus.UNAUTHORIZED, "用户未登录", HttpHeaders.EMPTY, null, null);
    }

}
