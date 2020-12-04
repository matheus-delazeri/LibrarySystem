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
int maxDiasProf = 15;
int maxDiasEstud = 7;

    public void verificarMultas(){
       conexao = ModuloConexao.conector();
       String sql = "select * from livros";
       try{
           pst = conexao.prepareStatement(sql);
           rsVerificar = pst.executeQuery();
           while(rsVerificar.next()){
               Date day = rsVerificar.getDate(9);
               if(day != null){
                   verificarCargo(rsVerificar.getString(8));
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
    private void verificarCargo(String user){
        String sql = "select * from users where usuario=?";
        try{
            pst = conexao.prepareStatement(sql);
            pst.setString(1, user);
            rs = pst.executeQuery();
            if(rs.next()){
                if("Professor".equals(rs.getString(4))){
                        verificarOutrasMultas(user, maxDiasProf);
                }else if("Estudante".equals(rs.getString(4))){
                        verificarOutrasMultas(user, maxDiasEstud);
                }
            }
        } catch(Exception e){
            JOptionPane.showMessageDialog(null,e);
        }
    }
    private void verificarOutrasMultas(String user, int cargoMaxDias){
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
                int daysBetweenDates = Math.toIntExact(ChronoUnit.DAYS.between(hoje, convDia));
                if(cargoMaxDias == maxDiasProf && daysBetweenDates > maxDiasProf){
                    valorMulta = daysBetweenDates + valorMulta - cargoMaxDias;
                }else if(cargoMaxDias == maxDiasEstud && daysBetweenDates > maxDiasEstud){
                    valorMulta = daysBetweenDates + valorMulta - cargoMaxDias;
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
