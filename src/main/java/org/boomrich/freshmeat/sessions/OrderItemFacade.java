/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.boomrich.freshmeat.sessions;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import org.boomrich.freshmeat.entities.OrderItem;

/**
 *
 * @author chenrz925
 */
@Stateless
public class OrderItemFacade extends AbstractFacade<OrderItem> {

    @PersistenceContext(unitName = "org.boomrich_fresh-meat_war_2.0-FUCKPU")
    private EntityManager em;

    /*@Resource
    UserTransaction utx;*/
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List getOrderItemByUserID(int id) {
        String sql = "select * from OrderItem where user_id = ?1 and order_id = -1 order by order_item_id desc";
        Query query = em.createQuery(sql);
        query.setParameter(1, id);
        List list = query.getResultList();
        return list;
    }

    public OrderItemFacade() {
        super(OrderItem.class);
    }

    public void updateOrderItem(OrderItem orderItem) {
        em.merge(orderItem);
    }
    
    public void saveOrderItem(OrderItem orderItem)
    {
        System.out.println("Fuck Save");
        /*String sql = "Insert into OrderItem Values(:amount,:order_item_id,:order_id,:product_id)";
        Query query = em.createQuery(sql);
        query.setParameter("amount", orderItem.getAmount());
        query.setParameter("order_item_id", orderItem.getOrderItemId());
        query.setParameter("order_id", orderItem.getOrderId());
        query.setParameter("", sql)*/
        //em.persist(orderItem);

        
        /*try {
            utx.begin();
            em.persist(orderItem);
            utx.commit();
        } catch (IllegalStateException | SecurityException | HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException e) {
            try {
                utx.rollback();
            } catch (IllegalStateException ex) {
                Logger.getLogger(OrderItemFacade.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(OrderItemFacade.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SystemException ex) {
                Logger.getLogger(OrderItemFacade.class.getName()).log(Level.SEVERE, null, ex);
            }
        }*/
        System.out.println("Fuck shit");
    }

    public void removeOrderItemByID(int order_item_id) {
        String sql = "delete from OrderItem where order_item_id = ?1";
        Query query = em.createQuery(sql);
        query.setParameter(1, order_item_id);
        query.executeUpdate();
    }
    
    public int getOrderItemAmount() {
        Query query = em.createQuery("SELECT COUNT(O.orderItemId) FROM OrderItem o");
        return query.getFirstResult();
    }

}
