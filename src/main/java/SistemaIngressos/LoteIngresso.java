import java.util.List;

public class LoteIngresso {
    private final int id;
    private final List<Ingresso> ingressos;
    private final double desconto;

    public LoteIngresso(int id, List<Ingresso> ingressos, double desconto) {
        if (desconto < 0 || desconto > 0.25) {
            throw new IllegalArgumentException("Desconto deve ser entre 0% e 25%");
        }
        this.id = id;
        this.ingressos = ingressos;
        this.desconto = desconto;
    }

    public int getId() {
        return id;
    }

    public List<Ingresso> getIngressos() {
        return ingressos;
    }

    public double getDesconto() {
        return desconto;
    }

    public double calcularReceita() {
        return ingressos.stream()
                .filter(Ingresso::isVendido)
                .filter(i -> i.getTipo() != TipoIngresso.MEIA_ENTRADA) // Desconto não se aplica a MEIA_ENTRADA
                .mapToDouble(i -> i.getPreco() * (1 - desconto))
                .sum();
    }
}
