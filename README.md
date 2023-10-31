**Pandoro Project**

**Consegna:**

Realizzare un sw per la gestione delle prenotazioni di una pista da corsa.

**Requisiti:**

- Ogni ora ha un costo di **1200 euro**;
- le promozioni vengono gestite secondo la seguente tabella:

|                                   |                                         |
| :-------------------------------: | :-------------------------------------: |
|         **Tipo di promo**         |           **Sconto Applicato**          |
|               4 ore               |                   10%                   |
|               6 ore               |                   20%                   |
|               8 ore               |                   25%                   |
| notturna (dalle 20:00 alle 24:00) |                   50%                   |
|      festivi (sabato incluso)     | 25% (sul totale eventualmente scontato) |

 

- ogni prenotazione può raggiungere **max 8 ore**;

- il lasso di tempo in cui si può prenotare la pista va **dalle 08:00 alle 24:00**;

- il cliente deve avere un **minimo di 18 anni**, e al momento della prenotazione deve fornire:

  - **nome**;
  - **cognome**;
  - **data di nascita**;
  - **codice fiscale**;
  - _e-mail e numero di telefono_;
  - **data** per la prenotazione;
  - numero di **ore da prenotare**;

- va effettuato il **check-in** e il **check-out** del cliente

<!---->

- **non è possibile sovrapporre gli orari** delle prenotazioni (es. pren. già presente dalle 12 alle 14 → disponibilità per altre pren .dalle 8 alle 12 e dalle 14 alle 24);

- ad ogni prenotazione deve essere assegnato automaticamente un **codice univoco alfanumerico**, al quale il cliente dovrà fare un bonifico entro 1 giorno dalla data prenotata, in caso di **mancato pagamento** viene rimossa la prenotazione → il pagamento avviene solo tramite bonifico;

- nel caso di **annullamento** della prenotazione da parte del cliente non ci sono penali da far pagare, _va effettuato il rimborso,_ e viene rimossa la prenotazione;

- in caso di **maltempo** vanno rimosse tutte le prenotazioni di quella giornata; è quindi necessario considerare le **condizioni meteo**;

- gli unici soggetti ad interagire col sw sono i **dipendenti** → non serve l’autenticazione per i dipendenti;

- non si possono modificare le tariffe e le promozioni;

- i dipendenti devono poter:

  - **aggiungere** le prenotazioni;
  - **cancellare** le prenotazioni;
  - **visualizzare** la lista delle prenotazioni pagate e da pagare;

- si può sviluppare un'interfaccia grafica o console;

- si possono usare dei file CSV per la memorizzazione dei dati;
