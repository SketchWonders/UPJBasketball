/*
* Basketball Program that adds accessories to players to order
* Authors: Babl, Joshua; Ferraro, Domenic; Good, Emily; Peschock, Austin; Wonders, Ashley

* ISSUE: Orders may come in backwards Time-Wise
 */

import java.io.*;
import java.util.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class BasketballA {
    //File playerData = new File("PlayerData.txt");
    //name description cost points
    //File accessoryData = new File("AccessoryData.txt");
    //name description cost points
    //File receiptsData = new File ("ReceiptData.txt");
    //Date, player and accessories (description), customer name, individual accessory prices, total price)

    //Global variables
    public static Stack<Accessories> stack = new Stack<Accessories>(); //Stack to keep track of accessories added in order
    public static Scanner sc = new Scanner(System.in); //Scanner

    public static ArrayList<Accessories> acc = new ArrayList<>(); //Array list for the accessories 
    public static ArrayList<Players> pList = new ArrayList<>(); //Array list for the players
	public static ArrayList<String> receipt = new ArrayList<>(); //Array list for the receipts 

    public static void main(String args[]) {
        String choice = "0"; //determines user type: 1 customer, 2 manager, 3 quit
        String name = ""; //customer's name
        String order = ""; //customer's order so far
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"); //current time and date for receipts
        File people = new File("Player.txt"); //file to store all players

        if (people.exists()) {
            //System.out.println("Exists");
            getPlayers(people); //Fills in the players to the array list from the file
        } else {
            //System.out.println("Made new");
            //players = new Players[100]; //set new arrayList
        }

        File equipment = new File("Accessory.txt"); //file to store all accessories
        if (equipment.exists()) {
            //System.out.println("Exists");
            getAccessories(equipment); //Fills in the accessories to the array list from the file
        } else {
            //System.out.println("Made new");
        }
		
		File receipts = new File("receipts.txt"); //file to store all receipts
		if(receipts.exists()){
			//System.out.println("Exists");
			getReceipts(receipts); //Fills in the receipts to the array list from the file
		} else{
			//System.out.println("Made new");
		}

         /*Determine user type*/
        while (choice.charAt(0) != '3') {
            System.out.println("Welcome. Press 1 for regular user. Press 2 for manager. Press 3 to quit.");
            choice = sc.nextLine();
            if (choice.length() > 1) {
                System.out.println("Input too long. Pick 1, 2, or 3");
            } else {
                /*
				* Customer
                 */
                if (choice.charAt(0) == '1') {
                    String correct = "0"; //determines if name is entered correctly
                    while (correct.charAt(0) != '1') {
                        System.out.println("Please enter your name for our records:");
                        name = (sc.nextLine()).toUpperCase(); //put to all caps
                        System.out.println(name);
                        System.out.println("Is " + name + " correct? Press 1 if yes, 0 if no.");
                        correct = sc.nextLine();
                        if (correct.length() > 1 || (correct.charAt(0) != '0' && correct.charAt(0) != '1')) {
                            System.out.println("Incorrect input. Please re-enter your name");
                        } else if (correct.charAt(0) == '0') {
                            System.out.println("Sorry about that. Please re-enter your name.");
                        } else {
                            //name is correct
                        }
                    }

                    LocalDateTime now = LocalDateTime.now(); //get Current Date for receipt
                    order = order + now;
                    order = order.substring(0, 10); //takes off Exact time to only leave the date MM/DD/YYYY

                    //Show menu of players
                    String p = "*"; //Customer's selection
                    int plyr = 0; //index of player
                    while (p.charAt(0) == '*') {
						System.out.println("Please indicate which Player you would like by inputting the corresponding number: ");
                        displayPlayersMenu();
                        p = (sc.nextLine());
                        try {
                            plyr = Integer.parseInt(p);
                            //Player is at players[plyr] - default to first entry.
                            //for now, assume correct.
							if(plyr < 0){
								System.out.println("Invalid choice, please try again.");
								p = "*";
							}
                        } catch (Exception e) { //NumberFormatException?
                            System.out.println("Incorrect input. Please re-enter your choice");
                            p = "*";
                        }
                    }
                    Players ordered = pList.get(plyr); //Customer's player
                    //Display User Options. -- Add accessory or complete order
                    boolean completeOrder = false;
                    while (!completeOrder) {
                        System.out.println("What would you like to do, " + name + "?");
                        String comp; //Customer's choice
                        System.out.println("Press 1 to add an accessory. Press 2 to complete your order.");
                        comp = sc.nextLine();
                        if (comp.length() > 1 || (comp.charAt(0) != '1' && comp.charAt(0) != '2')) {
                            System.out.println("Invalid input.");
                        } else if (comp.charAt(0) == '2') {
                            completeOrder = true;
                        } else {
                            String a = "*";
                            int access = 0;
                            while (a.charAt(0) == '*') {
								System.out.println("Please indicate which Accessory you would like by inputting the corresponding number: ");
                                displayAccessoriesMenu(); //acc.toArray - makes Object array? or just acc?
                                a = (sc.nextLine());
                                if (p.length() > 1) {
                                    System.out.println("Incorrect input. Please re-enter your choice");
                                    a = "*";
                                }
                                try {
                                    access = Integer.parseInt(a);
                                    //Player is at players[plyr] - default to first entry.
                                    //System.out.println("Is "+ players[plyr].name +" correct?");
                                    //for now, assume correct.

                                } catch (Exception e) { //NumberFormatException?
                                    System.out.println("Incorrect input. Please re-enter your choice");
                                    a = "*";
                                }
                            }
                            ordered = addAccessory(ordered, acc.get(Integer.parseInt(a)));
                            System.out.printf("Player: %s cost:$%.2f points:%d\n", ordered.description, ordered.cost, ordered.points);
                        }
                    }
                    String accList = "";
                    String accCosts = "";
					double tempCost;
                    while (!stack.empty()) {
						if(stack.size()>1){
							accList = accList + stack.peek().name + ", "; //want to somehow check for if more than one
							tempCost = stack.pop().cost;
							//tempCost = String.format("%02f", tempCost);
							accCosts = accCosts + tempCost + ", ";
						}
						else{
							accList = accList + stack.peek().name; //want to somehow check for if more than one
							tempCost = stack.pop().cost;
							//tempCost = String.format("%02f", tempCost);
							accCosts = accCosts + tempCost;
						}
                    }

                    order = order.format("%s;%s;%s;%s;%s;$%.2f", order, pList.get(plyr).name, accList, name, accCosts, ordered.cost);
                    System.out.printf("You ordered: %s for $%.2f to get %d points per game.\n", ordered.description, ordered.cost, ordered.points);
					System.out.println("Thank you for your order");
					receipt.add(order);
                    writeOrdersFile(order);

                } 
				/* Manager*/
                else if (choice.charAt(0) == '2') {
                    String mgChoice = "9";
                    int mgch = 8;
                    //give choices
                    while (!mgChoice.equals("7")) {
                        System.out.println("\nWhat would you like to do? Press the corresponding number.");
                        System.out.println("0: Read in file. \n1: Save file. \n2: Add new Accessory.");
                        System.out.println("3: Add new Player. \n4: Remove Accessory. \n5: Remove Player.");
                        System.out.println("6: Print Reports. \n7:Quit");
                        mgChoice = sc.nextLine();
                        //System.out.println(mgChoice);
                        try {
                            mgch = Integer.parseInt(mgChoice);
                        } catch (Exception e) {
                            System.out.println("Invalid input. Please enter a number from 0 to 7.");
                            sc.nextLine();
                        }
                        if (mgch >= 0) {
                            switch (mgch) {
                                case 0:
                                    getPlayers(people);
									System.out.println("Players:");
									displayPlayersMenu();
									/*
                                    for (int i = 0; i < pList.size(); i++) {
                                        System.out.println(i + ": " + pList.get(i).description + " Cost:" + pList.get(i).cost + " Points: " + pList.get(i).points);
                                    }
									*/
									System.out.println("\nAccessories:");
                                    getAccessories(equipment);
									displayAccessoriesMenu();
									/*
                                    for (int i = 0; i < acc.size(); i++) {
                                        System.out.println(i + ": " + acc.get(i).description + " Cost:" + acc.get(i).cost + " Points: " + acc.get(i).points);
                                    }
									*/
									System.out.println("\nOrders:");
									for(int i = receipt.size()-1; i>=0; i--){
										System.out.println(i+ ": " + receipt.get(i));
									}
                                    break; //read in files
                                case 1:
                                    writeToFile();
                                    break; //save files
                                case 2:
                                    addAccessory();
                                    sc.nextLine();
                                    break; //add accessory
                                case 3:
                                    addPlayer();
                                    sc.nextLine();
                                    break; //add player
                                case 4:
                                    removeAccessory();
                                    sc.nextLine();
                                    break; //remove accessory
                                case 5:
                                    removePlayer();
                                    sc.nextLine();
                                    break; //remove player
                                case 6:
                                    printReport();
									//sc.nextLine();
                                    break;//print report
                                case 7:
                                    break; //quit
                                default: //catches values greater than 7
                                    System.out.println("Invalid input");
                            }
                        } else {
                            System.out.println("Invalid input");
                        }
                    }
                } else if (choice.charAt(0) == '3') {
                    //quit
                    break;
                } else {
                    System.out.println("Invalid input. Pick 1, 2, or 3");
                }
            }
        }
    }

    //add accessory
    public static void addAccessory() { //
       try{
		String name;
        String desp;
        double cost;
        int points;
        System.out.println("Enter name of new accessory: ");
        name = sc.nextLine();
        System.out.println("Enter description of new accessory: ");
        desp = sc.nextLine();
        System.out.println("Enter cost of new accessory: ");
        cost = sc.nextDouble();
        System.out.println("Enter point value of new accessory: ");
        points = sc.nextInt();

        Accessories ac = new Accessories(name, desp, cost, points);
        acc.add(ac);
		}catch (InputMismatchException e) {
					System.out.println("\nPlease enter a number: ");
				}
				
    }

    //add player
    public static void addPlayer() {
		try{
        String name;
        String desp;
        double cost;
        int points;
        System.out.println("Enter name for Player type:");
        name = sc.nextLine();
        System.out.println("Enter a description for player:");
        desp = sc.nextLine();
        System.out.println("Enter a cost for player:");
        cost = sc.nextDouble();
        System.out.println("Enter a point value for the player:");
        points = sc.nextInt();
        Players p = new Players(name, desp, cost, points);
        pList.add(p);
		}catch (InputMismatchException e) {
					System.out.println("\nPlease enter a number: ");
				}
    }

    //remove accessory
    public static void removeAccessory() {
		try{
        int pick;
        displayAccessoriesMenu();
        System.out.println("Enter the number of the accessory you want to remove: ");
        pick = sc.nextInt();
        while (pick > acc.size()-1 || pick < 0) {
			displayAccessoriesMenu();
            System.out.println("Please pick a number from the list: ");
            pick = sc.nextInt();
        }
        acc.remove(pick);
		}catch (InputMismatchException e) {
					System.out.println("\nPlease enter an integer: ");
				}
    }

    //remove player
    public static void removePlayer() {
		try{
        int pick;
        displayPlayersMenu();
        System.out.println("Enter the number of the player you want to remove: ");
        pick = sc.nextInt();
        while (pick > pList.size()-1 || pick < 0) {
            System.out.println("Please pick a number from the list: ");
            pick = sc.nextInt();
        }
        pList.remove(pick);
		}catch (InputMismatchException e) {
					System.out.println("\nPlease enter an integer: ");
				}
    }

    //print report
    public static void printReport() {
		System.out.println("\nOrders: ");
        try{
			BufferedReader br = new BufferedReader(new FileReader("receipts.txt"));
			String line = null;
			while ((line = br.readLine()) != null){
			System.out.println(line);
			}
		} catch (FileNotFoundException e) {
					System.out.println("Source file not found");
				}
				catch (IOException ioe)
				{
				ioe.printStackTrace();
				}


    }

    public static Players addAccessory(Players p, Accessories a) {
        Players addedAccessPlayer = new Players();
        addedAccessPlayer.name = p.name + " with " + a.name;
        addedAccessPlayer.description = p.description + " with " + a.description;
        addedAccessPlayer.cost = p.cost + a.cost;
        addedAccessPlayer.points = p.points + a.points;
        System.out.println(a.name);
        stack.push(a);
        return addedAccessPlayer;
    }

    public static void displayPlayersMenu() {
        //change to arraylist
        //System.out.println("Please indicate which Player you would like by inputting the corresponding number: ");
        //to list
        for (int i = 0; i < pList.size(); i++) {
            System.out.println(i + ": " + pList.get(i).description + " Cost:" + pList.get(i).cost + " Points: " + pList.get(i).points);
        }
    }

    public static void displayAccessoriesMenu() {
        //System.out.println("Please indicate which Accessory you would like by inputting the corresponding number: ");
        //to list
        for (int i = 0; i < acc.size(); i++) {
            System.out.println(i + ": " + acc.get(i).description + " Cost:" + acc.get(i).cost + " Points: " + acc.get(i).points);
        }
    }

    public static void getPlayers(File inputFile) {
        //set list and array?
        //reset values
        pList.clear();

        try
        {

        			BufferedReader br = new BufferedReader(new FileReader(inputFile));
        			String fileRead = br.readLine();
        			// loop until all lines are read
        			while (fileRead != null)
        			{
        				// use string.split to seperate with ;
        				String[] tokenize = fileRead.split(";");
        				// and make temporary variables for the four types of data
        				String tempname = tokenize[0];
        				String tempdesc = tokenize[1];
        				double tempprice = Double.parseDouble(tokenize[2]);
                int temppoints = Integer.parseInt(tokenize[3]);
        				// and load with four data values
        				Players p = new Players(tempname, tempdesc, tempprice, temppoints);
        				// add to array list
        				pList.add(p);
        				// read next line before looping
        				// if end of file reached
        				fileRead = br.readLine();
        			}
        			// close file stream
        			br.close();
        		}
        		// handle exceptions
        		catch (FileNotFoundException fnfe)
        		{
        			System.out.println("file not found");
        		}
        		catch (IOException ioe)
        		{
        			ioe.printStackTrace();
        		}
        //return menu;
    }

    public static void getAccessories(File inputFile) {
        //set list and array?
        //reset the values
        acc.clear();


        String[] inputArr = readFileRet(inputFile);
        Accessories[] menu = new Accessories[inputArr.length];
        for (int i = 0; inputArr[i] != null; i++) {
            String[] tokens = (inputArr[i]).split(";");
            //for (int l = 0; l< tokens.length; l++){
            //	System.out.println(tokens[l]);
            //}
            Accessories a = new Accessories();
            a.setName(tokens[0]);
            a.setDescription(tokens[1]);
            a.setCost(Double.parseDouble(tokens[2])); //may not be int

            a.setPoints(Integer.parseInt(tokens[3]));
            menu[i] = a;
            acc.add(a);
            //System.out.println(menu.length);
        }
        //for(int i = 0; menu[i]!=null; i++){
        //	System.out.println((menu[i]).name);
        //}
        //return menu;
    }
	
	public static void getReceipts (File inputFile){
		if(!inputFile.exists()){
			System.out.println("Input file does not exist");
			System.exit(1);
		}
		try{
			Scanner fin = new Scanner (inputFile);
			while (fin.hasNextLine()){
				receipt.add(fin.nextLine());
			}
			fin.close();
		}
		catch(FileNotFoundException e){
			System.out.println("Source File not found.");
		}
	}

    public static String[] readFileRet(File inputFile) {
        String input;
        String name;
        String description;
        int cost;
        int points;
        String[] arr = new String[1000];  //buffer?

        //System.out.println("Got here");
        if (!inputFile.exists()) {
            //will want to make a new file
            System.out.println("Input file does not exist");
            //System.exit(1);
        }
        try {
            int numOfGrades = 0;
            Scanner fin = new Scanner(inputFile);
            //System.out.println("File Exists...");
            int i = 0;
            while (fin.hasNextLine()) {
                input = fin.nextLine();
                arr[i] = input;
                i++;
            }
            fin.close();
        } catch (FileNotFoundException e) {
            System.out.println("Source file not found");
        }
        return arr;
    }

    public static void writeToFile() {
        //write all three files - basically save
        //saving the accessory.txt
			try (PrintWriter out = new PrintWriter("Accessory.txt")){
				for (int i = 0; i < acc.size(); i++) {
            out.println(acc.get(i).name + ";" + acc.get(i).description + ";" + acc.get(i).cost + ";" + acc.get(i).points);
        }
				out.close();
	    } catch (FileNotFoundException e) {
					System.out.println("Source file not found");
			}
      //when saves it shows new menu

					//saving the player.txt
				try (PrintWriter out = new PrintWriter("Player.txt")){
				for (int i = 0; i < pList.size(); i++) {
					out.println(pList.get(i).name + ";" + pList.get(i).description + ";" + pList.get(i).cost + ";" + pList.get(i).points);
				}
				out.close();
			} catch (FileNotFoundException e) {
					System.out.println("Source file not found");
			}
      //when saves it shows new menu
    }
    public static void writeOrdersFile(String order){
		File reports = new File("receipts.txt");
		if(!reports.exists()){
			reports = new File("receipts.txt");
		}
		else{
			reports.delete();
			reports = new File("receipts.txt");
		}
		try{
			FileWriter f2 = new FileWriter(reports, false);
			for( int i = receipt.size()-1; i >=0; i--){
				f2.write(receipt.get(i)+"\n");
			}
			f2.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	  /*
	  try {
				//info to be added adds to the top for easy reports

        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("Orders.txt", true)));
        out.println(order);
        out.flush();
        out.close();
	    } catch (FileNotFoundException e) {
					System.out.println("Source file not found");
			}
      catch (IOException ioe)
      {
        ioe.printStackTrace();
      }
	  */
    }
}
