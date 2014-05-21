/**
 * Dies ist das Interface fuer den zu implementierenden Sortieralgorithmus. Nach
 * Aufruf von sort() soll das Array mit der durch cmp definierten
 * Vergleichsrelation sortiert sein.
 */

public interface SortInterface
{
    public void sort(int[] array, OurCompare cmp);
}
