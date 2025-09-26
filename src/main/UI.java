package main;

import Entity.Entity;
import Objects.OBJ_KEY;
import Objects.OBJ_LIFE;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class UI {
    GamePanel gp;
    Graphics2D g2;
    Font dragonSlayer;
    BufferedImage fullheart, halfheart, emptyheart;
    BufferedImage keyImage;
    private boolean messageOn = false;
    public BufferedImage scrollImage;
    public BufferedImage gameOverImage;
    public String playerName = "";
    public int classCommandNum = 0;
    public String[] classes = {"Warrior", "Wizard"};
    public String message = "";
    int messageCounter = 0;
    public boolean gameOver = false;
    public String currentDialogue = "";
    public int commandNum = 0;
    BufferedImage titleBackgroundImage;

    // Tile-based constants
    private final int SCROLL_MIN_HEIGHT_TILES = 6;
    private final int SCROLL_MAX_WIDTH_TILES = 16;
    private final int TEXT_PADDING_TILES = 1;

    public UI(GamePanel gp) {
        this.gp = gp;
        try {
            scrollImage = ImageIO.read(getClass().getResourceAsStream("/images/scroll4.png"));
            gameOverImage = ImageIO.read(getClass().getResourceAsStream("/images/GameOver.png"));
        } catch (IOException e) {
            System.out.println("Could not load scroll image: " + e.getMessage());
        }
        try (InputStream is = getClass().getResourceAsStream("/Font/DragonSlayer.ttf")) {
            dragonSlayer = Font.createFont(Font.TRUETYPE_FONT, is);
            dragonSlayer = dragonSlayer.deriveFont(Font.PLAIN, 48f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        OBJ_KEY key = new OBJ_KEY(gp);
        keyImage = key.image;

        // Load the title background image
        try {
            titleBackgroundImage = ImageIO.read(getClass().getResourceAsStream("/images/title_background.png"));
        } catch (IOException e) {
            System.out.println("Could not load title background image: " + e.getMessage());
            titleBackgroundImage = null;
        } catch (Exception e) {
            System.out.println("Error loading image: " + e.getMessage());
            titleBackgroundImage = null;
        }

        // Create HUD Object
//        SuperObject heart = new OBJ_LIFE(gp);
//        fullheart = heart.image;
//        halfheart = heart.image2;
//        emptyheart = heart.image3;
         Entity heart=new OBJ_LIFE(gp);
        fullheart=heart.image;
        halfheart = heart.image2;
        emptyheart = heart.image3;

    }

    public void showMessage(String text) {
        message = text;
        messageOn = true;
    }

    public void triggerGameOver() {
        gameOver = true;
        gp.gameState = gp.gameOverState;
    }

    public void drawScrollWindow(int x, int y, int width, int height) {
        if (scrollImage != null) {
            // For tile-based games, simple scaling often works better
            g2.drawImage(scrollImage, x, y, width, height, null);
        } else {
            // Fallback: draw a stylized scroll without images
            Color parchment = new Color(245, 230, 200);
            Color darkBrown = new Color(120, 80, 40);

            // Main scroll body
            g2.setColor(parchment);
            g2.fillRoundRect(x, y, width, height, 10, 10);

            // Scroll border (thinner for tile scale)
            g2.setColor(darkBrown);
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(x, y, width, height, 10, 10);

            // Scroll ends (smaller for tile scale)
            g2.fillRect(x - gp.tileSize/2, y + gp.tileSize/2, gp.tileSize/2, height - gp.tileSize);
            g2.fillRect(x + width, y + gp.tileSize/2, gp.tileSize/2, height - gp.tileSize);
        }
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;

        g2.setFont(dragonSlayer);
        g2.setColor(Color.white);

        if (gp.gameState == gp.titleState) {
            drawTitleScreen();
        }
        if (gp.gameState == gp.playState) {
            drawPlayerLife();
            currentDialogue = "";
        }
        if (gp.gameState == gp.pauseState) {
            drawPlayerLife();
            drawPauseScreen();
        }
        if (gp.gameState == gp.dialogueState) {
            drawPlayerLife();
            drawDialogueScreen();
        }
        if (gp.gameState == gp.nameState) {
            drawNameEntryScreen();
        }
        if (gp.gameState == gp.classState) {
            drawClassSelectionScreen();
        }
        if (gp.gameState == gp.gameOverState) {
            drawGameOverScreen();
        }
    }

    public void drawGameOverScreen() {
        // Draw game over image to fit the entire screen (no black background)
        if (gameOverImage != null) {
            // Scale image to fit the entire screen
            g2.drawImage(gameOverImage, 0, 0, gp.screenWidth, gp.screenHeight, null);

            // Draw text directly on top of the image with outline for readability
            g2.setFont(dragonSlayer.deriveFont(Font.BOLD, 36F));

            String question = "Start a new game?";
            int textX = getXforCenteredText(question);
            int textY = gp.screenHeight - 100;

            // Draw text outline for better visibility
            g2.setColor(Color.BLACK);
            g2.drawString(question, textX - 1, textY - 1);
            g2.drawString(question, textX + 1, textY - 1);
            g2.drawString(question, textX - 1, textY + 1);
            g2.drawString(question, textX + 1, textY + 1);

            // Draw main text
            g2.setColor(Color.WHITE);
            g2.drawString(question, textX, textY);

            // Draw options
            g2.setFont(dragonSlayer.deriveFont(Font.PLAIN, 32F));

            String[] options = {"New Game", "Quit"};
            for (int i = 0; i < options.length; i++) {
                String option = options[i];
                int optionX = getXforCenteredText(option);
                int optionY = gp.screenHeight - 50 + (i * 35);

                // Draw outline for options too
                g2.setColor(Color.BLACK);
                if (i == commandNum) {
                    String selectedOption = "> " + option + " <";
                    g2.drawString(selectedOption, optionX - 26, optionY - 1);
                    g2.drawString(selectedOption, optionX - 24, optionY - 1);
                    g2.drawString(selectedOption, optionX - 26, optionY + 1);
                    g2.drawString(selectedOption, optionX - 24, optionY + 1);
                } else {
                    g2.drawString(option, optionX - 1, optionY - 1);
                    g2.drawString(option, optionX + 1, optionY - 1);
                    g2.drawString(option, optionX - 1, optionY + 1);
                    g2.drawString(option, optionX + 1, optionY + 1);
                }

                if (i == commandNum) {
                    g2.setColor(new Color(255, 215, 0)); // Gold for selected
                    g2.drawString("> " + option + " <", optionX - 25, optionY);
                } else {
                    g2.setColor(Color.WHITE);
                    g2.drawString(option, optionX, optionY);
                }
            }
        } else {
            // Fallback without image - just use black background
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

            g2.setFont(dragonSlayer.deriveFont(Font.BOLD, 72F));
            g2.setColor(Color.RED);
            String text = "GAME OVER";
            int x = getXforCenteredText(text);
            int y = gp.screenHeight / 3;
            g2.drawString(text, x, y);

            g2.setFont(dragonSlayer.deriveFont(Font.BOLD, 32F));
            g2.setColor(Color.WHITE);
            String question = "Do you want to start a new game or quit?";
            x = getXforCenteredText(question);
            y = gp.screenHeight / 2;
            g2.drawString(question, x, y);

            g2.setFont(dragonSlayer.deriveFont(Font.PLAIN, 28F));
            String[] options = {"New Game", "Quit"};
            for (int i = 0; i < options.length; i++) {
                String option = options[i];
                x = getXforCenteredText(option);
                y = gp.screenHeight / 2 + 50 + (i * 40);

                if (i == commandNum) {
                    g2.setColor(new Color(255, 215, 0));
                    g2.drawString("> " + option + " <", x - 20, y);
                } else {
                    g2.setColor(Color.WHITE);
                    g2.drawString(option, x, y);
                }
            }
        }
    }
    public void drawDialogueScreen() {
        if (currentDialogue == null || currentDialogue.isEmpty()) return;

        // Calculate text dimensions with larger font (28)
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 28F));
        FontMetrics fm = g2.getFontMetrics();

        // Calculate maximum width for the scroll in tiles, then convert to pixels
        int maxScrollWidthPixels = SCROLL_MAX_WIDTH_TILES * gp.tileSize;

        // Get wrapped lines with padding (convert tile padding to pixels)
        int textAreaWidth = maxScrollWidthPixels - (TEXT_PADDING_TILES * gp.tileSize * 4);
        List<String> lines = getWrappedLines(currentDialogue, textAreaWidth);

        // Calculate required height based on text
        int lineHeight = fm.getHeight() + 4;
        int textHeight = lines.size() * lineHeight;

        // Calculate scroll height in tiles, then convert to pixels
        int minScrollHeightPixels = SCROLL_MIN_HEIGHT_TILES * gp.tileSize;
        int contentHeight = textHeight + (TEXT_PADDING_TILES * gp.tileSize * 4);
        int scrollHeightPixels = Math.max(contentHeight, minScrollHeightPixels);

        // Position the scroll at the TOP center of the screen
        int scrollWidthPixels = maxScrollWidthPixels;
        int x = (gp.screenWidth - scrollWidthPixels) / 2;
        int y = - gp.tileSize/4; // Position at top of screen

        // Draw the scroll
        drawScrollWindow(x, y, scrollWidthPixels, scrollHeightPixels);

        // Draw the text (position based on tile padding)
        g2.setColor(new Color(60, 40, 20));
        int textY = y + (TEXT_PADDING_TILES * gp.tileSize * 2) + fm.getAscent();
        for (String line : lines) {
            int textX = x + (scrollWidthPixels - fm.stringWidth(line)) / 2;
            g2.drawString(line, textX, textY);
            textY += lineHeight;
        }

        // Draw "Press Enter to continue" at the bottom if needed
        if (lines.size() > 3) {
            String continueText = "Press Enter to continue...";
            g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 16F));
            g2.setColor(new Color(100, 70, 40));
            int continueX = x + (scrollWidthPixels - g2.getFontMetrics().stringWidth(continueText)) / 2;
            int continueY = y + scrollHeightPixels - (TEXT_PADDING_TILES * gp.tileSize);
            g2.drawString(continueText, continueX, continueY);
        }
    }

    public void drawPlayerLife() {
        // Position life indicator at BOTTOM LEFT corner
        int x = gp.tileSize / 2;
        int y = gp.screenHeight - (gp.tileSize * 2); // Position near bottom
        int i = 0;

        // Empty heart
        while (i < gp.player.maxLife / 2) {
            g2.drawImage(emptyheart, x, y, null);
            i++;
            x += gp.tileSize;
        }

        x = gp.tileSize / 2;
        i = 0;

        // Draw current life
        while (i < gp.player.currentLife) {
            g2.drawImage(halfheart, x, y, null);
            i++;
            if (i < gp.player.currentLife) {
                g2.drawImage(fullheart, x, y, null);
            }
            i++;
            x += gp.tileSize;
        }
    }

    public void drawTitleScreen() {
        // Draw the background image scaled to fit the screen
        if (titleBackgroundImage != null) {
            g2.drawImage(titleBackgroundImage, 0, 0, gp.screenWidth, gp.screenHeight, null);
        } else {
            // Fallback to a dark gradient background
            for (int i = 0; i < gp.screenHeight; i++) {
                float ratio = (float) i / gp.screenHeight;
                int blue = (int) (10 + ratio * 40);
                int green = (int) (5 + ratio * 20);
                g2.setColor(new Color(0, green, blue));
                g2.drawLine(0, i, gp.screenWidth, i);
            }

            // Add retro scanlines effect
            g2.setColor(new Color(0, 0, 0, 30));
            for (int i = 0; i < gp.screenHeight; i += 3) {
                g2.drawLine(0, i, gp.screenWidth, i);
            }
        }

        // Title text
        g2.setFont(dragonSlayer);
        g2.setColor(new Color(212, 175, 55));
        String text = "";
        int x = getXforCenteredText(text);
        int y = gp.tileSize * 3;
        g2.drawString(text, x, y);

        // Menu options
        g2.setFont(dragonSlayer);
        y = gp.screenHeight / 2 + gp.tileSize * 2;

        text = "NEW GAME";
        x = getXforCenteredText(text);
        g2.drawString(text, x, y);
        if (commandNum == 0) {
            g2.drawString(">", x - gp.tileSize, y);
        }

        text = "LOAD GAME";
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if (commandNum == 1) {
            g2.drawString(">", x - gp.tileSize, y);
        }

        text = "QUIT";
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if (commandNum == 2) {
            g2.drawString(">", x - gp.tileSize, y);
        }

        // Footer instructions
        g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 18F));
        g2.setColor(new Color(150, 150, 150));
        String footer = "Press ENTER to select";
        x = getXforCenteredText(footer);
        y = gp.screenHeight - gp.tileSize / 2;
        g2.drawString(footer, x, y);
    }

    public void drawNameEntryScreen() {
        // Draw the same background as title screen
        if (titleBackgroundImage != null) {
            g2.drawImage(titleBackgroundImage, 0, 0, gp.screenWidth, gp.screenHeight, null);
        } else {
            // Fallback background (same as title screen)
            for (int i = 0; i < gp.screenHeight; i++) {
                float ratio = (float) i / gp.screenHeight;
                int blue = (int) (10 + ratio * 40);
                int green = (int) (5 + ratio * 20);
                g2.setColor(new Color(0, green, blue));
                g2.drawLine(0, i, gp.screenWidth, i);
            }

            // Add retro scanlines effect
            g2.setColor(new Color(0, 0, 0, 30));
            for (int i = 0; i < gp.screenHeight; i += 3) {
                g2.drawLine(0, i, gp.screenWidth, i);
            }
        }

        // Use smaller font size for "Enter Your Name"
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 28F));
        g2.setColor(new Color(212, 175, 55));

        String text = "Enter Your Name:";
        int x = getXforCenteredText(text);
        int y = gp.screenHeight / 2 + gp.tileSize;
        g2.drawString(text, x, y);

        // Show current name being typed
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 28F));
        x = getXforCenteredText(playerName + ">");
        y += gp.tileSize * 1.5;
        g2.drawString(">"+playerName, x, y);

        // Add instructions - keep at bottom of screen
        g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 18F));
        g2.setColor(new Color(150, 150, 150));
        String instructions = "Press ENTER when done";
        x = getXforCenteredText(instructions);
        y = gp.screenHeight - gp.tileSize;
        g2.drawString(instructions, x, y);
    }

    public void drawClassSelectionScreen() {
        // Draw the same background as title screen
        if (titleBackgroundImage != null) {
            g2.drawImage(titleBackgroundImage, 0, 0, gp.screenWidth, gp.screenHeight, null);
        } else {
            // Fallback background (same as title screen)
            for (int i = 0; i < gp.screenHeight; i++) {
                float ratio = (float) i / gp.screenHeight;
                int blue = (int) (10 + ratio * 40);
                int green = (int) (5 + ratio * 20);
                g2.setColor(new Color(0, green, blue));
                g2.drawLine(0, i, gp.screenWidth, i);
            }

            // Add retro scanlines effect
            g2.setColor(new Color(0, 0, 0, 30));
            for (int i = 0; i < gp.screenHeight; i += 3) {
                g2.drawLine(0, i, gp.screenWidth, i);
            }
        }

        // Use smaller font size for "Choose Your Class"
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32F));
        g2.setColor(new Color(212, 175, 55));

        String text = "Choose Your Class";
        int x = getXforCenteredText(text);
        int y = gp.screenHeight / 2 + gp.tileSize;
        g2.drawString(text, x, y);

        // List classes - smaller font and closer spacing
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 28F));
        for (int i = 0; i < classes.length; i++) {
            text = classes[i];
            x = getXforCenteredText(text);
            y += gp.tileSize;

            g2.drawString(text, x, y);
            if (i == classCommandNum) {
                g2.drawString(">", x - gp.tileSize, y);
            }
        }

        // Add instructions - keep at bottom of screen
        g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 18F));
        g2.setColor(new Color(150, 150, 150));
        String instructions = "Use W/S to select, ENTER to confirm";
        x = getXforCenteredText(instructions);
        y = gp.screenHeight - gp.tileSize;
        g2.drawString(instructions, x, y);
    }

    public void drawPauseScreen() {
        String text = "Paused";
        int x = getXforCenteredText(text);
        int y = gp.screenHeight / 2;
        g2.drawString(text, x, y);
    }

    public void drawSubWindow(int x, int y, int width, int height) {
        Color c = new Color(0, 0, 0, 200);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);
        c = new Color(255, 255, 255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }

    public int getXforCenteredText(String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth / 2 - length / 2;
        return x;
    }

    // Helper method to wrap text
    private List<String> getWrappedLines(String text, int maxWidth) {
        List<String> lines = new ArrayList<>();
        FontMetrics fm = g2.getFontMetrics();

        // If text is empty, return empty list
        if (text == null || text.isEmpty()) {
            return lines;
        }

        for (String paragraph : text.split("\n")) {
            StringBuilder line = new StringBuilder();
            for (String word : paragraph.split(" ")) {
                // Test if adding this word would exceed the width
                String testLine = line.length() == 0 ? word : line + " " + word;
                if (fm.stringWidth(testLine) > maxWidth) {
                    if (line.length() == 0) {
                        // Word is too long to fit, split it
                        for (char c : word.toCharArray()) {
                            String testChar = line.toString() + c;
                            if (fm.stringWidth(testChar) > maxWidth) {
                                lines.add(line.toString());
                                line = new StringBuilder(String.valueOf(c));
                            } else {
                                line.append(c);
                            }
                        }
                    } else {
                        // Add the current line and start a new one
                        lines.add(line.toString());
                        line = new StringBuilder(word);
                    }
                } else {
                    if (line.length() > 0) line.append(" ");
                    line.append(word);
                }
            }
            if (line.length() > 0) {
                lines.add(line.toString());
            }
        }
        return lines;
    }
}