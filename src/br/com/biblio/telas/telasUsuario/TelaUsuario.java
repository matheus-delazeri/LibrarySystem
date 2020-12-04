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
ResultSet rsReservar = null;
    public TelaUsuario() {
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
            DefaultTableModel modeloTabela = (DefaultTableModel)tableLivrosUsuario.getModel(); 
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
    public void listarLivrosReservados(String user){
        String sql ="select * from livros where user_que_reservou=?";
        String disponibilidade;
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, user);
            rs = pst.executeQuery();
            DefaultTableModel modeloTabela = (DefaultTableModel)tableLivrosReservados.getModel(); 
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
    public void displayTela(String user){
        String sql= "select * from users where usuario=?";
        try{
            pst = conexao.prepareStatement(sql);
            pst.setString(1, user);
            rs = pst.executeQuery();
            if (rs.next()){
                String cargo = rs.getString(4);
                userField.setText(rs.getString(3));
                userFieldReservas.setText(rs.getString(3));
                cargoField.setText(cargo);
                emprestadoField.setText(String.valueOf(verificarNumEmprestimos(user, cargo)));
                if("Professor".equals(cargo)){
                    maxEmpField.setText("5");
                }else{
                    maxEmpField.setText("3");
                }
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null,e);
        }
        listarLivrosReservados(user);
    }
    private void buscarLivro(){
        String disponibilidade;
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

                    DefaultTableModel modeloTabela = (DefaultTableModel)tableLivrosUsuario.getModel(); 
                    modeloTabela.setRowCount(0);       
                    if (rs.next()) {
                        do{
                            if(rs.getString(8) == null){
                                disponibilidade = "Disponível";
                            }else{
                                disponibilidade = "Alugado";
                            }
                            modeloTabela.addRow(new String[] {rs.getString("nome_livro"), rs.getString("autores"), rs.getString("edicao"), rs.getString("editora"), rs.getString("ano"), rs.getString("isbn"), disponibilidade});
                        }while(rs.next());
                    }else{
                        JOptionPane.showMessageDialog(null,"Nenhum livro cadastrado com essas informações! Tente novamente!");
                    }
                }
        } catch (Exception e) {
                JOptionPane.showMessageDialog(null,e);
        }
    }
    private void verificarEmprestimo(){
        DefaultTableModel modeloTabela = (DefaultTableModel)tableLivrosUsuario.getModel(); 
        String strIsbn = null;
        int row = tableLivrosUsuario.getSelectedRow();
        if(row == -1){
            JOptionPane.showMessageDialog(null, "Selecione a linha do livro que deseja realizar o empréstimo!");
        }else{
            Object isbn = modeloTabela.getValueAt(row, 5);
            strIsbn = isbn.toString();
            String sql= "select * from livros where isbn=?";
            String updateSql = "update livros set user_que_reservou=? where isbn=?";
            try{
                String user = userField.getText();
                String cargo = cargoField.getText();
                pst = conexao.prepareStatement(sql);
                pst.setString(1, strIsbn);
                rs = pst.executeQuery();
                if(rs.next() && rs.getString(8) == null){
                    if(rs.getString(10) == null || rs.getString(10).equals(userField.getText())){
                        int numEmprestimos = verificarNumEmprestimos(user, cargo);
                        boolean naoTemMultas = verificarMultas(user);
                        if(naoTemMultas){
                            if((numEmprestimos != -1 && "Professor".equals(cargo) && numEmprestimos != 5) || (numEmprestimos != -1 && "Estudante".equals(cargo) && numEmprestimos != 3)){
                                pst = conexao.prepareStatement(updateSql);
                                pst.setString(1, null);
                                pst.setString(2, strIsbn);
                                pst.executeUpdate();
                                fazerEmprestimo(strIsbn, user);
                            }else{
                                JOptionPane.showMessageDialog(null,"Você já excedeu o número máximo de empréstimos por vez!");
                            }
                        }else{
                            JOptionPane.showMessageDialog(null,"Usuários com multa não podem pegar livros emprestados!");
                        }
                    }else{
                        JOptionPane.showMessageDialog(null,"Este livro já está reservado!");
                    }
                }else{
                    JOptionPane.showMessageDialog(null,"Este livro já foi alugado!");
                }
            }catch (Exception e){
                JOptionPane.showMessageDialog(null, "Erro ao verificar disponibilidade de emprestimo");
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
            JOptionPane.showMessageDialog(null, "Erro ao verificar num de emprestimos!");
        }
        if(("Professor".equals(cargo) && cont <= 5) || ("Estudante".equals(cargo) && cont <= 3)){
            return cont;
        }else{
            return -1;
        }
    }
    private boolean verificarMultas(String user){
        boolean naoTemMultas = false;
        String sql = "select * from users where usuario=?";
        try{
            pst = conexao.prepareStatement(sql);
            pst.setString(1, user);
            rs = pst.executeQuery();
            if(rs.next()){
                int qntMultas = rs.getInt(5);
                if(qntMultas == 0){
                    naoTemMultas = true;
                }else{
                    naoTemMultas = false;
                }
            }
        } catch(Exception e){
            JOptionPane.showMessageDialog(null,"Erro ao verificar multas");
        }
    return naoTemMultas;
    }
    private void fazerEmprestimo(String isbn, String user){
        LocalDate hoje = java.time.LocalDate.now();  
        Date convHoje = Date.valueOf(hoje);
        int confirma=JOptionPane.showConfirmDialog(null, "Tem certeza que deseja fazer o empréstimo do livro selecionado?");
        if (confirma==JOptionPane.YES_OPTION){
            String sql= "update livros set user_que_alugou=?, dia_que_alugou=? where isbn=?";
            try{
                pst = conexao.prepareStatement(sql);
                pst.setString(1, user);
                pst.setDate(2, convHoje);
                pst.setString(3, isbn);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null,"O empréstimo foi feito com sucesso!");
                setVisible(false);
                TelaUsuario telaUsuario = new TelaUsuario();
                telaUsuario.displayTela(user);
                telaUsuario.setVisible(true);
            }catch(Exception e){
                JOptionPane.showMessageDialog(null,"Erro na função marcarLivroEmprestado!");
            }
        }
    }
    private void reservarLivroSelecionado(){
        DefaultTableModel modeloTabela = (DefaultTableModel)tableLivrosUsuario.getModel(); 
        String strIsbn = null;
        int row = tableLivrosUsuario.getSelectedRow();
        if(row == -1){
            JOptionPane.showMessageDialog(null, "Selecione a linha do livro que deseja reservar!");
        }else{
            Object isbn = modeloTabela.getValueAt(row, 5);
            strIsbn = isbn.toString();
            String updateSql = "update livros set user_que_reservou=? where isbn=?";
            String sql = "select * from livros where isbn=?";
            try{
                pst = conexao.prepareStatement(sql);
                pst.setString(1, strIsbn);
                rsReservar = pst.executeQuery();
                if(rsReservar.next() && verificarMultas(userField.getText())){
                    if(rsReservar.getString(10) == null){
                        pst = conexao.prepareStatement(updateSql);
                        pst.setString(1, userField.getText());
                        pst.setString(2, strIsbn);
                        pst.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Livro reservado com sucesso!");
                        setVisible(false);
                        TelaUsuario telaUsuario = new TelaUsuario();
                        telaUsuario.displayTela(userField.getText());
                        telaUsuario.setVisible(true);
                    }else{
                        JOptionPane.showMessageDialog(null, "Esse livro já está reservado! Tente novamente mais tarde.");
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Usuários com multas não podem reservar livros!");
                }
            } catch(Exception e){
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    private void cancelarReserva(){
        DefaultTableModel modeloTabela = (DefaultTableModel)tableLivrosReservados.getModel(); 
        String strIsbn = null;
        int row = tableLivrosReservados.getSelectedRow();
        if(row == -1){
            JOptionPane.showMessageDialog(null, "Selecione a linha do livro que deseja reservar!");
        }else{
            int confirma=JOptionPane.showConfirmDialog(null, "Tem certeza que deseja cancelar a reserva do livro selecionado?");
            if (confirma==JOptionPane.YES_OPTION){
                Object isbn = modeloTabela.getValueAt(row, 5);
                strIsbn = isbn.toString();
                String updateSql = "update livros set user_que_reservou=? where isbn=?";
                try{
                    pst = conexao.prepareStatement(updateSql);
                    pst.setString(1, null);
                    pst.setString(2, strIsbn);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Reserva cancelada com sucesso!");
                    setVisible(false);
                    TelaUsuario telaUsuario = new TelaUsuario();
                    telaUsuario.displayTela(userField.getText());
                    telaUsuario.setVisible(true);
                } catch(Exception e){
                    JOptionPane.showMessageDialog(null, e);
                }
            }
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        tableLivrosReservados = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        userField = new javax.swing.JTextField();
        emprestadoField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        maxEmpField = new javax.swing.JTextField();
        btnAlugarLivro = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        cargoField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        btnReservar = new javax.swing.JButton();
        searchField = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        btnVoltar = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableLivrosUsuario = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        userFieldReservas = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        btnCancelarReserva = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        tableLivrosReservados.setModel(new javax.swing.table.DefaultTableModel(
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
        tableLivrosReservados.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tableLivrosReservados.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tableLivrosReservados);
        if (tableLivrosReservados.getColumnModel().getColumnCount() > 0) {
            tableLivrosReservados.getColumnModel().getColumn(0).setResizable(false);
            tableLivrosReservados.getColumnModel().getColumn(2).setMinWidth(50);
            tableLivrosReservados.getColumnModel().getColumn(2).setMaxWidth(50);
            tableLivrosReservados.getColumnModel().getColumn(4).setMinWidth(50);
            tableLivrosReservados.getColumnModel().getColumn(4).setMaxWidth(50);
        }

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        userField.setEditable(false);
        userField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        emprestadoField.setEditable(false);
        emprestadoField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel1.setText("Livros emprestados:");

        jLabel2.setText(" de");

        maxEmpField.setEditable(false);
        maxEmpField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        btnAlugarLivro.setText("Alugar livro selecionado");
        btnAlugarLivro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlugarLivroActionPerformed(evt);
            }
        });

        jLabel3.setText("Logado como:");

        cargoField.setEditable(false);
        cargoField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel4.setText("Cargo");

        btnReservar.setText("Solicitar reserva do livro selecionado");
        btnReservar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReservarActionPerformed(evt);
            }
        });

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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(maxEmpField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(userField))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cargoField))
                    .addComponent(btnReservar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(userField)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(9, 9, 9)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cargoField, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(emprestadoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(maxEmpField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addComponent(btnAlugarLivro)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnReservar)
                .addGap(21, 21, 21))
        );

        btnSearch.setText("Buscar");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        btnVoltar.setText("Voltar");
        btnVoltar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVoltarActionPerformed(evt);
            }
        });

        tableLivrosUsuario.setModel(new javax.swing.table.DefaultTableModel(
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
        tableLivrosUsuario.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tableLivrosUsuario.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(tableLivrosUsuario);
        if (tableLivrosUsuario.getColumnModel().getColumnCount() > 0) {
            tableLivrosUsuario.getColumnModel().getColumn(0).setResizable(false);
            tableLivrosUsuario.getColumnModel().getColumn(2).setMinWidth(50);
            tableLivrosUsuario.getColumnModel().getColumn(2).setMaxWidth(50);
            tableLivrosUsuario.getColumnModel().getColumn(4).setMinWidth(50);
            tableLivrosUsuario.getColumnModel().getColumn(4).setMaxWidth(50);
        }

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setText("Livros reservados para");

        userFieldReservas.setEditable(false);
        userFieldReservas.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btnCancelarReserva.setText("Cancelar reserva do livro selecionado");
        btnCancelarReserva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarReservaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnCancelarReserva)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnCancelarReserva)
                .addContainerGap(161, Short.MAX_VALUE))
        );

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel6.setText("Todos os livros");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(378, 378, 378)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(userFieldReservas, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 902, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(85, 85, 85))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnVoltar, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(211, 211, 211)
                        .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(41, 41, 41)
                        .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 397, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(475, 475, 475)
                        .addComponent(jLabel6)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(userFieldReservas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(39, 39, 39)
                .addComponent(btnVoltar)
                .addContainerGap())
        );

        setSize(new java.awt.Dimension(1240, 688));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        buscarLivro();
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnAlugarLivroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlugarLivroActionPerformed
        verificarEmprestimo();
    }//GEN-LAST:event_btnAlugarLivroActionPerformed

    private void btnVoltarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVoltarActionPerformed
        setVisible(false);
        TelaInfoUsuario telaInfoUsuario = new TelaInfoUsuario(userField.getText());
        telaInfoUsuario.setVisible(true);
    }//GEN-LAST:event_btnVoltarActionPerformed

    private void btnReservarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReservarActionPerformed
        reservarLivroSelecionado();
    }//GEN-LAST:event_btnReservarActionPerformed

    private void btnCancelarReservaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarReservaActionPerformed
        cancelarReserva();
    }//GEN-LAST:event_btnCancelarReservaActionPerformed
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaUsuario().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlugarLivro;
    private javax.swing.JButton btnCancelarReserva;
    private javax.swing.JButton btnReservar;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnVoltar;
    private javax.swing.JTextField cargoField;
    private javax.swing.JTextField emprestadoField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField maxEmpField;
    private javax.swing.JTextField searchField;
    private javax.swing.JTable tableLivrosReservados;
    private javax.swing.JTable tableLivrosUsuario;
    private javax.swing.JTextField userField;
    private javax.swing.JTextField userFieldReservas;
    // End of variables declaration//GEN-END:variables
}
