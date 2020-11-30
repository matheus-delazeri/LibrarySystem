package br.com.biblio.telas.telasUsuario;

import br.com.biblio.dal.ModuloConexao;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class TelaInfoUsuario extends javax.swing.JFrame {
Connection conexao = null;
PreparedStatement pst = null;
ResultSet rs = null;  
    public TelaInfoUsuario(String user) {
        initComponents();
        conexao = ModuloConexao.conector();
        showUserInfo(user);
    }
    public void showUserInfo(String user){
        String sql = "select * from users where usuario=?";
        int numLivrosAlugados = listarLivrosAlugados(user);
        try{
            pst = conexao.prepareStatement(sql);
            pst.setString(1, user);
            rs = pst.executeQuery();
            if(rs.next()){
                titleField.setText(rs.getString(3));
                nameField.setText(rs.getString(1));
                lastnameField.setText(rs.getString(2));
                String cargo = rs.getString(5);
                cargoField.setText(cargo);
                if("Professor".equals(cargo)){
                    numMaxLivrosField.setText(String.valueOf(5));
                }else{
                    numMaxLivrosField.setText(String.valueOf(3));
                }
                numLivrosAlugadosField.setText(String.valueOf(numLivrosAlugados));
            }
        } catch(Exception e){
            JOptionPane.showMessageDialog(null,e);
        }
    }
    private int listarLivrosAlugados(String user){
        String sql ="select * from livros where user_que_alugou=?";
        int numLivrosAlugados = 0;
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, user);
            rs = pst.executeQuery();
            
            DefaultTableModel modeloTabela = (DefaultTableModel)tableLivrosAlugados.getModel(); 
            modeloTabela.setRowCount(0);       
            while (rs.next()) {
		numLivrosAlugados += 1;
                modeloTabela.addRow(new String[] {rs.getString("nome_livro"), rs.getString("autores"), rs.getString("edicao"), rs.getString("editora"), rs.getString("ano"), rs.getString("isbn") });
            }
            
        } catch (Exception e) {
                JOptionPane.showMessageDialog(null,e);
        }
        return numLivrosAlugados;
    }
    private void devolverLivro(){
        DefaultTableModel modeloTabela = (DefaultTableModel)tableLivrosAlugados.getModel(); 
        int row = tableLivrosAlugados.getSelectedRow();
        if(row == -1){
            JOptionPane.showMessageDialog(null, "Selecione a linha do livro que foi devolvido!");
        }else{
            Object isbn = modeloTabela.getValueAt(row, 5);
            String strIsbn = isbn.toString();   
            String sql ="update livros set user_que_alugou=? where isbn=?";
            int confirma=JOptionPane.showConfirmDialog(null, "Tem certeza que deseja marcar o livro selecionado como devolvido?");
            if (confirma==JOptionPane.YES_OPTION){
                try {
                    pst = conexao.prepareStatement(sql);
                    pst.setString(1,null);
                    pst.setString(2, strIsbn);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "O livro foi devolvido!");

                    setVisible(false);
                    TelaInfoUsuario telaInfoUsuario = new TelaInfoUsuario(titleField.getText());
                    telaInfoUsuario.setVisible(true);

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Não foi possível devolver o livro");
                }
            }
        }
            
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        titleField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableLivrosAlugados = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        cargoField = new javax.swing.JTextField();
        nameField = new javax.swing.JTextField();
        lastnameField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        btnVoltar = new javax.swing.JButton();
        btnDevolverLivro = new javax.swing.JButton();
        numLivrosAlugadosField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        numMaxLivrosField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        titleField.setEditable(false);
        titleField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        titleField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        titleField.setToolTipText("");

        jLabel1.setText("Nome:");

        jLabel2.setText("Sobrenome:");

        jLabel3.setText("Profissão:");

        tableLivrosAlugados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nome", "Autor(a)", "Edição", "Editora", "Ano", "ISBN"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableLivrosAlugados.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tableLivrosAlugados.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tableLivrosAlugados);
        if (tableLivrosAlugados.getColumnModel().getColumnCount() > 0) {
            tableLivrosAlugados.getColumnModel().getColumn(0).setMinWidth(150);
            tableLivrosAlugados.getColumnModel().getColumn(0).setMaxWidth(150);
            tableLivrosAlugados.getColumnModel().getColumn(1).setMinWidth(120);
            tableLivrosAlugados.getColumnModel().getColumn(1).setMaxWidth(120);
            tableLivrosAlugados.getColumnModel().getColumn(2).setMinWidth(50);
            tableLivrosAlugados.getColumnModel().getColumn(2).setMaxWidth(50);
            tableLivrosAlugados.getColumnModel().getColumn(3).setMinWidth(100);
            tableLivrosAlugados.getColumnModel().getColumn(3).setMaxWidth(100);
            tableLivrosAlugados.getColumnModel().getColumn(4).setMinWidth(50);
            tableLivrosAlugados.getColumnModel().getColumn(4).setMaxWidth(50);
            tableLivrosAlugados.getColumnModel().getColumn(5).setMinWidth(150);
            tableLivrosAlugados.getColumnModel().getColumn(5).setMaxWidth(150);
        }

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Livros alugados:");

        cargoField.setEditable(false);
        cargoField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        nameField.setEditable(false);
        nameField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        lastnameField.setEditable(false);
        lastnameField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setText("Informações sobre o usuário");

        btnVoltar.setText("Voltar");
        btnVoltar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVoltarActionPerformed(evt);
            }
        });

        btnDevolverLivro.setText("Marcar livro como devolvido");
        btnDevolverLivro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDevolverLivroActionPerformed(evt);
            }
        });

        numLivrosAlugadosField.setEditable(false);
        numLivrosAlugadosField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("de");

        numMaxLivrosField.setEditable(false);
        numMaxLivrosField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lastnameField, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
                        .addGap(176, 176, 176))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cargoField)
                                .addGap(433, 433, 433))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(138, 138, 138)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(titleField, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnVoltar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnDevolverLivro)))
                        .addContainerGap())))
            .addGroup(layout.createSequentialGroup()
                .addGap(216, 216, 216)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(numLivrosAlugadosField, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(numMaxLivrosField, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(titleField)
                    .addComponent(jLabel5))
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(nameField)
                    .addComponent(lastnameField))
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cargoField))
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(numLivrosAlugadosField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(numMaxLivrosField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnVoltar)
                    .addComponent(btnDevolverLivro))
                .addGap(9, 9, 9))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnVoltarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVoltarActionPerformed
        setVisible(false);
        TelaAdminUsuarios telaAdminUsuarios = new TelaAdminUsuarios();
        telaAdminUsuarios.setVisible(true);
    }//GEN-LAST:event_btnVoltarActionPerformed

    private void btnDevolverLivroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDevolverLivroActionPerformed
        devolverLivro();
    }//GEN-LAST:event_btnDevolverLivroActionPerformed

    public static void main(String args[]) {
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDevolverLivro;
    private javax.swing.JButton btnVoltar;
    private javax.swing.JTextField cargoField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField lastnameField;
    private javax.swing.JTextField nameField;
    private javax.swing.JTextField numLivrosAlugadosField;
    private javax.swing.JTextField numMaxLivrosField;
    private javax.swing.JTable tableLivrosAlugados;
    private javax.swing.JTextField titleField;
    // End of variables declaration//GEN-END:variables
}
