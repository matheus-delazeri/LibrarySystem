package br.com.biblio.telas.telasLivros;

import br.com.biblio.dal.ModuloConexao;
import java.sql.*;
import javax.swing.JOptionPane;

public class TelaCadastroLivro extends javax.swing.JFrame {
Connection conexao = null;
PreparedStatement pst = null;
ResultSet rs = null;  

    public TelaCadastroLivro() {
        initComponents();
        conexao = ModuloConexao.conector();
    }
    public boolean verificarIsbn(String isbn){
        String sql = "select * from livros where isbn=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, isbn);
            rs = pst.executeQuery();
            if (rs.next()){
                JOptionPane.showMessageDialog(null, "ISBN já cadastrado no sistema! Tente novamente");
                return false;
           }else{
               return true;
           }
        } catch (Exception e) {
            return true;
        }
    }
    public boolean verificarTamanho(String str, int size, String campo){
        if(str.length() == size){
            return true;
        }else{
            JOptionPane.showMessageDialog(null, campo+" deve ser um valor formado por "+size+" números! Sem espaços ou letras");
            return false;
        }
    }
    public boolean isNumber(String str, String campo){
        boolean number = false;
        try {
            Double.parseDouble(str);
            number = true;
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(null, campo + " só pode conter números!");
        }
        return number;
    }
    public void Cadastrar(){
    boolean isbnIsUnique = verificarIsbn(cadISBN.getText());
    
    String sql= "insert into livros(nome_livro, autores, editora, edicao, ano, isbn) values(?, ?, ?, ?, ?, ?)";    
        try {
           pst = conexao.prepareStatement(sql);
           pst.setString(1, cadNomeLivro.getText());
           pst.setString(2, cadAutor.getText());
           pst.setString(3, cadEditora.getText());
           
           boolean edicaoIsNumber = isNumber(cadEdicao.getText(),"A edição");
           if(edicaoIsNumber){
                pst.setString(4, cadEdicao.getText());
           }
           
           boolean anoIsNumber = isNumber(cadAno.getText(),"O ano");
           boolean anoCorrectSize = verificarTamanho(cadAno.getText(), 4, "O ano");
           if(anoIsNumber && anoCorrectSize){
                pst.setString(5, cadAno.getText());
           }else{
               JOptionPane.showMessageDialog(null, "Confira a data novamente!");
           }

           boolean isbnIsNumber = isNumber(cadISBN.getText(),"O ISBN");
           boolean isbnCorrectSize = verificarTamanho(cadISBN.getText(), 13, "O ISBN");
           if(isbnIsNumber && isbnCorrectSize && isbnIsUnique){
                pst.setString(6, cadISBN.getText());
           }
           if(cadNomeLivro.getText().equals("") || cadAutor.getText().equals("") || cadEditora.getText().equals("") || cadEdicao.getText().equals("") || cadAno.getText().equals("") || cadISBN.getText().equals("")){
                JOptionPane.showMessageDialog(null, "Todos os campos devem ser preenchidos!"); 
           }else{
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Livro cadastrado com sucesso!");
                setVisible(false);
                TelaAdminLivros adminLivros = new TelaAdminLivros();
                adminLivros.setVisible(true);
           }
        } catch (Exception e) {}
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        cadNomeLivro = new javax.swing.JTextField();
        cadAno = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        btnCadastrarLivro = new javax.swing.JButton();
        cadAutor = new javax.swing.JTextField();
        cadEditora = new javax.swing.JTextField();
        cadEdicao = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cadISBN = new javax.swing.JTextField();
        btnVoltar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Edição:");

        jLabel3.setText("Nome:");

        jLabel4.setText("Autor(a):");

        jLabel5.setText("Editora:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel7.setText("Cadastrar Livro");

        btnCadastrarLivro.setText("Cadastrar");
        btnCadastrarLivro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadastrarLivroActionPerformed(evt);
            }
        });
        btnCadastrarLivro.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                btnCadastrarLivroPropertyChange(evt);
            }
        });

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(223, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addGap(220, 220, 220))
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
                                .addComponent(cadNomeLivro, javax.swing.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
                                .addComponent(cadAutor)
                                .addComponent(cadEditora)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(cadAno, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                                    .addComponent(cadEdicao, javax.swing.GroupLayout.Alignment.LEADING)))
                            .addComponent(cadISBN, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnVoltar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCadastrarLivro)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jLabel7)
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cadNomeLivro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cadAutor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cadEditora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cadEdicao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cadAno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cadISBN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnVoltar)
                    .addComponent(btnCadastrarLivro))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCadastrarLivroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadastrarLivroActionPerformed
            Cadastrar();
    }//GEN-LAST:event_btnCadastrarLivroActionPerformed

    private void btnCadastrarLivroPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_btnCadastrarLivroPropertyChange

    }//GEN-LAST:event_btnCadastrarLivroPropertyChange

    private void btnVoltarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVoltarActionPerformed
            setVisible(false);
            TelaAdminLivros adminLivros = new TelaAdminLivros();
            adminLivros.setVisible(true);
    }//GEN-LAST:event_btnVoltarActionPerformed

    private void btnVoltarPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_btnVoltarPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_btnVoltarPropertyChange
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaCadastroLivro().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCadastrarLivro;
    private javax.swing.JButton btnVoltar;
    private javax.swing.JTextField cadAno;
    private javax.swing.JTextField cadAutor;
    private javax.swing.JTextField cadEdicao;
    private javax.swing.JTextField cadEditora;
    private javax.swing.JTextField cadISBN;
    private javax.swing.JTextField cadNomeLivro;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    // End of variables declaration//GEN-END:variables
}
