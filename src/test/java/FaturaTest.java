import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import processadorContas.processador.Fatura;
import processadorContas.processador.Pagamento;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

class FaturaTest {

    private Fatura fatura;

    @BeforeEach
    void setUp() {
        fatura = new Fatura(LocalDate.of(2023, 2, 20), 1500.00, "Cliente A");
    }

    @Test
    void testAdicionarPagamento() {
        Pagamento pagamento = new Pagamento(500.00, LocalDate.of(2023, 2, 20), "BOLETO");
        fatura.adicionarPagamento(pagamento);
        assertEquals(500.00, fatura.calcularTotalPagamentosValidos());
    }

    @Test
    void testStatusFaturaPaga() {
        fatura.adicionarPagamento(new Pagamento(1000.00, LocalDate.of(2023, 2, 20), "BOLETO"));
        fatura.adicionarPagamento(new Pagamento(500.00, LocalDate.of(2023, 2, 20), "BOLETO"));
        fatura.atualizarStatus();
        assertEquals("PAGA", fatura.getStatus());
    }

    @Test
    void testStatusFaturaPendente() {
        fatura.adicionarPagamento(new Pagamento(700.00, LocalDate.of(2023, 2, 20), "BOLETO"));
        fatura.adicionarPagamento(new Pagamento(600.00, LocalDate.of(2023, 2, 20), "BOLETO"));
        fatura.atualizarStatus();
        assertEquals("PENDENTE", fatura.getStatus());
    }

    @Test
    void testFaturaComValorNegativo() {
        assertThrows(IllegalArgumentException.class, () -> new Fatura(LocalDate.now(), -1500.00, "Cliente Teste"));
    }

    @Test
    void testAdicionarPagamentoComValorNegativo() {
        assertThrows(IllegalArgumentException.class, () -> new Pagamento(-500.00, LocalDate.now(), "BOLETO"));
    }
}
