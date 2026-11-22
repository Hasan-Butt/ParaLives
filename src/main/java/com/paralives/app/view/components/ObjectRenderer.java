package com.paralives.app.view.components;

import com.paralives.engine.world.GameObject;
import com.paralives.engine.world.GameObjectType;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.List;

public class ObjectRenderer {
    public void render(Graphics2D graphics, List<GameObject> objects,
                       Rectangle bounds, int tileWidth, int tileHeight) {
        for (GameObject object : objects) {
            int x = bounds.x + object.getTileX() * tileWidth;
            int y = bounds.y + object.getTileY() * tileHeight;

            switch (object.getType()) {
                case KEY -> drawKey(graphics, x, y, tileWidth, tileHeight,
                        object.isCollected());
                case DOOR -> drawDoor(graphics, x, y, tileWidth, tileHeight,
                        object.isActive());
                case LEVER -> drawLever(graphics, x, y, tileWidth, tileHeight,
                        object.isActive());
                case VALVE -> drawValve(graphics, x, y, tileWidth, tileHeight,
                        object.isActive());
                case BOX -> drawBox(graphics, x, y, tileWidth, tileHeight);
            }
        }
    }

    private void drawKey(Graphics2D graphics, int x, int y,
                         int width, int height, boolean collected) {
        if (collected) {
            return;
        }
        int insetX = width / 4;
        int insetY = height / 4;
        graphics.setColor(new Color(255, 205, 40));
        graphics.fillOval(x + insetX, y + insetY, width / 3, height / 3);
        graphics.setColor(new Color(130, 95, 20));
        graphics.setStroke(new BasicStroke(Math.max(2, width / 12)));
        graphics.drawLine(
                x + width / 2,
                y + height / 2,
                x + width - insetX,
                y + height - insetY);
    }

    private void drawDoor(Graphics2D graphics, int x, int y,
                          int width, int height, boolean closed) {
        graphics.setColor(closed
                ? new Color(120, 70, 45)
                : new Color(190, 150, 100));
        graphics.fillRect(
                x + width / 6,
                y + height / 10,
                width * 2 / 3,
                height * 8 / 10);

        if (closed) {
            graphics.setColor(new Color(240, 190, 50));
            graphics.fillOval(
                    x + width * 2 / 3,
                    y + height / 2,
                    Math.max(4, width / 8),
                    Math.max(4, height / 8));
        }
    }

    private void drawLever(Graphics2D graphics, int x, int y,
                           int width, int height, boolean active) {
        graphics.setColor(new Color(90, 95, 105));
        graphics.fillRect(
                x + width / 3,
                y + height * 2 / 3,
                width / 3,
                height / 3);

        graphics.setStroke(new BasicStroke(Math.max(3, width / 10)));
        graphics.setColor(active
                ? new Color(70, 220, 100)
                : new Color(220, 70, 70));
        int handleX = active ? x + width * 2 / 3 : x + width / 3;
        graphics.drawLine(
                x + width / 2,
                y + height * 2 / 3,
                handleX,
                y + height / 3);
    }

    private void drawValve(Graphics2D graphics, int x, int y,
                           int width, int height, boolean active) {
        graphics.setColor(active
                ? new Color(70, 190, 220)
                : new Color(190, 210, 215));
        int inset = Math.max(3, width / 5);
        graphics.fillOval(
                x + inset,
                y + inset,
                width - inset * 2,
                height - inset * 2);

        graphics.setColor(new Color(45, 70, 80));
        graphics.setStroke(new BasicStroke(Math.max(2, width / 12)));
        int centerX = x + width / 2;
        int centerY = y + height / 2;
        graphics.drawLine(centerX - width / 3, centerY, centerX + width / 3, centerY);
        graphics.drawLine(centerX, centerY - height / 3, centerX, centerY + height / 3);
    }

    private void drawBox(Graphics2D graphics, int x, int y,
                         int width, int height) {
        graphics.setColor(new Color(150, 105, 55));
        graphics.fillRect(
                x + width / 8,
                y + height / 8,
                width * 3 / 4,
                height * 3 / 4);

        graphics.setColor(new Color(95, 65, 35));
        graphics.setStroke(new BasicStroke(2));
        graphics.drawRect(
                x + width / 8,
                y + height / 8,
                width * 3 / 4,
                height * 3 / 4);
        graphics.drawLine(
                x + width / 4,
                y + height / 4,
                x + width * 3 / 4,
                y + height * 3 / 4);
    }
}
