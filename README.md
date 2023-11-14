# Pandoro Project

## Introduzione

Benvenuto nel **Pandoro Project**, un software per la gestione delle prenotazioni di una pista da corsa. Questo documento fornisce una panoramica dei requisiti, delle funzionalità e dell'architettura del sistema.

![Preview dell'Interfaccia](https://github.com/Bl4ckDrake/pandoro_project/blob/master/preview.png)


## Requisiti

Il software gestisce prenotazioni sulla pista da corsa, con i seguenti requisiti chiave:

- **Costo Orario**: Ogni ora ha un costo fisso di 1200 euro.
- **Promozioni**: Sono disponibili diverse promozioni con sconti, come 10% per 4 ore, 20% per 6 ore, ecc.
- **Limite Ore Prenotazione**: Ogni prenotazione può essere di massimo 8 ore.
- **Orari Prenotazione**: Le prenotazioni possono essere effettuate dalle 08:00 alle 24:00.
- **Dati Cliente**: I clienti devono fornire informazioni personali come nome, cognome, data di nascita, codice fiscale, e-mail, numero di telefono.
- **Check-in e Check-out**: Si richiede un processo di check-in e check-out per ogni cliente.
- **Codice Univoco**: Ogni prenotazione riceve un codice alfanumerico univoco (ID).
- **Pagamento**: Il pagamento avviene solo tramite bonifico entro 1 giorno dalla data prenotata.
- **Annullamento**: In caso di annullamento, il cliente riceve un rimborso.
- **Condizioni Meteo**: In caso di maltempo, vengono rimosse tutte le prenotazioni per quel giorno.
- **Interazione con Dipendenti**: Solo i dipendenti possono interagire col software.
- **Gestione Prenotazioni**: I dipendenti possono aggiungere, cancellare e visualizzare prenotazioni pagate e da pagare.
- **Import/Export Dati**: Deve essere possibile esportare ed importare dati tramite file CSV.

## Architettura del Sistema

Il software segue un'architettura semplice ma potente. I dati possono essere memorizzati in file CSV o in un database (mySQL). I dipendenti possono interagire con l'interfaccia grafica.

## Diagramma del Sistema

![Diagramma del Sistema](https://github.com/Bl4ckDrake/pandoro_project/blob/master/gestione_pista.png)

## Implementazione

È possibile implementare l'interfaccia utente grafica o console a discrezione del team di sviluppo. L'uso di file CSV semplifica la persistenza dei dati.

---

**Nota**: Questo documento è solo una guida iniziale. Il dettaglio dell'implementazione può variare in base alle specifiche necessità del progetto.
