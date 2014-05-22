/**
 * Die public Sorting Test cases. Diese werden im Praktomat mit einem anderen
 * Komparator ausgefuehrt und geben Punkte bei Bestehen.
 */

import static org.junit.Assert.assertTrue;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SortTestPublic
{
    /** Im Praktomat wird dies geaendert sein. */
    OurCompare cmp = new CompareC();

    final int n = 1 * 1024 * 1024;
    final double logn = Math.log(n) / Math.log(2);

    @Test
    public void test1ComparisonsAscending() {
        SortTestRunner ms = SortTestRunner.runSingle(new ListMergeSort(), n, new FillAscending(), new CompareA());
        assertTrue(ms.getSorted());
        assertTrue(ms.getComparisons() <= 2 * n * 1.10);
    }

    @Test
    public void test2ComparisonsDescending() {
        SortTestRunner ms = SortTestRunner.runSingle(new ListMergeSort(), n, new FillDescending(), new CompareA());
        assertTrue(ms.getSorted());
        assertTrue(ms.getComparisons() <= 2 * n * 1.10);
    }

    @Test
    public void test3ComparisonsSawtooth() {
        SortTestRunner ms = SortTestRunner.runSingle(new ListMergeSort(), n, new FillSawtooth(), new CompareA());
        assertTrue(ms.getSorted());
        assertTrue(ms.getComparisons() <= 8 * n * 1.10);
    }

    @Test
    public void test4ComparisonsRandom() {
        SortTestRunner ms = SortTestRunner.runSingle(new ListMergeSort(), n, new FillRandom(), cmp);
        assertTrue(ms.getSorted());
        assertTrue(ms.getComparisons() <= n * logn * 1.10);
    }

    /* Implementieren Sie FillWorstCase.
    @Test
    public void test5ComparisonsWorstCase() {
        SortTestRunner ms = SortTestRunner.runSingle(new ListMergeSort(), n, new FillWorstCase(), cmp);
        assertTrue(ms.getSorted());
        assertTrue(ms.getComparisons() <= 2 * n * logn * 1.10);
        assertTrue(ms.getComparisons() >= 1 * n * logn * 0.90);
    }
    */

    void assertRuntime(double r1, double r2) {
        if (r1 > r2) {
            System.out.println("Runtime Quicksort " + r2 + " ms < ListMergeSort " + r1 + " ms -> BAD!");
        }
        else {
            System.out.println("Runtime Quicksort " + r2 + " ms >= ListMergeSort " + r1 + " ms -> good.");
        }
        assertTrue(r1 <= r2);
    }

    @Test
    public void test6SpeedAscending() {
        SortTestRunner qs = SortTestRunner.runSingle(new OurQuickSort(), n, new FillAscending(), new CompareA());
        assertTrue(qs.getSorted());

        SortTestRunner ms = SortTestRunner.runSingle(new ListMergeSort(), n, new FillAscending(), new CompareA());
        assertTrue(ms.getSorted());

        assertRuntime(ms.getRuntime(), qs.getRuntime());
    }

    @Test
    public void test7SpeedDescending() {
        SortTestRunner qs = SortTestRunner.runSingle(new OurQuickSort(), n, new FillDescending(), new CompareA());
        assertTrue(qs.getSorted());

        SortTestRunner ms = SortTestRunner.runSingle(new ListMergeSort(), n, new FillDescending(), new CompareA());
        assertTrue(ms.getSorted());
        assertRuntime(ms.getRuntime(), qs.getRuntime());
    }

    @Test
    public void test8SpeedSawtooth() {
        SortTestRunner qs = SortTestRunner.runSingle(new OurQuickSort(), n, new FillSawtooth(), new CompareA());
        assertTrue(qs.getSorted());

        SortTestRunner ms = SortTestRunner.runSingle(new ListMergeSort(), n, new FillSawtooth(), new CompareA());
        assertTrue(ms.getSorted());
        assertRuntime(ms.getRuntime(), qs.getRuntime());
    }

    public static void main(String[] args) throws Exception {
        // mit JUnit ausfuehren:
        JUnitCore.main(SortTestPublic.class.getName());
    }
}
