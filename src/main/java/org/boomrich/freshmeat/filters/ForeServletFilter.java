/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.boomrich.freshmeat.filters;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.boomrich.freshmeat.entities.Category;
import org.boomrich.freshmeat.entities.OrderItem;
/**
 *
 * @author will
 */
import org.boomrich.freshmeat.entities.User;
import org.boomrich.freshmeat.facelets.CategoryController;
import org.boomrich.freshmeat.facelets.OrderItemController;

public class ForeServletFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String contextPath = req.getServletContext().getContextPath();
        req.getServletContext().setAttribute("contextPath", contextPath);

        User user = (User) req.getSession().getAttribute("user");
        int cartTotalItemNumber = 0;

        if (user != null) {
            List list = new OrderItemController().listByUser(user.getUserId());
            Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
                OrderItem orderItem = (OrderItem) iterator.next();
                cartTotalItemNumber += orderItem.getAmount();
            }
        }

        req.setAttribute("cartTotalItemNumber", cartTotalItemNumber);

        /**List<Category> cs = (List<Category>) req.getAttribute("cs");
        if (cs == null) {
            //cs = new CategoryController().getItems();
            //req.setAttribute("cs", cs);
        }*/

        String uri = req.getRequestURI();
        uri = StringUtils.remove(uri, contextPath);
        System.out.println(uri);
        if (uri.startsWith("/fore") && !uri.startsWith("/foreServlet")) {
            String method = StringUtils.substringAfterLast(uri, "/fore");
            req.setAttribute("method", method);
            request.getRequestDispatcher("/foreServlet").forward(req, res);
            return;
        }
        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {
    }

}
