package com.neves.DesafioCrud.config;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {

    // Carrega o arquivo .env que está na raiz do projeto
    private static final Dotenv dotenv = Dotenv.configure().load();

    // Pega as variáveis mágicas do arquivo .env sem expor no código
    private static final String URL = dotenv.get("DB_URL");
    private static final String USER = dotenv.get("DB_USER");
    private static final String PASSWORD = dotenv.get("DB_PASSWORD");

    // Método que abre a porta de conexão com o banco MySQL
    public static Connection getConnection() throws SQLException {
        try {
            // Garante que o driver do MySQL que colocamos no Maven seja carregado
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("❌ Driver do MySQL não encontrado: " + e.getMessage());
        }
    }
}