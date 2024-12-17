package processadorContas.processador;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Fatura {
    private LocalDate data;
    private double valorTotal;
    private String nomeCliente;
    private String status;
    private List<Pagamento> pagamentos;

    public Fatura(LocalDate data, double valorTotal, String nomeCliente) {
        if (data == null) {
            throw new IllegalArgumentException("Data não pode ser nula.");
        }
        if (valorTotal < 0) {
            throw new IllegalArgumentException("Valor total não pode ser negativo.");
        }
        if (nomeCliente == null || nomeCliente.isEmpty()) {
            throw new IllegalArgumentException("Nome do cliente não pode ser nulo ou vazio.");
        }

        this.data = data;
        this.valorTotal = valorTotal;
        this.nomeCliente = nomeCliente;
        this.pagamentos = new ArrayList<>();
        this.status = "PENDENTE"; // Inicialmente, o status da fatura é PENDENTE.
    }

    public void adicionarPagamento(Pagamento pagamento) {
        this.pagamentos.add(pagamento);
    }

    public double calcularTotalPagamentosValidos() {
        return pagamentos.stream().mapToDouble(Pagamento::getValorPago).sum();
    }

    public void atualizarStatus() {
        if (calcularTotalPagamentosValidos() >= valorTotal) {
            this.status = "PAGA";
        } else {
            this.status = "PENDENTE";
        }
    }

    public LocalDate getData() {
        return data;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public String getStatus() {
        return status;
    }
}