import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import processadorContas.processador.Pagamento;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

class PagamentoTest {

    private Pagamento pagamento;

    @BeforeEach
    void setUp() {
        pagamento = new Pagamento(500.00, LocalDate.of(2023, 2, 20), "BOLETO");
    }

    @Test
    void testGetValorPago() {
        assertEquals(500.00, pagamento.getValorPago());
    }

    @Test
    void testGetData() {
        assertEquals(LocalDate.of(2023, 2, 20), pagamento.getData());
    }

    @Test
    void testGetTipoPagamento() {
        assertEquals("BOLETO", pagamento.getTipoPagamento());
    }

    @Test
    void testPagamentoComValorNegativo() {
        Pagamento pagamentoInvalido = new Pagamento(-100.00, LocalDate.of(2023, 2, 20), "BOLETO");
        assertTrue(pagamentoInvalido.getValorPago() < 0);
    }
}
