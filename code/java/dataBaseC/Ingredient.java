// written by: Christina Segerholm
// tested by: Christina Segerholm
// debugged by: Christina Segerholm
package dataBaseC;

/**
 * Data structure to hold each ingredient's data. 
 * Used by Database C to keep track of inventory.
 * Only have one object for each ingredient to keep memory usage low.
 * @author cms549
 */
public class Ingredient {
	
	/**
	 * Amount of ingredient left in inventory (units specified by unit)
	 */
	Double amountLeftInInventory;
	/**
	 * Unit of amount that is used in amount left in inventory and threshold fields
	 */
	String unit;
	/**
	 * Name of ingredient
	 */
	String name;
	/**
	 * Amount of ingredient that will cause a low inventory notification. (units specified by unit)
	 */
	Double threshold;
	
	/**
	 * creates new ingredient
	 * @param ingredientName
	 * @param amountLeft
	 * @param unitOfAmount
	 * @param threshold
	 */
	public Ingredient(String ingredientName,Double amountLeft, String unitOfAmount, Double threshold ){
		amountLeftInInventory=amountLeft;
		unit=unitOfAmount;
		name = ingredientName;
		this.threshold=threshold;
	}
	
	/**
	 * Gets the difference of the amount left in the invetory and the threshold
	 * @return the amount until this ingredient reaches its threshold, null on failure
	 */
	public Double checkThreshold(){
		if(amountLeftInInventory==null || threshold==null){
			return null;
		}
		return amountLeftInInventory - threshold;
	}
	
	/**
	 * Decrements this ingredient amount by the amount specified
	 * @param amount to decrement this ingredient by
	 * @return the amount left in the inventory on success, null on failure
	 */
	public Double decrementAmountBy(Double amount){
		if(amount==null){
			return null;
		}
		amountLeftInInventory = amountLeftInInventory - amount;
		return amountLeftInInventory;
	}
	
	
}
