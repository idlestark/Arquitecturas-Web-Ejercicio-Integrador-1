package utils;
//CSV
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
//ENTIDADES
import entities.*;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;

public class HelperDerby {

    private Connection conn = null;

    public HelperDerby() {

        String driver = "org.apache.derby.jdbc.EmbeddedDriver";
        //String driver = "org.apache.derby.jdbc.ClientDriver";
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


    //CERRAR LA CONEXIÓN
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
        // CREACIÓN TABLA "PRODUCTO"
        try {
            String tableProducto = "CREATE TABLE Producto (" +
                    "idProducto INT NOT NULL, " +
                    "nombre VARCHAR(50), " +
                    "valor FLOAT, " +
                    "CONSTRAINT Producto PRIMARY KEY (idProducto))";
            this.conn.prepareStatement(tableProducto).execute();
            this.conn.commit();
        } catch (SQLException e) {
            if ("X0Y32".equals(e.getSQLState())) {
                System.out.println("La tabla 'Producto' ya existe.");
            } else {
                throw e;
            }
        }

        // CREACIÓN TABLA "CLIENTE"
        try {
            String tableCliente = "CREATE TABLE Cliente (" +
                    "idCliente INT NOT NULL, " +
                    "nombre VARCHAR(50), " +
                    "email VARCHAR(100), " +
                    "CONSTRAINT idCliente PRIMARY KEY (idCliente))";
            this.conn.prepareStatement(tableCliente).execute();
            this.conn.commit();
        } catch (SQLException e) {
            if ("X0Y32".equals(e.getSQLState())) {
                System.out.println("La tabla 'Cliente' ya existe.");
            } else {
                throw e;
            }
        }

        // CREACIÓN TABLA "FACTURA"
        try {
            String tableFactura = "CREATE TABLE Factura (" +
                    "idFactura INT NOT NULL, " +
                    "idCliente INT NOT NULL, " +
                    "CONSTRAINT idFactura PRIMARY KEY (idFactura), " +
                    "CONSTRAINT fk_idCliente FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente))";
            this.conn.prepareStatement(tableFactura).execute();
            this.conn.commit();
        } catch (SQLException e) {
            if ("X0Y32".equals(e.getSQLState())) {
                System.out.println("La tabla 'Factura' ya existe.");
            } else {
                throw e;
            }
        }

        // CREACIÓN TABLA "FACTURA-PRODUCTO"
        try {
            String tableFacturaProducto = "CREATE TABLE Factura_Producto (" +
                    "idFactura INT NOT NULL, " +
                    "idProducto INT NOT NULL, " +
                    "cantidad INT NOT NULL, " +
                    "CONSTRAINT fk_idFactura FOREIGN KEY (idFactura) REFERENCES Factura(idFactura), " +
                    "CONSTRAINT fk_idProducto FOREIGN KEY (idProducto) REFERENCES Producto(idProducto))";
            this.conn.prepareStatement(tableFacturaProducto).execute();
            this.conn.commit();
        } catch (SQLException e) {
            if ("X0Y32".equals(e.getSQLState())) {
                System.out.println("La tabla 'Factura_Producto' ya existe.");
            } else {
                throw e;
            }
        }
    }


    //OBTENER DATOS DE UN ARCHIVO .CSV
    private Iterable<CSVRecord> getData(String archivo) throws IOException {
        String path = "src\\main\\resources\\" + archivo;
        Reader in = new FileReader(path);
        String[] header = {};
        CSVParser csvParser = CSVFormat.EXCEL.withHeader(header).parse(in);

        Iterable<CSVRecord> records = csvParser.getRecords();
        return records;
    }

    //LLENADO DE TABLAS DE LA BASE DE DATOS
    public void populateDB() throws IOException, SQLException {
        System.out.println("Cargando datos...");
        for (CSVRecord row : getData("productos.csv")) {
            if (row.size() >= 3) {
                String idProducto = row.get(0);
                String nombre = row.get(1);
                String valor = row.get(2);
                if (!idProducto.isEmpty() && !nombre.isEmpty() && !valor.isEmpty()) {
                    try {
                        int idProductoInt = Integer.parseInt(idProducto);
                        float valorInt = Integer.parseInt(valor);
                        Producto producto = new Producto(idProductoInt, nombre, valorInt);
                        insertProducto(producto, conn);
                    } catch (Exception e) {
                        System.err.println("Error en la inserción de producto - " + e.getMessage());
                    }

                }
            }
        }
        System.out.println("Producto insertado con éxito");
        //POR CADA FILA DEL RESULTADO CSV RECORD...
        for (CSVRecord row : getData("clientes.csv")) {
            if (row.size() >= 3) {
                String idCliente = row.get(0);
                String nombre = row.get(1);
                String email = row.get(2);
                //SI LOS DATOS NO ESTÁN VACÍOS
                if (!idCliente.isEmpty() && !nombre.isEmpty() && !email.isEmpty()) {
                    try {
                        int id = Integer.parseInt(idCliente);
                        //SE CREA UN OBJETO CLIENTE CON LOS DATOS EXTRAÍDOS
                        Cliente client = new Cliente(id, nombre, email);
                        //Y SE INSERTA
                        insertCliente(client, conn);
                    } catch (Exception e) {
                        System.err.println("Error en la inserción de cliente - " + e.getMessage());
                    }
                }

            }
        }
        System.out.println("Clientes insertados con éxito");

        //MISMO PROCESO PARA EL RESTO DE LAS ENTIDADES...

        for (CSVRecord row : getData("facturas.csv")) {
            if (row.size() >= 2) {
                String idFactura = row.get(0);
                String idCliente = row.get(1);
                if (!idFactura.isEmpty() && !idCliente.isEmpty()) {
                    try {
                        int idFacturaInt = Integer.parseInt(idFactura);
                        int idClienteInt = Integer.parseInt(idCliente);
                        Factura factura = new Factura(idFacturaInt, idClienteInt);
                        insertFactura(factura, conn);
                    } catch (Exception e) {
                        System.err.println("Error en la inserción de factura - " + e.getMessage());
                    }

                }
            }
        }
        System.out.println("Facturas insertadas con éxito");

        for (CSVRecord row : getData("facturas-productos.csv")) {
            if (row.size() >= 3) {
                String idFactura = row.get(0);
                String idProducto = row.get(1);
                String cantidad = row.get(1);
                if (!idFactura.isEmpty() && !idProducto.isEmpty() && !cantidad.isEmpty()) {
                    try {
                        int idFacturaInt = Integer.parseInt(idFactura);
                        int idProdcutoInt = Integer.parseInt(idProducto);
                        int cantidadInt = Integer.parseInt(cantidad);
                        FacturaProducto facturaProd = new FacturaProducto(idFacturaInt, idProdcutoInt, cantidadInt);
                        insertFacturaProducto(facturaProd, conn);
                    } catch (Exception e) {
                        System.err.println("Error en la inserción de factura-producto - " + e.getMessage());
                    }

                }
            }
        }
        System.out.println("Facturas-productos insertadas con éxito");
    }


    //INSERTAR CLIENTE A LA BASE DE DATOS
    private int insertCliente(Cliente cliente, Connection conn) throws Exception {
        String insert = "INSERT INTO Cliente (idCliente, nombre, email) VALUES (?, ?, ?)";
        PreparedStatement ps = null;
        try{
            ps = conn.prepareStatement(insert);
            ps.setInt(1, cliente.getIdCliente());
            ps.setString(2, cliente.getNombre());
            ps.setString(3, cliente.getEmail());
            if(ps.executeUpdate() == 0){
                throw new Exception("Error en la inserción");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePsAndCommit(conn, ps);
        }
        return 0;
    }


    //INSERTAR FACTURA A LA BASE DE DATOS
    private int insertFactura(Factura factura, Connection conn) throws Exception {
        String insert = "INSERT INTO Factura (idFactura, idCliente) VALUES (?, ?)";
        PreparedStatement ps = null;
        try{
            ps = conn.prepareStatement(insert);
            ps.setInt(1, factura.getIdFactura());
            ps.setInt(2, factura.getIdCliente());
            if(ps.executeUpdate() == 0){
                throw new Exception("Error en la inserción");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePsAndCommit(conn, ps);
        }
        return 0;
    }


    //INSERTAR FACTURA-PRODUCTO A LA BASE DATOS
    private int insertFacturaProducto(FacturaProducto facturaProducto, Connection conn) throws Exception {
        String insert = "INSERT INTO Factura_Producto (idFactura, idProducto, cantidad) VALUES (?, ?, ?)";
        PreparedStatement ps = null;
        try{
            ps = conn.prepareStatement(insert);
            ps.setInt(1, facturaProducto.getIdFactura());
            ps.setInt(2, facturaProducto.getIdProducto());
            ps.setInt(3, facturaProducto.getCantidad());
            if(ps.executeUpdate() == 0){
                throw new Exception("Error en la inserción");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePsAndCommit(conn, ps);
        }
        return 0;
    }


    //INSERTAR PRODUCTO A LA BASE DE DATOS
    private int insertProducto(Producto producto, Connection conn) throws Exception {
        String insert = "INSERT INTO Producto (idProducto, nombre, valor) VALUES (?, ?, ?)";
        PreparedStatement ps = null;
        try{
            ps = conn.prepareStatement(insert);
            ps.setInt(1, producto.getIdProducto());
            ps.setString(2, producto.getNombre());
            ps.setFloat(3, producto.getValor());
            if(ps.executeUpdate() == 0){
                throw new Exception("Error en la inserción");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePsAndCommit(conn, ps);
        }
        return 0;
    }


    //CERRAR CONEXIÓN Y PREPARED STATEMENT
    private void closePsAndCommit(Connection conn, PreparedStatement ps) {
        if (conn != null){
            try {
                ps.close();
                conn.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    //BORRAR TODAS LAS TABLAS DE LA BASE DE DATOS
    public void dropTables() throws SQLException {


        try {
            String dropFacturaProducto = "DROP TABLE Factura_Producto";
            this.conn.prepareStatement(dropFacturaProducto).execute();
            this.conn.commit();
        }
        catch (SQLException e) {
            // Verifica si el estado SQL indica que la tabla no existe
            if ("42Y55".equals(e.getSQLState())) {
                // 42X05 es el SQLState para "Table does not exist"
                System.out.println("La tabla 'FacturaProducto' no existe");
            } else {
                // Si el error es diferente, relanza la excepción
                throw e;
            }
        }
        try {
            String dropFactura = "DROP TABLE Factura";
            this.conn.prepareStatement(dropFactura).execute();
            this.conn.commit();
        }
        catch (SQLException e) {
            // Verifica si el estado SQL indica que la tabla no existe
            if ("42Y55".equals(e.getSQLState())) {
                // 42X05 es el SQLState para "Table does not exist"
                System.out.println("La tabla 'Factura' no existe");
            } else {
                // Si el error es diferente, relanza la excepción
                throw e;
            }
        }

        //PRODUCTO
        try {
            String dropProducto = "DROP TABLE Producto";
            this.conn.prepareStatement(dropProducto).execute();
            this.conn.commit();
        }
        catch (SQLException e) {
            // Verifica si el estado SQL indica que la tabla no existe
            if ("42Y55".equals(e.getSQLState())) {
                // 42X05 es el SQLState para "Table does not exist"
                System.out.println("La tabla 'Producto' no existe");
            } else {
                // Si el error es diferente, relanza la excepción
                throw e;
            }
        }

        try {
            String dropCliente = "DROP TABLE Cliente";
            conn.prepareStatement(dropCliente).execute();
            conn.commit();
        }
        catch (SQLException e) {
            // Verifica si el estado SQL indica que la tabla no existe
            if (e.getSQLState().equals("42Y55")) {
                // 42X05 es el SQLState para "Table does not exist"
                System.out.println("La tabla 'Cliente' no existe");
            } else {
                // Si el error es diferente, relanza la excepción
                throw e;
            }
        }


    }

}
