package br.com.biblio.telas.telasGeral;

import br.com.biblio.dal.ModuloConexao;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.*;
import javax.swing.JOptionPane;
import java.time.temporal.ChronoUnit;

public class Multas {
Connection conexao = null;
PreparedStatement pst = null;
ResultSet rs = null;
ResultSet rsVerificar = null;
    public void verificarMultas(){
       conexao = ModuloConexao.conector();
       LocalDate hoje = java.time.LocalDate.now(); 
       String sql = "select * from livros";
       int testes = 16;
       try{
           pst = conexao.prepareStatement(sql);
           rsVerificar = pst.executeQuery();
           while(rsVerificar.next()){
               Date day = rsVerificar.getDate(9);
               if(day != null){
                   Date diaQueAlugou = rsVerificar.getDate(9);
                   System.out.println(diaQueAlugou);
                   LocalDate convDia = ((java.sql.Date) diaQueAlugou).toLocalDate();
                   long numDeDias = ChronoUnit.DAYS.between(hoje, convDia);
                   
                   if(testes > 7){
                       verificarCargo(rsVerificar.getString(8), testes);
                   }
               
               }else{
                   dateNull(rsVerificar.getString(2));
               }
           } 
       } catch(Exception e){
           JOptionPane.showMessageDialog(null,"Erro na função verificar multas " + e);
       }
    }
    private void dateNull(String isbn){
        String sql = "update livros set dia_que_alugou=? where isbn=?";
        try{
            pst = conexao.prepareStatement(sql);
            pst.setDate(1, null);
            pst.setString(2, isbn);
        } catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    private void verificarCargo(String user, long numDeDias){
        String sql = "select * from users where usuario=?";
        int dias = Math.toIntExact(numDeDias);
        try{
            pst = conexao.prepareStatement(sql);
            pst.setString(1, user);
            rs = pst.executeQuery();
            if(rs.next()){
                if("Professor".equals(rs.getString(5))){
                        verificarOutrasMultas(user, 15);
                }else if("Estudante".equals(rs.getString(5))){
                        verificarOutrasMultas(user, 7);
                }
            }
        } catch(Exception e){
            JOptionPane.showMessageDialog(null,e);
        }
    }
    private void verificarOutrasMultas(String user, int cargoDias){
        String sql = "select * from livros where user_que_alugou=?";
        int valorMulta = 0;
        
        LocalDate hoje = java.time.LocalDate.now();
        try{
            pst = conexao.prepareStatement(sql);
            pst.setString(1, user);
            rs = pst.executeQuery();
            while(rs.next()){
                Date diaQueAlugou = rs.getDate(9);
                LocalDate convDia = ((java.sql.Date) diaQueAlugou).toLocalDate();
                int daysBetweenDates = Math.toIntExact(ChronoUnit.DAYS.between(hoje, convDia));;
                if(cargoDias == 15 && daysBetweenDates > 15){
                    valorMulta = daysBetweenDates + valorMulta - cargoDias;
                    
                }else if(cargoDias == 7 && daysBetweenDates > 7){
                    valorMulta = daysBetweenDates + valorMulta - cargoDias;
                    System.out.println(valorMulta);
                }
            }
           
        } catch(Exception e){
            JOptionPane.showMessageDialog(null,e);
        }
        aplicarMulta(valorMulta, user);
    }
    private void aplicarMulta(int valorMulta, String user){
        String sql = "update users set multa=? where usuario=?";
        try{
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, valorMulta);
            pst.setString(2, user);
            pst.executeUpdate();
        } catch(Exception e){
            JOptionPane.showMessageDialog(null,e);
        }
    }
}
