package mainpackage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import mainpackage.company.CompanyConfig;

import org.springframework.context.support.AbstractApplicationContext;
public class EngineersLoginApp {
	public static void main(String[] args) {
		AbstractApplicationContext ctx = new ClassPathXmlApplicationContext("engineersinfo.xml");
		Engineers engineers = ctx.getBean("engineers", Engineers.class);
		Scanner scanner = new Scanner(System.in);
		boolean loginFail = true;
		boolean ifNotExit = true;
		while (loginFail) {
			System.out.println("Enter 'exit' for both username and password if you want to exit.");
			System.out.println("Enter username: ");
	        String inputname = scanner.nextLine();
	        System.out.println("Enter password: ");
	        String inputpw = scanner.nextLine();
	        ifNotExit = !inputname.equals("exit") || !inputpw.equals("exit");
	        loginFail = engineers.login(inputname, inputpw) && ifNotExit;
		}
		if(loginFail == false && ifNotExit == true) {
			String csvFilePath = "C:\\Users\\berkehan\\Downloads\\Tasks.csv";
			String targetCsvFilePath = "C:\\Users\\berkehan\\Downloads\\ArrangedTasks.csv";
			ArrayList<String> arrangedTasks = new ArrayList<>();
	        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath));
	                BufferedWriter bw = new BufferedWriter(new FileWriter(targetCsvFilePath))) {
	            String line;
	            if ((line = br.readLine()) != null) {
	                String[] tasks = line.split(",");
	                arrangedTasks = arrangeTasks(tasks);
	                for (String x : arrangedTasks) {
		                bw.write(x);
		                bw.write(",");
		            }
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		}
		scanner.close();
		ctx.close();
	}

	private static ArrayList<String> arrangeTasks(String[] tasks) {
		ArrayList<String> task1List = new ArrayList<>();
        ArrayList<String> task2List = new ArrayList<>();
        ArrayList<String> task3List = new ArrayList<>();
        for (String task : tasks) {
            if (task.equals("Task1")) {
                task1List.add(task);
            } else if (task.equals("Task2")) {
                task2List.add(task);
            } else if (task.equals("Task3")) {
                task3List.add(task);
            }
        }
        ArrayList<String> result = new ArrayList<>();
        int totalHours = 0;
        while (!task1List.isEmpty() || !task2List.isEmpty() || !task3List.isEmpty()) {
            ArrayList<String> sequence = new ArrayList<>();
            while (!task3List.isEmpty() && totalHours + CompanyConfig.getHourOfTask3() <= 10) {
            	if (totalHours + CompanyConfig.getHourOfTask3() == 10) {
            		totalHours += CompanyConfig.getHourOfTask3();
            		sequence.add(task3List.remove(0));
            		break;
            	} else {
            		if (totalHours + 2*CompanyConfig.getHourOfTask3()-1 > 10) {
            			totalHours += CompanyConfig.getHourOfTask3();
            		} else {
            			if (task3List.size() == 1) {
            				totalHours += CompanyConfig.getHourOfTask3();
            			} else {
            				totalHours += CompanyConfig.getHourOfTask3()-1;
            			}
            		}
            	}
                sequence.add(task3List.remove(0));
            }
            while (!task2List.isEmpty() && totalHours + CompanyConfig.getHourOfTask2() <= 10) {
            	if (totalHours + CompanyConfig.getHourOfTask2() == 10) {
            		totalHours += CompanyConfig.getHourOfTask2();
            		sequence.add(task2List.remove(0));
            		break;
            	} else {
            		if (totalHours + 2*CompanyConfig.getHourOfTask2()-1 > 10) {
            			totalHours += CompanyConfig.getHourOfTask2();
            		} else {
            			if (task2List.size() == 1) {
            				totalHours += CompanyConfig.getHourOfTask2();
            			} else {
            				totalHours += CompanyConfig.getHourOfTask2()-1;
            			}
            		}
            	}
                sequence.add(task2List.remove(0));
            }
            while (!task1List.isEmpty() && totalHours + CompanyConfig.getHourOfTask1()-1 <= 10) {
            	if (totalHours + CompanyConfig.getHourOfTask1() == 10) {
            		totalHours += CompanyConfig.getHourOfTask1();
            		sequence.add(task1List.remove(0));
            		break;
            	} else {
            		totalHours += CompanyConfig.getHourOfTask1()-1;
            	}
                sequence.add(task1List.remove(0));
            }
            result.addAll(sequence);
            if (totalHours == 10) {
                totalHours = 0;
            }
        }
        return result;
	}
}