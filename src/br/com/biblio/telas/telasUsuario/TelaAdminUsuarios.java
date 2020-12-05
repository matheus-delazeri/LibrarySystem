package br.com.biblio.telas.telasUsuario;

import java.sql.*;
import br.com.biblio.dal.ModuloConexao;
import javax.swing.JOptionPane;
import javax.swing.table.*;

import br.com.biblio.telas.telasGeral.*;

public class TelaAdminUsuarios extends javax.swing.JFrame {
Connection conexao = null;
PreparedStatement pst = null;
ResultSet rs = null;  
    public TelaAdminUsuarios() {
        initComponents();
        conexao = ModuloConexao.conector();
        listarUsuarios();
    }
    private void listarUsuarios(){
        String sql ="select * from users";
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            
            DefaultTableModel modeloTabela = (DefaultTableModel)tableUsers.getModel(); 
            modeloTabela.setRowCount(0);       
            while (rs.next()) {
		modeloTabela.addRow(new String[] {rs.getString("firstname"), rs.getString("lastname"), rs.getString("cargo"), rs.getString("usuario") });
            }
        } catch (Exception e) {
                JOptionPane.showMessageDialog(null,e);
        }
    }
    private void deletarTodosUsuarios(){
        int confirma=JOptionPane.showConfirmDialog(null, "Tem certeza que deseja deletar todos usuários?");
        if (confirma==JOptionPane.YES_OPTION){
            String sql = "delete from users";
            try {
                pst = conexao.prepareStatement(sql);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Os usuários foram excluídos!");
                listarUsuarios();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Não foi possível excluir os usuários");
            }
        }
    }
    private void deletarUsuarioSelecionado(){
        DefaultTableModel modeloTabela = (DefaultTableModel)tableUsers.getModel(); 
        int row = tableUsers.getSelectedRow();
        if(row == -1){
            JOptionPane.showMessageDialog(null, "Selecione a linha do usuário que deseja apagar!");
        }else{
            Object user = modeloTabela.getValueAt(row, 3);
            String strUser = user.toString();

            int confirma=JOptionPane.showConfirmDialog(null, "Tem certeza que deseja deletar o usuário "+strUser+" do banco de dados?");
            if (confirma==JOptionPane.YES_OPTION){
                String sql ="delete from users where usuario=?";
                try {
                    pst = conexao.prepareStatement(sql);
                    pst.setString(1,strUser);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "O usuário foi excluído!");
                    listarUsuarios();  
                } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Não foi possível excluir o usuário");
                }
            }
        }
    }
    private void abrirInfoUser(){
        DefaultTableModel modeloTabela = (DefaultTableModel)tableUsers.getModel(); 
        int row = tableUsers.getSelectedRow();
        if(row == -1){
            JOptionPane.showMessageDialog(null, "Selecione a linha do usuário que deseja ver as informações!");
        }else{
            Object user = modeloTabela.getValueAt(row, 3);
            String strUser = user.toString();
            
            setVisible(false);
            TelaInfoUsuario telaInfoUsuario = new TelaInfoUsuario(strUser);
            telaInfoUsuario.setVisible(true);
        }
    }
    private void buscarUsuario(){
        String sql ="select * from users where usuario like ? or firstname like ? or lastname like ?";
        try {
            pst = conexao.prepareStatement(sql);
                if(searchField.getText().equals("")){
                    listarUsuarios();
                }else{
                    pst.setString(1,"%"+searchField.getText()+"%");
                    pst.setString(2,"%"+searchField.getText()+"%");
                    pst.setString(3,"%"+searchField.getText()+"%");
                    rs = pst.executeQuery();

                    DefaultTableModel modeloTabela = (DefaultTableModel)tableUsers.getModel(); 
                    modeloTabela.setRowCount(0);       
                    if (rs.next()) {
                        do{
                            modeloTabela.addRow(new String[] {rs.getString("firstname"), rs.getString("lastname"), rs.getString("cargo"), rs.getString("usuario") });
                        }while(rs.next());
                    }else{
                        JOptionPane.showMessageDialog(null,"Nenhum usuário cadastrado com essas informações! Tente novamente!");
                    }
                }
        } catch (Exception e) {
                JOptionPane.showMessageDialog(null,e);
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFrame1 = new javax.swing.JFrame();
        btnDeleteAllUsers = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableUsers = new javax.swing.JTable();
        btnDeleteUser = new javax.swing.JButton();
        btnVoltar = new javax.swing.JButton();
        searchField = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        btnInfoUser = new javax.swing.JButton();
        btnCadUsuario = new javax.swing.JButton();

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Painel Administrativo de Usuários");
        setBackground(java.awt.SystemColor.activeCaption);
        setResizable(false);
        setSize(new java.awt.Dimension(100, 300));

        btnDeleteAllUsers.setText("Deletar todos usuários");
        btnDeleteAllUsers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteAllUsersActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("GERENCIAMENTO DE USUÁRIOS");

        tableUsers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nome", "Sobrenome", "Profissão", "Usuário"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableUsers.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tableUsers.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tableUsers);

        btnDeleteUser.setText("Deletar usuário selecionado");
        btnDeleteUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteUserActionPerformed(evt);
            }
        });

        btnVoltar.setText("Voltar");
        btnVoltar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVoltarActionPerformed(evt);
            }
        });

        btnSearch.setText("Buscar");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        btnInfoUser.setText("Abrir operações para o usuário selecionado");
        btnInfoUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInfoUserActionPerformed(evt);
            }
        });

        btnCadUsuario.setText("Cadastrar novo usuário");
        btnCadUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadUsuarioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnVoltar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCadUsuario)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnInfoUser)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDeleteUser)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDeleteAllUsers))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(284, 284, 284)
                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 407, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(308, Short.MAX_VALUE))
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jLabel1)
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch))
                .addGap(27, 27, 27)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 467, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnVoltar)
                    .addComponent(btnDeleteAllUsers)
                    .addComponent(btnDeleteUser)
                    .addComponent(btnInfoUser)
                    .addComponent(btnCadUsuario))
                .addGap(22, 22, 22))
        );

        setSize(new java.awt.Dimension(1133, 739));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnDeleteAllUsersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteAllUsersActionPerformed
        deletarTodosUsuarios();
    }//GEN-LAST:event_btnDeleteAllUsersActionPerformed

    private void btnDeleteUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteUserActionPerformed
        deletarUsuarioSelecionado();
    }//GEN-LAST:event_btnDeleteUserActionPerformed

    private void btnVoltarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVoltarActionPerformed
        setVisible(false);
        TelaAdmin admin = new TelaAdmin();
        admin.setVisible(true);
    }//GEN-LAST:event_btnVoltarActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        buscarUsuario();
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnInfoUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInfoUserActionPerformed
        abrirInfoUser();
    }//GEN-LAST:event_btnInfoUserActionPerformed

    private void btnCadUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadUsuarioActionPerformed
        setVisible(false);
        TelaCadastroUsuario telaCadastroUsuario = new TelaCadastroUsuario();
        telaCadastroUsuario.setVisible(true);
    }//GEN-LAST:event_btnCadUsuarioActionPerformed
    public static void main(String args[]) {
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaAdminUsuarios().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCadUsuario;
    private javax.swing.JButton btnDeleteAllUsers;
    private javax.swing.JButton btnDeleteUser;
    private javax.swing.JButton btnInfoUser;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnVoltar;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField searchField;
    private javax.swing.JTable tableUsers;
    // End of variables declaration//GEN-END:variables
}
