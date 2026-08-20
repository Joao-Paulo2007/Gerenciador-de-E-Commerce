package view;

import Enum.FiltrarProdutos;
import Enum.StatusDoProduto;
import Enum.StatusEntrega;

import java.awt.*;
import java.util.EnumMap;
import java.util.Map;

/**
 * Centraliza a paleta de cores usada nas abas, badges de status
 * e nos cards de produto, para manter a identidade visual da Loja Pontual.
 */
public class Cores {

    public static final Color PRIMARIA = new Color(0x1A, 0x3C, 0x6E);
    public static final Color PRIMARIA_CLARA = new Color(0x2E, 0x5D, 0x9F);
    public static final Color FUNDO = new Color(0xF4, 0xF6, 0xF9);
    public static final Color BRANCO = Color.WHITE;
    public static final Color TEXTO = new Color(0x22, 0x22, 0x22);
    public static final Color BORDA = new Color(0xDD, 0xE1, 0xE7);

    private static final Map<FiltrarProdutos, Color> CATEGORIA;
    private static final Map<StatusDoProduto, Color> STATUS;
    private static final Map<StatusEntrega, Color> ENTREGA;

    static {
        CATEGORIA = new EnumMap<>(FiltrarProdutos.class);
        CATEGORIA.put(FiltrarProdutos.Camisa, new Color(0xE7, 0x4C, 0x3C));
        CATEGORIA.put(FiltrarProdutos.Bone, new Color(0x27, 0xAE, 0x60));
        CATEGORIA.put(FiltrarProdutos.Caneca, new Color(0xE6, 0x7E, 0x22));
        CATEGORIA.put(FiltrarProdutos.Travesseiro, new Color(0x8E, 0x44, 0xAD));
        CATEGORIA.put(FiltrarProdutos.Aromatizador, new Color(0x16, 0xA0, 0x85));
        CATEGORIA.put(FiltrarProdutos.Chinelo, new Color(0xD3, 0x54, 0x00));
        CATEGORIA.put(FiltrarProdutos.JogoDeCama, new Color(0x29, 0x80, 0xB9));
        CATEGORIA.put(FiltrarProdutos.Calca, new Color(0x2C, 0x3E, 0x50));
        CATEGORIA.put(FiltrarProdutos.Moletom, new Color(0xC0, 0x39, 0x2B));

        STATUS = new EnumMap<>(StatusDoProduto.class);
        STATUS.put(StatusDoProduto.AVenda, new Color(0x27, 0xAE, 0x60));
        STATUS.put(StatusDoProduto.Vendido, new Color(0x7F, 0x8C, 0x8D));
        STATUS.put(StatusDoProduto.Pendente, new Color(0xF3, 0x9C, 0x12));

        ENTREGA = new EnumMap<>(StatusEntrega.class);
        ENTREGA.put(StatusEntrega.NaoEnviado, new Color(0xE7, 0x4C, 0x3C));
        ENTREGA.put(StatusEntrega.EmSeparacao, new Color(0xF1, 0xC4, 0x0F));
        ENTREGA.put(StatusEntrega.Enviado, new Color(0x30, 0x98, 0xDB));
        ENTREGA.put(StatusEntrega.EmTransito, new Color(0x9B, 0x59, 0xB6));
        ENTREGA.put(StatusEntrega.Entregue, new Color(0x27, 0xAE, 0x60));
        ENTREGA.put(StatusEntrega.Cancelada, new Color(0x2C, 0x3E, 0x50));
    }

    private Cores() {
    }

    public static Color daCategoria(FiltrarProdutos categoria) {
        return CATEGORIA.getOrDefault(categoria, PRIMARIA_CLARA);
    }

    public static Color doStatus(StatusDoProduto status) {
        return STATUS.getOrDefault(status, Color.GRAY);
    }

    public static Color daEntrega(StatusEntrega statusEntrega) {
        return ENTREGA.getOrDefault(statusEntrega, Color.GRAY);
    }

    public static String textoStatus(StatusDoProduto status) {
        return switch (status) {
            case AVenda -> "À Venda";
            case Vendido -> "Vendido";
            case Pendente -> "Pendente";
        };
    }

    public static String textoEntrega(StatusEntrega statusEntrega) {
        return switch (statusEntrega) {
            case NaoEnviado -> "Não Enviado";
            case EmSeparacao -> "Em Separação";
            case Enviado -> "Enviado";
            case EmTransito -> "Em Trânsito";
            case Entregue -> "Entregue";
            case Cancelada -> "Cancelada";
        };
    }

    /** Escolhe branco ou preto para o texto conforme o brilho da cor de fundo. */
    public static Color corDeTextoLegivel(Color fundo) {
        double luminancia = (0.299 * fundo.getRed() + 0.587 * fundo.getGreen() + 0.114 * fundo.getBlue()) / 255;
        return luminancia > 0.55 ? Color.BLACK : Color.WHITE;
    }
}
