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
        this.data = data;
        this.valorTotal = valorTotal;
        this.nomeCliente = nomeCliente;
        this.status = "PENDENTE";
        this.pagamentos = new ArrayList<>();
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