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
import org.boomrich.freshmeat.entities.User;

/**
 *
 * @author chenrz925
 */
@Stateless
public class UserFacade extends AbstractFacade<User> {

    @PersistenceContext(unitName = "org.boomrich_fresh-meat_war_2.0-FUCKPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserFacade() {
        super(User.class);
    }

    public void addUser(User user) {
        if (user != null) {
            em.persist(user);
        }
    }

    public User getUser(String email, String password) {
        //String sql = "select new User(u.email,u.password) from User u where Email=\"" + email + "\" and Password=\"" + password + "\"";
        //String sql = "select * from User where email=':email' and password=':password;'";
        Query query = em.createQuery("SELECT new User(u.userId) FROM User u where u.email=:email and u.password = :password");
        query.setParameter("email", email);
        query.setParameter("password", password);
        System.out.println("!!!!SQL");

        List list = query.getResultList();
        if (list == null || list.size() <= 0) {
            return null;
        } else {
            return (User) list.get(0);
        }
    }

    public boolean isSameUserExist(String email) {
        Query query = em.createQuery("SELECT u.userId FROM User u WHERE u.email=?1");
        query.setParameter(1, email);
        List list = query.getResultList();
        if (list != null && list.size() > 0) {
            return true;
        }
        return false;
    }

    public int getUserAmount() {
        Query query = em.createQuery("SELECT COUNT(U.email) FROM User u");
        List list = query.getResultList();
        if (list != null && list.size() > 0) {
            return ((Long) list.get(0)).intValue();
        }
        return -1;
    }

}
