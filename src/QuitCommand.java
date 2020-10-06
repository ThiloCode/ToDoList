
public class QuitCommand implements Command
{
	private ToDoList toDoList;
	
	public QuitCommand(ToDoList toDoList){
		this.toDoList = toDoList;
	}
	
	public void execute(){
		toDoList.clearScreen();
		toDoList.close();
		System.out.println("Bye!");
	}
}
