# 📱 Controle de Quebra - Rede Plus

> **⚠️ AVISO IMPORTANTE**: Este aplicativo **NÃO É OFICIAL** do supermercado Rede Plus. Foi desenvolvido como um projeto pessoal para auxiliar no controle de quebra de caixa, criado em apenas 1 dia como uma solução rápida e não foi levado a sério como um produto comercial.

## 📋 Descrição

Aplicativo Android desenvolvido em Kotlin com Jetpack Compose para auxiliar no controle de quebra de caixa em supermercados. O app oferece uma interface simples e intuitiva para contabilizar valores positivos e negativos durante o fechamento de caixa.

## 🎯 Funcionalidades

- **Contador Intuitivo**: Interface com botões grandes para incrementar/diminuir valores
- **Alertas Visuais**: Mudança de cores baseada no valor do contador
- **Alertas Sonoros**: Som de alerta quando valores críticos são atingidos
- **Vibração**: Feedback tátil para notificações
- **Modo Paisagem**: Interface otimizada para uso em tablets/terminais
- **Animações**: Transições suaves e efeitos visuais

## 🚀 Tecnologias Utilizadas

- **Kotlin** - Linguagem principal
- **Jetpack Compose** - UI moderna e declarativa
- **Material Design 3** - Design system
- **Android Studio** - IDE de desenvolvimento
- **Gradle** - Sistema de build

## 📱 Screenshots

### Tela Inicial
Interface de boas-vindas com animações e botão para acessar o controle.

### Tela de Controle
- Contador central com display grande
- Botões "+" e "-" para ajustar valores
- Cores de alerta baseadas no valor:
  - Verde: Valores normais
  - Amarelo: Atenção (≥10)
  - Laranja: Cuidado (≥20)
  - Vermelho: Crítico (≥50)
  - Vermelho piscando: Emergência (≥100)

## 🛠️ Instalação

### Pré-requisitos
- Android Studio Arctic Fox ou superior
- Android SDK API 24+
- Kotlin 1.8+

### Passos para Build

1. Clone o repositório:
```bash
git clone https://github.com/ronieremarques/app-android-breakage-control.git
cd app-android-breakage-control
```

2. Abra o projeto no Android Studio

3. Sincronize o Gradle e aguarde o download das dependências

4. Conecte um dispositivo Android ou inicie um emulador

5. Execute o projeto:
```bash
./gradlew assembleDebug
```

## 📦 Estrutura do Projeto

```
app/
├── src/main/
│   ├── java/com/example/controledequebra/
│   │   ├── HomeActivity.kt          # Tela inicial
│   │   ├── MainActivity.kt          # Tela de controle
│   │   └── ui/theme/                # Temas e cores
│   ├── res/
│   │   ├── anim/                    # Animações
│   │   ├── drawable/                # Ícones e imagens
│   │   └── values/                  # Strings e cores
│   └── AndroidManifest.xml
└── build.gradle.kts
```

## 🔧 Configuração

### Permissões
O app requer as seguintes permissões:
- `VIBRATE`: Para feedback tátil
- `MODIFY_AUDIO_SETTINGS`: Para controle de som

### Orientação
- **HomeActivity**: Portrait (padrão)
- **MainActivity**: Landscape (otimizado para uso em caixa)

## 🎨 Personalização

### Cores de Alerta
As cores de alerta podem ser modificadas no arquivo `MainActivity.kt`:

```kotlin
fun getAlertColor(): Color {
    return when {
        contador >= 100 || contador <= -100 -> Color(0xFF8B0000) // Vermelho escuro
        contador >= 50 || contador <= -50 -> Color(0xFFDC143C)   // Crimson
        contador >= 30 || contador <= -30 -> Color(0xFFFF4500)   // Orange Red
        // ... outras cores
    }
}
```

### Limites de Alerta
Os valores que ativam alertas podem ser ajustados conforme necessário.

## 📝 Licença

Este projeto é de código aberto e está disponível sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## ⚠️ Disclaimer

- Este aplicativo foi desenvolvido **APENAS PARA FINS EDUCACIONAIS**
- **NÃO É** um produto oficial do Rede Plus
- **NÃO É** recomendado para uso em produção
- Foi criado em **1 dia** como projeto de estudo
- **NÃO** possui suporte oficial ou garantias

## 🤝 Contribuições

Contribuições são bem-vindas! Sinta-se à vontade para:

1. Fazer um Fork do projeto
2. Criar uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abrir um Pull Request

## 📞 Contato

Se você tiver dúvidas ou sugestões sobre este projeto:

- **GitHub Issues**: [Abrir uma issue](https://github.com/ronieremarques/app-android-breakage-control/issues)
- **Email**: ronieremarques55@gmail.com

---

**Desenvolvido com ❤️ em apenas 1 dia para fins educacionais**

*"Às vezes as melhores soluções são as mais simples"* 🚀 