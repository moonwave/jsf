package org.moonwave.jpa.query;


import java.util.List;

import org.moonwave.jpa.bo.GenericBO;
import org.moonwave.jpa.model.Role;
import org.moonwave.jpa.model.Semester;
import org.moonwave.jpa.model.Week;

public class GenericTypeTest {

   public static void main( String[ ] args ) {
   
      GenericBO<Semester> bo = new GenericBO<Semester>(Semester.class);
      Class cls = bo.getClassType();
      System.out.println(cls.getName());
      System.out.println(cls.getSimpleName());

      List<Semester> list = bo.findAll();
      for (Semester s : list) {
          System.out.println(s);
      }

      System.out.println("---------------------------------------");
      System.out.println(bo.findById((short)3));

      System.out.println("===================================================");
      GenericBO<Week> bo1 = new GenericBO<>(Week.class);
      cls = bo1.getClassType();
      System.out.println(cls.getName());
      System.out.println(cls.getSimpleName());

      List<Week> list1 = bo1.findAll();
      for (Week s : list1) {
          System.out.println(s);
      }
      System.out.println("---------------------------------------");
      System.out.println(bo1.findById((short)3));

      System.out.println("===================================================");
      GenericBO<Role> bo2 = new GenericBO<>(Role.class);
      cls = bo2.getClassType();
      System.out.println(cls.getName());
      System.out.println(cls.getSimpleName());

      List<Role> list2 = bo2.findAll();
      for (Role s : list2) {
          System.out.println(s);
      }

      System.out.println("---------------------------------------");
      System.out.println(bo.findById((short)3));
   }
}