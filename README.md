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

Lancer App.java

### Fonctionnalités actuelles :

- Génération d'un labyrinthe généré à l'aide d'un algorithme. (Recursive Backtracing)
- Affichage de l'historique de déplacement du monstre pour le chasseur
- Placement du monstre/de la sortie aléatoirement
- Vue monstre/chasseur différente
- Condition de victoire monstre/chasseur
- Thèmes


### Fonctionnalités futures :

- Menu principal
- IA pour le monstre et le chasseur, pour pouvoir jouer en solo
- Possibilité de jouer sur 2 PC différents à l'IUT
- Règles du jeu personnalisables
- Créer un JAR pour lancer le jeu

### Fonctionnalités éventuelles :

- Brouillard pour le monstre au-delà de 2/3 de distance
- Mécaniques de gameplay et de jeu additionnelles

### Jalon 1


|Personne|Travail|
|--------|-------|
|Rayane|Implémenté les méthodes du JAR|
|Aymeri|IHM|
|Thomas|Commentaires, javadoc, review du code|
|Manon|Tests|
|Nicolas|Devait aider pour le javafx mais soucis pour le faire marcher|