Una volta inserita la cartella in IdeaProject eseguire le seguenti operazioni:

1. File --> Project Structure --> Libraries --> + New Project Libraries (Alt + Insert) --> Java --> e selezionare IdeaProjects\wiki\postgresql-42.7.1.jar
2. una volta importato il database (wiki.sql), cambiare nella classe ConnessioneDatabase a riga 16:
    private String url = "jdbc:postgresql://localhost:5432/wiki2.0";
    private String user = "postgres";
    private String password = "ciao";
con l'url, l'user e la psw del proprio sistema.

Account che sono Autori: 
mail: mariorossi@gmail.com 
psw: Ciao12345_
mail: gio@gmail.com 
psw: Ciao12345_

Account che non sono Autori:
mail: speres15@ycombinator.com
psw: gW5+N|cs
