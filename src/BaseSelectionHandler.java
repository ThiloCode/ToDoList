import java.util.HashMap;
import java.util.ArrayList;

public class BaseSelectionHandler {
	private HashMap<String, Command> rules = new HashMap<String, Command>();
	
	public BaseSelectionHandler(ToDoList toDoList){
		rules.put("s", new SelectCommand(toDoList));
		rules.put("a", new CreateItemCommand(toDoList));
		rules.put("q", new QuitCommand(toDoList));
	}
	
	public Command handle(String input) throws NoRuleException{
		if(rules.get(input) == null){
			throw new NoRuleException("BaseSelection", input);
		}else{
			return rules.get(input);
		}
	}
}
