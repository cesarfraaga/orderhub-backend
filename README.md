# ERP - Sistema de Gestão de Clientes e Pedidos

API REST desenvolvida com Spring Boot para gerenciamento de clientes, produtos e pedidos.
Projeto criado com foco em aprendizado prático e preparação para atuação como Desenvolvedor Java.

---

## Objetivo do Projeto

Construir um sistema backend seguindo práticas utilizadas no mercado, incluindo:

* Arquitetura REST
* Separação por camadas
* Uso de JPA/Hibernate
* Validações e tratamento de erros
* Testes unitários

---

## Domínio da Aplicação

O sistema simula um cenário de vendas (estilo e-commerce), contendo:

* Usuários (em evolução)
* Clientes
* Produtos
* Pedidos
* Itens de Pedido

---

## Tecnologias Utilizadas

* Java 21
* Spring Boot
* Spring Data JPA
* Hibernate
* PostgreSQL
* Maven
* JUnit

---

## Funcionalidades Implementadas

### Cliente

* Cadastro de clientes
* Listagem
* Consulta por ID
* Atualização de dados
* Exclusão lógica (inativação)
* Busca por nome ou documento

### Produto

* Cadastro de produtos
* Listagem
* Atualização
* Controle de status (ativo/inativo)
* Controle de estoque

### Pedido

* Criação de pedidos
* Associação com cliente
* Adição de itens
* Cálculo automático do total
* Atualização de status

---

## Regras de Negócio

* Um pedido só pode ser criado para clientes ativos
* Produtos inativos não podem ser adicionados ao pedido
* A quantidade de itens não pode exceder o estoque disponível
* O estoque é reduzido ao adicionar itens ao pedido
* Pedidos finalizados não podem ser alterados
* Pedidos cancelados devem devolver o estoque
* O valor total do pedido é calculado automaticamente

---

## Estrutura do Projeto

```text
controller  → Camada de entrada
service     → Regras de negócio
repository  → Acesso a dados (JPA)
entity      → Entidades do sistema
dto         → Objetos de transferência de dados
exception   → Tratamento global de erros
```

---

## Como Executar o Projeto

### Pré-requisitos

* Java 21
* Maven
* PostgreSQL

### Passos

```bash
# Clonar o repositório
git clone https://github.com/cesarfraaga/orderhub-backend.git

# Acessar o diretório
cd orderhub-backend

# Executar a aplicação
./mvnw spring-boot:run
```

A API estará disponível em:

```
http://localhost:8080
```

---

## Endpoints

### client

* POST /client
* GET /client
* GET /client/{id}
* PATCH /client/{id}/status
* PUT /client/{id}

### product

* POST /product
* GET /product
* GET /product/{id}
* PUT /product/{id}
* PATCH /product/{id}/status

### order

* POST /order/client/{clientId}
* POST /order/{id}/items
* PATCH /order/{id}/status
* GET /order/{id}
* GET /order

---

## Testes

```bash
./mvnw test
```

Testes unitários implementados para as camadas de service e controller, garantindo o comportamento das regras de negócio e endpoints da API.
(Evoluindo para maior cobertura)

---

## Status do Projeto

Em desenvolvimento contínuo

Próximos passos:

* Autenticação com JWT
* Dockerização
* Deploy em cloud
* Integração com frontend

---

## Aprendizados

Este projeto está sendo utilizado para consolidar conhecimentos em:

* Desenvolvimento backend com Spring
* Modelagem de domínio
* Boas práticas de código
* Estruturação de APIs REST

---

## Autor
César Fraga

Projeto desenvolvido para fins de estudo com foco em atuação como Desenvolvedor Java.

## Versionamento

Este projeto segue versionamento semântico.

Versão atual: v1.0.0
