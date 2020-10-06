
public class QuitCommand implements Command
{
	private Application app;
	
	public QuitCommand(Application app){
		this.app = app;
	}
	
	public void execute(){
		ScreenManager.clearScreen();
		app.close();
		ScreenManager.displayStatusMessage("Bye!");
	}
}
