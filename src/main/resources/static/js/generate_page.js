$(document).ready(function() {
    /*
        Tipi di navi con numero e dimensione:
        DESTROYER(4, 1), // 1 nave da 4 caselle
        CRUISER(3, 3),   // 3 navi da 3 caselle
        SUBMARINE(2, 3), // 3 navi da 2 caselle
        LANCE(2, 3);     // 3 navi da 2 caselle (?)
    */

    var type = "DESTROYER";
    var orientation = "HORIZONTAL"

    // Crea una griglia vuota con 100 celle
    function createEmptyGrid(container) {
        for (let i = 0; i < 100; i++) {
            $(container).append('<div class="cell" data-index="' + i + '"></div>');
        }
    }

    createEmptyGrid('#player-grid');
    createEmptyGrid('#computer-grid');

    // Click sulle celle della griglia del giocatore per posizionare la nave
    $('#player-grid').on('click', '.cell', function () {
        const index = $(this).data('index');
        console.log('Hai cliccato sulla cella ', index);

        $.ajax({
            url: '/api/place-ship/' + index + "/" + type + "/" + orientation,
            method: 'POST',
            success: function(response) {
                console.log('Posizionamento riuscito', response);
                // Qui potresti aggiornare la UI per mostrare la nave
                // Per esempio, aggiungi una classe CSS per evidenziare la nave
                $('#player-grid .cell').eq(index).addClass('ship');
            },
            error: function() {
                alert('Errore nel piazzamento della nave!');
            }
        });
    });

    /*
    // Se vuoi abilitare il popolamento casuale delle griglie, scommenta questo blocco
    $.ajax({
        url: '/api/popola-griglie',
        method: 'GET',
        success: function(response) {
            // response = { player: [1, 23, 45], computer: [10, 20, 30] }
            response.player.forEach(index => {
                $('#player-grid .cell').eq(index).addClass('ship');
            });
            response.computer.forEach(index => {
                $('#computer-grid .cell').eq(index).addClass('ship');
            });
        },
        error: function() {
            alert('Errore nel caricamento delle griglie!');
        }
    });
    */

    // Click sulle celle della griglia del computer per attaccare
    $('#computer-grid').on('click', '.cell', function () {
        const index = $(this).data('index');

        $.ajax({
            url: '/api/attacca/' + index,
            method: 'PUT',
            success: function (response) {
                if (response.hit) {
                    alert('Colpito!');
                    $('#computer-grid .cell').eq(index).css('background-color', 'red').addClass('hit');
                } else {
                    alert('Acqua!');
                    $('#computer-grid .cell').eq(index).css('background-color', 'lightgrey').addClass('miss');
                }
            },
            error: function () {
                alert('Errore nell\'attacco!');
            }
        });
    });
});
