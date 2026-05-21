package com.neves.DesafioCrud.service;
import com.neves.DesafioCrud.model.Address;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class ViaCepService {

    public static Address buscarEnderecoPorCEP(String cep) {
        // Limpa o CEP tirando qualquer traço ou espaço que o usuário digitar
        String cepLimpo = cep.replaceAll("[^0-9]", "");

        if (cepLimpo.length() != 8) {
            System.out.println("❌ CEP inválido! Deve conter exatamente 8 números.");
            return null;
        }

        try {
            // Cria a URL de chamada da API do ViaCEP
            URL url = new URL("https://viacep.com.br/ws/" + cepLimpo + "/json/");
            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
            conexao.setRequestMethod("GET"); // Requisição do tipo GET (buscar dados)

            if (conexao.getResponseCode() != 200) {
                System.out.println("❌ Erro ao conectar na API ViaCEP. Código HTTP: " + conexao.getResponseCode());
                return null;
            }

            // Lê a resposta de texto que a API devolve
            BufferedReader leitor = new BufferedReader(new InputStreamReader(conexao.getInputStream(), "UTF-8"));
            StringBuilder respostaTexto = new StringBuilder();
            String linha;

            while ((linha = leitor.readLine()) != null) {
                respostaTexto.append(linha);
            }
            leitor.close();

            // Transforma o texto recebido em um objeto JSON gerenciável
            JSONObject json = new JSONObject(respostaTexto.toString());

            // Se o ViaCEP retornar que o CEP não existe na base deles
            if (json.has("erro")) {
                System.out.println("❌ CEP não encontrado na base de dados do ViaCEP.");
                return null;
            }

            // Cria o objeto Address e preenche ele com os dados vindos do JSON da API
            Address endereco = new Address();
            endereco.setCep(json.getString("cep"));
            endereco.setLogradouro(json.getString("logradouro"));
            endereco.setBairro(json.getString("bairro"));
            endereco.setCidade(json.getString("localidade")); // No ViaCEP a cidade se chama 'localidade'
            endereco.setEstado(json.getString("uf"));         // No ViaCEP o estado se chama 'uf'

            return endereco;

        } catch (Exception e) {
            System.out.println("❌ Ocorreu um erro ao buscar o CEP: " + e.getMessage());
            return null;
        }
    }
}