package functionalTests;

import SistemaIngressos.LoteIngresso;
import SistemaIngressos.Show;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes funcionais para o sistema de ingressos baseado nas técnicas:
 * - Análise de Valores Limite
 * - Partições de Equivalência
 * - Tabelas de Decisão
 */
class ShowFunctionalTest {

    @Test
    void testAnalisedeValoresLimites_ReceitaLiquida() {
        LoteIngresso loteMin = new LoteIngresso(1, 10.0, 0.0, 20, 10);
        Show showMin = new Show("Artista X", 0.0, 0.0, false, List.of(loteMin));
        assertEquals(30.0, showMin.calcularReceitaTotal(), 0.01);
        
        LoteIngresso loteMax = new LoteIngresso(10000, 100.0, 25.0, 20, 10);
        Show showMax = new Show("Artista Y", 100000.0, 50000.0, true, List.of(loteMax));
        assertTrue(showMax.calcularReceitaLiquida() > 0);
    }

    @Test
    void testParticoesdeEquivalencia_DistribuicaoIngressos() {
        LoteIngresso lote = new LoteIngresso(100, 10.0, 10.0, 20, 10);
        assertEquals(20, lote.getTotalIngressosVip());
        assertEquals(10, lote.getTotalIngressosMeia());
        assertEquals(70, lote.getTotalIngressosNormal());
    }

    @Test
    void testTabelasdeDecisao_CalculoPrecoIngressos() {
        LoteIngresso lote = new LoteIngresso(500, 10.0, 15.0, 20, 10);
        assertEquals(20.0, lote.getPrecoVip(), 0.01);
        assertEquals(5.0, lote.getPrecoMeia(), 0.01);
    }

    @Test
    void testValoresLimite_DescontoMaximo() {
        LoteIngresso lote = new LoteIngresso(500, 10.0, 25.0, 20, 10);
        Show show = new Show("Artista A", 1000.0, 2000.0, false, List.of(lote));
        double receitaEsperada = 500 * ((10 * 0.75) + (20 * 2 * 0.75));
        assertEquals(receitaEsperada, show.calcularReceitaTotal(), 0.01);
    }

    @Test
    void testValoresLimite_DescontoAcimaPermitido() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> 
            new LoteIngresso(500, 10.0, 30.0, 20, 10)
        );
        assertEquals("Desconto máximo permitido é 25%", exception.getMessage());
    }

    @Test
    void testShowComDataEspecial_AumentoDeCusto() {
        Show show = new Show("Banda Z", 5000.0, 20000.0, true, List.of());
        assertEquals(23000.0, show.calcularCustoTotal(), 0.01);
    }

    @Test
    void testShowSemIngressos_VendaZerada() {
        Show show = new Show("Solo Artist", 5000.0, 15000.0, false, List.of());
        assertEquals(0.0, show.calcularReceitaTotal(), 0.01);
        assertEquals(-20000.0, show.calcularReceitaLiquida(), 0.01);
    }

    @Test
    void testVendaDeTodosOsIngressos() {
        LoteIngresso lote = new LoteIngresso(500, 10.0, 10.0, 20, 10);
        Show show = new Show("Festival ABC", 5000.0, 10000.0, false, List.of(lote));
        assertTrue(show.calcularReceitaTotal() > 0);
    }

    @Test
    void testStatusFinanceiroPrejuizo() {
        Show show = new Show("Evento Y", 10000.0, 30000.0, false, List.of());
        assertEquals("PREJUÍZO", show.getStatusFinanceiro());
    }

    @Test
    void testStatusFinanceiroLucro() {
        LoteIngresso lote = new LoteIngresso(1000, 50.0, 10.0, 20, 10);
        Show show = new Show("Evento X", 20000.0, 30000.0, false, List.of(lote));
        assertEquals("LUCRO", show.getStatusFinanceiro());
    }
}
