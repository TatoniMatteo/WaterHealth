package com.tatonimatteo.waterhealth.configuration;

import android.graphics.Color;

import java.util.Random;

/**
 * Classe di utilità per la gestione dei colori, inclusi il calcolo del contrasto e la generazione di colori casuali con buon contrasto.
 */
public class ColorUtility {

    /**
     * Genera un colore casuale con un buon contrasto rispetto al colore di sfondo specificato.
     *
     * @param background Il colore di sfondo con cui generare il contrasto.
     * @return Il colore generato.
     */
    public static int generateRandomColor(int background) {
        Random random = new Random();
        int generatedColor;

        do {
            int red = random.nextInt(256);
            int green = random.nextInt(256);
            int blue = random.nextInt(256);

            generatedColor = Color.rgb(red, green, blue);
        } while (!hasGoodContrast(background, generatedColor));

        return generatedColor;
    }

    /**
     * Verifica se il contrasto tra due colori è sufficiente per l'accessibilità.
     *
     * @param color1 Il primo colore.
     * @param color2 Il secondo colore.
     * @return True se il contrasto è sufficiente, altrimenti False.
     */
    public static boolean hasGoodContrast(int color1, int color2) {
        double contrastRatio = calculateContrastRatio(color1, color2);
        return contrastRatio >= 4.5;
    }

    /**
     * Calcola il rapporto di contrasto tra due colori.
     *
     * @param color1 Il primo colore.
     * @param color2 Il secondo colore.
     * @return Il rapporto di contrasto calcolato.
     */
    public static double calculateContrastRatio(int color1, int color2) {
        double luminance1 = calculateLuminance(color1);
        double luminance2 = calculateLuminance(color2);

        return (Math.max(luminance1, luminance2) + 0.05) / (Math.min(luminance1, luminance2) + 0.05);
    }

    /**
     * Calcola la luminanza di un colore.
     *
     * @param color Il colore di cui calcolare la luminanza.
     * @return La luminanza del colore.
     */
    public static double calculateLuminance(int color) {
        double red = Color.red(color) / 255.0;
        double green = Color.green(color) / 255.0;
        double blue = Color.blue(color) / 255.0;

        red = (red <= 0.03928) ? red / 12.92 : Math.pow((red + 0.055) / 1.055, 2.4);
        green = (green <= 0.03928) ? green / 12.92 : Math.pow((green + 0.055) / 1.055, 2.4);
        blue = (blue <= 0.03928) ? blue / 12.92 : Math.pow((blue + 0.055) / 1.055, 2.4);

        return 0.2126 * red + 0.7152 * green + 0.0722 * blue;
    }
}
