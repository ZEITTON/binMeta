import javax.naming.InsufficientResourcesException;
import java.awt.*;
import java.net.InterfaceAddress;
import java.util.*;
import java.util.List;

public class BinPacking implements Objective
{
    //Attributes
    private List<Integer> items;   //items: list of items to be stored in the bins
    private int maxCapacity; //max capacity of a bin

    private String name;
    private int nBin; //number of bin by default
    private int sizeBit; //size: Size of the bit sequence in the sample solution

    // Constructor
    public BinPacking(List<Integer> items, int maxCapacity){
        try {
            if(items == null) throw new Exception("BinPacking: items's list is null");
            if(items.isEmpty()) throw new Exception("BinPacking: items's list is empty");
            this.items = new ArrayList<>(items);
            this.nBin = this.items.size(); //the number of bins equal a number of items by default
            this.sizeBit = this.items.size() * ((int)(Math.log(nBin) / Math.log(2))); // items.size * log2(nBin)

            if (maxCapacity <= 0) throw new Exception("BinPacking: specified maxCapacity value is nonpositive");
            this.maxCapacity = maxCapacity;

            this.name = "BinPacking";
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }

    // Getter
    public List<Integer> getItems() { return this.items; }

    //toString
    public String toString()
    {
        return "[" + this.getName() + ": " + this.items.size() + " items, the capacity of a bin is " + this.maxCapacity + "]";
    }


    /* ====================== implements method of objectives ====================== */
    @Override
    public String getName() { return this.name; }

    /**
     * In a Data 'D' is stored the assignment of items to bins
     * [000 010 101 111] => (item[0] -> bin[000]), (item[1] -> bin[010]) ...
     */
    @Override
    public Data solutionSample() { return new Data(this.sizeBit, 0.5); }

    @Override
    public double value(Data D)
    {
        try
        {
            String err = "Impossible to evaluate BinPacking objective: ";
            if (D == null) throw new Exception(err + "the Data object is null");
            if (D.numberOfBits() != this.sizeBit)  throw new Exception(err + "number of bits in Data object differs from expected value");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }

        //attributes
        Map<Integer, Integer> map = new HashMap<>(D.numberOfBits()/2); //K: are the bins & V: are the articles
        int n = ((int)(Math.log(nBin) / Math.log(2))); //number of bits used to represent a bin
        Data val;

        //iterators
        Iterator<Integer> it1 = items.iterator();
        Data.bitIterator it2 = D.iterator();

        //Position of bits to be split
        int firstP = 0; int lastP = 0;

        //index is the bin number & value is an item
        int index; int value;

        //calculate the sum of the weights for the selected item
        while (it1.hasNext() && it2.hasNext()){
            firstP = lastP; //the last position becomes the first to keep moving forward

            //extracting a specific subsequence of bits
            for(int i=0; i<n; i++) { lastP++;}
            val = new Data(D, firstP, lastP);

            //we convert the bit sequence to an integer
            index = val.intValue();
            value = it1.next(); //the item

            if(map.get(index) == null) { //check if the bin is empty
                map.put(index, value);  //put the item in the bin "index"
            } else{
                int x = map.get(index) + value; //sum of the value of the items
                if(x <= maxCapacity) //check that it does not exceed the maximum capacity of the bin
                {
                    map.put(index, x); //put the item in the bin "index"
                }
                else
                {
                    System.err.println("the maximum capacity of bin n°" + index + " is exceeded");
                    return -1;  //return -1 if the maximum capacity is exceeded
                }
            }
        }

        System.err.println("here are the bins used: "+map.keySet());
        return map.size();  //return the number of bins used
    }

    public static void main(String[] args) {
        System.out.println("Objective SetCovering");
        String constrName = "BinPacking(List<Integer>, int, int)";

        //list of items
        List<Integer> l = Arrays.asList(1, 10, 5, 4, 9, 6, 5, 8);
        BinPacking obj = new BinPacking(l, 15);

        System.out.println("using constructor " + constrName);
        System.out.println(obj.toString());
        System.out.println("Items: " + obj.getItems());
        Data D = obj.solutionSample();
        System.out.println("sample solution : " + D);
        System.out.println("objective function value in sample solution : " + obj.value(D));
    }
}
