import java.io.*;
import java.util.*;

class User_data {
    String Name;
    int PlayerId;
    int score;

    User_data(String name, int id, int score) {
        this.Name = name;
        this.PlayerId = id;
        this.score = score;
    }

    // Constructor for new user
    User_data(String name) {
        this.Name = name;
        Random r = new Random(System.currentTimeMillis());
        this.PlayerId = r.nextInt(10000);
        this.score = 0;
    }
}

class Enter {
    int choice;
    int index;

    Enter(int c) {
        this.choice = c;
    }

    boolean check_whether(User_data u1) throws Exception {
        Scanner sc = new Scanner(System.in);
        List<User_data> users = readAllUsers();

        switch (choice) {
            case 1:
                System.out.print("Enter your PlayerId: ");
                int inputId = sc.nextInt();

                for (User_data user : users) {
                    if (user.PlayerId == inputId) {
                        index = users.indexOf(user);
                        u1.Name = user.Name;
                        u1.PlayerId = user.PlayerId;
                        u1.score = user.score;

                        System.out.println("Login successful.");
                        view_data(u1);
                        u1.score = 0;
                        return true;
                    }
                }

                System.out.println("Player ID not found.");
                return false;

            case 2:
                // Add new user to the file
                FileWriter fw = new FileWriter("userdata.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                for (User_data user : users) {
                    if (user.PlayerId == u1.PlayerId) {
                        index = users.indexOf(user);
                        u1.Name = user.Name;
                        u1.PlayerId = user.PlayerId;
                    }
                }
                bw.write("Name:" + u1.Name);
                bw.newLine();
                bw.write("Player ID:" + u1.PlayerId);
                bw.newLine();
                bw.write("Score:" + u1.score);
                bw.newLine();
                bw.close();
                fw.close();
                index = users.size() - 1;
                view_data(u1);
                System.out.println("Sign up successful.");
                return true;
        }
        return false;
    }

    public List<User_data> readAllUsers() throws Exception {
        List<User_data> users = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader("userdata.txt"));
        String line;

        while ((line = br.readLine()) != null) {
            if (line.startsWith("Name:")) {
                String name = line.substring(5).trim();
                String idLine = br.readLine();
                String scoreLine = br.readLine();

                if (idLine != null && scoreLine != null) {
                    int id = Integer.parseInt(idLine.substring(10).trim());
                    int score = Integer.parseInt(scoreLine.substring(6).trim());
                    users.add(new User_data(name, id, score));
                }
            }
        }

        br.close();
        return users;
    }

    public void view_data(User_data u1) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.println("Select an option:\n1. See your profile\n2. Skip");
        int ch = sc.nextInt();
        if (ch == 1) {
            System.out.println("Name: " + u1.Name);
            System.out.println("PlayerId: " + u1.PlayerId);
            System.out.println("Previous Score: " + u1.score);
        }
    }
}

class Read_class {
    final String mcq_file = "Mcq.txt";
    final String tf = "Tf.txt";
    private int no_qs;
    private User_data user;

    Read_class(int n, User_data u1) {
        this.no_qs = n;
        this.user = u1;
        
    }

    public void read_mqs(List<User_data> users, int loc) throws Exception {
        Scanner sc = new Scanner(System.in);
        List<String[]> questions = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(mcq_file));
        String line;
        while ((line = br.readLine()) != null) {
            if (line.startsWith("Q")) {
                String[] questionBlock = new String[6]; // FOR Q, op1, op2, op3, op4, Ans
                questionBlock[0] = line;
                for (int i = 1; i < 6; i++) {
                    questionBlock[i] = br.readLine();
                }
                questions.add(questionBlock);
            }
        }
        br.close();

        String[] ans = new String[no_qs];
        String[] arr = new String[no_qs];
        Set<Integer> used = new HashSet<>();
        Random rand = new Random();

        for (int i = 0; i < no_qs;) {
            int index = rand.nextInt(questions.size());
            if (used.contains(index))
                continue;
            used.add(index);

            String[] q = questions.get(index);
            System.out.println("\nQ" + (i + 1) + ": " + q[0].substring(q[0].indexOf('.') + 1).trim());
            System.out.println(q[1]);
            System.out.println(q[2]);
            System.out.println(q[3]);
            System.out.println(q[4]);

            ans[i] = q[5].substring(q[5].indexOf('.') + 1).trim(); // e.g., "C"
            System.out.print("Your answer: ");
            arr[i] = sc.nextLine().trim();

            i++;
        }

        int score = 0;
        for (int i = 0; i < no_qs; i++) {
            if (ans[i] != null && ans[i].equals(arr[i])) {
                score++;
            }
        }

        user.score = score;
        users.remove(loc);
        users.add(loc, user);
        System.out.println("\nYour final score is: " + score + " / " + no_qs);
    }

    public void read_tf(List<User_data> users, int loc) throws Exception {
        Scanner sc = new Scanner(System.in);
        List<String[]> questions = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(tf));
        String line;
        while ((line = br.readLine()) != null) {
            if (line.startsWith("Q")) {
                String[] questionBlock = new String[2]; // FOR Q, Ans
                questionBlock[0] = line;
                for (int i = 1; i < 2; i++) {
                    questionBlock[i] = br.readLine();
                }
                questions.add(questionBlock);
            }
        }
        br.close();

        String[] ans = new String[no_qs];
        String[] arr = new String[no_qs];
        Set<Integer> used = new HashSet<>();
        Random rand = new Random();

        for (int i = 0; i < no_qs;) {
            int index = rand.nextInt(questions.size());
            if (used.contains(index))
                continue;
            used.add(index);

            String[] q = questions.get(index);
            System.out.println("\nQ" + (i + 1) + ": " + q[0].substring(q[0].indexOf('.') + 1).trim());

            ans[i] = q[1].substring(q[1].indexOf('.') + 1).trim();
            System.out.print("Your answer: ");
            arr[i] = sc.nextLine().trim();

            i++;
        }

        int score = 0;
        for (int i = 0; i < no_qs; i++) {
            if (ans[i] != null && ans[i].equals(arr[i])) {
                score++;
            }
        }

        user.score = score;
        users.remove(loc);
        users.add(loc, user);
        System.out.println("\nYour final score is: " + score + " / " + no_qs);

    }

}

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("------Welcome to Quiz Game------");
        int choice, no;
        Scanner sc = new Scanner(System.in);
        System.out.println("\n1. Login(Old player) \n2. Sign up(New player)\nEnter the option: ");
        choice = sc.nextInt();
        sc.nextLine();
        Enter e1 = new Enter(choice);
        String name;
        System.out.print("Enter your name:\n");
        name = sc.nextLine();
        User_data user = new User_data(name);
        List<User_data> users = e1.readAllUsers();
        int index = e1.index;
        if (e1.check_whether(user)) {
            // Dashboard
            System.out.println("-------------------------");
            System.out.println("Welcome " + user.Name);
            System.out.println("Select the quiz category given below:");
            System.out.print("1. MCQ \n2. True/False\n3. Exit\n");
            System.out.print("Enter your choice here: ");

            choice = sc.nextInt();
            sc.nextLine();
            System.out.println(
                    "Instructions:\n1.Attempting all questions are compulsory.\n2.We have only 20 questions in each" +
                            "category,\nSo write no. of qustions in the range of 20.\n3.Each question contains 1 mark\n"
                            +
                            "4.Give answers in capital.");
            switch (choice) {

                case 1:
                    System.out.println("---MCQ QUESTIONS---");
                    System.out.println("Write answers in capitals (A,B,C,D)");

                    System.out.println("Enter number of questions:");
                    no = sc.nextInt();
                    Read_class rd = new Read_class(no, user);
                    rd.read_mqs(users, index);
                    break;

                case 2:
                    System.out.println("---MCQ QUESTIONS---");
                    System.out.println("Write answers in capitals (T,F)");
                    System.out.println("Enter number of questions:");
                    no = sc.nextInt();
                    Read_class rf = new Read_class(no, user);
                    rf.read_tf(users, index);
                    break;
                case 3:
                    return;
            }
            FileWriter fr = new FileWriter("userdata.txt");
            BufferedWriter br = new BufferedWriter(fr);
            for (User_data u : users) {
                br.write("Name:" + u.Name);
                br.newLine();
                br.write("Player ID:" + u.PlayerId);
                br.newLine();
                br.write("Score:" + u.score);
                br.newLine();
            }
            System.out.println("---Your score has been saved---");
            System.out.println("--The Program ends here.--");
            br.close();
            fr.close();

        }
    }

}
