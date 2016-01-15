package org.moonwave.jpa.query;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.moonwave.jpa.bo.UserBO;
import org.moonwave.jpa.model.Role;
import org.moonwave.jpa.model.Semester;
import org.moonwave.jpa.model.SemesterWeek;
import org.moonwave.jpa.model.TutorGroup;
import org.moonwave.jpa.model.User;

public class QueryTest {

   public static void main( String[ ] args ) {
   
      EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "jpa-mysql" );
      EntityManager em = emfactory.createEntityManager();
      System.out.println("");

      List<User> users = new UserBO().findActiveUsers();
      users = new UserBO().findActiveGenericUsers();

      User user = new UserBO().findByLoginId("jliu");
      System.out.println(user);
      System.out.println("");

      user = new UserBO().findById(3);
      System.out.println(user);
      System.out.println("");

      List<Integer> userIds = new ArrayList<>();
      userIds.add(3);
      userIds.add(4);
      userIds.add(5);
      users = new UserBO().findInIds(userIds);
      for (User u : users) {
          System.out.println(u);
      }
      System.out.println("");

      userIds = new ArrayList<>();
      userIds.add(5);
      users = new UserBO().findInIds(userIds);
      for (User u : users) {
          System.out.println(u);
      }
      System.out.println("");

      // TutorGroup
      Query query = em.createQuery("Select e from TutorGroup e");
      List<TutorGroup> list = query.getResultList();

      for(TutorGroup e:list) {
         System.out.println("Tutor Group: "+ e.getAlias() + ", " + e.getName());
      }
      System.out.println("");

      // Semester
      query = em.createQuery("Select e from Semester e");
      List<Semester> semesters = query.getResultList();

      for(Semester e: semesters) {
         System.out.println("Semester: "+ e.getAlias() + ", " + e.getName());
      }
      System.out.println("");

      // Role
      query = em.createQuery("Select e from Role e");
      List<Role> roles = query.getResultList();

      for(Role e: roles) {
         System.out.println("Role: "+ e.getAlias() + ", " + e.getName());
      }
      System.out.println("");

      // SemesterWeek
      query = em.createQuery("Select e from SemesterWeek e");
      List<SemesterWeek> semesterWeeks = query.getResultList();

      for(SemesterWeek e: semesterWeeks) {
         System.out.println("SemesterWeek: "+ e.getSemester() + ", " + e.getWeek());
      }

   }
}