package com.soulcode.Servicos.Security;

import com.soulcode.Servicos.Util.JWTUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

// Entra em ação em todos endpoints que está protegido
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
    private JWTUtils jwtUtils;

    public JWTAuthorizationFilter(AuthenticationManager manager, JWTUtils jwtUtils) {
        super(manager);
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = request.getHeader("Authorization"); // "Bearer dasdasd2313123"
        if (token != null && token.startsWith("Bearer")) { // token "válido"
            UsernamePasswordAuthenticationToken authToken = getAuthentication(token.substring(7));
            if (authToken != null) {
                SecurityContextHolder.getContext().setAuthentication(authToken);
                // SecurityContextHolder.getContext().getAuthentication();
                // Guarda informações do usuário autenticado no contexto do Spring
                // Essa informação pode ser utilizada dentro dos controllers da aplicação
            }
        }
        chain.doFilter(request, response);
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        String login = jwtUtils.getLogin(token); // extrai o login do subject
        if (login == null) {
            return null;
        }
        return new UsernamePasswordAuthenticationToken(login, null, new ArrayList<>());
    }
}
