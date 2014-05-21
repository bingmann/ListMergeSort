/**
 * Diese Datei enthaelt eine einfache Quicksort Implementierung, die es zu
 * schlagen gilt. Wenn Ihr ListMergeSort schneller ist als dieser QuickSort
 * fuer bestimmte Eingaben, erhalten Sie Punkte wie in der Aufgabenstellung
 * beschrieben.
 */
class OurQuickSort implements SortInterface
{
    OurCompare cmp;

    /** Median von drei */
    int med3func(int[] arr, int p1, int p2, int p3) {

        if (cmp.less(arr[p1], arr[p2])) {
            if (cmp.less(arr[p2], arr[p3]))
                return p2;
            else if (cmp.less(arr[p1], arr[p3]))
                return p3;
            else
                return p1;
        }
        else if (cmp.less(arr[p1], arr[p3])) {
            return p1;
        }
        else if (cmp.less(arr[p2], arr[p3])) {
            return p3;
        }
        else {
            return p2;
        }
    }

    /** Unser einfacher Quicksort */
    void quickSort(int[] arr, int left, int right) {

        int i = left, j = right;
        int pivotPos = med3func(arr, i, (i + j) / 2, j);

        // swap pivot to beginning
        int pivot = arr[pivotPos];
        arr[pivotPos] = arr[left];
        arr[left] = pivot;

        while (i <= j) {

            while (cmp.less(arr[i], pivot))
                i++;

            while (cmp.less(pivot, arr[j]))
                j--;

            if (i <= j) {
                int tmp = arr[i];
                arr[i] = arr[j];
                arr[j] = tmp;
                i++;
                j--;
            }
        }

        if (left < j)
            quickSort(arr, left, j);

        if (i < right)
            quickSort(arr, i, right);
    }

    public void sort(int[] array, OurCompare cmp) {
        this.cmp = cmp;
        quickSort(array, 0, array.length - 1);
    }
}
