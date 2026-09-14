This is a Kotlin Multiplatform project targeting Android, iOS, Desktop (JVM).

* [/iosApp](./iosApp/iosApp) contains an iOS application. Even if you’re sharing your UI with Compose Multiplatform,
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.

* [/shared](./shared/src) is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - [commonMain](./shared/src/commonMain/kotlin) is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    the [iosMain](./shared/src/iosMain/kotlin) folder would be the right place for such calls.
    Similarly, if you want to edit the Desktop (JVM) specific part, the [jvmMain](./shared/src/jvmMain/kotlin)
    folder is the appropriate location.

### Running the apps

Use the run configurations provided by the run widget in your IDE's toolbar. You can also use these commands and options:

- Android app: `./gradlew :androidApp:assembleDebug`
- Desktop app:
  - Hot reload: `./gradlew :desktopApp:hotRun --auto`
  - Standard run: `./gradlew :desktopApp:run`
- iOS app: open the [/iosApp](./iosApp) directory in Xcode and run it from there.

### Running tests

Use the run button in your IDE's editor gutter, or run tests using Gradle tasks:

- Android tests: `./gradlew :shared:testAndroidHostTest`
- Desktop tests: `./gradlew :shared:jvmTest`
- iOS tests: `./gradlew :shared:iosSimulatorArm64Test`

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…


# Documentação do Projeto — Aplicativo TripSplit

> Aplicativo mobile para divisão de contas em viagens em grupo

**Disciplina:** Desenvolvimento para dispositivos móveis  
**Equipe:** Thereza Angélica · Beatriz Camilo · Clara Macedo · Vladimir Vieira  
**Repositório:**  https://github.com/moveis-tripsplit/tripsplit  
**Kanban:**  https://github.com/orgs/moveis-tripsplit/projects/1/views/1  
**Versão:** 1.0 — 27 de Agosto de 2026.

---

### 📚 Guias e Tutoriais de Interface

- [🎨 Design System Oficial](./docs/designSystem.md) — Princípios, paleta de cores, tipografia, espaçamentos e componentes.
- [🎓 Tutorial & Guia da Tela de Login](./docs/README_LOGIN_TUTORIAL.md) — Passo a passo pedagógico explicando a arquitetura em Compose Multiplatform, State Hoisting, tokens e a construção da tela de login.