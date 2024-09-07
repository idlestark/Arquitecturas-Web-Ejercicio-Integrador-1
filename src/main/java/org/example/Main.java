package org.example;

import dao.ClienteDAO;
import dao.FacturaDAO;
import dao.FacturaProductoDAO;
import dao.ProductoDAO;
import factory.AbstractFactory;
import utils.HelperMySQL;
import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, IOException {

        System.out.println("Hello world! voy a poner el nombre de esta materia en mi nota de suicidio");

        System.out.println("BASE DE DATOS SQL");

        HelperMySQL dbMysql= new HelperMySQL();
        dbMysql.dropTables();
        dbMysql.createTables();
        dbMysql.populateDB();
        dbMysql.closeConnection();

        AbstractFactory MysqlFactory = AbstractFactory.getDAOFactory(1);
        //NEGROS DE MIERDAAAAAAAAAAAAAAAAAAAAAAA
        System.out.println();
        System.out.println("////////////////////////////////////////////");
        System.out.println("////////////////////////////////////////////");
        System.out.println();

        ClienteDAO cliente = MysqlFactory.getClienteDAO();
        FacturaDAO factura = MysqlFactory.getFacturaDAO();
        FacturaProductoDAO fap = MysqlFactory.getFacturaProductoDAO();
        ProductoDAO producto = MysqlFactory.getProductoDAO();

        System.out.println();
        System.out.println("////////////////////////////////////////////");
        System.out.println("////////////////////////////////////////////");
        System.out.println();

        //EJERCICIO 3
        System.out.println(producto.ejercicio3().toString());
    }
}