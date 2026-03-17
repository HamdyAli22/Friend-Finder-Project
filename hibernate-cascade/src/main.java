import com.hibernate.app.model.*;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import javax.persistence.Query;
import java.util.List;

public class main {
    public static void main(String[] args) {
        Configuration configuration = new Configuration()
                .configure("hibernate.cfg.xml")
               // .addAnnotatedClass(Student.class)
                .addAnnotatedClass(Students.class)
                .addAnnotatedClass(StudentDetails.class);
               // .addAnnotatedClass(Emails.class)
               // .addAnnotatedClass(Course.class);
        SessionFactory factory = configuration.buildSessionFactory();
        Session session = factory.getCurrentSession();
        //System.out.println("Current Session: " + session);
        Transaction transaction =  session.beginTransaction();

        //save
      /*  Students student = new Students("hamdy","1234");

        StudentDetails studentDetails = new StudentDetails("hamdy","alex","123456");

        student.setStudentDetails(studentDetails);

        session.persist(student);*/


       /* Student s2 = session.get(Student.class, 1);
        session.close(); // s2 أصبح Detached
        session.persist(s2);//❌ سيظهر TransientObjectException*/

        //update
     /*  Students student = session.get(Students.class,4);
        student.setUsername("hamdy updated");

        StudentDetails studentDetails = student.getStudentDetails();
        studentDetails.setAddress("Alex");
        session.update(student);*/

       // Students student = session.get(Students.class,4);

        //delete
       //Students student = session.get(Students.class,2);
        //session.remove(student);

        //detach
       /* Students student = session.get(Students.class,4);
        session.detach(student);
        student.setPassword("mmmm");
        student.getStudentDetails().setAddress("xxxx");
        session.merge(student);*/

     //   session.save(studentDetails);
      //  Student student = new Student(1,"ffff","cairo","88888");
      // session.update(student);
      //  Student student1 = session.get(Student.class,2);
      //  System.out.println(student1.toString());
      //  session.delete(student);
        //Student student2 = new Student("rrrr","cairo","7777");
       // session.save(student2);


        //Native SQL Query
       /* String sql = "SELECT student_name, address FROM student_details";
        Query query = session.createNativeQuery(sql);
        List<Object[]> results = query.getResultList();
        for (Object[] row : results) {
            String name = (String) row[0];
            String address = (String) row[1];

            System.out.println("Name: " + name + ", Address: " + address);
        }*/

        transaction.commit();

        factory.close();
        session.close();
    }
}
