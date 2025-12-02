document.addEventListener('DOMContentLoaded', function () {
    
    const tipoSel = document.getElementById('tipoConta');
    const contaSel = document.getElementById('contaSelect');
    
    if (tipoSel && contaSel) {
        tipoSel.addEventListener('change', function () {
            const tipo = this.value;
            contaSel.innerHTML = '<option value="">Carregando...</option>';
            contaSel.disabled = true;

            fetch(`${API_BASE_URL}/${tipo}/buscar`) 
                .then(r => r.json())
                .then(contas => {
                    contaSel.innerHTML = '<option value="" selected disabled>Selecione a conta</option>';
                    contas.forEach(c => {
                        const opt = document.createElement('option');
                        opt.value = c.id; 
                        opt.textContent = c.nome;
                        opt.dataset.nome = c.nome; 
                        contaSel.appendChild(opt);
                    });
                    contaSel.disabled = false;
                })
                .catch(err => {
                    console.error(err);
                    contaSel.innerHTML = '<option value="">Erro ao carregar</option>';
                });
        });
    }

    setupFormRedirect('razaoForm', 'razaoError', (formData) => {
        const contaId = document.getElementById('contaSelect').value;
        const contaOption = document.getElementById('contaSelect').options[document.getElementById('contaSelect').selectedIndex];
        const contaNome = contaOption ? contaOption.dataset.nome : '';
        const tipoConta = document.getElementById('tipoConta').value; 

        return `razao.html?id=${contaId}&contaNome=${encodeURIComponent(contaNome)}&type=${tipoConta}&start=${formData.start}&end=${formData.end}`;
    });

    setupFormRedirect('dreForm', 'dreError', (formData) => {
        return `dre.html?start=${formData.start}&end=${formData.end}`;
    }, 'startDateDRE', 'endDateDRE');

    setupFormRedirect('balanceteForm', 'balanceteError', (formData) => {
        return `balancete.html?start=${formData.start}&end=${formData.end}`;
    }, 'startDateBalancete', 'endDateBalancete');

    setupFormRedirect('periodoForm', 'periodoError', (formData) => {
        return `balancopatrimonial.html?start=${formData.start}&end=${formData.end}`;
    }, 'startDate', 'endDate');

});

function setupFormRedirect(formId, errorId, urlBuilderFn, startInputId = 'startDateRazao', endInputId = 'endDateRazao') {
    const form = document.getElementById(formId);
    if (!form) return;

    form.addEventListener('submit', function(e) {
        e.preventDefault();
        const errorEl = document.getElementById(errorId);
        if(errorEl) errorEl.textContent = '';

        const start = document.getElementById(startInputId)?.value || document.getElementById('startDateRazao')?.value;
        const end = document.getElementById(endInputId)?.value || document.getElementById('endDateRazao')?.value;

        if (!start || !end) {
            if(errorEl) errorEl.textContent = "Preencha o período completo.";
            return;
        }
        if (start > end) {
            if(errorEl) errorEl.textContent = "Data início maior que data fim.";
            return;
        }

        const modalEl = form.closest('.modal');
        if (modalEl) {
            const modalInstance = bootstrap.Modal.getInstance(modalEl);
            if(modalInstance) modalInstance.hide();
        }

        window.location.href = urlBuilderFn({ start, end });
    });
}