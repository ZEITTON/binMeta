import javax.naming.InsufficientResourcesException;
import java.awt.*;
import java.net.InterfaceAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/** NOTE:
 * Dans D:Data est stocké l'affectation des items aux bins (bin = boites)
 */
public class BinPacking implements Objective {
    // Attributes
    private List<Integer> items;   //items: list of items to be stored in the bins
    private int c; //c: Bin capacity
    private int size; //size: list of items size

    // Constructor (from List)
    public BinPacking(List<Integer> items, int c, int size){
        try {
            if(items == null) throw new Exception("BinPackin: items's list is null");
            if(items.isEmpty()) throw new Exception("BinPackin: items's list is empty");
            this.items = new ArrayList<>(items);
            this.c = c;
            this.size = size;
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public String getName() {
        return "BinPacking";
    }

    @Override
    public Data solutionSample() {
        return new Data(items.size(), 0.5);
    }

    @Override
    public double value(Data D) {
        int n = 0;
        try
        {
            if (D == null) throw new Exception("Impossible to evaluate BinPacking objective: the Data object is null");
            n = D.numberOfBits();
            if (n != this.size)  throw new Exception("Impossible to evaluate Knapsack objective: number of bits in Data object differs from expected value");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }

        //variables
        double sum = 0.0;
        double result = 0.0;
        Data val;

        //iterators
        Iterator<Integer> it = items.iterator();
        Data.bitIterator itD = D.iterator();

        //Position of bits to extract
        int firstPos = 0;
        int lastPos = 0;

        //calculate the sum of the weights for the selected item
        while (itD.hasNext() && it.hasNext()){
            firstPos = lastPos; //reset position

            //extracting a specific subsequence of bits
            for(int i=0; i<=n; i++) {
                itD.next();
                lastPos++;
            }
            val = new Data(D, firstPos, lastPos);

            sum += it.next()*(val.intValue());
        }

        // the constraint on the maximum weight is in the objective function
        if (sum <= this.c) {
            result = -sum;
        }else {
            result = sum - this.c;
        }
        return result;

    }

    public static void main(String[] args) {
        List<Integer> l = new ArrayList();
        l.add(1); l.add(10); l.add(5); l.add(4); l.add(4);
        BinPacking obj = new BinPacking(l, 10, l.size());
        Data D = obj.solutionSample();
        int x = D.intValue();
        int n = D.numberOfBits(); //nb d'items enft jsp
        System.out.println(D.toString()+"\n");
        System.out.println("intValue = "+x);
        System.out.println("numberOfBits = "+n);
        /*
        Data.bitIterator itbit = D.iterator();
        while (itbit.hasNext()){
            int i = itbit.next();
            System.out.println(i);
        }*/

        System.out.println("test : ");
    }
}
