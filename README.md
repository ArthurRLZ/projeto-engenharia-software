# Plataforma de Reserva de Laboratórios e Equipamentos

## Sobre o Projeto

A Plataforma de Reserva de Laboratórios e Equipamentos é uma solução desenvolvida para gerenciar o agendamento de espaços acadêmicos e equipamentos institucionais. O sistema permite que alunos, professores e pesquisadores consultem a disponibilidade de recursos, realizem e cancelem reservas, recebam notificações automáticas de reservas próximas, e permite que administradores cadastrem, editem e bloqueiem recursos (para manutenção, por exemplo), além de acompanhar relatórios de utilização — tudo com controle de acesso por autenticação JWT e autorização por perfil (Role).

---

## Deploy

| | URL |
|---|---|
| Frontend | https://frontend-reservas.onrender.com |
| Backend (API) | https://backend-reservas-8gvn.onrender.com |

> O plano free do Render hiberna por inatividade — a primeira requisição após um período ocioso pode demorar de 30 a 50 segundos para responder, enquanto a instância "acorda".

---

## Equipe

| Membro | GitHub |
|---|---|
| Joaci Laurindo | [@joacif](https://github.com/joacif) |
| Euclides Laurindo | [@euclideslaurindo](https://github.com/euclideslaurindo) |
| Luis Arthur | [@lu1s-4rthur](https://github.com/lu1s-4rthur) |
| Heitor Calado | [@heitorcalado](https://github.com/heitorcalado) |
| Arthur Ricardo | [@ArthurRLZ](https://github.com/ArthurRLZ) |

---

## Stack Tecnológica

| Camada | Tecnologia |
|---|---|
| Frontend | Angular 21 (standalone components) |
| Backend | Java 26 + Spring Boot 4.1 (Spring Security, Spring Data JPA, Bean Validation) |
| Autenticação | JWT (JJWT) |
| Banco de dados (dev/testes) | H2 (em memória) |
| Banco de dados (produção) | PostgreSQL (Render) |
| CI/CD | GitHub Actions |
| Cobertura de testes | JaCoCo |
| Análise de qualidade de código | SonarCloud |
| Containerização | Docker (multi-stage build, frontend e backend) |
| Deploy | Render (Web Services + PostgreSQL) |

---

## Estrutura do Repositório

```
projeto-engenharia-software/
├── .github/workflows/   # Pipelines de CI (build, testes, cobertura, análise de qualidade)
├── frontend/            # Aplicação web em Angular
│   ├── Dockerfile
│   └── src/environments/  # environment.ts (dev) e environment.prod.ts (produção)
└── backend/             # API REST em Java com Spring Boot
    ├── Dockerfile
    └── src/main/resources/
        ├── application.properties        # perfil padrão (dev)
        ├── application-test.properties   # perfil usado pelo CI/GitHub Actions
        └── application-prod.properties   # perfil usado em produção (Render)
```

---

## Funcionalidades Implementadas

### Autenticação e Controle de Acesso

- Cadastro de usuário (nome, e-mail, senha) — sempre recebe a role `USER`; não é possível se autopromover a `ADMIN` pelo formulário público.
- Login com autenticação via JWT e logout com limpeza do token.
- Credencial `ADMIN` criada automaticamente na inicialização da aplicação (`CommandLineRunner`).
- Interceptor HTTP no frontend que anexa o token JWT a todas as requisições autenticadas.
- `AuthGuard` (exige login) e `RoleGuard` (exige uma role específica) protegendo rotas do Angular.
- `SecurityFilterChain` no backend com regras explícitas por rota e por verbo HTTP (método `GET`/`POST`/`PUT`/`PATCH`/`DELETE`), incluindo CORS configurado para os domínios de desenvolvimento e produção.

### Gestão de Recursos (Laboratórios/Equipamentos)

- Cadastro de novo recurso (nome, descrição, capacidade, tipo, status de funcionamento) — somente ADMIN.
- Edição de recurso existente — somente ADMIN.
- Listagem de recursos disponíveis, acessível a qualquer usuário autenticado.
- Consulta de disponibilidade de um recurso para uma data/horário específicos, considerando reservas ativas **e** bloqueios administrativos no período.

### Reservas

- Solicitação de reserva de um recurso (data, horário de início e fim), com validação de horário (`@ValidHorario`) e checagem de conflito contra reservas já existentes.
- Isolamento de concorrência (`@Transactional(isolation = SERIALIZABLE)`) para evitar que duas reservas simultâneas sejam aceitas indevidamente para o mesmo horário.
- Listagem paginada de "Minhas Reservas", isolada por usuário autenticado (cada usuário só acessa as próprias).
- Cancelamento de reserva própria, com regras: não é possível cancelar uma reserva de outro usuário (`403`), já cancelada, recusada, ou já iniciada/encerrada (`409`).
- Consulta de reservas por recurso, com visão administrativa (nome/e-mail de quem reservou), paginada.

### Bloqueio Administrativo de Recursos

- Criação de bloqueio de um recurso por um período (ex.: manutenção), com motivo — somente ADMIN.
- Listagem dos bloqueios de um recurso.
- Remoção de bloqueio — somente ADMIN.
- Integração automática: um recurso bloqueado aparece como indisponível na consulta de disponibilidade, e uma tentativa de reserva dentro do período bloqueado é recusada (`409`) com mensagem específica.

### Notificações

- Job agendado (`@Scheduled`, a cada 5 minutos) que gera notificações automaticamente para reservas com início dentro da janela de antecedência configurada, evitando duplicidade.
- Listagem paginada das notificações do usuário autenticado.
- Marcação de notificação como lida (com checagem de que a notificação pertence ao usuário autenticado).
- Contagem de notificações não lidas (badge do sino).

### Relatórios

- Relatório de utilização de recursos por período (data início/fim), acessível somente a ADMIN.

---

## CI/CD e Qualidade de Código

O repositório possui dois workflows de GitHub Actions (`.github/workflows/backend.yml` e `frontend.yml`), disparados a cada `push` ou Pull Request na branch `main`:

- **Frontend:** instala dependências, executa a build Angular e roda a suíte de testes (Jest).
- **Backend:** compila, executa os testes com o perfil `test` (banco H2 isolado via `application-test.properties`), gera o relatório de cobertura com **JaCoCo**, e envia a análise para o **SonarCloud**.

> A análise do SonarCloud é executada apenas em `push` direto na `main` (não em Pull Requests), pois o GitHub Actions não repassa Secrets do repositório para workflows disparados por PRs vindos de forks, por padrão de segurança da plataforma.

**Critérios de qualidade atendidos no SonarCloud:**
- 0 Security Issues
- 0 Maintainability Issues
- Duplicação de código abaixo de 20%
- Cobertura de testes (JaCoCo): mínimo de 70% de instruções e 80% de branches no código novo

Ambos os serviços (frontend e backend) são implantados no **Render** via Docker, a partir dos `Dockerfile` multi-stage presentes em cada pasta.

---

## Como Executar o Projeto Localmente

### Pré-requisitos

- [Node.js](https://nodejs.org/) (versão LTS) e Angular CLI:
  ```bash
  npm install -g @angular/cli
  ```
- [Java JDK 26](https://www.oracle.com/java/technologies/downloads/) (ou compatível — verificar `java.version` em `backend/pom.xml`)
- Maven (ou use o `mvnw`/`mvnw.cmd` incluído no projeto)

### Frontend (Angular)

```bash
cd frontend
npm install
ng serve
```

Acesse em: http://localhost:4200

### Backend (Spring Boot)

```bash
cd backend
.\mvnw spring-boot:run       # Windows (PowerShell)
./mvnw spring-boot:run       # Linux/macOS
```

A API estará disponível em: http://localhost:8080 (banco H2 em memória, recriado a cada execução).

### Rodando os testes do backend

```bash
cd backend
.\mvnw test
```

### Rodando com Docker (localmente)

```bash
docker build -t reservas-backend ./backend
docker build -t reservas-frontend ./frontend
```

---

## Perfis de Configuração (Spring Profiles)

| Perfil | Arquivo | Uso |
|---|---|---|
| Padrão (dev) | `application.properties` | Execução local via `mvnw spring-boot:run` |
| `test` | `application-test.properties` | Usado automaticamente pelo GitHub Actions ao rodar os testes |
| `prod` | `application-prod.properties` | Usado em produção no Render; conecta-se ao PostgreSQL via variáveis de ambiente |

---

## Credencial de Administrador

Na inicialização, o sistema cria automaticamente um usuário administrador padrão:

| Campo | Valor |
|---|---|
| E-mail | `admin@ufape.br` |
| Senha | `admin123` |

---

## Endpoints da API

### Autenticação (`/api/auth`) — público

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/api/auth/register` | Cadastra um novo usuário (role `USER`) |
| `POST` | `/api/auth/login` | Autentica e retorna o token JWT |
| `POST` | `/api/auth/logout` | Encerra a sessão no servidor |

### Usuário e Administração

| Método | Rota | Descrição | Acesso |
|---|---|---|---|
| `GET` | `/api/users/me` | Dados do usuário autenticado | Autenticado |
| `GET` | `/api/admin/ping` | Rota de teste exclusiva para administradores | Role `ADMIN` |

### Recursos (`/api/resources`)

| Método | Rota | Descrição | Acesso |
|---|---|---|---|
| `POST` | `/api/resources` | Cadastra um novo recurso | Role `ADMIN` |
| `PUT` | `/api/resources/{id}` | Edita um recurso existente | Role `ADMIN` |
| `GET` | `/api/resources/{id}` | Busca um recurso pelo id | Autenticado |
| `GET` | `/api/resources` | Lista todos os recursos | Autenticado |
| `GET` | `/api/resources/disponibilidade?data=&horarioInicio=&horarioFim=` | Consulta disponibilidade num período | Autenticado |
| `GET` | `/api/resources/{id}/reservations?page=&size=&sort=` | Lista reservas de um recurso (visão admin) | Role `ADMIN` |

### Reservas (`/api/reservations`)

| Método | Rota | Descrição | Acesso |
|---|---|---|---|
| `POST` | `/api/reservations` | Cria uma nova reserva | Autenticado |
| `GET` | `/api/reservations/me?page=&size=&sort=` | Lista as reservas do usuário autenticado (paginado) | Autenticado |
| `PATCH` | `/api/reservations/{id}/cancelar` | Cancela uma reserva própria | Autenticado |

### Bloqueio de Recursos (`/api/resource-blocks`)

| Método | Rota | Descrição | Acesso |
|---|---|---|---|
| `POST` | `/api/resource-blocks` | Cria um bloqueio administrativo | Role `ADMIN` |
| `GET` | `/api/resource-blocks?resourceId=` | Lista bloqueios de um recurso | Autenticado |
| `DELETE` | `/api/resource-blocks/{id}` | Remove um bloqueio | Role `ADMIN` |

### Notificações (`/api/notifications`)

| Método | Rota | Descrição | Acesso |
|---|---|---|---|
| `GET` | `/api/notifications?page=&size=` | Lista notificações do usuário autenticado (paginado) | Autenticado |
| `PATCH` | `/api/notifications/{id}/ler` | Marca uma notificação como lida | Autenticado |
| `GET` | `/api/notifications/nao-lidas/contagem` | Retorna a contagem de notificações não lidas | Autenticado |

### Relatórios (`/api/reports`)

| Método | Rota | Descrição | Acesso |
|---|---|---|---|
| `GET` | `/api/reports/utilizacao?dataInicio=&dataFim=` | Relatório de utilização de recursos por período | Role `ADMIN` |
