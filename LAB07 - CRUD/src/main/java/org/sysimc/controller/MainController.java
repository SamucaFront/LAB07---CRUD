package org.sysimc.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.sysimc.dao.PessoaDAO;
import org.sysimc.model.Pessoa;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    // VARIAVEIS CAIXA DE TEXTO TEXTFIELD
    @FXML
    protected TextField txtNome;
    @FXML
    protected TextField txtPeso;
    @FXML
    protected TextField txtAltura;
    // VARIAVEIS LABEL
    @FXML
    protected Label lbIMC;
    @FXML
    protected Label lbClassificacao;

    // VARIAVEIS TABELA
    @FXML
    TableView<Pessoa> tbPessoas;
    @FXML
    TableColumn<Pessoa, Integer> colId;
    @FXML
    TableColumn<Pessoa, String> colNome;
    @FXML
    TableColumn<Pessoa, Float> colPeso;
    @FXML
    TableColumn<Pessoa, Float> colAltura;
    @FXML
    TableColumn<Pessoa, Float> colIMC;
    @FXML
    TableColumn<Pessoa, String> colClassificação ;


    // Objeto Modelo
    Pessoa pessoa;
    List<Pessoa> listaPessoas;
    ObservableList<Pessoa> observableListPessoas;
    PessoaDAO pessoaDAO;

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.pessoa = new Pessoa();
        this.listaPessoas = new ArrayList<>();
        iniciarGUI();

        // 1) Carrega do BANCO
        this.listaPessoas = pessoaDAO.listarTodos();
        atualizarTableView();

        // 2) (Opcional) Gera o CSV a partir do banco ao iniciar
        try (java.io.BufferedWriter writer =
                     java.nio.file.Files.newBufferedWriter(
                             java.nio.file.Paths.get("dados_pessoas.txt"),
                             java.nio.charset.StandardCharsets.UTF_8)) {

            for (Pessoa p : listaPessoas) {
                if (p.getImc() == null || p.getClassificacao() == null) {
                    p.classificacaoIMC();
                }

                String linha = String.format(
                        "%d,%s,%.2f,%.2f,%.2f,%s",
                        p.getId(),
                        p.getNome(),
                        p.getPeso(),
                        p.getAltura(),
                        p.getImc(),
                        p.getClassificacao()
                );
                writer.write(linha);
                writer.newLine();
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public void iniciarGUI(){
        // VINCULAR AS CADA CELULA DA LINHA DA TABELA COM O ATRIBUTO DO OBJETO PESSOA
        this.colId.setCellValueFactory( new PropertyValueFactory<>("id"));
        this.colNome.setCellValueFactory( new PropertyValueFactory<>("nome"));
        this.colPeso.setCellValueFactory( new PropertyValueFactory<>("peso"));
        this.colAltura.setCellValueFactory( new PropertyValueFactory<>("altura"));
        this.colIMC.setCellValueFactory( new PropertyValueFactory<>("imc"));
        this.colClassificação.setCellValueFactory(new PropertyValueFactory<>("classificacao"));
    }
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // EVENTOS
    @FXML
    public void onClickCalcularIMC(){
        lerFormulario();
        this.pessoa.classificacaoIMC();
        exibirClassificacaoIMC();
        exibirClassificacaoIMC();
        System.out.println(this.pessoa.toString());
    }
    //---------------------


    @FXML
    public void onClickSalvarIMC() {
        // 1) Ler dados da tela
        lerFormulario();

        // 2) Calcular IMC e classificação
        this.pessoa.classificacaoIMC();

        // 3) Salvar no BANCO
        pessoaDAO.inserir(this.pessoa);

        // 4) Recarregar lista a partir do BANCO
        this.listaPessoas = pessoaDAO.listarTodos();
        atualizarTableView();

        // 5) Atualizar o CSV com a lista inteira
        java.nio.file.Path path = java.nio.file.Paths.get("dados_pessoas.txt");

        try (java.io.BufferedWriter writer =
                     java.nio.file.Files.newBufferedWriter(path, java.nio.charset.StandardCharsets.UTF_8)) {

            for (Pessoa p : listaPessoas) {
                if (p.getImc() == null || p.getClassificacao() == null) {
                    p.classificacaoIMC();
                }

                String linha = String.format(
                        "%d,%s,%.2f,%.2f,%.2f,%s",
                        p.getId(),
                        p.getNome(),
                        p.getPeso(),
                        p.getAltura(),
                        p.getImc(),
                        p.getClassificacao()
                );
                writer.write(linha);
                writer.newLine();
            }

        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

        // 6) Preparar para o próximo cadastro
        this.pessoa = new Pessoa();
        txtNome.clear();
        txtPeso.clear();
        txtAltura.clear();
    }




    @FXML
    public void onClickNovo(){

        this.pessoa = new Pessoa();
        txtNome.setText("");
        txtPeso.setText("");
        txtAltura.setText("");
    }


    @FXML
    public void onClickExcluir() {
        // pega a pessoa selecionada na tabela
        Pessoa selecionada = tbPessoas.getSelectionModel().getSelectedItem();

        if (selecionada != null) {
            // remove da lista usando o id (caso você queira deixar explícito)
            listaPessoas.removeIf(p -> p.getId() == selecionada.getId());

            pessoaDAO.excluir(selecionada.getId());
            // recarrega lista
            this.listaPessoas = pessoaDAO.listarTodos();

            // atualiza a tabela
            atualizarTableView();
        } else {
            System.out.println("Nenhuma pessoa selecionada para exclusão.");
            // depois você pode trocar isso por um Alert do JavaFX
        }



    }


    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // FORMULARIO
    public void lerFormulario(){
        this.pessoa.setNome( txtNome.getText() );
        this.pessoa.setPeso( Float.parseFloat(txtPeso.getText()) );
        this.pessoa.setAltura( Float.parseFloat(txtAltura.getText()) );

    }
    public void exibirClassificacaoIMC(){
        DecimalFormat df = new DecimalFormat("#0.00");
        lbIMC.setText(df.format(this.pessoa.getImc()));
        lbClassificacao.setText(this.pessoa.getClassificacao() );
    }
    public void atualizarTableView(){

        this.listaPessoas.forEach( obj -> System.out.printf(obj.getNome() +", " + obj.getPeso() +", " + obj.getAltura() +"\n"));
        this.observableListPessoas = FXCollections.observableList(this.listaPessoas);
        this.tbPessoas.setItems(this.observableListPessoas);

    }



}
