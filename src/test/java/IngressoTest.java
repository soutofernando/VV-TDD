import org.junit.jupiter.api.Test;

import SistemaIngressos.Ingresso;
import SistemaIngressos.StatusIngresso;
import SistemaIngressos.TipoIngresso;

import static org.junit.jupiter.api.Assertions.*;

public class IngressoTest {

    @Test
    public void testMarcarIngressoComoVendido() {
        Ingresso ingresso = new Ingresso(1, TipoIngresso.NORMAL, 10.0);
        assertEquals(StatusIngresso.DISPONIVEL, ingresso.getStatus());

        ingresso.marcarComoVendido();
        assertEquals(StatusIngresso.VENDIDO, ingresso.getStatus());
    }

    @Test
    public void testPrecoIngresso() {
        Ingresso vip = new Ingresso(2, TipoIngresso.VIP, 20.0);
        assertEquals(20.0, vip.getPreco());
    }
}
