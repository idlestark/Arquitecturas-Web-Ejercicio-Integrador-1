package dao;

import entities.Factura;
import entities.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductoDAO {
    private Connection con;
    public void insert (Producto producto) {

        String query = "INSERT INTO Producto(idProducto, nombre, valor) VALUES(?, ?, ?)";

        PreparedStatement ps = null;

        try{
            ps = con.prepareStatement(query);
            ps.setInt(1, producto.getIdProducto());
            ps.setString(2, producto.getNombre());
            ps.setFloat(3, producto.getValor());
            ps.executeUpdate();
            System.out.println("Producto insertado correctamente.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally{
            try{
                if(ps != null){
                    ps.close();
                }
                con.commit();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void delete (int idProducto) {
        String query = "DELETE FROM producto WHERE idProducto = ?";

        PreparedStatement ps = null;

        try {
            ps = con.prepareStatement(query);
            ps.setInt(1, idProducto);
            ps.executeUpdate();
            System.out.println("El producto " + idProducto + " se eliminó correctamente.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally{
            try{
                if(ps != null){
                    ps.close();
                }
                con.commit();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void update (Producto producto){
        String query = "UPDATE INTO Producto(idFactura) WHERE idProducto = ?";
        PreparedStatement ps = null;
        try{
            ps = con.prepareStatement(query);
            ps.setInt(1, producto.getIdProducto());
            ps.executeUpdate();
            System.out.println("El producto con id: " + producto.getIdProducto() + "se actualizo correctamente");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            try{
                if(ps != null){
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
                salida = new Producto (pk, nombre, valor);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try{
                if(ps != null){
                    ps.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return salida;
    }


}
