/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.boomrich.freshmeat.facelets;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.boomrich.freshmeat.entities.Product;

/**
 *
 * @author chenrz925
 */
@Named
@SessionScoped
public class SearchController implements Serializable {

    @EJB
    private org.boomrich.freshmeat.sessions.ProductFacade productFacade;
    private String keyword;
    String[] defaults = new String[]{"鱼", "肉", "蛋", "奶", "储值卡"};

    public SearchController() {
        keyword = defaults[(int) (5 * Math.random())];
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    public List<Product> getProducts() {
        System.out.println("HELLO******" + keyword);
        System.out.println("fuck");
        return productFacade.searchProductsByKey(keyword);
    }

    public String getTitle() {
        return "搜索结果-" + keyword;
    }
}
