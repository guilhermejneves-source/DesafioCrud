#  Desafio Extra - CRUD com Integração ViaCEP

Aplicação interativa em Java utilizando Maven para o gerenciamento de dependências, conexão com banco de dados MySQL via JDBC puro, e consumo de API externa para enriquecimento de dados.

##  Funcionalidades
- **CRUD Completo de Duas Entidades Relacionadas:** Vínculo de 1 para 1 entre `Client` (Cliente) e `Address` (Endereço) com exclusão em cascata (`ON DELETE CASCADE`).
- **Consumo de API Externa:** Integração com a API do **ViaCEP** para buscar e preencher automaticamente o endereço do cliente a partir do CEP informado.
- **Segurança com Variáveis de Ambiente:** Utilização da biblioteca `dotenv-java` para mascarar as credenciais do banco de dados.
- **Menu Interativo:** Interface via Console (Terminal) com validações contra erros de input de dados.

##  Tecnologias e Ferramentas Utilizadas
- **Java 17**
- **Maven** (Gerenciador de Dependências)
- **MySQL** (Banco de Dados Relacional)
- **Driver MySQL Connector/J** (Conexão JDBC)
- **Org.JSON** (Processamento da resposta da API)
- **Dotenv-Java** (Gerenciamento do arquivo `.env`)

##  Pré-requisitos e Instalação

1. Clone este repositório para a sua máquina local.
2. Certifique-se de ter o **MySQL** instalado e rodando.
3. Crie o banco de dados e as tabelas executando o seguinte script no seu terminal SQL:

```sql
CREATE DATABASE IF NOT EXISTS sistema_clientes;
USE sistema_clientes;

CREATE TABLE addresses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cep VARCHAR(9) NOT NULL,
    logradouro VARCHAR(100),
    bairro VARCHAR(50),
    cidade VARCHAR(50),
    estado VARCHAR(2)
);

CREATE TABLE clients (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    address_id INT,
    FOREIGN KEY (address_id) REFERENCES addresses(id) ON DELETE CASCADE
);