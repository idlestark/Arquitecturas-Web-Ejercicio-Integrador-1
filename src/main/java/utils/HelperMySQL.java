package utils;
//CSV
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
//ENTIDADES
import entities.*;

import java.io.IOException;
import java.io.Reader;
import java.io.FileReader;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;

public class HelperMySQL {

    private Connection conn;

    public HelperMySQL() {

        String driver = "com.mysql.cj.jdbc.Driver";
        String uri = "jdbc:mysql://localhost:3306/MySQL_INTEGRADOR1";
        String user = "root";
        String password = "admin";

        try{
            Class.forName(driver).getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        try{
            conn = DriverManager.getConnection(uri, user, password);
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


    //CREACIÓN DE TODAS LAS TABLAS
    public void createTables() throws SQLException {
        //PRODUCTO
        String tablaProducto = "CREATE TABLE IF NOT EXISTS Producto(" +
                "idProducto INT NOT NULL," +
                " nombre VARCHAR(50)," +
                " valor FLOAT," +
                " CONSTRAINT idProducto PRIMARY KEY (idProducto))";
        this.conn.prepareStatement(tablaProducto).execute();
        this.conn.commit();

        //CLIENTE
        String tablaCliente = "CREATE TABLE IF NOT EXISTS Cliente(idCliente INT NOT NULL, nombre VARCHAR(50), email VARCHAR(100), CONSTRAINT idCliente PRIMARY KEY (idCliente))";
        this.conn.prepareStatement(tablaCliente).execute();
        this.conn.commit();

        //FACTURA
        String tablaFactura = "CREATE TABLE IF NOT EXISTS Factura(idFactura INT NOT NULL, idCliente INT NOT NULL, CONSTRAINT idFactura PRIMARY KEY (idFactura), CONSTRAINT idCliente FOREIGN KEY (IdCliente) REFERENCES Cliente(idCliente))";
        this.conn.prepareStatement(tablaFactura).execute();
        this.conn.commit();

        //FACTURA PRODUCTO
        String tablaFacturaProducto = "CREATE TABLE IF NOT EXISTS Factura_Producto(idFactura INT NOT NULL, idProducto INT NOT NULL, cantidad INT NOT NULL, CONSTRAINT idFactura FOREIGN KEY (idFactura) REFERENCES Factura(idFactura), CONSTRAINT idProducto FOREIGN KEY (idProducto) REFERENCES Producto(idProducto))";
        this.conn.prepareStatement(tablaFacturaProducto).execute();
        this.conn.commit();

    }


    //OBTENER DATOS DE UN ARCHIVO .CSV
    private Iterable<CSVRecord> getData(String archivo) throws IOException {
        String path = "src\\main\\resources\\" + archivo;
        Reader in = new FileReader(path);
        String [] header = {};
        CSVParser csvParser = CSVFormat.EXCEL.withHeader(header).parse(in);
        Iterable<CSVRecord> records = csvParser.getRecords();
        return records;
    }


    //LLENADO DE TABLAS DE LA BASE DE DATOS
    public void populateDB() throws IOException {
        System.out.println("Cargando datos...");
        //PRODUCTOS
        for(CSVRecord row: getData("productos.csv")){
            if(row.size() >= 3){
                String idProducto = row.get(0);
                String nombre = row.get(1);
                String valor = row.get(2);
                if(!idProducto.isEmpty() && !nombre.isEmpty() && !valor.isEmpty()){
                    try{
                        int idProductoInt = Integer.parseInt(idProducto);
                        float valorInt = Float.parseFloat(valor);
                        Producto producto = new Producto(idProductoInt, nombre, valorInt);
                        insertProducto(producto, conn);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        System.out.println("Productos insertados con éxito");
        //CLIENTE
        for(CSVRecord row: getData("clientes.CSV")){
            if(row.size() >=3) {
                String idCliente = row.get(0);
                String nombre = row.get(1);
                String email = row.get(2);
                if(!idCliente.isEmpty() && !nombre.isEmpty() && !email.isEmpty()){
                    try{
                        int idClienteInt = Integer.parseInt(idCliente);
                        Cliente cliente = new Cliente(idClienteInt, nombre, email);
                        insertCliente(cliente, conn);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        System.out.println("Clientes insertados con éxito");
        //FACTURAS OREADAS
        for(CSVRecord row: getData("facturas.CSV")){
            if(row.size() >= 2){
                String idFactura = row.get(0);
                String idCliente = row.get(1);
                if(!idFactura.isEmpty() && !idCliente.isEmpty()){
                    try{
                        int idFacturaInt = Integer.parseInt(idFactura);
                        int idClienteInt = Integer.parseInt(idCliente);
                        Factura factura = new Factura(idFacturaInt, idClienteInt);
                        insertFactura(factura, conn);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        System.out.println("Facturas insertadas con éxito");
        //FACTURAS PRODUCTO
        for (CSVRecord row: getData("facturas-productos.CSV")){
            if(row.size() >= 3){
                String idFactura = row.get(0);
                String idProducto = row.get(1);
                String cantidad = row.get(2);
                if(!idFactura.isEmpty() && !idProducto.isEmpty() && !cantidad.isEmpty()){
                    try{
                        int idFacturaInt = Integer.parseInt(idFactura);
                        int idProductoInt = Integer.parseInt(idProducto);
                        int cantidadInt = Integer.parseInt(cantidad);
                        FacturaProducto fp = new FacturaProducto(idFacturaInt, idProductoInt, cantidadInt);
                        insertFacturaProducto(fp, conn);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        System.out.println("Facturas-productos insertados con éxito");
    }


    //INSERTAR CLIENTE A LA BASE DE DATOS
    private int insertCliente(Cliente c, Connection conn)throws Exception{
        String query = "INSERT INTO cliente (idCliente, nombre, email) VALUES (?, ?, ?)";
        PreparedStatement ps = null;
        try{
            ps = conn.prepareStatement(query);
            ps.setInt(1, c.getIdCliente());
            ps.setString(2, c.getNombre());
            ps.setString(3, c.getEmail());
            if(ps.executeUpdate() == 0){
                throw new Exception("Error en la inserción");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally{
             closePsConnection(conn, ps);
        }
        return 0;
    }


    //INSERTAR FACTURA A LA BASE DE DATOS
    private int insertFactura(Factura f, Connection conn)throws Exception{
        String query = "INSERT INTO factura (idFactura, idCliente) VALUES (?, ?)";
        PreparedStatement ps = null;
        try{
            ps = conn.prepareStatement(query);
            ps.setInt(1, f.getIdFactura());
            ps.setInt(2, f.getIdCliente());
            if(ps.executeUpdate() == 0){
                throw new Exception("Error en la inserción");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally{
            closePsConnection(conn, ps);
        }
        return 0;
    }


    //INSERTAR FACTURA-PRODUCTO A LA BASE DE DATOS
    private int insertFacturaProducto(FacturaProducto fp, Connection conn)throws Exception{
        String query = "INSERT INTO factura_producto (idFactura, idProducto, cantidad) VALUES (?, ?, ?)";
        PreparedStatement ps = null;
        try{
            ps = conn.prepareStatement(query);
            ps.setInt(1, fp.getIdFactura());
            ps.setInt(2, fp.getIdProducto());
            ps.setInt(3, fp.getCantidad());
            if(ps.executeUpdate() == 0){
                throw new Exception("Error en la inserción");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally{
            closePsConnection(conn, ps);
        }
        return 0;
    }


    //INSERTAR PRODUCTO A LA BASE DE DATOS
    private int insertProducto(Producto p, Connection conn)throws Exception{
        String query = "INSERT INTO Producto (idProducto, nombre, valor) VALUES (?, ?, ?)";
        PreparedStatement ps = null;
        try{
            ps = conn.prepareStatement(query);
            ps.setInt(1, p.getIdProducto());
            ps.setString(2, p.getNombre());
            ps.setFloat(3, p.getValor());
            if(ps.executeUpdate() == 0){
                throw new Exception("Error en la inserción");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally{
            closePsConnection(conn, ps);
        }
        return 0;
    }


    //CERRAR CONEXIÓN Y PREPARED STATEMENT
    private void closePsConnection(Connection conn, PreparedStatement ps){
        if(conn != null){
            try{
                ps.close();
                conn.commit();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    //BORRAR TODAS LAS TABLAS DE LA BASE DE DATOS
    public void dropTables() throws SQLException {
        //POR TEMAS DE LAS DEPENCIAS DE LAS TABLAS
        //DEBEN BORRARSE EN EL ORDEN CORRECTO
        //CLIENTE
        String dropCliente = "DROP TABLE IF EXISTS Cliente";
        //FACTURA
        String dropFactura = "DROP TABLE IF EXISTS Factura";
        //FACTURA PRODUCTO
        String dropFacturaP = "DROP TABLE IF EXISTS Factura_producto";
        //PRODUCTO
        String dropProducto = "DROP TABLE IF EXISTS Producto";
        this.conn.prepareStatement(dropFacturaP).execute();
        this.conn.prepareStatement(dropFactura).execute();
        this.conn.prepareStatement(dropProducto).execute();
        this.conn.prepareStatement(dropCliente).execute();
        this.conn.commit();
    }

}
