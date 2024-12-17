PROJETO V&V TDD 

Alunos: 
- Fernando Souto Lima - 121110368
- Victor alexandre cavalcanti macedo - 120210046



Melhorias processador de contas

Críticas e Sugestões de Melhoria
1. Falta de Validação nos Construtores
Problema:
Os construtores das classes Conta, Fatura e Pagamento não realizam validações nos parâmetros recebidos, o que pode permitir a criação de objetos inválidos.

Sugestão:
Adicione verificações nos construtores para garantir que os dados estejam corretos.

Exemplo de melhoria em Conta:

```
public Conta(String codigo, LocalDate data, double valorPago, String tipoPagamento) {
    if (codigo == null || codigo.isEmpty()) throw new IllegalArgumentException("Código inválido.");
    if (data == null) throw new IllegalArgumentException("Data não pode ser nula.");
    if (valorPago < 0) throw new IllegalArgumentException("Valor pago não pode ser negativo.");
    if (tipoPagamento == null || tipoPagamento.isEmpty()) throw new IllegalArgumentException("Tipo de pagamento inválido.");
    
    this.codigo = codigo;
    this.data = data;
    this.valorPago = valorPago;
    this.tipoPagamento = tipoPagamento;
}
```

2. Testes Incompletos
Problema:
Alguns casos de teste não verificam situações-limite ou valores inválidos. Por exemplo:

Pagamentos com valores negativos são aceitos nos testes.
Falta de testes para null em atributos obrigatórios.
Sugestão:
Adicione testes para cobrir os casos-limite e entradas inválidas.

Exemplos de Testes Adicionais:
ContaTest.java:

```
@Test
void testContaComCodigoInvalido() {
    assertThrows(IllegalArgumentException.class, () -> new Conta("", LocalDate.now(), 500.00, "BOLETO"));
}

@Test
void testContaComValorNegativo() {
    assertThrows(IllegalArgumentException.class, () -> new Conta("001", LocalDate.now(), -100.00, "BOLETO"));
}
FaturaTest.java:
java
Copiar código
@Test
void testAdicionarPagamentoComValorNegativo() {
    assertThrows(IllegalArgumentException.class, () -> new Pagamento(-500.00, LocalDate.now(), "BOLETO"));
}
```

3. Classe ProcessadorDeContas Monolítica
Problema:
A lógica dentro de ProcessadorDeContas está centralizada em um único método (processarContas), o que torna o código menos modular e mais difícil de testar individualmente.

Sugestão:
Refatore o método para dividir as responsabilidades em métodos menores e privados.

Exemplo de Refatoração:
```
public static void processarContas(Fatura fatura, List<Conta> contas) {
    for (Conta conta : contas) {
        if (isPagamentoValido(conta, fatura.getData())) {
            double valorCorrigido = calcularValorCorrigido(conta, fatura.getData());
            fatura.adicionarPagamento(new Pagamento(valorCorrigido, conta.getData(), conta.getTipoPagamento()));
        }
    }
    fatura.atualizarStatus();
}

private static boolean isPagamentoValido(Conta conta, LocalDate dataFatura) {
    double valorPago = conta.getValorPago();
    return switch (conta.getTipoPagamento()) {
        case "BOLETO" -> valorPago >= 0.01 && valorPago <= 5000.00;
        case "CARTAO_CREDITO" -> ChronoUnit.DAYS.between(conta.getData(), dataFatura) >= 15;
        default -> !conta.getData().isAfter(dataFatura);
    };
}

private static double calcularValorCorrigido(Conta conta, LocalDate dataFatura) {
    if (conta.getTipoPagamento().equals("BOLETO") && conta.getData().isAfter(dataFatura)) {
        return conta.getValorPago() * 1.1;
    }
    return conta.getValorPago();
}
```

4. Ausência de Testes de Cobertura de Casos Específicos
Problema:
Os testes em ProcessadorDeContasTest não verificam situações como:

Nenhuma conta fornecida.
Contas com valores fora do limite permitido.
Fatura com valores menores que zero.
Sugestão:
Adicione testes para esses casos.

Exemplos de Testes:
```
@Test
void testProcessarContasSemContas() {
    ProcessadorDeContas.processarContas(fatura, List.of());
    assertEquals("PENDENTE", fatura.getStatus());
}

@Test
void testProcessarContasComValoresInvalidos() {
    Conta contaInvalida = new Conta("001", LocalDate.of(2023, 2, 20), 10000.00, "BOLETO");
    ProcessadorDeContas.processarContas(fatura, Arrays.asList(contaInvalida));
    assertEquals("PENDENTE", fatura.getStatus());
}

@Test
void testFaturaComValorNegativo() {
    assertThrows(IllegalArgumentException.class, () -> new Fatura(LocalDate.now(), -1500.00, "Cliente Teste"));
}
```

5. Testes com Nomes mais Descritivos
Problema:
Os nomes dos testes são genéricos e não expressam completamente a intenção do caso de teste.

Sugestão:
Renomeie os métodos de teste para deixá-los mais claros.

Exemplos:
testProcessarContasPagamentoBoletoEmDia → testProcessarContas_DeveMarcarFaturaComoPaga_QuandoBoletosSaoValidos
testPagamentoComValorNegativo → testPagamento_DeveLancarExcecao_QuandoValorNegativo
6. Cobertura de Código
Problema:
É necessário garantir uma cobertura de código próxima a 100%, incluindo todos os fluxos condicionais.

Sugestão:
Use ferramentas como JaCoCo para medir a cobertura de testes e identificar trechos não testados.

7. Classe Pagamento com Melhor Encapsulamento
Problema:
A classe Pagamento expõe getters para todos os atributos sem necessidade.

Sugestão:
Apenas exponha métodos realmente necessários e mantenha a classe mais encapsulada.

