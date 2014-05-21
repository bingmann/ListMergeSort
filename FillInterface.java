/**
 * Diese Datei enthaelt ein Interface fuer den Array Daten-Generator und
 * mehrere Eingaben zum Testen.
 */

import java.util.Random;

/** Daten Generator fuer das Eingabe-Array */
interface FillInterface
{
    public void fill(int[] array);
}

/** Fuelle aufsteigend, aber nur gerade Zahlen */
class FillAscending implements FillInterface
{
    public void fill(int[] array) {
        for (int i = 0; i < array.length; ++i) {
            array[i] = i * 2;
        }
    }
}

/** Fuelle mit absteigender Zahlenfolge */
class FillDescending implements FillInterface
{
    public void fill(int[] array) {
        for (int i = 0; i < array.length; ++i) {
            array[i] = array.length * 7 - i * 3;
        }
    }
}

/** Fuelle eine Saegezahn-Folge */
class FillSawtooth implements FillInterface
{
    public void fill(int[] array) {
        for (int i = 0; i < array.length; ++i) {
            array[i] = i % 4;
        }
    }
}

/** Fuelle Array mit Zufallszahlen */
class FillRandom implements FillInterface
{
    public void fill(int[] array) {
        Random rgen = new Random();
        for (int i = 0; i < array.length; ++i) {
            array[i] = rgen.nextInt(array.length);
        }
    }
}
