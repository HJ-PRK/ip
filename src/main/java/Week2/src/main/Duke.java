package Week2.src.main;
import java.io.FileNotFoundException;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.lang.*;
import java.io.FileWriter;
import java.io.IOException;
import java.time.*;

public class Duke {
    private static Storage storage;
    private Ui ui;
    private static TaskList tasklist;

    public Duke(String filePath) throws IOException {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasklist = new TaskList(storage.load());
        } catch(Exception e) {
            ui.showLoadingError();
            tasklist = new TaskList();
        }
    }

    public static void lining() {
        System.out.println("____________________________________________________________");
    }

    static FileWriter fw;

    static {
        try {
            fw = new FileWriter("saves/data.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeOn(Task currtask) throws IOException {
        fw.write(currtask.toString() +System.lineSeparator());
    }
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        new Duke("saves/data.txt");

        String logo = " ____        _        \n"
                + "|  _ \\ _   _| | _____ \n"
                + "| | | | | | | |/ / _ \\\n"
                + "| |_| | |_| |   <  __/\n"
                + "|____/ \\__,_|_|\\_\\___|\n";
        System.out.println("Hello from\n" + logo);

        lining();
        System.out.println("Hello! I'm Duke\nWhat can I do for you?");
        lining();

        String comm = "";

        //tasklist = new TaskList();

        try {
            while (!comm.equals("bye")) {
                comm = sc.nextLine();
                if (comm.equals("list")) {
                    lining();
                    System.out.println("Here are the tasks in your list:");
                    for (int i = 0; i < tasklist.size(); i++) {
                        Task current = (Task) tasklist.get(i);
                        int curnum = i+1;
                        System.out.println(curnum +"."+current.toString());
                    }
                    lining();

                } else if (comm.startsWith("mark")) {
                    String str = comm.substring(comm.length() - 1);
                    int marking = Integer.parseInt(str);
                    Task current = (Task) tasklist.get(marking - 1);
                    current.setDone();
                    lining();
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println("[X] " + current.content);
                    lining();
                    writeOn(current);

                } else if (comm.startsWith("unmark")) {
                    String str = comm.substring(comm.length() - 1);
                    int marking = Integer.parseInt(str);
                    Task current = (Task) tasklist.get(marking - 1);
                    current.setDone();
                    lining();
                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println("[ ]" +current.content);
                    lining();
                    writeOn(current);

                } else if(comm.startsWith("delete")) {
                    String str = comm.substring(comm.length() - 1);
                    int marking = Integer.parseInt(str);
                    Task current = (Task) tasklist.get(marking - 1);
                    tasklist.remove(marking-1);
                    lining();
                    System.out.println("Noted. I've removed this task:");
                    System.out.println(current.toString());
                    System.out.println("Now you have " +tasklist.size()+ " tasks in the list");
                    lining();

                }else if (comm.startsWith("todo")) {
                    String doit = comm.substring(5, comm.length());
                    Task current = new Todo(doit);
                    tasklist.add(current);
                    lining();
                    System.out.println("Got it. I've added this task:");
                    System.out.println(current.toString());
                    System.out.println("Now you have " + tasklist.size() + " tasks in the list");
                    lining();
                    writeOn(current);

                } else if (comm.startsWith("deadline")) {
                    String doit = comm.substring(9, comm.length());
                    String[] parts = doit.split("/by");
                    Task current = new Deadline(parts[0], parts[1]);
                    tasklist.add(current);
                    if(parts[1].contains("/")) {
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/mm/yyyy");
                        LocalDate date = LocalDate.parse(parts[1], dtf);
                    }
                    lining();
                    System.out.println("Got it. I've added this task:");
                    System.out.println(current.toString());
                    System.out.println("Now you have " + tasklist.size() + " tasks in the list");
                    lining();
                    writeOn(current);

                } else if (comm.startsWith("event")) {
                    String doit = comm.substring(6, comm.length());
                    String[] froms = doit.split("/from");
                    String[] fromses = froms[1].split("to");
                    String[] tos = doit.split("/to");
                    Task current = new Event(froms[0], fromses[1], tos[1]);
                    tasklist.add(current);
                    lining();
                    System.out.println("Got it. I've added this task:");
                    System.out.println(current.toString());
                    lining();
                    writeOn(current);

                } else if (comm.startsWith("find")) {
                    String keyword = comm.substring(6, comm.length());
                    Search sr = new Search(tasklist);
                    Search.find(keyword);

                } else if (!comm.equals("bye")){
                    lining();
                    System.out.println("OOPS!!! I'm sorry, but I don't know what that means :-(");
                    lining();
                }
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("OOPS!!! The description of a todo cannot be empty.");
        }
            lining();
            System.out.println("Bye. Hope to see you again soon!");
            lining();
    }
}
