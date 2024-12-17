package processadorContas.processador;
import java.time.LocalDate;

public class Conta {
    private String codigo;
    private LocalDate data;
    private double valorPago;
    private String tipoPagamento;

    public Conta(String codigo, LocalDate data, double valorPago, String tipoPagamento) {
        this.codigo = codigo;
        this.data = data;
        this.valorPago = valorPago;
        this.tipoPagamento = tipoPagamento;
    }

    public String getCodigo() {
        return codigo;
    }

    public LocalDate getData() {
        return data;
    }

    public double getValorPago() {
        return valorPago;
    }

    public String getTipoPagamento() {
        return tipoPagamento;
    }
}