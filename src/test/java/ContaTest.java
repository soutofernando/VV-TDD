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
    void testContaComCodigoInvalido() {
        assertThrows(IllegalArgumentException.class, () -> new Conta("", LocalDate.now(), 500.00, "BOLETO"));
    }

    @Test
    void testContaComValorNegativo() {
        assertThrows(IllegalArgumentException.class, () -> new Conta("001", LocalDate.now(), -100.00, "BOLETO"));
    }

    @Test
    void testContaComDataNull() {
        assertThrows(IllegalArgumentException.class, () -> new Conta("001", null, 500.00, "BOLETO"));
    }

    @Test
    void testContaComTipoPagamentoNull() {
        assertThrows(IllegalArgumentException.class, () -> new Conta("001", LocalDate.now(), 500.00, null));
    }
}
