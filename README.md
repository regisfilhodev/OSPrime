# Sistema de Ordem de Serviço (SistemaOS)

Sistema desktop para gerenciamento de Ordens de Serviço (OS), desenvolvido em **Java Swing** com persistência em **MySQL**, utilizando **Maven** como gerenciador de dependências e **NetBeans** como IDE.

## ✨ Funcionalidades

- **Autenticação** — Tela de login com controle de usuários
- **Ordens de Serviço** — Cadastro, listagem, edição e acompanhamento de OS
- **Clientes** — CRUD completo e independente de clientes
- **Produtos e Estoque** — Cadastro de produtos e controle de estoque
- **Usuários** — Gerenciamento de usuários do sistema
- **Relatórios** — Geração de relatórios do sistema
- **Configurações** — Tela de opções gerais e configurações do sistema

## 🛠️ Tecnologias

- Java (Swing) para a interface gráfica
- MySQL como banco de dados (via `mysql-connector-j`)
- Maven para build e gerenciamento de dependências
- NetBeans como ambiente de desenvolvimento

## 📁 Estrutura do Projeto

```
src/main/java/br/com/os/
├── dao/            # Camada de acesso a dados (OsDAO, UsuarioDAO, ProdutoDAO, ConfiguracaoDAO)
├── model/           # Entidades (OrdemServico, Usuario, Produto, Configuracao)
├── util/            # Utilitários (Conexao, Cores)
└── view/             # Telas do sistema (TelaLogin, TelaMenu, TelaOS, TelaFormOS,
                       # TelaEstoque, TelaUsuarios, TelaFormUsuario, TelaFormProduto,
                       # TelaRelatorios, TelaConfiguracoes, TelaOpcoesGerais)
```

## 🎨 Paleta de Cores

| Cor | Hex |
|---|---|
| ![#98B9E7](https://placehold.co/15x15/98B9E7/98B9E7.png) | `#98B9E7` |
| ![#9CA6B5](https://placehold.co/15x15/9CA6B5/9CA6B5.png) | `#9CA6B5` |
| ![#606E82](https://placehold.co/15x15/606E82/606E82.png) | `#606E82` |
| ![#2B3A4F](https://placehold.co/15x15/2B3A4F/2B3A4F.png) | `#2B3A4F` |
| ![#111F33](https://placehold.co/15x15/111F33/111F33.png) | `#111F33` |

## ⚙️ Configuração e Instalação

### Pré-requisitos

- JDK 17+ (ou versão configurada no `pom.xml`)
- MySQL Server
- Maven
- NetBeans (recomendado, mas não obrigatório)

### Passos

1. Clone o repositório:
   ```bash
   git clone <url-do-repositorio>
   ```

2. Crie o banco de dados no MySQL:
   ```sql
   CREATE DATABASE db_ordem_servico;
   ```

3. Configure as credenciais de conexão em `br.com.os.util.Conexao`.

4. Compile e execute o projeto via Maven ou diretamente pelo NetBeans:
   ```bash
   mvn clean install
   mvn exec:java
   ```

## 📌 Status

Projeto em desenvolvimento ativo.

## 📄 Licença

Defina a licença do projeto aqui (ex: MIT).
