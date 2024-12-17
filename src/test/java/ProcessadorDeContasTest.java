import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import processadorContas.processador.Conta;
import processadorContas.processador.Fatura;
import processadorContas.processador.ProcessadorDeContas;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Arrays;

class ProcessadorDeContasTest {

    private Fatura fatura;

    @BeforeEach
    void setUp() {
        fatura = new Fatura(LocalDate.of(2023, 2, 20), 1500.00, "Cliente A");
    }

    @Test
    void testProcessarContasPagamentoBoletoEmDia() {
        Conta conta1 = new Conta("001", LocalDate.of(2023, 2, 20), 500.00, "BOLETO");
        Conta conta2 = new Conta("002", LocalDate.of(2023, 2, 20), 400.00, "BOLETO");
        Conta conta3 = new Conta("003", LocalDate.of(2023, 2, 20), 600.00, "BOLETO");

        ProcessadorDeContas.processarContas(fatura, Arrays.asList(conta1, conta2, conta3));

        assertEquals("PAGA", fatura.getStatus());
    }

    @Test
    void testProcessarContasComPagamentoAtrasado() {
        Fatura fatura = new Fatura(LocalDate.of(2023, 2, 20), 500.00, "Cliente Teste");
        Conta conta1 = new Conta("001", LocalDate.of(2023, 2, 22), 500.00, "BOLETO");
        ProcessadorDeContas.processarContas(fatura, Arrays.asList(conta1));
    
        assertEquals("PAGA", fatura.getStatus());
    }

    @Test
    void testProcessarContasComPagamentoCartaoCreditoValido() {
        Conta conta1 = new Conta("001", LocalDate.of(2023, 2, 5), 500.00, "CARTAO_CREDITO");
        Conta conta2 = new Conta("002", LocalDate.of(2023, 2, 17), 1000.00, "TRANSFERENCIA");

        ProcessadorDeContas.processarContas(fatura, Arrays.asList(conta1, conta2));

        assertEquals("PAGA", fatura.getStatus());
    }

    @Test
    void testProcessarContasComPagamentoCartaoCreditoInvalido() {
        Conta conta1 = new Conta("001", LocalDate.of(2023, 2, 6), 500.00, "CARTAO_CREDITO"); // Pagamento inválido (15
                                                                                             // dias)
        Conta conta2 = new Conta("002", LocalDate.of(2023, 2, 17), 1000.00, "TRANSFERENCIA");

        ProcessadorDeContas.processarContas(fatura, Arrays.asList(conta1, conta2));

        assertEquals("PENDENTE", fatura.getStatus());
    }
}