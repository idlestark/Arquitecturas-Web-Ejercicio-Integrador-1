package factory;


import dao.ClienteDAO;
import dao.ProductoDAO;
import dao.FacturaDAO;
import dao.FacturaProductoDAO;

public abstract class AbstractFactory {

    public static final int MYSQL_JDBC = 1;
    public static final int DERBY_JDBC = 2;
    public abstract ClienteDAO getClienteDAO();
    public abstract ProductoDAO getProductoDAO();
    public abstract FacturaDAO getFacturaDAO();
    public abstract FacturaProductoDAO getFacturaProductoDAO();
    public static AbstractFactory getDAOFactory(int whichFactory){
        switch(whichFactory){
            case MYSQL_JDBC :{
                return MySQLDAOFactory.getInstance();
            }
            case DERBY_JDBC: {
                return DerbyFactory.getInstance();
            }
            default: return null;
        }
    }

}
