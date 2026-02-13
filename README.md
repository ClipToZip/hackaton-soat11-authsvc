![Static Badge](https://img.shields.io/badge/Java-21-blue)
![Static Badge](https://img.shields.io/badge/Spring_Boot-3.5.7-green)
[![Apache 2.0 License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)

# 📱 Hackaton FIAP - ClipToZip - Microsserviço de Autenticação - Grupo 13

![Logo ClipToZip](/docs/cliptozip.png)

## 📝 Sobre o Projeto

Este repositório contém o código-fonte do microsserviço de **Autenticação** do ecossistema **ClipToZip**, desenvolvido pelo Grupo 13 como parte do projeto Hackaton da FIAP.

O objetivo principal deste serviço é cadastrar usuários e gerenciar autenticação por meio de token.

### Funcionalidades Principais

*   **Cadastro de usuários**: Endpoint de cadastro de usuário com e-mail e senha.
*   **Gestão de autenticação**: Endpoint de autenticação por meio do e-mail e senha.
*   **Validação de token**: Endpoint para validar se o token informado é válido.
*   **Performance**: Implementação de cache para tokens ativos.

---

## 🛠️ Tecnologias Utilizadas

O projeto foi construído utilizando as seguintes tecnologias e bibliotecas:

*   **Linguagem**: [Java 21](https://openjdk.org/projects/jdk/21/)
*   **Framework**: [Spring Boot 3.5.7](https://spring.io/projects/spring-boot)
*   **Banco de Dados**: [Redis](https://redis.io/) (Cache), [PostgreSQL](https://www.postgresql.org/)
*   **Testes**: [JUnit 5](https://junit.org/junit5/), [Mockito](https://site.mockito.org/)
*   **Cobertura de Código**: [JaCoCo](https://www.eclemma.org/jacoco/)
*   **Utilitários**: [Lombok](https://projectlombok.org/)
*   **Containerização**: [Docker](https://www.docker.com/) & Docker Compose

---

## 🧩 Arquitetura da Solução

A aplicação segue os princípios da **Arquitetura Hexagonal (Ports and Adapters)**, promovendo o desacoplamento entre a lógica de negócio e os detalhes de infraestrutura.

### Camadas da Aplicação

1.  **Domain (Núcleo)**: Contém as entidades (`User`, `Token`) e regras de negócio fundamentais. É isolado de frameworks externos.
2.  **Application (Casos de Uso)**: Orquestra o fluxo de dados.
    *   `UserRegistrationUseCase`: Responsável por cadastrar novos usuários.
    *   `AuthenticationUseCase`: Responsável por autenticar usuários e gerar tokens.
    *   `TokenValidationUseCase`: Responsável por validar tokens.
    *   **Ports**: Interfaces que definem os contratos de entrada (In) e saída (Out).
3.  **Adapters (Infraestrutura)**: Implementações concretas das portas.
    *   **In (Entrada)**:
        *   `AuthController`: Exposição de API REST.
    *   **Out (Saída)**:
        *   `UserRepositoryImpl`: Persistência no PostgreSQL.
        *   `TokenCacheRepositoryImpl`: Gerenciamento de cache de tokens no Redis.

---

## 🚀 Como Executar

### Pré-requisitos
*   Java 21 instalado
*   Docker e Docker Compose instalados
*   Maven (wrapper incluído no projeto)

### Passo a Passo

1.  **Subir a Infraestrutura Local**:
    Utilize o Docker Compose para iniciar o PostgreSQL e o Redis.
    ```bash
    docker-compose up -d
    ```

2.  **Executar a Aplicação**:
    Inicie a aplicação.
    ```bash
    ./mvnw spring-boot:run
    ```

3.  **Acessar a API**:
    *   URL Base: `http://localhost:8080`
    *   Swagger UI: `http://localhost:8080/swagger-ui/index.html`

---

## 🧪 Testes e Qualidade

O projeto possui uma forte cultura de testes automatizados.

*   Utilizamos JUnit 5 e Mockito para validar a lógica de negócio isolada, garantindo que cada componente (especialmente nas camadas de domain e application) funcione conforme o esperado.
    Cobertura: O projeto mantém uma cobertura de código superior a 80%, verificada automaticamente no pipeline de CI/CD.
    Foco: Validação de regras de negócio, segurança e mapeamento de dados.

### ⚙️ Como executar os testes
Para rodar a suíte completa de testes unitários e gerar o relatório de cobertura, execute o comando Maven:

```Bash
mvn clean verify
```

Após a execução, o relatório estará disponível em:
- Relatório de Cobertura (JaCoCo): `target/site/jacoco/index.html`

---

## 📡 Endpoints Principais

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/auth/register` | Cadastra um novo usuário. |
| `POST` | `/auth/login` | Autentica um usuário e retorna um token. |
| `POST` | `/auth/validate` | Valida um token. |
| `GET` | `/swagger-ui/index.html` | Documentação interativa da API (Swagger/OpenAPI). |

---

## 📂 Recursos Adicionais

*   **Postman Collection**: Para facilitar os testes e a integração, disponibilizamos uma collection do Postman com as requisições configuradas.
    *   [Baixar Collection Postman](docs/ClickToZip-Auth.postman_collection.json)
---

## 👥 Autores - Grupo 13

| Nome | RM |
|---|---|
| **Fabiana Casagrande Costa** | RM362339 |
| **Felipe Costacurta Paruce** | RM364868 |
| **Rafael Fonseca Hermes Azevedo** | RM361445 |
| **Samuel Videira** | RM363405 |
