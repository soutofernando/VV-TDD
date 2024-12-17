import org.junit.jupiter.api.Test;

import SistemaIngressos.Ingresso;
import SistemaIngressos.LoteIngresso;
import SistemaIngressos.TipoIngresso;

import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

public class LoteIngressoTest {

    @Test
    public void testCalcularReceitaComDesconto() {
        Ingresso ingresso1 = new Ingresso(1, TipoIngresso.NORMAL, 10.0);
        ingresso1.marcarComoVendido();

        Ingresso ingresso2 = new Ingresso(2, TipoIngresso.VIP, 20.0);
        ingresso2.marcarComoVendido();

        LoteIngresso lote = new LoteIngresso(1, Arrays.asList(ingresso1, ingresso2), 0.15);
        double receita = lote.calcularReceita();

        assertEquals(25.5, receita, 0.01);
    }
}
