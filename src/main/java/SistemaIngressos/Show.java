package SistemaIngressos;
import java.util.List;

public class Show {
    private final String artista;
    private final double cacheArtista;
    private final double despesasInfra;
    private final boolean dataEspecial;
    private final List<LoteIngresso> lotes;

    public Show(String artista, double cacheArtista, double despesasInfra, boolean dataEspecial, List<LoteIngresso> lotes) {
        this.artista = artista;
        this.cacheArtista = cacheArtista;
        this.despesasInfra = despesasInfra;
        this.dataEspecial = dataEspecial;
        this.lotes = lotes;
    }

    public double calcularCustoTotal() {
        double custo = cacheArtista + despesasInfra;
        if (dataEspecial) {
            custo += despesasInfra * 0.15; // Adicional de 15% para datas especiais
        }
        return custo;
    }

    public double calcularReceitaTotal() {
        return lotes.stream().mapToDouble(LoteIngresso::calcularReceita).sum();
    }

    public double calcularReceitaLiquida() {
        return calcularReceitaTotal() - calcularCustoTotal();
    }

    public String getStatusFinanceiro() {
        double receitaLiquida = calcularReceitaLiquida();
        if (receitaLiquida > 0) return "LUCRO";
        if (receitaLiquida == 0) return "ESTÁVEL";
        return "PREJUÍZO";
    }

    public String gerarRelatorio() {
        StringBuilder relatorio = new StringBuilder();
        relatorio.append("Relatório do Show:\n");
        relatorio.append("Artista: ").append(artista).append("\n");
        relatorio.append("Receita Total: R$ ").append(calcularReceitaTotal()).append("\n");
        relatorio.append("Custo Total: R$ ").append(calcularCustoTotal()).append("\n");
        relatorio.append("Receita Líquida: R$ ").append(calcularReceitaLiquida()).append("\n");
        relatorio.append("Status Financeiro: ").append(getStatusFinanceiro()).append("\n");
        return relatorio.toString();
    }
}
