import java.util.Scanner;
import java.util.*;
import java.sql.*;
import java.awt.*;

class Player {
    String name, email_id, passcode, role;
    int player_id; // unique
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

                break;
            case 2: // for registerd people
                do {
                    System.out.println("Enter email id:");
                    String temp_id = sc.nextLine();
                    System.out.println("Enter passcode:");
                    String temp_pass = sc.nextLine();

                    rs = stmt.executeQuery("Select * from player where email_id='" + temp_id
                            + "' and player_passcode='" + temp_pass + "'");

                    if (rs.next()) {
                        display_details(rs);
                        break;
                    } else {
                        System.out.println("Provided Id or passcode invalid:");
                        if (ask_user()) {
                            continue;
                        } else {
                            break;
                        }

                    }

                } while (true);

                break;

        }
    }

    boolean ask_user() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Press Enter for retry or 0 for escape:");
        String press = sc.next();

        if (press.isEmpty()) {
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

class Quiz_Session { // implementing attempt database
    // catid cat_name
    // 101 G.K.
    // 102 C.T.
    // 103 Sci
    // 104 Men.A.

    int quiz_id, cat_id;
    String diff,type,subject; // difficulty
    String url = "jdbc:mysql://localhost:3306/quiz_application";
    String user = "root";
    String password = "yuv07";
    Scanner sc = new Scanner(System.in);
    
    Quiz_Session(Player p1, int choice) throws Exception {
        // Easy, Medium, Hard
        int choice1; 
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(url, user, password);
        Statement stmt = con.createStatement();
        ResultSet rs;
        System.out.println("Select the difficulty :1)Easy\n2)Medium\n3)Hard\n");
        choice1 = sc.nextInt();
        this.cat_id = choice;
        while(true){
            if(choice==1){
                diff="Easy";
            }
            else if(choice==2){
                diff="Medium";
            }
            else if(choice==3){
                diff="Hard";
            }
            else{
                System.out.println("Chosen wrong option:\n");
                if(ask_user()){
                    continue;
                }
                else{
                    break;
                }
            }
            rs = stmt.executeQuery("Select quiz_id from quiz where diff=" + diff + " and cat_id=" + this.cat_id);
            rs.next();
            this.quiz_id = rs.getInt("quiz_id");
            System.out.println("Choose the quiz type to play below:\n1. Mcq\n2. True/False \n3. Integer Type");
            choice1 = sc.nextInt();
            System.out.println("Select the subjects in which you want to play:\n");
             
            if(choice1==1 ||choice1==2 || choice1==3 ){
                switch(choice1){
                    case 1:
                        type="mcq";
                        break;
                    case 2:
                        type="tf";
                        break;
                    case 3:
                        type="sa";
                        break;

                }
            }
            else{
                System.out.println("Chosen wrong option:\n");
                if(ask_user()){
                    continue;
                }
                else{
                    break;
                }
            }
            rs = stmt.executeQuery("Select subject from "+this.type +"group by subject where quiz_id="+quiz_id);
            int i=1;
            while(rs.next()){
                System.out.println(i+") "+rs.getString("subject"));
                i++;
            }
            choice1 = sc.nextInt();
            
            

            

        }

        
    }
    boolean ask_user() {
    Scanner sc = new Scanner(System.in);
    System.out.println("Press Enter for retry or 0 for escape:");
    String press = sc.next();

    if (press.isEmpty()) {
        return true;
    } else {
        System.out.println("[Back to the previous section]");
        return false;
    }

}
}

public class Main_2 {
    public static void main(String[] args) throws Exception {
        System.out.println("----------Welcome to Quiz Game---------");
        System.out.println("\nA game where you examine your knowledge and see your stand among other players\n\n");
        // Ist page
        int choice;
        Scanner sc = new Scanner(System.in);
        System.out.println("\nChoose one of the options below\n1. Admin session\n2. Player Section");
        choice = sc.nextInt();
        sc.nextLine();

        while (true) {
            int choice2;
            if (choice == 1) { // for admin section
                System.out.println("ADMIN SECTION\n1. Login(Old player) \n2. Sign up(New player)\nEnter the option: ");

            }

            else if (choice == 2) { // for player section
                System.out
                        .println("BE READY PLAYER! \n1. Sign up(New player)\n2. Login(Old player)\nEnter the option: ");
                choice2 = sc.nextInt();
                Player p1 = new Player(choice2);
                System.out.println("Welcome " + p1.name + " to world of quiz problems!");
                System.out.println("Select a catogory from below: ");
                System.out.println("1. General Knowledge\n2. Computer Technologies\n3. Science\n4. Mental Ability");
                int cat_choice; // for catogory choice
                cat_choice = sc.nextInt() + 100;
                Quiz_Session quiz1 = new Quiz_Session(p1, cat_choice);

            }

        }
    }

}
