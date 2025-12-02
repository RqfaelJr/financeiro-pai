(function initRazao() {
  document.addEventListener('DOMContentLoaded', function () {
    
    const idConta = getQueryParam('id') || getQueryParam('contaId'); 
    const start = getQueryParam('start');
    const end = getQueryParam('end');
    const nomeContaParam = getQueryParam('contaNome');
    const tipoContaParam = getQueryParam('type');

    const emissaoEl = document.getElementById('emissaoHoje');
    const nomeContaEl = document.getElementById('nomeConta');
    const tbody = document.getElementById('transacoesTbody');
    const alertDiv = document.getElementById('alertPeriodoPlaceholder');

    if (emissaoEl) emissaoEl.textContent = new Date().toLocaleDateString('pt-BR');

    if (alertDiv && start && end) {
        alertDiv.innerHTML = `<div class="mb-3 alert alert-info py-2"><span class="fw-bold">Período Selecionado:</span> ${formatDateBR(start)} a ${formatDateBR(end)}</div>`;
    }

    if (nomeContaEl && idConta) {
        nomeContaEl.textContent = nomeContaParam ? decodeURIComponent(nomeContaParam) : `Conta ID: ${idConta}`;
    }

    if (idConta && start && end) {
        const apiUrl = `${API_BASE_URL}/razao/${idConta}/${start}/${end}`;

        fetch(apiUrl)
            .then(res => {
                if (!res.ok) throw new Error('Erro na requisição');
                return res.json();
            })
            .then(data => {
                let natureza = 'DEVEDORA';
                if (tipoContaParam) {
                    const t = tipoContaParam.toLowerCase();
                    if (t.includes('passivos') || t.includes('receitas') || t.includes('patrimonios')) {
                        natureza = 'CREDORA';
                    }
                }
                
                renderizarTabela(data, natureza);
            })
            .catch(error => {
                console.error(error);
                if(tbody) tbody.innerHTML = '<tr><td colspan="5" class="text-center text-danger">Erro ao carregar os dados.</td></tr>';
            });
    } else {
        if(tbody) tbody.innerHTML = '<tr><td colspan="5" class="text-center">Parâmetros de busca incompletos.</td></tr>';
    }

    const btnExport = document.getElementById('btnExport');
    if (btnExport) {
        btnExport.addEventListener('click', function() {
            const element = document.querySelector('.container');
            const nomeArquivo = nomeContaParam ? decodeURIComponent(nomeContaParam).replace(/\s+/g, '_') : 'Razao';
            const periodo = (start && end) ? `_${formatDateFile(start)}_a_${formatDateFile(end)}` : '';

            html2pdf().set({
                margin: 0.5,
                filename: `Razao_${nomeArquivo}${periodo}.pdf`,
                image: { type: 'jpeg', quality: 0.98 },
                html2canvas: { scale: 2, useCORS: true },
                jsPDF: { unit: 'in', format: 'a4', orientation: 'portrait' }
            }).from(element).save();
        });
    }

    function renderizarTabela(transacoes, natureza) {
        if (!tbody) return;
        tbody.innerHTML = '';

        if (!transacoes || transacoes.length === 0) {
            tbody.innerHTML = '<tr><td colspan="5" class="text-center">Nenhum lançamento encontrado.</td></tr>';
            return;
        }

        let saldoAcumulado = 0;

        transacoes.forEach((tx) => {
            const valor = parseFloat(tx.valor);

            if (tx.tipo === 'I') {
                saldoAcumulado = valor; 
                
                const tr = document.createElement('tr');
                tr.className = "table-secondary fw-bold";
                tr.innerHTML = `
                    <td></td>
                    <td>${tx.nome}</td> 
                    <td></td>
                    <td></td>
                    <td class="text-end">${formatMoney(saldoAcumulado)}</td> 
                `;
                tbody.appendChild(tr);
                return; 
            }

            if (tx.tipo === 'F') {
                const tr = document.createElement('tr');
                tr.className = "table-secondary fw-bold";
                tr.innerHTML = `
                    <td></td>
                    <td>${tx.nome}</td> 
                    <td></td>
                    <td></td>
                    <td class="text-end">${formatMoney(valor)}</td>
                `;
                tbody.appendChild(tr);
                return;
            }

            let valorDebito = 0;
            let valorCredito = 0;
            let htmlDebito = '';
            let htmlCredito = '';

            if (tx.tipo === 'D') {
                valorDebito = valor;
                htmlDebito = formatMoney(valorDebito);

                if (natureza === 'DEVEDORA') {
                    saldoAcumulado += valor; 
                } else {
                    saldoAcumulado -= valor; 
                }

            } else if (tx.tipo === 'C') {
                valorCredito = valor;
                htmlCredito = formatMoney(valorCredito);
                if (natureza === 'DEVEDORA') {
                    saldoAcumulado -= valor; 
                } else {
                    saldoAcumulado += valor; 
                }
            }

            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${formatDateBR(tx.data)}</td>
                <td>${tx.nome}</td>
                <td class="text-end text-danger">${htmlDebito}</td>
                <td class="text-end text-success">${htmlCredito}</td>
                <td class="text-end fw-bold">${formatMoney(saldoAcumulado)}</td>
            `;
            tbody.appendChild(tr);
        });
    }
  });
})();