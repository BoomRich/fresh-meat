/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.boomrich.freshmeat.comparator;

import java.util.Comparator;
import org.boomrich.freshmeat.entities.Product;

/**
 *
 * @author will
 */
public class ProductReviewComparator implements Comparator<Product>{
    @Override
    public int compare(Product p1,Product p2)
    {
        return p1.getReviewCollection().size()-p2.getReviewCollection().size();
    }
}
