public class TesterUtils {
    private TesterUtils() {}

    public static boolean arraysIdentical(int[] arr1, int[] arr2)
    {
        if (arr1.length != arr2.length)
        {
            return false;
        }
        for (int j=0; j<arr1.length; j++)
        {
            if (arr1[j] != arr2[j])
            {
                return false;
            }
        }
        return true;
    }

    public static int[] stringToInt(String[] arr)
    {
        int[] arr2 = new int[arr.length];
        for (int i=0 ; i<arr2.length ; i++)
        {
            arr2[i]=Integer.parseInt(arr[i]);
        }
        return arr2;
    }


    public static int intValue(String str)
    {
        if( str == null)
            return -1;
        else
            return Integer.parseInt(str);
    }



    public static int printStatus(SuccessStatus[] success, int actualOperations, int wavlOperations) {
        return printStatus(success,actualOperations,wavlOperations,true);
    }

    public static int printStatus(SuccessStatus[] success, int actualOperations, int wavlOperations, boolean verbose) {
        int passed = 0;
        int exceptions = 0;
        String exceptionList = "";
        String passedList = "";
        String failedList = "";

        for (int i = 0; i < success.length; i++) {
            SuccessStatus succes = success[i];
            passed += succes == SuccessStatus.PASS ? 1 : 0;
            exceptions += succes == SuccessStatus.EXCEPTION ? 1 : 0;
            exceptionList += succes == SuccessStatus.EXCEPTION ? " " + i : "";
            passedList += succes == SuccessStatus.PASS ? " " + i : "";
            failedList += succes == SuccessStatus.FAIL ? " " + i : "";
        }

        System.out.println("============================================");
        System.out.println("==              Run Results:              ==");
        System.out.println("==  ==  ==  ==  ==  ==  ==  ==  ==  ==  ====");
        System.out.println("== Total Runs: " + success.length);
        System.out.println("== Passed Runs: " + passed);
        System.out.println("== Failed Runs: " + (success.length - passed));
        System.out.println("== Of which are exceptions: " + exceptions);
        System.out.println("== Exceptions occurred in cases: " + exceptionList);
        if (verbose) {
            System.out.println("== The following cases passed: " + passedList);
            System.out.println("== The following cases failed: " + failedList);

        }
		return 100-(5*(success.length - passed));

    }
}
