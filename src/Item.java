import java.util.Scanner;

/**
 * Created by Juuso on 19.2.2017.
 */
public class Item implements Comparable<Item> {

    /**
     *@serial
     */
    private String name;
    private Scanner reader = new Scanner(System.in);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Item(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Item o) {
        try {
            System.out.println("Which one is better:");
            System.out.println("1. " + this.getName());
            System.out.println("2. " + o.getName());
            System.out.print("Give the number: ");
            String command = reader.nextLine();
            System.out.println("");

            if (command.equals("1")) {
                return -1;
            } else if (command.equals("2")) {
                return 1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            return 0;
        }

    }

    @Override
    public String toString() {
        return name;
    }


}