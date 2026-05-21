package com.neves.DesafioCrud;

import com.neves.DesafioCrud.model.Address;
import com.neves.DesafioCrud.model.Client;
import com.neves.DesafioCrud.repository.ClientAddressDAO;
import com.neves.DesafioCrud.service.ViaCepService;

import java.util.List;
import java.util.Scanner;


public class DesafioCrudApplication {

	public static void main(String[] args) {
		ClientAddressDAO dao = new ClientAddressDAO();
		Scanner scanner = new Scanner(System.in);
		int opcao = -1;

		System.out.println("🚀 BEM-VINDO AO SISTEMA DE GESTÃO DE CLIENTES NEVES 🚀");

		while (opcao != 0) {
			System.out.println("\n============== MENU CRUD ==============");
			System.out.println("1 - Cadastrar Cliente (Busca Automática via CEP)");
			System.out.println("2 - Listar Todos os Clientes");
			System.out.println("3 - Atualizar Dados de um Cliente");
			System.out.println("4 - Deletar um Cliente");
			System.out.println("0 - Sair do Sistema");
			System.out.print("Escolha uma opção: ");

			try {
				opcao = Integer.parseInt(scanner.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("❌ Digite apenas números válidos!");
				continue;
			}

			switch (opcao) {
				case 1:
					System.out.print("\nNome do Cliente: ");
					String nome = scanner.nextLine();
					System.out.print("Email do Cliente: ");
					String email = scanner.nextLine();
					System.out.print("Digite o CEP para buscar o endereço (Apenas números): ");
					String cep = scanner.nextLine();

					// Consumindo a API Externa do ViaCEP!
					Address endereco = ViaCepService.buscarEnderecoPorCEP(cep);

					if (endereco != null) {
						System.out.println("\n🌍 Endereço Encontrado pela API:");
						System.out.println("Rua: " + endereco.getLogradouro() + " | Bairro: " + endereco.getBairro() + " | Cidade: " + endereco.getCidade() + "-" + endereco.getEstado());

						Client novoCliente = new Client(nome, email, endereco);
						dao.salvar(novoCliente);
					} else {
						System.out.println("❌ Falha ao cadastrar cliente por erro no CEP.");
					}
					break;

				case 2:
					System.out.println("\n📋 LISTA DE CLIENTES CADASTRADOS:");
					List<Client> clientes = dao.listarTodos();
					if (clientes.isEmpty()) {
						System.out.println("Nenhum cliente cadastrado no momento.");
					} else {
						for (Client c : clientes) {
							System.out.println("ID: " + c.getId() + " | Nome: " + c.getNome() + " | Email: " + c.getEmail());
							System.out.println("   📍 Endereço: " + c.getAddress().getLogradouro() + ", " + c.getAddress().getBairro() + " - " + c.getAddress().getCidade() + "/" + c.getAddress().getEstado() + " (" + c.getAddress().getCep() + ")");
						}
					}
					break;

				case 3:
					System.out.print("\nDigite o ID do cliente que deseja atualizar: ");
					int idAtualizar;
					try {
						idAtualizar = Integer.parseInt(scanner.nextLine());
					} catch (NumberFormatException e) {
						System.out.println("❌ ID inválido.");
						break;
					}

					// Busca rápida na lista para pegar o ID do endereço antigo
					Client clienteExistente = null;
					for (Client c : dao.listarTodos()) {
						if (c.getId() == idAtualizar) {
							clienteExistente = c;
							break;
						}
					}

					if (clienteExistente == null) {
						System.out.println("❌ Cliente não encontrado!");
						break;
					}

					System.out.print("Novo Nome (Atual: " + clienteExistente.getNome() + "): ");
					String novoNome = scanner.nextLine();
					System.out.print("Novo Email (Atual: " + clienteExistente.getEmail() + "): ");
					String novoEmail = scanner.nextLine();
					System.out.print("Novo CEP (Atual: " + clienteExistente.getAddress().getCep() + "): ");
					String novoCep = scanner.nextLine();

					Address novoEndereco = ViaCepService.buscarEnderecoPorCEP(novoCep);
					if (novoEndereco != null) {
						novoEndereco.setId(clienteExistente.getAddress().getId()); // Mantém o mesmo ID de endereço do banco

						Client clienteAtualizado = new Client(novoNome, novoEmail, novoEndereco);
						clienteAtualizado.setId(idAtualizar);

						dao.atualizar(clienteAtualizado);
					} else {
						System.out.println("❌ Atualização cancelada por erro no CEP.");
					}
					break;

				case 4:
					System.out.print("\nDigite o ID do cliente que deseja DELETAR: ");
					int idDeletar;
					try {
						idDeletar = Integer.parseInt(scanner.nextLine());
					} catch (NumberFormatException e) {
						System.out.println("❌ ID inválido.");
						break;
					}
					dao.deletar(idDeletar);
					break;

				case 0:
					System.out.println("👋 Saindo do sistema. Até mais!");
					break;

				default:
					System.out.println("❌ Opção inválida! Escolha um número do menu.");
					break;
			}
		}
		scanner.close();
	}
}