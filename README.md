# SeniorManager

Android (Kotlin + Compose) MVP for **Senior Health Manager** with:

- 2 senior profiles (`senior_a`, `senior_b`)
- Room offline database with `profileId` indexed across health/medication/event entities
- Medication management + stock risk logic (`<=7`, `<=3`, `0` days)
- SOS flow (caregiver call + triple-click 112 trigger logic)
- Health diary entries
- PDF emergency report generator
- Glance home-screen widget with quick actions
- WorkManager stock checks + notification channels

## Tech stack

- Kotlin
- Jetpack Compose
- Room
- WorkManager
- Glance

## Local checks

- Unit tests: `./gradlew test`
