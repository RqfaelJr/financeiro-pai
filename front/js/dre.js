document.addEventListener('DOMContentLoaded', () => {
    const startDate = getQueryParam('start');
    const endDate = getQueryParam('end');

    if (!startDate || !endDate) {
        alert("Período não especificado.");
        window.location.href = 'relatorios.html';
        return;
    }

    const displayEl = document.getElementById('periodoDisplay');
    if (displayEl) {
        displayEl.textContent = `Período: ${formatDateBR(startDate)} a ${formatDateBR(endDate)}`;
    }

    const apiUrl = `${API_BASE_URL}/dre/${startDate}/${endDate}`;

    fetch(apiUrl)
        .then(response => {
            if (!response.ok) throw new Error('Erro na API');
            return response.json();
        })
        .then(data => renderizarDRE(data))
        .catch(error => {
            console.error('Erro:', error);
            document.getElementById('receitasTbody').innerHTML = '<tr><td colspan="2" class="text-danger text-center">Erro ao carregar dados.</td></tr>';
            document.getElementById('despesasTbody').innerHTML = '';
        });

    const btnExport = document.getElementById('btnExport');
    if (btnExport) {
        btnExport.addEventListener('click', function() {
            const element = document.getElementById('contentToExport');
            const buttonsDiv = document.getElementById('actionButtons');
            
            if(buttonsDiv) buttonsDiv.style.display = 'none';

            const filename = `DRE_${formatDateFile(startDate)}_a_${formatDateFile(endDate)}.pdf`;
            const opt = {
                margin: 0.5,
                filename: filename,
                image: { type: 'jpeg', quality: 0.98 },
                html2canvas: { scale: 2, useCORS: true },
                jsPDF: { unit: 'in', format: 'a4', orientation: 'portrait' }
            };

            html2pdf().set(opt).from(element).save().then(() => {
                if(buttonsDiv) buttonsDiv.style.display = 'block';
            });
        });
    }
});

function renderizarDRE(listaLancamentos) {
    const receitasTbody = document.getElementById('receitasTbody');
    const despesasTbody = document.getElementById('despesasTbody');
    
    receitasTbody.innerHTML = '';
    despesasTbody.innerHTML = '';

    let somaReceitas = 0;
    let somaDespesas = 0;

    listaLancamentos.forEach(item => {
        const valor = parseFloat(item.valor);
        const tr = document.createElement('tr');
        
        tr.innerHTML = `
            <td>${item.nome}</td>
            <td class="val-right">${formatMoney(valor)}</td>
        `;

        if (item.tipo === 'R') {
            receitasTbody.appendChild(tr);
            somaReceitas += valor;
        } else if (item.tipo === 'D') {
            despesasTbody.appendChild(tr);
            somaDespesas += valor;
        }
    });

    if (receitasTbody.children.length === 0) receitasTbody.innerHTML = '<tr><td colspan="2" class="text-muted fst-italic text-center">Nenhuma receita no período.</td></tr>';
    if (despesasTbody.children.length === 0) despesasTbody.innerHTML = '<tr><td colspan="2" class="text-muted fst-italic text-center">Nenhuma despesa no período.</td></tr>';

    document.getElementById('totalReceitas').textContent = formatMoney(somaReceitas);
    document.getElementById('totalDespesas').textContent = formatMoney(somaDespesas);

    const resultado = somaReceitas - somaDespesas; 
    const resultadoEl = document.getElementById('resultadoFinal');
    resultadoEl.textContent = formatMoney(resultado);

    resultadoEl.className = resultado >= 0 ? "h4 mb-0 fw-bold result-positive" : "h4 mb-0 fw-bold result-negative";
}