/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.boomrich.freshmeat.servlet;

import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import org.boomrich.freshmeat.comparator.ProductPriceComparator;
import org.boomrich.freshmeat.comparator.ProductReviewComparator;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.persistence.criteria.Order;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.RandomUtils;
import org.boomrich.freshmeat.entities.Category;
import org.boomrich.freshmeat.entities.OrderItem;
import org.boomrich.freshmeat.entities.Product;
import org.boomrich.freshmeat.entities.User;
import org.boomrich.freshmeat.facelets.UserController;
import org.boomrich.freshmeat.util.Page;
import org.boomrich.freshmeat.entities.OrderList;

/**
 *
 * @author will
 */
public class ForeServlet extends BaseForeServlet {

    @EJB
    org.boomrich.freshmeat.sessions.UserFacade userFacade;

    
    public String register(HttpServletRequest request, HttpServletResponse response, Page page) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String username = request.getParameter("username");
        boolean isExist = userFacade.isSameUserExist(email);
        if (isExist) {
            response.addCookie(new Cookie("msg", "用户名已经被使用"));
            return "register.html";
        } else {
            User user = new User();
            user.setEmail(email);
            user.setPassword(password);
            user.setUserId(userFacade.getUserAmount() + 1);
            user.setUsername(username);
            user.setCreateTime(new Date());
            userFacade.addUser(user);
            response.addCookie(new Cookie("msg", "注册成功"));
            return "login.html";
        }
    }

    public String login(HttpServletRequest request, HttpServletResponse response, Page page) {
        System.out.println("ENTERING LOGIN");
        Cookie[] cookies = request.getCookies();
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        if (email == null && password == null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("login")) {
                    if (cookie.getValue().equals("true")) {
                        response.addCookie(new Cookie("msg", "已登录"));
                        return "@faces/index.xhtml";
                    } else {
                        response.addCookie(new Cookie("msg", "未登录，请登录"));
                        return "login.html";
                    }
                }
            }
        }
        System.out.println("!!!!!!!!!!!!!");
        User user = userFacade.getUser(email, password);
        if (user == null) {
            response.addCookie(new Cookie("login", "false"));
            if (userFacade.isSameUserExist(email)) {
                response.addCookie(new Cookie("msg", "账号密码错误"));
                return "login.html";
            } else {
                response.addCookie(new Cookie("msg", "用户不存在"));
                return "register.html";
            }
        } else {
            Cookie loginCookie = new Cookie("login", Boolean.TRUE + "");
            loginCookie.setMaxAge(3600);
            response.addCookie(loginCookie);
            response.addCookie(new Cookie("msg", "登录成功"));
            response.addCookie(new Cookie("userId", user.getUserId() + ""));
            response.addCookie(new Cookie("email", email));
            response.addCookie(new Cookie("timestamp", (new Date()).getTime() + ""));
            return "@faces/index.xhtml";
        }
    }

    public String product(HttpServletRequest request, HttpServletResponse response, Page page) {
        System.out.println("--------");
        System.out.println("ENTER PRODUCT METHOD");
        System.out.println("---------");
        int product_id = Integer.parseInt(request.getParameter("product_id"));
        System.out.println("RETURNING---------------");
        return "@faces/product.xhtml";
    }

    public String logout(HttpServletRequest request, HttpServletResponse response, Page page) {
        response.addCookie(new Cookie("login", Boolean.FALSE + ""));
        response.addCookie(new Cookie("userId", null));
        response.addCookie(new Cookie("email", null));
        response.addCookie(new Cookie("timestamp", null));
        return "@faces/index.xhtml";
    }

    public String checkLogin(HttpServletRequest request, HttpServletResponse response, Page page) {
        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {
            return "%success";
        }
        return "%fail";
    }

    public String loginAjax(HttpServletRequest request, HttpServletResponse response, Page page) {
        String email = request.getParameter("emial");
        String password = request.getParameter("password");
        User user = userController.getUser(email, password);
        if (null == user) {
            return "%fail";
        }
        request.getSession().setAttribute("user", user);
        return "%success";
    }

    public String category(HttpServletRequest request, HttpServletResponse response, Page page) {
        int category_id = Integer.parseInt(request.getParameter("category_id"));
        Category category = categoryController.getCategory(category_id);
        String sort = request.getParameter("sort");
        if (sort != null) {
            switch (sort) {
                case "review":
                    Collections.sort((List<Product>) category.getProductCollection(), new ProductReviewComparator());
                    break;

                case "price":
                    Collections.sort((List<Product>) category.getProductCollection(), new ProductPriceComparator());
                    break;

            }
        }

        request.setAttribute("category", category);
        return "category.xhtml";
    }

    public String search(HttpServletRequest request, HttpServletResponse response, Page page) {
        String keyword = request.getParameter("keyword");
        List productlist = productController.getProductByKey(keyword);
        request.setAttribute("productlist", productlist);
        return "searchResult.xhtml";
    }

    public String buy(HttpServletRequest request, HttpServletResponse response, Page page) {
        String[] order_item_ids = request.getParameterValues("order_item_ids");
        List<OrderItem> orderItemList = new ArrayList<>();
        float total = 0;
        for (String order_id : order_item_ids) {
            OrderItem orderItem = orderItemController.getOrderItem(Integer.parseInt(order_id));
            Product product = orderItem.getProductId();
            total += product.getProductPrice().floatValue();
            orderItemList.add(orderItem);
        }
        request.getSession().setAttribute("order_item_list", orderItemList);
        request.setAttribute("total", total);
        return "buy.xhtml";
    }

    
    
    
    
    public String addCart(HttpServletRequest request, HttpServletResponse response, Page page) {
        return "%success";
    }

    public String cart(HttpServletRequest request, HttpServletResponse response, Page page) {
        User user = (User) request.getSession().getAttribute("user");
        List orderItemList = orderItemController.listByUser(user.getUserId());
        request.setAttribute("orderItemList", orderItemList);
        return "@faces/cart.xhtml";
    }

    public String changeOrderItem(HttpServletRequest request, HttpServletResponse response, Page page) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "%fail";
        }

        int product_id = Integer.parseInt(request.getParameter("product_id"));
        int number = Integer.parseInt(request.getParameter("number"));

        List orderItemList = orderItemController.listByUser(user.getUserId());
        for (Object o : orderItemList) {
            OrderItem orderItem = (OrderItem) o;
            if (orderItem.getProductId().getProductId() == product_id) {
                orderItem.setAmount(number);
                orderItemController.updateOrderItem(orderItem);
                break;
            }
        }

        return "%success";
    }

    public String deleteOrderItem(HttpServletRequest request, HttpServletResponse response, Page page) {
        User user = (User) request.getSession().getAttribute("user");
        if (null == user) {
            return "%fail";
        }
        int order_item_id = Integer.parseInt(request.getParameter("order_item_id"));
        orderItemController.removeOrderItemById(order_item_id);
        return "%success";
    }

    public String createOrder(HttpServletRequest request, HttpServletResponse response, Page page) {
        User user = (User) request.getSession().getAttribute("user");

        String ship_address = request.getParameter("ship_address");
        String ship_name = request.getParameter("ship_name");
        String mobile = request.getParameter("mobile");
        Date create_time = new Date();

        OrderList orderList = new OrderList();
        orderList.setOrderId(create_time.hashCode());
        orderList.setUserId(user);
        orderList.setShipName(ship_name);
        orderList.setShipAddress(ship_address);
        orderList.setMobile(mobile);
        orderList.setCreateTime(create_time);

        orderListController.InsertObject(orderList);

        float total = 0;
        List<OrderItem> order_item_list = (List<OrderItem>) request.getSession().getAttribute("order_item_list");
        for (OrderItem orderItem : order_item_list) {
            total += orderItem.getAmount() * orderItem.getProductId().getProductPrice().floatValue();
        }

        return "@forealipay?oid=" + orderList.getOrderId() + "&total=" + total;
    }

    public String alipay(HttpServletRequest request, HttpServletResponse response, Page page) {
        return "alipay.xhtml";
    }

    public String payed(HttpServletRequest request, HttpServletResponse response, Page page) {
        int order_id = Integer.parseInt(request.getParameter("order_id"));
        Order order = (Order) orderListController.getOrderList(order_id);
        request.setAttribute("order", order);
        return "payed.xhtml";
    }

}
