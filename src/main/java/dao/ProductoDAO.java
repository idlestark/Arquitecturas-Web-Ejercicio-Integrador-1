package dao;

import entities.FacturaProducto;
import entities.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProductoDAO {
    private Connection con;

    public ProductoDAO(Connection con) {
        this.con = con;
    }

    public void insert(Producto producto) {

        String query = "INSERT INTO Producto(idProducto, nombre, valor) VALUES(?, ?, ?)";

        PreparedStatement ps = null;

        try {
            ps = con.prepareStatement(query);
            ps.setInt(1, producto.getIdProducto());
            ps.setString(2, producto.getNombre());
            ps.setFloat(3, producto.getValor());
            ps.executeUpdate();
            System.out.println("Producto insertado correctamente.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                con.commit();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void delete(int idProducto) {
        String query = "DELETE FROM producto WHERE idProducto = ?";

        PreparedStatement ps = null;

        try {
            ps = con.prepareStatement(query);
            ps.setInt(1, idProducto);
            ps.executeUpdate();
            System.out.println("El producto " + idProducto + " se eliminó correctamente.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                con.commit();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void update(Producto producto) {
        String query = "UPDATE INTO Producto(idFactura) WHERE idProducto = ?";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(query);
            ps.setInt(1, producto.getIdProducto());
            ps.executeUpdate();
            System.out.println("El producto con id: " + producto.getIdProducto() + "se actualizo correctamente");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                con.commit();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Producto find(Integer pk) {
        String query = "SELECT * FROM Producto WHERE idProducto = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        Producto salida = null;
        try {
            ps = con.prepareStatement(query);
            ps.setInt(1, pk);
            rs = ps.executeQuery();
            if (rs.next()) {
                String nombre = rs.getString("nombre");
                float valor = rs.getFloat("valor");
                salida = new Producto(pk, nombre, valor);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return salida;
    }


    //EJERCICIO 3
    //Escriba un programa JDBC que retorne el producto que más recaudó. Se define
    //“recaudación” como cantidad de productos vendidos multiplicado por su valor.


    public Producto ejercicio3() {
        String query = "SELECT p.idProducto, p.nombre, p.valor, SUM(f.cantidad * p.valor) AS total FROM Producto p " +
                "INNER JOIN Factura_Producto f ON p.idProducto = f.idProducto " +
                "GROUP BY p.idProducto ORDER BY total DESC LIMIT 1";
        PreparedStatement ps = null;
        ResultSet rs = null;
        Producto produ = null;
        try {
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            int idProducto = rs.getInt("idProducto");
            String nombre = rs.getString("nombre");
            int valor = rs.getInt("valor");
            produ = new Producto(idProducto, nombre, valor);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return produ;
        }

    }
}
