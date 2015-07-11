

/**
 * Created by gordoneliel on 4/3/15.
 */
public class Sort implements SortInterface
{
    public Sort()
    {

    }


    /**
     * Uses insertion sort with a high and low index
     * Use of tenery operator to support reversal of sorting
     * @param array
     * @param lowindex
     * @param highindex
     * @param reversed
     */
    public void insertionSort(Comparable[] array, int lowindex, int highindex, boolean reversed)
    {
        int i, j;
        Comparable curr;

            for (i = lowindex + 1; i <= highindex; i++)
            {
                curr = array[i];
                for (j = i - 1; j >= lowindex && (reversed ? array[j].compareTo(curr) < 0 : array[j].compareTo(curr) > 0); j--)
                {
                    array[j + 1] = array[j];
                }
                array[j + 1] = curr;
            }
    }

    /**
     * Uses selectionSort sort with a high and low index
     * @param array
     * @param lowindex
     * @param highindex
     * @param reversed
     */
    public void selectionSort(Comparable[] array, int lowindex, int highindex, boolean reversed)
    {
        int i, j;
        Comparable smallest;
        int swapindex;


            for (i = lowindex; i <= highindex; i++)
            {
                smallest = array[i];
                swapindex = i;
                for (j = i + 1; j <= highindex; j++)
                {
                    if (reversed ? array[j].compareTo(smallest) > 0 : array[j].compareTo(smallest) < 0)
                    {
                        smallest = array[j];
                        swapindex = j;
                    }
                }
                array[swapindex] = array[i];
                array[i] = smallest;
            }

    }

    @Override
    public void shellSort(Comparable[] array, int lowindex, int highindex, boolean reversed)
    {
        Comparable curr;
        int n = highindex - lowindex + 1;
        int i, j, k;
        i = j = k = 1;


        while (k * 2 - 1 < n)
        {
            k *= 2;
        }

        k--;
        i = k;
        while (i < highindex && k >=1)
        {
            curr = array[i];
            for (j = i - k; j >= lowindex && (reversed ? array[j].compareTo(curr) < 0 : array[j].compareTo(curr) > 0); j -= k)
            {
                array[j + k] = array[j];
            }
            array[j + k] = curr;

            if (i + k > highindex)
            {
                k /= 2;
                i = 0;
            }
            i+=k;
        }
    }

    @Override
    public void bucketSort(int[] array, int lowindex, int highindex, boolean reversed)
    {

    }

    /**
     * Performs heapSort with
     * @param array
     * @param lowindex
     * @param highindex
     * @param reversed
     */
    public void heapSort(Comparable[] array, int lowindex, int highindex, boolean reversed)
    {
        int maxIdx;
        int parent;
        Comparable temp;
        int adj;
        int i;

        if (highindex < 1) return;

        for (i = highindex; i > lowindex; i -= 2)
        {
            adj = i - 1;
            maxIdx = findMax(array, i, adj, reversed);
            parent = (maxIdx - 1) / 2;
            maxIdx = findMax(array, maxIdx, parent, reversed);

            if (maxIdx != parent)
            {
                swapReferences(array, parent, maxIdx);
            }
        }

        swapReferences(array, lowindex, highindex);
        heapSort(array, lowindex, highindex - 1, reversed);
    }

    private int findMax(Comparable[] array, int i, int sib, boolean reversed)
    {
        if (array[i].compareTo(array[sib]) < 0)
            return reversed ? i : sib;
        else
            return reversed ? sib: i;
    }

    /**
     * Helper method for heap & quicksort to swap elements in arrays
     */
    public void swapReferences(Comparable[] array, int first, int last)
    {
        Comparable temp = array[first];
        array[first] = array[last];
        array[last] = temp;
    }

    /**
     * Sorts from high to low using quicksort
     * @param array
     * @param lowindex
     * @param highindex
     * @param reversed
     */
    public void quicksort(Comparable[] array, int lowindex, int highindex, boolean reversed)
    {
        int length = highindex - lowindex + 1;
        Comparable top = array[lowindex];
        Comparable mid = array[(highindex + lowindex) / 2];
        Comparable bottom = array[highindex];
        Comparable temp;
        Comparable[] average = {top, mid, bottom};

        //if we have just one or less elements, we are sorted
        if (length <= 1) return;

        else if (length == 2)
        {

            if (reversed ? (array[lowindex].compareTo(array[highindex]) < 0) : (array[lowindex].compareTo(array[highindex]) > 0))
            {
                temp = array[lowindex];
                array[lowindex] = array[highindex];
                array[highindex] = temp;
            }
        }
        else
        {

            Comparable pivot = findPivot(average);
            int pivotIndex = Partition(array, pivot, lowindex + 1, highindex, reversed);
            quicksort(array, lowindex, pivotIndex - 1, reversed);
            quicksort(array, pivotIndex, highindex, reversed);
        }
    }

    public Comparable findPivot(Comparable[] array)
    {
        Comparable pivot = 0;

        for (int i = 0; i < array.length - 1; i++)
        {
            if (array[i].compareTo(array[i + 1]) > 0)
            {
                swapReferences(array, i, i + 1);
            }

            if (i == 1)
            {
                if (array[i].compareTo(array[i - 1]) < 0)
                {
                    pivot = array[i - 1];
                }
                else
                {
                    pivot = array[i];
                }

            }
        }
        return pivot;
    }
    public int Partition(Comparable[] array, Comparable pivot, int lowindex, int highindex, boolean reversed)
    {
        Comparable temp;
        int j = highindex;

        if (array[lowindex - 1].compareTo(pivot) != 0)
        {
            for (int k = lowindex; k <= highindex; k++)
            {
                if (array[k].compareTo(pivot) == 0)
                {
                    swapReferences(array, lowindex - 1, k);
                    array[lowindex - 1] = pivot;
                }
            }
        }

        for (int i = lowindex; i < j; i++)
        {
            if (reversed ? array[i].compareTo(pivot) < 0 : array[i].compareTo(pivot) > 0)
            {
                while (reversed ? array[j].compareTo(pivot) < 0 :array[j].compareTo(pivot) > 0)
                {
                    j--;
                }

                if (i <= j)
                {
                    swapReferences(array, i, j);
                }
                else
                {
                    swapReferences(array, j, lowindex - 1);
//                    temp = array[j];
                    array[j] = pivot;
//                    array[lowindex - 1] = temp;
                }
            }
        }

        return j;
    }

    @Override
    public void radixSort(int[] array, int lowindex, int highindex, boolean reversed)
    {
//        int i , j , rtok;
//        int k = highindex;
//        int r = array.length;
//
//        for(i=0,rtok = 1; i<k; i++, rtok *= r)
//        {
//            for (j=0; j<r; j++)
//                count[j] = 0;
//            for(j=0; j<A.length; j++)
//                count[(A[k].key()/rtok)%r]++;
//
//            for(j=1; j<r; j++)
//                count[j] = count[j-1] + count[j];
//
//            for(j=A.length-1; j>=0; j--)
//                B[--count[(A[j].key()/rtok)%r]] = A[j];
//
//            for(j=0; j<A.length; j++)
//                A[j] = B[j];
//        }

        final int SIZE = highindex - lowindex;

        int[] A = new int[SIZE];
        int[] B = new int[SIZE];

        for (int k = lowindex, j = 0; k < highindex; k++, j++)
        {
            B[j] = array[k];
        }

        int k;
        int r = (SIZE) + 1;
        int[] count = new int[r];
        int i, j;
        long rtok;
        for (i = 0, rtok = 1; i < B.length; i++, rtok *= r)
        {
            for (j = 0; j < r; j++)
            {
                count[j] = 0;
            }
            for (j = 0; j < B.length; j++)
            {
                count[(int)(B[j] / rtok) % r]++;
            }
            for (j = 1; j < r; j++)
            {
                count[j] = count[j - 1] + count[j];
            }

            for (j = B.length - 1; j >= 0; j--)
            {
                A[--count[(int)(B[j] / rtok) % r]] = B[j];
            }

            for (j = 0; j < B.length; j++)
            {
                B[j] = A[j];
            }
        }

        k = lowindex;
        int l;

        l = reversed ? B.length - 1 : 0;

        while (k < highindex)
        {
            array[k] = B[l];
            l += reversed ? -1 : 1;
            k++;
        }

    }

    /**
     * Performs a selection sort on a linked list of values
     * @param list
     * @param reversed
     * @return list - the sorted list
     */
    public LLNode selectionSortLL(LLNode list, boolean reversed)
    {
        LLNode nodeHead = list;

        while (nodeHead != null)
        {
            LLNode min = nodeHead;

            LLNode nodeTemp = nodeHead;

            while (nodeTemp != null)
            {
                if (reversed ? min.elem().compareTo(nodeTemp.elem()) < 0 : min.elem().compareTo(nodeTemp.elem()) > 0)
                {
                    min = nodeTemp;
                }
                nodeTemp = nodeTemp.next();
            }

            if(min!= nodeHead)
            {
                Comparable temp = min.elem();
                min.setElem(nodeHead.elem());
                nodeHead.setElem(temp);
            }
            nodeHead = nodeHead.next();

        }
        list = nodeHead;
        return list;

    }

    @Override
    public LLNode mergeSortLL(LLNode list, boolean reversed)
    {
        if (list == null || list.next() == null)
        {
            return list;
        }

        return null;
    }

    /**
     * Performs an insertion sort on a linked list of values
     * @param list
     * @param reversed
     * @return
     */
    public LLNode insertionSortLL(LLNode list, boolean reversed)
    {
        if (list == null || list.next() == null)
        {
            return list;
        }

        LLNode newHead = new LLNode(list.elem(), null);
        LLNode current = list.next();

        while(current != null)
        {
            LLNode temp = newHead;
            LLNode next = current.next();

            if (reversed ? current.elem().compareTo(newHead.elem()) >= 0 : current.elem().compareTo(newHead.elem()) <= 0)
            {
                LLNode oldHead = newHead;
                newHead = current;
                newHead.setNext(oldHead);

            } else
            {
                while (temp.next() != null)
                {
                    if (( reversed ?  current.elem().compareTo(temp.elem()) < 0 : current.elem().compareTo(temp.elem()) > 0 )&& current.elem().compareTo(temp.next().elem()) <= 0)
                    {
                        LLNode oldNext = temp.next();
                        temp.setNext(current);
                        current.setNext(oldNext);
                    }
                    temp = temp.next();
                }

                if (temp.next() == null && (reversed ? (current.elem().compareTo(temp.elem()) < 0) : current.elem().compareTo(temp.elem()) > 0))
                {
                    temp.setNext(current);
                    current.setNext(null);
                }
            }
            current = next;
        }

        return newHead;
    }

    /**
     * An optimized version of quicksort that uses insertion sort for smaller list sizes
     * @param array
     * @param lowindex
     * @param highindex
     * @param reversed
     */
    public void optimizedQuicksort(Comparable[] array, int lowindex, int highindex, boolean reversed)
    {
        int length = highindex - lowindex + 1;
        Comparable top = array[lowindex];
        Comparable mid = array[(highindex + lowindex) / 2];
        Comparable bottom = array[highindex];
        Comparable[] average = {top, mid, bottom};

        if (length <= 1) return;

        if (length <= 200)
        {
            insertionSort(array, lowindex, highindex, reversed);
        }

        else
        {

            Comparable pivot = findPivot(average);
            int pivotIndex = Partition(array, pivot, lowindex + 1, highindex, reversed);
            quicksort(array, lowindex, pivotIndex - 1, reversed);
            quicksort(array, pivotIndex, highindex, reversed);
        }
    }
}
