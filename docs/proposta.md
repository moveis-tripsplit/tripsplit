# 📱 Documentação do Projeto — TripSplit

> Aplicativo mobile para gerencia mento e divisão de despesas em viagens em grupo.

**Disciplina:** Processos de Software  
**Equipe:** Theresa Angélica · Beatriz Camilo · Clara Macedo · Fernando Santos
**Repositório:** https://github.com/moveis-tripsplit/tripsplit
**Kanban:** https://github.com/orgs/moveis-tripsplit/projects/1/views/1
**Versão:** 1.0 — 27 de agosto de 2026.

---

## Índice

1. [Visão Geral do Projeto](#1-visão-geral-do-projeto)
2. [MPV](#2-mpv)
3. [Requisitos do sistema](#3-requisitos-do-sistema)
4. [Arquitetura do sistema](#4-arquitetura-do-sistema)
5. [Modelagem do Banco de Dados](#5-modelagem-do-banco-de-dados)
6. [Casos de Uso](#6-casos-de-uso)
7. [Plano de Projeto](#7-plano-de-projeto)
8. [Manual do Utilizador](#8-manual-do-usuário)
9. [Documentação da API](#9-documentação-da-api)
10. [Como Rodar o Projeto](#10-como-rodar-o-projeto)

---

# 1. Visão Geral do Projeto

## 1.1 sobre

Para os aventureiros de viagens em grupo 
Que tem dificuldades na divisão de contas das viagens entre os participantes
O TripSplit é um aplicativo mobile 
Que lhe auxiliara na divisão e cobraças de despesas do grupo
Diferente do Split wise o TripSplit pretende gerar avisos de cobranças aos membros displicentes com depesas a serem pagas
O nosso produto tem a divisão voltada para cada despesa feita na viagem.

---

## 1.2 Problema e Solução

| Item | Descrição |
|---|---|
| **Problema** | Dificuldade para controlar e dividir despesas durante viagens em grupo |
| **Impacto** | Confusão sobre quem deve pagar, quem tem valores a receber e quais despesas já foram quitadas |
| **Solução** | Aplicativo que registra despesas e calcula automaticamente os valores devidos por cada participante |

---

## 1.3 Objetivo

O objetivo do TripSplit é simplificar o controle financeiro de viagens em grupo, permitindo que os participantes saibam de forma clara:

- Quanto cada pessoa gastou;
- Quanto cada pessoa deve;
- Quanto cada pessoa tem a receber;
- Quais dívidas estão pendentes;
- Quais dívidas já foram pagas.

---

## 1.4 Funcionalidades Principais

- Cadastro e login de utilizadores;
- Cadastro de viagens;
- Cadastro de participantes;
- Cadastro de despesas;
- Definição de quem pagou cada despesa;
- Seleção dos participantes de cada despesa;
- Divisão automática das despesas;
- Visualização do saldo de cada participante;
- Controle de dívidas;
- Registro de pagamentos;
- Resumo financeiro da viagem;
- Cadastro de chave Pix;
- Cobrança de participantes via Pix.

---

## 1.5 Partes Interessadas

| Ator | Descrição |
|---|---|
| **Usuário** | Pessoa que utiliza o aplicativo para gerenciar suas viagens |
| **Participante** | Pessoa que participa de uma viagem e pode possuir despesas ou dívidas |
| **Equipe de Desenvolvimento** | Responsável pelo desenvolvimento e manutenção do sistema |

---

## 1.6 Tecnologias

| Camada | Tecnologia |
|---|---|
| **Aplicativo** | Kotlin Multiplatform |
| **Interface** | Compose Multiplatform |
| **Linguagem** | Kotlin |
| **Backend** | Kotlin + Ktor |
| **Banco de Dados** | MySQL |
| **Comunicação** | API REST |
| **Autenticação** | JWT |
| **Build** | Gradle |
| **Controle de Versão** | Git + GitHub |

---
# 2. MPV

| Funcionalidades do MVP                         |
|------------------------------------------------|
| **Criar uma viagem**                           |
| **Adicionar participantes**                    |
| **Cadastrar despesas**                         |
| **selecionar participantes de cada despesa**   |
| **Calcular automaticamente a divisão**         |
| **Visualizar valores a pagar**                 |
| **Visualizar valores a receber**               |               
| **Marcar dívidas como pagas**                  |                  
| **Visualizar o resumo financeiro da viagem.**  |   

## 2.1 Resultado esperado

Ao final do MVP, o utilizador deverá conseguir criar uma viagem, adicionar os seus participantes, registrar as despesas realizadas, definir quem participou de cada gasto e visualizar automaticamente quem deve pagar, quanto deve pagar e quem deve receber.

# 3. Requisitos do sistema

## 3.1 Convenção de Prioridades

| Código | Prioridade |
|---|---|
| **P1** | Alta — essencial para o MVP |
| **P2** | Média — importante, mas não bloqueante |
| **P3** | Baixa — desejável para versões futuras |

---

## 3.2 Requisitos Funcionais

### Módulo de Autenticação

| ID | Requisito | Prioridade |
|---|---|---|
| RF01 | O sistema deve permitir cadastro de usuários | P1 |
| RF02 | O sistema deve permitir login com e-mail e senha | P1 |
| RF03 | O sistema deve permitir logout | P1 |
| RF04 | O sistema deve permitir recuperação de senha | P2 |

---

### Módulo de Viagens

| ID | Requisito | Prioridade |
|---|---|---|
| RF05 | O sistema deve permitir criar uma viagem | P1 |
| RF06 | O sistema deve permitir informar nome da viagem | P1 |
| RF07 | O sistema deve permitir informar destino da viagem | P1 |
| RF08 | O sistema deve permitir informar período da viagem | P1 |
| RF09 | O sistema deve listar as viagens do usuário | P1 |
| RF10 | O sistema deve permitir editar uma viagem | P2 |
| RF11 | O sistema deve permitir excluir uma viagem | P2 |

---

### Módulo de Participantes

| ID | Requisito | Prioridade |
|---|---|---|
| RF12 | O sistema deve permitir adicionar participantes à viagem | P1 |
| RF13 | O sistema deve permitir cadastrar um participante que não possui conta | P1 |
| RF14 | O sistema deve permitir remover participantes | P2 |
| RF15 | O sistema deve permitir associar um usuário cadastrado a uma viagem | P2 |

---

### Módulo de Despesas

| ID | Requisito | Prioridade |
|---|---|---|
| RF16 | O sistema deve permitir cadastrar uma despesa | P1 |
| RF17 | O sistema deve permitir informar a descrição da despesa | P1 |
| RF18 | O sistema deve permitir informar o valor da despesa | P1 |
| RF19 | O sistema deve permitir informar quem realizou o pagamento | P1 |
| RF20 | O sistema deve permitir selecionar os participantes da despesa | P1 |
| RF21 | O sistema deve calcular automaticamente a divisão da despesa | P1 |
| RF22 | O sistema deve listar as despesas da viagem | P1 |
| RF23 | O sistema deve permitir editar uma despesa | P2 |
| RF24 | O sistema deve permitir excluir uma despesa | P2 |

---

### Módulo Financeiro

| ID | Requisito | Prioridade |
|---|---|---|
| RF25 | O sistema deve calcular o saldo de cada participante | P1 |
| RF26 | O sistema deve identificar quem deve dinheiro | P1 |
| RF27 | O sistema deve identificar quem tem dinheiro a receber | P1 |
| RF28 | O sistema deve exibir as dívidas pendentes | P1 |
| RF29 | O sistema deve permitir marcar uma dívida como paga | P1 |
| RF30 | O sistema deve registrar a data do pagamento | P2 |
| RF31 | O sistema deve apresentar o resumo financeiro da viagem | P2 |

---

### Módulo de Pix

| ID | Requisito | Prioridade |
|---|---|---|
| RF32 | O sistema deve permitir cadastrar uma chave Pix | P2 |
| RF33 | O sistema deve permitir visualizar a chave Pix de um participante | P2 |
| RF34 | O sistema deve facilitar a cobrança de uma dívida via Pix | P3 |

---

### Módulo Social

| ID | Requisito | Prioridade |
|---|---|---|
| RF35 | O sistema deve permitir adicionar outro usuário como amigo | P3 |
| RF36 | O sistema deve listar os amigos do usuário | P3 |
| RF37 | O sistema deve permitir adicionar um amigo a uma viagem | P3 |

---

## 3.3 Requisitos Não Funcionais

| ID | Requisito |
|---|---|
| RNF01 | O aplicativo deve possuir interface responsiva para diferentes tamanhos de tela |
| RNF02 | O aplicativo deve funcionar em dispositivos Android |
| RNF03 | O sistema deve utilizar autenticação segura |
| RNF04 | As senhas devem ser armazenadas utilizando hash seguro |
| RNF05 | A comunicação entre aplicativo e servidor deve utilizar HTTPS |
| RNF06 | A API deve utilizar autenticação baseada em JWT |
| RNF07 | O backend deve seguir arquitetura em camadas |
| RNF08 | O código compartilhado deve utilizar Kotlin Multiplatform |
| RNF09 | As regras de negócio devem ser separadas da interface |
| RNF10 | O banco de dados deve possuir scripts de migração |
| RNF11 | O sistema deve apresentar mensagens de erro compreensíveis ao usuário |
| RNF12 | O aplicativo deve manter os dados da viagem sincronizados com o servidor |

---

## 3.4 Regras de Negócio

| ID | Regra |
|---|---|
| RN01 | Uma viagem deve possuir pelo menos um participante |
| RN02 | Uma despesa deve pertencer a uma viagem |
| RN03 | Uma despesa deve possuir um único pagador |
| RN04 | Uma despesa deve possuir pelo menos um participante na divisão |
| RN05 | O valor total dividido deve corresponder ao valor da despesa |
| RN06 | O saldo de um participante deve considerar os valores pagos e os valores devidos |
| RN07 | Uma dívida só pode ser marcada como paga quando existir uma dívida registrada |
| RN08 | O participante que pagou uma despesa pode possuir saldo a receber |
| RN09 | Uma pessoa sem cadastro no aplicativo pode participar de uma viagem |
| RN10 | Apenas usuários autorizados podem alterar informações de uma viagem |

---

# 4. Arquitetura do sistema

## 4.1 Visão Geral

O TripSplit utiliza uma arquitetura baseada em Kotlin Multiplatform, permitindo compartilhar código entre diferentes plataformas.

A interface é desenvolvida utilizando Compose Multiplatform, enquanto as regras de negócio e parte da camada de dados podem ser compartilhadas.

```text
┌─────────────────────────────────────┐
│             USUÁRIO                 │
│          Android / iOS              │
└──────────────────┬──────────────────┘
                   │
┌──────────────────▼──────────────────┐
│          COMPOSE MULTIPLATFORM      │
│              UI / UX                │
└──────────────────┬──────────────────┘
                   │
┌──────────────────▼──────────────────┐
│            PRESENTATION             │
│         ViewModel / State           │
└──────────────────┬──────────────────┘
                   │
┌──────────────────▼──────────────────┐
│              DOMAIN                 │
│       Regras de Negócio             │
│          Use Cases                  │
└──────────────────┬──────────────────┘
                   │
┌──────────────────▼──────────────────┐
│               DATA                  │
│ Repository / API / Local            │
└──────────────────┬──────────────────┘
                   │
                   │ HTTP / REST
                   ▼
┌─────────────────────────────────────┐
│              BACKEND                │
│           Kotlin + Ktor             │
└──────────────────┬──────────────────┘
                   │
                   ▼
┌─────────────────────────────────────┐
│           BANCO DE DADOS            │
│               MySQL                 │
└─────────────────────────────────────┘
```

## 4.2 Estrutura do Projeto

```text
TripSplit/
│
├── composeApp/
│   └── src/
│       ├── commonMain/
│       │   ├── kotlin/
│       │   │   ├── ui/
│       │   │   ├── navigation/
│       │   │   └── App.kt
│       │
│       ├── androidMain/
│       └── iosMain/
│
├── shared/
│   └── src/
│       ├── commonMain/
│       │   ├── domain/
│       │   │   ├── model/
│       │   │   └── usecase/
│       │   │
│       │   ├── data/
│       │   │   ├── repository/
│       │   │   ├── remote/
│       │   │   └── local/
│       │   │
│       │   └── presentation/
│       │
│       ├── androidMain/
│       └── iosMain/
│
├── server/
│   └── src/
│       └── main/
│           └── kotlin/
│               └── com/tripsplit/
│                   ├── controller/
│                   ├── service/
│                   ├── repository/
│                   ├── model/
│                   ├── dto/
│                   ├── security/
│                   └── config/
│
├── docs/
│   └── proposta.md
│
└── README.md
```