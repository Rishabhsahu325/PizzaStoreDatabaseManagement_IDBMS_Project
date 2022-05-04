import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.io.FileInputStream;
import java.io.IOException;

//Table Schema creation by                          Naman Dhanotia      19ucs128
//table entry part and sql connection part  by      Rishabh Sahu        19ucs129
//table viewing part by                             Aditya Khandelwal   19ucs163
//table update part by                              Manit Mehta         19ucs147



class SQL_connector
{
    public static Connection getConnection()throws SQLException{
        Connection connect=null;
        //Connection parameters to the sql server like password and url are specified in the connector.properties file from where this class works
        try (FileInputStream f = new FileInputStream("connector.properties"))
        {

            // load the connector.properties file see this webpage for more info https://www.mysqltutorial.org/connecting-to-mysql-using-jdbc-driver/
            Properties fetch_database = new Properties();
            fetch_database.load(f);

            // assign  database connection parameters from connector.properties file containing the password to sql server
            String link = fetch_database.getProperty("url");
            String user = fetch_database.getProperty("user");
            String password = fetch_database.getProperty("password");

            // creating a connection to the database
            connect = DriverManager.getConnection(link, user, password);

        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
        return connect;
    }
}
// This program allows user to manage the tables and their data for a pizza store database which is already created in sql local host server

class Pizza_store
{

    static void Enter_values()
    {
        Scanner sc1 = new Scanner(System.in);

        //If  ENTER option is selected ask whether to enter data in orders table ,Products table ,Customers table ,Employees table
        System.out.println("Please specify the table in which you want to enter the data \n Type\n\t 1 for orders \n\t2 for products \n\t3 for Customers\n\t4 for Employees");

        int enter_option = sc1.nextInt();

        switch (enter_option)
        {
            //orders
            case (1):
            {

                ResultSet rs1 = null;
                int OrderID_point = 0;
                String entry = "INSERT INTO orders(Date_of_order,Time_of_order,CID,PID,Cost) " + "VALUES(?,?,?,?,?)";

                String str;
                String str2;
                Date Order_date;//date object
                Time Order_time;//time object
                int Order_Cid;
                int Order_Pid;
                float Order_cost;
                try (Connection GATE = SQL_connector.getConnection(); PreparedStatement order_record = GATE.prepareStatement(entry, Statement.RETURN_GENERATED_KEYS);) {
                    //order id is autoincrement in database schema of orders table so no need to enter manually
                    System.out.println("Enter the values of Order date in YYYY-MM-DD format,Order time in 24 Hour HH-MM-SS format,Customer Id,Product Id,Cost ,respectively\n");
                    System.out.println("Enter  Order date in YYYY-MM-DD format :\n");
                    Scanner sc = new Scanner(System.in);
                    str = sc.nextLine();

                    Order_date = Date.valueOf(str);
                    order_record.setDate(1, Order_date);//convert to date object

                    System.out.println("\nEnter time in HH:MM:SS format :\t");
                    Scanner sc2 = new Scanner(System.in);
                    str2 = sc2.nextLine();
                    Order_time = Time.valueOf(str2);//convert to time object
                    order_record.setTime(2, Order_time);

                    System.out.println("\nEnter the customer ID:\t");
                    Order_Cid = sc.nextInt();
                    //Will soon add the condition to check if this customerID exists already or not  , and if not then we will add a new customer record also
                    order_record.setInt(3, Order_Cid);

                    System.out.println("\nEnter the Product ID:\t");
                    Order_Pid = sc.nextInt();
                    order_record.setInt(4, Order_Pid);

                    //Need to choose whether to calculate cost from product automatically using sql query , or manually enter
                    System.out.println("\nEnter the value of Order_cost");
                    Order_cost = sc.nextFloat();
                    order_record.setFloat(5, Order_cost);


                    int insert_result;
                    insert_result = order_record.executeUpdate();//add this record to the sql database
                    if (insert_result == 1) {
                        // get last entered order id ,
                        rs1 = order_record.getGeneratedKeys();
                        if (rs1.next())
                            OrderID_point = rs1.getInt(1);//return the first attribute of currently entered result set which is the auto generated order id
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                } finally {
                    try {
                        if (rs1 != null)
                            rs1.close();
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                }

            }
            break;

            //products
            case (2):
            {

                    ResultSet rs2 = null;
                    int ProductID_point = 0;
                    String entry = "INSERT INTO products(Name,Cost,Rating) " + "VALUES(?,?,?)";//product id generates automatically

                    String str;

                    float Order_cost;
                    try (Connection GATE = SQL_connector.getConnection(); PreparedStatement product_record = GATE.prepareStatement(entry, Statement.RETURN_GENERATED_KEYS);) {
                        //order id is autoincrement in database schema of orders table so no need to enter manually
                        System.out.println("Enter the  Product Name for this pizza\n");

                        Scanner sc = new Scanner(System.in);
                        str = sc.nextLine();
                        product_record.setString(1, str);


                        int Product_rating;
                        System.out.println("\nEnter the Rating between 1 to 10 both inclusive ID:\t");
                        Product_rating = sc.nextInt();
                        //Will soon add the condition to check if this customerID exists already or not  , and if not then we will add a new customer record also
                        product_record.setInt(2, Product_rating);
                        float Product_cost;

                        System.out.println("\nEnter the Product Cost:\t");
                        Product_cost= sc.nextFloat();
                        product_record.setFloat(3, Product_cost);



                        int insert_result;
                        insert_result = product_record.executeUpdate();//add this record to the sql database
                        if (insert_result == 1) {
                            // get last entered order id ,
                            rs2 = product_record.getGeneratedKeys();
                            if (rs2.next())
                                ProductID_point = rs2.getInt(1);//return the first attribute of currently entered result set which is the auto generated order id
                        }
                    } catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                    } finally {
                        try {
                            if (rs2 != null)
                                rs2.close();
                        } catch (SQLException e) {
                            System.out.println(e.getMessage());
                        }
                    }

                }
            break;

            //Customers
            case (3): {

                ResultSet rs3 = null;
                int CustomerID_point = 0;
                String entry = "INSERT INTO customers(Address,Name,Phone_number) " + "VALUES(?,?,?)";//Customer id generates automatically

                String str;
                String str2;

                float Order_cost;
                try (Connection GATE = SQL_connector.getConnection(); PreparedStatement customer_record = GATE.prepareStatement(entry, Statement.RETURN_GENERATED_KEYS);) {
                    //order id is autoincrement in database schema of orders table so no need to enter manually
                    System.out.println("Enter the  Customer Name \n");

                    Scanner sc = new Scanner(System.in);
                    str = sc.nextLine();
                    customer_record.setString(2, str);

                    System.out.println("Enter the  Customer's Address \n");
                    str2 = sc.nextLine();
                    customer_record.setString(1, str2);

                    int Phone_number;
                    System.out.println("\nEnter the Phone number of the customer:\t");
                    Phone_number = sc.nextInt();
                    //Will soon add the condition to check if this customerID exists already or not  , and if not then we will add a new customer record also
                    customer_record.setInt(3,Phone_number );
                    int insert_result;
                    insert_result = customer_record.executeUpdate();//add this record to the sql database
                    if (insert_result == 1) {
                        // get last entered order id ,
                        rs3 = customer_record.getGeneratedKeys();
                        if (rs3.next())
                            CustomerID_point = rs3.getInt(1);//return the first attribute of currently entered result set which is the auto generated order id
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                } finally {
                    try {
                        if (rs3 != null)
                            rs3.close();
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
            break;

            //Employees
            case (4): {

                ResultSet rs4 = null;
                int EmployeeID_point = 0;
                String entry = "INSERT INTO Employees(Name,Phone_number) " + "VALUES(?,?)";//Employee id generates automatically

                String str;
                String str2;

                float Order_cost;
                try (Connection GATE = SQL_connector.getConnection(); PreparedStatement Employee_record = GATE.prepareStatement(entry, Statement.RETURN_GENERATED_KEYS);) {
                    //order id is autoincrement in database schema of orders table so no need to enter manually
                    System.out.println("Enter the  Employee Name \n");

                    Scanner sc = new Scanner(System.in);
                    str = sc.nextLine();
                    Employee_record.setString(1, str);

                    int Phone_number;
                    System.out.println("\nEnter the Phone number of the Employee:\t");
                    Phone_number = sc.nextInt();
                    //Will soon add the condition to check if this EmployeeID exists already or not  , and if not then we will add a new Employee record also
                    Employee_record.setInt(2,Phone_number );

                    int insert_result;
                    insert_result = Employee_record.executeUpdate();//add this record to the sql database
                    if (insert_result == 1) {
                        // get last entered order id ,
                        rs4 = Employee_record.getGeneratedKeys();
                        if (rs4.next())
                            EmployeeID_point = rs4.getInt(1);//return the first attribute of currently entered result set which is the auto generated order id
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                } finally {
                    try {
                        if (rs4 != null)
                            rs4.close();
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
            break;
            default:
                System.out.println("Didn't match with any of the existing option ,Please try again\n ");
        }
    }


    public static void View_data() {


        System.out.println("Please specify the table whose data you want to view-(1)orders,(2)products (3)Customers ,(4)Employees");
        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();
        switch (choice)
        {
            //orders
            case (1):
            {

                int data_option = 0;
                System.out.println("Enter how you want to see data by want the entire data(press 1) or do you want to search(press 2) with specific criteria");
                data_option = sc.nextInt();
                if (data_option == 1) //entire data of orders
                {
                    try (Connection GATE = SQL_connector.getConnection(); Statement stmt = GATE.createStatement(); ResultSet rs = stmt.executeQuery("SELECT *" + " from orders")) {
                        System.out.println("Contents of the table are: ");
                        int i=1;
                        while (rs.next()) {
                            System.out.println("Order number +"+i+"\n");
                            System.out.println("\t Date of ordering"+rs.getString("Date_Of_Order"));
                            System.out.println("\t Time of ordering"+rs.getString("Time_Of_Order"));
                            System.out.println("\t Cost of the order"+rs.getString("Cost"));
                            System.out.println("\n");
                            i++;

                        }
                    } catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                    }
                } else //searching data from user specification
                {

                    System.out.println("You can search by date");

                    try (Connection GATE = SQL_connector.getConnection(); Statement smt = GATE.createStatement()) {


                        System.out.print("Enter date: in YYYY-MM-DD pattern");
                        String date = sc.nextLine();

                        String q = "Select * from orders where Date_Of_Order='" + date + "'";

                        ResultSet rs = smt.executeQuery(q);
                        //to print the resultset on console
                        if (rs.next()) {
                            do {
                                System.out.println("OrderID:"+rs.getString(1)+"\nOrder date:"  + rs.getString(2) + "\nOrder time:" + rs.getString(3) + "\nCustomer ID who ordered:" + rs.getString(4) + "\nProduct Id of pizza which was ordered" + rs.getString(5) + "\nCost of the order:" + rs.getString(4)+"\n");
                            }
                            while (rs.next());
                        } else {
                            System.out.println("Record Not Found...");
                        }

                    } catch (Exception e) {
                        System.out.println(e);
                    }


                }
            }
            break;
            //Products
            case (2): {
                try (Connection GATE = SQL_connector.getConnection(); Statement stmt = GATE.createStatement();) {

                    //Creating a Statement
                    int entire_option = 0;
                    System.out.println("Enter how you want to see data by-By searching(type 2) or do want to see the entire data(type 1) then press enter");
                    entire_option = sc.nextInt();
                    if (entire_option == 1) {


                        //Retrieving the data
                        ResultSet rs = stmt.executeQuery("SELECT *" + " from products");
                        int i=1;
                        System.out.println("Contents of the table are: ");
                        while (rs.next()) {
                            System.out.println("Product number :"+i+"\n");
                            System.out.println("\t Pizza name: "+rs.getString("Name"));
                            System.out.println("\t Pizza MRP cost: "+rs.getString("Cost"));
                            System.out.println("\t Pizza Rating: "+rs.getString("Rating"));
                            i++;
                        }

                    }
                    else
                    {
                        int option = 0;
                        System.out.println("You can search by id(type 1) or by using Name(type 2) then press enter");
                        option = sc.nextInt();
                        if(option == 1)
                        {
                            //creating object of Scanner

                            Scanner sc2 = new Scanner(System.in);

                            System.out.print("Enter id: ");
                            int id = sc2.nextInt();

                            String q = "Select * from Products where PID= " + id;

                            ResultSet rs = stmt.executeQuery(q);
                            //to print the resultset on console
                            if (rs.next())
                            {
                                do
                                {
                                    System.out.println("Product Id: "+rs.getString(1)+"\n Pizza Name " + ":" + rs.getString(2)+"\n Cost:"  + "," + rs.getString(3) + "\nRating:" + rs.getString(4));
                                }
                                while (rs.next());
                            }
                            else
                            {
                                System.out.println("Record Not Found...");
                            }


                        }
                        else
                        {
                            //creating object of Scanner
                            Scanner sc2 = new Scanner(System.in);

                            System.out.print("Enter name: ");
                            String name = sc2.nextLine();

                            String q = "Select * from Products where Name = " + name;
                            ResultSet rs = stmt.executeQuery(q);
                            //to print the resultset on console
                            if (rs.next())
                            {
                                do
                                {

                                    System.out.println("Product Id: "+rs.getString(1)+"\n Pizza Name " + ":" + rs.getString(2)+"\n Cost:"  + "," + rs.getString(3) + "\nRating:" + rs.getString(4));
                                }
                                while (rs.next());
                            }
                            else
                            {
                                System.out.println("Record Not Found...");
                            }
                        }

                    }

                }
                catch (SQLException e)
                {
                    System.out.println(e);
                }

            }
            break;
            //Customers
            case (3):
            {
                int entire_choice = 0;
                System.out.println("Do u want to display entire data (type 1 then press enter) OR search particular record(type 2 then press enter)");
                entire_choice = sc.nextInt();
                if (entire_choice == 1) {
                    try (Connection GATE = SQL_connector.getConnection(); Statement stmt = GATE.createStatement())
                    {

                        //Retrieving the data
                        ResultSet rs = stmt.executeQuery("SELECT *" + " from customers");

                        System.out.println("Contents of the table are: ");
                        while (rs.next()) {
                            System.out.println(rs.getString("Name"));
                            System.out.println(rs.getString("Phone_Number"));
                            System.out.println(rs.getString("Address"));

                        }
                    }
                    catch (SQLException e) {
                        System.out.println(e);
                    }
                }
                else
                {
                    int option = 0;
                    System.out.println("You can search by id(press 1) or by using Name(press 2)");
                    option = sc.nextInt();
                    if (option == 1)
                    {

                        try (Connection GATE = SQL_connector.getConnection(); Statement smt = GATE.createStatement()) {
                            //creating object of Scanner

                            Scanner sc2 = new Scanner(System.in);

                            System.out.print("Enter id: \n");
                            int id = sc2.nextInt();

                            String q = "Select * from Customers where id = " + Integer.toString(id);

                            ResultSet rs2 = smt.executeQuery(q);
                            //to print the resultset on console
                            if (rs2.next()) {
                                do {
                                    System.out.println(rs2.getString(1) + "," + rs2.getString(2) + "," + rs2.getString(3) + "," + rs2.getString(4));
                                }
                                while (rs2.next());
                            } else {
                                System.out.println("Record Not Found...");
                            }
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                    else
                    {
                        try (Connection GATE = SQL_connector.getConnection(); Statement smt = GATE.createStatement())
                        {
                            //creating object of Scanner

                            Scanner sc2 = new Scanner(System.in);

                            System.out.print("Enter name: ");
                            String name = sc2.nextLine();

                            String q = "Select * from Customers where Name =" + name;

                            ResultSet rs3 = smt.executeQuery(q);
                            //to print the resultset on console
                            if (rs3.next()) {
                                do {
                                    System.out.println(rs3.getString(1) + "," + rs3.getString(2) + "," + rs3.getString(3) + "," + rs3.getString(4));
                                }
                                while (rs3.next());
                            } else {
                                System.out.println("Record Not Found...");
                            }

                        }
                        catch (Exception e)
                        {
                            System.out.println(e);
                        }

                    }

                }
            }
            break;
            //Employees table query
            case (4):
            {
                try (Connection GATE = SQL_connector.getConnection(); Statement stmt = GATE.createStatement()) {
                    int option = 0;
                    System.out.println("You can search by id or by using Name(press 1) or display entire info(press 0)");
                    option = sc.nextInt();
                    if (option == 0) {
                        //Retrieving the data
                        ResultSet rs = stmt.executeQuery("SELECT Name,Phone_Number" + " from Employees");

                        System.out.println("Contents of the table are: ");
                        while (rs.next()) {
                            System.out.println(rs.getString("Name"));
                            System.out.println(rs.getString("Phone_Number"));
                        }
                    }
                    else
                    {
                        int sub_option = 0;
                        System.out.println("You can search by id(press 1) or by using Name(press 2)");
                        sub_option = sc.nextInt();
                        if (option == 1)//search by id
                        {

                            Scanner sc2 = new Scanner(System.in);
                            System.out.print("Enter id: ");
                            int id = sc2.nextInt();
                            String q = "Select * from Employees where id = " + Integer.toString(id);
                            ResultSet rs = stmt.executeQuery(q);
                            //to print the resultset on console
                            if (rs.next()) {
                                do {
                                    System.out.println(rs.getString(1) + "," + rs.getString(2) + "," + rs.getString(3) + "," + rs.getString(4));
                                }
                                while (rs.next());
                            }
                            else {
                                System.out.println("Record Not Found...");
                            }

                        }
                        else //search by name
                        {
                            Scanner sc2 = new Scanner(System.in);

                            System.out.print("Enter name: ");
                            String name = sc2.nextLine();

                            String q = "Select * from Employees where Name = " + name;

                            ResultSet rs = stmt.executeQuery(q);
                            //to print the resultset on console
                            if (rs.next()) {
                                do {
                                    System.out.println(rs.getString(1) + "," + rs.getString(2) + "," + rs.getString(3) + "," + rs.getString(4));
                                }
                                while (rs.next());
                            } else {
                                System.out.println("Record Not Found...");
                            }

                        }

                    }
                }
                catch (SQLException ex)
                {
                    System.out.println(ex);
                }
            }
            break;

            default:
            {
                System.out.println("You had to  choose among (1-4)!! ");
            }
        }
    }
    public static void update_data() {

        System.out.println("Please specify the table whose data you want to update\n(Type 1 then press enter for)ORDERS\n(Type 2 then press enter for)PRODUCTS\n(Type 3 then press enter for)CUSTOMERS\n(Type 4 then press enter for)EMPLOYEES\n");
        Scanner sc = new Scanner(System.in);
        int table_select = sc.nextInt();
        switch (table_select) {
            //update orders table
            case (1): {

                String sqlUpdate = "UPDATE orders "
                        + " SET  PID = ? "
                        + " where OrderID = ? ";
                try (Connection GATE = SQL_connector.getConnection(); PreparedStatement pstmt = GATE.prepareStatement(sqlUpdate);) {

                    System.out.print("enter OrderID whose record is to be updated");
                    int OrderID = sc.nextInt();

                    System.out.print("enter productID to be updated in this record");
                    int newproduct = sc.nextInt();
                    int PID = newproduct;

                    pstmt.setInt(1, PID);
                    pstmt.setInt(2, OrderID);
                    int rowAffected = pstmt.executeUpdate();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            break;
            //Update products table
            case (2): {
                System.out.print("enter ProductID whose record is to be updated");
                int Product_ID = sc.nextInt();
                System.out.print("Please select: (a)update product name,(b)update product price");
                char u = sc.next().charAt(0);
                switch (u) {

                    case ('a'): {

                        String sqlUpdate = " UPDATE products "
                                + " SET NAME = ? "
                                + " where ProductID= ? ";
                        try (Connection Gate2 = SQL_connector.getConnection();PreparedStatement pstmt = Gate2.prepareStatement(sqlUpdate);) {

                            System.out.print("enter new product name");
                            String newname = sc.nextLine();
                            String NAME = newname;

                            pstmt.setString(1, NAME);
                            pstmt.setInt(2, Product_ID);
                            int rowAffected = pstmt.executeUpdate();
                        } catch (SQLException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                    break;
                    case ('b'): {
                        String sqlUpdate = "UPDATE products "
                                + " SET Cost = ? "
                                + " where PID = ? ";
                        try (Connection Gate = SQL_connector.getConnection();PreparedStatement pstmt = Gate.prepareStatement(sqlUpdate);) {

                            System.out.print("enter new product cost");
                            float newcost = sc.nextFloat();
                            float cost = newcost;

                            pstmt.setFloat(1, cost);
                            pstmt.setInt(2, Product_ID);
                            int rowAffected = pstmt.executeUpdate();
                        } catch (SQLException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                    break;
                    default: {
                        System.out.print("enter legal value");
                    }
                }
            }

            break;
            //Customers table update
            case (3): {
                System.out.print("Please select: (a)update customer adress,(b)update customer phone number");

                char parameter_select = sc.next().charAt(0);
                switch (parameter_select) {
                    case ('a'): {
                        String sqlUpdate = "UPDATE Customers "
                                + " SET Address = ? "
                                + " where CID = ? ";
                        try (Connection Gate = SQL_connector.getConnection(); PreparedStatement pstmt = Gate.prepareStatement(sqlUpdate);) {

                            System.out.print("enter new address");
                            String newaddress = sc.nextLine();
                            String address = newaddress;
                            int CustomerID = 2;
                            pstmt.setString(1, address);
                            pstmt.setInt(2, CustomerID);
                            int rowAffected = pstmt.executeUpdate();
                        } catch (SQLException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                    break;
                    case ('b'): {
                        String sqlUpdate = "UPDATE products "
                                + " SET Phone_NO = ? "
                                + " where CID = ? ";
                        try (Connection Gate = SQL_connector.getConnection(); PreparedStatement pstmt = Gate.prepareStatement(sqlUpdate);) {

                            System.out.print("enter new phone number");
                            int newnumber = sc.nextInt();
                            int Phone_NO = newnumber;
                            int CustomerID = 2;
                            pstmt.setInt(1, Phone_NO);
                            pstmt.setInt(2, CustomerID);
                            int rowAffected = pstmt.executeUpdate();
                        } catch (SQLException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                    break;
                    default: {
                        System.out.print("Value entered does not match given list of options");
                    }
                }
            }
            break;

            //Employees table update
            case (4):
            {
                System.out.print("Please select: (a)update employee address,(b)update employee phone number");
                char parameter_select = sc.next().charAt(0);
                switch (parameter_select)
                {
                    case ('a'):
                    {
                        String sqlUpdate = "UPDATE Employees "
                                           + " SET Address = ? "
                                        + " where EID = ? ";
                        try (Connection Gate = SQL_connector.getConnection();PreparedStatement pstmt = Gate.prepareStatement(sqlUpdate))
                        {
                                    System.out.print("enter new address");
                                    String newaddress = sc.nextLine();
                                    String address = newaddress;
                                    int Employee_ID = 2;
                                    pstmt.setString(1, address);
                                    pstmt.setInt(2, Employee_ID);
                                    int rowAffected = pstmt.executeUpdate();

                        }

                        catch (SQLException ex)
                        {
                            System.out.println(ex.getMessage());
                        }
                    }
                    break;
                    case ('b'):
                    {
                        String sqlUpdate = "UPDATE products "
                                        + " SET Phone_NO= ? "
                                        + " where EID=?;";
                        try (Connection Gate = SQL_connector.getConnection();PreparedStatement pstmt = Gate.prepareStatement(sqlUpdate))
                        {
                            System.out.print("enter new phone number");
                            int newnumber = sc.nextInt();
                            int Phone_NO = newnumber;
                            int EmployeeID = 2;
                            pstmt.setInt(1, Phone_NO);
                            pstmt.setInt(2, EmployeeID);
                            int rowAffected = pstmt.executeUpdate();
                        }
                        catch (SQLException ex)
                        {
                            System.out.println(ex.getMessage());
                        }
                    }
                    break;

                    default: {
                                System.out.print("enter legal value");
                            }
                }
            }
            break;
            default: {
                        throw new java.lang.IllegalStateException("Unexpected value: " + table_select);
                    }
        }
    }



    //}
    public static void main(String[] Args)
    {
        //Part to test sql connection to database,before user interaction
        try (Connection check_connection = SQL_connector.getConnection())
        {
            // print out a message
            System.out.println(String.format("Connected to database %s "
                    + "successfully.", check_connection.getCatalog()));
        }
        catch (SQLException ex)
        {

            System.out.println(ex.getMessage());
            /*if connection is unsuccessful then we display error message and system administrator can check for problems
            encountered while accessing the database*/
            return;
        }
        // If connection is successful then user interaction begins
        int option=5;
        while(option!=6) {
            System.out.println("Type 1 for Entering values in existing tables OR\nType 2 for Viewing data in existing tables OR\nType 3 for Updating existing data present in tables\n Type 6 to exit program");
            Scanner sk = new Scanner(System.in);
            option = sk.nextInt();

            switch (option) {
                // Take new values from user
                case (1):
                    Enter_values();
                    break;
                //view existing data in tables on user specified parameters
                case (2):
                    View_data();
                    break;
                // update existing data in tables
                case (3):
                    update_data();
                    break;
                case(6):{
                    System.out.println("Exiting program\n");
                    return;
                }
                default: {
                    System.out.println("You had to choose from among 1 ,2 ,3 or 6 Since you have chosen option as " + option + " the program will now terminate");
                    return;
                }
            }
        }
    }
}