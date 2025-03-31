package packages;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class Polynomial
    implements
        Comparable<Polynomial>,
        Cloneable
{

    //region Fields

    private final HashMap<Integer, Double> _polynomialTerms;

    //endregion

    //region Constructors

    public Polynomial()
    {
        _polynomialTerms = new HashMap<>();
    }

    public Polynomial(double... coefficients)
    {
        this();
        for(int i = 0; i < coefficients.length; ++i)
        {
            _polynomialTerms.put(i, coefficients[i]);
        }
    }

    //In java, ref for all variables
    public Polynomial(HashMap<Integer, Double> polynomialTerms)
    {
        _polynomialTerms = new HashMap<>(polynomialTerms);
    }

    public Polynomial(Polynomial polynomial)
    {
        this(polynomial._polynomialTerms);
    }

    //endregion

    //region Methods get set

    public void putCoefficient(int degree, double value)
            throws IllegalArgumentException
    {
        if(degree<0)
            throw new IllegalArgumentException("Degree must be more than 0");

        _polynomialTerms.put(degree, value);
    }

    public void addCoefficient(int degree, double value)
            throws IllegalArgumentException
    {
        if(degree<0)
            throw new IllegalArgumentException("Degree must be more than 0");

        _polynomialTerms.put(
                degree,
                _polynomialTerms.getOrDefault(degree, 0.0) + value
        );
    }

    public double getCoefficient(int degree)
            throws IllegalArgumentException
    {
        if(degree<0)
            throw new IllegalArgumentException("Degree must be more than 0");

        return _polynomialTerms.get(degree);
    }

    public Integer getHighestDegree()
    {
        return Polynomial.getHighestDegree(_polynomialTerms);
    }

    private static Integer getHighestDegree(HashMap<Integer, Double> hashMap)
    {
        return Polynomial.getHighestDegree(hashMap, null);
    }

    private static Integer getHighestDegree(HashMap<Integer, Double> hashMap, Integer returnIfEmpty)
    {
        //Return null if HashSet is empty
        //Else find max value in ketSet
        return hashMap.keySet().stream().max(Integer::compare).orElse(returnIfEmpty);
    }

    //endregion


    //region Methods static

    public static Polynomial addition(Polynomial polynomial1, Polynomial polynomial2)
            throws IllegalArgumentException
    {
        if(polynomial1 == null || polynomial2 == null)
            throw new IllegalArgumentException("The input polynomial was null");

        var p = new Polynomial();
        performPolynomialOperation(polynomial1, polynomial2, p, Double::sum);
        return p;
    }

    public static Polynomial subtract(Polynomial polynomial1, Polynomial polynomial2)
            throws IllegalArgumentException
    {
        if(polynomial1 == null || polynomial2 == null)
            throw new IllegalArgumentException("The input polynomial was null");

        var p = new Polynomial();
        performPolynomialOperation(polynomial1, polynomial2, p, (x, y) -> x - y);
        return p;
    }

    public static Polynomial multipleByNumber(Polynomial polynomial, double number)
            throws IllegalArgumentException
    {
        if(polynomial == null)
            throw new IllegalArgumentException("The input polynomial was null");

        var p = new Polynomial();
        performPolynomialConstant(polynomial, p, (a) -> a * number);
        return p;
    }

    public static Polynomial multiply(Polynomial polynomial1, Polynomial polynomial2)
            throws IllegalArgumentException
    {
        if(polynomial1 == null || polynomial2 == null)
            throw new IllegalArgumentException("The input polynomial was null");

        var outputPolynomial = new Polynomial();
        for (var entry1 : polynomial1._polynomialTerms.entrySet())
        {
            for (var entry2 : polynomial2._polynomialTerms.entrySet())
            {
                int newExponent = entry1.getKey() + entry2.getKey();
                double newCoefficient = entry1.getValue() * entry2.getValue();

                outputPolynomial._polynomialTerms.put(
                        newExponent,
                        outputPolynomial
                                ._polynomialTerms
                                .getOrDefault(newExponent, 0.0)
                                + newCoefficient
                );
            }
        }
        return outputPolynomial;
    }


    public static DivideResultPair<Polynomial, Polynomial> divide(Polynomial dividend, Polynomial divisor)
            throws IllegalArgumentException
    {
        if(dividend == null || divisor == null)
            throw new IllegalArgumentException("The input polynomial was null");

        HashMap<Integer, Double> quotient = new HashMap<>();                            //Частное от деления
        HashMap<Integer, Double> remainder = new HashMap<>(dividend._polynomialTerms);  //Остаток

        Integer divisorLeadingDegree = divisor.getHighestDegree();
        Double divisorLeadingCoefficient = divisor._polynomialTerms.get(divisorLeadingDegree);

        if (divisorLeadingDegree == null || divisorLeadingCoefficient == null)
            return new DivideResultPair<>(new Polynomial(), dividend);


        while (!remainder.isEmpty() &&
                Polynomial.getHighestDegree(remainder, -1) >= divisorLeadingDegree)  //return -1 if keySet is clear
        {
            Integer leadingDegree = getHighestDegree(remainder);
            Double leadCoefficient = remainder.get(leadingDegree);
            if (leadingDegree == null || leadCoefficient == null)
                break;

            int termDegree = leadingDegree - divisorLeadingDegree;
            double termCoefficient = leadCoefficient / divisorLeadingCoefficient;
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

        return new DivideResultPair<>(new Polynomial(quotient), new Polynomial(remainder));
    }

    //endregion

    //region Methods private

    private static void performPolynomialConstant(
            Polynomial inputPolynomial,
            Polynomial outputPolynomial,
            Function<Double, Double> operation)
            throws  IllegalArgumentException
    {
        if(inputPolynomial == null)
            throw new IllegalArgumentException("The input polynomial was null");

        if(outputPolynomial==null)
            outputPolynomial=new Polynomial();
        else
            outputPolynomial.clearValues();

        for(var entry : inputPolynomial._polynomialTerms.entrySet())
        {
            double result = operation.apply(entry.getValue());
            outputPolynomial._polynomialTerms.put(entry.getKey(), result);
        }
    }

    private static void performPolynomialOperation(
            Polynomial polynomial1,
            Polynomial polynomial2,
            Polynomial outputPolynomial,
            BiFunction<Double, Double,
                    Double> operation)
            throws IllegalArgumentException
    {
        if(polynomial1 == null || polynomial2 == null)
            throw new IllegalArgumentException("The input polynomial was null");

        if(outputPolynomial==null)
            outputPolynomial=new Polynomial();
        else
            outputPolynomial.clearValues();

        var keyList = getKeysUnionSet(
                polynomial1._polynomialTerms.keySet(),
                polynomial2._polynomialTerms.keySet());

        for (var key: keyList)
        {
            double currentPolynomialValue = polynomial1._polynomialTerms.getOrDefault(key, 0.0);
            double comparePolynomialValue = polynomial2._polynomialTerms.getOrDefault(key, 0.0);

            var result = operation.apply(currentPolynomialValue, comparePolynomialValue);
            outputPolynomial._polynomialTerms.put(key, result);
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

    public void clearValues()
    {
        _polynomialTerms.clear();
    }

    public void addition(Polynomial polynomial)
            throws IllegalArgumentException
    {
        if(polynomial==null)
            throw new IllegalArgumentException("The input polynomial was null");

        performPolynomialOperation(this, polynomial, this, Double::sum);
    }

    public void subtract(Polynomial polynomial)
            throws IllegalArgumentException
    {
        if(polynomial==null)
            throw new IllegalArgumentException("The input polynomial was null");

        performPolynomialOperation(this, polynomial, this, (x, y) -> x - y);
    }

    public void multiplyByNumber(double number)
    {
        _polynomialTerms.replaceAll((key, value) -> value * number);
    }

    public void multiply(Polynomial polynomial)
            throws IllegalArgumentException
    {
        if(polynomial==null)
            throw new IllegalArgumentException("The input polynomial was null");

        var result = multiply(this, polynomial)._polynomialTerms;
        _polynomialTerms.clear();
        _polynomialTerms.putAll(result);
    }

    public void divide(Polynomial polynomial)
            throws IllegalArgumentException
    {
        if(polynomial==null)
            throw new IllegalArgumentException("The input polynomial was null");

        var result = divide(this, polynomial).quotient._polynomialTerms;
        _polynomialTerms.clear();
        _polynomialTerms.putAll(result);
    }

    //endregion


    //region Nested

    public record DivideResultPair<T1, T2>(T1 quotient, T2 remainder)
        {
            @Override
            public String toString()
            {
                return String.format("quotient: %s; remainder: %s",quotient, remainder);
            }
        }

    /* ^ It's record-class. Like:
    public static class Pair<T1, T2>
    {
        public final T1 first;
        public final T2 second;

        public Pair(T1 first, T2 second)
        {
            this.first = first;
            this.second = second;
        }
    }
    */

    //endregion

    //region Override

    @Override
    public int compareTo(Polynomial comparePolynomial)
            throws IllegalArgumentException
    {
        if(comparePolynomial==null)
            throw new IllegalArgumentException("The input polynomial was null");

        var keySet = getKeysUnionSet(
                _polynomialTerms.keySet(),
                comparePolynomial._polynomialTerms.keySet());

        for(var key : keySet.descendingSet())
        {
            double currentPolynomialValue = this._polynomialTerms.getOrDefault(key, 0.0);
            double comparePolynomialValue = comparePolynomial._polynomialTerms.getOrDefault(key, 0.0);

            if(currentPolynomialValue!=comparePolynomialValue)
                return Double.compare(currentPolynomialValue, comparePolynomialValue);
        }

        return 0;
    }

    @Override
    public Polynomial clone()
            throws CloneNotSupportedException
    {
        return (Polynomial) super.clone();
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
