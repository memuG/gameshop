package testutils;

public final class TestUtils {
    public static String generateGivenLengthXString(final int length) {
        char[] generatedCharArray = new char[length];
        for (int i =0; i < length; ++i)
            generatedCharArray[i] = 'X';
        return String.valueOf(generatedCharArray);
    }
}
