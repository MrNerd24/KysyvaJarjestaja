import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Juuso on 19.2.2017.
 */
public class Main {
    private static Scanner reader = new Scanner(System.in);

    public static void main(String[] args) {
        printIntroduction();

        ArrayList<ArrayList<Item>> lists = new ArrayList<>();
        ArrayList<Boolean> unsortedLists = new ArrayList<>();


        askLists(lists, unsortedLists);
        sortLists(lists, unsortedLists);

        ArrayList<Item> sortedList = combine(lists);

        if (sortedList != null && !sortedList.isEmpty()) {
            System.out.println("Final sorted list for saving in a file:");
            printForFile(sortedList);
            System.out.println("Final sorted list:");
            print(sortedList);
        }

    }



    private static void printIntroduction() {
        System.out.println("ASKING SORTER");
        System.out.println("");
        System.out.println("You can sort things, like songs, based on your opinions ");
        System.out.println("");
        System.out.println("The program uses \"Merge sort\" sorting algorithm.");
        System.out.println("Its idea is to merge already sorted lists into one.");
        System.out.println("Everything begins from the knowledge that list of 1 item is already sorted.");
        System.out.println("Two lists of 1 item can be merged into a list of two items, and by mergin two lists of two items a sorted list of 4 items can be made etc.");
        System.out.println("When the algorithm wants to compare two items, it asks you");
        System.out.println("");
        System.out.println("You can add multiple lists that can be either unsorted or already sorted.");
        System.out.println("In the end you get one sorted list.");
    }

    private static ArrayList<Item> combine(ArrayList<ArrayList<Item>> lists) {
        if (lists.size() == 0) {
            return null;
        }

        if (lists.size() == 1) {
            return lists.get(0);
        }

        if (lists.size() == 2) {
            return mergeSortedLists(lists.get(0), lists.get(1));
        }

        ArrayList<ArrayList<Item>> listsLeft = new ArrayList<>();
        ArrayList<ArrayList<Item>> listsRight = new ArrayList<>();
        int half = lists.size() / 2;

        for (int i = 0; i < half; i++) {
            listsLeft.add(lists.get(i));
        }
        for (int i = half; i < lists.size(); i++) {
            listsRight.add(lists.get(i));
        }

        return mergeSortedLists(combine(listsLeft), combine(listsRight));

    }


    private static void askLists(ArrayList<ArrayList<Item>> lists, ArrayList<Boolean> unsortedLists) {
        while (true) {
            System.out.println("");
            System.out.println("What would you like to do?");
            System.out.println("1. Add unsorted list.");
            System.out.println("2. Add sorted list.");
            System.out.println("3. Add unsorted list from file.");
            System.out.println("4. Add sorted list from file.");
            if (lists.isEmpty()) {
                System.out.println("5. quit");
            } else {
                System.out.println("5. Sort lists");
            }
            System.out.print("Give the number of option: ");
            int input;
            try {
                input = Integer.parseInt(reader.nextLine());
            } catch (Exception e) {
                System.out.println("You need to type in a number.");
                continue;
            }
            if (input < 1 || input > 5) {
                System.out.println("Type in a number between 1 and 5");
                continue;
            }
            if (input == 1) {
                lists.add(askList());
                unsortedLists.add(true);
                continue;
            }

            if (input == 2) {
                lists.add(askList());
                unsortedLists.add(false);
                continue;
            }

            if (input == 3) {
                lists.add(askListFromFile());
                unsortedLists.add(true);
                continue;
            }

            if (input == 4) {
                lists.add(askListFromFile());
                unsortedLists.add(false);
                continue;
            }

            System.out.println("");
            return;
        }
    }

    private static ArrayList<Item> askListFromFile() {
        File file = null;
        ArrayList<Item> ans = new ArrayList<>();
        Scanner fileReader = null;
        while(file == null) {
            System.out.println();
            System.out.print("Give file address: ");
            String address = reader.nextLine();

            if (address.isEmpty()) {
                return ans;
            }

            try {
                file = new File(address);
            } catch (Exception e) {
                System.out.println("Invalid address, try again.");
                System.out.println("(" + e.getMessage() + ")");
                continue;
            }

            try {
                fileReader = new Scanner(file);
            } catch (Exception e) {
                System.out.println("Invalid address, try again.");
                System.out.println("(" + e.getMessage() + ")");
                continue;
            }

            System.out.println("File found.");
            break;
        }

        while(fileReader.hasNextLine()) {
            ans.add(new Item(fileReader.nextLine()));
        }

        return ans;

    }

    private static void sortLists(ArrayList<ArrayList<Item>> lists, ArrayList<Boolean> unsortedLists) {
        for (int i = 0; i < lists.size(); i++) {
            if (unsortedLists.get(i)) {
                lists.set(i, sort(lists.get(i)));
            }
        }
    }

    private static ArrayList<Item> askList() {
        System.out.println("");
        System.out.println("Give names of all items:");
        System.out.println("Enter empty string when ready.");
        int index = 1;
        ArrayList<Item> items = new ArrayList<>();
        System.out.println("");
        while (true) {
            System.out.print(index + ". ");
            String name = reader.nextLine();
            if (name.isEmpty()) {
                break;
            }
            items.add(new Item(name));
            index++;
        }
        System.out.println("");
        return items;
    }

    private static void print(ArrayList<Item> items, int start, int end) {
        if (items == null || items.isEmpty()) {
            return;
        }
        int index = 1;

        for (int i = start; i < Math.min(end, items.size()); i++) {
            System.out.println(index + ". " + items.get(i).getName());
            index++;
        }
        System.out.println("");
    }

    private static void print(ArrayList<Item> items) {
        if (items == null || items.isEmpty()) {
            return;
        }
        print(items, 0, items.size());
    }

    private static ArrayList<Item> sort(ArrayList<Item> items) {
        ArrayList<ArrayList<Item>> lists = new ArrayList();
        lists.add(items);
        int width = 1;
        int depth = 0;
        while (width < items.size()) {
            int section = 0;
            lists.add(new ArrayList<Item>());
            while (section < items.size()) {
                int left = section;
                int right = section + width;

                while (left < Math.min(section + width, items.size()) || right < Math.min(section + 2 * width, items.size())) {

                    if (left >= section + width) {
                        lists.get(depth + 1).add(lists.get(depth).get(right));
                        right++;
                        continue;
                    }

                    if (right >= items.size() || right >= section + 2 * width) {
                        lists.get(depth + 1).add(lists.get(depth).get(left));
                        left++;
                        continue;
                    }

                    if (lists.get(depth).get(left).compareTo(lists.get(depth).get(right)) <= 0) {
                        lists.get(depth + 1).add(lists.get(depth).get(left));
                        left++;
                    } else {
                        lists.get(depth + 1).add(lists.get(depth).get(right));
                        right++;
                    }

                }
                System.out.println("");
                System.out.println("Sorted sublist:");
                print(lists.get(depth + 1), section, section + 2 * width);
                section += 2 * width;
            }
            width *= 2;
            depth++;
        }
        return lists.get(lists.size() - 1);
    }

    public static ArrayList<Item> mergeSortedLists(ArrayList<Item> listLeft, ArrayList<Item> listRight) {
        ArrayList<Item> newList = new ArrayList<>();
        int left = 0;
        int right = 0;
        while (left < listLeft.size() || right < listRight.size()) {

            if (left >= listLeft.size()) {
                newList.add(listRight.get(right));
                right++;
                continue;
            }

            if (right >= listRight.size()) {
                newList.add(listLeft.get(left));
                left++;
                continue;
            }

            if (listLeft.get(left).compareTo(listRight.get(right)) <= 0) {
                newList.add(listLeft.get(left));
                left++;
            } else {
                newList.add(listRight.get(right));
                right++;
            }

        }
        return newList;
    }

    private static void printForFile(ArrayList<Item> sortedList) {
        if (sortedList == null || sortedList.isEmpty()) {
            return;
        }

        for (int i = 0; i < sortedList.size(); i++) {
            System.out.println(sortedList.get(i).getName());
        }
        System.out.println("");
    }
}
