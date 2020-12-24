package br.com.biblio.telas.telasUsuario;

import br.com.biblio.dal.ModuloConexao;
import java.sql.*;
import java.time.LocalDate;
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
                valorTotalMultas.setText(rs.getString(5));
                userField.setText(rs.getString(3));
                nameField.setText(rs.getString(1));
                lastnameField.setText(rs.getString(2));
                String cargo = rs.getString(4);
                cargoField.setText(cargo);
                if("Professor".equals(cargo)){
                    numMaxLivrosAlugados.setText(String.valueOf(5));
                }else{
                    numMaxLivrosAlugados.setText(String.valueOf(3));
                }
                numLivrosAlugadosField.setText(String.valueOf(numLivrosAlugados));
            }
        } catch(Exception e){
            JOptionPane.showMessageDialog(null,"Erro em show user info");
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
                LocalDate convDia = ((java.sql.Date) rs.getDate("dia_que_alugou")).toLocalDate();
                String strDia = String.valueOf(convDia);
		numLivrosAlugados += 1;
                modeloTabela.addRow(new String[] {rs.getString("nome_livro"), rs.getString("autores"), rs.getString("edicao"), rs.getString("editora"), rs.getString("ano"), rs.getString("isbn"), strDia});
            }
            
        } catch (Exception e) {
                JOptionPane.showMessageDialog(null,"Erro em listar livros alugados");
        }
        return numLivrosAlugados;
    }
    private void devolverLivro(){
        DefaultTableModel modeloTabela = (DefaultTableModel)tableLivrosAlugados.getModel(); 
        int row = tableLivrosAlugados.getSelectedRow();
        if(row == -1){
            JOptionPane.showMessageDialog(null, "Selecione a linha do livro que será devolvido!");
        }else{
            Object isbn = modeloTabela.getValueAt(row, 5);
            String strIsbn = isbn.toString();   
            String sql ="update livros set user_que_alugou=?, dia_que_alugou=? where isbn=?";
            int confirma=JOptionPane.showConfirmDialog(null, "Tem certeza que deseja marcar o livro selecionado como devolvido?");
            if (confirma==JOptionPane.YES_OPTION){
                try {
                    pst = conexao.prepareStatement(sql);
                    pst.setString(1, null);
                    pst.setDate(2, null);
                    pst.setString(3, strIsbn);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "O livro foi devolvido!");

                    setVisible(false);
                    TelaInfoUsuario telaInfoUsuario = new TelaInfoUsuario(userField.getText());
                    telaInfoUsuario.setVisible(true);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Não foi possível devolver o livro");
                }
            }
        }
    }
    private void pagarMultas(){
        String user =  userField.getText();
        String sql = "update users set multa=? where usuario=?";
        if(Integer.parseInt(numLivrosAlugadosField.getText()) == 0 && Integer.parseInt(valorTotalMultas.getText()) > 0){
            int confirma=JOptionPane.showConfirmDialog(null, "Deseja zerar a multa?");
            if (confirma==JOptionPane.YES_OPTION){
                try{
                    pst = conexao.prepareStatement(sql);
                    pst.setString(1, String.valueOf(0));
                    pst.setString(2, user);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "A multa foi paga!");
                    setVisible(false);
                    TelaInfoUsuario telaInfoUsuario = new TelaInfoUsuario(user);
                    telaInfoUsuario.setVisible(true);
                } catch(Exception e){
                    JOptionPane.showMessageDialog(null, e);
                }
            }
        }else{
             JOptionPane.showMessageDialog(null, "Os livros ainda não foram devolvidos ou não há multa a ser paga!");
        }
    }
    private void devolverTodosLivros(){
        String select = "select * from livros where user_que_alugou=?";
        String user =  userField.getText();
        String sql ="update livros set user_que_alugou=?, dia_que_alugou=? where user_que_alugou=?";
        try{
            pst = conexao.prepareStatement(select);
            pst.setString(1, user);
            rs = pst.executeQuery();
            while(rs.next()){
                pst = conexao.prepareStatement(sql);
                pst.setString(1, null);
                pst.setDate(2, null);
                pst.setString(3, user);
                pst.executeUpdate();
            }
        } catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        setVisible(false);
        TelaInfoUsuario telaInfoUsuario = new TelaInfoUsuario(user);
        telaInfoUsuario.setVisible(true);
        
    }
    private void renovarEmprestimo(){
        LocalDate hoje = java.time.LocalDate.now();  
        Date convHoje = Date.valueOf(hoje);
        DefaultTableModel modeloTabela = (DefaultTableModel)tableLivrosAlugados.getModel(); 
        int row = tableLivrosAlugados.getSelectedRow();
        if(row == -1){
            JOptionPane.showMessageDialog(null, "Selecione a linha do livro que será renovado!");
        }else{
            Object isbn = modeloTabela.getValueAt(row, 5);
            String strIsbn = isbn.toString();
            String sql = "select * from livros where isbn=?";
            String sqlUpdate = "update livros set dia_que_alugou=? where isbn=?";
            String sqlResUser = "update livros set dia_que_alugou=?, user_que_reservou=? where isbn=?";
            try{
                pst = conexao.prepareStatement(sql);
                pst.setString(1, strIsbn);
                rs = pst.executeQuery();
                if(rs.next()){
                    if(rs.getString(10) == null){
                        int confirma=JOptionPane.showConfirmDialog(null, "Deseja renovar o livro selecionado?");
                        if (confirma==JOptionPane.YES_OPTION){
                            pst = conexao.prepareStatement(sqlUpdate);
                            pst.setDate(1, convHoje);
                            pst.setString(2, strIsbn);
                            pst.executeUpdate();
                            JOptionPane.showMessageDialog(null, "O livro foi renovado com sucesso!");
                            setVisible(false);
                            TelaInfoUsuario telaInfoUsuario = new TelaInfoUsuario(userField.getText());
                            telaInfoUsuario.setVisible(true);
                        }
                    }else if(rs.getString(10).equals(userField.getText())){
                        int confirma=JOptionPane.showConfirmDialog(null, "Deseja renovar o livro selecionado?");
                        if (confirma==JOptionPane.YES_OPTION){
                            pst = conexao.prepareStatement(sqlResUser);
                            pst.setDate(1, convHoje);
                            pst.setString(2, null);
                            pst.setString(3, strIsbn);
                            pst.executeUpdate();
                            JOptionPane.showMessageDialog(null, "O livro foi renovado com sucesso!");
                            setVisible(false);
                            TelaInfoUsuario telaInfoUsuario = new TelaInfoUsuario(userField.getText());
                            telaInfoUsuario.setVisible(true);
                        }
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "A renovação não pôde ser concluída! Esse livro foi reservado por outro usuário.");
                    }
                }
            } catch(Exception e){
                
            }
            
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        userField = new javax.swing.JTextField();
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
        numMaxLivrosAlugados = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        valorTotalMultas = new javax.swing.JTextField();
        btnPagarMulta = new javax.swing.JButton();
        btnDevolverTodosLivros = new javax.swing.JButton();
        btnAlugarReservar = new javax.swing.JButton();
        btnRenovar = new javax.swing.JButton();
        numLivrosAlugadosField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Informações de usuário");
        setResizable(false);

        userField.setEditable(false);
        userField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        userField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        userField.setToolTipText("");

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setText("Nome:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("Sobrenome:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("Profissão:");

        tableLivrosAlugados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nome", "Autor(a)", "Edição", "Editora", "Ano", "ISBN", "Data do empréstimo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableLivrosAlugados.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tableLivrosAlugados.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tableLivrosAlugados);
        if (tableLivrosAlugados.getColumnModel().getColumnCount() > 0) {
            tableLivrosAlugados.getColumnModel().getColumn(0).setMinWidth(300);
            tableLivrosAlugados.getColumnModel().getColumn(0).setMaxWidth(300);
            tableLivrosAlugados.getColumnModel().getColumn(1).setMinWidth(210);
            tableLivrosAlugados.getColumnModel().getColumn(1).setMaxWidth(210);
            tableLivrosAlugados.getColumnModel().getColumn(2).setMinWidth(50);
            tableLivrosAlugados.getColumnModel().getColumn(2).setMaxWidth(50);
            tableLivrosAlugados.getColumnModel().getColumn(3).setMinWidth(150);
            tableLivrosAlugados.getColumnModel().getColumn(3).setMaxWidth(150);
            tableLivrosAlugados.getColumnModel().getColumn(4).setMinWidth(50);
            tableLivrosAlugados.getColumnModel().getColumn(4).setMaxWidth(50);
            tableLivrosAlugados.getColumnModel().getColumn(5).setMinWidth(170);
            tableLivrosAlugados.getColumnModel().getColumn(5).setMaxWidth(170);
            tableLivrosAlugados.getColumnModel().getColumn(6).setMinWidth(150);
            tableLivrosAlugados.getColumnModel().getColumn(6).setMaxWidth(150);
        }

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("LIVROS ALUGADOS");

        cargoField.setEditable(false);
        cargoField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        nameField.setEditable(false);
        nameField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        lastnameField.setEditable(false);
        lastnameField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("INFORMAÇÕES SOBRE O USUÁRIO:");

        btnVoltar.setText("Voltar");
        btnVoltar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVoltarActionPerformed(evt);
            }
        });

        btnDevolverLivro.setText("Devolver livro selecionado");
        btnDevolverLivro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDevolverLivroActionPerformed(evt);
            }
        });

        numMaxLivrosAlugados.setEditable(false);
        numMaxLivrosAlugados.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("DE");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setText("Valor total de multas a pagar: R$");

        valorTotalMultas.setEditable(false);
        valorTotalMultas.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        btnPagarMulta.setText("Pagar multa");
        btnPagarMulta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPagarMultaActionPerformed(evt);
            }
        });

        btnDevolverTodosLivros.setText("Devolver todos os livros");
        btnDevolverTodosLivros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDevolverTodosLivrosActionPerformed(evt);
            }
        });

        btnAlugarReservar.setText("Alugar/Reservar novo livro");
        btnAlugarReservar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlugarReservarActionPerformed(evt);
            }
        });

        btnRenovar.setText("Renovar livro selecionado");
        btnRenovar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRenovarActionPerformed(evt);
            }
        });

        numLivrosAlugadosField.setEditable(false);
        numLivrosAlugadosField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnVoltar))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(btnRenovar)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnAlugarReservar)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnDevolverTodosLivros)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnDevolverLivro))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(437, 437, 437)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(numLivrosAlugadosField, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(numMaxLivrosAlugados, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1075, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(58, 58, 58)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGap(245, 245, 245)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(userField, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addGap(18, 18, 18)
                                        .addComponent(cargoField, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel7)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(valorTotalMultas, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(btnPagarMulta))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lastnameField, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(userField)
                    .addComponent(jLabel5))
                .addGap(42, 42, 42)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(nameField)
                    .addComponent(lastnameField))
                .addGap(56, 56, 56)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(valorTotalMultas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPagarMulta))
                .addGap(41, 41, 41)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cargoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel6)
                        .addComponent(numMaxLivrosAlugados, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel4)
                        .addComponent(numLivrosAlugadosField, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(33, 33, 33)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnVoltar)
                    .addComponent(btnDevolverLivro)
                    .addComponent(btnDevolverTodosLivros)
                    .addComponent(btnAlugarReservar)
                    .addComponent(btnRenovar))
                .addGap(24, 24, 24))
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
    private void btnPagarMultaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPagarMultaActionPerformed
        pagarMultas();
    }//GEN-LAST:event_btnPagarMultaActionPerformed
    private void btnDevolverTodosLivrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDevolverTodosLivrosActionPerformed
        if(Integer.parseInt(numLivrosAlugadosField.getText()) > 0){
            int confirma=JOptionPane.showConfirmDialog(null, "Deseja devolver todos os livros?");
            if (confirma==JOptionPane.YES_OPTION){
                devolverTodosLivros();
                JOptionPane.showMessageDialog(null, "Os livros foram devolvidos!");
            }
        }else{
            JOptionPane.showMessageDialog(null, "Não há nenhum livro para ser devolvido!");
        }
    }//GEN-LAST:event_btnDevolverTodosLivrosActionPerformed
    private void btnAlugarReservarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlugarReservarActionPerformed
        setVisible(false);
        TelaUsuario telaUsuario = new TelaUsuario();
        telaUsuario.displayTela(userField.getText());
        telaUsuario.setVisible(true);
    }//GEN-LAST:event_btnAlugarReservarActionPerformed

    private void btnRenovarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRenovarActionPerformed
        renovarEmprestimo();
    }//GEN-LAST:event_btnRenovarActionPerformed
    public static void main(String args[]) {
        
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlugarReservar;
    private javax.swing.JButton btnDevolverLivro;
    private javax.swing.JButton btnDevolverTodosLivros;
    private javax.swing.JButton btnPagarMulta;
    private javax.swing.JButton btnRenovar;
    private javax.swing.JButton btnVoltar;
    private javax.swing.JTextField cargoField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField lastnameField;
    private javax.swing.JTextField nameField;
    private javax.swing.JTextField numLivrosAlugadosField;
    private javax.swing.JTextField numMaxLivrosAlugados;
    private javax.swing.JTable tableLivrosAlugados;
    private javax.swing.JTextField userField;
    private javax.swing.JTextField valorTotalMultas;
    // End of variables declaration//GEN-END:variables
}
