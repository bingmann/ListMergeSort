/*
 * Dies ist unser Loesungsvorschlag. Er ist nicht minimal und enthaelt viele
 * Optimierungen um Teile schneller zu machen. Nicht alle Optimierungen sind
 * notwendig um volle Punktzahl bei der Aufgabe zu bekommen, viele haben sich
 * als ueberfluessig herausgestellt.
 */

import java.util.AbstractList;
import java.util.ArrayList;

/*
 * Zuerst zwei eigene Datenstrukturen: ein dynamisches int[] Array und eine
 * dynamisch wachsende Deque (double-ended queue), die aus zwei der Array
 * aufgebaut ist.
 */

/**
 * IntArray und IntDeque haben beide das folgende Interface, da bei beiden am
 * Ende angefuegt werden kann.
 */
interface IIntArray
{
    /** Alloziere Speicher fuer mindestens newlen Elemente. */
    void reserve(int newlen);

    /** Fuege e am Ende der Datenstruktur an. */
    void add(int e);

    /** Liefere das Elemente an Position pos zurueck. */
    int get(int pos);

    /** Liefere die Anzahl enthaltener Elemente. */
    int size();
}

/**
 * Da java.util.ArrayList nicht mit primitiven ints funktioniert, bauen wir ein
 * einfaches dynamisch wachsendes int[].
 */
class IntArray implements IIntArray
{
    protected int[] m_array;

    protected int m_size;

    public IntArray() {
        m_size = 0;
        m_array = new int[0];
    }

    public void reserve(int newlen) {
        if (newlen < 16)
            newlen = 16;

        int[] newarray = new int[newlen];
        System.arraycopy(m_array, 0, newarray, 0, m_size);

        m_array = newarray;
    }

    public int[] data() {
        return m_array;
    }

    public void add(int e) {
        if (m_size >= m_array.length) {
            reserve(m_array.length * 2);
        }
        m_array[m_size++] = e;
    }

    public int get(int pos) {
        return m_array[pos];
    }

    public void set(int pos, int e) {
        m_array[pos] = e;
    }

    public int size() {
        return m_size;
    }

    /** Kopieren Inhalt des Arrays nach output */
    public void copyTo(int[] output) {
        assert (size() == output.length);
        System.arraycopy(m_array, 0, output, 0, m_size);
    }
}

/**
 * Noch eine Datenstruktur, weil Java keine primitive int[] als generics
 * Parameter unterstuetzt. Die IntDeque ist aus zwei IntArray aufgebaut: eine
 * in normaler Reihenfolge und eine in umgekehrter Reihenfolge. Der Anfang der
 * Deque ist am Ende der umgekehrten Folge, das Ende der Deque am Ende der
 * normalen Folge.
 */
class IntDeque implements IIntArray
{
    /** Die IntDeque enthaelt die Folge: reverse(head) + tail */
    protected IntArray head, tail;

    IntDeque() {
        head = new IntArray();
        tail = new IntArray();
    }

    /** Am Anfang in amortisiert O(1) anfuegen. */
    public void pushHead(int e) {
        head.add(e);
    }

    /** Am Ende in amortisiert O(1) anfuegen. */
    public void pushTail(int e) {
        tail.add(e);
    }

    /** Das gleiche wie pushTail() fuer IIntArray interface. */
    public void add(int e) {
        tail.add(e);
    }

    /** Berechne welches das aktuelle Element an pos ist */
    public int get(int pos) {
        if (pos < head.size())
            return head.get(head.size() - 1 - pos);
        else
            return tail.get(pos - head.size());
    }

    public int getHead() {
        if (head.size() != 0)
            return head.get(head.size() - 1);
        else
            return tail.get(0);
    }

    public int getTail() {
        if (tail.size() != 0)
            return tail.get(tail.size() - 1);
        else
            return head.get(0);
    }

    public int size() {
        return head.size() + tail.size();
    }

    /** Kopieren Inhalt des Arrays nach output. */
    public void copyTo(int[] output) {
        assert (size() == output.length);

        // kopiere head in umgekehrter Reihenfolge
        int j = 0;
        for (int i = head.size()-1; i >= 0; --i) {
            output[j++] = head.get(i);
        }

        // primitiver Speicher-Kopiervorgang fuer den Rest
        System.arraycopy(tail.data(), 0, output, j, tail.size());
    }

    public void reserve(int newlen) {
        tail.reserve(newlen);
    }
}

public class ListMergeSort implements SortInterface
{
    // Liste der sortierten Listen
    AbstractList<IntDeque> lists;

    // Listen von Anhang und Ende der sortierten Listen
    IntArray heads, tails;

    // Vergleichsrelation
    OurCompare cmp;

    ListMergeSort() {
        lists = new ArrayList<IntDeque>();

        heads = new IntArray();
        tails = new IntArray();
    }

    /**
     * Binaere Suche in einem sortierten Array und liefere den kleinste Index i
     * mit list[i] >= key. Die Grenze hi liegt ein Element _nach_ dem Ende.
     */
    static int lowerBound(int[] list, int size, int key, OurCompare cmp) {
        // hi ist size-1, da wir schon wissen, dass list[size-1] >= key.
        int lo = 0, hi = size - 1;
        while (lo < hi) {
            int mid = (lo + hi) / 2;
            if (cmp.less_equal(key, list[mid]))
                hi = mid;
            else
                lo = mid + 1;
        }
        return lo;
    }

    /**
     * Binaere Suche in einem umgekehrt sortierten Array und liefere den
     * kleinste Index i mit list[i] <= key. Die Grenze hi liegt ein Element
     * _nach_ dem Ende.
     */
    static int revLowerBound(int[] list, int size, int key, OurCompare cmp) {
        // hi ist size-1, da wir schon wissen, dass list[size-1] <= key.
        int lo = 0, hi = size - 1;
        while (lo < hi) {
            int mid = (lo + hi) / 2;
            if (cmp.greater_equal(key, list[mid]))
                hi = mid;
            else
                lo = mid + 1;
        }
        return lo;
    }

    // Merge Inhalt von zwei Listen A und B
    static void merge(IIntArray listA, IIntArray listB, IIntArray out, OurCompare cmp) {

        out.reserve(listA.size() + listB.size());

        int pA = 0, pB = 0;

        while (pA != listA.size() && pB != listB.size()) {

            if (cmp.less_equal(listA.get(pA), listB.get(pB))) {
                out.add(listA.get(pA++));
            }
            else {
                out.add(listB.get(pB++));
            }
        }

        while (pA != listA.size())
            out.add(listA.get(pA++));

        while (pB != listB.size())
            out.add(listB.get(pB++));
    }

    // Sort Funktion die zum Sortieren aufgerufen wird.
    public void sort(int[] input, OurCompare xcmp) {

        cmp = xcmp;

        // Erstelle Liste von Listen fuer das erste Element
        if (input.length == 0)
            return;
        else {
            int x = input[0];

            IntDeque il = new IntDeque();
            il.pushTail(x);
            lists.add(il);
            heads.add(x);
            tails.add(x);
        }

        // Fuege jedes weitere Element an den besten Platz ein
        for (int i = 1; i < input.length; ++i) {

            // Fuege element array[i] in die Datenstruktur ein
            int x = input[i];

            // Teste ob x kleiner-gleich dem kleinsten Element in der innersten
            // Liste ist.
            if (cmp.less_equal(x, heads.get(heads.size() - 1))) {

                // dann fuehre binaere Suche auf den kleinsten Elementen der
                // Listen nach dem besten Platz durch
                int pos = lowerBound(heads.data(), heads.size(), x, cmp);

                lists.get(pos).pushHead(x);
                heads.set(pos, x);
            }
            // Teste ob x groesser-gleich dem groessten Element in der
            // innersten Liste ist.
            else if (cmp.greater_equal(x, tails.get(tails.size() - 1))) {

                // dann fuehre binaere Suche auf den groessten Elementen der
                // Listen nach dem besten Platz durch
                int pos = revLowerBound(tails.data(), tails.size(), x, cmp);

                lists.get(pos).pushTail(x);
                tails.set(pos, x);
            }
            // sonst: erzeuge eine neue innerste Liste
            else {
                IntDeque il = new IntDeque();
                il.pushTail(x);
                lists.add(il);
                heads.add(x);
                tails.add(x);
            }

            // check(); // optional: Invarianten pruefen.
        }

        System.out.println("Created " + lists.size() + " ordered lists");

        // for (int i = 0; i < lists.size(); ++i) {
        //   System.out.println("list[" + i + "] = " + lists.get(i));
        // }

        // Binaeres Merge der Liste von sortierten Listen.

        int listsize = lists.size();

        if (listsize == 1) {
            lists.get(0).copyTo(input);
            return;
        }

        IntArray[] alists = new IntArray[listsize / 2];

        // Erste Merge-Iteration reduziert die Deques zu einem (einfachen) Array
        {
            // ungerade Anzahl von Listen? -> merge letzten beiden.
            if (listsize % 2 == 1) {
                IntDeque m = new IntDeque();

                merge(lists.get(listsize - 2), lists.get(listsize - 1), m, cmp);

                // loesche allerletzte und ersetze vorletzte.
                lists.set(listsize - 1, null);
                lists.set(listsize - 2, m);
                listsize--;
            }

            // gerade Anzahl von Listen? -> merge Paare von Listen.
            for (int i = 0, j = listsize / 2; i < listsize / 2; ++i, ++j) {

                IntArray m = new IntArray();
                merge(lists.get(i), lists.get(j), m, cmp);

                // loesche zweite Liste, die in der zweiten Haelfte liegt.
                lists.set(i, null);
                lists.set(j, null);
                alists[i] = m;
            }

            listsize /= 2;
        }

        // Nun werden nur noch einfache Arrays gemergt.
        while (listsize != 1) {

            // ungerade Anzahl von Listen? -> merge letzten beiden.
            if (listsize % 2 == 1) {
                IntArray m = new IntArray();

                merge(alists[listsize - 2], alists[listsize - 1], m, cmp);

                alists[listsize - 1] = null;
                alists[listsize - 2] = m;
                listsize--;
            }

            // gerade Anzahl von Listen? -> merge Paare von Listen.
            for (int i = 0, j = listsize / 2; i < listsize / 2; ++i, ++j) {

                IntArray m = new IntArray();

                merge(alists[i], alists[j], m, cmp);

                alists[i] = m;
                alists[j] = null;
            }

            listsize /= 2;
        }

        // nur noch eine Liste uebrig -> kopiere zurueck in das Eingabearray.

        alists[0].copyTo(input);
    }

    // Pruefe ob eine Liste sortiert ist.
    boolean isSorted(IIntArray list) {

        for (int i = 1; i < list.size(); ++i) {
            assert (cmp.less_equal(list.get(i - 1), list.get(i)));
        }
        return true;
    }

    // Pruefe die Invarianten der Liste von sortierten Listen Datenstruktur.
    void check() {

        for (int i = 1; i < heads.size(); ++i)
            assert (heads.get(i - 1) <= heads.get(i));

        for (int i = 1; i < tails.size(); ++i)
            assert (tails.get(i) <= tails.get(i - 1));

        for (int i = 0; i < lists.size(); ++i) {
            assert (heads.get(i) == lists.get(i).getHead());
            assert (tails.get(i) == lists.get(i).getTail());
            assert (isSorted(lists.get(i)));
        }
    }
}
