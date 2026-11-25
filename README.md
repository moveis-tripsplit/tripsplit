# TripSplit --- Documentação do Projeto

> Aplicativo multiplataforma para organizar viagens, registrar despesas
> e dividir os custos entre os participantes.

**Disciplina:** Desenvolvimento para Dispositivos Móveis\
**Equipe:** Thereza Angélica · Beatriz Camilo · Clara Macedo · Vladimir
Vieira\
**Repositório:**
[moveis-tripsplit/tripsplit](https://github.com/moveis-tripsplit/tripsplit)\
**Quadro de tarefas:** [GitHub
Projects](https://github.com/users/moveis-tripsplit/projects)\
**Versão deste documento:** 1.0\
**Status:** Em desenvolvimento\
**Última revisão:** Novembro de 2026

> \[!NOTE\] Este documento reúne as informações conhecidas sobre o
> TripSplit e descreve a organização planejada do projeto.
> A documentação não considera uma funcionalidade
> concluída apenas por ela estar especificada aqui.

------------------------------------------------------------------------

## Sumário

1.  [Apresentação](#1-apresentação)
2.  [Objetivos e escopo](#2-objetivos-e-escopo)
3.  [Público-alvo e atores](#3-público-alvo-e-atores)
4.  [Funcionalidades](#4-funcionalidades)
5.  [Requisitos do sistema](#5-requisitos-do-sistema)
6.  [Regras de negócio](#6-regras-de-negócio)
7.  [Arquitetura e tecnologias](#7-arquitetura-e-tecnologias)
8.  [Estrutura do projeto](#8-estrutura-do-projeto)
9.  [Modelo conceitual de dados](#9-modelo-conceitual-de-dados)
10. [Navegação e telas](#10-navegação-e-telas)
11. [Design System](#11-design-system)
12. [Casos de uso](#12-casos-de-uso)
13. [Testes e qualidade](#13-testes-e-qualidade)
14. [Configuração e execução](#14-configuração-e-execução)
15. [Organização da equipe e
    desenvolvimento](#15-organização-da-equipe-e-desenvolvimento)
16. [Estado atual e pendências](#16-estado-atual-e-pendências)
17. [Histórico de alterações](#17-histórico-de-alterações)

------------------------------------------------------------------------

## 1. Apresentação

### 1.1 Sobre o TripSplit

O TripSplit é um aplicativo voltado à organização financeira de viagens
em grupo. Seu propósito é facilitar o registro das despesas realizadas
durante uma viagem e tornar mais simples a divisão dos valores entre os
participantes.

Em viagens com várias pessoas, é comum que diferentes integrantes paguem
por alimentação, transporte, hospedagem, passeios e outras despesas.
Quando esses pagamentos são anotados separadamente, pode ser difícil
identificar quanto cada pessoa gastou, quanto deveria contribuir e quem
ainda precisa acertar sua parte.

O TripSplit propõe centralizar essas informações em uma única viagem,
permitindo registrar participantes e despesas, calcular a divisão dos
custos e acompanhar valores pagos e pendentes.

Um ponto importante do projeto é contemplar participantes que utilizam o
aplicativo e pessoas que participam da viagem sem possuir uma conta.
Dessa forma, o organizador pode manter todos os integrantes no controle
financeiro da viagem, mesmo que nem todos utilizem o aplicativo
diretamente.

### 1.2 Problema

A divisão de despesas em viagens pode gerar:

-   Dificuldade para acompanhar quem pagou cada despesa.
-   Cálculos manuais e possibilidade de erros.
-   Falta de transparência sobre quanto cada participante deve.
-   Confusão entre valores já pagos e valores ainda pendentes.
-   Dificuldade para organizar os acertos financeiros ao final da
    viagem.

### 1.3 Solução proposta

Desenvolver um aplicativo que permita criar viagens, adicionar
participantes, registrar despesas e distribuir os valores entre as
pessoas envolvidas.

O sistema deverá apresentar uma visão organizada dos gastos e dos
acertos financeiros, ajudando o grupo a entender quanto foi gasto,
quanto cabe a cada participante e quais valores ainda estão pendentes.

### 1.4 Objetivo geral

Desenvolver um aplicativo multiplataforma para gerenciamento e divisão
de despesas de viagens em grupo, oferecendo uma forma simples e
transparente de registrar gastos e acompanhar os pagamentos dos
participantes.

### 1.5 Objetivos específicos

-   Permitir organizar despesas por viagem.
-   Cadastrar e gerenciar participantes.
-   Permitir incluir participantes que não possuem conta no aplicativo.
-   Registrar despesas e identificar quem realizou cada pagamento.
-   Dividir automaticamente os valores entre os participantes definidos
    para cada despesa.
-   Exibir valores individuais e totais da viagem.
-   Diferenciar valores pagos e pendentes.
-   Facilitar a conferência dos acertos entre os participantes.
-   Disponibilizar informações para apoiar cobranças e pagamentos via
    Pix, conforme o escopo implementado.
-   Manter uma interface consistente nas plataformas contempladas pelo
    projeto.

------------------------------------------------------------------------

## 2. Objetivos e escopo

### 2.1 Escopo funcional

O núcleo do TripSplit é a organização de viagens e suas despesas. A
aplicação deve permitir que o usuário trabalhe com:

-   Viagens.
-   Participantes.
-   Despesas.
-   Divisão dos valores.
-   Situação dos pagamentos.
-   Resumo financeiro da viagem.
-   Cobranças e informações para pagamento via Pix, conforme
    implementação.

### 2.2 Plataformas

O projeto utiliza Kotlin Multiplatform e Compose Multiplatform, com
estrutura prevista para compartilhar código entre plataformas.

  Plataforma             Módulo/ambiente   Situação
  ---------------------- ----------------- ------------------------------------
  Android                `androidApp`      Incluído no projeto
  iOS                    `iosApp`          Incluído no projeto
  Desktop JVM            `desktopApp`      Incluído na configuração conhecida
  Código compartilhado   `shared`          Base comum da aplicação

**TO DO ---** Confirmar quais plataformas fazem parte da entrega
acadêmica obrigatória e quais são mantidas como plataformas adicionais
de desenvolvimento.

### 2.3 Limites do escopo

A documentação descreve o aplicativo TripSplit. Serviços externos,
sincronização remota, autenticação em servidor e processamento
financeiro real só devem ser considerados disponíveis quando houver
implementação e configuração correspondentes.

**TO DO ---** Documentar a estratégia definitiva de persistência dos
dados, sincronização entre dispositivos e serviços externos utilizados
pelo projeto.

------------------------------------------------------------------------

## 3. Público-alvo e atores

### 3.1 Público-alvo

-   Pessoas que organizam viagens com amigos ou familiares.
-   Grupos que compartilham despesas durante viagens.
-   Participantes que precisam acompanhar sua parte nos gastos.
-   Organizadores que registram despesas pagas por diferentes
    integrantes.

### 3.2 Atores

  -----------------------------------------------------------------------
  Ator                                Descrição
  ----------------------------------- -----------------------------------
  Usuário                             Pessoa que utiliza o aplicativo
                                      para criar ou participar de
                                      viagens.

  Organizador                         Usuário responsável por criar ou
                                      administrar uma viagem.

  Participante com conta              Integrante da viagem que possui
                                      acesso ao aplicativo.

  Participante sem conta              Integrante registrado na viagem,
                                      mas que não utiliza uma conta no
                                      aplicativo.
  -----------------------------------------------------------------------

**Observação:** um participante sem conta deve poder fazer parte dos
cálculos e dos registros da viagem. Isso não significa que ele tenha
acesso próprio ao aplicativo.

**TO DO ---** Formalizar as permissões de cada papel, especialmente
quais ações podem ser realizadas apenas pelo organizador e quais ficam
disponíveis a todos os participantes com conta.

------------------------------------------------------------------------

## 4. Funcionalidades

### 4.1 Gerenciamento de viagens

O aplicativo deve organizar as informações financeiras em viagens
independentes. Cada viagem reúne seus participantes e despesas.

Funcionalidades previstas:

-   Criar uma viagem.
-   Informar os dados básicos da viagem.
-   Consultar viagens cadastradas.
-   Abrir os detalhes de uma viagem.
-   Gerenciar os participantes associados.
-   Consultar o resumo financeiro da viagem.

**TO DO ---** Confirmar os campos definitivos de uma viagem, como nome,
destino, data de início, data de término e descrição.

### 4.2 Gerenciamento de participantes

Uma viagem pode possuir diversos participantes. O aplicativo deve
permitir registrar pessoas que utilizam o TripSplit e pessoas que não
possuem conta.

Funcionalidades previstas:

-   Adicionar participantes à viagem.
-   Identificar participantes com conta e participantes sem conta.
-   Associar despesas a quem realizou o pagamento.
-   Incluir participantes na divisão de uma despesa.
-   Consultar os valores atribuídos a cada pessoa.

**TO DO ---** Definir regras para edição, remoção e transferência de
responsabilidade de participantes após o registro de despesas.

### 4.3 Gerenciamento de despesas

As despesas representam os gastos realizados durante a viagem.

Funcionalidades previstas:

-   Registrar uma despesa.
-   Informar sua descrição e valor.
-   Identificar quem realizou o pagamento.
-   Selecionar os participantes que dividirão a despesa.
-   Consultar as despesas registradas.
-   Atualizar ou excluir uma despesa, respeitando as regras que forem
    definidas.

**TO DO ---** Confirmar os campos obrigatórios e opcionais de uma
despesa, incluindo categoria, data, observação e comprovante.

### 4.4 Divisão automática

O TripSplit deve calcular a participação financeira de cada integrante
de acordo com as pessoas selecionadas para dividir determinada despesa.

No caso de uma divisão igualitária, o valor é distribuído entre os
participantes selecionados. O sistema deve considerar valores monetários
e arredondamento para centavos.

**TO DO ---** O sistema deve em tese permitir divisão por valores personalizados,
pesos ou percentuais.

### 4.5 Acompanhamento de pagamentos

O sistema deve permitir visualizar a situação dos valores associados aos
participantes.

Informações previstas:

-   Total de despesas da viagem.
-   Total pago por cada participante.
-   Valor que cabe a cada participante.
-   Valores já acertados.
-   Valores pendentes.
-   Resumo de quem deve receber e quem deve pagar, conforme a regra de
    acerto definida.

**TO DO ---** Definir como será registrada a confirmação de um pagamento
entre participantes e quem terá permissão para alterar seu status.


### 4.6 Pix e cobranças

O TripSplit permite que os participantes realizem o acerto dos valores devidos por meio do Pix, facilitando a divisão e o pagamento das despesas da viagem.

A funcionalidade prevê a disponibilização dos dados necessários para que um participante possa efetuar o pagamento ao responsável pela cobrança.

**TO DO ---** Detalhar como os dados Pix serão cadastrados, exibidos e compartilhados entre os participantes, bem como a forma de registrar no sistema que um pagamento foi realizado.

------------------------------------------------------------------------

## 5. Requisitos do sistema

### 5.1 Prioridades

| **Prioridade** | **Significado** |
|----------------|-----------------|
| P1 | Essencial para o fluxo principal do aplicativo. |
| P2 | Importante, mas pode ser desenvolvido após o núcleo funcional. |
| P3 | Evolução ou melhoria futura. |

As prioridades são uma proposta de organização. Devem ser comparadas
com o backlog e com os critérios oficiais da disciplina.

### 5.2 Requisitos funcionais

#### Módulo de viagens

| **ID** | **Requisito** | **Prioridade** |
|--------|---------------|----------------|
| RF01 | O sistema deve permitir criar uma viagem. | P1 |
| RF02 | O sistema deve apresentar as viagens disponíveis ao usuário. | P1 |
| RF03 | O sistema deve permitir visualizar os detalhes de uma viagem. | P1 |
| RF04 | O sistema deve permitir editar os dados de uma viagem, conforme permissões definidas. | P2 |
| RF05 | O sistema deve permitir encerrar ou arquivar uma viagem, conforme regra definida. | P2 |

---

#### Módulo de participantes

| **ID** | **Requisito** | **Prioridade** |
|--------|---------------|----------------|
| RF06 | O sistema deve permitir adicionar participantes a uma viagem. | P1 |
| RF07 | O sistema deve permitir registrar participantes sem conta. | P1 |
| RF08 | O sistema deve distinguir participantes com conta e sem conta. | P1 |
| RF09 | O sistema deve permitir consultar os participantes de uma viagem. | P1 |
| RF10 | O sistema deve permitir editar ou remover participantes, respeitando as regras de negócio. | P2 |

---

#### Módulo de despesas

| **ID** | **Requisito** | **Prioridade** |
|--------|---------------|----------------|
| RF11 | O sistema deve permitir registrar despesas em uma viagem. | P1 |
| RF12 | O sistema deve permitir identificar quem pagou uma despesa. | P1 |
| RF13 | O sistema deve permitir selecionar os participantes que dividirão uma despesa. | P1 |
| RF14 | O sistema deve calcular a divisão do valor entre os participantes selecionados. | P1 |
| RF15 | O sistema deve apresentar a lista de despesas da viagem. | P1 |
| RF16 | O sistema deve permitir consultar os detalhes de uma despesa. | P1 |
| RF17 | O sistema deve permitir editar ou excluir despesas conforme regras definidas. | P2 |

---

#### Módulo financeiro

| **ID** | **Requisito** | **Prioridade** |
|--------|---------------|----------------|
| RF18 | O sistema deve apresentar o total de despesas da viagem. | P1 |
| RF19 | O sistema deve apresentar o valor atribuído a cada participante. | P1 |
| RF20 | O sistema deve distinguir valores pagos e pendentes. | P1 |
| RF21 | O sistema deve apresentar um resumo financeiro por participante. | P1 |
| RF22 | O sistema deve auxiliar na identificação de quem deve pagar ou receber. | P2 |
| RF23 | O sistema deve permitir consultar informações de cobrança via Pix, conforme escopo aprovado. | P2 |

---

#### Módulos complementares

| **ID** | **Requisito** | **Prioridade** |
|--------|---------------|----------------|
| RF24 | O sistema poderá permitir o compartilhamento de informações da viagem. | P2 |
| RF25 | O sistema poderá apresentar histórico de alterações financeiras. | P3 |
| RF26 | O sistema poderá oferecer notificações de despesas e cobranças. | P3 |

> **TO DO ---** Conferir a numeração e a descrição dos requisitos com a especificação oficial da equipe. Ajustar os IDs caso já exista uma matriz de requisitos aprovada.

### 5.3 Requisitos não funcionais

| **ID** | **Requisito não funcional** |
|--------|-----------------------------|
| RNF01 | A aplicação deve manter uma experiência consistente nas plataformas suportadas. |
| RNF02 | A interface deve se adaptar a diferentes dimensões de tela. |
| RNF03 | A lógica de divisão de despesas deve ser separada dos componentes visuais. |
| RNF04 | O código compartilhado deve ser organizado de forma que possa ser reutilizado entre plataformas. |
| RNF05 | Os valores monetários devem ser tratados com precisão de centavos. |
| RNF06 | A aplicação deve apresentar feedback para operações realizadas pelo usuário. |
| RNF07 | A interface deve tratar estados de carregamento, erro e ausência de dados quando aplicável. |
| RNF08 | O projeto deve utilizar controle de versão. |
| RNF09 | As regras centrais de cálculo devem possuir testes automatizados. |
| RNF10 | Dados pessoais e financeiros devem ser tratados com cuidado e não devem ser expostos desnecessariamente. |

> **TO DO ---** Definir requisitos mensuráveis de desempenho, acessibilidade, compatibilidade mínima de sistema operacional e política de armazenamento de dados.

---

## 6. Regras de negócio

As regras abaixo descrevem o comportamento esperado do domínio. Devem ser confirmadas pela equipe antes de serem tratadas como regras definitivas.

| **ID** | **Regra de negócio** |
|--------|----------------------|
| RN01 | Uma viagem pode possuir vários participantes. |
| RN02 | Uma viagem pode possuir várias despesas. |
| RN03 | Cada despesa deve estar associada a uma viagem. |
| RN04 | Uma despesa deve possuir um valor monetário válido e positivo. |
| RN05 | Uma despesa deve identificar quem realizou o pagamento. |
| RN06 | A divisão de uma despesa deve considerar somente os participantes selecionados para aquela despesa. |
| RN07 | Participantes sem conta devem poder ser considerados nos cálculos da viagem. |
| RN08 | A soma das parcelas atribuídas aos participantes deve corresponder ao valor total da despesa, respeitando o arredondamento monetário. |
| RN09 | O status de pagamento deve permitir distinguir valores pagos e pendentes. |
| RN10 | O resumo financeiro deve considerar as despesas registradas e os pagamentos confirmados conforme as regras do sistema. |
  -----------------------------------------------------------------------

### 6.1 Exemplo de divisão igualitária

Exemplo ilustrativo: uma despesa de R\$ 120,00 dividida igualmente entre
quatro participantes resulta em R\$ 30,00 para cada pessoa.

  Participante              Parcela
  ---------------- ----------------
  Participante A          R\$ 30,00
  Participante B          R\$ 30,00
  Participante C          R\$ 30,00
  Participante D          R\$ 30,00
  **Total**          **R\$ 120,00**

### 6.2 Arredondamento

Divisões monetárias podem gerar frações de centavo. O sistema deverá
distribuir eventuais centavos residuais de forma determinística para que
a soma das parcelas seja exatamente igual ao valor original.

**TO DO ---** Documentar o algoritmo de distribuição dos centavos
residuais e criar testes para valores que não sejam divisíveis
igualmente.

### 6.3 Acerto entre participantes

O objetivo do resumo financeiro é apresentar a situação de cada pessoa e
facilitar a identificação dos acertos necessários.

**TO DO ---** Formalizar o algoritmo de consolidação de saldos: como
considerar quem pagou cada despesa, a participação individual e os
pagamentos já realizados entre integrantes.

------------------------------------------------------------------------

## 7. Arquitetura e tecnologias

### 7.1 Tecnologias conhecidas

  -----------------------------------------------------------------------
  Área                    Tecnologia              Utilização
  ----------------------- ----------------------- -----------------------
  Linguagem               Kotlin                  Linguagem principal do
                                                  projeto.

  Multiplataforma         Kotlin Multiplatform    Compartilhamento de
                          (KMP)                   código entre
                                                  plataformas.

  Interface               Compose Multiplatform   Construção de
                                                  interfaces
                                                  compartilhadas.

  IDE                     Android Studio          Ambiente principal de
                                                  desenvolvimento.

  Build                   Gradle com Kotlin DSL   Configuração e
                                                  compilação.

  Controle de versão      Git e GitHub            Histórico e
                                                  colaboração.

  Testes                  Testes Kotlin/Gradle    Validação do código
                                                  compartilhado e das
                                                  plataformas.
  -----------------------------------------------------------------------

### 7.2 Arquitetura da aplicação

O projeto utiliza uma organização multiplataforma. O código comum fica
no módulo compartilhado, enquanto os pontos de entrada e integrações
específicas de cada plataforma ficam nos módulos correspondentes.

A organização das telas segue a separação entre interface,
estado/apresentação e lógica de dados. A intenção é manter os
componentes visuais independentes das regras de negócio.

Visão conceitual:

``` text
┌─────────────────────────────────────────────────────┐
│                   APLICAÇÕES                         │
├───────────────────┬─────────────────┬───────────────┤
│     Android       │       iOS       │   Desktop JVM │
│   androidApp      │     iosApp      │   desktopApp  │
└─────────┬─────────┴────────┬────────┴───────┬───────┘
          │                  │                │
          └──────────────────┼────────────────┘
                             │
                    ┌────────▼────────┐
                    │      shared     │
                    │                 │
                    │ UI / Compose    │
                    │ Presentation    │
                    │ Domain          │
                    │ Data            │
                    │ Repositories    │
                    └─────────────────┘
```

### 7.3 Separação de responsabilidades

  -----------------------------------------------------------------------
  Camada/área                         Responsabilidade
  ----------------------------------- -----------------------------------
  UI                                  Exibir telas, componentes e
                                      informações.

  Presentation                        Manter estado de tela e coordenar
                                      ações do usuário.

  Domain                              Concentrar modelos e regras
                                      centrais do negócio.

  Data                                Fornecer e organizar o acesso aos
                                      dados.

  Repository                          Abstrair a origem dos dados para as
                                      demais camadas.

  Platform source sets                Implementar integrações que
                                      dependem de uma plataforma
                                      específica.
  -----------------------------------------------------------------------

### 7.4 Padrões e decisões de arquitetura

A organização do código deve evitar que regras financeiras sejam
implementadas diretamente nos componentes de interface. Cálculos de
divisão e consolidação de saldos devem ser isoláveis e testáveis.

**TO DO ---** Registrar formalmente o padrão arquitetural adotado (por
exemplo, MVVM com Repository), os limites de cada camada e a estratégia
de injeção de dependência, caso utilizada.

### 7.5 Versões das ferramentas

As versões de ferramentas e bibliotecas devem ser registradas a partir
dos arquivos reais do projeto, principalmente
`gradle/libs.versions.toml` e arquivos Gradle.

**TO DO ---** Preencher a tabela com os valores conferidos no
repositório:

  Componente              Versão configurada
  ----------------------- --------------------
  Android Studio          TO DO
  Kotlin                  TO DO
  Kotlin Multiplatform    TO DO
  Compose Multiplatform   TO DO
  Android Gradle Plugin   TO DO
  Gradle                  TO DO
  Material 3              TO DO
  Kotlin Coroutines       TO DO
  Lifecycle               TO DO

------------------------------------------------------------------------

## 8. Estrutura do projeto

### 8.1 Organização de módulos

A estrutura conhecida do projeto utiliza módulos de aplicação por
plataforma e um módulo compartilhado.

``` text
TripSplit/
├── androidApp/
├── iosApp/
├── desktopApp/
├── shared/
│   └── src/
│       ├── commonMain/
│       ├── commonTest/
│       ├── androidMain/
│       ├── androidHostTest/
│       ├── iosMain/
│       ├── jvmMain/
│       └── jvmTest/
├── gradle/
│   └── libs.versions.toml
├── build.gradle.kts
├── settings.gradle.kts
└── gradle.properties
```

> A árvore acima é uma representação de referência. A estrutura exata
> deve ser mantida sincronizada com o repositório, pois módulos e source
> sets podem mudar durante a evolução do projeto.

### 8.2 Pacote principal

O pacote Kotlin utilizado no projeto é:

``` text
com.imd.tripsplit
```

### 8.3 Organização interna do código compartilhado

A organização interna deve refletir as funcionalidades e
responsabilidades do aplicativo, evitando que todas as telas e regras
fiquem concentradas em poucos arquivos.

Áreas conhecidas ou previstas:

-   `ui`: componentes, telas e tema.
-   `home`: funcionalidades da área inicial.
-   `perfil`: área de perfil.
-   `navigation`: rotas e navegação.
-   `data`: repositórios e fontes de dados.
-   `domain`: modelos e regras do domínio.

**TO DO ---** Atualizar esta seção com a árvore real do módulo
`shared/src/commonMain/kotlin/com/imd/tripsplit/`, incluindo todos os
pacotes e telas existentes.

------------------------------------------------------------------------

## 9. Modelo conceitual de dados

Esta seção apresenta os principais conceitos do domínio do TripSplit.
Ela não substitui o modelo de persistência efetivamente implementado.

### 9.1 Entidades principais

  -----------------------------------------------------------------------
  Entidade                Descrição               Atributos conceituais
  ----------------------- ----------------------- -----------------------
  Viagem                  Agrupa participantes e  identificador, nome,
                          despesas de uma viagem. destino, datas

  Participante            Pessoa que participa de identificador, nome,
                          uma viagem.             vínculo com conta

  Despesa                 Gasto registrado dentro identificador,
                          de uma viagem.          descrição, valor, data,
                                                  pagador

  Divisão da despesa      Parcela atribuída a um  despesa, participante,
                          participante.           valor atribuído

  Pagamento/Acerto        Registro de um valor    identificador, origem,
                          quitado entre           destino, valor, status
                          participantes.          

  Usuário                 Pessoa que possui uma   identificador, nome,
                          conta no aplicativo.    e-mail
  -----------------------------------------------------------------------

Os atributos acima são conceituais. Os nomes, tipos e nulabilidade devem
corresponder aos modelos Kotlin efetivamente definidos.

### 9.2 Relacionamentos conceituais

``` text
USUÁRIO       1 ───── N VIAGEM (criação/participação, a confirmar)
VIAGEM        1 ───── N PARTICIPANTE
VIAGEM        1 ───── N DESPESA
DESPESA       1 ───── N DIVISÃO_DESPESA
PARTICIPANTE  1 ───── N DIVISÃO_DESPESA
PARTICIPANTE  1 ───── N DESPESA (como pagador)
PARTICIPANTE  1 ───── N PAGAMENTO (como origem/destino)
```

### 9.3 Participante com e sem conta

O modelo deve distinguir a pessoa registrada como participante da
eventual conta de usuário.

Um participante sem conta continua sendo uma pessoa considerada na
divisão das despesas. A associação a uma conta deve ser opcional no
modelo conceitual.

**TO DO ---** Confirmar como o projeto representa essa diferença nos
modelos Kotlin e como evita duplicidade de participantes.

### 9.4 Persistência

**TO DO ---** Documentar a tecnologia de persistência realmente
utilizada, as entidades implementadas, as relações, as migrações e a
política de sincronização, se houver.

------------------------------------------------------------------------

## 10. Navegação e telas

### 10.1 Áreas do aplicativo

As telas devem apoiar o fluxo de organização da viagem, registro de
gastos e acompanhamento dos acertos.

  -------------------------------------------------------------------------------
  Área                    Finalidade              Situação documental
  ----------------------- ----------------------- -------------------------------
  Login                   Acesso à conta do       Prevista no projeto
                          usuário.                

  Cadastro                Criação de uma conta.   Prevista no projeto

  Recuperação de senha    Apoio ao acesso à       Prevista/implementação a
                          conta.                  confirmar

  Home                    Apresentar viagens e    Em desenvolvimento
                          acesso às funções       
                          principais.             

  Viagens                 Criar, listar e         Parte central do produto
                          consultar viagens.      

  Detalhes da viagem      Reunir participantes,   Parte central do produto
                          despesas e resumo       
                          financeiro.             

  Participantes           Gerenciar pessoas       Parte central do produto
                          incluídas na viagem.    

  Despesas                Registrar e consultar   Parte central do produto
                          gastos.                 

  Perfil                  Exibir informações do   Em
                          usuário.                desenvolvimento/implementação a
                                                  confirmar
  -------------------------------------------------------------------------------

### 10.2 Navegação

O projeto utiliza Navigation Compose/Kotlin Serialization conforme a
estrutura de rotas adotada no desenvolvimento.

Uma estrutura de rotas conhecida inclui:

-   `Login`
-   `Home`
-   `TripDetail(tripId)`
-   `NewTrip`

A navegação deve transportar identificadores necessários, como `tripId`,
evitando depender de objetos completos passados diretamente entre telas.

**TO DO ---** Atualizar a lista com todas as rotas serializáveis atuais,
incluindo cadastro, recuperação de senha, perfil e demais destinos
existentes.

### 10.3 Fluxo principal

``` text
Login
  |
  v
Home
  |
  +----> Criar viagem
  |         |
  |         v
  |     Informar dados
  |         |
  |         v
  |     Adicionar participantes
  |
  v
Selecionar viagem
  |
  v
Detalhes da viagem
  |
  +----> Participantes
  |
  +----> Registrar despesa
  |         |
  |         v
  |     Selecionar pagador
  |         |
  |         v
  |     Selecionar participantes
  |         |
  |         v
  |     Calcular divisão
  |
  v
Resumo financeiro
  |
  v
Acompanhar valores pagos e pendentes
```

### 10.4 Responsabilidade das telas

As telas devem receber os dados e callbacks necessários para apresentar
o conteúdo e comunicar ações. A navegação deve permanecer concentrada na
camada responsável por coordenar as rotas, evitando acoplamento
desnecessário entre telas.

------------------------------------------------------------------------

## 11. Design System

O TripSplit possui uma identidade visual baseada em tons de azul, verde
e laranja, com cores de fundo claras e suporte a tema escuro.

### 11.1 Paleta de cores

| **Token**   | **Nome**     | **Hex**   | **Uso**                  |
|-------------|--------------|-----------|--------------------------|
| `Primary`   | Azul Oceano  | `#2563EB` | Ações principais         |
| `Secondary` | Verde Viagem | `#16A34A` | Sucesso / saldo positivo |
| `Accent`    | Laranja      | `#F97316` | Viagens / destaques      |

---

### 11.2 Cores de superfície

| **Token**    | **Nome**       | **Hex**   | **Uso**            |
|--------------|----------------|-----------|--------------------|
| `Background` | Branco Azulado | `#F8FAFC` | Fundo das telas    |
| `Surface`    | Branco         | `#FFFFFF` | Cards e containers |
| `Border`     | Cinza Claro    | `#E2E8F0` | Bordas e divisores |

---

### 11.3 Cores de texto

| **Token**       | **Nome**         | **Hex**   | **Uso**                 |
|-----------------|------------------|-----------|-------------------------|
| `TextPrimary`   | Azul Quase Preto | `#172033` | Texto principal         |
| `TextSecondary` | Cinza Azulado    | `#64748B` | Texto secundário        |
| `TextDisabled`  | Cinza            | `#94A3B8` | Elementos desabilitados |

---

### 11.4 Cores semânticas

| **Token** | **Hex**   | **Significado**      |
|-----------|-----------|----------------------|
| `Success` | `#16A34A` | Positivo / concluído |
| `Error`   | `#DC2626` | Dívida / erro        |
| `Warning` | `#EAB308` | Atenção              |
| `Info`    | `#2563EB` | Informação           |

### 11.5 Princípios visuais

- Azul como cor de identidade e ação principal.
- Verde para comunicar estados positivos e informações financeiras
  favoráveis.
- Laranja para chamar atenção para ações ou informações que exigem
  destaque.
- Fundos claros e superfícies com contraste suficiente.
- Componentes reutilizáveis e consistentes.
- Suporte a tema escuro.

---

### 11.6 Tipografia, espaçamento e componentes

O projeto utiliza tokens de tema e espaçamento para evitar valores
visuais repetidos diretamente nas telas.

**TO DO ---** Completar a documentação com a escala tipográfica, pesos,
tamanhos, espaçamentos, raios, elevações e especificações dos
componentes definidos em `docs/designSystem.md`.

---

### 11.7 Referência oficial

A documentação completa do Design System está disponível no arquivo
[`docs/designSystem.md`](https://github.com/moveis-tripsplit/tripsplit/blob/main/docs/designSystem.md),
no repositório oficial do TripSplit.

------------------------------------------------------------------------

## 12. Casos de uso

### 12.1 Atores principais

-   Usuário autenticado.
-   Organizador da viagem.
-   Participante com conta.
-   Participante sem conta, representado no registro da viagem.

### 12.2 Identificação dos casos de uso

O projeto possui uma referência a casos de uso numerados de **CU01 a
CU14**.

**TO DO ---** Inserir a tabela oficial com o nome, ator, pré-condições e
pós-condições de cada CU01--CU14. A descrição individual de todos os
casos não está suficientemente registrada nesta versão da documentação e
não deve ser inventada.

### 12.3 Modelo de descrição recomendado

Para manter consistência, cada caso de uso deve apresentar:

  Campo                 Conteúdo
  --------------------- -------------------------------------------------
  Identificador         CUxx
  Nome                  Nome da funcionalidade.
  Ator principal        Quem inicia a ação.
  Objetivo              Resultado esperado.
  Pré-condições         O que precisa ser verdadeiro antes da execução.
  Fluxo principal       Sequência normal de ações.
  Fluxos alternativos   Variações e situações de exceção.
  Pós-condições         Estado do sistema após a execução.

------------------------------------------------------------------------

## 13. Testes e qualidade

### 13.1 Objetivo

Os testes devem verificar o comportamento das funcionalidades,
principalmente os cálculos financeiros e a consistência dos dados
relacionados às viagens.

### 13.2 Testes prioritários

-   Criação e consulta de viagens.
-   Inclusão de participantes com conta.
-   Inclusão de participantes sem conta.
-   Registro de despesas.
-   Identificação do pagador.
-   Seleção dos participantes que dividem uma despesa.
-   Divisão igualitária e tratamento de centavos.
-   Atualização dos valores pagos e pendentes.
-   Cálculo do resumo financeiro.
-   Navegação entre as telas.
-   Comportamento em diferentes plataformas.

### 13.3 Testes multiplataforma conhecidos

Comandos registrados para executar testes por alvo:

``` powershell
.\gradlew.bat :shared:testAndroidHostTest
.\gradlew.bat :shared:jvmTest
.\gradlew.bat :shared:iosSimulatorArm64Test
```

A disponibilidade do alvo iOS depende do ambiente de compilação e das
ferramentas Apple instaladas.

### 13.4 Qualidade e integração contínua

O repositório possui uma verificação obrigatória de CI identificada como
`build-and-lint`.

**TO DO ---** Documentar o workflow completo, os eventos que o acionam,
os jobs executados e os critérios exigidos para aprovação de Pull
Requests.

------------------------------------------------------------------------

## 14. Configuração e execução

### 14.1 Ferramentas

-   Android Studio.
-   Kotlin Multiplatform Plugin.
-   Gradle Wrapper do repositório.
-   Git.
-   JDK compatível com a configuração Gradle do projeto.
-   Xcode para compilação e execução do iOS.

### 14.2 Clonar o projeto

``` bash
git clone https://github.com/moveis-tripsplit/tripsplit.git
cd tripsplit
```

### 14.3 Sincronizar o Gradle

Abra o projeto no Android Studio e aguarde a sincronização do Gradle.
Utilize o Gradle Wrapper incluído no repositório para executar as
tarefas.

No Windows PowerShell:

``` powershell
.\gradlew.bat tasks
```

### 14.4 Executar por plataforma

Comandos registrados para as configurações do projeto:

**Android**

``` powershell
.\gradlew.bat :androidApp:assembleDebug
```

**Desktop JVM**

``` powershell
.\gradlew.bat :desktopApp:run
```

Para execução contínua durante o desenvolvimento, quando suportado pela
configuração do projeto:

``` powershell
.\gradlew.bat :desktopApp:hotRun --auto
```

**iOS**

A compilação e execução do aplicativo iOS exigem macOS e Xcode. O
projeto iOS utiliza o módulo `iosApp` como ponto de entrada da
plataforma.

### 14.5 Observações sobre ambiente

-   O Android pode ser desenvolvido e executado em Windows, desde que o
    ambiente Android esteja configurado.
-   O alvo Desktop JVM pode ser executado em ambiente compatível com a
    configuração Gradle.
-   A compilação e execução local do iOS exigem macOS e Xcode.

**TO DO ---** Registrar versões mínimas de JDK, Android Studio, SDK
Android, Xcode e sistemas operacionais suportados.

------------------------------------------------------------------------

## 15. Organização da equipe e desenvolvimento

### 15.1 Equipe

  Integrante         Participação
  ------------------ -------------------------------
  Thereza Angélica   Desenvolvimento do TripSplit.
  Beatriz Camilo     Desenvolvimento do TripSplit.
  Clara Macedo       Desenvolvimento do TripSplit.
  Vladimir Vieira    Desenvolvimento do TripSplit.

**TO DO ---** Atualizar a tabela com as responsabilidades atuais de cada
integrante, módulos atribuídos e responsáveis por revisão.

### 15.2 Organização do trabalho

O desenvolvimento deve ser acompanhado pelo quadro de tarefas do GitHub,
mantendo as atividades relacionadas a funcionalidades, correções, testes
e documentação.

### 15.3 Git e branches

Convenções de branches registradas para o projeto:

  Prefixo       Finalidade            Exemplo
  ------------- --------------------- ----------------------------
  `feat/`       Nova funcionalidade   `feat/login`
  `fix/`        Correção de defeito   `fix/top-safe-area`
  `hotfix/`     Correção urgente      `hotfix/navigation-crash`
  `refactor/`   Refatoração           `refactor/register-screen`
  `docs/`       Documentação          `docs/readme`
  `test/`       Testes                `test/expense-calculation`
  `chore/`      Manutenção            `chore/update-gradle`

### 15.4 Commits

O projeto utiliza convenção inspirada em Conventional Commits:

  Tipo         Uso
  ------------ -------------------------------------------------
  `feat`       Nova funcionalidade.
  `fix`        Correção de defeito.
  `refactor`   Refatoração sem alteração funcional pretendida.
  `test`       Inclusão ou alteração de testes.
  `docs`       Alterações na documentação.
  `chore`      Tarefas de manutenção.
  `ci`         Configuração de integração contínua.
  `perf`       Melhoria de desempenho.
  `build`      Alterações no sistema de build.

Exemplos:

``` text
feat: add trip registration screen
fix: correct top safe area
refactor: separate register form state
test: cover expense split calculation
docs: update project documentation
```

### 15.5 Pull Requests

O fluxo de colaboração deve utilizar branches por tarefa e revisão antes
da integração à branch principal.

**TO DO ---** Registrar as regras atuais de proteção da branch
principal, exigência de aprovação, checks obrigatórios e modelo de Pull
Request.

------------------------------------------------------------------------

## 16. Estado atual e pendências

### 16.1 Informações e decisões conhecidas

-   [x] O projeto se chama TripSplit.
-   [x] O objetivo é organizar e dividir despesas de viagens em grupo.
-   [x] O projeto utiliza Kotlin.
-   [x] O projeto utiliza Kotlin Multiplatform.
-   [x] A interface é desenvolvida com Compose Multiplatform.
-   [x] Existe um módulo compartilhado `shared`.
-   [x] Existem módulos de aplicação `androidApp`, `iosApp` e
    `desktopApp` na configuração conhecida.
-   [x] O pacote principal é `com.imd.tripsplit`.
-   [x] O domínio contempla viagens, participantes e despesas.
-   [x] O projeto considera participantes sem conta.
-   [x] O acompanhamento de valores pagos e pendentes faz parte da
    proposta.
-   [x] O Design System utiliza azul, verde e laranja como cores
    principais.
-   [x] O projeto utiliza GitHub e fluxo de branches por
    funcionalidade/correção.
-   [x] Há referência a casos de uso CU01--CU14.

### 16.2 Pendências para completar esta documentação

-   [ ] **TO DO:** Inserir a descrição oficial dos casos de uso
    CU01--CU14.
-   [ ] **TO DO:** Atualizar a árvore de diretórios com a estrutura real
    do repositório.
-   [ ] **TO DO:** Registrar as versões atuais das ferramentas e
    bibliotecas.
-   [ ] **TO DO:** Documentar todos os modelos Kotlin existentes.
-   [ ] **TO DO:** Documentar a estratégia de persistência.
-   [ ] **TO DO:** Formalizar o algoritmo de divisão e arredondamento.
-   [ ] **TO DO:** Formalizar o cálculo de saldos e acertos entre
    participantes.
-   [ ] **TO DO:** Descrever o fluxo de cobrança via Pix e seus limites.
-   [ ] **TO DO:** Completar a especificação das telas e rotas.
-   [ ] **TO DO:** Inserir a divisão atual de responsabilidades da
    equipe.
-   [ ] **TO DO:** Documentar os workflows de CI e os critérios do check
    `build-and-lint`.
-   [ ] **TO DO:** Registrar os critérios de aceite definidos para a
    entrega acadêmica.

### 16.3 Pendências de produto e implementação

-   [ ] **TO DO:** Confirmar quais funcionalidades estão implementadas e
    quais permanecem planejadas.
-   [ ] **TO DO:** Confirmar as regras de edição e exclusão de despesas.
-   [ ] **TO DO:** Confirmar permissões de organizadores e
    participantes.
-   [ ] **TO DO:** Confirmar se a divisão será exclusivamente
    igualitária ou terá outros modos.
-   [ ] **TO DO:** Confirmar o comportamento de participantes sem conta.
-   [ ] **TO DO:** Confirmar como o status de pagamento será atualizado.
-   [ ] **TO DO:** Confirmar se há sincronização de dados entre
    dispositivos.

------------------------------------------------------------------------

## Documentos complementares

Os seguintes documentos podem detalhar aspectos específicos do projeto:

-   [`docs/designSystem.md`](docs/designSystem.md) --- identidade
    visual, tokens e componentes.
-   `README.md` --- apresentação e instruções rápidas do repositório.
-   `CONTRIBUTING.md` --- convenções de contribuição e colaboração.
-   **TO DO:** criar ou atualizar documentação de arquitetura.
-   **TO DO:** criar ou atualizar documentação dos casos de uso.
-   **TO DO:** criar ou atualizar documentação de testes.

------------------------------------------------------------------------

*TripSplit --- Projeto acadêmico de Desenvolvimento para Dispositivos
Móveis.*

> **Manutenção:** manter este documento alinhado ao código e às decisões
> da equipe. Ao concluir uma funcionalidade, atualizar sua situação e
> substituir os itens `TO DO` correspondentes por informações
> verificadas.
