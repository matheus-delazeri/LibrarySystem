package br.com.biblio.telas.telasLivros;

import br.com.biblio.dal.ModuloConexao;
import java.sql.*;
import javax.swing.JOptionPane;

public class TelaEditarLivro extends javax.swing.JFrame {
Connection conexao = null;
PreparedStatement pst = null;
ResultSet rs = null; 

    public TelaEditarLivro() {
        initComponents();
        conexao = ModuloConexao.conector();
    }
    public String showLivro(String isbn){
        String sql = "select * from livros where isbn=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, isbn);
            rs = pst.executeQuery();
            if (rs.next()){
                originalIsbn.setText(rs.getString(2));
                
                editISBN.setText(rs.getString(2));
                editAutor.setText(rs.getString(3));
                editEdicao.setText(rs.getString(4));
                editEditora.setText(rs.getString(5));
                editNomeLivro.setText(rs.getString(6));
                editAno.setText(rs.getString(7));
                
           }else{
               JOptionPane.showMessageDialog(null, "Ops! Alguma coisa deu errado. Tente novamente!");
           }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return isbn;
    }
    private void editarLivro(String isbn){
        TelaCadastroLivro cadLivro = new TelaCadastroLivro();
        
        int confirma=JOptionPane.showConfirmDialog(null, "Tem certeza que deseja salvar as edições feitas?");
        if (confirma==JOptionPane.YES_OPTION){
            String sql = "update livros set isbn=?, autores=?, edicao=?, editora=?, nome_livro=?, ano=? where isbn=? ";
            try {
                pst = conexao.prepareStatement(sql);
                if(editISBN.getText().equals(isbn)){
                    pst.setString(1, editISBN.getText());
                }else{
                    boolean isbnIsUnique = cadLivro.verificarIsbn(editISBN.getText());
                    boolean isbnIsNumber = cadLivro.isNumber(editISBN.getText(),"O ISBN");
                    boolean isbnCorrectSize = cadLivro.verificarTamanho(editISBN.getText(), 13, "O ISBN");
                    if(isbnIsNumber && isbnCorrectSize && isbnIsUnique){
                        pst.setString(1, editISBN.getText());
                    }
                }
                
                pst.setString(2, editAutor.getText());
                boolean edicaoIsNumber = cadLivro.isNumber(editEdicao.getText(),"A edição");
                if(edicaoIsNumber){
                    pst.setString(3, editEdicao.getText());
                }
                pst.setString(4, editEditora.getText());
                pst.setString(5, editNomeLivro.getText());
                boolean anoIsNumber = cadLivro.isNumber(editAno.getText(),"O ano");
                boolean anoCorrectSize = cadLivro.verificarTamanho(editAno.getText(), 4, "O ano");
                if(anoIsNumber && anoCorrectSize){
                    pst.setString(6, editAno.getText());
                }
                pst.setString(7, isbn);
                if(editNomeLivro.getText().equals("") || editAutor.getText().equals("") || editEditora.getText().equals("") || editEdicao.getText().equals("") || editAno.getText().equals("") || editISBN.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "Todos os campos devem ser preenchidos!"); 
                }else{
                    pst.executeUpdate();

                    JOptionPane.showMessageDialog(null, "As alterações foram salvas!");
                    setVisible(false);
                    TelaAdminLivros adminLivros = new TelaAdminLivros();
                    adminLivros.setVisible(true);
                }
            } catch (Exception e) {}
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        editEditora = new javax.swing.JTextField();
        editEdicao = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        editISBN = new javax.swing.JTextField();
        btnVoltar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        editNomeLivro = new javax.swing.JTextField();
        editAno = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        btnSalvarEdit = new javax.swing.JButton();
        editAutor = new javax.swing.JTextField();
        originalIsbn = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Editar livro");

        jLabel2.setText("Ano:");

        jLabel6.setText("ISBN:");

        btnVoltar.setText("Voltar");
        btnVoltar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVoltarActionPerformed(evt);
            }
        });
        btnVoltar.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                btnVoltarPropertyChange(evt);
            }
        });

        jLabel1.setText("Edição:");

        jLabel3.setText("Nome:");

        jLabel4.setText("Autor(a):");

        jLabel5.setText("Editora:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("EDITAR LIVRO");

        btnSalvarEdit.setText("Salvar");
        btnSalvarEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarEditActionPerformed(evt);
            }
        });
        btnSalvarEdit.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                btnSalvarEditPropertyChange(evt);
            }
        });

        originalIsbn.setEditable(false);
        originalIsbn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                originalIsbnActionPerformed(evt);
            }
        });

        jLabel8.setText("ISBN Original:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(editNomeLivro, javax.swing.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
                                .addComponent(editAutor)
                                .addComponent(editEditora)
                                .addComponent(editAno, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                                .addComponent(editEdicao))
                            .addComponent(editISBN, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnVoltar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 220, Short.MAX_VALUE)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(originalIsbn, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                .addComponent(btnSalvarEdit)
                .addGap(18, 18, 18))
            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel7)
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editNomeLivro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editAutor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editEditora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editEdicao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(editAno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editISBN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnVoltar)
                    .addComponent(btnSalvarEdit)
                    .addComponent(originalIsbn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnVoltarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVoltarActionPerformed
        setVisible(false);
        TelaAdminLivros adminLivros = new TelaAdminLivros();
        adminLivros.setVisible(true);
    }//GEN-LAST:event_btnVoltarActionPerformed

    private void btnVoltarPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_btnVoltarPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_btnVoltarPropertyChange

    private void btnSalvarEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarEditActionPerformed
        String isbn = originalIsbn.getText();
        editarLivro(isbn);
    }//GEN-LAST:event_btnSalvarEditActionPerformed

    private void btnSalvarEditPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_btnSalvarEditPropertyChange

    }//GEN-LAST:event_btnSalvarEditPropertyChange

    private void originalIsbnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_originalIsbnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_originalIsbnActionPerformed
    public static void main(String args[]) {
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaEditarLivro().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSalvarEdit;
    private javax.swing.JButton btnVoltar;
    private javax.swing.JTextField editAno;
    private javax.swing.JTextField editAutor;
    private javax.swing.JTextField editEdicao;
    private javax.swing.JTextField editEditora;
    private javax.swing.JTextField editISBN;
    private javax.swing.JTextField editNomeLivro;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JTextField originalIsbn;
    // End of variables declaration//GEN-END:variables
}
