package utils;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.lang.reflect.InvocationTargetException;

public class HelperDerby {

    private Connection conn = null;

    public HelperDerby() {

        String driver = "org.apache.derby.jdbc.ClientDriver";
        String uri = "jdbc:derby:INTEGRADOR1;create=true";

        try {
            conn = DriverManager.getConnection(uri);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try{

            Class.forName(driver).getDeclaredConstructor().newInstance();

        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            e.printStackTrace();
            System.exit(1);
        }

        try{
            conn = DriverManager.getConnection(uri);
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeConnection(){
        if(conn != null){
            try{
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public void createTables() throws SQLException {
        try{
            String tableCliente = "CREATE TABLE cliente("+
                    "idCliente INT NOT NULL, "+
                    "nombre VARCHAR(50), "+
                    "email VARCHAR(50), "+
                    "CONSTRAINT cliente PRIMARY KEY (idCliente))";
            this.conn.prepareStatement(tableCliente).execute();
            this.conn.commit();
        }catch (SQLException e) {
            // Verifica si el estado SQL indica que la tabla no existe
            if ("X0Y32".equals(e.getSQLState())) {
                // 42X05 es el SQLState para "Table does not exist"
                System.out.println("Table  Cliente  does not exist.");
            } else {
                // Si el error es diferente, relanza la excepción
                throw e;
            }
        }

        try {
            String tableFactura = "CREATE TABLE  factura(" +
                    "idFactura INT NOT NULL, " +
                    "idCliente int NOT NULL,, " +
                    "CONSTRAINT factura PRIMARY KEY (idFactura), " +
                    "CONSTRAINT factura FOREIGN KEY (idCliente) REFERENCES cliente (idCliente))";
            this.conn.prepareStatement(tableFactura).execute();
            this.conn.commit();
        }catch (SQLException e) {
            // Verifica si el estado SQL indica que la tabla no existe
            if ("X0Y32".equals(e.getSQLState())) {
                // 42X05 es el SQLState para "Table does not exist"
                System.out.println("Table  factura  does not exist.");
            } else {
                // Si el error es diferente, relanza la excepción
                throw e;
            }
        }

        try{
            String tableFacturaProducto = "CREATE TABLE facturaProducto("+
                    "idFactura INT NOT NULL, "+
                    "idProducto INT NOT NULL, "+
                    "cantidad INT NOT NULL, ";
            this.conn.prepareStatement(tableFacturaProducto).execute();
            this.conn.commit();
        }catch (SQLException e) {
            // Verifica si el estado SQL indica que la tabla no existe
            if ("X0Y32".equals(e.getSQLState())) {
                // 42X05 es el SQLState para "Table does not exist"
                System.out.println("Table  factura producto  does not exist.");
            } else {
                // Si el error es diferente, relanza la excepción
                throw e;
            }
        }

        try{
            String tableFacturaProducto = "CREATE TABLE Producto("+
                    "idProducto INT NOT NULL, "+
                    "nombre VARCHAR(50), "+
                    "valor FLOAT NOT NULL, "+
                    "CONSTRAINT Producto PRIMARY KEY (idProducto))";
            this.conn.prepareStatement(tableFacturaProducto).execute();
            this.conn.commit();
        }catch (SQLException e) {
            // Verifica si el estado SQL indica que la tabla no existe
            if ("X0Y32".equals(e.getSQLState())) {
                // 42X05 es el SQLState para "Table does not exist"
                System.out.println("Table producto does not exist.");
            } else {
                // Si el error es diferente, relanza la excepción
                throw e;
            }
        }
    }






}
