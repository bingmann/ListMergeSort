/**
 * Diese Datei enthaelt drei Komparatoren: OurCompareA/B/C. Die Komparatoren
 * zaehlen die Anzahl von Vergleichen, die ein Sortieralgorithmus benoetigt. Sie
 * _muessen_ diese verwenden um Punkte zu bekommen, da die Test die erwartete
 * Anzahl von Vergleichen prueft.
 */

import java.util.Comparator;

/**
 * Gemeinsame Funktionen fuer alle Komparatoren:
 */
abstract class OurCompare implements Comparator<Integer>
{
    protected int count = 0;

    /** Eine Methode um einen neuen Komparator gleichen Typs zu erzeugen. */
    abstract public OurCompare newInstance();

    /** Dies wird vom individuellen Komparator definiert. */
    public abstract int cmp(int a, int b);

    /** true if a == b */
    public boolean equal(int a, int b) {
        return cmp(a, b) == 0;
    }

    /** true if a != b */
    public boolean unequal(int a, int b) {
        return cmp(a, b) != 0;
    }

    /** true if a < b */
    public boolean less(int a, int b) {
        return cmp(a, b) < 0;
    }

    /** true if a <= b */
    public boolean less_equal(int a, int b) {
        return cmp(a, b) <= 0;
    }

    /** true if a > b */
    public boolean greater(int a, int b) {
        return cmp(a, b) > 0;
    }

    /** true if a >= b */
    public boolean greater_equal(int a, int b) {
        return cmp(a, b) >= 0;
    }

    /** Die Anzahl der gezaehlten Vergleiche */
    public int get() {
        return count;
    }

    /** Diese Methode von Comparator<Integer> wird zur Pruefung verwendet. */
    @Override
    public int compare(Integer arg0, Integer arg1) {
        return cmp(arg0, arg1);
    }
}

/** Dies ist ein einfacher Komparator, der aufsteigend sortiert. */
class CompareA extends OurCompare
{
    public int cmp(int a, int b) {
        ++count;
        return a - b;
    }

    public OurCompare newInstance() {
        return new CompareA();
    }
}

/** Dies ist ein einfacher Komparator, der absteigend sortiert. */
class CompareB extends OurCompare
{
    public int cmp(int a, int b) {
        ++count;
        return b - a;
    }

    public OurCompare newInstance() {
        return new CompareB();
    }
}

/**
 * Verwenden Sie diesen (komplizierten) Komparator, um sicherzustellen, dass sie
 * ints nur mit Komparatoren vergleichen!
 */
class CompareC extends OurCompare
{
    public int cmp(int a, int b) {
        ++count;
        return (a ^ 9) - (b ^ 9);
    }

    public OurCompare newInstance() {
        return new CompareC();
    }
}
