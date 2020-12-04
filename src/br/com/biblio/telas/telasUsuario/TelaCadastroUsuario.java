package br.com.biblio.telas.telasUsuario;

import java.sql.*;
import br.com.biblio.dal.ModuloConexao;
import javax.swing.JOptionPane;

public class TelaCadastroUsuario extends javax.swing.JFrame {
Connection conexao = null;
PreparedStatement pst = null;
ResultSet rs = null;    
    public TelaCadastroUsuario() {
        initComponents();
        conexao = ModuloConexao.conector();
    }
    public boolean verificarUsuario(String usuario){
        String sql = "select * from users where usuario=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, usuario);
            rs = pst.executeQuery();
            if (rs.next()){
                return true;
           }else{
               return false;
           }
        } catch (Exception e) {
            return false;
        }
    }
    public void Cadastrar(){
    String usuario = cadUser.getText();
    boolean verificador = verificarUsuario(usuario);
   
    String sql= "insert into users(usuario, firstname, lastname, cargo) values(?, ?, ?, ?)";
        try {
           pst = conexao.prepareStatement(sql);
           if(verificador != true){
                pst.setString(1, cadUser.getText());
           }else{
               JOptionPane.showMessageDialog(null, "Usuário já em uso!");
           }
           pst.setString(2, cadNome.getText());
           pst.setString(3, cadSobrenome.getText());
           pst.setString(4, cadCheckProf.getSelectedItem().toString());
           
           if(cadUser.getText().equals("") || cadNome.getText().equals("") || cadSobrenome.getText().equals("") || cadCheckProf.getSelectedItem().toString().equals("")){
                JOptionPane.showMessageDialog(null, "Todos os campos devem ser preenchidos!"); 
           }else{
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Usuário cadastrado com sucesso!");
                setVisible(false);
                TelaAdminUsuarios telaAdminUsuarios = new TelaAdminUsuarios();
                telaAdminUsuarios.setVisible(true);
           }
        } catch (Exception e) {}
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cadUser = new javax.swing.JTextField();
        cadNome = new javax.swing.JTextField();
        cadSobrenome = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        btnCadastrar = new javax.swing.JButton();
        cadCheckProf = new javax.swing.JComboBox<>();
        btnVoltar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Cadastro");
        setResizable(false);

        jLabel1.setText("Nome:");

        jLabel2.setText("Sobrenome:");

        jLabel3.setText("Usuário:");

        jLabel6.setText("Profissão:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("CADASTRAR USUÁRIO");

        btnCadastrar.setText("Cadastrar");
        btnCadastrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadastrarActionPerformed(evt);
            }
        });
        btnCadastrar.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                btnCadastrarPropertyChange(evt);
            }
        });

        cadCheckProf.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Professor", "Estudante" }));

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
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(btnVoltar)
                            .addGap(307, 307, 307)
                            .addComponent(btnCadastrar))
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel6)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(cadCheckProf, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(11, 11, 11)
                                    .addComponent(cadNome)))
                            .addGap(35, 35, 35)
                            .addComponent(jLabel2)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(cadSobrenome, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cadUser, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(71, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(43, 43, 43)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cadUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cadNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cadSobrenome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cadCheckProf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnVoltar)
                    .addComponent(btnCadastrar))
                .addGap(19, 19, 19))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    private void btnCadastrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadastrarActionPerformed
        Cadastrar();  
    }//GEN-LAST:event_btnCadastrarActionPerformed
    private void btnCadastrarPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_btnCadastrarPropertyChange
    }//GEN-LAST:event_btnCadastrarPropertyChange

    private void btnVoltarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVoltarActionPerformed
        setVisible(false);
        TelaAdminUsuarios telaAdminUsuarios = new TelaAdminUsuarios();
        telaAdminUsuarios.setVisible(true);
    }//GEN-LAST:event_btnVoltarActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaCadastroUsuario().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCadastrar;
    private javax.swing.JButton btnVoltar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cadCheckProf;
    private javax.swing.JTextField cadNome;
    private javax.swing.JTextField cadSobrenome;
    private javax.swing.JTextField cadUser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    // End of variables declaration//GEN-END:variables
}
