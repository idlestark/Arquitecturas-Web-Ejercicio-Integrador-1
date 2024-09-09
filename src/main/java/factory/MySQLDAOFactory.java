package factory;

import dao.ClienteDAO;
import dao.FacturaDAO;
import dao.FacturaProductoDAO;
import dao.ProductoDAO;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLDAOFactory extends AbstractFactory {


    private static MySQLDAOFactory instance = null;

    public static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String uri = "jdbc:mysql://localhost:3306/MySQL_INTEGRADOR1";
    public static Connection conn;

    private MySQLDAOFactory() {

    }

    public static synchronized MySQLDAOFactory getInstance() {
        if (instance == null) {
            instance = new MySQLDAOFactory();
        }
        return instance;
    }

    public static Connection createConnection() {
        if (conn != null) {
            return conn;
        }
        String driver = DRIVER;
        try {
            Class.forName(driver).getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            e.printStackTrace();
            System.exit(1);
        }

        try {
            conn = DriverManager.getConnection(uri, "root", "dragonoidEV19");
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return conn;
    }

    public void closeConnection() {
        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public FacturaDAO getFacturaDAO() {
        return new FacturaDAO(createConnection());
    }

    @Override
    public FacturaProductoDAO getFacturaProductoDAO() {
        return new FacturaProductoDAO(createConnection());
    }

    public ClienteDAO getClienteDAO() {
        return new ClienteDAO(createConnection());
    }

    public ProductoDAO getProductoDAO() {
        return new ProductoDAO(createConnection());
    }
    //  @Override
    // public PersonaDAO getPersonaDAO() {
    //   return new PersonaDAO(createConnection());
    // }

    // @Override
    //public DireccionDAO getDireccionDAO() {
    //   return new DireccionDAO(createConnection());

}
