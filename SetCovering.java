import java.util.*;

public class SetCovering implements Objective
{
    //Attributes
    private String name;
    private List<Integer> elements; //list of all elements
    private List<Set<Integer>> subsets; //list of subsets of List<Integer> elements

    //Constructor
    public SetCovering(List<Integer> elements, List<Set<Integer>> subsets) {
        try
        {
            if (elements == null) throw new Exception("SetCovering: specified list of elements is null");
            if (elements.isEmpty()) throw new Exception("SetCovering: specified list of elements object is empty");
            this.elements = new ArrayList<>(elements);

            if (subsets == null) throw new Exception("SetCovering: specified list of subset is null");
            if (subsets.isEmpty()) throw new Exception("SetCovering: specified list of subset is empty");
            if (subsets.size() <= 1) throw new Exception("SetCovering: specified list of subset contains only one subset");
            this.subsets = new ArrayList<>(subsets);

            this.name = "SetCovering";
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }

    //Getters
    public List<Integer> getU() { return this.elements; }
    public List<Set<Integer>> getS() { return this.subsets; }

    //toString
    public String toString()
    {
        return "[" + this.getName() + ": " + this.elements.size() + " elements, " + this.subsets.size() + " subsets]";
    }


    /* ====================== implements method of objectives ====================== */

    @Override
    public String getName() { return this.name; }

    @Override
    public Data solutionSample() { return new Data(this.subsets.size(), 0.5); }

    @Override
    public double value(Data D)
    {
        try
        {
            String err = "Impossible to evaluate SetCovering objective: ";
            if (D == null) throw new Exception(err + "the Data object is null");
            if (D.numberOfBits() != this.subsets.size()) throw new Exception(err + "unexpected bit string length in Data object");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }

        //attribute
        Set<Integer> covered = new HashSet<>(); //a set to put all the elements covered
        int c = 0; //subset counter

        //iterators
        Iterator<Set<Integer>> it1 = subsets.iterator();
        Data.bitIterator it2 = D.iterator();

        //add the elements of the subset in "covered" if they are selected by solutionSample
        while (it1.hasNext() && it2.hasNext())
        {
            int bit = it2.next();
            Set<Integer> s = it1.next();

            //we add as long as all the elements are not covered
            if(bit == 1 && !covered.containsAll(this.elements)){
                covered.addAll(s);
                c++;
            }

        }

        //check that all elements are covered
        if(covered.containsAll(this.elements))
            return c; //returns the number of subsets used to cover all elements
        else
            return -1; //return -1 when all elements are not covered
    }

    public static void main(String[] args) {
        System.out.println("Objective SetCovering");
        String constrName = "SetCovering(List<Integer>, List<Set<Integer>>)";

        //set of all of elements
        List<Integer> lU = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        //Subsets
        Set<Integer> s1 = new HashSet<>(Arrays.asList(1, 3, 4, 6, 7));
        Set<Integer> s2 = new HashSet<>(Arrays.asList(2, 5, 8, 9, 10));
        Set<Integer> s3 = new HashSet<>(Arrays.asList(1, 2, 6, 7, 10));
        Set<Integer> s4 = new HashSet<>(Arrays.asList(3, 4, 7, 8, 9));
        Set<Integer> s5 = new HashSet<>(Arrays.asList(5, 6, 7, 9, 10));
        Set<Integer> s6 = new HashSet<>(Arrays.asList(2, 3, 4, 5));
        Set<Integer> s7 = new HashSet<>(Arrays.asList(1, 3, 5, 6, 8, 10));
        Set<Integer> s8 = new HashSet<>(Arrays.asList(4, 7, 8, 9));
        List<Set<Integer>> lS = Arrays.asList(s1, s2, s3, s4, s5, s6 , s7, s8);

        SetCovering obj = new SetCovering(lU, lS);

        System.out.println("using constructor " + constrName);
        System.out.println(obj.toString());
        System.out.println("Elements: " + obj.getU());
        System.out.println("Subsets: " + obj.getS());
        Data D = obj.solutionSample();
        System.out.println("sample solution : " + D);
        System.out.println("objective function value in sample solution : " + obj.value(D));
    }
}
