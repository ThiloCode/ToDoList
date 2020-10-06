
public class ModularIndex {
	private int numberOfItems;
	private int indexValue;
	
	public ModularIndex(int numberOfItems){
		this.numberOfItems = numberOfItems;
		if(numberOfItems > 0){
			indexValue = 0;
		}else{
			indexValue = -1;
		}
	}
	
	public void addItem(){
		if(numberOfItems == 0){
			indexValue = 0;
		}
		numberOfItems++;
	}
	
	public void removeItem(){
		if(numberOfItems != 0){
			numberOfItems--;
			if(indexValue != 0 || numberOfItems == 1){
				decrement();
			}else{
				indexValue = 0;
			}
		}
	}
	
	public void increment(){
		if(indexValue == numberOfItems - 1){
			indexValue = 0;
		}else{
			indexValue++;
		}
	}
	
	public void decrement(){
		if(indexValue >= 0){
			if(indexValue == 0){
				indexValue = numberOfItems - 1;
			}else{
				indexValue--;
			}
		}
	}
	
	public int getValue(){
		return indexValue;
	}
}
