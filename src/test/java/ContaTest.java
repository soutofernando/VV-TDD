import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import processadorContas.processador.Conta;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

class ContaTest {

    private Conta conta;

    @BeforeEach
    void setUp() {
        conta = new Conta("001", LocalDate.of(2023, 2, 20), 500.00, "BOLETO");
    }

    @Test
    void testGetCodigo() {
        assertEquals("001", conta.getCodigo());
    }

    @Test
    void testGetData() {
        assertEquals(LocalDate.of(2023, 2, 20), conta.getData());
    }

    @Test
    void testGetValorPago() {
        assertEquals(500.00, conta.getValorPago());
    }

    @Test
    void testGetTipoPagamento() {
        assertEquals("BOLETO", conta.getTipoPagamento());
    }

    @Test
    void testContaComValorZero() {
        Conta contaInvalida = new Conta("002", LocalDate.of(2023, 2, 20), 0.00, "BOLETO");
        assertEquals(0.00, contaInvalida.getValorPago());
    }
}
