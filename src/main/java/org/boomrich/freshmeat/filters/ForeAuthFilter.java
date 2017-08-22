/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.boomrich.freshmeat.filters;

import java.io.IOException;
import java.util.Arrays;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.boomrich.freshmeat.entities.User;



/**
 *
 * @author will
 */
public class ForeAuthFilter implements Filter{

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        
        String contextPath = request.getServletContext().getContextPath();
        String[] noNeedAuthPage = new String[]{
            "home.xhtml",
            "checkLogin.xhtml",
            "register.xhtml",
            "login.xhtml",
            "product.xhtml",
            "category.xhtml",
            "search.xhtml"
        };
        
        String uri = req.getRequestURI();
        uri = StringUtils.remove(uri,contextPath);
        String pageName = uri.substring(1);
        if(!Arrays.asList(noNeedAuthPage).contains(pageName))
        {
            User user = (User) req.getSession().getAttribute("user");
            if(user==null)
            {
                res.sendRedirect("login.xhtml");
                return;
            }
        }
        chain.doFilter(req, res);
        
    }

    @Override
    public void destroy() {
    }
    
}
