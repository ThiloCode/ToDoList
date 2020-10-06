import java.io.Serializable;

public class Item implements Serializable{
	private boolean isSelected;
	private String content;
	
	public Item(String content){
		this.isSelected = false;
		this.content = content;
	}
	
	public void modifyContent(String newContent){
		content = newContent;
	}
	
	public void select(){
		isSelected = true;
	}
	
	public void deSelect(){
		isSelected = false;
	}
	
	public boolean isSelected(){
		return isSelected;
	}
	
	public String toString(){
		return content;
	}
}
