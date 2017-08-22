/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.boomrich.freshmeat.facelets;

import java.io.Serializable;
import java.util.Map;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.Cookie;
import org.boomrich.freshmeat.entities.User;

/**
 *
 * @author chenrz925
 */
@Named
@SessionScoped
public class NavController implements Serializable {

    @EJB
    org.boomrich.freshmeat.sessions.UserFacade userFacade;

    public NavController() {
    }

    public String getUserName() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            Map<String, Object> cookieMap = context.getExternalContext().getRequestCookieMap();
            int userId = -1;
            boolean login = false;
            for (Map.Entry<String, Object> entry : cookieMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (entry.getKey().equals("userId") && ((Cookie) entry.getValue()).getValue() != null) {
                    userId = Integer.parseInt(((Cookie) entry.getValue()).getValue());
                } else if (entry.getKey().equals("login") && ((Cookie) entry.getValue()).getValue() != null) {
                    login = Boolean.parseBoolean(((Cookie) entry.getValue()).getValue());
                }
            }
            User user;
            if (userId > -1) {
                user = userFacade.find(userId);
                if (user.getUsername() != null && !user.getUsername().equals("")) {
                    return user.getUsername();
                }
            }
            return "用户";
        } catch (Exception e) {
            return "用户";
        }
    }
    
    public User getUser() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            Map<String, Object> cookieMap = context.getExternalContext().getRequestCookieMap();
            int userId = -1;
            boolean login = false;
            for (Map.Entry<String, Object> entry : cookieMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (entry.getKey().equals("userId") && ((Cookie) entry.getValue()).getValue() != null) {
                    userId = Integer.parseInt(((Cookie) entry.getValue()).getValue());
                } else if (entry.getKey().equals("login") && ((Cookie) entry.getValue()).getValue() != null) {
                    login = Boolean.parseBoolean(((Cookie) entry.getValue()).getValue());
                }
            }
            User user;
            System.out.println("FUCK___!!!___");
            if (userId > -1) {
                user = userFacade.find(userId);
                if (user.getUsername() != null && !user.getUsername().equals("")) {
                    return user;
                }
            }
            return new User(-1);
        } catch (Exception e) {
            return new User(-1);
        }
    }

    public String[] getIndexBGNames() {
        String[] ibgn = new String[6];
        for (int i = 0; i < 6; i++) {
            ibgn[i] = "img/indexbg/IB" + (i + 1) + ".jpeg";
        }
        return ibgn;
    }
    
    public String getLogButtonLabel() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, Object> cookies = context.getExternalContext().getRequestCookieMap();
        boolean login = false;
        for (Map.Entry<String, Object> entry : cookies.entrySet()) {
            String key = entry.getKey();
            Cookie value = (Cookie) entry.getValue();
            if (value.getName().equals("login")) {
                login |= Boolean.valueOf(value.getValue());
            }
        }
        if (login) return "注销"; else return "登录";
    }
    
    public String getLogButtonHref() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, Object> cookies = context.getExternalContext().getRequestCookieMap();
        boolean login = false;
        for (Map.Entry<String, Object> entry : cookies.entrySet()) {
            String key = entry.getKey();
            Cookie value = (Cookie) entry.getValue();
            if (value.getName().equals("login")) {
                login |= Boolean.valueOf(value.getValue());
            }
        }
        if (login) return "forelogout"; else return "forelogin";
    }
}
