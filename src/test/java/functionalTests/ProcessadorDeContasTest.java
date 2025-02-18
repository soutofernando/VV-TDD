package functionalTests;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.Arrays;
import processadorContas.processador.Conta;
import processadorContas.processador.Fatura;
import processadorContas.processador.ProcessadorDeContas;

import static org.junit.jupiter.api.Assertions.*;

public class ProcessadorDeContasTest {

    //  TESTES DE ANÁLISE DE VALORES LIMITES
    @Test
    public void testeValorMinimoBoletoAceito() {
        Fatura fatura = new Fatura(LocalDate.now(), 0.01, "Cliente 1");
        Conta conta = new Conta("123", LocalDate.now(), 0.01, "BOLETO");

        ProcessadorDeContas.processarContas(fatura, Arrays.asList(conta));
        assertEquals("PAGA", fatura.getStatus());
    }

    @Test
    public void testeValorMinimoBoletoNegado() {
        Fatura fatura = new Fatura(LocalDate.now(), 0.01, "Cliente 2");
        Conta conta = new Conta("124", LocalDate.now(), 0.00, "BOLETO");

        ProcessadorDeContas.processarContas(fatura, Arrays.asList(conta));
        assertEquals("PENDENTE", fatura.getStatus());
    }

    @Test
    public void testeValorAcimaDoLimiteBoleto() {
        Fatura fatura = new Fatura(LocalDate.now(), 5000.01, "Cliente 3");
        Conta conta = new Conta("125", LocalDate.now(), 5000.01, "BOLETO");

        ProcessadorDeContas.processarContas(fatura, Arrays.asList(conta));
        assertEquals("PENDENTE", fatura.getStatus());
    }

    @Test
    public void testeValorMaximoBoletoAceito() {
        Fatura fatura = new Fatura(LocalDate.now(), 5000.00, "Cliente 4");
        Conta conta = new Conta("124", LocalDate.now(), 5000.00, "BOLETO");

        ProcessadorDeContas.processarContas(fatura, Arrays.asList(conta));
        assertEquals("PAGA", fatura.getStatus());
    }

    @Test
    public void testeValorMaximoCartaoAceito() {
        Fatura fatura = new Fatura(LocalDate.now().plusDays(15), 10000, "Cliente 5");
        Conta conta = new Conta("145", LocalDate.now().plusDays(15), 10000, "CARTAO_CREDITO");

        ProcessadorDeContas.processarContas(fatura, Arrays.asList(conta));
        assertEquals("PAGA", fatura.getStatus());
    }

    @Test
    public void testeValorMinimoCartaoAceito() {
        Fatura fatura = new Fatura(LocalDate.now().plusDays(15), 0.01, "Cliente 6");
        Conta conta = new Conta("144", LocalDate.now().plusDays(15), 0.01, "CARTAO_CREDITO");

        ProcessadorDeContas.processarContas(fatura, Arrays.asList(conta));
        assertEquals("PAGA", fatura.getStatus());
    }

    // TESTES DE PARTIÇÕES DE EQUIVALÊNCIA
    @Test
    public void testeCartaoDentroDoPrazo() {
        Fatura fatura = new Fatura(LocalDate.now().plusDays(15), 1500, "Cliente 7");
        Conta conta = new Conta("125", LocalDate.now().plusDays(15), 1500, "CARTAO_CREDITO");

        ProcessadorDeContas.processarContas(fatura, Arrays.asList(conta));
        assertEquals("PAGA", fatura.getStatus());
    }

    @Test
    public void testeCartaoForaDoPrazo() {
        Fatura fatura = new Fatura(LocalDate.now().plusDays(14), 1500, "Cliente 8");
        Conta conta = new Conta("127", LocalDate.now().plusDays(14), 1500, "CARTAO_CREDITO");

        ProcessadorDeContas.processarContas(fatura, Arrays.asList(conta));
        assertEquals("PENDENTE", fatura.getStatus());
    }

    @Test
    public void testeBoletoValorMedio() {
        Fatura fatura = new Fatura(LocalDate.now(), 2500, "Cliente 9");
        Conta conta = new Conta("131", LocalDate.now(), 2500, "BOLETO");

        ProcessadorDeContas.processarContas(fatura, Arrays.asList(conta));
        assertEquals("PAGA", fatura.getStatus());
    }

    @Test
    public void testeOutroValorLimite() {
        Fatura fatura = new Fatura(LocalDate.now().minusDays(1), 500, "Cliente 10");
        Conta conta = new Conta("132", LocalDate.now().minusDays(1), 500, "OUTRO");

        ProcessadorDeContas.processarContas(fatura, Arrays.asList(conta));
        assertEquals("PAGA", fatura.getStatus());
    }

    @Test
    public void testeCartaoValorMedio() {
        Fatura fatura = new Fatura(LocalDate.now().plusDays(16), 5000, "Cliente 11");
        Conta conta = new Conta("190", LocalDate.now().plusDays(16), 5000, "CARTAO_CREDITO");

        ProcessadorDeContas.processarContas(fatura, Arrays.asList(conta));
        assertEquals("PAGA", fatura.getStatus());
    }

    // TESTES DE TABELAS DE DECISÃO
    @Test
    public void testeOutroDentroDoPrazo() {
        Fatura fatura = new Fatura(LocalDate.now(), 3000, "Cliente 12");
        Conta conta = new Conta("126", LocalDate.now(), 3000, "OUTRO");

        ProcessadorDeContas.processarContas(fatura, Arrays.asList(conta));
        assertEquals("PAGA", fatura.getStatus());
    }

    @Test
    public void testeOutroAposOPrazo() {
        Fatura fatura = new Fatura(LocalDate.now().plusDays(1), 2000, "Cliente 13");
        Conta conta = new Conta("129", LocalDate.now().plusDays(1), 2000, "OUTRO");

        ProcessadorDeContas.processarContas(fatura, Arrays.asList(conta));
        assertEquals("PENDENTE", fatura.getStatus());
    }

    @Test
    public void testeBoletoAtrasado() {
        Fatura fatura = new Fatura(LocalDate.now().plusDays(1), 1000, "Cliente 14");
        Conta conta = new Conta("139", LocalDate.now().plusDays(1), 1000, "BOLETO");

        ProcessadorDeContas.processarContas(fatura, Arrays.asList(conta));
        assertTrue(fatura.getStatus().equals("PAGA")); // Com multa
    }

    @Test
    public void testeCartaoPrazoExato() {
        Fatura fatura = new Fatura(LocalDate.now().plusDays(15), 3000, "Cliente 15");
        Conta conta = new Conta("140", LocalDate.now().plusDays(15), 3000, "CARTAO_CREDITO");

        ProcessadorDeContas.processarContas(fatura, Arrays.asList(conta));
        assertEquals("PAGA", fatura.getStatus());
    }

    @Test
    public void testeBoletoComValorZero() {
        Fatura fatura = new Fatura(LocalDate.now(), 0, "Cliente 16");
        Conta conta = new Conta("283", LocalDate.now(), 0, "BOLETO");

        ProcessadorDeContas.processarContas(fatura, Arrays.asList(conta));
        assertEquals("PENDENTE", fatura.getStatus());
    }

    @Test
    public void testeOutroComValorNegativo() {
        try {
            Conta conta = new Conta("322", LocalDate.now().minusDays(1), -400, "OUTRO");
            fail("Deveria ter lançado exceção.");
        } catch (IllegalArgumentException e) {
            assertEquals("Valor pago não pode ser negativo.", e.getMessage());
        }
    }
}
