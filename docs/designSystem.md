# 🎨 TripSplit — Design System

> Design System oficial do aplicativo **TripSplit**.

O TripSplit é um aplicativo para gerenciamento e divisão de despesas em viagens em grupo. Seu Design System foi criado para transmitir **confiança, praticidade, organização e leveza**, combinando elementos de aplicativos financeiros com uma identidade visual relacionada a viagens.

---

# 📑 Sumário

* [1. Princípios de Design](#1-princípios-de-design)
* [2. Identidade Visual](#2-identidade-visual)
* [3. Cores](#3-cores)
* [4. Tipografia](#4-tipografia)
* [5. Espaçamentos](#5-espaçamentos)
* [6. Bordas e Radius](#6-bordas-e-radius)
* [7. Sombras](#7-sombras)
* [8. Botões](#8-botões)
* [9. Campos de Entrada](#9-campos-de-entrada)
* [10. Cards](#10-cards)
* [11. Estados](#11-estados)
* [12. Ícones](#12-ícones)
* [13. Navegação](#13-navegação)
* [14. Feedback e Mensagens](#14-feedback-e-mensagens)
* [15. Valores Financeiros](#15-valores-financeiros)
* [16. Layout](#16-layout)
* [17. Acessibilidade](#17-acessibilidade)
* [18. Dark Mode](#18-dark-mode)
* [19. Aplicação no Kotlin Compose](#19-aplicação-no-kotlin-compose)
* [20. Regras de Uso](#20-regras-de-uso)

---

# 1. Princípios de Design

O design do TripSplit deve seguir cinco princípios principais.

## 1.1 Simplicidade

As informações financeiras devem ser apresentadas de forma simples e fácil de entender.

O usuário deve conseguir identificar rapidamente:

* Quanto gastou;
* Quanto deve;
* Quanto tem para receber;
* Quem pagou;
* Quais despesas estão pendentes.

---

## 1.2 Clareza

Informações importantes devem possuir maior destaque visual.

Exemplo:

```text
Viagem para Natal

Saldo
R$ 320,00

Você tem a receber
```

O valor financeiro possui maior destaque que informações secundárias.

---

## 1.3 Consistência

Componentes semelhantes devem possuir aparência e comportamento semelhantes em todo o aplicativo.

Exemplo:

* Todos os botões principais utilizam `Primary`;
* Todos os valores positivos utilizam `Success`;
* Todas as dívidas utilizam `Error`.

---

## 1.4 Leveza

O TripSplit é um aplicativo relacionado a viagens e deve evitar uma aparência excessivamente corporativa ou bancária.

A interface deve utilizar:

* Espaços generosos;
* Cards;
* Cantos arredondados;
* Ícones simples;
* Poucas cores simultaneamente.

---

## 1.5 Feedback visual

Toda ação importante deve possuir uma resposta visual.

Exemplos:

* Despesa adicionada → sucesso;
* Dívida marcada como paga → sucesso;
* Campo inválido → erro;
* Informação incompleta → alerta.

---

# 2. Identidade Visual

A identidade do TripSplit utiliza três cores principais:

```text
🔵 Azul   → ações e navegação
🟢 Verde  → dinheiro positivo e sucesso
🟠 Laranja → viagens e destaques
```

Cores auxiliares:

```text
🔴 Vermelho → dívida / erro
🟡 Amarelo  → alerta
⚪ Branco   → superfícies
🌫️ Cinza    → informações secundárias
```

---

# 3. Cores

## 3.1 Paleta principal

| Token       | Nome         | Hex       | Uso                      |
| ----------- | ------------ | --------- | ------------------------ |
| `Primary`   | Azul Oceano  | `#2563EB` | Ações principais         |
| `Secondary` | Verde Viagem | `#16A34A` | Sucesso / saldo positivo |
| `Accent`    | Laranja      | `#F97316` | Viagens / destaques      |

---

## 3.2 Cores de superfície

| Token        | Nome           | Hex       | Uso                |
| ------------ | -------------- | --------- | ------------------ |
| `Background` | Branco Azulado | `#F8FAFC` | Fundo das telas    |
| `Surface`    | Branco         | `#FFFFFF` | Cards e containers |
| `Border`     | Cinza Claro    | `#E2E8F0` | Bordas e divisores |

---

## 3.3 Cores de texto

| Token           | Nome             | Hex       | Uso                     |
| --------------- | ---------------- | --------- | ----------------------- |
| `TextPrimary`   | Azul Quase Preto | `#172033` | Texto principal         |
| `TextSecondary` | Cinza Azulado    | `#64748B` | Texto secundário        |
| `TextDisabled`  | Cinza            | `#94A3B8` | Elementos desabilitados |

---

## 3.4 Cores semânticas

| Token     | Hex       | Significado          |
| --------- | --------- | -------------------- |
| `Success` | `#16A34A` | Positivo / concluído |
| `Error`   | `#DC2626` | Dívida / erro        |
| `Warning` | `#EAB308` | Atenção              |
| `Info`    | `#2563EB` | Informação           |

---

## 3.5 Regra de significado

As cores semânticas devem manter significado consistente:

```text
🟢 Success
Dinheiro a receber
Pagamento realizado
Despesa paga
Operação concluída

🔴 Error
Dívida
Saldo negativo
Erro
Pagamento atrasado

🟡 Warning
Atenção
Pendência
Informação importante

🔵 Info
Informação
Ação
Navegação
```

---

# 4. Tipografia

A tipografia deve priorizar legibilidade e aparência moderna.

## 4.1 Família

Fonte recomendada:

**Inter**

Caso a fonte não esteja disponível na plataforma, utilizar uma fonte sans-serif equivalente.

---

## 4.2 Escala tipográfica

| Token       | Tamanho |     Peso | Uso                           |
| ----------- | ------: | -------: | ----------------------------- |
| `Display`   |    32sp |     Bold | Valores e títulos de destaque |
| `H1`        |    28sp |     Bold | Títulos principais            |
| `H2`        |    24sp |     Bold | Títulos de seção              |
| `H3`        |    20sp | SemiBold | Subtítulos                    |
| `BodyLarge` |    18sp |  Regular | Informações importantes       |
| `Body`      |    16sp |  Regular | Texto padrão                  |
| `BodySmall` |    14sp |  Regular | Informações secundárias       |
| `Caption`   |    12sp |  Regular | Labels e detalhes             |

---

## 4.3 Hierarquia

Exemplo:

```text
Viagem para Natal          ← H1

Resumo financeiro          ← H2

Saldo                      ← BodySmall

R$ 320,00                  ← Display

Você tem a receber         ← Body
```

---

# 5. Espaçamentos

O sistema utiliza uma escala baseada em múltiplos de `4dp`.

| Token        | Valor |
| ------------ | ----: |
| `SpacingXS`  |   4dp |
| `SpacingSM`  |   8dp |
| `SpacingMD`  |  12dp |
| `SpacingLG`  |  16dp |
| `SpacingXL`  |  24dp |
| `Spacing2XL` |  32dp |
| `Spacing3XL` |  40dp |
| `Spacing4XL` |  48dp |

### Regra

Sempre que possível, utilizar valores dessa escala em vez de valores aleatórios.

---

# 6. Bordas e Radius

O TripSplit utiliza bordas arredondadas para transmitir uma aparência amigável.

| Token        | Valor | Uso                          |
| ------------ | ----: | ---------------------------- |
| `RadiusSM`   |   8dp | Campos pequenos              |
| `RadiusMD`   |  12dp | Botões                       |
| `RadiusLG`   |  16dp | Cards                        |
| `RadiusXL`   |  24dp | Cards especiais / containers |
| `RadiusFull` | 999dp | Avatares / chips             |

### Padrão

Cards:

```text
16dp
```

Botões:

```text
12dp
```

Campos:

```text
12dp
```

---

# 7. Sombras

O TripSplit deve utilizar sombras de forma discreta.

## Small

Utilizada para:

* Cards;
* Campos;
* Elementos elevados.

## Medium

Utilizada para:

* Modais;
* Bottom Sheets;
* Menus.

## Regra

Evitar sombras muito fortes.

A hierarquia deve ser criada principalmente através de:

1. Espaçamento;
2. Contraste;
3. Superfícies;
4. Bordas;
5. Sombras.

---

# 8. Botões

## 8.1 Primary Button

Utiliza a cor:

```text
#2563EB
```

Exemplo:

```text
┌──────────────────────────┐
│      Criar viagem        │
└──────────────────────────┘
```

Utilizado para ações principais.

---

## 8.2 Secondary Button

Utiliza a cor de destaque:

```text
#F97316
```

Utilizado para ações secundárias relacionadas à viagem.

---

## 8.3 Success Button

Utiliza:

```text
#16A34A
```

Exemplo:

```text
Marcar como paga
```

---

## 8.4 Destructive Button

Utiliza:

```text
#DC2626
```

Utilizado para ações destrutivas ou relacionadas a dívidas.

Exemplo:

```text
Excluir despesa
```

---

## 8.5 Estados dos botões

Todo botão deve possuir pelo menos:

```text
Default
Pressed
Disabled
Loading
```

---

# 9. Campos de Entrada

Os campos devem possuir:

* Background `Surface`;
* Border `Border`;
* Radius `12dp`;
* Padding horizontal `16dp`;
* Padding vertical `12dp`.

Exemplo:

```text
┌───────────────────────────────┐
│ Nome da viagem                │
└───────────────────────────────┘
```

## Estados

### Default

```text
Border → #E2E8F0
```

### Focus

```text
Border → #2563EB
```

### Error

```text
Border → #DC2626
```

### Disabled

```text
Text → #94A3B8
```

---

# 10. Cards

Cards são um dos principais componentes do TripSplit.

## Card padrão

Características:

```text
Background → #FFFFFF
Radius → 16dp
Padding → 16dp
```

Utilização:

* Viagens;
* Despesas;
* Pessoas;
* Resumo financeiro.

---

## Card de saldo

O saldo deve receber maior destaque.

Exemplo:

```text
┌──────────────────────────────┐
│ Saldo da viagem              │
│                              │
│ R$ 320,00                    │
│ ↑ Você tem a receber         │
└──────────────────────────────┘
```

---

# 11. Estados

Os componentes devem utilizar estados visuais consistentes.

| Estado       | Cor       |
| ------------ | --------- |
| Sucesso      | `#16A34A` |
| Erro         | `#DC2626` |
| Alerta       | `#EAB308` |
| Informação   | `#2563EB` |
| Desabilitado | `#94A3B8` |

---

# 12. Ícones

Os ícones devem ser:

* Simples;
* Consistentes;
* Preferencialmente outline;
* Fáceis de reconhecer.

## Exemplos

| Função        | Ícone sugerido |
| ------------- | -------------- |
| Viagem        | ✈️             |
| Despesa       | Receipt        |
| Pessoas       | Group          |
| Dinheiro      | Payments       |
| Pix           | Pix            |
| Adicionar     | Add            |
| Editar        | Edit           |
| Excluir       | Delete         |
| Voltar        | Arrow Back     |
| Configurações | Settings       |

Os ícones não devem substituir textos quando a ação puder gerar dúvida.

---

# 13. Navegação

A navegação principal deve priorizar as funcionalidades mais utilizadas.

Sugestão:

```text
┌─────────────────────────────────────┐
│                                     │
│        Conteúdo da tela             │
│                                     │
├─────────────────────────────────────┤
│  🏠       ✈️       💰       👤      │
│ Início  Viagens  Finanças  Perfil   │
└─────────────────────────────────────┘
```

A opção selecionada deve utilizar `Primary`.

---

# 14. Feedback e Mensagens

## Success

Utilizar para ações concluídas.

Exemplo:

```text
✓ Despesa adicionada com sucesso.
```

Cor:

```text
#16A34A
```

---

## Error

Utilizar para erros ou dívidas.

Exemplo:

```text
! Não foi possível adicionar a despesa.
```

Cor:

```text
#DC2626
```

---

## Warning

Utilizar para situações que precisam de atenção.

Exemplo:

```text
! João ainda não pagou sua parte.
```

Cor:

```text
#EAB308
```

---

## Info

Utilizar para informações gerais.

Exemplo:

```text
i Você pode adicionar pessoas que ainda não possuem uma conta.
```

Cor:

```text
#2563EB
```

---

# 15. Valores Financeiros

O dinheiro é uma das informações mais importantes do TripSplit.

## Valor positivo

```text
R$ 320,00
```

Cor:

```text
#16A34A
```

Representa:

> Dinheiro a receber.

---

## Valor negativo

```text
-R$ 45,00
```

Cor:

```text
#DC2626
```

Representa:

> Dinheiro a pagar.

---

## Valor neutro

```text
R$ 100,00
```

Cor:

```text
#172033
```

Utilizado quando o valor não representa diretamente ganho ou dívida.

---

# 16. Layout

## Margem horizontal

Padrão:

```text
16dp
```

Em telas maiores:

```text
24dp
```

---

## Estrutura padrão

```text
┌─────────────────────────────┐
│                             │
│  Header                     │
│                             │
│  ┌───────────────────────┐  │
│  │ Card                  │  │
│  └───────────────────────┘  │
│                             │
│  Título                     │
│                             │
│  Conteúdo                   │
│                             │
│  ┌───────────────────────┐  │
│  │ Card                  │  │
│  └───────────────────────┘  │
│                             │
│              +              │
│                             │
└─────────────────────────────┘
```

---

# 17. Acessibilidade

O TripSplit deve seguir princípios básicos de acessibilidade.

## Contraste

Textos devem possuir contraste suficiente com o background.

Evitar:

```text
Texto cinza claro
+
Background branco
```

---

## Não utilizar somente cores

Informações importantes não devem depender exclusivamente da cor.

Exemplo ruim:

```text
🟢
```

Exemplo melhor:

```text
🟢 R$ 80,00 a receber
```

---

## Tamanho dos elementos

Áreas interativas devem possuir tamanho adequado para toque.

Botões e elementos clicáveis devem possuir área confortável para interação em dispositivos móveis.

---

# 18. Dark Mode

O Dark Mode deve preservar a mesma linguagem visual do Light Mode.

## Cores sugeridas

| Token         | Light     | Dark      |
| ------------- | --------- | --------- |
| Background    | `#F8FAFC` | `#0F172A` |
| Surface       | `#FFFFFF` | `#1E293B` |
| TextPrimary   | `#172033` | `#F8FAFC` |
| TextSecondary | `#64748B` | `#CBD5E1` |
| Border        | `#E2E8F0` | `#334155` |
| Primary       | `#2563EB` | `#3B82F6` |
| Success       | `#16A34A` | `#22C55E` |
| Error         | `#DC2626` | `#EF4444` |
| Warning       | `#EAB308` | `#FACC15` |

As cores semânticas devem manter seus significados no Dark Mode.

---

# 19. Aplicação no Kotlin Compose

As cores do Design System devem ser centralizadas no tema do aplicativo.

Estrutura sugerida:

```text
shared/
└── src/
    └── commonMain/
        └── kotlin/
            └── com/
                └── imd/
                    └── tripsplit/
                        └── ui/
                            └── theme/
                                ├── Color.kt
                                ├── Theme.kt
                                ├── Type.kt
                                └── Shapes.kt
```

---

## Color.kt

As cores devem ser declaradas centralmente.

Exemplo:

```kotlin
val BlueOcean = Color(0xFF2563EB)
val GreenTravel = Color(0xFF16A34A)
val OrangeAccent = Color(0xFFF97316)

val BackgroundLight = Color(0xFFF8FAFC)
val SurfaceLight = Color(0xFFFFFFFF)

val TextPrimary = Color(0xFF172033)
val TextSecondary = Color(0xFF64748B)

val ErrorRed = Color(0xFFDC2626)
val WarningYellow = Color(0xFFEAB308)
```

---

## Theme.kt

O tema deve centralizar as cores utilizadas pelos componentes.

Exemplo conceitual:

```kotlin
MaterialTheme(
    colorScheme = lightColorScheme(
        primary = BlueOcean,
        secondary = GreenTravel,
        background = BackgroundLight,
        surface = SurfaceLight,
        error = ErrorRed
    )
)
```

Os componentes devem preferencialmente utilizar:

```kotlin
MaterialTheme.colorScheme.primary
```

em vez de declarar diretamente:

```kotlin
Color(0xFF2563EB)
```

Isso facilita a manutenção e permite implementar Dark Mode posteriormente.

---

# 20. Regras de Uso

## 20.1 Não criar cores aleatórias

Evitar:

```kotlin
Color(0xFF123456)
```

em componentes individuais.

Sempre verificar se existe um token correspondente no Design System.

---

## 20.2 Prioridade das cores

A hierarquia visual recomendada é:

```text
Primary
   ↓
Secondary
   ↓
Accent
   ↓
Semantic Colors
   ↓
Neutral Colors
```

---

## 20.3 Verde é semântico

O verde deve representar principalmente:

```text
✓ Sucesso
✓ Dinheiro a receber
✓ Pago
✓ Positivo
```

Não utilizar verde indiscriminadamente em todos os botões e componentes.

---

## 20.4 Vermelho é semântico

O vermelho deve representar:

```text
✕ Erro
✕ Dívida
✕ Saldo negativo
✕ Atraso
✕ Ação destrutiva
```

---

## 20.5 Azul é a cor de interação

O azul deve representar:

```text
→ Ações
→ Navegação
→ Links
→ Seleções
→ Elementos interativos
```

---

# 🎯 Resumo Visual

```text
                 TRIPSPLIT
                     │
        ┌────────────┼────────────┐
        ↓            ↓            ↓
      🔵 AZUL      🟢 VERDE     🟠 LARANJA
      Ações        Positivo     Viagem
      Navegação    Sucesso      Destaque
        │            │            │
        └────────────┼────────────┘
                     │
              Informações
               financeiras
                     │
           ┌─────────┴─────────┐
           ↓                   ↓
       🔴 VERMELHO         🟡 AMARELO
       Dívida/Erro         Atenção
```

---

# 📌 Tokens Principais

```text
Primary          #2563EB
Secondary        #16A34A
Accent           #F97316

Background       #F8FAFC
Surface          #FFFFFF
Border           #E2E8F0

TextPrimary      #172033
TextSecondary    #64748B
TextDisabled     #94A3B8

Success          #16A34A
Error            #DC2626
Warning          #EAB308
Info             #2563EB

RadiusSM         8dp
RadiusMD         12dp
RadiusLG         16dp
RadiusXL         24dp

SpacingXS        4dp
SpacingSM        8dp
SpacingMD        12dp
SpacingLG        16dp
SpacingXL        24dp
Spacing2XL       32dp
Spacing3XL       40dp
Spacing4XL       48dp
```

---

# 🚀 Objetivo do Design System

O Design System do TripSplit deve garantir que todas as telas do aplicativo tenham:

* Identidade visual consistente;
* Boa legibilidade;
* Hierarquia clara;
* Feedback visual;
* Fácil compreensão financeira;
* Aparência moderna;
* Adaptação para Light e Dark Mode;
* Facilidade de implementação no Kotlin Compose.

Qualquer novo componente ou tela deve seguir os tokens e regras definidos neste documento.
