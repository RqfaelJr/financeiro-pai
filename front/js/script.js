let tipoAtual = null;
const categoriasPorTipoAtivo = {
  "DISPONIBILIDADE": ["DINHEIRO_CAIXA", "CONTA_CORRENTE", "CONTA_POUPANCA", "OUTROS"],
  "INVESTIMENTOS": ["TESOURO_DIRETO", "FUNDOS_INVESTIMENTO", "ACOES", "OUTROS"],
  "DIREITOS": ["IMOVEIS", "VEICULOS", "ELETRONICOS", "ASSINATURAS", "OUTROS"]
};

const categoriasPorTipoPassivo = {
  "CURTO_PRAZO": ["CARTAO_CREDITO", "EMPRESTIMO", "BOLETOS", "OUTROS"],
  "LONGO_PRAZO": ["FINANCIAMENTO_IMOBILIARIO", "FINANCIAMENTO_VEICULO", "OUTROS"]
};

const tokenToLabel = {
  'DINHEIRO': 'Dinheiro',
  'CAIXA': 'Caixa',
  'CONTA': 'Conta',
  'CORRENTE': 'Corrente',
  'POUPANCA': 'Poupança',
  'TESOURO': 'Tesouro',
  'DIRETO': 'Direto',
  'FUNDOS': 'Fundos',
  'INVESTIMENTO': 'Investimento',
  'INVESTIMENTOS': 'Investimentos',
  'ACOES': 'Ações',
  'IMOVEIS': 'Imóveis',
  'VEICULOS': 'Veículos',
  'ELETRONICOS': 'Eletrônicos',
  'ASSINATURAS': 'Assinaturas',
  'OUTROS': 'Outros',
  'FINANCEIRO': 'Financeiro',
  'TRIBUTARIO': 'Tributário',
  'CUSTO': 'Custo',
  'FIXO': 'Fixo',
  'VARIAVEL': 'Variável',
  'CUSTO_FIXO': 'Custo Fixo',
  'CUSTO_VARIAVEL': 'Custo Variável',
  'OPERACIONAL': 'Operacional',
  'NAO': 'Não',
  'NAO_OPERACIONAL': 'Não Operacional',
  'DISPONIBILIDADE': 'Disponibilidade',
  'DIREITOS': 'Direitos',
  'CARTAO_CREDITO': 'Cartão Crédito',
  'EMPRESTIMO': 'Empréstimo',
  'BOLETOS/FATURAS': 'Boletos/Faturas',
  'FINANCIAMENTO_IMOBILIARIO': 'Financiamento Imobiliário',
  'FINANCIAMENTO_VEICULO': 'Financiamento Veículo'
};

function prettifyLabel(key) {
  if (!key) return '';
  if (tokenToLabel[key]) return tokenToLabel[key];

  const parts = key.split('_');
  const words = parts.map(part => {
    const up = part.toUpperCase();
    if (tokenToLabel[up]) return tokenToLabel[up];
    const lower = up.toLowerCase();
    return lower.charAt(0).toUpperCase() + lower.slice(1);
  });
  return words.join(' ');
}

function filtrarCategoriasAtivo(tipoSelecionado) {
  const select = document.getElementById('CategoriaAtivo');
  if (!select) return;

  while (select.options.length > 1) select.remove(1);

  if (!tipoSelecionado) {
    select.disabled = true;
    return;
  }

  const permitidas = categoriasPorTipoAtivo[tipoSelecionado] || [];
  permitidas.forEach(val => {
    const opt = document.createElement('option');
    opt.value = val;
    opt.textContent = prettifyLabel(val);
    select.appendChild(opt);
  });
  select.disabled = false;
}

function filtrarCategoriasPassivo(tipoSelecionado) {
  const select = document.getElementById('CategoriaPassivo');
  if (!select) return;

  while (select.options.length > 1) select.remove(1);

  if (!tipoSelecionado) {
    select.disabled = true;
    return;
  }

  const permitidas = categoriasPorTipoPassivo[tipoSelecionado] || [];
  permitidas.forEach(val => {
    const opt = document.createElement('option');
    opt.value = val;
    opt.textContent = prettifyLabel(val);
    select.appendChild(opt);
  });
  select.disabled = false;
}

function abrirFormulario(tipo) {
  tipoAtual = tipo;
  const containers = [
    'formContainerAtivo',
    'formContainerPassivo',
    'formContainerDespesa',
    'formContainerReceita'
  ];
  containers.forEach(id => {
    const el = document.getElementById(id);
    if (el) el.style.display = 'none';
  });

  switch (tipoAtual && tipoAtual.toLowerCase()) {
    case 'ativos':
      if (document.getElementById('formContainerAtivo')) document.getElementById('formContainerAtivo').style.display = 'block';
      break;
    case 'passivos':
      if (document.getElementById('formContainerPassivo')) document.getElementById('formContainerPassivo').style.display = 'block';
      break;
    case 'despesas':
      if (document.getElementById('formContainerDespesa')) document.getElementById('formContainerDespesa').style.display = 'block';
      break;
    case 'receitas':
      if (document.getElementById('formContainerReceita')) document.getElementById('formContainerReceita').style.display = 'block';
      break;
    default:
      console.warn(`Tipo desconhecido ao abrir formulário: ${tipo}`);
      tipoAtual = null;
      alert('Tipo desconhecido. Selecione um tipo válido.');
      return;
  }

  const tipoCap = tipo.charAt(0).toUpperCase() + tipo.slice(1);
  const tituloEl = document.getElementById(`formTitulo${tipoCap}`);
  if (tituloEl) tituloEl.textContent = `Cadastrar ${tipoCap}`;
  const dadosEl = document.getElementById('dados');
  if (dadosEl) dadosEl.textContent = '';

  if (tipoAtual && tipoAtual.toLowerCase() === 'ativos') {
    const tipoSelect = document.getElementById('TipoAtivo');
    if (tipoSelect) {
      tipoSelect.onchange = function () {
        filtrarCategoriasAtivo(tipoSelect.value);
      };
      filtrarCategoriasAtivo(tipoSelect.value);
    }
  }

  if (tipoAtual && tipoAtual.toLowerCase() === 'passivos') {
    const tipoPassivoSelect = document.getElementById('TipoPassivo');
    if (tipoPassivoSelect) {
      tipoPassivoSelect.onchange = function () {
        filtrarCategoriasPassivo(tipoPassivoSelect.value);
      };
      filtrarCategoriasPassivo(tipoPassivoSelect.value);
    }
  }
}

function fecharFormulario() {
  const containers = ['formContainerAtivo','formContainerPassivo','formContainerDespesa','formContainerReceita'];
  containers.forEach(id => { const el = document.getElementById(id); if (el) el.style.display = 'none'; });
  tipoAtual = null;
}

document.addEventListener('submit', async function (event) {
  event.preventDefault();

  const form = event.target;
  if (!form || form.tagName !== 'FORM') return;
  if (!tipoAtual) {
    const formId = form.id ? form.id.toLowerCase() : '';
    if (formId.includes('ativo')) tipoAtual = 'ativos';
    else if (formId.includes('passivo')) tipoAtual = 'passivos';
    else if (formId.includes('despesa')) tipoAtual = 'despesas';
    else if (formId.includes('receita')) tipoAtual = 'receitas';
  }

  

  const dadosDiv = document.getElementById('dados');
  const baseUrl = `${API_BASE_URL}/${tipoAtual}/criar`;

  try {
    let payload = {};
    const formData = new FormData(form);

    switch (tipoAtual.toLowerCase()) {
      case 'ativos': {
        const tipoAtivo = formData.get('tipo') || '';
        const categoria = formData.get('categoria') || '';
        const nome = formData.get('nome') || '';
        payload = { tipo: tipoAtivo, categoria: categoria, nome: nome};
        break;
      }
      case 'passivos': {
        const tipoPassivo = formData.get('tipo') || '';
        const categoriaPassivo = formData.get('categoria') || '';
        const nomePassivo = formData.get('nome') || '';
        payload = { tipo: tipoPassivo, categoria: categoriaPassivo, nome: nomePassivo };
        break;
      }
      case 'despesas': {
        const tipoDespesa = formData.get('tipo') || '';
        const nome = formData.get('descricao') || '';
        payload = { tipo: tipoDespesa, nome: nome};
        break;
      }
      case 'receitas': {
        const tipoReceita = formData.get('tipo') || '';
        const nomeR = formData.get('descricao') || '';
        payload = { tipo: tipoReceita, nome: nomeR};
        break;
      }
      default:
        throw new Error(`Tipo inválido: ${tipoAtual}`);
    }

    const response = await fetch(baseUrl, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    });

    if (!response.ok) {
      throw new Error(`Erro ${response.status}: falha ao cadastrar ${tipoAtual}`);
    }

    const data = await response.json();
    if (dadosDiv) dadosDiv.textContent = JSON.stringify(data, null, 2);

    const containers = ['formContainerAtivo','formContainerPassivo','formContainerDespesa','formContainerReceita'];
    containers.forEach(id => { const el = document.getElementById(id); if (el) el.style.display = 'none'; });
    tipoAtual = null;
  } catch (erro) {
    if (dadosDiv) dadosDiv.textContent = `❌ Erro: ${erro.message}`;
  }
});