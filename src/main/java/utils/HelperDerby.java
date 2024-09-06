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

    private Iterable<CSVRecord> getData(String archivo) throws IOException {
        String path = "src\\main\\resources\\" + archivo;
        Reader in = new FileReader(path);
        String[] header = {};
        CSVParser csvParser = CSVFormat.EXCEL.withHeader(header).parse(in);

        Iterable<CSVRecord> records = csvParser.getRecords();
        return records;
    }

    public void populateDB() throws IOException, SQLException {
        System.out.println("Populating DB...");

        for (CSVRecord row : getData("clientes.csv")) {
            if (row.size() >= 4) {
                String idCliente = row.get(0);
                String nombre = row.get(1);
                String email = row.get(2);
                if (!idCliente.isEmpty() && !nombre.isEmpty() && !email.isEmpty()) {
                    try {
                        int id = Integer.parseInt(idCliente);
                        Cliente client = new Cliente(id, nombre, email);
                        insertCliente(client, conn);
                    } catch (Exception e) {
                        System.err.println("Error de formato en datos de dirección: " + e.getMessage());
                    }
                }

            }
        }
        System.out.println("clientes insertadas");

        for (CSVRecord row : getData("facturas.csv")) {
            if (row.size() >= 4) {
                String idFactura = row.get(0);
                String idCliente = row.get(1);

                if (!idFactura.isEmpty() && !idCliente.isEmpty()) {
                    try {
                        int idFactur = Integer.parseInt(idFactura);
                        int idClient = Integer.parseInt(idCliente);

                        Factura factura = new factura(idFactur, idClient);
                        System.out.println(factura.getIdFactura());
                        insertFactura(factura, conn);
                    } catch (Exception e) {
                        System.err.println("Error de formato en datos de persona: " + e.getMessage());
                    }

                }
            }
        }
        System.out.println("Factura insertadas");

        for (CSVRecord row : getData("facturas-productos.csv")) {
            if (row.size() >= 4) {
                String idFactura = row.get(0);
                String idProducto = row.get(1);
                String cantidad = row.get(1);

                if (!idFactura.isEmpty() && !idProducto.isEmpty() && !cantidad.isEmpty()) {
                    try {
                        int idFactur = Integer.parseInt(idFactura);
                        int idProdcut = Integer.parseInt(idProducto);
                        int cantida = Integer.parseInt(cantidad);

                        FacturaProducto facturap = new facturap(idFactur, idProdcut, cantida);
                        System.out.println(facturap.getIdFactura());
                        insertFacturaProducto(factura, conn);
                    } catch (Exception e) {
                        System.err.println("Error de formato en datos de persona: " + e.getMessage());
                    }

                }
            }
        }
        System.out.println("Factura producto insertadas");

        for (CSVRecord row : getData("productos.csv")) {
            if (row.size() >= 4) {
                String idProducto = row.get(0);
                String nombre = row.get(1);
                String valor = row.get(1);

                if (!idProducto.isEmpty() && !nombre.isEmpty() && !valor.isEmpty()) {
                    try {
                        int idProduct = Integer.parseInt(idProducto);
                        float valo = Integer.parseInt(valor);

                        Producto producto = new producto(idProduct, nombre, valo);
                        System.out.println(producto.getIdFactura());
                        insertFacturaProducto(producto, conn);
                    } catch (Exception e) {
                        System.err.println("Error de formato en datos de persona: " + e.getMessage());
                    }

                }
            }
        }
        System.out.println("Factura producto insertadas");
    }


    private int insertCliente(Cliente cliente, Connection conn) throws Exception {
        String insert = "INSERT INTO Cliente (idCliente, nombre, email) VALUES (?, ?, ?)";
        PreparedStatement ps = null;
        try{
            ps = conn.prepareStatement(insert);
            ps.setInt(1, cliente.getIdCliente());
            ps.setString(2, cliente.getNombre());
            ps.setInt(3, cliente.getEmail());
            if(ps.executeUpdate() == 0){
                throw new Exception("No se pudo insertar");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePsAndCommit(conn, ps);
        }
        return 0;
    }

    private int insertFactura(Factura factura, Connection conn) throws Exception {
        String insert = "INSERT INTO Factura (idFactura, idCliente) VALUES (?, ?)";
        PreparedStatement ps = null;
        try{
            ps = conn.prepareStatement(insert);
            ps.setInt(1, factura.getFactura());
            ps.setString(2, factura.getIdCliente());
            if(ps.executeUpdate() == 0){
                throw new Exception("No se pudo insertar");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePsAndCommit(conn, ps);
        }
        return 0;
    }

    private int insertFacturaProducto(FacturaProducto facturaProducto, Connection conn) throws Exception {
        String insert = "INSERT INTO FacturaProducto (idFactura, idProducto, cantidad) VALUES (?, ?, ?)";
        PreparedStatement ps = null;
        try{
            ps = conn.prepareStatement(insert);
            ps.setInt(1, facturaProducto.getIdFactura());
            ps.setInt(2, facturaProducto.getIdProducto());
            ps.setInt(3, facturaProducto.getCantidad());
            if(ps.executeUpdate() == 0){
                throw new Exception("No se pudo insertar");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePsAndCommit(conn, ps);
        }
        return 0;
    }

    private int insertFacturaProducto(Producto producto, Connection conn) throws Exception {
        String insert = "INSERT INTO Producto (idProducto, nombre, valor) VALUES (?, ?, ?)";
        PreparedStatement ps = null;
        try{
            ps = conn.prepareStatement(insert);
            ps.setInt(1, producto.getIdProducto());
            ps.setString(2, producto.getNombre());
            ps.setFloat(3, producto.getValor());
            if(ps.executeUpdate() == 0){
                throw new Exception("No se pudo insertar");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePsAndCommit(conn, ps);
        }
        return 0;
    }

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





    public void dropTables() throws SQLException {

        try {
            String dropCliente = "DROP TABLE Cliente";
            conn.prepareStatement(dropCliente).execute();
            conn.commit();
        } catch (SQLException e) {
            // Verifica si el estado SQL indica que la tabla no existe
            if (e.getSQLState().equals("42Y55")) {
                // 42X05 es el SQLState para "Table does not exist"
                System.out.println("Table  Cliente does not exist.");
            } else {
                // Si el error es diferente, relanza la excepción
                throw e;
            }
        }

        try {
            String dropFactura = "DROP TABLE Factura";
            this.conn.prepareStatement(dropFactura).execute();
            this.conn.commit();
        }catch (SQLException e) {
            // Verifica si el estado SQL indica que la tabla no existe
            if ("42Y55".equals(e.getSQLState())) {
                // 42X05 es el SQLState para "Table does not exist"
                System.out.println("Table  direccion  does not exist.");
            } else {
                // Si el error es diferente, relanza la excepción
                throw e;
            }
        }

        try {
            String dropFacturaProducto = "DROP TABLE FacturaProducto";
            this.conn.prepareStatement(dropFacturaProducto).execute();
            this.conn.commit();
        }catch (SQLException e) {
            // Verifica si el estado SQL indica que la tabla no existe
            if ("42Y55".equals(e.getSQLState())) {
                // 42X05 es el SQLState para "Table does not exist"
                System.out.println("Table  direccion  does not exist.");
            } else {
                // Si el error es diferente, relanza la excepción
                throw e;
            }
        }

        try {
            String dropProducto = "DROP TABLE Producto";
            this.conn.prepareStatement(dropProducto).execute();
            this.conn.commit();
        }catch (SQLException e) {
            // Verifica si el estado SQL indica que la tabla no existe
            if ("42Y55".equals(e.getSQLState())) {
                // 42X05 es el SQLState para "Table does not exist"
                System.out.println("Table  direccion  does not exist.");
            } else {
                // Si el error es diferente, relanza la excepción
                throw e;
            }
        }

    }


}
