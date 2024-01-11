# Astre

Administration et Suivi des Temps des Ressources d’Enseignement

# Installation (Avec installateur)

Executez l'installateur correspondant à votre système d'exploitation 

# Installation avec un .jar 

Pré-requis : Version Java supérieure ou égale à 17

Ouvrez un invité de commande et executez le .jar correspondant à votre système d'exploitation 

- Mac os --> Astre-macOS-1.0.jar
- Linux --> Astre-linux-1.0.jar
- Windows --> Astre-windows-1.0.jar 

```bash
java -jar [ nom du fichier ].jar
```

# Compilation du projet
1. Clonez le dépôt Astre sur vôtre machine locale :
   ```bash
   git clone https://github.com/El-Pine/Astre.git
   ```

2. Naviguez dans le dossier Astre :
   ```bash
   cd Astre
   ```

3. Construisez le projet Gradle, cela va générer les fichiers nécessaires au fonctionnement :
   ```bash
   ./gradlew build
   ```

4. Une fois construit, lancez Astre avec :
   ```bash
   ./gradlew run
   ```

# Connexion à la base de donnée 

Tout se fait automatiquement, vous n'avez donc aucune autre action à effectuer, la base de données est créee au lancement de l'application , après avoir renseigné les identifiants.

# Erreur possible

Sur linux une erreur de performance peut arriver, si c'est le cas utilisez la commande : 
   ```bash
   ulimit -c unlimited
   ```
