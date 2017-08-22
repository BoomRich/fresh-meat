/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.boomrich.freshmeat.sessions;

import java.util.List;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import org.boomrich.freshmeat.entities.OrderList;

/**
 *
 * @author chenrz925
 */
@Stateless
public class OrderListFacade extends AbstractFacade<OrderList> {

    @PersistenceContext(unitName = "org.boomrich_fresh-meat_war_2.0-FUCKPU")
    private EntityManager em;
    
    /*@Resource
    UserTransaction utx;*/
    

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OrderListFacade() {
        super(OrderList.class);
    }

    public void InsertObject(OrderList orderList) {
        em.persist(orderList);
    }
    
    public int getOrderListAmount() {
        Query query = em.createQuery("SELECT COUNT(O.orderId) FROM OrderList o");
        return query.getFirstResult();
    }
    
    public void saveOrderList(OrderList orderList)
    {
        //em.persist(orderList)；
        /*try {
            utx.begin();
            em.persist(orderList);
            utx.commit();
        } catch (IllegalStateException | SecurityException | HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException e) {
            utx.rollback();
        }*/
    }

}
