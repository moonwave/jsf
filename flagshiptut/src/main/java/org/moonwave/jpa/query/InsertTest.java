package org.moonwave.jpa.query;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.moonwave.jpa.model.Role;
import org.moonwave.jpa.model.User;

public class InsertTest {

   public static void main( String[ ] args ) {

      EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "jpa-mysql" );
      EntityManager em = emfactory.createEntityManager();
      System.out.println("");

      Query query = em.createQuery("Select e from User e where e.loginId = :login_id", User.class);
      query.setParameter("login_id", "***");

      List<User> list = (List<User>) query.getResultList();

      for(User e : list) {
         System.out.println("User: "+ e.getLoginId() + ", " + e.getPassword());
      }
      System.out.println("");

      query = em.createQuery("Select e from Role e", Role.class);

      List<Role> roles = (List<Role>) query.getResultList();

      for(Role e : roles) {
         System.out.println("Role: "+ e.getAlias() + ", " + e.getName());
      }
      System.out.println("");

      // insert / update new role data
      User jon = list.get(0);
      jon.setHint("Your favorite school"); // this may be ignored - get saved
      em.getTransaction().begin();

      jon.setFirstName("Jon");
      jon.setLastName("Luo");
      jon.setAnswer("YC High School");
      List<Role> newRoles = new ArrayList<Role>();
      newRoles.add(roles.get(2));
      newRoles.add(roles.get(0));
      jon.setRoles(newRoles);

      em.getTransaction().commit();
   }
}