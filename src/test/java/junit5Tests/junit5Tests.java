package junit5Tests;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import processadorContas.processador.Conta;
import processadorContas.processador.Fatura;
import processadorContas.processador.ProcessadorDeContas;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

class junit5Tests {

    @BeforeAll
    static void setupAll() {
        System.out.println("Iniciando testes de unidade");
    }

    @BeforeEach
    void setup() {
        System.out.println("Configurando ambiente de teste");
    }

    @ParameterizedTest
    @CsvSource({
        "123, 0.01, BOLETO, PAGA",      
        "124, 0.00, BOLETO, PENDENTE",   
        "125, 5000.01, BOLETO, PENDENTE",
        "126, 5000.00, BOLETO, PAGA",    
        "127, 1000.00, CARTAO_CREDITO, PENDENTE", 
        "128, 1000.00, OUTRO, PAGA"      
    })

    @DisplayName("Testando pagamentos com valores limite")
    void testPagamentosLimite(int codigoConta, String data, double valorPago, String tipoPagamento, String statusEsperado) {
        Fatura fatura = new Fatura(LocalDate.now(), valorPago, "Cliente Teste");
        Conta conta = new Conta(String.valueOf(codigoConta), LocalDate.now(), valorPago, tipoPagamento);
        ProcessadorDeContas.processarContas(fatura, List.of(conta));
        assertEquals(statusEsperado, fatura.getStatus(), "O status esperado não foi atingido");
    }

    @ParameterizedTest
    @MethodSource("dadosParaTestesDeExcecao")
    @DisplayName("Testando exceções em pagamentos inválidos")
    void testPagamentosInvalidos(String codigoConta, LocalDate data, double valorPago, String tipoPagamento) {
        assertThrows(IllegalArgumentException.class, () -> new Conta(codigoConta, data, valorPago, tipoPagamento));
    }

    static Stream<Arguments> dadosParaTestesDeExcecao() {
        return Stream.of(
            Arguments.of("322", LocalDate.now().minusDays(1), -400, "OUTRO"), 
            Arguments.of("283", LocalDate.now(), 0, "BOLETO"), 
            Arguments.of("400", null, 500, "BOLETO"), 
            Arguments.of("401", LocalDate.now(), 500, ""),
            Arguments.of("402", LocalDate.now().plusDays(1), 1000, "OUTRO"), 
            Arguments.of("403", LocalDate.now(), -1, "BOLETO") 
        );
    }


    @DisplayName("Testando regras de processamento de conta")
    void testRegrasProcessamento(String codigoConta, double valorPago, String tipoPagamento, String statusEsperado) {
        LocalDate hoje = LocalDate.now();
        Fatura fatura = new Fatura(hoje, valorPago, "Cliente Teste");
        Conta conta = new Conta(codigoConta, hoje, valorPago, tipoPagamento);
        
        ProcessadorDeContas.processarContas(fatura, List.of(conta));
        
        assertEquals(statusEsperado, fatura.getStatus(), 
            "Status incorreto para " + tipoPagamento + " no valor de " + valorPago);
    }

    @Test
    @DisplayName("Testando pagamento com boleto após a data da fatura")
    void testBoletoAposDataFatura() {
        LocalDate dataFatura = LocalDate.now();
        LocalDate dataPagamento = dataFatura.plusDays(5);
        double valorOriginal = 1000.00;
        double valorEsperado = valorOriginal * 1.1; 
        
        Fatura fatura = new Fatura(dataFatura, valorEsperado, "Cliente Boleto Atrasado");
        Conta conta = new Conta("201", dataPagamento, valorOriginal, "BOLETO");
        
        ProcessadorDeContas.processarContas(fatura, List.of(conta));
        
        assertEquals("PAGA", fatura.getStatus(), 
            "A fatura deveria ser PAGA com o valor de boleto acrescido de 10%");
    }

    @Test
    @DisplayName("Testando pagamento com cartão de crédito antes de 15 dias")
    void testCartaoAntes15Dias() {
        LocalDate dataFatura = LocalDate.now().plusDays(10);
        Fatura fatura = new Fatura(dataFatura, 2000, "Cliente Cartão Recente");
        Conta conta = new Conta("202", LocalDate.now(), 2000, "CARTAO_CREDITO");
        
        ProcessadorDeContas.processarContas(fatura, List.of(conta));
        
        assertEquals("PENDENTE", fatura.getStatus(), 
            "Pagamento com cartão antes de 15 dias da fatura deve manter status PENDENTE");
    }

    @Test
    @DisplayName("Testando pagamento com cartão de crédito após 15 dias")
    void testCartaoApos15Dias() {
        LocalDate dataPagamento = LocalDate.now();
        LocalDate dataFatura = dataPagamento.plusDays(20); 
        
        Fatura fatura = new Fatura(dataFatura, 2000, "Cliente Cartão Válido");
        Conta conta = new Conta("203", dataPagamento, 2000, "CARTAO_CREDITO");
        
        ProcessadorDeContas.processarContas(fatura, List.of(conta));
        
        assertEquals("PAGA", fatura.getStatus(), 
            "Pagamento com cartão após 15 dias da fatura deve alterar status para PAGA");
    }

    @Test
    @DisplayName("Testando pagamento de outro tipo após a data da fatura")
    void testOutroTipoAposDataFatura() {
        LocalDate dataFatura = LocalDate.now();
        LocalDate dataPagamento = dataFatura.plusDays(1);
        
        Fatura fatura = new Fatura(dataFatura, 1500, "Cliente Outro Tipo Atrasado");
        Conta conta = new Conta("204", dataPagamento, 1500, "OUTRO");
        
        ProcessadorDeContas.processarContas(fatura, List.of(conta));
        
        assertEquals("PENDENTE", fatura.getStatus(), 
            "Pagamento de outro tipo após a data da fatura deve ser ignorado");
    }

    @Test
    @DisplayName("Testando pagamento de múltiplos boletos")
    void testMultiplosBoletos() {
        LocalDate dataFatura = LocalDate.now();
        Fatura fatura = new Fatura(dataFatura, 3000, "Cliente Multi-Boleto");
        
        Conta conta1 = new Conta("205", dataFatura, 1000, "BOLETO");
        Conta conta2 = new Conta("206", dataFatura, 2000, "BOLETO");
        
        ProcessadorDeContas.processarContas(fatura, List.of(conta1, conta2));
        
        assertEquals("PAGA", fatura.getStatus(), 
            "Múltiplos boletos somando o valor da fatura devem alterar status para PAGA");
    }

    @Test
    @DisplayName("Testando fatura com pagamentos parciais")
    void testPagamentosParciais() {
        LocalDate dataFatura = LocalDate.now();
        Fatura fatura = new Fatura(dataFatura, 5000, "Cliente Pagamento Parcial");
        
        Conta conta1 = new Conta("207", dataFatura, 1000, "BOLETO");
        Conta conta2 = new Conta("208", dataFatura, 2000, "BOLETO");
        
        ProcessadorDeContas.processarContas(fatura, List.of(conta1, conta2));
        
        assertEquals("PENDENTE", fatura.getStatus(), 
            "Pagamentos parciais que não somam o valor total devem manter status PENDENTE");
    }

    @Test
    @DisplayName("Testando pagamento com diferentes tipos")
    void testDiferentesTiposPagamento() {
        LocalDate dataFatura = LocalDate.now();
        LocalDate dataPagamentoCartao = dataFatura.minusDays(20);
        
        Fatura fatura = new Fatura(dataFatura, 3000, "Cliente Tipos Diversos");
        
        Conta contaBoleto = new Conta("209", dataFatura, 1000, "BOLETO");
        Conta contaCartao = new Conta("210", dataPagamentoCartao, 2000, "CARTAO_CREDITO");
        
        ProcessadorDeContas.processarContas(fatura, List.of(contaBoleto, contaCartao));
        
        assertEquals("PAGA", fatura.getStatus(), 
            "Combinação de tipos de pagamento válidos deve alterar status para PAGA");
    }

    @Test
    @DisplayName("Testando pagamento com boleto no limite máximo")
    void testBoletoLimiteMaximo() {
        LocalDate dataFatura = LocalDate.now();
        Fatura fatura = new Fatura(dataFatura, 5000, "Cliente Boleto Limite Máximo");
        
        Conta conta = new Conta("211", dataFatura, 5000, "BOLETO");
        
        ProcessadorDeContas.processarContas(fatura, List.of(conta));
        
        assertEquals("PAGA", fatura.getStatus(), 
            "Boleto no valor máximo permitido (5000) deve ser processado corretamente");
    }

    @Test
    @DisplayName("Testando pagamento com boleto no limite mínimo")
    void testBoletoLimiteMinimo() {
        LocalDate dataFatura = LocalDate.now();
        Fatura fatura = new Fatura(dataFatura, 0.01, "Cliente Boleto Limite Mínimo");
        
        Conta conta = new Conta("212", dataFatura, 0.01, "BOLETO");
        
        ProcessadorDeContas.processarContas(fatura, List.of(conta));
        
        assertEquals("PAGA", fatura.getStatus(), 
            "Boleto no valor mínimo permitido (0.01) deve ser processado corretamente");
    }

    @Test
    @DisplayName("Testando processamento sem contas")
    void testProcessamentoSemContas() {
        LocalDate dataFatura = LocalDate.now();
        Fatura fatura = new Fatura(dataFatura, 1000, "Cliente Sem Contas");
        
        ProcessadorDeContas.processarContas(fatura, new ArrayList<>());
        
        assertEquals("PENDENTE", fatura.getStatus(), 
            "Fatura sem contas associadas deve manter status PENDENTE");
    }

    @ParameterizedTest
    @MethodSource("dadosParaTestesDeExcecao")
    @DisplayName("Testando exceções em criação de contas inválidas")
    void testCriacaoContasInvalidas(String codigo, LocalDate data, double valorPago, String tipoPagamento, String mensagemEsperada) {
        Exception exception = assertThrows(IllegalArgumentException.class, 
            () -> new Conta(codigo, data, valorPago, tipoPagamento));
        
        assertEquals(mensagemEsperada, exception.getMessage(), 
            "Mensagem de erro para criação de conta inválida está incorreta");
    }

    @AfterEach
    void tearDown() {
        System.out.println("Finalizando teste");
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("Todos os testes concluídos");
    }
}