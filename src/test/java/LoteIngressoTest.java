import org.junit.jupiter.api.Test;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

public class LoteIngressoTest {

    @Test
    public void testDescontoIgnoradoParaMeiaEntrada() {
        Ingresso normal = new Ingresso(1, TipoIngresso.NORMAL, 10.0);
        Ingresso meia = new Ingresso(2, TipoIngresso.MEIA_ENTRADA, 5.0);
        normal.marcarComoVendido();
        meia.marcarComoVendido();

        LoteIngresso lote = new LoteIngresso(1, Arrays.asList(normal, meia), 0.2);
        assertEquals(8.0, lote.calcularReceita(), 0.01);
    }

    @Test
    public void testReceitaComIngressosNaoVendidos() {
        Ingresso vendido = new Ingresso(1, TipoIngresso.NORMAL, 10.0);
        Ingresso naoVendido = new Ingresso(2, TipoIngresso.NORMAL, 10.0);
        vendido.marcarComoVendido();

        LoteIngresso lote = new LoteIngresso(1, Arrays.asList(vendido, naoVendido), 0.1);
        assertEquals(9.0, lote.calcularReceita(), 0.01);
    }

    @Test
    public void testDescontoMaximoInvalido() {
        assertThrows(IllegalArgumentException.class, () -> new LoteIngresso(1, Arrays.asList(), 0.3));
    }
}
