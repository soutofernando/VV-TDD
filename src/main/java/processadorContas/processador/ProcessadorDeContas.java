package processadorContas.processador;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ProcessadorDeContas {
    public static void processarContas(Fatura fatura, List<Conta> contas) {
        for (Conta conta : contas) {
            double valorPago = conta.getValorPago();
            String tipoPagamento = conta.getTipoPagamento();
            LocalDate dataPagamento = conta.getData();
            LocalDate dataFatura = fatura.getData();

            if (tipoPagamento.equals("BOLETO")) {
                if (valorPago < 0.01 || valorPago > 5000.00) continue;

                if (dataPagamento.isAfter(dataFatura)) {
                    valorPago *= 1.1;
                }
            }
            else if (tipoPagamento.equals("CARTAO_CREDITO")) {
                if (ChronoUnit.DAYS.between(dataPagamento, dataFatura) < 15) continue;
            }
            else {
                if (dataPagamento.isAfter(dataFatura)) continue;
            }

            fatura.adicionarPagamento(new Pagamento(valorPago, dataPagamento, tipoPagamento));
        }

        fatura.atualizarStatus();
    }
}
