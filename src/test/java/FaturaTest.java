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
    void testPagamentoAtrasadoNaoPagaFatura() {
        fatura.adicionarPagamento(new Pagamento(1000.00, LocalDate.of(2023, 2, 22), "BOLETO"));
        fatura.atualizarStatus();
        assertEquals("PENDENTE", fatura.getStatus());
    }

    @Test
    void testFaturaSemPagamentos() {
        fatura.atualizarStatus();
        assertEquals("PENDENTE", fatura.getStatus());
    }
}
