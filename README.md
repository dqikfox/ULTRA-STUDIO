# Ultra Studio (Android)

A native Android client for **Ultra Studio**, a self-hosted AI image/video generation
app backed by ComfyUI. Built with Kotlin and Jetpack Compose as a full replacement for
using the web UI from a phone.

This app is a client only — it talks to an existing Ultra Studio / SHInEyVErSE server
over HTTP and does not include or run any generation backend itself.

## Features

- **Create** — pick a model/quality preset, write a prompt, configure batch count and
  advanced settings (sampler, scheduler, steps, CFG, seed), and generate images or
  videos (text-to-video and image-to-video) via a foreground service so a batch keeps
  running in the background.
- **Gallery** — browse past generations by album, move/copy/delete items, remix a past
  prompt back into Create, and view full-size images or play back videos.
- **Live Queue** — see generation jobs in flight across every signed-in session, with
  your own jobs highlighted and other users' model choices redacted.
- **Admin dashboard** — account management (disable, promote, reset password) for
  accounts with admin rights, detected at runtime since the API has no dedicated
  "am I admin" endpoint.
- **Nyx** — an ElevenLabs Conversational AI companion with voice and text chat, wired
  to the same gallery/generation actions as the rest of the app via client tools
  (list/create/move/copy/delete gallery items and folders, kick off a generation).

## Tech stack

- Kotlin + Jetpack Compose + Navigation Compose
- Hilt for dependency injection
- Retrofit + OkHttp + kotlinx.serialization, with a runtime-configurable base URL
  (server address is set in-app under Settings, not baked in at build time)
- Cookie-based session auth via a custom `CookieJar` backed by `EncryptedSharedPreferences`
- Coil for images and Media3/ExoPlayer for video, both wired through the same
  authenticated OkHttp client so gallery thumbnails and playback work behind login
- A foreground `Service` for long-running/batch generation requests
- `io.elevenlabs:elevenlabs-android` for the Nyx voice/text companion

## Building

Requires JDK 21 (a newer JDK will fail against this Gradle/AGP version) and the
Android SDK.

```bash
JAVA_HOME=/path/to/jdk-21 ./gradlew assembleDebug
```

The resulting APK is at `app/build/outputs/apk/debug/app-debug.apk`.

## Server address

On first launch the app asks for the Ultra Studio server's address (e.g. a Tailscale
hostname or LAN IP with port). This can be changed later from Settings without
reinstalling. There is a sibling project, **ShinyVerse**, that points at the same
backend's Kid Mode persona with a simplified, reskinned UI.
