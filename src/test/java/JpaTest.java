
import com.nigeeks.jpa.domain.Employee;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created on 16/02/15.
 */
public class JpaTest {


    private static EntityManager manager;
    public Employee employee = new Employee("me");
    private static EntityTransaction tx;

    @BeforeClass
    public static void beforeClass() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("persistenceUnit");
        manager = factory.createEntityManager();
    }
    
    @Before
    public void setUp(){
        tx = manager.getTransaction();
        assertNotNull(tx);
    }
    
    @After
    public void tearDown() {
        clearEmployeeTable();
    }

    @Test
    public void insertToDbTest() {
        tx.begin();
        manager.persist(employee);
        tx.commit();
        assertEquals(1, countEmployeeTable());
    }

    @Test
    public void clearDepartmentTableTest() {
        clearEmployeeTable();
        assertEquals(0, countEmployeeTable());
    }

    @Test
         public void removeFromDbTest() {
        tx.begin();
        manager.persist(employee);
        tx.commit();
        assertEquals(1, countEmployeeTable());

        tx.begin();
        manager.remove(employee);
        tx.commit();
        assertEquals(0, countEmployeeTable());
    }

    @Test
    public void removeFromEmptyTableTest() {
        tx.begin();
        manager.remove(employee);
        tx.commit();
        // does not exist and does not throw an Exception
        assertEquals(0, countEmployeeTable());
    }

    private int countEmployeeTable() {
        return manager.createQuery("Select d From Employee d", Employee.class).getResultList().size();
    }

    private void clearEmployeeTable() {
        tx.begin();
        Query query = manager.createQuery("Delete From Employee d");
        query.executeUpdate();
        tx.commit();
    }
}