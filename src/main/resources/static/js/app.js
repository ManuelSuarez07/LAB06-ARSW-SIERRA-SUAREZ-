const app = (function() {
    // Estado privado
    let selectedAuthor = "";
    let blueprintsList = [];

    // Operación pública para cambiar el autor seleccionado
    function setSelectedAuthor(author) {
        selectedAuthor = author;
    }

    // Operación pública para actualizar el listado de planos
    function updateBlueprintsList(author) {
        apimock.getBlueprintsByAuthor(author, function(data) {
            blueprintsList = data.map(bp => ({
                name: bp.name,
                points: bp.points.length
            }));

            renderBlueprintsTable();
            updateTotalPoints();
        });
    }

    // Renderizar la tabla de planos
    function renderBlueprintsTable() {
        const tableBody = $('#blueprintsTableBody');
        tableBody.empty();

        blueprintsList.forEach(bp => {
            tableBody.append(`
                <tr>
                    <td>${bp.name}</td>
                    <td>${bp.points}</td>
                    <td><button class="btn btn-info" onclick="app.viewBlueprint('${selectedAuthor}', '${bp.name}')">View</button></td>
                </tr>
            `);
        });
    }

    // Actualizar el total de puntos
    function updateTotalPoints() {
        const totalPoints = blueprintsList.reduce((sum, bp) => sum + bp.points, 0);
        $('#totalPoints').text(totalPoints);
    }

    // Operación pública para ver un plano
    function viewBlueprint(author, bpname) {
        apimock.getBlueprintsByAuthor(author, function(data) {
            const blueprint = data.find(bp => bp.name === bpname);
            if (blueprint) {
                drawBlueprint(blueprint);
            }
        });
    }

    // Dibujar el plano en el canvas
    function drawBlueprint(blueprint) {
        const canvas = document.getElementById('blueprintCanvas');
        const ctx = canvas.getContext('2d');
        ctx.clearRect(0, 0, canvas.width, canvas.height);

        const points = blueprint.points;
        if (points.length > 0) {
            ctx.beginPath();
            ctx.moveTo(points[0].x, points[0].y);
            for (let i = 1; i < points.length; i++) {
                ctx.lineTo(points[i].x, points[i].y);
            }
            ctx.stroke();
        }
    }

    // Exponer funciones públicas
    return {
        setSelectedAuthor,
        updateBlueprintsList,
        viewBlueprint
    };
})();

// Asociar eventos
$(document).ready(function() {
    $('#getBlueprintsBtn').click(function() {
        const author = $('#authorInput').val();
        if (author) {
            app.setSelectedAuthor(author);
            app.updateBlueprintsList(author);
        } else {
            alert("Please enter an author name.");
        }
    });
});