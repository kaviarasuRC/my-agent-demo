import java.sql.*;
import java.util.Scanner;

/**
 *  ATM Bank Program
 */
public class AtmProject {
    public static void main(String args[]){

        System.out.println("##############################################################################################");
        System.out.println("----------------------------------------------------------------------------------------------");
        System.out.println("\t\t\t\t\t\tWelcome to the ATM Mini Project");
        System.out.println("----------------------------------------------------------------------------------------------");
        System.out.println("##############################################################################################\n");
        Scanner scanner=new Scanner(System.in);

        boolean parentCheck=true;
        while (parentCheck){
            System.out.println("Please choose one of the below option to continue");
            System.out.println("================================================================================================");
            System.out.println("Press 1 to Perform transaction");
            System.out.println("Press 2 to Register a user");
            System.out.println("Press 3 to Exit");
            int parentOpt = scanner.nextInt();
            if(parentOpt == 1){
                doTransaction(scanner);
            }else if(parentOpt == 2){
                registerUser(scanner);
            }else if(parentOpt == 3){
                parentCheck=false;
                System.out.println("================================================================================================");
                System.out.println("Thanks for your time... Visit again...");
                System.out.println("Developed by Divya");
                System.out.println("================================================================================================");
            }else {
                System.out.println("********* Sorry, Invalid option *********\n");
            }
        }
    }

    /**
     * Method used to Perform transactions
     * @param scanner Sacnner object to get the User inputs
     * @return Returns boolean value
     */
    public static boolean doTransaction(Scanner scanner){
        int addAmount=0;
        int takeAmount=0;
        double availableBalance=0;
        boolean accountCheck = true;
        System.out.println("Enter your Debit card number");
        String dcNumber = scanner.next();
        while (accountCheck){
            if(dcNumber == null || dcNumber.isEmpty()){
                System.out.println("Please enter your Debit card number");
                System.out.println("Do you want to continue : Y/N");
                String cond= scanner.next();
                if(cond.equalsIgnoreCase("y")){
                    System.out.println("Please enter valid Debit card number");
                    dcNumber=scanner.next();
                    accountCheck=true;
                    continue;
                }
                else {
                    accountCheck=false;
                }
            } else if (dcNumber.length()<12 || dcNumber.length()>12) {
                System.out.println("********* Debit card number should be 12 Digit *********\n");
                System.out.println("Do you want to continue : Y/N");
                String cond= scanner.next();
                if(cond.equalsIgnoreCase("y")){
                    System.out.println("Please enter valid Debit card number");
                    dcNumber=scanner.next();
                    accountCheck=true;
                    continue;
                }
                else {
                    accountCheck=false;
                }
            } else {
                Connection connection = getConnection();
                try
                {
                    PreparedStatement preparedStatement = connection
                            .prepareStatement("select * from userDetails where debitCardNumber =?",
                                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    preparedStatement.setString(1,dcNumber);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    int rowCount=0;
                    if(resultSet != null && resultSet.last()){
                        rowCount = resultSet.getRow();
                        resultSet.beforeFirst();
                    }
                    if(rowCount <= 0){
                        System.out.println("********* No records found. Please check the Debit card number *********\n");
                        System.out.println("Do you want to continue : Y/N");
                        String cond= scanner.next();
                        if(cond.equalsIgnoreCase("y")){
                            System.out.println("Please enter valid Debit card number");
                            dcNumber=scanner.next();
                            accountCheck=true;
                            continue;
                        }
                        else {
                            accountCheck=false;
                            connection.close();
                        }
                    }
                    String userName="";
                    int pinNumber=0;
                    while (resultSet.next()){
                        pinNumber = resultSet.getInt(3);
                        userName = resultSet.getString(4);
                        availableBalance=resultSet.getDouble(7);
                    }
                    System.out.println("================================================================================================");
                    System.out.println("\t\t\t\tHi " +userName+ ", Welcome to the ATM");
                    System.out.println("================================================================================================\n");
                    System.out.println("Please Enter your PIN number");
                    int pwd=scanner.nextInt();

                    boolean exit = true;
                    while (exit){
                        if(pwd == pinNumber){
                            while(true){
                                System.out.println("Please choose one of the below option to continue");
                                System.out.println("================================================================================================");
                                System.out.println("Press 1 to check your balance");
                                System.out.println("Press 2 to Add amount");
                                System.out.println("Press 3 to Withdraw amount");
                                System.out.println("Press 4 to Take receipt");
                                System.out.println("Press 5 to Exit");

                                int opt=scanner.nextInt();
                                switch (opt){
                                    case 1:
                                        PreparedStatement balPStmt = connection
                                                .prepareStatement("select availableBalance from userDetails where debitCardNumber =?",
                                                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                                        balPStmt.setString(1,dcNumber);
                                        ResultSet balRes = balPStmt.executeQuery();
                                        balRes.next();
                                        availableBalance = balRes.getDouble(1);
                                        System.out.println("Your current balance is : "+availableBalance);
                                        System.out.println("================================================================================================");
                                        break;
                                    case 2:
                                        System.out.println("How much amount, Do you want to add to your account");
                                        addAmount=scanner.nextInt();
                                        availableBalance = addAmount+availableBalance;
                                        PreparedStatement updatePstmt = connection.prepareStatement("update userDetails set availableBalance=? where debitCardNumber=?");
                                        updatePstmt.setDouble(1,availableBalance);
                                        updatePstmt.setString(2,dcNumber);
                                        int updateCount = updatePstmt.executeUpdate();
                                        if(updateCount == 1){
                                            System.out.println("Amount added to your account successfully");
                                            System.out.println("================================================================================================\n");
                                        }
                                        break;
                                    case 3:
                                        System.out.println("How much do you want to withdraw");
                                        takeAmount=scanner.nextInt();
                                        if(takeAmount > availableBalance){
                                            System.out.println("********* Insufficient Balance in your account *********");
                                            System.out.println("Please try to withdraw less than available balance");
                                            System.out.println("================================================================================================\n");
                                        }else {
                                            System.out.println("Successfully withdrawn");
                                            availableBalance = availableBalance - takeAmount;
                                            PreparedStatement addAmtPstmt = connection.prepareStatement("update userDetails set availableBalance=? where debitCardNumber=?");
                                            addAmtPstmt.setDouble(1,availableBalance);
                                            addAmtPstmt.setString(2,dcNumber);
                                            int updateCount1 = addAmtPstmt.executeUpdate();
                                            if(updateCount1 == 1){
                                                System.out.println("Please take your amount : "+takeAmount);
                                                System.out.println("================================================================================================\n");
                                            }
                                        }
                                        break;
                                    case 4:
                                        PreparedStatement allPstmt = connection
                                                .prepareStatement("select * from userDetails where debitCardNumber =?",
                                                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                                        allPstmt.setString(1,dcNumber);
                                        ResultSet allRes = allPstmt.executeQuery();
                                        allRes.next();
                                        userName = allRes.getString(4);
                                        availableBalance = allRes.getDouble(7);

                                        System.out.println("\n\t\t\t\t Welcome to Mini ATM ");
                                        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                                        System.out.println("Username : "+userName);
                                        System.out.println("Available balance : "+availableBalance);
                                        System.out.println("Thanks for your visit. Have a great day");
                                        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
                                        break;
                                    default:
                                        if(opt != 5){
                                            System.out.println("********* Please choose the correct option *********");
                                        }
                                        break;
                                }
                                if(opt == 5){
                                    System.out.println("Logging off...");
                                    Thread.sleep(2000);
                                    System.out.println("\n================================================================================================");
                                    System.out.println("\t\t\t\tExiting from Transaction");
                                    System.out.println("================================================================================================\n");
                                    exit=false;
                                    accountCheck=false;
                                    break;
                                }
                            }
                        }else {
                            System.out.println("********* Invalid PIN. Please enter valid PIN number *********");
                            System.out.println("Do you want to continue : Y/N");
                            String cond= scanner.next();
                            if(cond.equalsIgnoreCase("y")){
                                System.out.println("Try again, Enter your PIN");
                                pwd=scanner.nextInt();
                                exit=true;
                                continue;
                            }
                            else {
                                return false;
                            }
                        }
                    }
                }catch (SQLException | InterruptedException sqle){
                    System.out.println("Exception occurred while performing transaction : "+sqle);
                    accountCheck=false;
                }
            }
        }
        return accountCheck;
    }

    /**
     * Method sed for User Registration
     * @param scanner Scanner object to get the user inputs
     */
    public static void registerUser(Scanner scanner){
        System.out.println("\n===========================================================================================");
        System.out.println("\t\t\t\t\t\tWelcome to User Registration");
        System.out.println("===========================================================================================\n");
        boolean userRegCheck=true;
        String dcNumber="";
        Integer pinNumber=0;
        while (userRegCheck){
            dcNumber = dbCardCheck(dcNumber, scanner);
            if(dcNumber == null || dcNumber.isEmpty()){
                userRegCheck=false;
                continue;
            }
            pinNumber = pinNumberCheck(pinNumber, scanner);
            if(pinNumber <= 0){
                userRegCheck=false;
                continue;
            }
            scanner.nextLine();
            System.out.println("Enter your Username");
            String uName = scanner.nextLine();
            System.out.println("Enter your Firstname");
            String fName = scanner.nextLine();
            System.out.println("Enter your Lastname");
            String lName = scanner.nextLine();
            System.out.println("Enter your Address");
            String address = scanner.nextLine();
            System.out.println("Enter your City");
            String city = scanner.nextLine();
            try{
                Connection connection = getConnection();
                PreparedStatement addUserPstmt = connection.prepareStatement("insert into userDetails(debitCardNumber, pinNumber, userName," +
                        " firstName, lastName, availableBalance, address, city) values (?,?,?,?,?,?,?,?)");
                addUserPstmt.setString(1,dcNumber);
                addUserPstmt.setInt(2,pinNumber);
                addUserPstmt.setString(3,uName);
                addUserPstmt.setString(4,fName);
                addUserPstmt.setString(5,lName);
                addUserPstmt.setDouble(6,5000.0);
                addUserPstmt.setString(7,address);
                addUserPstmt.setString(8,city);
                int updateCount1 = addUserPstmt.executeUpdate();
                if(updateCount1 == 1){
                    System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                    System.out.println("\t\t"+uName+", your registration completed successfully");
                    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
                    System.out.println("************************************************************************************************");
                    System.out.println("\t\t\t\t\tThanks for the registration");
                    System.out.println("************************************************************************************************\n");
                }
                connection.close();
                break;
            }catch (SQLException e){
                System.out.println("Exception occurred while Registering User : "+e);
            }
        }
    }

    public static String dbCardCheck(String dcNumber, Scanner scanner){
        System.out.println("Enter your debit card number");
        dcNumber = scanner.next();

        boolean dcCheck=true;
        int count=1;
        while (dcCheck){
            if (count >3){
                System.out.println("You have crossed the limited attempts. So exiting from User registration. Thanks");
                dcCheck=false;
                dcNumber="";
                continue;
            }
            if(count > 1){
                System.out.println("Please enter valid Debit card number");
                dcNumber = scanner.next();
            }
            if (dcNumber == null || dcNumber.length() != 12) {
                System.out.println("********* Debit card number length must be 12 *********\n");
                System.out.println("Do you want to continue : Y/N");
                String cond = scanner.next();
                if (cond.equalsIgnoreCase("y")) {
                    dcCheck = true;
                    count++;
                    dcNumber="";
                    continue;
                } else {
                    return "";
                }
            }
            Connection connection = getConnection();
            try{
                PreparedStatement preparedStatement = connection
                        .prepareStatement("select * from userDetails where debitCardNumber =?",
                                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                preparedStatement.setString(1,dcNumber);
                ResultSet resultSet = preparedStatement.executeQuery();
                int rowCount=0;
                if(resultSet != null && resultSet.last()){
                    rowCount = resultSet.getRow();
                    resultSet.beforeFirst();
                }
                if(rowCount >= 1){
                    System.out.println("********* Debit card number already exists *********\n");
                    System.out.println("Do you want to try the different one : Y/N");
                    String cond= scanner.next();
                    if(cond.equalsIgnoreCase("y")){
                        dcCheck=true;
                        count++;
                        continue;
                    }
                    else {
                        connection.close();
                        return "";
                    }
                }else {
                    return dcNumber;
                }
            }catch (SQLException e){
                System.out.println("Exception occurred while validating Debit card : "+e);
            }
        }
        return dcNumber;
    }

    public static Integer pinNumberCheck(Integer pinNumber,Scanner scanner){
        System.out.println("Enter your PIN number");
        pinNumber = scanner.nextInt();
        boolean pinCheck=true;
        int count=1;
        while (pinCheck){
            if(count > 3){
                System.out.println("You have crossed the limited attempts. So exiting from User registration. Thanks");
                pinNumber=0;
                pinCheck=false;
                continue;
            }
            if(count > 1){
                System.out.println("Please Enter your valid PIN number");
                pinNumber = scanner.nextInt();
            }
            if(pinNumber.toString().length() != 4){
                System.out.println("********* PIN number length must be 4 *********");
                System.out.println("Do you want to try it again : Y/N");
                String cond= scanner.next();
                if(cond.equalsIgnoreCase("y")){
                    pinCheck=true;
                    count++;
                    continue;
                }else {
                    return 0;
                }
            }
            break;
        }

        int confPinNumberCount=1;
        while (pinCheck){
            int confPinNumber =0;
            if(confPinNumberCount == 1){
                System.out.println("Re-Enter your PIN number to confirm");
                confPinNumber = scanner.nextInt();
            }
            if(confPinNumberCount > 3){
                System.out.println("You have crossed the limited attempts. So exiting from User registration. Thanks");
                pinNumber=0;
                pinCheck=false;
                continue;
            }
            if(confPinNumberCount > 1){
                System.out.println("Please Re-Enter your PIN number to confirm");
                confPinNumber = scanner.nextInt();
            }
            if(pinNumber != confPinNumber){
                System.out.println("********* PIN number doesn't match *********");
                System.out.println("Do you want to try it again : Y/N");
                String cond= scanner.next();
                if(cond.equalsIgnoreCase("y")){
                    pinCheck=true;
                    confPinNumberCount++;
                    continue;
                }else {
                    return 0;
                }
            }
            break;
        }
        return pinNumber;
    }

    /**
     * Method will return the Database connection Object
     * @return Returns the Connection Object
     */
    public static Connection getConnection(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/atm","root","root");
            return con;
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
