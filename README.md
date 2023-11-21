# **Pandoro Project**

## **Presentazione**

Benvenuto nel **Pandoro Project**, un software per la gestione delle prenotazioni di una pista da corsa. Questo documento fornisce una panoramica dei requisiti, delle funzionalità e dell'architettura del sistema.

![Preview dell'Interfaccia](https://github.com/Bl4ckDrake/pandoro_project/blob/master/preview.png)

---

## **1. Introduzione**

  È richiesta l'implementazione di un software necessario a semplificare la gestione di una pista da corse, situata ad Imola, per i dipendenti. 
  I clienti, i quali devono avere un minimo di 18 anni, possono effettuare le prenotazioni a partire dalle 08:00 fino alle 24:00, con una tariffa oraria di 1200. 
  Vengono applicate le seguenti promozioni in base la numero di ore o tipo di giornata:
  
  - 4 ore - 10% di sconto sul totale;
  - 6 ore - 20% di sconto sul totale;
  - 8 ore - 25% di sconto sul totale;
  - notturna (dalle 20:00 alle 24:00) - 50% sul totale già eventualmente scontato;
  - festivi (sabato incluso) - 25% sul totale già eventualmente scontato.

  Inoltre, il limite massimo di ore da poter prenotare equivale ad 8. Per quanto riguarda la cancellazione di una prenotazione, essa può essere effettuata entro un giorno rispetto alla data prenotata.

## **2. Requisiti Funzionali - Casi d'Uso**

  - Aggiunta di una prenotazione: <br>
      **Attori:** dipendenti; <br>
      **Descrizione:** i dipendenti inseriscono i dati del cliente e della prenotazione, e quest'ultima viene registrata dal sistema; <br>
      **Scenari alternativi:** se l'orario è già prenotato o il cliente non raggiunge l'età minima il sistema stampa un messaggio di avviso; <br>
  
  - Rimozione di una prenotazione: <br>
      **Attori:** dipendenti; <br>
      **Descrizione:** i dipendenti inseriscono l'ID della prenotazione da rimuovere, ed il sistema procede contrassegnare la prenotazione come cancellata;<br>
      **Scenari alternativi:** nel caso in cui la prenotazione non dovesse essere presente, o nel caso in cui l'operazione di cancellazione venga effettuata il giorno stesso rispetto alla data prenotata, il sistema provvede a stampare un messaggio di avviso.<br>
  
  - Modifica di una prenotazione: <br>
      **Attori:** dipendenti;<br>
      **Descrizione:** i dipendenti inseriscono l'ID della prenotazione da modificare, ed il sistema procede all'aggiornamento dello stato di pagamento (confermata/pagata o da pagare);<br>
      **Scenari alternativi:** nel caso in cui la prenotazione non dovesse essere presente il sistema provvede a stampare un messaggio di avviso.<br>
  
  - Visualizzazione delle prenotazioni per giornata: <br>
      **Attori:** dipendenti;<br>
      **Descrizione:** i dipendenti inseriscono la data che vogliono visualizzare ed il sistema provvede a visualizzare la lista di prenotazioni relative alla giornata;<br>
      **Scenari alternativi:** nel caso in cui la data non dovesse essere valida o non contenga prenotazioni il sistema provvede a stampare un messaggio di avviso.<br>
  
  - Visualizzazione di una prenotazione per ID: <br>
      **Attori:** dipendenti;<br>
      **Descrizione:** i dipendenti inseriscono l'ID della prenotazione da visualizzare e il sistema provvede a stampare i dati relativi a quella prenotazione;<br>
      **Scenari alternativi:** nel caso in cui dovessero esserci prenotazioni con l'ID inserito il sistema provvede a stampare un messaggio di avviso.<br>
  
  - Visualizzazione delle prenotazioni per ID di un utente: <br>
      **Attori:** dipendenti;<br>
      **Descrizione:** i dipendenti inseriscono l'ID del cliente di cui vogliono visualizzare le prenotazioni;<br>
      **Scenari alternativi:** nel caso in cui non dovessero esistere clienti con l'ID inserito il sistema provvede a stampare un messaggio di avviso.<br>

## 3. Requisiti non funzionali 

  - Salvataggio dei dati:
      I dati relativi a clienti e prenotazioni possono essere salvati in un file con estensione .csv, ed eventualmente anche in un database.

  - Interfaccia utente:
      L'interfaccia fornita all'utilizzatore del sistema può essere su console o grafica.

  - Modifica automatica delle prenotazioni passate:
      Ad ogni avvio del programma, il flag di ogni prenotazione viene settato come Stored quando e' stata svolta in passato.

## 4. Architettura del Sistema

  Il software segue un'architettura semplice. I dati possono essere memorizzati in file CSV o in un database (mySQL). I dipendenti possono interagire con l'interfaccia grafica.

## 5. Diagramma del Sistema

![Diagramma del Sistema](https://github.com/Bl4ckDrake/pandoro_project/blob/master/pandoro_project.png)


## 6. Diagramma dei Casi d'Uso

![Diagramma Use Case](https://github.com/Bl4ckDrake/pandoro_project/blob/master/use_cases.png)

## 7. Test del software

![Tabella di testing](https://github.com/Bl4ckDrake/pandoro_project/blob/master/pandoro_project_tests.xlsx)

## Requisiti e Guida per l'uso

  Steps:
  1. Avviare IntelliJ ed importare il progetto.
  2. Scaricare la libreria di interfaccia grafica JavaFX dal sito ufficiale: [Link to JavaFX SDK Download](https://download2.gluonhq.com/openjfx/21.0.1/openjfx-21.0.1_windows-x64_bin-sdk.zip)
  3. Esportare il file ``.zip`` in una path a scelta.
  4. Andare sulla sezione File cliccare su ``Project Structure``
  5. Nella sezione ``Project Settings`` cliccare su ``modules`` e alla destra cliccare sulla sezione ``Dependencies``
  6. Cliccare sul ``+`` e importare la libreria di ``JavaFX``, selezionando la cartella delle librerie. Fare ``apply`` e ``ok``
  7. Tasto destro su GestionePista e cliccare su ``Modify Run Configuration`` che si trova nella sezione ``More Run/Debug``
  8. Cliccare su ``Modify options`` e cliccare sull'opzione ``Add VM options``
  9. Accanto alla versione di Java è comparsa una casella in cui importare le VM options
  10. Incollare il seguente testo:

```--module-path "/path/to/javafx-sdk-21.0.1/lib/" --add-modules javafx.base,javafx.controls,javafx.fxml,javafx.graphics --add-opens javafx.controls/com.sun.javafx.scene.control=ALL-UNNAMED --add-opens javafx.controls/com.sun.javafx.scene.control.behavior=ALL-UNNAMED```

   11. Modificare la path dell'SDK e applicare le modifiche.
   12. In caso funzioni correttamente, fare tasto destro sul file ``pom.xml`` andare sulla sezione Maven e cliccare il tasto ``Reload Project``
   13. E ripetere i primi 4 passaggi.

## Argomenti al programma 

  Qualsivoglia avviare l'interfaccia di console di debug (CLI) e' necessario aggiungere ``--cli`` come argomento all'interno della lista degli argomenti passati all'entry point. <br> ***Attenzione: quando la CLI e' attiva, l'interfaccia grafica non verra' caricata.***
