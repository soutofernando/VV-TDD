package processadorContas.processador;

import java.time.LocalDate;

public class Conta {
    private String codigo;
    private LocalDate data;
    private double valorPago;
    private String tipoPagamento;

    public Conta(String codigo, LocalDate data, double valorPago, String tipoPagamento) {
        if (codigo == null || codigo.isEmpty()) {
            throw new IllegalArgumentException("Código inválido.");
        }
        if (data == null) {
            throw new IllegalArgumentException("Data não pode ser nula.");
        }
        if (valorPago < 0) {
            throw new IllegalArgumentException("Valor pago não pode ser negativo.");
        }
        if (tipoPagamento == null || tipoPagamento.isEmpty()) {
            throw new IllegalArgumentException("Tipo de pagamento inválido.");
        }

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