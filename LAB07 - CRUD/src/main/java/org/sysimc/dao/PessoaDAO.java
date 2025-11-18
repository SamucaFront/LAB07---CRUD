package org.sysimc.dao;

import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import org.sysimc.model.Pessoa;
import org.sysimc.utils.ConnectionPostgres;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class PessoaDAO {


    public List<Pessoa> listarTodos() {
        List<Pessoa> lista = new ArrayList<>();

        String sql = "SELECT id, nome, peso, altura, imc, classificacao FROM pessoa ORDER BY id";

        try (Connection conn = ConnectionPostgres.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Pessoa p = new Pessoa();
                p.setId(rs.getInt("id"));
                p.setNome(rs.getString("nome"));
                p.setPeso(rs.getFloat("peso"));
                p.setAltura(rs.getFloat("altura"));
                p.setImc(rs.getFloat("imc"));
                p.setClassificacao(rs.getString("classificacao"));
                lista.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public void inserir (Pessoa p) {
        String sql = "INSERT INTO pessoa (nome, peso, altura, imc, classificacao) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionPostgres.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, p.getNome());
            ps.setFloat(2, p.getPeso());
            ps.setFloat(3, p.getAltura());
            ps.setFloat(4, p.getImc());
            ps.setString(5, p.getClassificacao());

            ps.executeUpdate();

            // pega o ID gerado pelo banco
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    p.setId(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void excluir(int id) {
        String sql = "DELETE FROM pessoa WHERE id = ?";

        try (Connection conn = ConnectionPostgres.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
    public class MainController implements Initializable {

        private Pessoa pessoa;
        private List<Pessoa> listaPessoas;
        private ObservableList<Pessoa> observableListPessoas;

        private PessoaDAO pessoaDAO = new PessoaDAO();


        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {

        }
    }


}