package junior.rafael.financeiro.repository;

import junior.rafael.financeiro.domain.PatrimonioLiquido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PatrimonioLiquidoRepository extends JpaRepository<PatrimonioLiquido, Long> {
    @Query("SELECT pl.id FROM PatrimonioLiquido pl")
    List<Long> findAllIds();
}
