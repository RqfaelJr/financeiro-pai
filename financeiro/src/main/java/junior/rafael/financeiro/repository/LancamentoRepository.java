package junior.rafael.financeiro.repository;

import junior.rafael.financeiro.domain.Lancamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

    @Query("SELECT COALESCE(SUM(l.valor), 0) FROM Lancamento l WHERE l.debito.id IN :ids")
    BigDecimal findSomaValorByIdInLancamentosDebito(List<Long> ids);

    @Query("SELECT COALESCE(SUM(l.valor), 0) FROM Lancamento l WHERE l.credito.id IN :ids")
    BigDecimal findSomaValorByIdInLancamentosCredito(List<Long> ids);

    @Query("SELECT COALESCE(SUM(l.valor), 0) " +
            "FROM Lancamento l " +
            "WHERE l.debito.id = :id " +
            "AND l.data BETWEEN :startDate AND :endDate")
    BigDecimal findSomaValorByIdInLancamentosDebitoNoPeriodo(Long id, LocalDate startDate, LocalDate endDate);

    @Query("SELECT COALESCE(SUM(l.valor), 0) " +
            "FROM Lancamento l " +
            "WHERE l.credito.id = :id " +
            "AND l.data BETWEEN :startDate AND :endDate")
    BigDecimal findSomaValorByIdInLancamentosCreditoNoPeriodo(Long id, LocalDate startDate, LocalDate endDate);

    @Query("""
    SELECT l FROM Lancamento l
    WHERE (l.debito.id = :id OR l.credito.id = :id)
    AND l.data BETWEEN :startDate AND :endDate
""")
    List<Lancamento> findByIdAndDataBetween(Long id, LocalDate startDate, LocalDate endDate);

    @Query("""
    SELECT l FROM Lancamento l
    WHERE l.credito IN (SELECT r FROM Receita r)
    AND l.data BETWEEN :startDate AND :endDate
    ORDER BY l.valor DESC
""")
    List<Lancamento> findByInReceitasAndDataBetween(LocalDate startDate, LocalDate endDate);

    @Query("""
    SELECT l FROM Lancamento l
    WHERE l.debito IN (SELECT d FROM Despesa d)
    AND l.data BETWEEN :startDate AND :endDate
""")
    List<Lancamento> findByInDespesasAndDataBetween(LocalDate startDate, LocalDate endDate);

    @Query("""
    SELECT l FROM Lancamento l
    WHERE l.debito IN (SELECT a FROM Ativo a)
    AND l.data <= :endDate
""")
    List<Lancamento> findAllAtivosDebito(LocalDate endDate);

    @Query("""
    SELECT l FROM Lancamento l
    WHERE l.credito IN (SELECT a FROM Ativo a)
    AND l.data <= :endDate
""")
    List<Lancamento> findAllAtivosCredito(LocalDate endDate);

    @Query("""
    SELECT l FROM Lancamento l
    WHERE l.credito IN (SELECT p FROM Passivo p)
    AND l.data <= :endDate
""")
    List<Lancamento> findAllPassivosCredito(LocalDate endDate);

    @Query("""
    SELECT l FROM Lancamento l
    WHERE l.debito IN (SELECT p FROM Passivo p)
    AND l.data <= :endDate
""")
    List<Lancamento> findAllPassivosDebito(LocalDate endDate);

    @Query("""
    SELECT l FROM Lancamento l
    WHERE l.debito IN (SELECT d FROM Despesa d)
    AND l.data <= :endDate
""")
    List<Lancamento> findAllDespesasDebito(LocalDate endDate);

    @Query("""
    SELECT l FROM Lancamento l
    WHERE l.credito IN (SELECT d FROM Despesa d)
    AND l.data <= :endDate
""")
    List<Lancamento> findAllDespesasCredito(LocalDate endDate);

    @Query("""
    SELECT l FROM Lancamento l
    WHERE l.credito IN (SELECT r FROM Receita r)
    AND l.data <= :endDate
""")
    List<Lancamento> findAllReceitasCredito(LocalDate endDate);

    @Query("""
    SELECT l FROM Lancamento l
    WHERE l.debito IN (SELECT r FROM Receita r)
    AND l.data <= :endDate
""")
    List<Lancamento> findAllReceitasDebito(LocalDate endDate);

    @Query("""
    SELECT l FROM Lancamento l
    WHERE l.debito IN (SELECT p FROM PatrimonioLiquido p)
    AND l.data <= :endDate
""")
    List<Lancamento> findAllPLDebito(LocalDate endDate);

    @Query("""
    SELECT l FROM Lancamento l
    WHERE l.credito IN (SELECT p FROM PatrimonioLiquido p)
    AND l.data <= :endDate
""")
    List<Lancamento> findAllPLCredito(LocalDate endDate);

    @Query("""
    SELECT l FROM Lancamento l
    ORDER BY l.id DESC
""")
    List<Lancamento> findAllOrdenado();
}
