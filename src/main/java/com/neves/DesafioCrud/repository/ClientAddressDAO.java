package com.neves.DesafioCrud.repository;

import com.neves.DesafioCrud.config.DatabaseConfig;
import com.neves.DesafioCrud.model.Address;
import com.neves.DesafioCrud.model.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientAddressDAO {

    // 1. CREATE: Salva o Endereço e o Cliente no Banco de Dados
    public void salvar(Client client) {
        String sqlAddress = "INSERT INTO addresses (cep, logradouro, bairro, cidade, estado) VALUES (?, ?, ?, ?, ?)";
        String sqlClient = "INSERT INTO clients (nome, email, address_id) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection()) {
            conn.setAutoCommit(false); // Transação: garante segurança nos dados relacionados

            // Salva o Endereço primeiro para descobrir o ID que o banco vai gerar para ele
            try (PreparedStatement stmtAdd = conn.prepareStatement(sqlAddress, Statement.RETURN_GENERATED_KEYS)) {
                stmtAdd.setString(1, client.getAddress().getCep());
                stmtAdd.setString(2, client.getAddress().getLogradouro());
                stmtAdd.setString(3, client.getAddress().getBairro());
                stmtAdd.setString(4, client.getAddress().getCidade());
                stmtAdd.setString(5, client.getAddress().getEstado());
                stmtAdd.executeUpdate();

                ResultSet rs = stmtAdd.getGeneratedKeys();
                if (rs.next()) {
                    client.getAddress().setId(rs.getInt(1));
                }
            }

            // Salva o Cliente vinculando o ID do endereço criado acima (Chave Estrangeira)
            try (PreparedStatement stmtCli = conn.prepareStatement(sqlClient)) {
                stmtCli.setString(1, client.getNome());
                stmtCli.setString(2, client.getEmail());
                stmtCli.setInt(3, client.getAddress().getId());
                stmtCli.executeUpdate();
            }

            conn.commit(); // Confirma as duas gravações
            System.out.println("✅ Cliente e Endereço cadastrados com sucesso!");

        } catch (SQLException e) {
            System.out.println("❌ Erro ao salvar no banco: " + e.getMessage());
        }
    }

    // 2. READ: Lista todos os Clientes com seus Endereços juntos
    public List<Client> listarTodos() {
        List<Client> lista = new ArrayList<>();
        String sql = "SELECT c.id AS cli_id, c.nome, c.email, a.id AS add_id, a.cep, a.logradouro, a.bairro, a.cidade, a.estado " +
                "FROM clients c INNER JOIN addresses a ON c.address_id = a.id";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Address add = new Address();
                add.setId(rs.getInt("add_id"));
                add.setCep(rs.getString("cep"));
                add.setLogradouro(rs.getString("logradouro"));
                add.setBairro(rs.getString("bairro"));
                add.setCidade(rs.getString("cidade"));
                add.setEstado(rs.getString("estado"));

                Client cli = new Client();
                cli.setId(rs.getInt("cli_id"));
                cli.setNome(rs.getString("nome"));
                cli.setEmail(rs.getString("email"));
                cli.setAddress(add);

                lista.add(cli);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erro ao listar clientes: " + e.getMessage());
        }
        return lista;
    }

    // 3. UPDATE: Atualiza os dados do cliente e do endereço dele
    public void atualizar(Client client) {
        String sqlClient = "UPDATE clients SET nome = ?, email = ? WHERE id = ?";
        String sqlAddress = "UPDATE addresses SET cep = ?, logradouro = ?, bairro = ?, cidade = ?, estado = ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmtCli = conn.prepareStatement(sqlClient)) {
                stmtCli.setString(1, client.getNome());
                stmtCli.setString(2, client.getEmail());
                stmtCli.setInt(3, client.getId());
                stmtCli.executeUpdate();
            }

            try (PreparedStatement stmtAdd = conn.prepareStatement(sqlAddress)) {
                stmtAdd.setString(1, client.getAddress().getCep());
                stmtAdd.setString(2, client.getAddress().getLogradouro());
                stmtAdd.setString(3, client.getAddress().getBairro());
                stmtAdd.setString(4, client.getAddress().getCidade());
                stmtAdd.setString(5, client.getAddress().getEstado());
                stmtAdd.setInt(6, client.getAddress().getId());
                stmtAdd.executeUpdate();
            }

            conn.commit();
            System.out.println("✅ Dados atualizados com sucesso!");
        } catch (SQLException e) {
            System.out.println("❌ Erro ao atualizar dados: " + e.getMessage());
        }
    }

    // 4. DELETE: Apaga o cliente (o MySQL apaga o endereço sozinho por causa do ON DELETE CASCADE)
    public void deletar(int clientId) {
        String sql = "DELETE FROM clients WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, clientId);
            int linhas = stmt.executeUpdate();

            if (linhas > 0) {
                System.out.println("✅ Cliente removido do sistema com sucesso!");
            } else {
                System.out.println("❌ Nenhum cliente encontrado com o ID: " + clientId);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erro ao deletar cliente: " + e.getMessage());
        }
    }
}