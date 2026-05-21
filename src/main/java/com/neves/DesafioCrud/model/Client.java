package com.neves.DesafioCrud.model;

public class Client {
    private int id;
    private String nome;
    private String email;

    // Aqui está o relacionamento! O cliente guarda um objeto do tipo Address dentro dele
    private Address address;

    public Client() {}

    public Client(String nome, String email, Address address) {
        this.nome = nome;
        this.email = email;
        this.address = address;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }
}