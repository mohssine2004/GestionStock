package org.example.gestionstock.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.gestionstock.model.Utilisateur;

import java.io.IOException;

@WebFilter("/pages/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);

        String uri = request.getRequestURI();

        // ── Pages et ressources publiques (pas besoin de connexion) ──
        boolean isPublic = uri.contains("/pages/login.xhtml")
                || uri.contains("/pages/about.xhtml")
                || uri.contains("/pages/contact.xhtml")
                || uri.contains("/index.xhtml")
                || uri.endsWith("/")
                || uri.contains("/pages/error403.xhtml")
                || uri.contains("/jakarta.faces.")
                || uri.contains("/javax.faces.")
                || uri.contains("/faces/")
                || uri.contains(".css")
                || uri.contains(".js")
                || uri.contains(".png")
                || uri.contains(".jpg")
                || uri.contains(".ico")
                || uri.contains("/resources/");

        if (isPublic) {
            chain.doFilter(req, res);
            return;
        }

        // ── Vérifie si l'utilisateur est connecté ──
        Utilisateur utilisateur = (session != null)
                ? (Utilisateur) session.getAttribute("utilisateurConnecte")
                : null;

        if (utilisateur == null) {
            response.sendRedirect(request.getContextPath() + "/pages/login.xhtml");
        } else {
            chain.doFilter(req, res);
        }
    }
}