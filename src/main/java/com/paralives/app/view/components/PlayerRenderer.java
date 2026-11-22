package com.paralives.app.view.components;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class PlayerRenderer {
    private static final int SPRITE_WIDTH = 32;
    private static final int SPRITE_HEIGHT = 44;

    public void render(Graphics2D graphics, int centerX, int centerY,
                       boolean active, boolean playerA) {
        Graphics2D spriteGraphics = (Graphics2D) graphics.create();
        if (!active) {
            spriteGraphics.setComposite(
                    AlphaComposite.SrcOver.derive(0.38f));
        }

        int left = centerX - SPRITE_WIDTH / 2;
        int top = centerY - SPRITE_HEIGHT / 2;

        spriteGraphics.setColor(playerA
                ? new Color(235, 75, 75)
                : new Color(65, 125, 225));
        spriteGraphics.fillRect(left + 6, top + 18, SPRITE_WIDTH - 12, 20);

        spriteGraphics.setColor(new Color(195, 145, 110));
        spriteGraphics.fillOval(left + 8, top + 2, SPRITE_WIDTH - 16, 17);

        spriteGraphics.setColor(new Color(55, 45, 40));
        spriteGraphics.fillRect(left + 8, top + 2, SPRITE_WIDTH - 16, 5);

        spriteGraphics.setColor(playerA
                ? new Color(255, 205, 210)
                : new Color(205, 225, 255));
        spriteGraphics.fillRect(left + 10, top + 22, 4, 10);
        spriteGraphics.fillRect(left + SPRITE_WIDTH - 14, top + 22, 4, 10);

        spriteGraphics.setColor(new Color(45, 55, 65));
        spriteGraphics.fillRect(left + 8, top + 38, 7, 6);
        spriteGraphics.fillRect(left + SPRITE_WIDTH - 15, top + 38, 7, 6);

        spriteGraphics.setColor(active ? Color.BLACK : new Color(70, 70, 70));
        spriteGraphics.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        spriteGraphics.drawString(
                "Alex",
                centerX - 13,
                top - 5);

        spriteGraphics.dispose();
    }
}
