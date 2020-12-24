package br.com.biblio.dal;

import java.sql.*;
public class ModuloConexao {
PreparedStatement pst = null;
ResultSet rs = null;
    public static Connection conector(){
        Statement stmt = null;
        java.sql.Connection conexao = null;
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3307";
        String user = "root";
        String password = "";
        try {
            Class.forName(driver);
            conexao = DriverManager.getConnection(url, user, password);
            stmt = conexao.createStatement();
            String sql = "CREATE DATABASE IF NOT EXISTS library";
            stmt.executeUpdate(sql);
            sql = "USE library";
            stmt.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS `users` (\n" +
                "  `firstname` varchar(20) DEFAULT NULL,\n" +
                "  `lastname` varchar(20) DEFAULT NULL,\n" +
                "  `usuario` varchar(10) DEFAULT NULL,\n" +
                "  `cargo` varchar(20) DEFAULT NULL,\n" +
                "  `multa` int(11) DEFAULT 0\n" +
                ")";
            stmt.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS `livros` (\n" +
                "  `id_livro` int(5) NOT NULL AUTO_INCREMENT,\n" +
                "  `isbn` varchar(13) DEFAULT NULL,\n" +
                "  `autores` varchar(30) DEFAULT NULL,\n" +
                "  `edicao` int(3) NOT NULL DEFAULT 1,\n" +
                "  `editora` varchar(30) DEFAULT NULL,\n" +
                "  `nome_livro` varchar(50) DEFAULT NULL,\n" +
                "  `ano` int(4) DEFAULT NULL,\n" +
                "  `user_que_alugou` varchar(30) DEFAULT NULL,\n" +
                "  `dia_que_alugou` datetime DEFAULT NULL,\n" +
                "  `user_que_reservou` varchar(30) DEFAULT NULL,\n" +
                "  PRIMARY KEY (`id_livro`)\n" +
                ")";
           stmt.executeUpdate(sql);
           return conexao;
        } catch (Exception e) {
            return null;
        }
    }
}
