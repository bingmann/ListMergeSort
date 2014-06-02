/**
 * Diese Datei enthaelt Klassen um Ihr Sortier-Ergebnis zu pruefen und die
 * Laufzeit zu messen.
 */

import java.text.NumberFormat;
import java.util.Arrays;

/**
 * Ein einfacher Checker, der das Array mit dem Sortierer der Java SDK sortiert
 * und dann prueft ob das Ergebnis gleich ist.
 */
class SortChecker
{
    // array of boxed integer type
    Integer[] sorted;

    SortChecker(int[] array, OurCompare cmp) {
        sorted = new Integer[array.length];
        for (int i = 0; i < array.length; ++i)
            sorted[i] = array[i];
        Arrays.sort(sorted, cmp);
    }

    boolean check(int[] array) {
        if (array.length != sorted.length)
            return false;
        for (int i = 0; i < array.length; ++i) {
            if (array[i] != sorted[i])
                return false;
        }
        return true;
    }
}

/**
 * Eine Klasse um einen Sortier-Test laufen zu lassen und die Ergebnisse
 * abzuspeichern.
 */
class SortTestRunner
{
    /** Das Ergebnis war sortiert (oder eben nicht) */
    protected boolean mSorted;

    /** Die gemessene Anzahl von Vergleichen */
    protected int mComparisons;

    /** Die gemessene Laufzeit */
    protected double mRuntime;

    SortTestRunner(SortInterface sorter, FillInterface filler, int[] constArray, SortChecker checker, OurCompare cmpN) {

        int[] array = constArray.clone(); // duplicate array for sorting

        NumberFormat fmt = NumberFormat.getIntegerInstance();

        OurCompare cmp = cmpN.newInstance(); // new resetted comparison counter

        System.out.println("--------------------------------------------------------------------------------");
        System.out.print("Sorting " + filler.getClass().getName() + " array size " + fmt.format(array.length));
        System.out.println(" using " + sorter.getClass().getName() + " with " + cmp.getClass().getName());

        long startTime = System.currentTimeMillis();
        sorter.sort(array, cmp);
        long endTime = System.currentTimeMillis();

        endTime -= startTime;
        mRuntime = endTime;

        System.out.println("Finished after " + endTime / 1000.0 + " seconds");

        if ((mSorted = checker.check(array))) {
            System.out.println("checking order: ok");
        }
        else {
            System.out.println("checking order: FAILED!");
            if (array.length <= 1024) {
                System.out.println("input: " + Arrays.toString(constArray));
                System.out.println("output: " + Arrays.toString(array));
            }
        }

        mComparisons = cmp.get();
        System.out.println("sorting required " + fmt.format(cmp.get()) + " comparisons.");
        System.gc();
    }

    /** Teste nur eine bestimmte Kombination */
    static SortTestRunner runSingle(SortInterface sorter, int size, FillInterface filler, OurCompare cmp) {

        int[] array = new int[size];
        filler.fill(array);

        SortChecker checker = new SortChecker(array, cmp.newInstance());

        return new SortTestRunner(sorter, filler, array, checker, cmp.newInstance());
    }

    /** War das Ergebnis sortiert? */
    boolean getSorted() {
        return mSorted;
    }

    /** Liefert die gemessene Anzahl von Vergleichen zurueck */
    int getComparisons() {
        return mComparisons;
    }

    /** Liefert die gemessene Laufzeit zurueck */
    double getRuntime() {
        return mRuntime;
    }
}
