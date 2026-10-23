# TraceLess Android — Prototype V0.1

TraceLess est un prototype éducatif Android qui guide l'utilisateur dans quelques réglages de confidentialité Wi-Fi.

## Ce que fait cette version
- Présente une checklist manuelle de confidentialité.
- Explique l'adresse MAC aléatoire, le DNS privé et l'historique local.
- Ouvre les paramètres Android de DNS privé.
- Fonctionne hors ligne et ne demande aucune permission réseau.
- Ne collecte ni n'envoie de données.

## Ce que cette version ne fait pas
- Ne détecte pas automatiquement les paramètres MAC ou DNS.
- Ne masque pas l'appareil au routeur.
- Ne supprime pas les journaux du routeur ou du fournisseur d'accès.
- Ne garantit pas l'anonymat.
- Ne contient pas de fonction VPN.

## Ouvrir le projet
1. Installer Android Studio sur un ordinateur.
2. Ouvrir le dossier `TraceLessAndroid`.
3. Laisser Gradle synchroniser les dépendances.
4. Lancer l'application sur un appareil Android ou un émulateur.

Le projet utilise Kotlin, Jetpack Compose et Android SDK 35, avec une version minimale Android 8.0 (API 26).

## État
Code source initial fourni comme prototype non compilé dans cet environnement. Une compilation et des tests sur un appareil réel sont nécessaires avant toute distribution.
