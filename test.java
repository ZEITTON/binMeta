import java.rmi.ServerError;
import java.util.*;

public class test implements Objective
{
    //Attribute
    private String name;
    private List<Integer> pi;   //LISTE DES VALEURS DES ITEMS i
    private List<Integer> wi;  //LISTE DES POIDS DES ITEMS i
    private int W;  //CAPACITER MAX DU SAC

    public test(List<Integer> pi, List<Integer> wi, int w)
    {
        try
        {
            if(pi == null) throw new Exception("Knapscak: specified list of values is null");
            if(pi.isEmpty()) throw new Exception("Knapscak: specified list of values is empty");
            if(Collections.min(pi) < 0.0) throw new Exception("Knapscak: specified list of values contains nonpositive elements");

            if(wi == null) throw new Exception("Knapscak: specified list of weight is null");
            if(wi.isEmpty()) throw new Exception("Knapscak: specified list of weight is empty");
            if(Collections.min(wi) < 0.0) throw new Exception("Knapscak: specified list of weight contains nonpositive elements");

            if (pi.size() != wi.size()) throw new Exception("Knapsack: specified lists differ in length");

            this.pi = new ArrayList<Integer> (pi);
            this.wi = new ArrayList<Integer> (wi);

            if (w <= 0.0) throw new Exception("Knapsack: specified maximum weight must be strictly positive");
            this.W = w;

            this.name = "Knapsack";
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public String getName() { return this.name; }

    @Override
    public Data solutionSample() { return new Data(this.wi.size(),0.5); }
    //Rend une liste random de bits (list length = wi length)

    @Override
    public double value(Data D)
    {
        try
        {
            String err = "Impossible to evaluate Knapsack objective: ";
            if (D == null) throw new Exception(err + "the Data object is null");
            if (D.numberOfBits() != this.wi.size()) throw new Exception(err + "unexpected bit string length in Data object");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }

        int sumW = 0;   //SOMME DES VALEURS
        int sumP = 0;   //SOMME DES POIDS

        //iterateur
        Iterator<Integer> itP = this.pi.iterator();
        Iterator<Integer> itW = this.wi.iterator();
        Data.bitIterator itD = D.iterator();

        //Calcul de la somme des valeurs de X
        while(itD.hasNext() && itP.hasNext() && itW.hasNext())
        {
            int bit = itD.next();
            int p = itP.next();
            int w = itW.next();

           // sumW += (bit * w); //quoi qu'il arrive on addition les poids selectionné
            //Car on doit le comptabilisé avant d'ajouté l'elmt dans le sac
            sumW += (bit * w);

            //on check si notre somme de poids est inf ou eq à notre capacite max du sac
            if(sumW <= this.W)
            {
                sumP += (bit * p); //ssi oui alors on ajoute l'elmt dans le sac
                if(bit != 0) {
                    System.err.println("value: ("+bit+" * "+p+") || poids: "+sumW);
                    System.err.println("Z = "+sumP);
                } //affichage
            } else{ System.err.println("Poids = " + sumW + " exces de +" + (sumW-W) + " (" + w + ")"); return sumP; } //On peux plus remplir le sac donc on stop
        }
        return sumP;
    }

    // main
    public static void main(String[] args)
    {
        System.out.println("Objective Knapsack");
        Random R = new Random();
        test obj = null;
        String constrName = null;
        int constructor = R.nextInt(2);
        if (constructor == 0)
        {
            int w1 = 15;
            constrName = "Knapsack(List<Integer>, List<Integer>, W: "+w1+")";
            ArrayList<Integer> values = new ArrayList<Integer> (10);
            ArrayList<Integer> weights = new ArrayList<Integer> (10);
            values.add(5);  weights.add(7);  // solution 27 with indices 0|(5), 1|(4), 2|(7), 7|(5), 8|(6) (weight is 15)
            values.add(4);  weights.add(2);  // solution 27 with indices 1|(4), 2|(7), 6|(3), 7|(5), 8|(6), 9|(2) (weight is 13)
            values.add(7);  weights.add(1);
            values.add(2);  weights.add(9);
            values.add(1);  weights.add(5);
            values.add(8);  weights.add(10);
            values.add(3);  weights.add(2);
            values.add(5);  weights.add(1);
            values.add(6);  weights.add(4);
            values.add(2);  weights.add(3);
            obj = new test(values,weights,w1);
        }
        else
        {
            int w2 = 27;
            constrName = "Knapsack(Integer[], Integer[], W: "+w2+")";
            Integer [] values = new Integer [] {1,4,5,8,1,3,9,7,12,8};
            Integer [] weights = new Integer [] {7,8,2,6,3,12,11,9,4,6};
            List<Integer> V = new ArrayList<>(Arrays.asList(values));
            List<Integer> W = new ArrayList<>(Arrays.asList(weights));
            obj = new test(V,W,w2);  // solution 37 with indices 1, 2, 3, 8, 9
        }
        System.out.println("using constructor " + constrName);
        System.out.println(obj);
        System.out.println("values of the elements : " + obj.getArrayListValues());
        System.out.println("weights of the elements : " + obj.getArrayListWeights());
        Data D = obj.solutionSample();
        System.out.println("sample solution : " + D);
        System.out.println("maxWeight : " + obj.W);
        System.out.println("objective function value in sample solution : " + obj.value(D));
    }

    // getArrayListValues (copy)
    public ArrayList<Integer> getArrayListValues()
    {
        return new ArrayList<Integer> (this.pi);
    }

    // getArrayListWeights (copy)
    public ArrayList<Integer> getArrayListWeights()
    {
        return new ArrayList<Integer> (this.wi);
    }

}
