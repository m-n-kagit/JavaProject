import java.util.*;

class rough{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String email;
        String regex = "^[\\w]+@[\\w]+\\.[\\w]+$";
        
        email = sc.nextLine();
        if(email.matches(regex)){
            System.out.println("Yes");
        }
        else{
            System.out.println("No");
        }

    }
}