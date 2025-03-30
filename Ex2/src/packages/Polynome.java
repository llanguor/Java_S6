package packages;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class Polynome
    implements
        Comparable<Polynome>,
        Cloneable
{

    //region Fields

    private final HashMap<Integer, Double> _polynomialTerms;

    //endregion

    //region Constructors

    public Polynome()
    {
        _polynomialTerms = new HashMap<>();
    }

    public Polynome(double... coefficients)
    {
        this();
        for(int i = 0; i < coefficients.length; ++i)
        {
            _polynomialTerms.put(i, coefficients[i]);
        }
    }

    //In java, ref for all variables?
    public Polynome(HashMap<Integer, Double> polynomialTerms)
    {
        _polynomialTerms = new HashMap<>(polynomialTerms);
    }

    public Polynome(Polynome polynome)
    {
        this(polynome._polynomialTerms);
    }

    //endregion

    //region Methods get set

    public void putCoefficient(int degree, double value)
    {
        _polynomialTerms.put(degree, value);
    }

    public void addCoefficient(int degree, double value)
    {
        _polynomialTerms.put(
                degree,
                _polynomialTerms.getOrDefault(degree, 0.0) + value
        );
    }

    public double getСoefficient(int degree)
    {
        return _polynomialTerms.get(degree);
    }

    public Integer getHighestDegree()
    {
        return Polynome.getHighestDegree(_polynomialTerms, null);
    }

    private static Integer getHighestDegree(HashMap<Integer, Double> hashMap)
    {
        return Polynome.getHighestDegree(hashMap, null);
    }

    private static Integer getHighestDegree(HashMap<Integer, Double> hashMap, Integer returnIfEmpty)
    {
        //Return null if HashSet is empty
        //Else find max value in ketSet
        return hashMap.keySet().stream().max(Integer::compare).orElse(returnIfEmpty);
    }

    //endregion


    //region Methods static

    public static Polynome addition(Polynome polynome1, Polynome polynome2)
    {
        var p = new Polynome();
        performPolynomeOperation(polynome1, polynome2, p, Double::sum);
        return p;
    }

    public static Polynome subtract(Polynome polynome1, Polynome polynome2)
    {
        var p = new Polynome();
        performPolynomeOperation(polynome1, polynome2, p, (x, y) -> x - y);
        return p;
    }

    public static Polynome multipleByNumber(Polynome polynome, double number)
    {
        var p = new Polynome();
        performPolynomeConstant(polynome, p, (a) -> a * number);
        return p;
    }

    public static Polynome multiply(Polynome polynome1, Polynome polynome2)
    {
        var outputPolynome = new Polynome();
        for (var entry1 : polynome1._polynomialTerms.entrySet())
        {
            for (var entry2 : polynome2._polynomialTerms.entrySet())
            {
                int newExponent = entry1.getKey() + entry2.getKey();
                double newCoefficient = entry1.getValue() * entry2.getValue();

                outputPolynome._polynomialTerms.put(
                        newExponent,
                        outputPolynome
                                ._polynomialTerms
                                .getOrDefault(newExponent, 0.0)
                                + newCoefficient
                );
            }
        }
        return outputPolynome;
    }


    public static Pair<Polynome, Polynome> divide(Polynome dividend, Polynome divisor) throws CloneNotSupportedException
    {
        HashMap<Integer, Double> quotient = new HashMap<>();                            //Частное от деления
        HashMap<Integer, Double> remainder = new HashMap<>(dividend._polynomialTerms);  //Остаток

        Integer divisorLeadingDegree = divisor.getHighestDegree();
        if (divisorLeadingDegree == null)
            return new Pair<>(new Polynome(), dividend);

        Double divisorLeadingCoefficient = divisor._polynomialTerms.get(divisorLeadingDegree);
        if (divisorLeadingCoefficient == null)
            return new Pair<>(new Polynome(), dividend);



        while (!remainder.isEmpty() &&
                Polynome.getHighestDegree(remainder, -1) >= divisorLeadingDegree)  //return -1 if keySet is clear
        {
            Integer leadingDegree = getHighestDegree(remainder);
            if (leadingDegree == null)
                break;

            Double leadCoeff = remainder.get(leadingDegree);
            if (leadCoeff == null)
                break;

            int termDegree = leadingDegree - divisorLeadingDegree;
            double termCoefficient = leadCoeff / divisorLeadingCoefficient;
            quotient.put(termDegree, termCoefficient);


            for (var entry : divisor._polynomialTerms.entrySet())
            {
                int subDegree = termDegree + entry.getKey();

                remainder.put(
                        subDegree,
                        remainder.getOrDefault(subDegree, 0.0)
                                - termCoefficient * entry.getValue());

                if (remainder.get(subDegree) == 0.0)
                    remainder.remove(subDegree);
            }
        }


        return new Pair<>(new Polynome(quotient), new Polynome(remainder));
    }

    //endregion

    //region Methods private

    private static void performPolynomeConstant(Polynome inputPolynome, Polynome outputPolynome, Function<Double, Double> operation)
    {
        for(var entry : inputPolynome._polynomialTerms.entrySet())
        {
            double result = operation.apply(entry.getValue());
            outputPolynome._polynomialTerms.put(entry.getKey(), result);
        }
    }

    private static void performPolynomeOperation(Polynome polynome1, Polynome polynome2, Polynome outputPolynome, BiFunction<Double, Double, Double> operation)
    {
        var keyList = getKeysUnionSet(
                polynome1._polynomialTerms.keySet(),
                polynome2._polynomialTerms.keySet());

        for (var key: keyList)
        {
            double currentPolynomeValue = polynome1._polynomialTerms.getOrDefault(key, 0.0);
            double comparePolynomeValue = polynome2._polynomialTerms.getOrDefault(key, 0.0);

            var result = operation.apply(currentPolynomeValue, comparePolynomeValue);
            outputPolynome._polynomialTerms.put(key, result);
        }
    }

    //TreeSet using for unique + sorting
    @SafeVarargs
    private static TreeSet<Integer> getKeysUnionSet(Set<Integer>... sets)
    {
        var keySet = new TreeSet<Integer>();
        for(var set : sets)
        {
            keySet.addAll(set);
        }
        return keySet;
    }


    //endregion

    //region Methods

    public void addition(Polynome polynome)
    {
        performPolynomeOperation(this, polynome, this, Double::sum);
    }

    public void subtract(Polynome polynome)
    {
        performPolynomeOperation(this, polynome, this, (x, y) -> x - y);
    }

    public void multiplyByNumber(double number)
    {
        _polynomialTerms.replaceAll((key, value) -> value * number);
    }

    public void multiply(Polynome polynome)
    {
        var result = Polynome.multiply(this, polynome)._polynomialTerms;
        _polynomialTerms.clear();
        _polynomialTerms.putAll(result);
    }

    public void divide(Polynome polynome) throws CloneNotSupportedException
    {
        var result = Polynome.divide(this, polynome).first._polynomialTerms;
        _polynomialTerms.clear();
        _polynomialTerms.putAll(result);
    }

    //endregion


    //region Nested

    public static class Pair<T1, T2>
    {
        public final T1 first;
        public final T2 second;

        public Pair(T1 first, T2 second) {
            this.first = first;
            this.second = second;
        }
    }

    //endregion

    //region Override

    @Override
    public int compareTo(Polynome comparePolynome)
    {
        var keySet = getKeysUnionSet(
                _polynomialTerms.keySet(),
                comparePolynome._polynomialTerms.keySet());

        for(var key : keySet.descendingSet())
        {
            double currentPolynomeValue = this._polynomialTerms.getOrDefault(key, 0.0);
            double comparePolynomeValue = comparePolynome._polynomialTerms.getOrDefault(key, 0.0);

            if(currentPolynomeValue!=comparePolynomeValue)
                return Double.compare(currentPolynomeValue, comparePolynomeValue);
        }

        return 0;
    }

    @Override
    public Polynome clone() throws CloneNotSupportedException
    {
        return (Polynome) super.clone();
    }

    @Override
    public String toString()
    {
        if(_polynomialTerms.isEmpty())
            return "0.0";

        StringBuilder sb = new StringBuilder();
        for(var entry : _polynomialTerms.entrySet())
        {
            var value = entry.getValue();
            if(value == 0) continue;

            sb.insert(0, entry.getKey())
                    .insert(0, "*x^")
                    .insert(0, Math.abs(value))
                    .insert(0, value<0? " - " : " + ");
        }

        var lastSeparator = sb.substring(0,3);
        sb.delete(0,3);
        if(lastSeparator.equals(" - "))
        {
            sb.insert(0, "-");
        }

        return sb.toString();
    }

    //endregion

}
