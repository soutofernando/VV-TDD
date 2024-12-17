package processadorContas.processador;
import java.time.LocalDate;

public class Pagamento {
    private final double valorPago;
    private final LocalDate data;
    private final String tipoPagamento;

    public Pagamento(double valorPago, LocalDate data, String tipoPagamento) {
        if (valorPago < 0) throw new IllegalArgumentException("Valor pago não pode ser negativo.");
        if (data == null) throw new IllegalArgumentException("Data não pode ser nula.");
        if (tipoPagamento == null || tipoPagamento.isEmpty()) throw new IllegalArgumentException("Tipo de pagamento inválido.");

        this.valorPago = valorPago;
        this.data = data;
        this.tipoPagamento = tipoPagamento;
    }

    public double getValorPago() {
        return valorPago;
    }

    public LocalDate getData() {
        return data;
    }

    public String getTipoPagamento() {
        return tipoPagamento;
    }
}
