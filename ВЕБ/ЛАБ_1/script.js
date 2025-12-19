let selectedX = null;
let selectedR = [];
let currentR = 2;
let allPoints = {};
let availableRValues = [1, 1.5, 2, 2.5, 3];
let allResults = [];

document.addEventListener('DOMContentLoaded', function() {
    initXButtons();
    initRCheckboxes();
    setupFormHandler();
    setupClearButton();
    loadInitialData();
    drawGraph();
});

// Функция для загрузки данных при загрузке страницы
async function loadInitialData() {
    try {
        showLoading('Загрузка данных...');
        
        const response = await sendToBackend({
            action: 'get_data'
        });
        
        processServerResponse(response);
        
    } catch (error) {
        hideResult();
    }
}

function initXButtons() {
    const xValues = ['-3', '-2', '-1', '0', '1', '2', '3', '4', '5'];
    const container = document.getElementById('x-buttons');
    
    xValues.forEach(value => {
        const button = document.createElement('button');
        button.type = 'button';
        button.className = 'coord-button';
        button.textContent = value;
        button.dataset.value = value;
        button.addEventListener('click', function() {
            document.querySelectorAll('.coord-button').forEach(btn => {
                btn.classList.remove('selected');
            });
            this.classList.add('selected');
            selectedX = parseFloat(value);
            hideError('x-error');
        });
        container.appendChild(button);
    });
}

function initRCheckboxes() {
    const container = document.getElementById('r-checkboxes');
    
    availableRValues.forEach(value => {
        allPoints[value] = [];
    });
    
    availableRValues.forEach(value => {
        const item = document.createElement('div');
        item.className = 'checkbox-item';
        
        const checkbox = document.createElement('input');
        checkbox.type = 'checkbox';
        checkbox.id = `r-${value}`;
        checkbox.value = value;
        checkbox.addEventListener('change', function() {
            if (this.checked) {
                selectedR.push(parseFloat(value));
            } else {
                selectedR = selectedR.filter(r => r !== parseFloat(value));
            }
            
            if (selectedR.length > 0) {
                currentR = Math.max(...selectedR);
            } else {
                currentR = 2;
            }
            
            drawGraph();
            hideError('r-error');
        });
        
        const label = document.createElement('label');
        label.htmlFor = `r-${value}`;
        label.textContent = value;
        
        item.appendChild(checkbox);
        item.appendChild(label);
        container.appendChild(item);
    });
}

function setupFormHandler() {
    document.getElementById('coord-form').addEventListener('submit', async function(e) {
        e.preventDefault();
        
        if (validateForm()) {
            const yValue = parseFloat(document.getElementById('y-input').value.replace(',', '.'));
            
            showLoading('Проверка точки...');
            
            try {
                const maxR = Math.max(...selectedR);
                const response = await sendToBackend({
                    x: selectedX,
                    y: yValue,
                    r: maxR
                });
                
                processServerResponse(response);
                
            } catch (error) {
                showResult(`Ошибка: ${error.message}`);
            }
        }
    });

    document.getElementById('y-input').addEventListener('input', function() {
        const value = this.value.replace(',', '.');
        
        if (value === '' || value === '-') {
            this.style.borderColor = '#ddd';
            hideError('y-error');
            return;
        }
        
        const numValue = parseFloat(value);
        if (isNaN(numValue) || numValue < -3 || numValue > 3) {
            this.style.borderColor = '#e74c3c';
        } else {
            this.style.borderColor = '#27ae60';
            hideError('y-error');
        }
    });
}

function setupClearButton() {
    document.getElementById('clear-btn').addEventListener('click', async function() {
        try {
            showLoading('Очистка данных...');
            
            const response = await sendToBackend({
                action: 'clear'
            });
            
            processServerResponse(response);
                
        } catch (error) {
            showResult('Ошибка при очистке данных: ' + error.message);
        }
    });
}

function processServerResponse(response) {
    allResults = [];
    
    if (response && response.success && response.results && Array.isArray(response.results)) {
        response.results.forEach(item => {
            if (item && typeof item === 'object') {
                const result = createResultObject(item);
                allResults.push(result);
            }
        });
    }
    
    updatePointsFromResults();
    hideResult();
    updateResultsTable();
    drawGraph();
}

function createResultObject(item) {
    return {
        x: typeof item.x === 'number' ? item.x : 0,
        y: typeof item.y === 'number' ? item.y : 0,
        r: typeof item.r === 'number' ? item.r : 0,
        hit: Boolean(item.hit),
        execution_time: typeof item.execution_time === 'number' ? item.execution_time : 0,
        current_time: typeof item.current_time === 'string' ? item.current_time : '',
        hit_icon: typeof item.hit_icon === 'string' ? item.hit_icon : (item.hit ? '✅' : '❌')
    };
}

function updatePointsFromResults() {
    availableRValues.forEach(r => {
        allPoints[r] = [];
    });
    
    allResults.forEach(result => {
        if (result && typeof result.r === 'number') {
            const r = result.r;
            if (availableRValues.includes(r)) {
                allPoints[r].push({
                    x: result.x,
                    y: result.y,
                    hit: result.hit,
                    timestamp: new Date().getTime()
                });
            }
        }
    });
}

function updateResultsTable() {
    const tableBody = document.getElementById('results-body');
    const table = document.getElementById('results-table');
    const clearBtn = document.getElementById('clear-btn');
    
    tableBody.innerHTML = '';
    
    if (allResults.length === 0) {
        table.classList.remove('show');
        clearBtn.style.display = 'none';
        return;
    }
    
    table.classList.add('show');
    clearBtn.style.display = 'block';
    
    allResults.slice().reverse().forEach(result => {
        const row = document.createElement('tr');
        
        const executionTime = result.execution_time === 0 ? '<1' : result.execution_time;
        
        row.innerHTML = `
            <td>${result.x}</td>
            <td>${result.y}</td>
            <td>${result.r}</td>
            <td class="${result.hit ? 'hit-true' : 'hit-false'}">
                ${result.hit_icon}
            </td>
            <td>${executionTime} мс</td>
            <td>${result.current_time}</td>
        `;
        
        tableBody.appendChild(row);
    });
}

function validateForm() {
    let isValid = true;

    if (selectedX === null) {
        showError('x-error');
        isValid = false;
    }

    const yInput = document.getElementById('y-input');
    const yValue = parseFloat(yInput.value.replace(',', '.'));
    if (isNaN(yValue) || yValue < -3 || yValue > 3) {
        showError('y-error');
        isValid = false;
    }

    if (selectedR.length === 0) {
        showError('r-error');
        isValid = false;
    }

    return isValid;
}

function showError(elementId) {
    document.getElementById(elementId).style.display = 'block';
}

function hideError(elementId) {
    document.getElementById(elementId).style.display = 'none';
}

function showLoading(message) {
    const result = document.getElementById('result');
    result.className = 'result loading';
    result.textContent = message;
    result.style.display = 'block';
}

function showResult(message) {
    const result = document.getElementById('result');
    result.className = 'result error';
    result.textContent = message;
    result.style.display = 'block';
}

function hideResult() {
    const result = document.getElementById('result');
    result.style.display = 'none';
}

async function sendToBackend(data) {
    let response;
    try {
        response = await fetch('/fcgi-bin/untitled-1.0-SNAPSHOT.jar', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data)
        });
    } catch (error) {
        throw new Error('Ошибка сети');
    }
    
    if (!response.ok) {
        if (response.status === 400) {
            throw new Error('Некорректный запрос');
        } else if (response.status === 500) {
            let errorData;
            try {
                errorData = await response.json();
            } catch (e) {
                throw new Error('Сервер недоступен');
            }
            throw new Error(errorData.error || 'Ошибка сервера');
        } else {
            throw new Error(`Ошибка HTTP: ${response.status}`);
        }
    }
    
    try {
        const responseData = await response.json();
        return responseData;
    } catch (error) {
        throw new Error('Неверный ответ от сервера');
    }
}

function drawGraph() {
    const canvas = document.getElementById('graph');
    const ctx = canvas.getContext('2d');
    const width = canvas.width;
    const height = canvas.height;
    const centerX = width / 2;
    const centerY = height / 2;
    const scale = 40;

    ctx.clearRect(0, 0, width, height);

    ctx.fillStyle = '#f8f9fa';
    ctx.fillRect(0, 0, width, height);

    if (selectedR.length > 0) {
        ctx.fillStyle = 'rgba(52, 152, 219, 0.3)';
        
        ctx.fillRect(centerX - (currentR/2) * scale, centerY - currentR * scale, 
                    (currentR/2) * scale, currentR * scale);
        
        ctx.beginPath();
        ctx.moveTo(centerX, centerY);
        ctx.lineTo(centerX, centerY + (currentR/2) * scale);
        ctx.arc(centerX, centerY, (currentR/2) * scale, Math.PI/2, Math.PI, false);
        ctx.closePath();
        ctx.fill();
        
        ctx.beginPath();
        ctx.moveTo(centerX, centerY);
        ctx.lineTo(centerX + (currentR/2) * scale, centerY);
        ctx.lineTo(centerX, centerY + currentR * scale);
        ctx.closePath();
        ctx.fill();
    }

    ctx.strokeStyle = '#2c3e50';
    ctx.lineWidth = 2;
    
    ctx.beginPath();
    ctx.moveTo(30, centerY);
    ctx.lineTo(width - 30, centerY);
    ctx.stroke();
    
    ctx.beginPath();
    ctx.moveTo(centerX, 30);
    ctx.lineTo(centerX, height - 30);
    ctx.stroke();

    ctx.fillStyle = '#2c3e50';
    
    ctx.beginPath();
    ctx.moveTo(width - 40, centerY - 8);
    ctx.lineTo(width - 20, centerY);
    ctx.lineTo(width - 40, centerY + 8);
    ctx.fill();
    
    ctx.beginPath();
    ctx.moveTo(centerX - 8, 40);
    ctx.lineTo(centerX, 20);
    ctx.lineTo(centerX + 8, 40);
    ctx.fill();

    ctx.font = '16px Arial';
    ctx.fillStyle = '#2c3e50';
    ctx.fillText('X', width - 15, centerY - 15);
    ctx.fillText('Y', centerX + 15, 25);

    ctx.font = '12px Arial';
    ctx.textAlign = 'center';
    ctx.textBaseline = 'middle';

    for (let i = -5; i <= 5; i++) {
        if (i === 0) continue;
        const x = centerX + i * scale;
        const y = centerY - i * scale;
        
        ctx.beginPath();
        ctx.moveTo(x, centerY - 5);
        ctx.lineTo(x, centerY + 5);
        ctx.stroke();
        ctx.fillText(i, x, centerY + 20);
        
        ctx.beginPath();
        ctx.moveTo(centerX - 5, y);
        ctx.lineTo(centerX + 5, y);
        ctx.stroke();
        ctx.fillText(i, centerX - 20, y);
    }

    ctx.fillText('0', centerX - 15, centerY + 20);
    
    if (selectedR.length > 0 && allPoints[currentR]) {
        allPoints[currentR].forEach(point => {
            drawPoint(point.x, point.y, point.hit);
        });
    }
    
    if (selectedR.length > 0) {
        const pointsCount = allPoints[currentR] ? allPoints[currentR].length : 0;
        document.getElementById('graph-status').textContent = `Текущий R: ${currentR} (точек: ${pointsCount})`;
    } else {
        document.getElementById('graph-status').textContent = 'Выберите значения R для отображения области';
    }
}

function drawPoint(x, y, hit) {
    const canvas = document.getElementById('graph');
    const ctx = canvas.getContext('2d');
    const centerX = canvas.width / 2;
    const centerY = canvas.height / 2;
    const scale = 40;
    
    const pointX = centerX + x * scale;
    const pointY = centerY - y * scale;
    
    ctx.beginPath();
    ctx.arc(pointX, pointY, 6, 0, Math.PI * 2);
    ctx.fillStyle = hit ? '#27ae60' : '#e74c3c';
    ctx.fill();
    ctx.strokeStyle = '#2c3e50';
    ctx.lineWidth = 2;
    ctx.stroke();
}