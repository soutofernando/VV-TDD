import org.junit.jupiter.api.Test;

import SistemaIngressos.Ingresso;
import SistemaIngressos.StatusIngresso;
import SistemaIngressos.TipoIngresso;

import static org.junit.jupiter.api.Assertions.*;

public class IngressoTest {

    @Test
    public void testMarcarIngressoComoDisponivel() {
        Ingresso ingresso = new Ingresso(1, TipoIngresso.NORMAL, 10.0);
        ingresso.marcarComoVendido();
        assertEquals(StatusIngresso.VENDIDO, ingresso.getStatus());

        ingresso.marcarComoDisponivel();
        assertEquals(StatusIngresso.DISPONIVEL, ingresso.getStatus());
    }

    @Test
    public void testPrecoDiferentesTiposIngresso() {
        Ingresso normal = new Ingresso(1, TipoIngresso.NORMAL, 10.0);
        Ingresso vip = new Ingresso(2, TipoIngresso.VIP, 20.0);
        Ingresso meia = new Ingresso(3, TipoIngresso.MEIA_ENTRADA, 5.0);

        assertEquals(10.0, normal.getPreco());
        assertEquals(20.0, vip.getPreco());
        assertEquals(5.0, meia.getPreco());
    }

    @Test
    public void testPrecoNegativoNaoPermitido() {
        assertThrows(IllegalArgumentException.class, () -> new Ingresso(1, TipoIngresso.NORMAL, -5.0));
    }
}
