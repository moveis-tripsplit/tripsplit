# 🎓 Tutorial & Guia de Estudo: Construção da Tela de Login no TripSplit (Compose Multiplatform)

> **Objetivo:** Este documento foi elaborado de forma estritamente pedagógica para guiar o seu estudo sobre como estruturar, desenhar e implementar uma interface profissional utilizando **Compose Multiplatform (Android, Desktop, iOS)** e **Material Design 3**, conectando os conceitos de Design System com código Kotlin limpo.

---

## 📑 Sumário

1. [Visão Geral e Mentalidade Declarativa](#1-visão-geral-e-mentalidade-declarativa)
2. [Arquitetura de Pastas: A Ordem dos Fatos](#2-arquitetura-de-pastas-a-ordem-dos-fatos)
3. [Passo 1 — A Fundação: Design System e Tema (`ui/theme`)](#3-passo-1--a-fundação-design-system-e-tema-uitheme)
   - [3.1 Cores e Tokens (`Color.kt`)](#31-cores-e-tokens-colorkt)
   - [3.2 Formas e Arredondamentos (`Shapes.kt`)](#32-formas-e-arredondamentos-shapeskt)
   - [3.3 Espaçamentos (`Spacing.kt`)](#33-espaçamentos-spacingkt)
   - [3.4 Tipografia e Hierarquia de Texto (`Type.kt`)](#34-tipografia-e-hierarquia-de-texto-typekt)
   - [3.5 O Provedor de Tema (`Theme.kt`)](#35-o-provedor-de-tema-themekt)
4. [Passo 2 — Ícones e Identidade Visual em Vetor (`ui/components`)](#4-passo-2--ícones-e-identidade-visual-em-vetor-uicomponents)
   - [4.1 Por que Vetores em Código? (`TripSplitIcons.kt`)](#41-por-que-vetores-em-código-tripspliticonskt)
   - [4.2 O Logotipo Oficial da Viagem (`TripSplitLogo.kt`)](#42-o-logotipo-oficial-da-viagem-tripsplitlogokt)
5. [Passo 3 — Estado da Interface (*State Holder Pattern*) (`LoginUiState.kt`)](#5-passo-3--estado-da-interface-state-holder-pattern-loginuistatekt)
6. [Passo 4 — A Tela de Login (`LoginScreen.kt`)](#6-passo-4--a-tela-de-login-loginscreenkt)
   - [6.1 Stateless vs. Stateful (*State Hoisting*)](#61-stateless-vs-stateful-state-hoisting)
   - [6.2 Tratamento de Teclado e Rolagem (*IME Padding & Scroll*)](#62-tratamento-de-teclado-e-rolagem-ime-padding--scroll)
   - [6.3 O Card com Campos de Entrada e Mascaramento de Senha](#63-o-card-com-campos-de-entrada-e-mascaramento-de-senha)
7. [Passo 5 — Integração no Ponto de Entrada (`App.kt`)](#7-passo-5--integração-no-ponto-de-entrada-appkt)
8. [Como Testar e Próximos Passos de Estudo](#8-como-testar-e-próximos-passos-de-estudo)

---

## 1. Visão Geral e Mentalidade Declarativa

No desenvolvimento mobile tradicional imperativo (como os antigos XMLs do Android), você criava a tela estática e, no código Kotlin/Java, buscava os elementos com `findViewById` para alterar propriedades (`button.setText(...)`, `editText.setError(...)`).

No **Jetpack Compose / Compose Multiplatform**, a interface é **declarativa**:
$$\text{UI} = f(\text{Estado})$$

- A interface é apenas uma **projeção visual** do estado atual.
- Quando o usuário digita uma letra no teclado, o evento altera o estado (`state = state.copy(email = novoTexto)`).
- O Compose detecta essa mudança e executa a **Recomposição** (*recomposition*), redesenhando com altíssima performance apenas os componentes que dependem desse valor.

---

## 2. Arquitetura de Pastas: A Ordem dos Fatos

Para manter o código manutenível e escalável, organizamos os arquivos criados dentro do módulo compartilhado `shared/src/commonMain/kotlin/com/imd/tripsplit/`:

```text
shared/src/commonMain/kotlin/com/imd/tripsplit/
├── App.kt                              <-- Ponto de entrada que aplica o tema
└── ui/
    ├── theme/                          <-- 1º: A base de design do app
    │   ├── Color.kt                    <-- Paleta de cores oficial
    │   ├── Shapes.kt                   <-- Raios de borda (8dp, 12dp, 24dp)
    │   ├── Spacing.kt                  <-- Escala de espaçamento (4dp, 8dp...)
    │   ├── Type.kt                     <-- Tipografia (H1, Body, Display...)
    │   └── Theme.kt                    <-- Configuração do MaterialTheme
    ├── components/                     <-- 2º: Elementos visuais reutilizáveis
    │   ├── TripSplitIcons.kt           <-- Ícones em vetor (Email, Lock, Olho...)
    │   └── TripSplitLogo.kt            <-- Logo oficial (Avião + Moeda)
    └── screens/
        └── login/                      <-- 3º: A funcionalidade de Login
            ├── LoginUiState.kt         <-- Modelo de dados do estado da tela
            └── LoginScreen.kt          <-- Composable (Stateless + Stateful)
```

---

## 3. Passo 1 — A Fundação: Design System e Tema (`ui/theme`)

Nenhum botão ou texto deve ter cores ou tamanhos "chumbados" (*hardcoded*) aleatoriamente. O Design System centraliza as decisões visuais.

### 3.1 Cores e Tokens (`Color.kt`)
Local: `shared/src/commonMain/kotlin/com/imd/tripsplit/ui/theme/Color.kt`

Declaramos constantes do tipo `androidx.compose.ui.graphics.Color` usando notação hexadecimal em `0xFF...`:
```kotlin
val BlueOcean = Color(0xFF2563EB)      // Azul principal: Ações, navegação, botões
val GreenTravel = Color(0xFF16A34A)    // Verde: Dinheiro positivo, segurança
val BackgroundLight = Color(0xFFF8FAFC)// Fundo limpo, levemente azulado
val SurfaceLight = Color(0xFFFFFFFF)   // Branco para o Card do formulário
val TextPrimary = Color(0xFF172033)    // Quase preto azulado (alto contraste)
val TextSecondary = Color(0xFF64748B)  // Cinza intermediário para legendas
val BorderLight = Color(0xFFE2E8F0)    // Cinza claro para bordas
```

### 3.2 Formas e Arredondamentos (`Shapes.kt`)
Local: `shared/src/commonMain/kotlin/com/imd/tripsplit/ui/theme/Shapes.kt`

O TripSplit tem identidade visual amigável e moderna, utilizando cantos arredondados:
```kotlin
val RadiusSM = 8.dp   // Badges e elementos pequenos
val RadiusMD = 12.dp  // Campos de texto e botões
val RadiusLG = 16.dp  // Cards pequenos
val RadiusXL = 24.dp  // Card do formulário de login
```

### 3.3 Espaçamentos (`Spacing.kt`)
Local: `shared/src/commonMain/kotlin/com/imd/tripsplit/ui/theme/Spacing.kt`

Seguindo a regra de múltiplos de `4dp`:
- `SpacingXS = 4.dp`
- `SpacingSM = 8.dp`
- `SpacingMD = 12.dp`
- `SpacingLG = 16.dp`
- `SpacingXL = 24.dp`
- `Spacing2XL = 32.dp`

### 3.4 Tipografia e Hierarquia de Texto (`Type.kt`)
Local: `shared/src/commonMain/kotlin/com/imd/tripsplit/ui/theme/Type.kt`

Mapeia a escala para o objeto `Typography` do Material 3:
- `headlineLarge` (28sp, Negrito) para títulos.
- `bodyMedium` (16sp) para texto comum.
- `bodySmall` (14sp) para descrições.

### 3.5 O Provedor de Tema (`Theme.kt`)
Local: `shared/src/commonMain/kotlin/com/imd/tripsplit/ui/theme/Theme.kt`

O composable `TripSplitTheme` empacota o `MaterialTheme`:
```kotlin
@Composable
fun TripSplitTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        shapes = TripSplitShapes,
        typography = TripSplitTypography,
        content = content
    )
}
```
**Conceito chave:** Qualquer componente dentro desse bloco pode acessar as cores do tema usando `MaterialTheme.colorScheme.primary` ou `MaterialTheme.colorScheme.background`.

---

## 4. Passo 2 — Ícones e Identidade Visual em Vetor (`ui/components`)

### 4.1 Por que Vetores em Código? (`TripSplitIcons.kt`)
Local: `shared/src/commonMain/kotlin/com/imd/tripsplit/ui/components/TripSplitIcons.kt`

Em Compose Multiplatform, para não depender de bibliotecas externas pesadas ou tarefas de build adicionais, criamos um utilitário com `ImageVector` e `PathParser`:
- `TripSplitIcons.Email`: Ícone de envelope com proporção exata para o campo de e-mail.
- `TripSplitIcons.Lock`: Ícone de cadeado para o campo de senha.
- `TripSplitIcons.Visibility` e `VisibilityOff`: Ícones de olho aberto/fechado para o botão de mostrar/ocultar senha.
- `TripSplitIcons.ShieldCheck`: Escudo com checkmark verde para o selo de segurança.

### 4.2 O Logotipo Oficial da Viagem (`TripSplitLogo.kt`)
Local: `shared/src/commonMain/kotlin/com/imd/tripsplit/ui/components/TripSplitLogo.kt`

O logotipo do TripSplit une dois elementos em uma única imagem vetorial:
1. O laço/moeda verde (`GreenTravel`) com o cifrão centralizado `$`.
2. O avião azul (`BlueOcean`) sobrevoando o laço.

A função composable `TripSplitLogo(size = 76.dp)` desenha o logotipo de forma nítida em qualquer densidade de tela sem perda de qualidade.

---

## 5. Passo 3 — Estado da Interface (*State Holder Pattern*) (`LoginUiState.kt`)

Local: `shared/src/commonMain/kotlin/com/imd/tripsplit/ui/screens/login/LoginUiState.kt`

Em vez de criar dezenas de variáveis soltas na tela, agrupamos todo o estado da tela em uma única `data class`:

```kotlin
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val rememberMe: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val isLoginEnabled: Boolean
        get() = email.isNotBlank() && password.isNotBlank() && !isLoading
}
```

**Benefícios:**
- Facilidade de teste de unidade.
- Imutabilidade (mudamos o estado com `.copy(...)`).
- A propriedade calculada `isLoginEnabled` desabilita o botão se o e-mail ou a senha estiverem em branco.

---

## 6. Passo 4 — A Tela de Login (`LoginScreen.kt`)

Local: `shared/src/commonMain/kotlin/com/imd/tripsplit/ui/screens/login/LoginScreen.kt`

### 6.1 Stateless vs. Stateful (*State Hoisting*)
Dividimos a tela em duas funções complementares:

1. **`LoginContent` (Stateless):**
   - Não armazena nenhum estado internamente.
   - Recebe `state: LoginUiState` e callbacks para cada ação (ex: `onEmailChange: (String) -> Unit`, `onLoginClick: () -> Unit`).
   - É fácil de testar e visualizar no `@Preview`.

2. **`LoginScreen` (Stateful):**
   - Cria o estado persistente com `rememberSaveable { mutableStateOf(LoginUiState()) }`.
   - Passa o estado e atualiza as variáveis quando os eventos acontecem.

### 6.2 Tratamento de Teclado e Rolagem (*IME Padding & Scroll*)
Em dispositivos móveis, quando o teclado virtual sobe, ele pode cobrir o botão de login se a tela for estática. Resolvemos isso com três modificadores:

```kotlin
Column(
    modifier = modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState()) // Permite rolar a tela se faltar espaço
        .imePadding()                          // Adiciona espaçamento automático quando o teclado abre
        .padding(...)
)
```

### 6.3 O Card com Campos de Entrada e Mascaramento de Senha
- **Título com Estilo Misto:** Usamos `buildAnnotatedString` para renderizar "Trip" em cor escura e "Split" em azul oceano sem perigo de quebra de linha.
- **Card de Formulário:** `Card` com fundo branco, borda sutil cinza `#E2E8F0` e raio `RadiusXL (24.dp)`.
- **E-mail:**
  - `singleLine = true`
  - `keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next)`
- **Senha:**
  - `visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()`
  - `trailingIcon`: Botão com ícone de olho que aciona `onTogglePasswordVisibility()`.
- **Botão Primário:**
  - Desabilitado automaticamente (`enabled = state.isLoginEnabled`) se os campos estiverem vazios.
  - Exibe um indicador de progresso giratório (`CircularProgressIndicator`) caso `state.isLoading` seja verdadeiro.
- **Selo de Criptografia:**
  - Uma `Row` ao final com o ícone de escudo verde e a mensagem "Dados protegidos com criptografia".

---

## 7. Passo 5 — Integração no Ponto de Entrada (`App.kt`)

Local: `shared/src/commonMain/kotlin/com/imd/tripsplit/App.kt`

No arquivo principal compartilhado, configuramos:
```kotlin
@Composable
@Preview
fun App() {
    TripSplitTheme {
        LoginScreen(
            onLoginSuccess = { println("Login realizado!") },
            onForgotPasswordClick = { println("Recuperar senha") },
            onSignUpClick = { println("Cadastrar conta") }
        )
    }
}
```

---

## 8. Como Testar e Próximos Passos de Estudo

### Como rodar a tela no seu ambiente:

- **Desktop (Execução Rápida via JVM):**
  ```bash
  ./gradlew :desktopApp:run
  ```
- **Android (Emulador ou Dispositivo Físico):**
  ```bash
  ./gradlew :androidApp:installDebug
  ```
- **Compilação do código compartilhado:**
  ```bash
  ./gradlew :shared:compileKotlinJvm
  ```

### Sugestões de experimentos para aprender praticando:
1. **Experimente alternar o tema:** No `App.kt`, chame `TripSplitTheme(darkTheme = true)` para ver a tela renderizada na paleta escura.
2. **Adicionar validação de formato de e-mail:** No `LoginUiState`, adicione um método que valide se o texto contém `@` e `.`.
3. **Conectar com um ViewModel real:** No futuro, o `LoginScreen` pode coletar o estado a partir de um `ViewModel` compartilhado (`androidx.lifecycle.viewmodel.compose`).
