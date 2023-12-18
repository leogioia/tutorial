# Isolation Level: quale scegliere?

In determinati contesti non è necessario garantire immediatamente tutte le caratteristiche di una transazione **ACID**, ma potrebbe essere preferibile avere una maggior performance.

I problemi che si possono verificare sono:
1. **Dirty Read**: si verifica in caso di letture di dati non ancora “committed” (es. una transazione legge un dato su cui viene eseguita una rollback subito dopo)
2. **Unrepeatable Read**: si verifica in caso di due letture dello stesso dato con una scrittura nel mezzo (es. lettura di un saldo di un conto corrente in caso di autorizzazioni su movimenti bancari)
3. **Phantom Row**: si verifica in caso di inserimento o cancellazione di nuove righe che potrebbero non comparire/comparire nel risultato di una query (es. aggiornare l'indirizzo di consegna ti tutti gli ordini di un cliente nel caso di inserimento o cancellazione di un ordine)

Un tipico esempio di applicazione del corretto isolation level in base al caso funzionale che si vuole implementare è un grosso negozio online.
Supponiamo di voler recuperare per ogni prodotto il numero di vendite dell'ultimo anno, non è necessario evitare dirty read, ma è preferibile avere elevate performance sul database.

Il livello di isolamento di una transazione determina il grado di protezione dei dati utilizzati concorrentemente dalle altre transazioni.

Lo standard SQL definisce 4 livelli di isolamento:
1. **Serializable**: le transazioni non vengono eseguite in parallelo, quindi nessuno dei problemi precedenti si verifica
2. **Repeatable Read**: viene posto un lock su tutti i dati usati dalla transazione, se si esegue una SELECT su una tabella il lock è posto su tutta la tabella e non solo sul risultato. In questo caso può verificarsi **Phantom Row**
3. **Read Commited**: il lock è posto unicamente sulle righe attualmente utilizzate dalla transazione e si possono leggere unicamente righe committate. In questo caso possono verificarsi **Phantom Row** e **Unrepeatable Read**
4. **Uncommited Read**: vengono lette anche righe dove è posto un lock, è utile in caso di SELECT su tabelle read-only. In questo caso possono verificarsi **Phantom Row**, **Unrepeatable Read** e **Dirty Read**