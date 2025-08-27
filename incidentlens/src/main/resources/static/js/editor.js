document.addEventListener('DOMContentLoaded', function() {
    // Инициализация графа и paper
    const graph = new joint.dia.Graph({}, { cellNamespace: joint.shapes });
    const paper = new joint.dia.Paper({
        el: document.getElementById('paper'),
        width: 1120,  // A4 горизонтальный (297x210 мм) в пикселях при 100dpi
        height: 794,
        model: graph,
        gridSize: 10,
        drawGrid: true,
        background: {
            color: '#f8f9fa'
        },
        perpendicularLinks: true,
        snapLinks: { radius: 75 },
        embeddingMode: false,
        linkPinning: false,
        interactive: {
            labelMove: false
        }
    });

    // Настройка палитры
    setupPalette();

    // Настройка обработчиков для элементов
    setupElementHandlers();

    // Загрузка существующей диаграммы при редактировании
    loadExistingChart();

    // Обработчик сохранения
    document.getElementById('save-btn').addEventListener('click', saveChart);

    function setupPalette() {
        document.querySelectorAll('.palette-item').forEach(item => {
            item.addEventListener('dragstart', function(e) {
                e.dataTransfer.setData('text/plain', this.dataset.type);
                e.dataTransfer.setData('application/x-shape-type', this.dataset.type);
            });
        });

        const paperContainer = document.getElementById('paper').parentElement;
        paperContainer.addEventListener('dragover', function(e) {
            e.preventDefault();
            e.dataTransfer.dropEffect = 'move';
        });

        paperContainer.addEventListener('drop', function(e) {
            e.preventDefault();
            const type = e.dataTransfer.getData('application/x-shape-type');
            if (type) {
                const position = paper.clientToLocalPoint({
                    x: e.clientX,
                    y: e.clientY
                });
                addShape(type, position);
            }
        });
    }

    function addShape(type, position) {
        let element;
        const width = 100;
        const height = 60;
        const tempId = 'temp-' + Math.random().toString(36).substr(2, 9);

        switch (type) {
            case 'rectangle':
                element = new joint.shapes.standard.Rectangle({
                    position: position,
                    size: { width, height },
                    attrs: {
                        body: { fill: '#4e79a7', stroke: '#333' },
                        label: { text: 'Rectangle', fill: 'white' }
                    }
                });
                break;
            case 'circle':
                element = new joint.shapes.standard.Circle({
                    position: position,
                    size: { width, height },
                    attrs: {
                        body: { fill: '#f28e2b', stroke: '#333' },
                        label: { text: 'Circle', fill: 'white' }
                    }
                });
                break;
            case 'ellipse':
                element = new joint.shapes.standard.Ellipse({
                    position: position,
                    size: { width, height },
                    attrs: {
                        body: { fill: '#e15759', stroke: '#333' },
                        label: { text: 'Ellipse', fill: 'white' }
                    }
                });
                break;
            case 'rhombus':
                element = new joint.shapes.standard.Rhombus({
                    position: position,
                    size: { width, height },
                    attrs: {
                        body: { fill: '#76b7b2', stroke: '#333' },
                        label: { text: 'Rhombus', fill: 'white' }
                    }
                });
                break;
        }

        if (element) {
            element.set('type', type);
            element.set('tempId', tempId);
            graph.addCell(element);
        }
    }

    function setupElementHandlers() {
        // Двойной клик для редактирования текста
        paper.on('element:dblclick', function(elementView) {
            const element = elementView.model;
            const text = prompt('Enter text:', element.attr('label/text') || '');
            if (text !== null) {
                element.attr('label/text', text);
            }
        });

        // Контекстное меню для удаления
        paper.on('element:contextmenu', function(elementView, evt) {
            evt.preventDefault();
            if (confirm('Delete this element?')) {
                elementView.model.remove();
            }
        });

        // Обработка удаления через клавишу Delete
        document.addEventListener('keydown', function(e) {
            if (e.key === 'Delete' || e.key === 'Backspace') {
                const selectedCells = paper.getSelectedCells();
                if (selectedCells.length > 0) {
                    graph.removeCells(selectedCells);
                }
            }
        });
    }

    function loadExistingChart() {
        const path = window.location.pathname;
        const match = path.match(/\/editor\/(\d+)/);

        if (match) {
            const workId = match[1];
            axios.get(`/api/works/${workId}`)
                .then(response => {
                    const work = response.data;
                    document.getElementById('chart-title').value = work.title || 'Untitled Chart';
                    renderChart(work);
                })
                .catch(error => {
                    console.error('Error loading chart:', error);
                    alert('Failed to load chart');
                });
        }
    }

    function renderChart(work) {
        graph.clear();

        // Создаем маппинг ID для соединений
        const elementMap = new Map();

        // Добавляем фигуры
        work.figures.forEach(figure => {
            let element;
            const position = { x: figure.x, y: figure.y };
            const size = { width: figure.width, height: figure.height };

            switch (figure.type) {
                case 'rectangle':
                    element = new joint.shapes.standard.Rectangle({
                        id: figure.id,
                        position: position,
                        size: size,
                        attrs: {
                            body: { fill: '#4e79a7', stroke: '#333' },
                            label: { text: figure.text || 'Rectangle', fill: 'white' }
                        }
                    });
                    break;
                case 'circle':
                    element = new joint.shapes.standard.Circle({
                        id: figure.id,
                        position: position,
                        size: size,
                        attrs: {
                            body: { fill: '#f28e2b', stroke: '#333' },
                            label: { text: figure.text || 'Circle', fill: 'white' }
                        }
                    });
                    break;
                case 'ellipse':
                    element = new joint.shapes.standard.Ellipse({
                        id: figure.id,
                        position: position,
                        size: size,
                        attrs: {
                            body: { fill: '#e15759', stroke: '#333' },
                            label: { text: figure.text || 'Ellipse', fill: 'white' }
                        }
                    });
                    break;
                case 'rhombus':
                    element = new joint.shapes.standard.Rhombus({
                        id: figure.id,
                        position: position,
                        size: size,
                        attrs: {
                            body: { fill: '#76b7b2', stroke: '#333' },
                            label: { text: figure.text || 'Rhombus', fill: 'white' }
                        }
                    });
                    break;
            }

            if (element) {
                element.set('type', figure.type);
                graph.addCell(element);
                elementMap.set(figure.id, element);
            }
        });

        // Добавляем соединения
        work.connections.forEach(connection => {
            const source = elementMap.get(connection.source.id);
            const target = elementMap.get(connection.target.id);

            if (source && target) {
                const link = new joint.shapes.standard.Link({
                    source: { id: source.id },
                    target: { id: target.id },
                    attrs: {
                        line: { stroke: '#333', strokeWidth: 2 }
                    }
                });
                graph.addCell(link);
            }
        });
    }

    function saveChart() {
        const title = document.getElementById('chart-title').value || 'Untitled Chart';

        // Собираем данные для сохранения
        const figures = [];
        const connections = [];

        graph.getElements().forEach(element => {
            const position = element.position();
            const size = element.size();
            const type = element.get('type');
            const text = element.attr('label/text');

            // Для новых элементов используем временный ID
            const id = element.id.toString().startsWith('temp-') ?
                element.get('tempId') : element.id;

            figures.push({
                tempId: id,
                type: type,
                x: position.x,
                y: position.y,
                width: size.width,
                height: size.height,
                text: text
            });
        });

        graph.getLinks().forEach(link => {
            const sourceId = link.source().id.toString();
            const targetId = link.target().id.toString();

            // Используем временные ID для новых элементов
            const sourceTempId = sourceId.startsWith('temp-') ?
                graph.getCell(sourceId).get('tempId') : sourceId;
            const targetTempId = targetId.startsWith('temp-') ?
                graph.getCell(targetId).get('tempId') : targetId;

            connections.push({
                sourceTempId: sourceTempId,
                targetTempId: targetTempId
            });
        });

        // Отправляем данные на сервер
        const dto = {
            title: title,
            figures: figures,
            connections: connections
        };

        // Определяем режим: создание или обновление
        const path = window.location.pathname;
        const match = path.match(/\/editor\/(\d+)/);

        if (match) {
            // Обновление существующей работы
            axios.put(`/api/works/${match[1]}`, dto)
                .then(response => {
                    alert('Chart updated successfully!');
                })
                .catch(error => {
                    console.error('Error saving chart:', error);
                    alert('Failed to save chart');
                });
        } else {
            // Создание новой работы
            axios.post('/api/works', dto)
                .then(response => {
                    alert('Chart saved successfully!');
                    window.location.href = `/editor/${response.data.id}`;
                })
                .catch(error => {
                    console.error('Error saving chart:', error);
                    alert('Failed to save chart');
                });
        }
    }
});