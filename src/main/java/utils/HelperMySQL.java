package utils;

import entities.Cliente;
import entities.Factura;
import entities.FacturaProducto;
import entities.Producto;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.Reader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class HelperMySQL {

    private Connection conn;

    public HelperMySQL() {

        String driver = "com.mysql.cj.jdbc.Driver";
        String uri = "jdbc:mysql://localhost:3306/MySQL_INTEGRADOR1";
        String user = "root";
        String password = "";
        String skibidi = "toilet";

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

    public void closeConnection(){
        if(conn != null){
            try{
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void dropTables() throws SQLException {
        //CLIENTE
        String dropCliente = "DROP TABLE IF EXISTS Cliente";
        this.conn.prepareStatement(dropCliente).execute();
        this.conn.commit();
        //FACTURA
        String dropFactura = "DROP TABLE IF EXISTS Factura";
        this.conn.prepareStatement(dropFactura).execute();
        this.conn.commit();
        //FACTURA PRODUCTO
        String dropFacturaP = "DROP TABLE IF EXISTS Factura_producto";
        this.conn.prepareStatement(dropFacturaP).execute();
        this.conn.commit();
        //PRODUCTO
        String dropProducto = "DROP TABLE IF EXISTS Producto";
        this.conn.prepareStatement(dropProducto).execute();
        this.conn.commit();
    }

    public void createTables() throws SQLException {
        //CLIENTE
        String tablaCliente = "CREATE TABLE IF NOT EXISTS Cliente(idCliente INT NOT NULL"+
                "nombre VARCHAR(50)"+
                "email VARCHAR(100), CONSTRAINT idCliente PRIMARY KEY (idCliente))";

        this.conn.prepareStatement(tablaCliente).execute();
        this.conn.commit();
        //FACTURA
        String tablaFactura = "CREATE TABLE IF NOT EXISTS Factura(idFactura INT NOT NULL"+
                "idCliente INT NOT NULL, "+
                "CONSTRAINT idFactura PRIMARY KEY (idFactura))"+
                "CONSTRAINT idCliente FOREIGN KEY REFERENCES Cliente(idCliente)";
        this.conn.prepareStatement(tablaFactura).execute();
        this.conn.commit();
        //FACTURA PRODUCTO
        String tablaFacturaProducto = "CREATE TABLE IF NOT EXISTS Factura_Producto(idFactura INT NOT NULL"+
                "idProducto INT NOT NULL"+
                "cantidad INT";
        this.conn.prepareStatement(tablaFacturaProducto).execute();
        this.conn.commit();
        //PRODUCTO
        String tablaProducto = "CREATE TABLE IF NOT EXISTS Producto(idProducto INT NOT NULL"+
                "nombre VARCHAR(50)"+
                "valor FLOAT, CONSTRAINT idFactura PRIMARY KEY (idFactura))";
        this.conn.prepareStatement(tablaProducto).execute();
        this.conn.commit();
    }

    private Iterable<CSVRecord> getData(String archivo) throws IOException {
        String path = "src\\main\\resources\\" + archivo;
        Reader in = new FileReader(path);
        String [] header = {};
        CSVParser csvParser = CSVFormat.EXCEL.withHeader(header).parse(in);
        Iterable<CSVRecord> records = csvParser.getRecords();
        return records;
    }

    public void populateDB() throws IOException {
        System.out.println("populating db... (lopez puto)");
        //CLIENTE
        for(CSVRecord row: getData("clientes.CSV")){
            if(row.size() >=4) {
                String idCliente = row.get(0);
                String nombre = row.get(1);
                String email = row.get(2);
                if(!idCliente.isEmpty() && !nombre.isEmpty() && !email.isEmpty()){
                    try{
                        int idClienteInt = Integer.parseInt(idCliente);
                        Cliente cliente = new Cliente(idClienteInt, nombre, email);
                    } catch (NumberFormatException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        System.out.println("Clientes insertados (lopez puto)");
        //FACTURAS
        for(CSVRecord row: getData("facturas.CSV")){
            if(row.size() >= 4){
                String idFactura = row.get(0);
                String idCliente = row.get(1);
                if(!idFactura.isEmpty() && !idCliente.isEmpty()){
                    try{
                        int idFacturaInt = Integer.parseInt(idFactura);
                        int idClienteInt = Integer.parseInt(idCliente);
                        Factura factura = new Factura(idFacturaInt, idClienteInt);
                    } catch (NumberFormatException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        System.out.println("Facturas insertados (iñaki puto)");
        //FACTURAS PRODUCTO
        for (CSVRecord row: getData("facturas-productos.CSV")){
            if(row.size() >= 4){
                String idFactura = row.get(0);
                String idProducto = row.get(1);
                String cantidad = row.get(2);
                if(!idFactura.isEmpty() && !idProducto.isEmpty() && !cantidad.isEmpty()){
                    try{
                        int idFacturaInt = Integer.parseInt(idFactura);
                        int idClienteInt = Integer.parseInt(idProducto);
                        int cantidadInt = Integer.parseInt(cantidad);
                        FacturaProducto fp = new FacturaProducto(idFacturaInt, idClienteInt, cantidadInt);
                    } catch (NumberFormatException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        System.out.println("Facturas-producto insertados (esta materia esta re quebrada)");
        //PRODUCTOS
        for(CSVRecord row: getData("productos.CSV")){
            if(row.size() >= 4){
                String idFactura = row.get(0);
                String nombre = row.get(1);
                String valor = row.get(2);
                if(!idFactura.isEmpty() && !nombre.isEmpty() && valor.isEmpty()){
                    try{
                        int idFacturaInt = Integer.parseInt(idFactura);
                        float valorInt = Float.parseFloat(valor);
                        Producto producto = new Producto(idFacturaInt, nombre, valorInt);
                    }catch (NumberFormatException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        System.out.println("Productos insertados (me voy a matar)");
    }

    private int insertCliente(Cliente c, Connection conn)throws Exception{
        String query = "INSERT INTO cliente(idCliente, nombre, email) VALUES (?, ?, ?)";
        PreparedStatement ps = null;
        try{
            ps = conn.prepareStatement(query);
            ps.setInt(1, c.getIdCliente());
            ps.setString(2, c.getNombre());
            ps.setString(3, c.getEmail());
            if(ps.executeUpdate() == 0){
                throw new Exception("No se pudo insertar");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally{
             closePsConnection(conn, ps);
        }
        return 0;
    }

    private int insertFactura(Factura f, Connection conn)throws Exception{
        String query = "INSERT INTO factura(idFactura, idCliente) VALUES (?, ?)";
        PreparedStatement ps = null;
        try{
            ps = conn.prepareStatement(query);
            ps.setInt(1, f.getIdFactura());
            ps.setInt(2, f.getIdCliente());
            if(ps.executeUpdate() == 0){
                throw new Exception("No se pudo insertar");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally{
            closePsConnection(conn, ps);
        }
        return 0;
    }

    private int insertFacturaP(FacturaProducto fp, Connection conn)throws Exception{
        String query = "INSERT INTO factura-producto(idFactura, idProducto, cantidad) VALUES (?, ?, ?)";
        PreparedStatement ps = null;
        try{
            ps = conn.prepareStatement(query);
            ps.setInt(1, fp.getIdFactura());
            ps.setInt(2, fp.getIdProducto());
            ps.setInt(3, fp.getCantidad());
            if(ps.executeUpdate() == 0){
                throw new Exception("No se pudo insertar");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally{
            closePsConnection(conn, ps);
        }
        return 0;
    }

    private int insertProducto(Producto p, Connection conn)throws Exception{
        String query = "INSERT INTO Producto(idProducto, nombre, valor) VALUES (?, ?, ?)";
        PreparedStatement ps = null;
        try{
            ps = conn.prepareStatement(query);
            ps.setInt(1, p.getIdProducto());
            ps.setString(2, p.getNombre());
            ps.setFloat(3, p.getValor());
            if(ps.executeUpdate() == 0){
                throw new Exception("No se pudo insertar");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally{
            closePsConnection(conn, ps);
        }
        return 0;
    }

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

}
