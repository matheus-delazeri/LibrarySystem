package br.com.biblio.telas.telasLivros;

import br.com.biblio.dal.ModuloConexao;
import java.sql.*;
import br.com.biblio.telas.telasGeral.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import br.com.biblio.telas.telasGeral.Multas;

public class TelaAdminLivros extends javax.swing.JFrame {
Connection conexao = null;
PreparedStatement pst = null;
ResultSet rs = null; 
Multas multas = new Multas();
    public TelaAdminLivros() {
        multas.verificarMultas();
        initComponents();
        conexao = ModuloConexao.conector();
        listarLivros();
    }
    private void listarLivros(){
        String sql ="select * from livros";
        String disponibilidade;
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            
            DefaultTableModel modeloTabela = (DefaultTableModel)tableLivrosAdmin.getModel(); 
            modeloTabela.setRowCount(0);       
            while (rs.next()) {
                if(rs.getString(8) == null && rs.getString(10) == null){
                    disponibilidade = "Disponível";
                }else if(rs.getString(8) != null && rs.getString(10) == null){
                    disponibilidade = "Alugado";
                }else{
                    disponibilidade = "Reservado";
                }
		modeloTabela.addRow(new String[] {rs.getString("nome_livro"), rs.getString("autores"), rs.getString("edicao"), rs.getString("editora"), rs.getString("ano"), rs.getString("isbn"), disponibilidade});
            }
        } catch (Exception e) {
                JOptionPane.showMessageDialog(null,e);
        }
    }
    private void deletarTodosLivros(){
        int confirma=JOptionPane.showConfirmDialog(null, "Tem certeza que deseja deletar todos livros?");
        if (confirma==JOptionPane.YES_OPTION){
            String sql = "delete from livros";
            try {
                pst = conexao.prepareStatement(sql);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Os livros foram excluídos!");
                listarLivros();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Não foi possível excluir os livros");
            }
        }
    }
    private void deletarLivroSelecionado(){
        DefaultTableModel modeloTabela = (DefaultTableModel)tableLivrosAdmin.getModel(); 
        int row = tableLivrosAdmin.getSelectedRow();
        if(row == -1){
            JOptionPane.showMessageDialog(null, "Selecione a linha do livro que deseja apagar!");
        }else{
            Object isbn = modeloTabela.getValueAt(row, 5);
            String strIsbn = isbn.toString();
            Object nomeLivro = modeloTabela.getValueAt(row, 0);
            String strNomeLivro = nomeLivro.toString();

            int confirma=JOptionPane.showConfirmDialog(null, "Tem certeza que deseja deletar o livro "+strNomeLivro+" de ISBN igual a "+strIsbn+" do banco de dados?");
            if (confirma==JOptionPane.YES_OPTION){
                String sql ="delete from livros where isbn=?";
                try {
                    pst = conexao.prepareStatement(sql);
                    pst.setString(1,strIsbn);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "O livro foi excluído!");
                    listarLivros();  
                } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Não foi possível excluir o livro");
                }
            }
        }
    }
    private void buscarLivro(){
        String sql ="select * from livros where nome_livro like ? or editora like ? or isbn like ?";
        try {
            pst = conexao.prepareStatement(sql);
                if(searchField.getText().equals("")){
                    listarLivros();
                }else{
                    pst.setString(1,"%"+searchField.getText()+"%");
                    pst.setString(2,"%"+searchField.getText()+"%");
                    pst.setString(3,"%"+searchField.getText()+"%");
                    rs = pst.executeQuery();

                    DefaultTableModel modeloTabela = (DefaultTableModel)tableLivrosAdmin.getModel(); 
                    modeloTabela.setRowCount(0);       
                    if (rs.next()) {
                        do{
                            modeloTabela.addRow(new String[] {rs.getString("nome_livro"), rs.getString("autores"), rs.getString("edicao"), rs.getString("editora"), rs.getString("ano"), rs.getString("isbn") });
                        }while(rs.next());
                    }else{
                        JOptionPane.showMessageDialog(null,"Nenhum livro cadastrado com essas informações! Tente novamente!");
                    }
                }
        } catch (Exception e) {
                JOptionPane.showMessageDialog(null,e);
        }
    }
    public void getIsbn(){
        DefaultTableModel modeloTabela = (DefaultTableModel)tableLivrosAdmin.getModel(); 
        String strIsbn = null;
        int row = tableLivrosAdmin.getSelectedRow();
        if(row == -1){
            JOptionPane.showMessageDialog(null, "Selecione a linha do livro que deseja editar!");
        }else{
            Object isbn = modeloTabela.getValueAt(row, 5);
            strIsbn = isbn.toString();
            
            setVisible(false);
            TelaEditarLivro telaEditarLivro = new TelaEditarLivro();
            telaEditarLivro.showLivro(strIsbn);
            telaEditarLivro.setVisible(true);
        }
    }
  
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        btnDeleteLivro = new javax.swing.JButton();
        btnVoltar = new javax.swing.JButton();
        btnDeleteAllLivros = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableLivrosAdmin = new javax.swing.JTable();
        searchField = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        btnAddLivro = new javax.swing.JButton();
        btnEditLivro = new javax.swing.JButton();
        btnMostrarEmprestimos = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Painel Administrativo de Livros");
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("GERENCIAR LIVROS");

        btnDeleteLivro.setText("Deletar livro selecionado");
        btnDeleteLivro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteLivroActionPerformed(evt);
            }
        });

        btnVoltar.setText("Voltar");
        btnVoltar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVoltarActionPerformed(evt);
            }
        });

        btnDeleteAllLivros.setText("Deletar todos livros");
        btnDeleteAllLivros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteAllLivrosActionPerformed(evt);
            }
        });

        tableLivrosAdmin.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nome", "Autor(a)", "Edição", "Editora", "Ano", "ISBN", "Situação"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableLivrosAdmin.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tableLivrosAdmin.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tableLivrosAdmin);
        if (tableLivrosAdmin.getColumnModel().getColumnCount() > 0) {
            tableLivrosAdmin.getColumnModel().getColumn(0).setMinWidth(300);
            tableLivrosAdmin.getColumnModel().getColumn(0).setMaxWidth(300);
            tableLivrosAdmin.getColumnModel().getColumn(2).setMinWidth(50);
            tableLivrosAdmin.getColumnModel().getColumn(2).setMaxWidth(50);
            tableLivrosAdmin.getColumnModel().getColumn(6).setMinWidth(100);
            tableLivrosAdmin.getColumnModel().getColumn(6).setMaxWidth(100);
        }

        btnSearch.setText("Buscar");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        btnAddLivro.setText("Adicionar novo livro");
        btnAddLivro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddLivroActionPerformed(evt);
            }
        });

        btnEditLivro.setText("Editar livro selecionado");
        btnEditLivro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditLivroActionPerformed(evt);
            }
        });

        btnMostrarEmprestimos.setText("Mostrar Empréstimos");
        btnMostrarEmprestimos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMostrarEmprestimosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnMostrarEmprestimos)
                .addGap(18, 18, 18)
                .addComponent(btnEditLivro)
                .addGap(18, 18, 18)
                .addComponent(btnAddLivro)
                .addGap(18, 18, 18)
                .addComponent(btnDeleteLivro)
                .addGap(18, 18, 18)
                .addComponent(btnDeleteAllLivros, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(314, 314, 314)
                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(62, 62, 62)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 391, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(290, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(btnVoltar)
                            .addGap(0, 1036, Short.MAX_VALUE))
                        .addComponent(jScrollPane2))
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jLabel1)
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 536, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDeleteLivro)
                    .addComponent(btnAddLivro)
                    .addComponent(btnDeleteAllLivros)
                    .addComponent(btnEditLivro)
                    .addComponent(btnMostrarEmprestimos))
                .addGap(29, 29, 29))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(131, 131, 131)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 467, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                    .addComponent(btnVoltar)
                    .addGap(30, 30, 30)))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnDeleteLivroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteLivroActionPerformed
            deletarLivroSelecionado();
    }//GEN-LAST:event_btnDeleteLivroActionPerformed

    private void btnVoltarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVoltarActionPerformed
            setVisible(false);
            TelaAdmin admin = new TelaAdmin();
            admin.setVisible(true);
    }//GEN-LAST:event_btnVoltarActionPerformed

    private void btnDeleteAllLivrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteAllLivrosActionPerformed
            deletarTodosLivros();
    }//GEN-LAST:event_btnDeleteAllLivrosActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
            buscarLivro();
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnAddLivroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddLivroActionPerformed
            setVisible(false);
            TelaCadastroLivro cadLivro = new TelaCadastroLivro();
            cadLivro.setVisible(true);
    }//GEN-LAST:event_btnAddLivroActionPerformed

    private void btnEditLivroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditLivroActionPerformed
            getIsbn();
    }//GEN-LAST:event_btnEditLivroActionPerformed

    private void btnMostrarEmprestimosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostrarEmprestimosActionPerformed
        setVisible(false);
        TelaEmprestimos telaEmprestimos = new TelaEmprestimos();
        telaEmprestimos.setVisible(true);
    }//GEN-LAST:event_btnMostrarEmprestimosActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaAdminLivros().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddLivro;
    private javax.swing.JButton btnDeleteAllLivros;
    private javax.swing.JButton btnDeleteLivro;
    private javax.swing.JButton btnEditLivro;
    private javax.swing.JButton btnMostrarEmprestimos;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnVoltar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField searchField;
    private javax.swing.JTable tableLivrosAdmin;
    // End of variables declaration//GEN-END:variables
}
