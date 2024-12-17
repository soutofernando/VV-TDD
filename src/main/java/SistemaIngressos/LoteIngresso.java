package SistemaIngressos;

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
        double receita = 0;
        for (Ingresso ingresso : ingressos) {
            if (ingresso.isVendido()) {
                double precoComDesconto = ingresso.getPreco() * (1 - desconto);
                receita += precoComDesconto;
            }
        }
        return receita;
    }
}
