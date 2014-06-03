/**
 * Die (private) Sorting Test cases.
 */

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runners.MethodSorters;

/**
 * Ein weiterer (komplizierter) Komparator.
 */
class CompareD extends OurCompare
{
    public int cmp(int a, int b) {
        ++count;
        return (a ^ 13) - (b ^ 13);
    }

    public OurCompare newInstance() {
        return new CompareD();
    }
}

/** Fuelle aufsteigend, aber nur gerade Zahlen */
class FillAscendingOrder implements FillInterface
{
    OurCompare cmp;

    FillAscendingOrder(OurCompare _cmp) {
        cmp = _cmp;
    }

    public void fill(int[] array) {
        FillAscending filler = new FillAscending();
        filler.fill(array);

        Integer[] boxed = new Integer[array.length];
        for (int i = 0; i < array.length; ++i)
            boxed[i] = array[i];

        Arrays.sort(boxed, cmp);

        for (int i = 0; i < array.length; ++i)
            array[i] = boxed[i];
    }
}

/** Fuelle mit absteigender Zahlenfolge */
class FillDescendingOrder implements FillInterface
{
    OurCompare base_cmp;

    class ReverseCompare extends OurCompare
    {
        public OurCompare newInstance() {
            return new ReverseCompare();
        }

        public int cmp(int a, int b) {
            return -base_cmp.cmp(a, b);
        }
    }

    FillDescendingOrder(OurCompare _cmp) {
        base_cmp = _cmp;
    }

    public void fill(int[] array) {
        FillDescending filler = new FillDescending();
        filler.fill(array);

        Integer[] boxed = new Integer[array.length];
        for (int i = 0; i < array.length; ++i)
            boxed[i] = array[i];

        Arrays.sort(boxed, new ReverseCompare());

        for (int i = 0; i < array.length; ++i)
            array[i] = boxed[i];
    }
}

/** Fuelle mit worst-case Zahlenfolge */
class FillWorstCase implements FillInterface
{
    public void fill(int[] array) {
        for (int i = 0; i < array.length;) {
            array[i++] = i / 2;
            array[i++] = array.length - i / 2;
        }
    }
}

/**
 * Die private Sorting Test cases. Diese werden im Praktomat mit einem anderen
 * Komparator ausgefuehrt und geben Punkte bei Bestehen.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SortTestPrivate
{
    /** Im Praktomat wird dies geaendert sein. */
    OurCompare cmp = new CompareD();

    final int n = 2 * 1024 * 1024;
    final double logn = Math.log(n) / Math.log(2);

    @Test(timeout = 60000)
    public void test1ComparisonsAscending() {
        SortTestRunner ms = SortTestRunner.runSingle(new ListMergeSort(), n, new FillAscendingOrder(cmp), cmp);
        assertTrue(ms.getSorted());
        assertTrue(ms.getComparisons() <= 2 * n * 1.10);
    }

    @Test(timeout = 60000)
    public void test2ComparisonsDescending() {
        SortTestRunner ms = SortTestRunner.runSingle(new ListMergeSort(), n, new FillDescendingOrder(cmp), cmp);
        assertTrue(ms.getSorted());
        assertTrue(ms.getComparisons() <= 2 * n * 1.10);
    }

    @Test(timeout = 60000)
    public void test3ComparisonsSawtooth() {
        SortTestRunner ms = SortTestRunner.runSingle(new ListMergeSort(), n, new FillSawtooth(), new CompareA());
        assertTrue(ms.getSorted());
        assertTrue(ms.getComparisons() <= 8 * n * 1.10);
    }

    @Test(timeout = 60000)
    public void test4ComparisonsRandom() {
        SortTestRunner ms = SortTestRunner.runSingle(new ListMergeSort(), n, new FillRandom(), cmp);
        assertTrue(ms.getSorted());
        assertTrue(ms.getComparisons() <= n * logn * 1.10);
    }

    @Test(timeout = 60000)
    public void test5ComparisonsWorstCase() {
        SortTestRunner ms = SortTestRunner.runSingle(new ListMergeSort(), n, new FillWorstCase(), cmp);
        assertTrue(ms.getSorted());
        assertTrue(ms.getComparisons() <= 2 * n * logn * 1.10);
        assertTrue(ms.getComparisons() >= 1 * n * logn * 0.90);
    }

    void assertRuntime(double r1, double r2) {
        if (r1 > r2) {
            System.out.println("Runtime Quicksort " + r2 + " ms < ListMergeSort " + r1 + " ms -> BAD!");
        }
        else {
            System.out.println("Runtime Quicksort " + r2 + " ms >= ListMergeSort " + r1 + " ms -> good.");
        }
        assertTrue(r1 <= r2);
    }

    @Test(timeout = 60000)
    public void test6SpeedAscending() {
        double msRuntime = Double.POSITIVE_INFINITY, qsRuntime = Double.POSITIVE_INFINITY;

        for (int i = 0; i < 4; ++i) {
            SortTestRunner qs = SortTestRunner.runSingle(new OurQuickSort(), n, new FillAscendingOrder(cmp), cmp);
            assertTrue(qs.getSorted());
            qsRuntime = Math.min(qsRuntime, qs.getRuntime());

            SortTestRunner ms = SortTestRunner.runSingle(new ListMergeSort(), n, new FillAscendingOrder(cmp), cmp);
            assertTrue(ms.getSorted());
            msRuntime = Math.min(msRuntime, ms.getRuntime());
        }

        assertRuntime(msRuntime, qsRuntime);
    }

    @Test(timeout = 60000)
    public void test7SpeedDescending() {
        double msRuntime = Double.POSITIVE_INFINITY, qsRuntime = Double.POSITIVE_INFINITY;

        for (int i = 0; i < 4; ++i) {
            SortTestRunner qs = SortTestRunner.runSingle(new OurQuickSort(), n, new FillDescendingOrder(cmp), cmp);
            assertTrue(qs.getSorted());
            qsRuntime = Math.min(qsRuntime, qs.getRuntime());

            SortTestRunner ms = SortTestRunner.runSingle(new ListMergeSort(), n, new FillDescendingOrder(cmp), cmp);
            assertTrue(ms.getSorted());
            msRuntime = Math.min(msRuntime, ms.getRuntime());
        }

        assertRuntime(msRuntime, qsRuntime);
    }

    @Test(timeout = 60000)
    public void test8SpeedSawtooth() {
        double msRuntime = Double.POSITIVE_INFINITY, qsRuntime = Double.POSITIVE_INFINITY;

        for (int i = 0; i < 4; ++i) {
            SortTestRunner qs = SortTestRunner.runSingle(new OurQuickSort(), n, new FillSawtooth(), new CompareA());
            assertTrue(qs.getSorted());
            qsRuntime = Math.min(qsRuntime, qs.getRuntime());

            SortTestRunner ms = SortTestRunner.runSingle(new ListMergeSort(), n, new FillSawtooth(), new CompareA());
            assertTrue(ms.getSorted());
            msRuntime = Math.min(msRuntime, ms.getRuntime());
        }

        assertRuntime(msRuntime, qsRuntime);
    }

    public static void main(String[] args) throws Exception {
        // mit JUnit ausfuehren:
        JUnitCore.main(SortTestPrivate.class.getName());
    }
}
