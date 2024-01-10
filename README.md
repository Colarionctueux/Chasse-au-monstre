# Saé "chasse au monstre"

Groupe H1 (S3-H):

- TOURNEUR Aymeri
- LECLERCQ Manon
- ECKMAN Nicolas
- BELGUEBLI Rayane
- GYSEMANS Thomas


### Description du projet :

Ce projet est un jeu _"chasse au monstre"_, c'est-à-dire qu'on a un monstre et un chasseur. L'objectif du monstre est de s'échapper en trouvant la sortie, et celle du chasseur est de trouver et tuer le monstre. 

Chaque tour, le monstre bouge et essaie de se frayer un chemin vers la sortie, et le chasseur tire sur la case où il pense que le monstre se trouve.


![](screenshots/image.png)

### Comment lancer le jeu :

Lancer [App.java](./src/main/java/fr/univlille/App.java)

### Fonctionnalités actuelles :

- Menu principal
- Génération d'un labyrinthe à l'aide d'un algorithme (Recursive Backtracing)
- Utilisation d'un labyrinthe prédéfini
- Affichage de l'historique de déplacement du monstre pour le chasseur
- Affichage de l'historique de de tir du chasseur pour le monstre
- Placement aléatoires du monstre et de la sortie
- Condition de victoire monstre/chasseur
- Règles du jeu personnalisables
- Vue monstre/chasseur différente
- Brouillard pour le monstre au-delà de 2/3 de distance
- Utiliser des grenades pour le chasseur
- Utiliser un "super jump" pour le monstre
- Possibilité de jouer contre une IA
- Possibilité de jouer à 2 sur un même PC
- Possibilité de jouer sur 2 PC différents à l'IUT (Multijoueur)

### Fonctionnalités futures :

- Créer un JAR pour lancer le jeu

### Jalon 1

|Personne|Travail|
|--------|-------|
|Rayane|Implémenté les méthodes du JAR|
|Aymeri|IHM|
|Thomas|Commentaires, javadoc, review du code|
|Manon|Tests|
|Nicolas|Devait aider pour le javafx mais soucis pour le faire marcher|


### Jalon 2

|Personne|Travail|
|--------|-------|
|Rayane|Ajout grenade pour chasseur, superjump pour monstre, refactoring|
|Aymeri|Clean code, peaufinage, brouillard de guerre, algorithme labyrinthe, paramétrage de la partie|
|Thomas|Lobby multijoueur (un joueur peut host une game et rejoindre une game)|
|Manon|IA du monstre, clean code|
|Nicolas|Menu en javaFX (UI), controllers|

### Jalon 3

|Personne|Travail|
|--------|-------|
|Rayane|Correction bug, ajout fonctionnalité labyrinthe prédéfini|
|Aymeri|Clean code, écriture rapport dev efficace|
|Thomas|Mode multijoueur fini et fonctionnel|
|Manon|Finalisation de l'ia, écriture rapport dev efficace|
|Nicolas|Finalisation aspect visuel|