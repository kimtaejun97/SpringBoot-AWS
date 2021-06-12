package com.study.springboot.config.auth;


import com.study.springboot.config.auth.dto.SesstionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
    private final HttpSession httpSession;


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isLonginUserAnnotation = parameter.getParameterAnnotation(LoginUser.class) !=null;
        boolean isUserClass = SesstionUser.class.equals(parameter.getParameterType());
        return isLonginUserAnnotation && isUserClass;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        return httpSession.getAttribute("user");
    }
}
