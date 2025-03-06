const apimock = (function() {
    const mockdata = [];

    // Función para generar puntos de un cuadrado
    function generateSquare(size) {
        return [
            { x: 0, y: 0 },
            { x: size, y: 0 },
            { x: size, y: size },
            { x: 0, y: size },
            { x: 0, y: 0 }
        ];
    }

    // Función para generar puntos de un triángulo
    function generateTriangle(size) {
        return [
            { x: size / 2, y: 0 },
            { x: size, y: size },
            { x: 0, y: size },
            { x: size / 2, y: 0 }
        ];
    }

    // Función para generar puntos de un círculo (aproximado)
    function generateCircle(radius, numPoints = 20) {
        const points = [];
        for (let i = 0; i < numPoints; i++) {
            const angle = (i / numPoints) * 2 * Math.PI;
            points.push({
                x: radius * Math.cos(angle) + radius,
                y: radius * Math.sin(angle) + radius
            });
        }
        points.push(points[0]);
        return points;
    }

    // Planos para el autor "Manuel"
    mockdata["Manuel"] = [
        { name: "Cuadrado", points: generateSquare(100) },
        { name: "Triángulo", points: generateTriangle(100) },
        { name: "Círculo", points: generateCircle(50) }
    ];

    // Planos para el autor "Yeka"
    mockdata["Yeka"] = [
        { name: "Cuadrado Grande", points: generateSquare(150) },
        { name: "Triángulo Grande", points: generateTriangle(150) },
        { name: "Círculo Grande", points: generateCircle(75) }
    ];

    return {
        getBlueprintsByAuthor: function(author, callback) {
            callback(mockdata[author] || []);
        }
    };
})();