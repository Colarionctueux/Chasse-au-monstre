if [[ ! -f "lib/cam-player-api-base.jar" ]] ; then
  echo "T'as oublié d'ajouter la librairie du GOAAAT !!! (dans 'lib' qui s'appelle 'cam-player-api-base.jar' sur Moodle)"
  exit 1
fi

javac -cp lib/*.jar -d bin src/main/java/fr/univlille/*.java && java -cp bin:lib/*.jar fr.univlille.Main