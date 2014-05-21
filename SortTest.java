/**
 * Diese Datei enthaelt eine lange Reihe von Test Cases, die Ihnen bei der
 * Entwicklung helfen sollen.
 */

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.JUnitCore;

/**
 * Eine Implementierung des SortInterface mit Hilfe des Java SDK Sorter und
 * boxed Integer-Typen.
 */
class JavaSDKSort implements SortInterface
{
    public void sort(int[] array, OurCompare cmp) {

        Integer[] boxed = new Integer[array.length];
        for (int i = 0; i < array.length; ++i)
            boxed[i] = array[i];

        Arrays.sort(boxed, cmp);

        for (int i = 0; i < array.length; ++i)
            array[i] = boxed[i];
    }
}

/**
 * Die Development Sorting Test cases. Sie koennen in den testManyTests()
 * verschiedene Zeilen ein- und auskommentieren um mehr oder weniger
 * Kombinationen von Eingaben, Komparatoren und Inputgroessen laufen zu lassen.
 */
public class SortTest
{
    public void testManyTests(OurCompare cmp, int size, FillInterface filler) {
        int[] array = new int[size];
        filler.fill(array);

        SortChecker checker = new SortChecker(array, cmp.newInstance());

        new SortTestRunner(new JavaSDKSort(), filler, array, checker, cmp);
        new SortTestRunner(new OurQuickSort(), filler, array, checker, cmp);
        new SortTestRunner(new ListMergeSort(), filler, array, checker, cmp);
    }

    public void testManyTests(OurCompare cmp, int size) {
        testManyTests(cmp, size, new FillAscending());
        testManyTests(cmp, size, new FillDescending());
        testManyTests(cmp, size, new FillSawtooth());
        testManyTests(cmp, size, new FillRandom());
        // implementieren Sie FillWorstCase selbst:
        //testManyTests(cmp, size, new FillWorstCase());
    }

    public void testManyTests(OurCompare cmp) {
        testManyTests(cmp, 128);
        // testen Sie spaeter auch fuer groessere Eingaben:
        // testManyTests(cmp, 1024 * 1024);
        // testManyTests(cmp, 4 * 1024 * 1024);
    }

    /** Teste viele Kombinationen von Komparatoren, Eingaben, Groessen. */
    @Test
    public void testManyTests() {
        testManyTests(new CompareA());
        testManyTests(new CompareB());
        testManyTests(new CompareC());
    }

    public static void main(String[] args) throws Exception {
        // mit JUnit ausfuehren:
        JUnitCore.main(SortTest.class.getName());

        // oder direkt per "java SortTest"
        // SortTest st = new SortTest();
        // st.testManyTests();
    }
}
