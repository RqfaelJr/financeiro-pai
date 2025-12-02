package junior.rafael.financeiro.repository;

import junior.rafael.financeiro.domain.despesa.Despesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DespesaRepository extends JpaRepository<Despesa, Long> {

    @Query("SELECT d.id FROM Despesa d")
    List<Long> findAllIds();
}
