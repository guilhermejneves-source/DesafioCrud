package com.neves.DesafioCrud.model;

public class Address {
    // Atributos que representam as colunas do banco de dados e os dados que vem da API
    private int id;
    private String cep;
    private String logradouro;
    private String bairro;
    private String cidade;
    private String estado;

    // Construtor vazio: Necessário para o Java conseguir criar o objeto antes de preencher os dados
    public Address() {}

    // Construtor cheio: Usado para criar o endereço com todos os dados de uma vez só
    public Address(String cep, String logradouro, String bairro, String cidade, String estado) {
        this.cep = cep;
        this.logradouro = logradouro;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
    }

    // Getters e Setters: Permitem ler (get) e alterar (set) os dados protegidos (private)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }

    public String getLogradouro() { return logradouro; }
    public void setLogradouro(String logradouro) { this.logradouro = logradouro; }

    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
