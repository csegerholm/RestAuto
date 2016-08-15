// written by: Christina Segerholm
// tested by: Christina Segerholm
// debugged by: Christina Segerholm
package dataBaseC;

import java.util.HashMap;

/**
 * This class is used to hold the ingredients of one dish on the menu.
 * It will hold the ingredients and the name of the dish
 * The Database C hold these so instead of having multiple copies of one dish 
 * with all of its ingredients we have each Dish's name linked to a DishData 
 * which will hold the info of that dish. This lowers data usage.
 * @author cms549
 */

public class DishData {
	/**
	 * Name of the dish
	 */
	String name;
	/**
	 * Ingredients in the dish
	 */
	HashMap<String, Ingredient> listOfIngredients;
	
	/**
	 * Amount of each Ingredient in the dish
	 */
	HashMap<String, Double> amtOfIngredient;
	
	/**
	 * Price of dish
	 */
	public double price;
	
	/**
	 * Creates a dish data with the name dishName.
	 * Makes an empty list of ingredients.
	 * Use addIngredient to add to this dish data
	 * @parameter dishName = name of dish you wish to add
	 */
	public DishData(String dishName){
		this.name = dishName;
		this.listOfIngredients= new HashMap<String, Ingredient>();
		this.amtOfIngredient = new HashMap<String, Double>();
	}
	
	/**
	 * Adds Ingredient to this Dishdata
	 * @param ingredientName- name of ingredient
	 * @param ingredientData - Ingredient obect 
	 * @return true on success, false if that ingredient was already added
	 */
	public boolean addIngredient(Ingredient ingredientData){
		String ingredientName = ingredientData.name;
		if(listOfIngredients.get(ingredientName) ==null){
			listOfIngredients.put(ingredientName,ingredientData);
			return true;
		}
		return false;
	}
	
	/**
	 * Gets the amount of ingredient that is used by this dish.
	 * @param ing - ingredient name
	 * @return amount the dish uses, -1.0 if that ingredient is not in the dish
	 */
	public double getAmount(String ing){
		Double d= amtOfIngredient.get(ing);
		if(d==null){
			d=-1.0;
		}
		return d;
	}

	/**
	 * Returns the ingredients used in this dish.
	 * @return ingredients used in this dish as an array.
	 */
	public String[] getListOfIngredients() {
		return listOfIngredients.keySet().toArray(null);
	}
	
	
}
