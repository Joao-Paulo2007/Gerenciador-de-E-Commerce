package view;

import java.awt.*;

/**
 * Layout parecido com FlowLayout, mas que calcula corretamente a altura
 * necessária quando os componentes "quebram linha" dentro de um JScrollPane.
 * Usado para exibir os cards de produto em grade responsiva.
 */
public class WrapLayout extends FlowLayout {

    public WrapLayout(int align, int hgap, int vgap) {
        super(align, hgap, vgap);
    }

    public WrapLayout() {
        super(FlowLayout.LEFT, 12, 12);
    }

    @Override
    public Dimension preferredLayoutSize(Container target) {
        return layoutSize(target, true);
    }

    @Override
    public Dimension minimumLayoutSize(Container target) {
        Dimension minimo = layoutSize(target, false);
        minimo.width -= (getHgap() + 1);
        return minimo;
    }

    private Dimension layoutSize(Container target, boolean preferred) {
        synchronized (target.getTreeLock()) {

            int larguraDisponivel = obterLarguraDisponivel(target);

            int hgap = getHgap();
            int vgap = getVgap();
            Insets insets = target.getInsets();
            int larguraMaxima = larguraDisponivel - (insets.left + insets.right + hgap * 2);

            Dimension dimensao = new Dimension(0, 0);
            int linhaLargura = 0;
            int linhaAltura = 0;

            int quantidade = target.getComponentCount();

            for (int i = 0; i < quantidade; i++) {
                Component componente = target.getComponent(i);

                if (!componente.isVisible()) {
                    continue;
                }

                Dimension tamanhoComponente = preferred ? componente.getPreferredSize() : componente.getMinimumSize();

                if (linhaLargura + tamanhoComponente.width > larguraMaxima && linhaLargura > 0) {
                    dimensao.width = Math.max(dimensao.width, linhaLargura);
                    dimensao.height += linhaAltura + vgap;
                    linhaLargura = 0;
                    linhaAltura = 0;
                }

                if (linhaLargura != 0) {
                    linhaLargura += hgap;
                }

                linhaLargura += tamanhoComponente.width;
                linhaAltura = Math.max(linhaAltura, tamanhoComponente.height);
            }

            dimensao.width = Math.max(dimensao.width, linhaLargura);
            dimensao.height += linhaAltura + vgap;

            dimensao.width += insets.left + insets.right + hgap * 2;
            dimensao.height += insets.top + insets.bottom + vgap * 2;

            return dimensao;
        }
    }

    private int obterLarguraDisponivel(Container target) {
        Container atual = target;
        while (atual != null && !(atual instanceof java.awt.Window)) {
            if (atual.getWidth() > 0) {
                return atual.getWidth();
            }
            atual = atual.getParent();
        }
        return 900;
    }
}
