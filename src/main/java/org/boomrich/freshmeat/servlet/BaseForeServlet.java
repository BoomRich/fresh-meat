/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.boomrich.freshmeat.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.boomrich.freshmeat.facelets.CategoryController;
import org.boomrich.freshmeat.facelets.OrderItemController;
import org.boomrich.freshmeat.facelets.OrderListController;
import org.boomrich.freshmeat.facelets.ProductController;
import org.boomrich.freshmeat.facelets.ProductPageController;
import org.boomrich.freshmeat.facelets.ReviewController;
import org.boomrich.freshmeat.facelets.UserController;
import org.boomrich.freshmeat.util.Page;

/**
 *
 * @author will
 */
class BaseForeServlet extends HttpServlet{
    protected CategoryController categoryController = new CategoryController();
    protected OrderItemController orderItemController = new OrderItemController();
    protected OrderListController orderListController = new OrderListController();
    protected ProductController productController = new ProductController();
    protected ProductPageController productImageController = new ProductPageController();
    protected ReviewController reviewController = new ReviewController();
    protected UserController userController = new UserController();
    
    @Override
    public void service(HttpServletRequest request,HttpServletResponse response)
    {
        try
        {
            int start = 0;
            int count = 20;
            try
            {
                start = Integer.parseInt(request.getParameter("page.start"));
            }catch (NumberFormatException ex) {
                
            }
            try
            {
                count = Integer.parseInt(request.getParameter("page.count"));
                
            }catch (NumberFormatException ex) {
                
            }
            
            Page page = new Page(start,count);
            String method = (String) request.getAttribute("method");
            
            Method m = this.getClass().getMethod(method, HttpServletRequest.class,HttpServletResponse.class,Page.class);
            
            String redirect = (String) m.invoke(this, request,response,page);
            
            System.out.println(redirect);
            if(redirect.startsWith("@"))
                //redirect.substring-->1?????
                response.sendRedirect(redirect.substring(1));
            else if(redirect.startsWith("%"))
                response.getWriter().print(redirect.substring(1));
            else
                request.getRequestDispatcher(redirect).forward(request, response);
          
        }catch (IOException | IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException | ServletException ex) {
            ex.printStackTrace();
        }
    }
}
