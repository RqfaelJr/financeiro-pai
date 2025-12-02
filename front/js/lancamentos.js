const statusEl = document.getElementById('status');
const tableBody = document.querySelector('#lancamentosTable tbody');

const endpoint = `${API_BASE_URL}/lancamentos/buscar`;

let allData = [];
const pageSize = 10;
let currentPage = 1;

let globalChecks = {
    ativoNegativo: false,
    passivoNegativo: false,
    receitasNegativo: false,
    despesasNegativo: false,
    comparativoErrado: false
};

function renderPagination() {
    const containerId = 'paginationContainer';
    let container = document.getElementById(containerId);
    if (!container) {
        container = document.createElement('div');
        container.id = containerId;
        container.className = 'mt-3 d-flex justify-content-center gap-2';
        document.querySelector('.table-container').appendChild(container);
    }
    container.innerHTML = '';

    const totalPages = Math.max(1, Math.ceil(allData.length / pageSize));

    const prev = document.createElement('button');
    prev.className = 'btn btn-sm btn-outline-secondary';
    prev.textContent = 'Anterior';
    prev.disabled = currentPage === 1;
    prev.onclick = () => { currentPage = Math.max(1, currentPage - 1); renderPage(); };
    container.appendChild(prev);

    for (let i = 1; i <= totalPages; i++) {
        const btn = document.createElement('button');
        btn.className = 'btn btn-sm ' + (i === currentPage ? 'btn-primary' : 'btn-outline-primary');
        btn.textContent = String(i);
        btn.onclick = () => { currentPage = i; renderPage(); };
        container.appendChild(btn);
    }

    const next = document.createElement('button');
    next.className = 'btn btn-sm btn-outline-secondary';
    next.textContent = 'Próxima';
    next.disabled = currentPage >= totalPages;
    next.onclick = () => { currentPage = Math.min(totalPages, currentPage + 1); renderPage(); };
    container.appendChild(next);
}

function renderGlobalChecks() {
    const containerId = 'globalChecks';
    let container = document.getElementById(containerId);
    if (!container) {
        container = document.createElement('div');
        container.id = containerId;
        container.className = 'mb-2';
        const tableCard = document.querySelector('.table-container');
        if (tableCard) tableCard.insertBefore(container, tableCard.firstChild.nextSibling);
    }

    const parts = [];
    if (globalChecks.ativoNegativo) parts.push('<span class="badge bg-danger me-1">Ativo Negativo</span>');
    if (globalChecks.passivoNegativo) parts.push('<span class="badge bg-danger me-1">Passivo Negativo</span>');
    if (globalChecks.receitasNegativo) parts.push('<span class="badge bg-warning text-dark me-1">Receitas Negativas</span>');
    if (globalChecks.despesasNegativo) parts.push('<span class="badge bg-warning text-dark me-1">Despesas Negativas</span>');
    if (globalChecks.comparativoErrado) parts.push('<span class="badge bg-secondary me-1">Comparativo Errado</span>');

    container.innerHTML = parts.length ? `<div class="mb-2">${parts.join(' ')}</div>` : '';
}

function renderPage() {
    const start = (currentPage - 1) * pageSize;
    const pageItems = allData.slice(start, start + pageSize);
    tableBody.innerHTML = '';

    if (!pageItems.length) {
        tableBody.innerHTML = `<tr><td colspan="6" class="text-center">Nenhum lançamento nesta página</td></tr>`;
        return;
    } 

    pageItems.forEach(item => {
        const tr = document.createElement('tr');
        const descricaoText = item.descricao ?? '';

        const flags = [];
        if (item.ativoNegativo) flags.push({ label: 'Ativo Neg.', cls: 'badge bg-danger' });
        if (item.passivoNegativo) flags.push({ label: 'Passivo Neg.', cls: 'badge bg-danger' });
        if (item.receitasNegativo) flags.push({ label: 'Receitas Neg.', cls: 'badge bg-warning text-dark' });
        if (item.despesasNegativo) flags.push({ label: 'Despesas Neg.', cls: 'badge bg-warning text-dark' });
        if (item.comparativoErrado) flags.push({ label: 'Comparativo Errado', cls: 'badge bg-secondary' });

        const flagContainer = document.createElement('div');
        flagContainer.className = 'mt-1';
        flags.forEach(f => {
            const span = document.createElement('span');
            span.className = f.cls + ' me-1';
            span.textContent = f.label;
            flagContainer.appendChild(span);
        });

        tr.innerHTML = `
            <td>${item.id ?? ''}</td>
            <td>${formatDateBR(item.data ?? item.createdAt ?? '')}</td>
            <td>${item.debito ?? ''}</td>
            <td>${item.credito ?? ''}</td>
            <td>
                <div>${descricaoText}</div>
            </td>
            <td class="text-end">${formatMoney(item.valor ?? item.amount ?? 0)}</td>
        `;

        if (flags.length > 0) {
            const hasDanger = flags.some(f => f.cls.includes('bg-danger'));
            tr.classList.add(hasDanger ? 'table-danger' : 'table-warning');

            const descricaoCell = tr.children[4];
            descricaoCell.appendChild(flagContainer);
        }

        tableBody.appendChild(tr);
    });

    renderPagination();
}


function setStatusLoading() {
    statusEl.innerHTML = `<div class="alert alert-info p-2">Carregando lançamentos...</div>`;
}

function setStatusError(message) {
    statusEl.innerHTML = `<div class="alert alert-danger p-2">${message}</div>`;
}

function clearStatus() { statusEl.innerHTML = ''; }



async function loadLancamentos() {
    setStatusLoading();
    tableBody.innerHTML = '';
    try {
        const res = await fetch(endpoint);
        if (!res.ok) throw new Error(`Erro ${res.status}`);
        const data = await res.json();
        
        if (data && typeof data === 'object' && !Array.isArray(data)) {
            allData = Array.isArray(data.lancamentos) ? data.lancamentos : [];
            globalChecks.ativoNegativo = !!data.ativoNegativo;
            globalChecks.passivoNegativo = !!data.passivoNegativo;
            globalChecks.receitasNegativo = !!data.receitasNegativo;
            globalChecks.despesasNegativo = !!data.despesasNegativo;
            globalChecks.comparativoErrado = !!data.comparativoErrado;
        } else {
            allData = Array.isArray(data) ? data : [];
            globalChecks = { ativoNegativo:false, passivoNegativo:false, receitasNegativo:false, despesasNegativo:false, comparativoErrado:false };
        }

        if (!allData.length) {
            tableBody.innerHTML = `<tr><td colspan="6" class="text-center">Nenhum lançamento encontrado</td></tr>`;
            clearStatus();
            return;
        }
        currentPage = 1;
        renderGlobalChecks();
        renderPage();
        clearStatus();
    } catch (err) {
        setStatusError('Falha ao carregar lançamentos: ' + err.message);
        tableBody.innerHTML = `<tr><td colspan="6" class="text-center text-danger">Erro ao obter dados</td></tr>`;
    }
}

document.getElementById('btnRefresh').addEventListener('click', loadLancamentos);

loadLancamentos();


const formCadastrar = document.getElementById('formCadastrarLancamento');
if (formCadastrar) {
    formCadastrar.addEventListener('submit', async function (e) {
        e.preventDefault();
        const fd = new FormData(formCadastrar);
        const payload = {
            debito: fd.get('debito'),
            credito: fd.get('credito'),
            data: fd.get('data'),
            descricao: fd.get('descricao'),
            valor: parseFloat(fd.get('valor') || 0)
        };

        try {
            setStatusLoading();
            const res = await fetch(`${API_BASE_URL}/lancamentos/criar`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });
            if (!res.ok) throw new Error(`Erro ${res.status}`);

            const modalEl = document.getElementById('cadastrarModal');
            const modal = bootstrap.Modal.getInstance(modalEl) || new bootstrap.Modal(modalEl);
            modal.hide();

            formCadastrar.reset();
            await loadLancamentos(); 
            
            statusEl.innerHTML = `<div class="alert alert-success p-2">Lançamento cadastrado com sucesso.</div>`;
            setTimeout(clearStatus, 2500);
        } catch (err) {
            setStatusError('Falha ao cadastrar lançamento: ' + err.message);
        }
    });
}

const resourceNames = ['ativos','passivos','despesas','receitas','patrimonios-liquidos'];

async function fetchResourceList(resource) {
    try {
        const res = await fetch(`${API_BASE_URL}/${resource}/buscar`);
        if (!res.ok) throw new Error(`Erro ${res.status}`);
        const data = await res.json();
        return Array.isArray(data) ? data : [];
    } catch (err) {
        console.error('Erro ao buscar', resource, err);
        return [];
    }
}

async function populateAccountSelects() {
    const debitoSelect = document.getElementById('debito');
    const creditoSelect = document.getElementById('credito');
    if (!debitoSelect || !creditoSelect) return;

    debitoSelect.querySelectorAll('option:not([value=""])').forEach(o => o.remove());
    creditoSelect.querySelectorAll('option:not([value=""])').forEach(o => o.remove());

    const promises = resourceNames.map(r => fetchResourceList(r));
    const results = await Promise.all(promises);

    const combined = [];
    results.forEach((arr, idx) => {
        const resource = resourceNames[idx];
        arr.forEach(item => {
            const id = item.id ?? item.codigo;
            if (id === undefined || id === null) return;
            const label = item.nome ?? item.descricao ?? item.tipo ?? String(id);
            combined.push({ id: String(id), label: `${label} (${resource.replace(/s$/, '')})`, resource });
        });
    });

    combined.forEach(entry => {
        const opt1 = document.createElement('option');
        opt1.value = entry.id;
        opt1.textContent = entry.label;
        debitoSelect.appendChild(opt1);

        const opt2 = document.createElement('option');
        opt2.value = entry.id;
        opt2.textContent = entry.label;
        creditoSelect.appendChild(opt2);
    });
}

const modalEl = document.getElementById('cadastrarModal');
if (modalEl) {
    modalEl.addEventListener('show.bs.modal', function () {
        populateAccountSelects();
    });
}