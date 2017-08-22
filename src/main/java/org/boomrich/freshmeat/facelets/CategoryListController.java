/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.boomrich.freshmeat.facelets;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.boomrich.freshmeat.entities.Category;
import org.boomrich.freshmeat.entities.Product;

/**
 *
 * @author chenrz925
 */
@Named
@SessionScoped
public class CategoryListController implements Serializable {

    @EJB
    org.boomrich.freshmeat.sessions.CategoryFacade categoryFacade;

    @EJB
    org.boomrich.freshmeat.sessions.ProductFacade productFacade;

    private int categoryId = -1;

    public CategoryListController() {
    }

    public int categoryIDFrom(String first, String second) {
        List<Category> categorys = categoryFacade.findAll();
        for (Category category : categorys) {
            if (category.getCategoryName().matches(".*" + second + ".*@.*" + first + ".*")) {
                return category.getCategoryId();
            }
        }
        return -1;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public Category getCategory() {
        return categoryFacade.find(categoryId);
    }

    public List<Product> getProducts() {
        Collection<Product> products = categoryFacade.find(categoryId).getProductCollection();
        if (products == null || products.size() == 0) {
            List sProducts = productFacade.searchProductsByCategory(getCategory());
            System.out.println(sProducts.size() + "SIZE-----");
            return sProducts;
        } else {
            List<Product> nProducts = new ArrayList<>();
            for (Product product : products) {
                nProducts.add(product);
            }
            System.out.println(nProducts.size() + "SIZE-----");
            return nProducts;
        }
    }
}
