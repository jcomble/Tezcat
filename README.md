# Tezcat
## _Le bot Discord pour les questionnaires événementiels_

**Tezcat** est un bot Discord entièrement développé en **Java** par soucis de performances et de réduction de l'empreinte carbone.
Il permet de lancer un questionnaire préparé à l'avance pendant un événement vocal, de récolter efficacement les réponses et d'afficher efficacement le top 5.
Le questionnaire perdure dans le temps, ainsi, il est possible de l'utiliser comme "Question du jour" pour soit récompenser les meilleurs, soit pour pouvoir uniquement poster des questions journalièrement.

## Commandes
Les commandes du bot ont pour but d'être le plus simple possible.
L'administrateur peut utiliser toutes les commandes et peut choisir un @rôle modérateur afin de déléguer le travail à d'autres personnes.

- %setmodorole : Définit le rôle qui modérera le bot

Les modérateurs ont accès au reste des commandes:
- %changeprefix : change le prefix des commandes
- %changequestion : change l'intitulé d'une question en particulier
- %creategame : initialise un questionnaire à partir d'une question par défaut et le nombre de question y est défini
- %downloadresults : télécharge les résultats du questionnaire
- %play : lance le questionnaire
- %resetgame : efface toutes les questions et les résultats
- %showquestions : affiche les questions et réponses du jeu
- %switchanswer : inverse les réponses des questions choisies du questionnaire
- %top5 : affiche le top 5

Les autres membres auront accès à la commande de base:
- %help : affiche la liste de commande

## Scénario habituel
##### Etape 1 - Créer le questionnaire
Il faut déterminer le nombre de questions et la question par défaut, voici un exemple:
> %creategame "Est-ce une musique officielle de Pokémon?" 10

Il vaut mieux taper cette commande à l'abri des regards indiscrets.
Cette commande va créer un questionnaire de 10 questions qui auront le même intitulé et la même réponse (*Vrai*).

Pour observer les questions et les réponses tapez : 
> %showquestions

Si vous avez besoin de changer l'intitulé des questions, vous taper une commande pour chacune des question, voici un exemple:
> %changequestion 10 "Est-ce une musique de Jojo's bizarre Adventure?"

Cette commande change la question n°10

Maintenant que vous avez préparé les questions, il faut mettre les bonnes réponses au bon endroit, par défaut les réponses sont toujours sur *Vrai*, il est plus efficace d'inverser toutes les réponses mauvaises en une commande non?, voici un exemple:
> %switchanswer 1 6 9

Cette commande a inversé la réponse 1, 6 et 9.
Etant donné que les réponses de la question 1, 6 et 9 étaient sur *Vrai*, elles sont toutes passées sur *Faux*.
Bien sûr si vous inversez une réponse sur *Faux*, elle passera sur *Vrai*.

Voilà vous avez initialisé le questionnaire!

##### Etape 2 - Lancer le questionnaire
Maintenant il est temps de lancer le questionnaire. Pour cela, rien de bien compliqué:
> %play

Faites cette commande là où les participants peuvent le voir sauf si vous testez le bot. 
Le modérateur/l'administrateur est le seul qui peut changer la question en réagissant sur l'emoji : ↘️
Laissez le temps aux participants de réagir, seule la première réaction effectué est pris en compte, cela évite le retournement de veste.
Une fois le questionnaire fini, il ne reste plus qu'à récolter les résultats!
##### Etape 3 - Afficher le top5 ou récupérer les résultats
S'il vous suffit de connaître le top5, cette commande vous suffira:
> %top5

Si vous avez besoin de plus de précisions voici une commande qui vous sera utile:
> %downloadresults

Elle résume sur un fichier texte le classement entier, les bonnes réponses données et toutes les réponses données.

## Installation
-Insérer un fichier "src/Tezcat.txt" contenant le token du bot Discord
Les libraries spécifiées sont nécessaires :
JDA-5.0.0-alpha.13-withDependencies.jar
sqlite-jdbc-3.36.0.3.jar

## Droits d'usage:
Author : jcomble
Libre d'usage.