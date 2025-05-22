$(document).ready(function() {
    /*
        tipi di navi con numero e dimensione:
        DESTROYER(4, 1), // 1 nave da 4 caselle
        CRUISER(3, 3),   // 3 navi da 3 caselle
        SUBMARINE(2, 3), // 3 navi da 2 caselle
        LANCE(2, 3);     // 3 navi da 2 caselle (?)
    */

    // crea una griglia vuota con 100 celle
    function createEmptyGrid(container) {
        for (let i = 0; i < 100; i++) {
            $(container).append('<div class="cell" data-index="' + i + '"></div>');
        }
    }

    // crea le griglie del giocatore e del computer
    createEmptyGrid('#player-grid');
    createEmptyGrid('#computer-grid');

    // click sulle celle della griglia del giocatore per posizionare la nave
    $('#player-grid').on('click', '.cell', function () {
        const index = $(this).data('index');
        const type = $('#ship-type').val();
        const orientation = $('#ship-orientation').val();
        console.log(`[SHIP] Clicked cell index: ${index}, type: ${type}, orientation: ${orientation}`);
        // invia la richiesta per piazzare la nave
        $.ajax({
            url: '/api/place-ship/' + index + "/" + type + "/" + orientation,
            method: 'POST',
            success: function(response) {
                console.log('[SHIP] risposta:', response);
                // aggiorna la griglia del giocatore con le navi piazzate
                if (response && response.battleships) {
                    $('#player-grid .cell').removeClass('ship');
                    response.battleships.forEach(function(ship, shipIdx) {
                        console.log(`[SHIP] nave #${shipIdx} tipo: ${ship.type}, punti:`, ship.points);
                        if (ship.points) {
                            ship.points.forEach(function(point) {
                                const idx = point.x * 10 + point.y;
                                console.log(`[SHIP] cambio colore alla nave nella cella: ${idx} (x=${point.x}, y=${point.y})`);
                                $('#player-grid .cell').eq(idx).addClass('ship');
                            });
                        }
                    });
                } else {
                    console.log('[SHIP] nessuna nave piazzata o risposta non valida');
                }
            },
            error: function(xhr) {
                console.log('[SHIP] errore piazzando la nave:', xhr);
                alert('errore nel piazzamento della nave!');
            }
        });
    });

    // click sulle celle della griglia del computer per attaccare
    $('#computer-grid').on('click', '.cell', function () {
        const index = $(this).data('index');
        // invia la richiesta di attacco al backend
        $.ajax({
            url: '/api/attacca/' + index,
            method: 'PUT',
            success: function (response) {
                // colora la cella attaccata in base al risultato
                if (response.hit) {
                    $('#computer-grid .cell').eq(index).addClass('hit');
                } else {
                    $('#computer-grid .cell').eq(index).addClass('miss');
                }
                // attacco nemico automatico: colora la cella attaccata dal nemico sulla griglia del giocatore
                const enemyIdx = response.enemyIndex;
                $('#player-grid .cell').eq(enemyIdx).addClass('enemy-attack');
            },
            error: function (xhr) {
                alert('errore nell attacco!');
            }
        });
    });
});
