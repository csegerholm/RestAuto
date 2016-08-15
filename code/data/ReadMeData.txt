Code -> Data -> ReadMe

Since we did not have time to implement SQL server for the databases,
we used java classes and text files to hold the data.

In this directory you will find:
+employeeInfo.txt:
	This is the data needed for Database A. It is a list of all the employee's names 
as well as their positions. In our sample we made 5 employees. T
he format of the file is as follows:
	Name,Position
	Exp: Emma Ruossos,w 
		means there is an employee with the name Emma Ruossos who works as a waitress.

+menu.txt:
	This is the data needed for Database C. It is a list of all the dishes the menu contains.
It allows the owner to specify the type of dish, the dish's name, and the price.
he format of the file is as follows:
	TypeOfDish,NameOfDish,Price
	Exp: appetizer,Buffalo Wings,7.99
		means there is a dish called Buffalo Wings that is an appetizer and costs $7.99.
		
+tableNumber.txt:
	This is the data needed for Database B. It is a list of all the tables in the restaurant.
It allows the owner to specify the table number, max occupancy, and whether or not the table is
a booth.
he format of the file is as follows:
	TableNumber,NumberOfSeats,Booth(b)/Table(t)
	Exp: 1,4,t
		means tabler number 1 is not a booth and allows for a maximum of four guests to dine.


