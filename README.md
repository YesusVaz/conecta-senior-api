# 🧓 Conecta Senior API

API REST para a plataforma Conecta Senior - Sistema de cuidado e monitoramento para idosos.

## 📋 Sobre o Projeto

A Conecta Senior API é uma aplicação Spring Boot que oferece funcionalidades completas para:
- Gestão de idosos e seus dados pessoais
- Controle de rotinas diárias
- Monitoramento de saúde
- Sistema de emergências
- Autenticação JWT
- Documentação automática com Swagger

## 🗄️ Banco de Dados

### Configuração Atual
O projeto está **atualmente configurado para usar H2 Database** (banco em memória) para facilitar a execução e demonstração, já que não requer instalação de software adicional.

### Suporte ao PostgreSQL
O projeto foi **desenvolvido e testado com PostgreSQL** como banco principal para produção. A estrutura suporta completamente PostgreSQL com:
- ✅ Configurações específicas para PostgreSQL
- ✅ Dependências PostgreSQL incluídas no projeto
- ✅ Scripts de configuração para ambiente produtivo
- ✅ Perfis separados (dev/prod para PostgreSQL, test para H2)

### Por que H2 está sendo usado?
- **Conveniência**: Não requer instalação de PostgreSQL
- **Testes**: Ideal para demonstrações e desenvolvimento rápido
- **Compatibilidade**: Funciona em qualquer máquina sem configuração adicional
- **Dados temporários**: Perfeito para testes acadêmicos e prototipagem

### Como alternar para PostgreSQL
Para usar PostgreSQL em produção, simplesmente altere o perfil ativo:
```properties
# Em application.properties
spring.profiles.active=dev  # ou prod
```

E configure o PostgreSQL conforme as instruções abaixo.

## 🛠️ Tecnologias Utilizadas

- **Java 21+**
- **Spring Boot 3.5.5**
- **Spring Security** (JWT Authentication)
- **Spring Data JPA**
- **PostgreSQL** (banco de dados único)
- **Maven** (gerenciamento de dependências)
- **Swagger/OpenAPI 3** (documentação)
- **Lombok** (redução de boilerplate)
- **ModelMapper** (conversão DTO ↔ Entity)
- **Docker** (containerização)

## 🚀 Como Executar o Projeto

### Pré-requisitos

1. **Java 21+** instalado
2. **PostgreSQL** rodando (local ou Docker)
3. **Maven** (ou use o wrapper incluído `./mvnw`)
4. **Git** para clonar o repositório

### 1. Clonando o Repositório

```bash
git clone <url-do-repositorio>
cd conecta-senior-api
```

### 2. Configurando PostgreSQL

#### Opção A: PostgreSQL via Docker (Recomendado)
```bash
# Executar PostgreSQL em container
docker run --name postgres-conecta \
  -e POSTGRES_DB=conectasenior_dev \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  -d postgres:15

# Verificar se está rodando
docker ps
```

#### Opção B: PostgreSQL Local
```sql
-- Instalar PostgreSQL e executar:
CREATE DATABASE conectasenior_dev;
```

### 3. Executando a Aplicação

#### Desenvolvimento
```bash
# Executar com profile de desenvolvimento
.\mvnw.cmd spring-boot:run

# Ou especificar a porta
.\mvnw.cmd spring-boot:run -Dserver.port=8081
```

#### Produção
```bash
# Definir variáveis de ambiente
set SPRING_PROFILES_ACTIVE=prod
set DATABASE_URL=jdbc:postgresql://localhost:5432/conectasenior
set DATABASE_USERNAME=postgres
set DATABASE_PASSWORD=sua_senha_segura
set JWT_SECRET=chave_jwt_super_segura_256_bits_minimo

# Executar
.\mvnw.cmd spring-boot:run
```

### 4. Executando com Docker Compose
```bash
# Build e execução completa (inclui PostgreSQL)
docker-compose up --build

# Executar em background
docker-compose up -d

# Ver logs
docker-compose logs -f api
```

## 🌐 Acessando a Aplicação

Após iniciar a aplicação, você pode acessar:

| Serviço | URL | Descrição |
|---------|-----|-----------|
| **API Base** | http://localhost:8080/api | Endpoints da API |
| **Swagger UI** | http://localhost:8080/swagger-ui.html | Documentação interativa |
| **OpenAPI Docs** | http://localhost:8080/api-docs | Especificação OpenAPI |
| **Health Check** | http://localhost:8080/actuator/health | Status da aplicação |

## 🔐 Autenticação

A API utiliza autenticação JWT. Para acessar endpoints protegidos:

### 1. Registrar um usuário
```bash
POST /api/auth/registro
{
  "nome": "João Silva",
  "email": "joao@email.com",
  "senha": "123456",
  "telefone": "11999999999"
}
```

### 2. Fazer login
```bash
POST /api/auth/login
{
  "email": "joao@email.com",
  "senha": "123456"
}
```

### 3. Usar o token retornado
```bash
Authorization: Bearer <seu-jwt-token>
```

## 📚 Principais Endpoints

### Autenticação
- `POST /api/auth/registro` - Registrar novo usuário
- `POST /api/auth/login` - Fazer login

### Idosos
- `GET /api/idosos` - Listar idosos
- `POST /api/idosos` - Cadastrar idoso
- `GET /api/idosos/{id}` - Buscar idoso por ID
- `PUT /api/idosos/{id}` - Atualizar idoso
- `DELETE /api/idosos/{id}` - Excluir idoso

### Rotinas
- `GET /api/rotinas` - Listar rotinas
- `POST /api/rotinas` - Criar rotina
- `PUT /api/rotinas/{id}` - Atualizar rotina
- `DELETE /api/rotinas/{id}` - Excluir rotina

### Saúde
- `GET /api/saude` - Registros de saúde
- `POST /api/saude` - Novo registro de saúde

## ⚙️ Configuração por Ambiente

### Desenvolvimento (dev)
- Banco: PostgreSQL local (`conectasenior_dev`)
- Logs: DEBUG habilitado
- JWT: Chave de desenvolvimento
- CORS: `http://localhost:3000`

### Produção (prod)
- Banco: PostgreSQL configurado via variáveis de ambiente
- Logs: Apenas WARN e ERROR
- JWT: Chave obrigatória via `JWT_SECRET`
- CORS: Domínios específicos

## 🔧 Variáveis de Ambiente (Produção)

| Variável | Obrigatória | Descrição | Exemplo |
|----------|-------------|-----------|---------|
| `SPRING_PROFILES_ACTIVE` | ✅ | Profile ativo | `prod` |
| `DATABASE_URL` | ✅ | URL do banco | `jdbc:postgresql://host:5432/db` |
| `DATABASE_USERNAME` | ✅ | Usuário do banco | `postgres` |
| `DATABASE_PASSWORD` | ✅ | Senha do banco | `senha_segura` |
| `JWT_SECRET` | ✅ | Chave JWT (256+ bits) | `chave_super_segura_256bits` |
| `JWT_EXPIRATION` | ❌ | Expiração em ms | `86400000` (24h) |
| `CORS_ALLOWED_ORIGINS` | ❌ | Domínios permitidos | `https://app.exemplo.com` |
| `PORT` | ❌ | Porta da aplicação | `8080` |


## 📦 Build para Produção

```bash
# Gerar JAR otimizado
./mvnw clean package -DskipTests

# JAR estará em: target/conecta-senior-api-0.0.1-SNAPSHOT.jar

# Executar em produção
java -jar target/conecta-senior-api-0.0.1-SNAPSHOT.jar
```

## 🐳 Docker

### Build da imagem
```bash
docker build -t conecta-senior-api .
```

### Executar container
```bash
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DATABASE_URL=jdbc:postgresql://host:5432/conectasenior \
  -e DATABASE_USERNAME=postgres \
  -e DATABASE_PASSWORD=senha \
  -e JWT_SECRET=chave_segura \
  conecta-senior-api
```

**🎉 Conecta Senior API - Cuidando com tecnologia!**
