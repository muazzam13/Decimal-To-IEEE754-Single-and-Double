import java.math.BigDecimal;
import java.util.Scanner;

/**
 * Main class which contains the converter
 */
public class DecToIEEE {

    /**
     * Scanner object which is declared globally for this class to allow reuse of code
     */
    public static Scanner input = new Scanner(System.in);

    /**
     * Main method of the entire converter
     * @param args
     */
    public static void main(String[] args) {
        boolean checker = false;
        while (!checker)
        {
            menu();
            int inputNumber = input.nextInt();
            switch(inputNumber)
            {
                case 1:
                {
                    String output = decimalToIEEE754Single();
                    System.out.println();
                    System.out.println("The decimal in IEEE-754 Single Precision is: "+ output);
                    System.out.println();

                    //To display in an easily readable format
                    String[] explanationSplit = output.split(" ");
                    System.out.println("Sign (1 bit): "+ explanationSplit[0]);
                    System.out.println("Exponent (8 bits): "+ explanationSplit[1]);
                    System.out.println("Mantissa (23 bits): "+ explanationSplit[2]);
                    break;
                }
                case 2:
                {
                    String output2 = decimalToIEEE754Double();
                    System.out.println();
                    System.out.println("The decimal in IEEE-754 Double Precision is: "+ output2);
                    System.out.println();

                    //To display in an easily readable format
                    String[] explanationSplit = output2.split(" ");
                    System.out.println("Sign (1 bit): "+ explanationSplit[0]);
                    System.out.println("Exponent (11 bits): "+ explanationSplit[1]);
                    System.out.println("Mantissa (52 bits) : "+ explanationSplit[2]);
                    break;
                }
                case 3:
                {
                    System.out.println("Goodbye");
                    checker = true;
                    break;
                }
                default:
                {
                    System.out.println("Wrong input please enter the option number");
                    break;
                }
            }
            System.out.println();

        }
    }

    /**
     * Method to show the menu
     */
    public static void menu()
    {
        System.out.println("1) Decimal to IEEE-754 single precision");
        System.out.println("2) Decimal to IEEE-754 double precision");
        System.out.print("3) Quit\nPlease enter select an option number: ");
    }

    /**
     * Method to get the decimal input from the user
     * @return decimal which is to be converted
     */
    public static Double inputGetter()
    {
        System.out.print("Please enter a Decimal: ");
        double decimal = input.nextDouble();
        return decimal;
    }

    /**
     * Converts from decimal to IEEE754 Single Precision
     * @return converted IEEE754 Single Precision in a string form
     */
    public static String decimalToIEEE754Single()
    {
        Double binaryNum = inputGetter();
        String []x;
        if(binaryNum < 0)
        {
            //Removing the negative sign
            String [] negRemover = String.valueOf(binaryNum).split("\\-");
            //Input splitting into 2 before and after decimal place
            x = negRemover[1].split("\\.");
        }
        else
        {
            //Input splitting into 2 before and after decimal place
            x = String.valueOf(binaryNum).split("\\.");
        }

        int beforePoint = Integer.parseInt(x[0]);
        int afterPoint = Integer.parseInt(x[1]);


        System.out.println();
        //conversion of the before point int into binary using built in converter for ease of use
        System.out.println("Integer part Conversion to Binary");
        String bPoint = integerSideToBinary(beforePoint);


        //the following code from line 123 -131 is done to set up the fractional side of the value received
        int length = String.valueOf(afterPoint).length();
        double correctDecPlace =1.00;
        for(int u = 0; u<length;u++)
        {
            correctDecPlace *= 10.00;
        }

        double fracNumber = afterPoint/correctDecPlace;
        boolean runner = false;
        StringBuilder storage = new StringBuilder();
        storage.append(bPoint);
        storage.append(".");
        int tester = 0;

        System.out.println();
        System.out.println("Fractional Part Conversion to Binary");
        System.out.println("This process goes on until the fractional part of the output is 0 or until 31 iterations");
        System.out.println("#multiplication = integer + fractional part");
        while(tester != 31 && runner != true)
        {
            double z = fracNumber *2;
            String [] spliter = String.valueOf(z).split("\\.");
            int b = Integer.parseInt(spliter[0]);
            int a = Integer.parseInt(spliter[1]);
            System.out.println(fracNumber + " * 2 = "+ b +" + "+ a);
            storage.append(b);

            if (a == 0)
            {
                runner = true;
                break;
            }

            //following code allows to make sure the next number is in correct position respect to the decimal place
            int l = String.valueOf(a).length();
            double correctDecPlace2 =1.00;
            for(int u = 0; u<l;u++)
            {
                correctDecPlace2 *= 10.00;
            }
            fracNumber = a/correctDecPlace2;
            ++tester;
        }

        int exp = bPoint.length()-1;
        int expB = 127 +exp;
        System.out.println();
        System.out.println("Adjusted Exponent: (127 + unadjusted exponent): "+expB);
        System.out.println("Exponent Conversion to Binary");
        String exponent =integerSideToBinary(expB);
        System.out.println("Exponent in Binary: "+exponent);


        // done to move the decimal the correct places in the mantissa
        String temp = storage.toString();
        String [] j = temp.split("\\.");
        int lengthOFMantissaBeforePoint = j[0].length();
        double correctDecPlace1 =1.00000000;
        for(int u = 0; u<lengthOFMantissaBeforePoint-1.00000000;u++)
        {
            correctDecPlace1 *= 10.00000000;
        }
        String correctDecPlace1ToString = "" + correctDecPlace1;

        //Big Decimal used for precision
        BigDecimal d = new BigDecimal(temp);
        BigDecimal divisor = new BigDecimal(correctDecPlace1ToString);
        BigDecimal xc = d.divide(divisor);

        StringBuilder mantissaResizer = new StringBuilder();
        mantissaResizer.append(xc);
        System.out.println();
        if(mantissaResizer.length()>25)
        {
            mantissaResizer.delete(25, mantissaResizer.length());
            System.out.println("Mantissa was decreased to the size of 23 bits");
        }
        else if (mantissaResizer.length() < 25)
        {
            while(mantissaResizer.length() < 25) {
                mantissaResizer.append(0);
            }
            System.out.println("Mantissa was increased to the size of 23 bits");
        }
        String finalSring = mantissaResizer.toString();

        System.out.println("Mantissa in scientific notation: " + finalSring +"*2^" +(lengthOFMantissaBeforePoint-1));



        //Combining everything
        StringBuilder finalStringBuilder = new StringBuilder();
        if(binaryNum >= 0)
        {
            finalStringBuilder.append(0);
        }
        else
        {
            finalStringBuilder.append(1);
        }
        finalStringBuilder.append(" ");
        finalStringBuilder.append(exponent);
        finalStringBuilder.append(" ");

        //normalizing the mantissa
        String []trueFinalString = finalSring.split("\\.");
        finalStringBuilder.append(trueFinalString[1]);
        System.out.println("Mantissa normalised: "+ trueFinalString[1]);
        String singlePrecBinary = finalStringBuilder.toString();
        return singlePrecBinary;
    }

    /**
     * Method to convert from decimal to IEEE754 Double Precision
     * @return converted IEEE754 Double Precision in a string form
     */
    public static String decimalToIEEE754Double()
    {
        Double binaryNum = inputGetter();
        String []x;
        if(binaryNum < 0)
        {
            // Removing the negative sign
            String [] negRemover = String.valueOf(binaryNum).split("\\-");
            //Input splitting into 2 before and after decimal place
            x = negRemover[1].split("\\.");
        }
        else
        {
            //Input splitting into 2 before and after decimal place
            x = String.valueOf(binaryNum).split("\\.");
        }

        int beforePoint = Integer.parseInt(x[0]);
        int afterPoint = Integer.parseInt(x[1]);
        System.out.println();

        //conversion of the before point int into binary using built in converter for ease of use
        System.out.println("Integer part Conversion to Binary");
        String bPoint = integerSideToBinary(beforePoint);


        //the following code from line 267 -274 is done to set up the fractional side of the value received
        int length = String.valueOf(afterPoint).length();
        double correctDecPlace =1.00;
        for(int u = 0; u<length;u++)
        {
            correctDecPlace *= 10.00;
        }

        double fracNumber = afterPoint/correctDecPlace;
        boolean runner = false;
        StringBuilder storage = new StringBuilder();
        storage.append(bPoint);
        storage.append(".");
        int tester = 0;

        System.out.println();
        System.out.println("Fractional Part Conversion to Binary");
        System.out.println("This process goes on until the fractional part of the output is 0 or until 64 iterations");
        System.out.println("#multiplication = integer + fractional part");
        while(tester != 64 && runner != true)
        {
            double z = fracNumber *2;
            String [] spliter = String.valueOf(z).split("\\.");
            int b = Integer.parseInt(spliter[0]);
            int a = Integer.parseInt(spliter[1]);
            System.out.println(fracNumber + " * 2 = "+ b +" + "+ a);
            storage.append(b);

            if (a == 0)
            {
                runner = true;
                break;
            }

            //following code allows to make sure the next number is in correct position respect to the decimal place
            int l = String.valueOf(a).length();
            double correctDecPlace2 =1.00;
            for(int u = 0; u<l;u++)
            {
                correctDecPlace2 *= 10.00;
            }
            fracNumber = a/correctDecPlace2;
            ++tester;
        }

        int exp = bPoint.length()-1;
        int expB = exp + (int)(Math.pow(2.00,10.00)-1);
        System.out.println();
        System.out.println("Adjusted Exponent: Unadjusted Exponent +(2^11-1) -1: "+expB);
        System.out.println("Exponent Conversion to Binary");
        String exponent =integerSideToBinary(expB);
        System.out.println("Exponent in Binary: "+exponent);


        // done to move the decimal the correct places in the mantissa
        String temp = storage.toString();
        String [] j = temp.split("\\.");
        int lengthOFMantissaBeforePoint = j[0].length();
        double correctDecPlace1 =1.00000000;
        for(int u = 0; u<lengthOFMantissaBeforePoint-1.00000000;u++)
        {
            correctDecPlace1 *= 10.00000000;
        }
        String correctDecPlace1ToString = "" + correctDecPlace1;

        //Big Decimal used for precision
        BigDecimal d = new BigDecimal(temp);
        BigDecimal divisor = new BigDecimal(correctDecPlace1ToString);
        BigDecimal xc = d.divide(divisor);

        StringBuilder mantissaResizer = new StringBuilder();
        mantissaResizer.append(xc);
        System.out.println();


        if(mantissaResizer.length()>53)
        {
            mantissaResizer.delete(53, mantissaResizer.length());
            System.out.println("Mantissa was decreased to the size of 52 bits");
        }
        else if (mantissaResizer.length() < 53)
        {
            while(mantissaResizer.length()!=53)
            {
                mantissaResizer.append(0);
            }
            System.out.println("Mantissa was increased to the size of 52 bits");
        }
        String finalSring = mantissaResizer.toString();

        System.out.println("Mantissa in scientific notation: " + finalSring +"*2^" +(lengthOFMantissaBeforePoint-1));



        //Combining everything
        StringBuilder finalStringBuilder = new StringBuilder();
        if(binaryNum >= 0)
        {
            finalStringBuilder.append(0);
        }
        else
        {
            finalStringBuilder.append(1);
        }
        finalStringBuilder.append(" ");
        finalStringBuilder.append(exponent);
        finalStringBuilder.append(" ");

        //normalizing the mantissa
        String []trueFinalString = finalSring.split("\\.");
        finalStringBuilder.append(trueFinalString[1]);
        System.out.println("Mantissa normalised: "+ trueFinalString[1]);
        String singlePrecBinary = finalStringBuilder.toString();
        return singlePrecBinary;
    }

    /**
     * This converts the left side(integer side) of an decimal the program recieves
     * @param "A" is the left/integer side value which needs to be converted
     * @return The converted value in string form
     */
    public static String integerSideToBinary(int a)
    {
        StringBuilder z = new StringBuilder();
        System.out.println("This process occurs until the quotient becomes 0");
        System.out.println("#division = quotient + remainder");
        while (a>0)
        {

            int k = a%2;
            System.out.println(a+" ÷ 2 = " +(a/2)+ " + "+k);
            a= a/2;
            z.append(k);
        }

        z.reverse();

        return z.toString();
    }

}
