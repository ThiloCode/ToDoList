import java.time.LocalDate;
public class NewDayChecker extends Thread{
	private LocalDate initializationDate;
	
	private ToDoList toDoList;
	
	public NewDayChecker(ToDoList toDoList){
		initializationDate = LocalDate.now();
		this.toDoList = toDoList;
	}
	
	public void run(){
		while(true){
			LocalDate testDateAdvanced = LocalDate.of(2021, 1, 1);
			LocalDate testDateBehind = LocalDate.of(2019, 1, 1);
			if(testDateAdvanced.isAfter(initializationDate)){
			//if(LocalDate.now().isAfter(initializationDate)){
				/*try{
					this.wait();
					toDoList.newDayReset();
				}catch(InterruptedException e){
					System.out.print(e);
				}*/
				
				//test code
				try {
					System.out.println("Day Checked!");
					Thread.sleep(10000);
					toDoList.newDayReset();
				} catch (InterruptedException e) {
					System.out.println(e);
				}
				//end tet code
				
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
}
