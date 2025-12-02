document.addEventListener('DOMContentLoaded', function() {
    

    const start = getQueryParam('start');
    const end = getQueryParam('end');

    if (!start || !end) {
        alert("Período não informado.");
        return;
    }

    const container = document.getElementById('periodoContainer');
    if (container) {
        container.innerHTML = `<div class="alert alert-info py-2 mb-0"><span class="fw-bold">Período:</span> ${formatDateBR(start)} a ${formatDateBR(end)}</div>`;
    }

    const ativoUrl = `${API_BASE_URL}/ativos/buscar/balanco-patrimonial/${start}/${end}`;
    const passivoUrl = `${API_BASE_URL}/passivos/buscar/balanco-patrimonial/${start}/${end}`;
    const patrimonioUrl = `${API_BASE_URL}/patrimonios-liquidos/buscar/balanco-patrimonial/${start}/${end}`;

    Promise.all([
        fetch(ativoUrl).then(r => r.ok ? r.json() : []),
        fetch(passivoUrl).then(r => r.ok ? r.json() : []),
        fetch(patrimonioUrl).then(r => r.ok ? r.json() : [])
    ]).then(([ativos, passivos, patrimonios]) => {
        renderAtivos(ativos);
        renderPassivos(passivos);
        renderPatrimonio(patrimonios);
    });

    const btnExport = document.getElementById('btnExport');
    if (btnExport) {
        btnExport.addEventListener('click', function() {
            const element = document.querySelector('.container');
            const periodo = `_${formatDateFile(start)}_a_${formatDateFile(end)}`;
            
            html2pdf().set({
                margin: 0.5,
                filename: `BalancoPatrimonial${periodo}.pdf`,
                image: { type: 'jpeg', quality: 0.98 },
                html2canvas: { scale: 2, useCORS: true },
                jsPDF: { unit: 'in', format: 'a4', orientation: 'portrait' }
            }).from(element).save();
        });
    }
});

function renderAtivos(ativos) {
    const tbody = document.getElementById('ativosTbody');
    if (!tbody) return;
    tbody.innerHTML = '';
    
    let ativoSum = 0;
    
    if (!ativos || ativos.length === 0) {
        tbody.innerHTML = '<tr><td colspan="2" class="text-center text-muted">Nenhum ativo encontrado</td></tr>';
    } else {
        ativos.forEach(it => {
            const nome = escapeHtml(it.nome || it.nomeAtivo);
            const valor = parseNumber(it.valor || it.amount);
            ativoSum += valor;
            tbody.innerHTML += `<tr><td>${nome}</td><td class="val-right">${formatMoney(valor)}</td></tr>`;
        });
    }

    tbody.innerHTML += `<tr class="table-secondary fw-bold"><td>Total do Ativo</td><td class="val-right">${formatMoney(ativoSum)}</td></tr>`;
}

function renderPassivos(passivos) {
    const tbody = document.getElementById('passivosTbody');
    if (!tbody) return;
    tbody.innerHTML = '';

    let passivoSum = 0;
    
    if (!passivos || passivos.length === 0) {
        tbody.innerHTML = '<tr><td colspan="2" class="text-center text-muted">Nenhum passivo encontrado</td></tr>';
    } else {
        passivos.forEach(it => {
            const nome = escapeHtml(it.nome || it.nomePassivo);
            const valor = parseNumber(it.valor || it.amount);
            passivoSum += valor;
            tbody.innerHTML += `<tr><td>${nome}</td><td class="val-right">${formatMoney(valor)}</td></tr>`;
        });
    }

    tbody.innerHTML += `<tr class="table-secondary fw-bold"><td>Total do Passivo</td><td id="totalPassivoVal" data-val="${passivoSum}" class="val-right">${formatMoney(passivoSum)}</td></tr>`;
    updateTotalGeral();
}

function renderPatrimonio(patrimonios) {
    const tbody = document.getElementById('patrimonioTbody');
    if (!tbody) return;
    tbody.innerHTML = '';

    let patSum = 0;

    if (!patrimonios || patrimonios.length === 0) {
        tbody.innerHTML = '<tr><td colspan="2" class="text-center text-muted">Nenhum patrimônio encontrado</td></tr>';
    } else {
        patrimonios.forEach(it => {
            const nome = escapeHtml(it.nome || it.descricao);
            const valor = parseNumber(it.valor || it.amount);
            patSum += valor;
            tbody.innerHTML += `<tr><td>${nome}</td><td class="val-right">${formatMoney(valor)}</td></tr>`;
        });
    }

    document.getElementById('totalPassivoPatrimonio').dataset.patrimonio = patSum;
    updateTotalGeral();
}

function updateTotalGeral() {
    const passivoEl = document.getElementById('totalPassivoVal');
    const totalEl = document.getElementById('totalPassivoPatrimonio');
    
    if (passivoEl && totalEl) {
        const passivo = parseFloat(passivoEl.dataset.val || 0);
        const patrimonio = parseFloat(totalEl.dataset.patrimonio || 0);
        totalEl.textContent = formatMoney(passivo + patrimonio);
    }
}