$(document).ready(function() {
    // crea una griglia vuota di 100 celle
    function createEmptyGrid(container) {
        for (let i = 0; i < 100; i++) {
            $(container).append('<div class="cell" data-index="' + i + '"></div>');
        }
    }

    createEmptyGrid('#player-grid');
    createEmptyGrid('#computer-grid');

    // calcola il numero totale di celle occupate da tutte le navi
    const totalShipCells = 4 * 1 + 3 * 3 + 2 * 3 + 1 * 2;

    // controlla se tutte le navi sono state piazzate
    function allShipsPlaced() {
        return $('#player-grid .cell.ship').length === totalShipCells;
    }

    // piazzamento nave
    $('#player-grid').on('click', '.cell', function () {
        const index = $(this).data('index');
        const type = $('#ship-type').val();
        const orientation = $('#ship-orientation').val();

        $.post(`/api/place-ship/${index}/${type}/${orientation}`, function(response) {
            // aggiorna la griglia: rimuove tutte le navi e colora quelle piazzate
            $('#player-grid .cell').removeClass('ship');
            if (response && response.battleships) {
                for (var i = 0; i < response.battleships.length; i++) {
                    var ship = response.battleships[i];
                    for (var j = 0; j < ship.points.length; j++) {
                        var point = ship.points[j];
                        var idx = point.x * 10 + point.y;
                        $('#player-grid .cell').eq(idx).addClass('ship');
                    }
                }
            }
        }).fail(function() {
            alert('Errore nel piazzamento della nave!');
        });
    });

    // attacco al campo nemico
    $('#computer-grid').on('click', '.cell', function () {
        if (!allShipsPlaced()) {
            alert('Devi piazzare tutte le navi prima di attaccare!');
            return;
        }
        const index = $(this).data('index');
        $.ajax({
            url: `/api/attacca/${index}`,
            method: 'PUT',
            success: function (response) {
                // colora la cella attaccata
                $('#computer-grid .cell').eq(index).addClass(response.hit ? 'hit' : 'miss');
                // colora la cella attaccata dal nemico
                $('#player-grid .cell').eq(response.enemyIndex).addClass('enemy-attack');
                // controlla la vittoria
                if (response.playerWin) {
                    alert("Hai vinto! Tutte le navi nemiche sono state affondate!");
                    $('#computer-grid .cell').off('click');
                } else if (response.enemyWin) {
                    alert("Hai perso! Tutte le tue navi sono state affondate!");
                    $('#computer-grid .cell').off('click');
                }
            },
            error: function () {
                alert('Errore nell\'attacco!');
            }
        });
    });
});
