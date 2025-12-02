(function initBalancete() {
    document.addEventListener('DOMContentLoaded', function () {
        
        const start = getQueryParam('start');
        const end = getQueryParam('end');
        const dataEmissaoEl = document.getElementById('dataEmissao');
        const periodoTextoEl = document.getElementById('periodoTexto');
        const tbody = document.getElementById('balanceteBody');
        const tfoot = document.getElementById('balanceteFooter');

        if(dataEmissaoEl) dataEmissaoEl.textContent = new Date().toLocaleDateString('pt-BR');
        
        if (periodoTextoEl) {
            if (start && end) {
                periodoTextoEl.textContent = `${formatDateBR(start)} a ${formatDateBR(end)}`;
            } else {
                periodoTextoEl.textContent = "Período não informado";
            }
        }

        const apiUrl = `${API_BASE_URL}/balancete/${start}/${end}`;

        fetch(apiUrl)
            .then(res => {
                if(!res.ok) throw new Error("Erro na requisição");
                return res.json();
            })
            .then(renderizarTabela)
            .catch(err => {
                console.error(err);
                if(tbody) tbody.innerHTML = '<tr><td colspan="7" class="text-center text-danger">Erro ao carregar dados. Verifique a API.</td></tr>';
            });

        function renderizarTabela(dados) {
            if(!tbody) return;
            tbody.innerHTML = '';
            
            let totalIniD = 0, totalIniC = 0;
            let totalMovD = 0, totalMovC = 0;
            let totalFinD = 0, totalFinC = 0;

            if (!dados || dados.length === 0) {
                tbody.innerHTML = '<tr><td colspan="7" class="text-center">Nenhum registro encontrado.</td></tr>';
                return;
            }

            dados.forEach(conta => {
                totalIniD += conta.saldoInicialDevedor || 0;
                totalIniC += conta.saldoInicialCredor || 0;
                totalMovD += conta.saldoMovimentacaoDevedor || 0;
                totalMovC += conta.saldoMovimentacaoCredor || 0;
                totalFinD += conta.saldoFinalDevedor || 0;
                totalFinC += conta.saldoFinalCredor || 0;

                const tr = document.createElement('tr');
                tr.innerHTML = `
                    <td class="fw-bold text-secondary">${conta.nome}</td>
                    <td class="val-num border-left-thick text-primary">${formatMoney(conta.saldoInicialDevedor)}</td>
                    <td class="val-num text-primary">${formatMoney(conta.saldoInicialCredor)}</td>
                    <td class="val-num border-left-thick">${formatMoney(conta.saldoMovimentacaoDevedor)}</td>
                    <td class="val-num">${formatMoney(conta.saldoMovimentacaoCredor)}</td>
                    <td class="val-num border-left-thick fw-bold">${formatMoney(conta.saldoFinalDevedor)}</td>
                    <td class="val-num fw-bold">${formatMoney(conta.saldoFinalCredor)}</td>
                `;
                tbody.appendChild(tr);
            });

            if(tfoot) {
                tfoot.innerHTML = `
                    <tr>
                        <td class="text-end">TOTAIS</td>
                        <td class="val-num border-left-thick">${formatMoney(totalIniD)}</td>
                        <td class="val-num">${formatMoney(totalIniC)}</td>
                        <td class="val-num border-left-thick">${formatMoney(totalMovD)}</td>
                        <td class="val-num">${formatMoney(totalMovC)}</td>
                        <td class="val-num border-left-thick">${formatMoney(totalFinD)}</td>
                        <td class="val-num">${formatMoney(totalFinC)}</td>
                    </tr>
                `;
            }
        }
    });
})();