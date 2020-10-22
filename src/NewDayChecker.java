import java.time.LocalDate;
public class NewDayChecker extends Thread{
	private LocalDate initializationDate;
	
	private ToDoList toDoList;
	
	
	
	public NewDayChecker(ToDoList toDoList){
		initializationDate = LocalDate.now();
		this.toDoList = toDoList;
	}
	
	public void run(){
		boolean quit = false;
		while(!quit){
			if(LocalDate.now().isAfter(initializationDate)){
				toDoList.newDayReset();
				initializationDate = LocalDate.now();
			}else{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					//System.out.println(e);
					quit = true;
				}
			}
		}
	}
	
	public void test(){
		LocalDate testDateAdvanced = LocalDate.of(3000, 1, 1);
		LocalDate testDateBehind = LocalDate.of(1900, 1, 1);
		if(testDateAdvanced.isAfter(initializationDate)){
			toDoList.newDayReset();
		}else{
			try {
				System.out.println("Day Checked!");
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.out.println(e);
			}
		}
	}
}
