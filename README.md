# PikBoard

Chess Companion est une application mobile développée en **Kotlin** avec **Jetpack Compose**. Le but de l'application est de permettre aux utilisateurs de continuer les parties d'échecs entamées sur un échiquier physique, en les poursuivant directement sur leur appareil mobile.

> **Note** : Ce projet est réalisé dans le cadre d'un cours. Il présente donc à la fois des fonctionnalités abouties et d'autres en cours de développement.

## Fonctionnalités

### Fonctionnalités terminées
- **Création d'un utilisateur (Signup)**
- **Connexion (Login)**
- **Recherche d'utilisateurs**
    - Recherche d'utilisateurs existants
    - Envoi de demandes d'amis
    - Acceptation ou refus des demandes d'amis
- **Consultation des parties en cours**
- **Consultation du profil utilisateur**
- **Conversion d'une image d'échiquier en FEN**
- Démarrer une nouvelle partie
- Consulter l'historique complet des parties

### Fonctionnalités à venir
- Modifier le profil utilisateur
- Modifier l'image de profil
- Gérer les coups en temps réel pendant une partie
- Terminer une partie

## Prérequis

- **Environnement de développement** : [Android Studio](https://developer.android.com/studio)
- **Langage** : Kotlin
- **UI Framework** : Jetpack Compose
- **Backend** : Un serveur est requis pour le bon fonctionnement de l'application.

## Configuration du Backend

L'application se connecte à un backend disponible à l'URL suivante : `https://gnat-happy-drake.ngrok-free.app/v1`

Assurez-vous que ce backend est en ligne et accessible pour tester l'ensemble des fonctionnalités de l'application.


