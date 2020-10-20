import java.util.concurrent.TimeUnit;

public class AchieveItemCommand implements Command{
	private ToDoList toDoList;
	
	private String[] praise = {"Good Job!", "Nice work!", "Woohoo!"};
	
	
	public AchieveItemCommand(ToDoList toDoList){
		this.toDoList = toDoList;
	}
	
	public void execute(){
		
		ScreenManager.displayStatusMessage(praise[(int)(Math.random() * 3)]);
		toDoList.achieveItem();
		
		try {
			TimeUnit.SECONDS.sleep((long)(1));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
