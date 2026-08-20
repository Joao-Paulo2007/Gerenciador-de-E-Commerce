package view;

import javax.swing.*;
import java.awt.*;

/**
 * Botão que pinta sua própria cor de fundo manualmente.
 * Alguns Look and Feels do sistema (ex.: GTK no Linux) ignoram setBackground()
 * em JButton normal e só mostram a cor no hover/foco. Esta classe resolve isso
 * desenhando o retângulo de fundo diretamente, independente do L&F ativo.
 */
public class BotaoEstilizado extends JButton {

    private final Color corFundo;

    public BotaoEstilizado(String texto, Color corFundo) {
        super(texto);
        this.corFundo = corFundo;

        setForeground(Color.WHITE);
        setFont(new Font("SansSerif", Font.BOLD, 12));
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color corAtual = corFundo;
        if (getModel().isPressed()) {
            corAtual = corFundo.darker();
        } else if (getModel().isRollover()) {
            corAtual = corFundo.brighter();
        }

        g2.setColor(corAtual);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
        g2.dispose();

        super.paintComponent(g);
    }
}