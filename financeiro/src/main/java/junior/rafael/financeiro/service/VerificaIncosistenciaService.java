package junior.rafael.financeiro.service;

import junior.rafael.financeiro.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VerificaIncosistenciaService {


    private final AtivoRepository ativoRepository;
    private final PassivoRepository passivoRepository;
    private final ReceitaRepository receitaRepository;
    private final DespesaRepository despesaRepository;
    private final LancamentoRepository lancamentoRepository;
    private final PatrimonioLiquidoRepository patrimonioLiquidoRepository;

    public boolean verificaAtivoNegativo() {
        List<Long> ids = ativoRepository.findAllIds();
        BigDecimal valorDebito = lancamentoRepository.findSomaValorByIdInLancamentosDebito(ids);
        BigDecimal valorCredito = lancamentoRepository.findSomaValorByIdInLancamentosCredito(ids);

        return valorCredito.compareTo(valorDebito) > 0;
    }

    public boolean verificaPassivoNegativo() {
        List<Long> ids = passivoRepository.findAllIds();
        BigDecimal valorDebito = lancamentoRepository.findSomaValorByIdInLancamentosDebito(ids);
        BigDecimal valorCredito = lancamentoRepository.findSomaValorByIdInLancamentosCredito(ids);

        return valorDebito.compareTo(valorCredito) > 0;
    }

    public boolean verificaReceitaNegativa() {
        List<Long> ids = receitaRepository.findAllIds();
        BigDecimal valorDebito = lancamentoRepository.findSomaValorByIdInLancamentosDebito(ids);
        BigDecimal valorCredito = lancamentoRepository.findSomaValorByIdInLancamentosCredito(ids);

        return valorDebito.compareTo(valorCredito) > 0;
    }

    public boolean verificaDespesaNegativa() {
        List<Long> ids = despesaRepository.findAllIds();
        BigDecimal valorDebito = lancamentoRepository.findSomaValorByIdInLancamentosDebito(ids);
        BigDecimal valorCredito = lancamentoRepository.findSomaValorByIdInLancamentosCredito(ids);

        return valorCredito.compareTo(valorDebito) > 0;
    }

    public boolean verificaPassivoXPatrimonioLiquido() {
        List<Long> passivoIds = passivoRepository.findAllIds();
        BigDecimal valorDebitoPassivo = lancamentoRepository.findSomaValorByIdInLancamentosDebito(passivoIds);
        BigDecimal valorCreditoPassivo = lancamentoRepository.findSomaValorByIdInLancamentosCredito(passivoIds);
        BigDecimal saldoPassivo = valorCreditoPassivo.subtract(valorDebitoPassivo);

        List<Long> ativoIds = ativoRepository.findAllIds();
        BigDecimal valorDebitoAtivo = lancamentoRepository.findSomaValorByIdInLancamentosDebito(ativoIds);
        BigDecimal valorCreditoAtivo = lancamentoRepository.findSomaValorByIdInLancamentosCredito(ativoIds);
        BigDecimal saldoAtivo = valorDebitoAtivo.subtract(valorCreditoAtivo);

        List<Long> despesaIds = despesaRepository.findAllIds();
        BigDecimal valorDebitoDespesa = lancamentoRepository.findSomaValorByIdInLancamentosDebito(despesaIds);
        BigDecimal valorCreditoDespesa = lancamentoRepository.findSomaValorByIdInLancamentosCredito(despesaIds);
        BigDecimal saldoDespesa = valorDebitoDespesa.subtract(valorCreditoDespesa);

        List<Long> receitaIds = receitaRepository.findAllIds();
        BigDecimal valorCreditoReceita = lancamentoRepository.findSomaValorByIdInLancamentosCredito(receitaIds);
        BigDecimal valorDebitoReceita = lancamentoRepository.findSomaValorByIdInLancamentosDebito(receitaIds);
        BigDecimal saldoReceita =  valorCreditoReceita.subtract(valorDebitoReceita);

        List<Long> plIds = patrimonioLiquidoRepository.findAllIds();
        BigDecimal valorCreditoPL = lancamentoRepository.findSomaValorByIdInLancamentosCredito(plIds);
        BigDecimal valorDebitoPL = lancamentoRepository.findSomaValorByIdInLancamentosDebito(plIds);
        BigDecimal saldoPL =  valorCreditoPL.subtract(valorDebitoPL);

        return saldoAtivo.compareTo(saldoPassivo.add(saldoReceita.add(saldoPL)).subtract(saldoDespesa)) != 0;

        // SaldoAtivo = (SaldoPassivo + SaldoPL + SaldoReceita - SaldoDespesa)
    }
}

