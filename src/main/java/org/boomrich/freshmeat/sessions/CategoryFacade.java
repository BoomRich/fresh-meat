/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.boomrich.freshmeat.sessions;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.boomrich.freshmeat.entities.Category;

/**
 *
 * @author chenrz925
 */
@Stateless
public class CategoryFacade extends AbstractFacade<Category> {

    //@PersistenceContext(unitName = "org.boomrich_fresh-meat_war_2.0-FUCKPU")
    @PersistenceContext(unitName = "org.boomrich_fresh-meat_war_2.0-FUCKPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CategoryFacade() {
        super(Category.class);
    }
    
}
