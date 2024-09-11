package factory;

import dao.ClienteDAO;
import dao.FacturaDAO;
import dao.FacturaProductoDAO;
import dao.ProductoDAO;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DerbyFactory extends AbstractFactory {

    private static DerbyFactory instance = null;

    private static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    private static final String uri = "jdbc:derby:INTEGRADOR1;create=true";
    public static Connection conn;

    private DerbyFactory(){

    }

    public static synchronized DerbyFactory getInstance(){
        if(instance == null){
            instance = new DerbyFactory();
        }
        return instance;
    }

    public static Connection getConnection(){
        if(conn != null){
            return conn;
        }
        String driver = DRIVER;
        try{
            Class.forName(driver).getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        try {
            conn = DriverManager.getConnection(uri);
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return conn;
    }

    public void closeCon(){
        try{
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ClienteDAO getClienteDAO() {
        return new ClienteDAO(getConnection());
    }

    public FacturaDAO getFacturaDAO() {
        return new FacturaDAO(getConnection());
    }

    public FacturaProductoDAO getFacturaProductoDAO() {
        return new FacturaProductoDAO(getConnection());
    }

    public ProductoDAO getProductoDAO() {
        return new ProductoDAO(getConnection());
    }

}
