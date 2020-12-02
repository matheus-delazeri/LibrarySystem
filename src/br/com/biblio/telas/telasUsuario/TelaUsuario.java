package br.com.biblio.telas.telasUsuario;

import br.com.biblio.dal.ModuloConexao;
import java.sql.*;
import java.time.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class TelaUsuario extends javax.swing.JFrame {
Connection conexao = null;
PreparedStatement pst = null;
ResultSet rs = null;
    public TelaUsuario() {
        initComponents();
        conexao = ModuloConexao.conector();
        listarLivros();
    }
    private void listarLivros(){
        String sql ="select * from livros";
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            
            DefaultTableModel modeloTabela = (DefaultTableModel)tableLivros.getModel(); 
            modeloTabela.setRowCount(0);       
            while (rs.next()) {
		modeloTabela.addRow(new String[] {rs.getString("nome_livro"), rs.getString("autores"), rs.getString("edicao"), rs.getString("editora"), rs.getString("ano"), rs.getString("isbn") });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,e);
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

                    DefaultTableModel modeloTabela = (DefaultTableModel)tableLivros.getModel(); 
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
    public void displayTela(String user){
        String sql= "select * from users where usuario=?";
        try{
            pst = conexao.prepareStatement(sql);
            pst.setString(1, user);
            rs = pst.executeQuery();
            if (rs.next()){
                String cargo = rs.getString(5);
                hiField.setText(rs.getString(3));
                cargoField.setText(rs.getString(5));
                emprestadoField.setText("  " + verificarNumEmprestimos(user, cargo));
                if("Professor".equals(cargo)){
                    maxEmpField.setText("  5");
                }else{
                    maxEmpField.setText("  3");
                }
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null,e);
        }
    }
    private void verificarEmprestimo(){
        DefaultTableModel modeloTabela = (DefaultTableModel)tableLivros.getModel(); 
        String strIsbn = null;
        int row = tableLivros.getSelectedRow();
        if(row == -1){
            JOptionPane.showMessageDialog(null, "Selecione a linha do livro que deseja pegar emprestado!");
        }else{
            Object isbn = modeloTabela.getValueAt(row, 5);
            strIsbn = isbn.toString();
            String sql= "select * from livros where isbn=?";
            try{
                String user = hiField.getText();
                String cargo = cargoField.getText();
                pst = conexao.prepareStatement(sql);
                pst.setString(1, strIsbn);
                rs = pst.executeQuery();
                if(rs.next() && rs.getString(8) == null){
                    int numEmprestimos = verificarNumEmprestimos(user, cargo);
                    if((numEmprestimos != -1 && "Professor".equals(cargo) && numEmprestimos != 5) || (numEmprestimos != -1 && "Estudante".equals(cargo) && numEmprestimos != 3)){
                        marcarLivroEmprestado(strIsbn, user);
                    }else{
                        JOptionPane.showMessageDialog(null,"Você já excedeu o número máximo de empréstimos por vez!");
                    }
                }else{
                    JOptionPane.showMessageDialog(null,"Este livro já foi alugado!");
                }
            }catch (Exception e){
                JOptionPane.showMessageDialog(null,"Erro no primeiro try da função fazer emprestimo!");
            }
        }
    }
    private int verificarNumEmprestimos(String user, String cargo){
        String sql = "select * from livros where user_que_alugou=?";
        int cont = 0;
        try{
            pst = conexao.prepareStatement(sql);
            pst.setString(1, user);
            rs = pst.executeQuery();
            while(rs.next()){
                cont += 1;
            }
        } catch(Exception e){
            JOptionPane.showMessageDialog(null, "Erro no verificar num de emprestimos!");
        }
        if(("Professor".equals(cargo) && cont <= 5) || ("Estudante".equals(cargo) && cont <= 3)){
            return cont;
        }else{
            return -1;
        }
    }
    private void marcarLivroEmprestado(String isbn, String user){
        LocalDate hoje = java.time.LocalDate.now();  
        Date convHoje = Date.valueOf(hoje);
        int confirma=JOptionPane.showConfirmDialog(null, "Tem certeza que deseja alugar o livro selecionado?");
        if (confirma==JOptionPane.YES_OPTION){
            String sql= "update livros set user_que_alugou=?, dia_que_alugou=? where isbn=?";
            try{
                pst = conexao.prepareStatement(sql);
                pst.setString(1, user);
                pst.setDate(2, convHoje);
                pst.setString(3, isbn);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null,"O livro foi alugado com sucesso!");
                setVisible(false);
                TelaUsuario telaUsuario = new TelaUsuario();
                telaUsuario.setVisible(true);
                telaUsuario.displayTela(user);
                
            }catch(Exception e){
                JOptionPane.showMessageDialog(null,"Erro na função marcarLivroEmprestado!");
            }
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        tableLivros = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        hiField = new javax.swing.JTextField();
        emprestadoField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        maxEmpField = new javax.swing.JTextField();
        btnAlugarLivro = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        cargoField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        searchField = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        btnSair = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        tableLivros.setModel(new javax.swing.table.DefaultTableModel(
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
        tableLivros.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tableLivros.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tableLivros);
        if (tableLivros.getColumnModel().getColumnCount() > 0) {
            tableLivros.getColumnModel().getColumn(0).setResizable(false);
            tableLivros.getColumnModel().getColumn(2).setMinWidth(50);
            tableLivros.getColumnModel().getColumn(2).setMaxWidth(50);
            tableLivros.getColumnModel().getColumn(4).setMinWidth(50);
            tableLivros.getColumnModel().getColumn(4).setMaxWidth(50);
        }

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        hiField.setEditable(false);

        emprestadoField.setEditable(false);

        jLabel1.setText("Livros emprestados:");

        jLabel2.setText(" de");

        maxEmpField.setEditable(false);

        btnAlugarLivro.setText("Alugar livro selecionado");
        btnAlugarLivro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlugarLivroActionPerformed(evt);
            }
        });

        jLabel3.setText("Logado como:");

        cargoField.setEditable(false);

        jLabel4.setText("Cargo");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAlugarLivro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(emprestadoField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(maxEmpField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(hiField))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cargoField)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(hiField)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(9, 9, 9)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cargoField)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(emprestadoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(maxEmpField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addComponent(btnAlugarLivro)
                .addGap(400, 400, 400))
        );

        btnSearch.setText("Buscar");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        btnSair.setText("Sair");
        btnSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSairActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnSair, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 15, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(38, 38, 38)
                                .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 397, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(165, 165, 165))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 902, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(85, 85, 85))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(34, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSearch))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 470, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(26, 26, 26)
                .addComponent(btnSair)
                .addGap(24, 24, 24))
        );

        setSize(new java.awt.Dimension(1255, 688));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        buscarLivro();
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnAlugarLivroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlugarLivroActionPerformed
        verificarEmprestimo();
    }//GEN-LAST:event_btnAlugarLivroActionPerformed

    private void btnSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSairActionPerformed
        setVisible(false);
        TelaLogin telaLogin = new TelaLogin();
        telaLogin.setVisible(true);
    }//GEN-LAST:event_btnSairActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaUsuario().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlugarLivro;
    private javax.swing.JButton btnSair;
    private javax.swing.JButton btnSearch;
    private javax.swing.JTextField cargoField;
    private javax.swing.JTextField emprestadoField;
    private javax.swing.JTextField hiField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField maxEmpField;
    private javax.swing.JTextField searchField;
    private javax.swing.JTable tableLivros;
    // End of variables declaration//GEN-END:variables
}
