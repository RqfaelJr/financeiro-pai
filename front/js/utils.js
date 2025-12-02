
function formatMoney(value) {
    if (value === null || value === undefined) return 'R$ 0,00';
    const num = typeof value === 'string' ? parseFloat(value) : value;
    return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(num || 0);
}

function formatDateBR(dataString) {
    if (!dataString) return '';
    const dataLimpa = dataString.split('T')[0];
    const partes = dataLimpa.split('-');
    if (partes.length < 3) return dataString; 
    return `${partes[2]}/${partes[1]}/${partes[0]}`;
}

function formatDateFile(dataString) {
    if (!dataString) return '';
    const dataLimpa = dataString.split('T')[0];
    const partes = dataLimpa.split('-');
    return `${partes[2]}-${partes[1]}-${partes[0]}`;
}

function getQueryParam(name) {
    const url = new URL(window.location.href);
    return url.searchParams.get(name);
}

function parseNumber(value) {
    if (value === null || value === undefined) return 0;
    if (typeof value === 'number') return value;
    let s = String(value).trim();
    if (s.indexOf('.') !== -1 && s.indexOf(',') !== -1) {
        s = s.replace(/\./g, '').replace(/,/g, '.');
    } else {
        const parts = s.split('.');
        if (parts.length > 1 && parts[parts.length-1].length === 3) {
            s = s.replace(/\./g, '');
        }
        s = s.replace(/,/g, '.');
    }
    const n = parseFloat(s);
    return isNaN(n) ? 0 : n;
}

function escapeHtml(str) {
    if (str === null || str === undefined) return '';
    return String(str)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#39;');
}