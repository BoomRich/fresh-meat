/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.boomrich.freshmeat.facelets;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.boomrich.freshmeat.entities.Category;
import org.boomrich.freshmeat.entities.Product;

/**
 *
 * @author chenrz925
 */
@Named("productPageController")
@SessionScoped
public class ProductPageController implements Serializable {

    private int productId = 0;
    private int amount = 1;

    @EJB
    private org.boomrich.freshmeat.sessions.ProductFacade ejbProductFacade;
    @EJB
    private org.boomrich.freshmeat.sessions.CategoryFacade ejbCategoryFacade;

    public ProductPageController() {
    }

    private org.boomrich.freshmeat.sessions.ProductFacade getProductFacade() {
        return ejbProductFacade;
    }

    private org.boomrich.freshmeat.sessions.CategoryFacade getCategoryFacade() {
        return ejbCategoryFacade;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getProductId() {
        return productId;
    }

    private String getImagePath(int id, char type, int order) {
        return "img/" + id + "/" + type + "" + order + ".jpg";
    }

    public List<String> getPreviewImagePaths() {
        List<String> previewImagePaths = new ArrayList<String>();
        Product product;
        product = getProductFacade().find(productId);
        for (int i = 0; i < product.getPreviewImage(); ++i) {
            previewImagePaths.add(getImagePath(productId, 'P', i));
        }
        return previewImagePaths;
    }

    public List<String> getDetailImagePaths() {
        List<String> detailImagePaths = new ArrayList<String>();
        Product product;
        product = getProductFacade().find(productId);
        for (int i = 0; i < product.getDetailImage(); i++) {
            detailImagePaths.add(getImagePath(productId, 'D', i));
        }
        return detailImagePaths;
    }

    public String getFirstCategory() {
        String[] cats = getProductFacade().find(productId).getCategoryId().getCategoryName().split("@");
        int i = 0;
        for (String cat : cats) {
            if (!cat.equals("")) {
                if (i >= 1) {
                    return cat;
                } else {
                    i++;
                }
            }
        }
        return "!NOTFOUND";
    }

    public String getSecondCategory() {
        String[] cats = getProductFacade().find(productId).getCategoryId().getCategoryName().split("@");
        int i = 0;
        for (String cat : cats) {
            if (!cat.equals("")) {
                if (i >= 0) {
                    return cat;
                } else {
                    i++;
                }
            }
        }
        return "!NOTFOUND";
    }

    public Product getProduct() {
        return getProductFacade().find(productId);
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
    
    public String getCartUrl() {
        return "cart.xhtml?product_id=" + getProductId() + "&amount=" + getAmount();
    }
}
