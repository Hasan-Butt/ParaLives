package com.paralives.app.view.components;

import com.paralives.engine.world.TileMap;
import com.paralives.engine.world.TileType;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class TileRenderer {
    public void render(Graphics2D graphics, TileMap tileMap, Rectangle bounds,
                       int tileWidth, int tileHeight) {
        for (int y = 0; y < tileMap.getHeight(); y++) {
            for (int x = 0; x < tileMap.getWidth(); x++) {
                int tileX = bounds.x + x * tileWidth;
                int tileY = bounds.y + y * tileHeight;
                drawTile(graphics, tileMap.getTile(x, y),
                        tileX, tileY, tileWidth, tileHeight);
            }
        }
    }

    private void drawTile(Graphics2D graphics, TileType tileType,
                          int x, int y, int width, int height) {
        switch (tileType) {
            case FLOOR -> drawFloor(graphics, x, y, width, height);
            case WALL -> drawWall(graphics, x, y, width, height);
            case WATER -> drawWater(graphics, x, y, width, height);
            case POWERED -> drawPowered(graphics, x, y, width, height);
            case VOID -> graphics.setColor(Color.DARK_GRAY);
        }
        if (tileType == TileType.VOID) {
            graphics.fillRect(x, y, width, height);
            return;
        }

        graphics.setColor(new Color(150, 155, 160, 150));
        graphics.setStroke(new BasicStroke(1));
        graphics.drawRect(x, y, width - 1, height - 1);
    }

    private void drawFloor(Graphics2D graphics, int x, int y, int width, int height) {
        graphics.setColor(new Color(220, 225, 218));
        graphics.fillRect(x, y, width, height);
    }

    private void drawWall(Graphics2D graphics, int x, int y, int width, int height) {
        graphics.setColor(new Color(88, 92, 100));
        graphics.fillRect(x, y, width, height);

        graphics.setColor(new Color(125, 130, 138));
        graphics.fillRect(x + 3, y + 3, width - 6, Math.max(3, height / 4));
    }

    private void drawWater(Graphics2D graphics, int x, int y, int width, int height) {
        graphics.setColor(new Color(70, 145, 205));
        graphics.fillRect(x, y, width, height);

        graphics.setColor(new Color(160, 210, 240));
        graphics.drawArc(x + 3, y + height / 3, width - 6, height / 3, 0, 180);
    }

    private void drawPowered(Graphics2D graphics, int x, int y, int width, int height) {
        graphics.setColor(new Color(75, 65, 155));
        graphics.fillRect(x, y, width, height);

        graphics.setColor(new Color(255, 220, 80));
        int inset = Math.max(4, width / 5);
        graphics.fillOval(x + inset, y + inset, width - inset * 2, height - inset * 2);
    }
}
