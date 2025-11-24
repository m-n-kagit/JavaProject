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
                do {
                    System.out.println("Enter your name:");
                    this.name = sc.nextLine();
                    System.out.println("Enter your email id (e.g. quiz_app@gmail.com):");
                    this.email_id = sc.nextLine();
                    while (true) {
                        System.out.println("Enter your passcode:\n(The passcode must be of 6 letters atleast) ");
                        String temp = sc.nextLine();
                        System.out.println("Confirm your passcode:");
                        String temp2 = sc.nextLine();
                        if(temp.equals(temp2) && temp.length()>=6){
                            passcode = temp;
                            break;
                        }
                        else{
                            System.out.println("Invalid (Passcode not accepted)");
                            continue;
                        }
                        
                    }
                    String regex = "^[a-z\\dA-Z.+_%]+@[A-Z\\da-z]+\\.[A-Z\\da-z]+$"; // after . only those @ characters
                   this. role = "player";
                    if (email_id.matches(regex)) {
                        System.out.println("Saving...\n");
                        stmt.execute("INSERT INTO player (user_name,role,email_id,player_passcode) VALUES('" + name + "','" + role + "','"
                                + email_id + "'," + "'"+passcode+"') ");
                        System.out.println("Saved succesfully.");
                        break;
                    } else {
                        System.out.println("Not a valid email Id\nPlease enter valid email Id");
                        
                        continue;
                    }
                } while (true);
                rs = stmt.executeQuery("Select user_name from player;");
                rs.next();
                this.player_id= rs.getInt("user_id");
                System.out.println("Your details saved:\nID:"+this.player_id+"\nName:"+this.name+"\nEmail_id:"+this.email_id);


                break;
            case 2: // for registerd people
            do{
                System.out.println("Enter email id:");
                String temp_id  = sc.nextLine();
                System.out.println("Enter passcode:");
                String temp_pass = sc.nextLine();
                    try{
                        rs=stmt.executeQuery("Select email_Id,passcode from player where email_Id="+temp_id+" and temp_pass="+temp_pass);
                        rs.next();
                        break;
                    }
                    catch (Exception e) {
                        System.out.println("Either email_Id or passcode is invalid\nPlease enter correct details.");
                        ask_user();
                        continue;
                    }

                }
                while(true);

                break;
            default:
                break;
        }
    }


    
    boolean ask_user(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Press Enter for retry or 0 for escape:");
        String press=sc.next();

        if(press.isEmpty()){
            return true;
        }
        else{
            System.out.println("[Back to the previous section]");
            return false;
        }
      
    }
}

class Admin {

}

class Quiz_Session{ //implementing attempt database
    int quiz_id,cat_id;
    String diff; //difficulty
    Quiz_Session(Player p1,int choice ){
        //Easy, Medium, Hard
        System.out.println("Select the difficulty :1)Easy\n2)Medium\n3)Hard");


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
                System.out.println("Welcome"+p1.name+" to world of quiz problems!");
                System.out.println("Select a catogory from below: ");
                System.out.println("1. General Knowledge\n2. Computer Technologies\n3. Science\n4. Mental Ability");
                int cat_choice; //for catogory choice 
                cat_choice = sc.nextInt();
                Quiz_Session quiz1= new Quiz_Session(p1, cat_choice);
                 
                
            }

        }
    }

}
