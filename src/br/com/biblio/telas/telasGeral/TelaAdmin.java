/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.biblio.telas.telasGeral;

import java.sql.*;
import br.com.biblio.dal.ModuloConexao;
import java.time.*;
import java.time.temporal.ChronoUnit;
import javax.swing.JOptionPane;

import br.com.biblio.telas.telasGeral.Multas;
import br.com.biblio.telas.telasUsuario.*;
import br.com.biblio.telas.telasLivros.*;

public class TelaAdmin extends javax.swing.JFrame {
Connection conexao = null;
PreparedStatement pst = null;
ResultSet rs = null;
Multas multas = new Multas();
    public TelaAdmin() {
        multas.verificarMultas();
        initComponents();
        conexao = ModuloConexao.conector();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        btnGerencUsers = new javax.swing.JButton();
        btnGerencLivros = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("PAINEL ADMINISTRATIVO");

        btnGerencUsers.setText("GERENCIAR USUÁRIOS");
        btnGerencUsers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGerencUsersActionPerformed(evt);
            }
        });

        btnGerencLivros.setText("GERENCIAR LIVROS");
        btnGerencLivros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGerencLivrosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(94, 94, 94)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(btnGerencUsers, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGerencLivros, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(93, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(103, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(77, 77, 77)
                .addComponent(btnGerencUsers, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(84, 84, 84)
                .addComponent(btnGerencLivros, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(124, 124, 124))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnGerencUsersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGerencUsersActionPerformed
            setVisible(false);
            TelaAdminUsuarios adminUsers = new TelaAdminUsuarios();
            adminUsers.setVisible(true);
    }//GEN-LAST:event_btnGerencUsersActionPerformed
            
    private void btnGerencLivrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGerencLivrosActionPerformed
            setVisible(false);
            TelaAdminLivros adminLivros = new TelaAdminLivros();
            adminLivros.setVisible(true);
    }//GEN-LAST:event_btnGerencLivrosActionPerformed

    public static void main(String args[]) {
    
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaAdmin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGerencLivros;
    private javax.swing.JButton btnGerencUsers;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
