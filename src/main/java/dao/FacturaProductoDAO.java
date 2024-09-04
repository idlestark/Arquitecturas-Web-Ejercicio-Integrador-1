package dao;

import entities.Cliente;
import entities.FacturaProducto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FacturaProductoDAO {

    private Connection conn;

    public FacturaProductoDAO(Connection conn){this.conn = conn;}

    public void insert(FacturaProducto factura){

        String query = "INSERT INTO factura_producto(idFactura, idProducto, cantidad) VALUES (?, ?, ?)";

        PreparedStatement ps = null;

        try{
            ps = conn.prepareStatement(query);
            ps.setInt(1, factura.getIdFactura());
            ps.setInt(2, factura.getIdProducto());
            ps.setInt(3, factura.getCantidad());
            ps.executeUpdate();
            System.out.println("Factura Producto insertada correctamente");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally{
            try{
                if(ps != null){
                    ps.close();
                }
                conn.commit();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void delete (int idFactura) {
        String query = "DELETE FROM facturaProducto WHERE idFactura = ?";

        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, idFactura);
            ps.executeUpdate();
            System.out.println("La factura " + idFactura + " se eliminó correctamente.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally{
            try{
                if(ps != null){
                    ps.close();
                }
                conn.commit();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void update(FacturaProducto facPro){
        String query = "UPDATE INTO factura_producto(cantidad) WHERE idFactura = ? AND idProducto = ?";
        PreparedStatement ps = null;

        try{
            ps = conn.prepareStatement(query);
            ps.setInt(3, facPro.getCantidad());
            ps.setInt(1, facPro.getIdFactura());
            ps.setInt(2, facPro.getIdProducto());
            ps.executeUpdate();
            System.out.println("Factura Producto actualizada correctamente");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally{
            try{
                if(ps != null){
                    ps.close();
                }
                conn.commit();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public FacturaProducto find(Integer fp) {
        String query = "SELECT * FROM FacturaProducto WHERE idFactura = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        FacturaProducto salida = null;
        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, fp);
            rs = ps.executeQuery();
            if (rs.next()) {
                Integer idProducto = rs.getInt("idProducto");
                Integer cantidad = rs.getInt("cantidad");
                salida = new FacturaProducto(fp, idProducto, cantidad);
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


