/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.boomrich.freshmeat.sessions;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.boomrich.freshmeat.entities.Review;

/**
 *
 * @author chenrz925
 */
@Stateless
public class ReviewFacade extends AbstractFacade<Review> {

    @PersistenceContext(unitName = "org.boomrich_fresh-meat_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ReviewFacade() {
        super(Review.class);
    }
    
}
