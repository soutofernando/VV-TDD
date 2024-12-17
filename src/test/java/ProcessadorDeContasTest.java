import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import processadorContas.processador.Fatura;
import processadorContas.processador.Conta;
import processadorContas.processador.ProcessadorDeContas;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

class ProcessadorDeContasTest {

    private Fatura fatura;

    @BeforeEach
    void setUp() {
        fatura = new Fatura(LocalDate.of(2023, 2, 20), 1500.00, "Cliente A");
    }

    @Test
    void testProcessarContas_DeveMarcarFaturaComoPaga_QuandoBoletosSaoValidos() {
        Conta conta1 = new Conta("001", LocalDate.of(2023, 2, 20), 500.00, "BOLETO");
        Conta conta2 = new Conta("002", LocalDate.of(2023, 2, 20), 500.00, "BOLETO");
        Conta conta3 = new Conta("003", LocalDate.of(2023, 2, 20), 500.00, "BOLETO");

        ProcessadorDeContas.processarContas(fatura, Arrays.asList(conta1, conta2, conta3));

        assertEquals("PAGA", fatura.getStatus());
    }

    @Test
    void testProcessarContas_DeveMarcarFaturaComoPendente_QuandoContasNaoAtingemValor() {
        Conta conta1 = new Conta("001", LocalDate.of(2023, 2, 20), 500.00, "BOLETO");
        Conta conta2 = new Conta("002", LocalDate.of(2023, 2, 20), 400.00, "BOLETO");

        ProcessadorDeContas.processarContas(fatura, Arrays.asList(conta1, conta2));

        assertEquals("PENDENTE", fatura.getStatus());
    }

    @Test
    void testProcessarContas_DeveMarcarFaturaComoPaga_QuandoValoresValidosDeCartaoECorrigidos() {
        Conta conta1 = new Conta("001", LocalDate.of(2023, 2, 5), 700.00, "CARTAO_CREDITO");
        Conta conta2 = new Conta("002", LocalDate.of(2023, 2, 17), 800.00, "TRANSFERENCIA_BANCARIA");

        ProcessadorDeContas.processarContas(fatura, Arrays.asList(conta1, conta2));

        assertEquals("PAGA", fatura.getStatus());
    }

    @Test
    void testProcessarContas_DeveMarcarFaturaComoPendente_QuandoCartaoDeCreditoComMenosDe15Dias() {
        Conta conta1 = new Conta("001", LocalDate.of(2023, 2, 6), 700.00, "CARTAO_CREDITO");
        Conta conta2 = new Conta("002", LocalDate.of(2023, 2, 17), 800.00, "TRANSFERENCIA_BANCARIA");

        ProcessadorDeContas.processarContas(fatura, Arrays.asList(conta1, conta2));

        assertEquals("PENDENTE", fatura.getStatus());
    }

    @Test
    void testProcessarContas_DeveMarcarFaturaComoPendente_QuandoContasComValoresInvalidos() {
        Conta contaInvalida = new Conta("001", LocalDate.of(2023, 2, 20), 10000.00, "BOLETO");
        ProcessadorDeContas.processarContas(fatura, Arrays.asList(contaInvalida));

        assertEquals("PENDENTE", fatura.getStatus());
    }

    @Test
    void testProcessarContasSemContas() {
        ProcessadorDeContas.processarContas(fatura, List.of());
        assertEquals("PENDENTE", fatura.getStatus());
    }
}
