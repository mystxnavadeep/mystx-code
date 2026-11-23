# mystxcode

[![Releases](https://img.shields.io/github/v/release/mystxnavadeep/mystx-code?style=flat-square&label=Download)](https://github.com/mystxnavadeep/mystx-code/releases)

**mystxcode** — a coding-agent platform for Android: chat with AI agents, run a Linux terminal, edit code in a built-in IDE, and browse the web from your phone. Self-contained: it sets up its own Linux environment from the Termux packages, no root needed.

*(End-to-end rebrand with an iOS 18+ glassmorphism UI and automated signed APK releases on GitHub Actions.)*

---

## Installation

Install the **mystxcode** app from the [GitHub Releases](https://github.com/mystxnavadeep/mystx-code/releases):

| Release | Package |
|---|---|
| Latest (arm64) | [mystxcode-v1.apk](https://github.com/mystxnavadeep/mystx-code/releases/latest) (or the newest `mystxcode-vN.apk`) |

The app is `com.mystxcode.app` and can be installed next to the Termux app without a conflict.

> **Building from source:** `./gradlew assembleRelease` (needs the Android SDK, JDK 17+ and Go; see CI below). Local signing is configured for `app/release.jks` by default, or through the `KEYSTORE_PATH` / `KEYSTORE_PASSWORD` / `KEY_ALIAS` / `KEY_PASSWORD` environment variables.

## Features

- **AI chat** — conversations with the agents of your choice (Claude, Codex, OpenCode, Gemini, and more), with a floating bubble, voice calling, and history.
- **Terminal** — a real Linux terminal in the app (Termux terminal emulator + bundled bash/zsh, x86_64 glibc assets), plus a persistent SSH pool.
- **IDE** — a built-in code editor with syntax highlighting (OneDark,emon) via the [Sora editor](https://github.com/Rosemoe/sora-editor), git integration, and a run terminal.
- **Browser** — a WebView-based browser with Eruda dev tools and browser-runtime extension hosting.
- **Server mode** — expose the terminal and editor on your phone so your PC can connect.
- **Tools** — file explorer, settings, customization, and much more.

### iOS 18+ glassmorphism UI

Every surface is a real frosted glass panel: deep ambient-mesh backdrops (#070B14 / #0F1629 / #18102B) with soft glow orbs behind 18–28dp backdrop blurs (RenderEffect, API 33+, with a semi-transparent fallback scrim below), white fills at 6–10% over #0E1526 tints, and crisp #FFFFFF glass borders on the top edge. Design tokens live in `theme/Glass.kt` (`GlassTokens` / `glassSurface` / `GlassCard` / `GlassBottomBar` / `GlassSheet`), so the chat, IDE and browser panels, dialogs and the About card all render in the same frosted language. Code syntax highlighting and terminal text stay at maximum contrast so nothing is unreadable through the translucency.

## Getting started

1. Install the app from the releases page above.
2. Open mystxcode: it installs its own Ubuntu-like Linux environment from the Termux packages on first start (or pick the built-in environment later in Settings → Local Tools).
3. Chat with your agents, or start the terminal, IDE or browser from the main screen.

## Development

```bash
# Java 17+, Android SDK, Go and the Android NDK available
./gradlew assembleDebug
```

The project builds with the Kotlin + Compose + Material3 stack (Jetpack Compose, AGP 9.5.1, Kotlin 2.3.20) and is fully rebranded: package `com.mystxcode.app`, applicationId `com.mystxcode.app`, debug build `com.mystxcode.debug`, icons generated in `app/src/main/res/mipmap-*` and `mipmap-anydpi-v26`, and every user-visible string says mystxcode.

## Automated signed APK releases

The release pipeline is fully automated on GitHub Actions — no local Android SDK required:

| Workflow | Trigger | What it does |
|---|---|---|
| [`release-apk.yml`](.github/workflows/release-apk.yml) | Tag push matching `v*` · or manual run (`v1`, `v2`, `v3`) | Builds the signed release APK, verifies it with `apksigner`, publishes it as an artifact and a GitHub Release (auto-generated notes) |
| [`ci-build.yml`](.github/workflows/ci-build.yml) | Push/PR to `main` | Fast debug APK build so every change is verified to compile |

**To ship v1 / v2 / v3:**

1. **One-time — create the release key and the 4 repository secrets.** Generate a keystore:

   ```bash
   keytool -genkeypair -v -keystore mystxcode-release.keystore -alias mystxcode \
       -keyalg RSA -keysize 2048 -validity 10000 -storePassword MYSTXCODE_STORE_PASSWORD \
       -keyPassword MYSTXCODE_KEY_PASSWORD -dname "CN=mystxcode, OU=mystxcode, O=mystxcode, L=mystxcode, ST=mystxcode, C=US"
   ```

2. Base64 it and add four secrets to the repository (Settings → Secrets and variables → Actions → New repository secret):

   | Secret | Value |
   |---|---|
   | `KEYSTORE_BASE64` | `base64 -w0 mystxcode-release.keystore` (the whole keystore file, newline included) |
   | `KEYSTORE_PASSWORD` | the store password used above |
   | `KEY_ALIAS` | `mystxcode` |
   | `KEY_PASSWORD` | the key password used above |

   (With the default password, you can skip the `-storePassword`/`-keyPassword` flags and use `123456` for both — the workflow reads `KEYSTORE_PASSWORD`/`KEY_PASSWORD` and falls back to `123456` if they are missing.)

3. Push a tag to release (each tag gets its own signed `mystcode…` release):

   ```bash
   git tag v1 && git push origin v1    # or v2, v3, v1.0.0, v2.1.0, …
   ```

   Or run the workflow manually: **Actions → Release APK → Run workflow** (enter `v1` / `v2` / `v3`).

   You'll get a `mystxcode-v1.apk` (v2 → `mystxcode-v2.apk`, …) attached to the GitHub Release with auto-generated notes. The versioning comes from the tag: `VERSION_NAME` is the tag without the `v` (so `v1` → `1`), and `VERSION_CODE` is the tag's number or the run number — `version.json` in the repo lists the latest release.

   To verify the signature yourself:

   ```bash
   apksigner verify --verbose mystxcode-v1.apk
   ```

## Repository map

- `app/` — the Android app (Kotlin + Jetpack Compose).
- `app/src/main/java/com/mystxcode/app` — sources (UI in `ui/`, data/services in `data/`, editor, terminal…).
- `app/src/main/assets/` — bundled environment: glibc rootfs, Termux packages, the IDE bridge, mini apps, browser extensions.
- `agy_ide_bridge/` — the Go IDE bridge (the terminal/editor/server backend).
- `termux-am/`, `termux-api/`, `termux-x11/`, `termux-am-build/` — Termux companion apps.
- `extensions/`, `extension_docs/`, `mini_app_docs/`, `user_docs/`, `docs/` — documentation and the mini app SDK.
- `scripts/` — install/backup/restore scripts.
- `.github/workflows/` — CI: `ci-build.yml` (push/PR), `release-apk.yml` (tag/manual releases).

## License

GPLv3+ (GNU General Public License v3.0 or any later version).
