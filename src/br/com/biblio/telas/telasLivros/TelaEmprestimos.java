package br.com.biblio.telas.telasLivros;

import br.com.biblio.dal.ModuloConexao;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class TelaEmprestimos extends javax.swing.JFrame {
Connection conexao = null;
PreparedStatement pst = null;
ResultSet rs = null; 
ResultSet rsSearch = null; 
    public TelaEmprestimos() {
        initComponents();
        conexao = ModuloConexao.conector();
        listarLivrosEmprestimo();
    }
    private void listarLivrosEmprestimo(){
        String sql ="select * from livros";
        String userReservou;
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            DefaultTableModel modeloTabela = (DefaultTableModel)tableLivrosEmprestimo.getModel(); 
            modeloTabela.setRowCount(0);       
            while (rs.next()) {
                if(rs.getString(8) != null){
                    if(rs.getString(10) == null){
                        userReservou = "Livro não reservado";
                    }else{
                        userReservou = rs.getString(10);
                    }
                    modeloTabela.addRow(new String[] {rs.getString("nome_livro"), rs.getString("autores"), rs.getString("isbn"), rs.getString("editora"), "Alugado", rs.getString("user_que_alugou"), userReservou});
                }
            }
        } catch (Exception e) {
                JOptionPane.showMessageDialog(null,e);
        }
    }
    private void buscarEmprestimo(){
        String sql ="select * from livros where nome_livro like ? or user_que_alugou like ? or isbn like ?";
        String userReservou;
        try {
            pst = conexao.prepareStatement(sql);
                if(searchField.getText().equals("")){
                    listarLivrosEmprestimo();
                }else{
                    pst.setString(1,"%"+searchField.getText()+"%");
                    pst.setString(2,"%"+searchField.getText()+"%");
                    pst.setString(3,"%"+searchField.getText()+"%");
                    rsSearch = pst.executeQuery();
                    DefaultTableModel modeloTabela = (DefaultTableModel)tableLivrosEmprestimo.getModel(); 
                    modeloTabela.setRowCount(0);
                    if(rsSearch.next()){
                        do{
                            userReservou = rsSearch.getString(10);
                            if(userReservou == null){
                                userReservou = "Livro não reservado";
                            }
                            if(rsSearch.getString(8) != null){
                                modeloTabela.addRow(new String[] {rsSearch.getString("nome_livro"), rsSearch.getString("autores"), rsSearch.getString("isbn"), rsSearch.getString("editora"), "Alugado", rsSearch.getString("user_que_alugou"), userReservou});
                            }
                        }while(rsSearch.next());
                    }else{
                        JOptionPane.showMessageDialog(null,"Nenhum empréstimo com essas informações! Tente novamente!");
                    }
                }
        } catch (Exception e) {
                JOptionPane.showMessageDialog(null,e);
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        tableLivrosEmprestimo = new javax.swing.JTable();
        searchField = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        btnVoltar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Empréstimos");
        setResizable(false);

        tableLivrosEmprestimo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nome", "Autor(a)", "ISBN", "Editora", "Situação", "Alugado por", "Reservado para"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableLivrosEmprestimo.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tableLivrosEmprestimo.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tableLivrosEmprestimo);
        if (tableLivrosEmprestimo.getColumnModel().getColumnCount() > 0) {
            tableLivrosEmprestimo.getColumnModel().getColumn(0).setMinWidth(250);
            tableLivrosEmprestimo.getColumnModel().getColumn(0).setMaxWidth(250);
            tableLivrosEmprestimo.getColumnModel().getColumn(4).setMinWidth(100);
            tableLivrosEmprestimo.getColumnModel().getColumn(4).setMaxWidth(100);
        }

        btnSearch.setText("Buscar empréstimo");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("GERENCIAR EMPRÉSTIMOS");

        btnVoltar.setText("Voltar");
        btnVoltar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVoltarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(217, 217, 217)
                        .addComponent(btnSearch)
                        .addGap(18, 18, 18)
                        .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 391, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(422, 422, 422)
                        .addComponent(jLabel1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 20, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnVoltar)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1079, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addComponent(jLabel1)
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch))
                .addGap(39, 39, 39)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnVoltar)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        buscarEmprestimo();
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnVoltarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVoltarActionPerformed
        setVisible(false);
        TelaAdminLivros telaAdminLivros = new TelaAdminLivros();
        telaAdminLivros.setVisible(true);
    }//GEN-LAST:event_btnVoltarActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaEmprestimos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnVoltar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField searchField;
    private javax.swing.JTable tableLivrosEmprestimo;
    // End of variables declaration//GEN-END:variables
}
