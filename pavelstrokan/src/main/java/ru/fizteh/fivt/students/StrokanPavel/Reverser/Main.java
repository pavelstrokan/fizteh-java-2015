package ru.fizteh.fivt.students.StrokanPavel.Reverser;

/**
 * Created by pavel on 23/11/15.
 */
public class Main {
    public static void main(String[] args) {
        int size = args.length;
        for (int i = 0; i < size; ++i) {
            String[] splitArgument = args[i].split("\\s+");
            int splittedSize = splitArgument.length;
            for (int j = splittedSize - 1; j >= 0; ++j) {
                System.out.println(splitArgument[j]);
            }
        }
    }
}
