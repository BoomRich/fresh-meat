/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.boomrich.freshmeat.sessions;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.boomrich.freshmeat.entities.Category;
import org.boomrich.freshmeat.entities.Product;

/**
 *
 * @author chenrz925
 */
@Stateless
public class ProductFacade extends AbstractFacade<Product> {

    @PersistenceContext(unitName = "org.boomrich_fresh-meat_war_2.0-FUCKPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProductFacade() {
        super(Product.class);
    }

    public List<Product> searchProductsByKey(String keyword) {
        System.out.println(keyword);
        Query query = em.createQuery("select P from Product p where p.productName LIKE '%" + keyword + "%'");
        //query.setParameter("key", keyword);
        List list = query.getResultList();
        return list; //To change body of generated methods, choose Tools | Templates.
    }
    
    public List<Product> searchProductsByCategory(Category categoryId) {
        Query query = em.createQuery("SELECT p FROM Product p WHERE p.categoryId=?1");
        query.setParameter(1, categoryId);
        return query.getResultList();
    }
    
}
