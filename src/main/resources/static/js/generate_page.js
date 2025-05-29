$(document).ready(function() {
    // crea una griglia 10x10 
    function createEmptyGrid(container) {
        $(container).empty();
        for (let i = 0; i < 100; i++) {
            $(container).append('<div class="cell" data-index="' + i + '"></div>');
        }
    }

    function attachHandlers() {
        // piazza le navi
        $('#player-grid').off('click').on('click', '.cell', function () {
            const index = $(this).data('index');
            const type = $('#ship-type').val();
            const orientation = $('#ship-orientation').val();

            $.post(`/api/place-ship/${index}/${type}/${orientation}`, function(response) {
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
            }).fail(function(xhr) {
                let msg = 'Errore nel piazzamento della nave!';
                alert(msg);
            });
        });

        // attacca
        $('#computer-grid').off('click').on('click', '.cell', function () {
            const totalShipCells = 4 * 1 + 3 * 3 + 2 * 3 + 1 * 2;
            function allShipsPlaced() {
                return $('#player-grid .cell.ship').length === totalShipCells;
            }
            if (!allShipsPlaced()) {
                alert('Devi piazzare tutte le navi prima di attaccare!');
                return;
            }
            const index = $(this).data('index');
            $.ajax({
                url: `/api/attacca/${index}`,
                method: 'PUT',
                success: function (response) {
                    $('#computer-grid .cell').eq(index).addClass(response.hit ? 'hit' : 'miss');
                    $('#player-grid .cell').eq(response.enemyIndex).addClass('enemy-attack');
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
    }

    
    createEmptyGrid('#player-grid');
    createEmptyGrid('#computer-grid');
    attachHandlers();

    //reset button ricrea le griglie e riattacca gli handler
    $('#reset-btn').on('click', function () {
        $.post('/api/reset', function () {
            createEmptyGrid('#player-grid');
            createEmptyGrid('#computer-grid');
            attachHandlers();
        });
    });
});
