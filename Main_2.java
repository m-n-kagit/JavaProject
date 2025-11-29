import java.util.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class Player {
    String name, email_id, passcode, role;
    int player_id; // unique
    boolean want_continue = true;
    String url = "jdbc:mysql://localhost:3306/quiz_application";
    String user = "root";
    String password = "yuv07";

    Player(int case_) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(url, user, password);
        Scanner sc = new Scanner(System.in);
        Statement stmt = con.createStatement();
        ResultSet rs;
        switch (case_) {
            case 1: // not registered , signup
                System.out.println("Enter your name:");
                this.name = sc.nextLine();
                do {
                    System.out.println("Enter your email id (e.g. quiz_app@gmail.com):");
                    this.email_id = sc.nextLine();
                    String regex = "^[a-z\\dA-Z.+_%]+@[A-Z\\da-z]+\\.[A-Z\\da-z]+$"; // after . only those @ characters

                    if (email_id.matches(regex)) {
                        while (true) {
                            System.out.println("Enter your passcode:\n(The passcode must be of 6 letters atleast) ");
                            String temp = sc.nextLine();
                            System.out.println("Confirm your passcode:");
                            String temp2 = sc.nextLine();

                            if (temp.equals(temp2) && temp.length() >= 6) {
                                passcode = temp;
                                break;
                            } else {
                                System.out.println("Invalid (Passcode not accepted)");
                                continue;
                            }

                        }

                    } else {
                        System.out.println("Not a valid email Id\nPlease enter valid email Id");

                        continue;
                    }

                    System.out.println("Saving...\n");
                    this.role = "player";
                    stmt.execute("INSERT INTO player (user_name,role,email_id,player_passcode) VALUES('" + name
                            + "','" + role + "','"
                            + email_id + "', '" + passcode + "') ");
                    System.out.println("Saved succesfully.");
                    rs = stmt.executeQuery("Select user_Id from player where user_name='" + this.name
                            + "' and email_Id='" + this.email_id + "'");
                    rs.next();
                    this.player_id = rs.getInt("user_Id");
                    break;
                } while (true);
                rs = stmt.executeQuery(
                        "Select * from player where user_name='" + this.name + "' and user_Id=" + this.player_id);
                rs.next();
                display_details(rs);
                rs.close();

                break;
            case 2: // for registerd people
                do {
                    System.out.println("Enter email id:");
                    String emp_id = sc.nextLine();
                    System.out.println("Enter passcode:");
                    String temp_pass = sc.nextLine();

                    rs = stmt.executeQuery("Select * from player where email_id='" + emp_id
                            + "' and player_passcode='" + temp_pass + "'");

                    if (rs.next()) {
                        display_details(rs);
                        break;
                    } else {
                        System.out.println("Provided Id or passcode invalid:");
                        if (ask_user()) {

                            continue;
                        } else {
                            this.want_continue = false;
                            break;
                        }

                    }

                } while (true);

                break;

        }
        stmt.close();
        con.close();

    }

    boolean ask_user() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Press 1 for retry or anything else for escape:");
        int press = sc.nextInt();

        if (press == 1) {
            return true;
        } else {
            System.out.println("[Back to the previous section]");
            return false;
        }

    }

    public void display_details(ResultSet rs) throws Exception {
        this.name = rs.getString("user_Name");
        this.email_id = rs.getString("email_id");
        this.role = "Player";
        this.player_id = rs.getInt("user_Id");
        System.out.println("Your details are as follows:\nPlayer ID:" + this.player_id + "\nName:" + this.name
                + "\nEmail ID:" + this.email_id);
    }
}

class Admin {

}

class Question {
    int qid;
    String question_text;
}

class Answer {
    String correct_ans;
    int qid;
}

class Quiz_Session { // implementing attempt database
    // catid cat_name
    // 101 G.K.
    // 102 C.T.
    // 103 Sci
    // 104 Men.A.

    int quiz_id, cat_id, want_continue = 1;
    String diff, type; // difficulty , type: mcq , tf, sa
    String url = "jdbc:mysql://localhost:3306/quiz_application";
    String user = "root";
    String password = "yuv07";
    Scanner sc = new Scanner(System.in);

    Quiz_Session(Player p1, int choice) throws Exception {
        java.util.List<String> subjects;
        // Easy, Medium, Hard
        int choice1;
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(url, user, password);
        Statement stmt = con.createStatement();
        ResultSet rs;
        System.out.println("Select the difficulty :\n1) Easy\n2) Medium\n3) Hard\n");
        choice1 = sc.nextInt();
        this.cat_id = choice;
        while (true) { // session 1
            if (choice1 == 1) {
                diff = "Easy";
            } else if (choice1 == 2) {
                diff = "Medium";
            } else if (choice1 == 3) {
                diff = "Hard";
            } else {
                System.out.println("Chosen wrong option:\n");
                if (ask_user()) {
                    continue;
                } else {
                    break;
                }
            }
            rs = stmt
                    .executeQuery("Select quiz_id from quiz where difficulty='" + diff + "' and cat_id=" + this.cat_id);
            rs.next();
            this.quiz_id = rs.getInt("quiz_id");
            System.out.println("Choose the quiz type to play below:\n1. Mcq\n2. True/False\n3. Integer Type");
            choice1 = sc.nextInt();
            System.out.println("Select the subjects in which you want to play:\n");

            if (choice1 == 1 || choice1 == 2 || choice1 == 3) {
                switch (choice1) {
                    case 1:
                        type = "mcq";
                        break;
                    case 2:
                        type = "tf";
                        break;
                    case 3:
                        type = "sa";
                        break;
                }
            } else {
                System.out.println("Chosen wrong option:\n");
                if (ask_user()) {
                    continue;
                } else {
                    this.want_continue = 0;
                    rs.close();
                    break;
                }
            }
            subjects = new ArrayList<>();
            rs = stmt.executeQuery(
                    "Select subject from " + this.type + " where quiz_id=" + this.quiz_id + " group by subject");
            int i = 1;
            String sub;
            while (rs.next()) {
                sub = rs.getString("subject");
                System.out.println(i + ") " + sub);
                subjects.add(sub);
                i++;
            }
            choice1 = sc.nextInt();
            sc.nextLine();
            sub = subjects.get(choice1 - 1);
            java.util.List<Question> problems = new ArrayList<>();
            java.util.List<String[]> options = new ArrayList<>(); // [[op1,op2,op3,op4],[op11,op22,op33,op44],...]
            i = 1;

            rs = stmt
                    .executeQuery("Select question_text,q_id,ans from " + this.type + " where quiz_id = " + this.quiz_id
                            + " and subject ='" + sub + "'");
            if (this.type.equals("mcq")) {
                // Now we want 10 questions to be generated in random manner with options
                // first question will be displayed and options or ans will be asked from the
                // user

                List<Answer> o_ans = new ArrayList<>();

                while (rs.next() && i <= 10) { // only 10 questions will be added
                    Question ques = new Question();
                    Answer ans1 = new Answer();
                    ques.question_text = rs.getString("question_text");
                    ques.qid = rs.getInt("q_id");
                    ans1.qid = rs.getInt("q_id");
                    ans1.correct_ans = rs.getString("ans");
                    o_ans.add(ans1);
                    problems.add(ques);
                    i++;

                }

                // below i'am assigning options and displaying the corresponding question
                System.out.println("\n--------------------MCQ TYPE QUESTIONS-----------------");
                System.out.println("SUBJECT: " + sub);
                System.out.println("Write answers in (A,B,C,D) or (a,b,c,d)");
                Random rand = new Random();
                java.util.Set chosen_numbers = new HashSet<>();
                String[] ans = new String[10];
                i = 0;
                int marks = 0;

                long start = System.currentTimeMillis();
                while (i < 10) { // game section
                    int chosen_problem = rand.nextInt(problems.size());
                    if (chosen_numbers.contains(chosen_problem)) {
                        continue;
                    }
                    chosen_numbers.add(chosen_problem);
                    rs = stmt.executeQuery(
                            "Select option_text from options where q_id=" + problems.get(chosen_problem).qid);

                    String[] temp = new String[4];
                    int j = 0;
                    while (rs.next()) {
                        temp[j] = rs.getString("option_text");
                        j++;
                    }
                    options.add(temp);
                    System.out.println(problems.get(chosen_problem).question_text);
                    for (j = 0; j < temp.length; j++) {
                        System.out.println(temp[j]);
                    }
                    System.out.println("Select option:");
                    // to read the leftover \n in the scanner buffer
                    ans[i] = sc.nextLine();
                    ans[i] = ans[i].toUpperCase().trim();
                    if (ans[i].equals(o_ans.get(chosen_problem).correct_ans)) {
                        marks++;
                    }
                    i++;
                }
                long end = System.currentTimeMillis();
                long time_taken = (end - start) / 1_000;
                LocalDateTime now = LocalDateTime.now();

                // Format to MySQL DATETIME format: "YYYY-MM-DD HH:MM:SS"
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String mysqlDateTime = now.format(formatter);
                System.out.println("Your Score in this quiz: " + marks + "\nTime Taken: " + time_taken);
                System.out.println("Saving your result...");
                stmt.execute("Insert into attempt values(" + p1.player_id + "," + this.quiz_id + ",'"
                        + mysqlDateTime + "'," + marks + "," + time_taken + ")");
                System.out.println("Saved successfully..");

            } else if (this.type.equals("sa")) {
            } else if (this.type.equals("tf")) {
            }
            System.out.println(
                    "Choose the below option:\n1. Want to see where you stand in the quiz\n2. Play another quiz with same catogory\n3. Play another quiz with different category.");
            choice1 = sc.nextInt();
            if (choice1 == 1) {
                ask_stand(p1);
            } else if (choice1 == 2) {
                continue;
            } else if (choice == 3) {
                break;
            } else {
                if (ask_user()) {
                    continue;
                } else {
                    break;
                }
            }

        }

    }

    boolean ask_user() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Press 1 for retry or anything else for escape:");
        int press = sc.nextInt();

        if (press == 1) {
            return true;
        } else {
            System.out.println("[Back to the previous section]");
            return false;
        }

    }

    void ask_stand(Player p1) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(this.url, this.user, this.password);
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(
                "Select p.user_name,max(a.score) as best_score from player p join attempt a  attempt a on p.user_id=a.user_id where a.quiz_id="
                        + this.quiz_id + " group by p.user_id  order by best_score desc ");
        int i = 1;
        while (rs.next()) {
            System.out.println(i + " " + p1.name + "\nMarks:" + rs.getInt("score"));
            i++;
        }
    }
}

public class Main_2 {
    public static void main(String[] args) throws Exception {
        System.out.println("\n----------------------------------------------------");
        System.out.println("----------------Welcome to the Quiz Game--------------");
        System.out.println("------------------------------------------------------");
        System.out.println("\nA game where you examine your knowledge and see your stand among other players\n");
        // Ist page
        int choice;
        Scanner sc = new Scanner(System.in);
        while (true) {

            System.out.println("\nChoose one of the options below\n1. Admin session\n2. Player Section\n0. Exit");
            choice = sc.nextInt();
            sc.nextLine();
            if (choice == 1 || choice == 2) {
                while (true) {
                    int choice2, choice3;
                    if (choice == 1) { // for admin section
                        System.out.println(
                                "ADMIN SECTION\n1. Login(Old player) \n2. Sign up(New player)\nEnter the option: ");
                    }

                    else if (choice == 2) { // for player section
                        System.out
                                .println(
                                        "BE READY PLAYER! \n1. Sign up(New player)\n2. Login(Old player)\nEnter the option: ");
                        choice2 = sc.nextInt();
                        if (choice2 == 1 || choice2 == 2) {
                            Player p1 = new Player(choice2);
                            if (p1.want_continue) {
                                while (true) { // for menu bar => catogory section
                                    System.out.println("Welcome " + p1.name + " to world of quiz problems!");
                                    System.out.println("Select a catogory from below: ");
                                    System.out.println(
                                            "1. General Knowledge\n2. Computer Technologies\n3. Science\n4. Mental Ability\n0. Back");
                                    int cat_choice; // for catogory choice
                                    cat_choice = sc.nextInt() + 100;
                                    String cat_choice_name = get_cat_name(cat_choice);
                                    System.out.println(
                                            "Choose from the below options:\n1) Start Game\n2) See your analytics");
                                    choice3 = sc.nextInt();

                                    if (choice3 == 1) {
                                        if (cat_choice == 101 || cat_choice == 102 || cat_choice == 103
                                                || cat_choice == 104) {
                                            Quiz_Session quiz1 = new Quiz_Session(p1, cat_choice);
                                        } else {
                                            break;
                                        }
                                    } else if (choice3 == 2) {
                                        while (true) {
                                            System.out.println(
                                                    "--------------------------Analytics Section---------------------------");
                                            System.out.println("Choose from below options:");
                                            System.out.println(
                                                    "1) See scores of respective subjects\n2) See your rank among various players");
                                            choice3 = sc.nextInt();
                                            if (choice3 == 1) {
                                                System.out.println("+--------------------Scores & Attemp in respective catogory ");
                                                user_scores(p1);
                                                if (ask_user_back()) {
                                                    continue;
                                                } else {
                                                    break;
                                                }

                                            } else if (choice == 2) {
                                                System.out.println("+---------Rank Section of "+cat_choice_name+"catogory----------+");
                                                ask_stand(p1, cat_choice);
                                                if (ask_user_back()) {
                                                    continue;
                                                } else {
                                                    break;
                                                }

                                            }
                                        }
                                    }

                                }

                            } else {
                                break;
                            }
                        } else {
                            if (ask_user()) {
                                continue;
                            } else {
                                break;
                            }

                        }

                    }

                }
            } else {
                if (ask_user()) {
                    continue;
                } else {
                    System.out.println("Program closed.");
                    break;
                }
            }
        }
    }

    static void user_scores(Player p1, int cat_id) {
        String url = "jdbc:mysql://localhost:3306/quiz_application";
        String user = "root";
        String password = "yuv07";
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(url, user, password);
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(
                "Select p.user_name,a.score,a.attempt_at,c.cat_name from player p join attempt a on p.user_id=a.user_id join quiz q on a.quiz_id=q.quiz_id join category c on q.cat_id=c.cat_id where p.user_name="
                        + p1.name + " and q.cat_id=" + cat_id);

        // Print table header only once before loop
        System.out.printf("%-15s%-10s%-20s%-20s%n",
                "Name", "Score", "Attempt Date", "Category");

        System.out.println("--------------------------------------------------------------");

        while (rs.next()) {
            System.out.printf("%-15s%-10d%-20s%-20s%n",
                    rs.getString("user_name"),
                    rs.getInt("score"),
                    rs.getString("attempt_at"),
                    rs.getString("cat_name"));
        }

        rs.close();

    }

    static string get_cat_name(int cat_id) {
        String url = "jdbc:mysql://localhost:3306/quiz_application";
        String user = "root";
        String password = "yuv07";
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(url, user, password);
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("Select cat_name from category where cat_id="+ cat_id);
        String cat_name = rs.getString("cat_name");
        rs.close();stmt.close();con.close();
        return cat_name;

    }

    static boolean ask_user() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Press 1 for retry or anything else for escape:");
        int press = sc.nextInt();

        if (press == 1) {
            return true;
        } else {
            System.out.println("[Back to the previous section]");
            return false;
        }

    }

    static void ask_stand(Player p1, int cat_choice) throws Exception {
        String url = "jdbc:mysql://localhost:3306/quiz_application";
        String user = "root";
        String password = "yuv07";
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(url, user, password);
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(
                "Select p.user_name,max(a.score) as best_score from player p join attempt a on p.user_id=a.user_id join quiz q on a.quiz_id = q.quiz_id where cat_id="
                        + cat_choice + " group by p.user_name order by best_score desc, MIN(a.time_taken) asc");
        int i = 1;
        while (rs.next()) {
            System.out.println("R.no.    |     Name");
            System.out.println(i + "  " + rs.getString("user_name") + "  Marks: " + rs.getInt("best_score"));
            i++;
        }
        rs.close();stmt.close();con.close();
    }

    static boolean ask_user_back() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Press 1 for continue or anything else for back to previous section:");
        int press = sc.nextInt();

        if (press == 1) {
            return true;
        } else {
            System.out.println("[Back to the previous section]");
            return false;
        }

    }

}
