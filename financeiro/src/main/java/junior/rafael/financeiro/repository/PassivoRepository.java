package junior.rafael.financeiro.repository;

import junior.rafael.financeiro.domain.passivo.Passivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PassivoRepository extends JpaRepository<Passivo, Long> {
    @Query("SELECT p.id FROM Passivo p")
    List<Long> findAllIds();
}
