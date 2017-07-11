/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.boomrich.freshmeat.facelets;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author chenrz925
 */
@Named("productImageController")
@SessionScoped
public class ProductImageController implements Serializable {
    private int innerOrder;
    private char imageType;
    private int productId;

    public int getInnerOrder() {
        return innerOrder;
    }

    public void setInnerOrder(int innerOrder) {
        this.innerOrder = innerOrder;
    }

    public char getImageType() {
        return imageType;
    }

    public void setImageType(char imageType) {
        this.imageType = imageType;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getProductId() {
        return productId;
    }
    
    public String getImagePath() {
        return "img/" + productId + "/" + imageType + "" + innerOrder + ".jpg";
    }
}
