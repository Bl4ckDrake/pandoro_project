# Pandoro Project

## Introduzione

Benvenuto nel **Pandoro Project**, un software per la gestione delle prenotazioni di una pista da corsa. Questo documento fornisce una panoramica dei requisiti, delle funzionalità e dell'architettura del sistema.

![Preview dell'Interfaccia](https://github.com/Bl4ckDrake/pandoro_project/blob/master/preview.png)

---

## 1. Introduzione

  È richiesta l'implementazione di un software necessario a semplificare la gestione di una pista da corse, situata ad Imola, per i dipendenti. 
  I clienti, i quali devono avere un minimo di 18 anni, possono effettuare le prenotazioni a partire dalle 08:00 fino alle 24:00, con una tariffa oraria di 1200. 
  Vengono applicate le seguenti promozioni in base la numero di ore o tipo di giornata:
  
  - 4 ore - 10% di sconto sul totale;
  - 6 ore - 20% di sconto sul totale;
  - 8 ore - 25% di sconto sul totale;
  - notturna (dalle 20:00 alle 24:00) - 50% sul totale già eventualmente scontato;
  - festivi (sabato incluso) - 25% sul totale già eventualmente scontato.

  Inoltre, il limite massimo di ore da poter prenotare equivale ad 8. Per quanto riguarda la cancellazione di una prenotazione, essa può essere effettuata entro un giorno rispetto alla data prenotata.

## 2. Requisiti Funzionali - Casi d'Uso

  - Aggiunta di una prenotazione:
      Attori: dipendenti;
      Descrizione: i dipendenti inseriscono i dati del cliente e della prenotazione, e quest'ultima viene registrata dal sistema;
      Scenari alternativi: se l'orario è già prenotato o il cliente non raggiunge l'età minima il sistema stampa un messaggio di avviso;
  
  - Rimozione di una prenotazione:
      Attori: dipendenti;
      Descrizione: i dipendenti inseriscono l'ID della prenotazione da rimuovere, ed il sistema procede contrassegnare la prenotazione come cancellata;
      Scenari alternativi: nel caso in cui la prenotazione non dovesse essere presente, o nel caso in cui l'operazione di cancellazione venga effettuata il giorno stesso rispetto alla data prenotata, il sistema provvede a stampare un messaggio di avviso.
  
  - Modifica di una prenotazione:
      Attori: dipendenti;
      Descrizione: i dipendenti inseriscono l'ID della prenotazione da modificare, ed il sistema procede all'aggiornamento dello stato di pagamento (confermata/pagata o da pagare);
      Scenari alternativi: nel caso in cui la prenotazione non dovesse essere presente il sistema provvede a stampare un messaggio di avviso.
  
  - Visualizzazione delle prenotazioni per giornata
      Attori: dipendenti;
      Descrizione: i dipendenti inseriscono la data che vogliono visualizzare ed il sistema provvede a visualizzare la lista di prenotazioni relative alla giornata;
      Scenari alternativi: nel caso in cui la data non dovesse essere valida o non contenga prenotazioni il sistema provvede a stampare un messaggio di avviso.
  
  - Visualizzazione di una prenotazione per ID
      Attori: dipendenti;
      Descrizione: i dipendenti inseriscono l'ID della prenotazione da visualizzare e il sistema provvede a stampare i dati relativi a quella prenotazione;
      Scenari alternativi: nel caso in cui dovessero esserci prenotazioni con l'ID inserito il sistema provvede a stampare un messaggio di avviso.
  
  - Visualizzazione delle prenotazioni per ID di un utente
      Attori: dipendenti;
      Descrizione: i dipendenti inseriscono l'ID del cliente di cui vogliono visualizzare le prenotazioni;
      Scenari alternativi: nel caso in cui non dovessero esistere clienti con l'ID inserito il sistema provvede a stampare un messaggio di avviso.

## 3. Requisiti non funzionali 

  - Salvataggio dei dati:
      I dati relativi a clienti e prenotazioni possono essere salvati in un file con estensione .csv, ed eventualmente anche in un database.

  - Interfaccia utente:
      L'interfaccia fornita all'utilizzatore del sistema può essere su console o grafica.

## 4. Architettura del Sistema

  Il software segue un'architettura semplice. I dati possono essere memorizzati in file CSV o in un database (mySQL). I dipendenti possono interagire con l'interfaccia grafica.

## 5. Diagramma del Sistema

![Diagramma del Sistema](https://github.com/Bl4ckDrake/pandoro_project/blob/master/pandoro_project.png)


## 6. Diagramma degli Use Case

![Diagramma Use Case](https://github.com/Bl4ckDrake/pandoro_project/blob/master/use_cases.png)
