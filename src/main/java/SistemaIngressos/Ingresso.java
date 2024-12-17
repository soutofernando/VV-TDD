package SistemaIngressos;
public class Ingresso {
    private final int id;
    private final TipoIngresso tipo;
    private StatusIngresso status;
    private final double preco;

    public Ingresso(int id, TipoIngresso tipo, double preco) {
        if (preco < 0) {
            throw new IllegalArgumentException("O preço do ingresso não pode ser negativo");
        }
        this.id = id;
        this.tipo = tipo;
        this.preco = preco;
        this.status = StatusIngresso.DISPONIVEL;
    }

    public int getId() {
        return id;
    }

    public TipoIngresso getTipo() {
        return tipo;
    }

    public double getPreco() {
        return preco;
    }

    public StatusIngresso getStatus() {
        return status;
    }

    public void marcarComoVendido() {
        this.status = StatusIngresso.VENDIDO;
    }

    public void marcarComoDisponivel() {
        this.status = StatusIngresso.DISPONIVEL;
    }

    public boolean isVendido() {
        return this.status == StatusIngresso.VENDIDO;
    }
}
