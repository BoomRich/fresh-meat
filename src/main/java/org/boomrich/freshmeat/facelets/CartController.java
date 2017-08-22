/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.boomrich.freshmeat.facelets;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.Cookie;
import org.boomrich.freshmeat.entities.OrderItem;
import org.boomrich.freshmeat.entities.OrderList;
import org.boomrich.freshmeat.entities.User;
import org.boomrich.freshmeat.sessions.ProductFacade;

/**
 *
 * @author chenrz925
 */
@Named
@SessionScoped
@ManagedBean
public class CartController implements Serializable {

    private List<OrderItem> orderItems = new ArrayList<>();
    private int newProductId = 0;
    private int newAmount = 0;
    private int newOrderItemId = -1;
    private String mobile = "";
    private String address = "";
    @EJB
    org.boomrich.freshmeat.sessions.ProductFacade ejbProductFacade;
    @EJB
    org.boomrich.freshmeat.sessions.OrderListFacade orderListFacade;
    @EJB
    org.boomrich.freshmeat.sessions.OrderItemFacade orderItemFacade;
    @EJB
    org.boomrich.freshmeat.sessions.UserFacade userFacade;

    public ProductFacade getProductFacade() {
        return ejbProductFacade;
    }

    public CartController() {
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public String addNewOrderItem() {
        if (newAmount == 0 || newProductId == 0) {
            return "购物车";
        }
        OrderItem newOrderItem = new OrderItem();
        newOrderItem.setProductId(getProductFacade().find(newProductId));
        newOrderItem.setAmount(newAmount);
        newOrderItem.setOrderItemId(newOrderItemId);
        newAmount = 0;
        newProductId = 0;
        boolean isFound = false;
        for (int i = 0; i < orderItems.size(); ++i) {
            if (Objects.equals(orderItems.get(i).getProductId().getProductId(), newOrderItem.getProductId().getProductId())) {
                OrderItem t = orderItems.get(i);
                t.setAmount(orderItems.get(i).getAmount() + newOrderItem.getAmount());
                orderItems.set(i, t);
                isFound = true;
            }
        }
        if (!isFound) {
            orderItems.add(newOrderItem);
        }
        return "购物车";
    }

    public void setNewProductId(int newProductId) {
        this.newProductId = newProductId;
    }

    public int getNewProductId() {
        return newProductId;
    }

    public void setNewAmount(int newAmount) {
        this.newAmount = newAmount;
    }

    public int getNewAmount() {
        return newAmount;
    }

    public String getSizeString() {
        return "" + orderItems.size();
    }

    public void removeOrderItem(OrderItem oi) {
        List<OrderItem> newOrderItems = new ArrayList<>();
        for (OrderItem odItem : orderItems) {
            if (Objects.equals(odItem.getProductId().getProductId(), oi.getProductId().getProductId())) {
            } else {
                newOrderItems.add(odItem);
            }
        }
        orderItems = newOrderItems;
        System.out.println("SUCCESS DELETE");
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getTotal() {
        BigDecimal sum = new BigDecimal(0);
        for (OrderItem oi : orderItems) {
            sum = sum.add(oi.getSumPrice());
        }
        return sum;
    }

    public String checkCart() throws IOException {
        System.out.println("-----CHECK START-----");
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, Object> cookieMap = context.getExternalContext().getRequestCookieMap();
        System.out.println("-----COOKIE-----");
        int userId = -1;
        boolean loginState = false;
        String email = "";
        for (Map.Entry<String, Object> entry : cookieMap.entrySet()) {
            String key = entry.getKey();
            Cookie value = (Cookie) entry.getValue();
            System.out.println(value.getName() + "::" + value.getValue());
            if (value.getName().equals("userId")) {
                userId = Integer.valueOf(value.getValue());
            } else if (value.getName().equals("login")) {
                loginState = Boolean.valueOf(value.getValue());
            } else if (value.getName().equals("email")) {
                email = value.getValue();
            }
        }
        System.out.println(loginState);
        if (loginState) {
            Date now = new Date();
            OrderList orderList = new OrderList(orderListFacade.getOrderListAmount() + 1);
            orderList.setCreateTime(now);
            orderList.setMobile(getMobile());
            System.out.println(getMobile());
            orderList.setShipAddress(getAddress());
            User user = userFacade.find(userId);
            if (user == null) {
                System.out.println("NOT FOUND USER LOGIN");
            } else {
                System.out.println(user.getUsername());
                if (!user.getEmail().equals(email)) {
                    return "forelogin";
                } else {
                    System.out.println("-----LOGIN-----");
                }
            }
            orderList.setUserId(user);
            if (user.getUsername() != null && !user.getUsername().equals("")) {
                orderList.setShipName(user.getUsername());
            }
            System.out.println("FUCK！！！！！！！！！！！！！");
            for (OrderItem oi : orderItems) {
                System.out.println(orderList.getOrderId());
                oi.setOrderId(orderList);
                oi.setOrderItemId(orderItemFacade.getOrderItemAmount() + 1);
                System.out.println("DAMN Fuck ");
                orderItemFacade.saveOrderItem(oi);
                System.out.println("Fuck@@@@@@@@@@@@@@@@");
                orderItemFacade.saveOrderItem(oi);
                System.out.println("Fuck~~~~~~~~~~~~~");
            }
            System.out.println("FUCK###############");

            orderList.setOrderId(orderListFacade.getOrderListAmount() + 1);
            orderList.setOrderItemCollection(orderItems);
            orderListFacade.saveOrderList(orderList);
            //orderListFacade.create(orderList);
            System.out.println("-----FINISH-----");
        } else {
            return "forelogin";
        }
        return "index.xhtml";
    }
}
