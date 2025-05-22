$(document).ready(function() {
    /*
        Tipi di navi con numero e dimensione:
        DESTROYER(4, 1), // 1 nave da 4 caselle
        CRUISER(3, 3),   // 3 navi da 3 caselle
        SUBMARINE(2, 3), // 3 navi da 2 caselle
        LANCE(2, 3);     // 3 navi da 2 caselle (?)
    */

    var type = "DESTROYER";
    var orientation = "HORIZONTAL";

    // Crea una griglia vuota con 100 celle
    function createEmptyGrid(container) {
        for (let i = 0; i < 100; i++) {
            $(container).append('<div class="cell" data-index="' + i + '"></div>');
        }
    }

    createEmptyGrid('#player-grid');
    createEmptyGrid('#computer-grid');

    // click sulle celle della griglia del giocatore per posizionare la nave
    $('#player-grid').on('click', '.cell', function () {
        const index = $(this).data('index');
        console.log(`[SHIP] Clicked cell index: ${index}, type: ${type}, orientation: ${orientation}`);
        $.ajax({
            url: '/api/place-ship/' + index + "/" + type + "/" + orientation,
            method: 'POST',
            success: function(response) {
                console.log('[SHIP] risposta:', response);
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
        console.log(`[ATTACK] cliccato alla cella con index: ${index}`);
        $.ajax({
            url: '/api/attacca/' + index,
            method: 'PUT',
            success: function (response) {
                console.log('[ATTACK] risposta:', response);
                if (response.hit) {
                    alert('colpito!');
                    $('#computer-grid .cell').eq(index).css('background-color', 'red').addClass('hit');
                } else {
                    alert('Acqua!');
                    $('#computer-grid .cell').eq(index).css('background-color', 'lightgrey').addClass('miss');
                }
            },
            error: function (xhr) {
                console.log('[ATTACK] erorre attaccando:', xhr);
                alert('errore nell\'attacco!');
            }
        });
    });
});
