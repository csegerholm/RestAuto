Data Collection

Inventory
The inventory is a data collection that holds the list of ingredients as well 
as the amount of each ingredient that the restaurant has in stock. This data is 
updated each time a dish is started. When the chef changes the status of a dish
to started, the chef interface sends a message that includes the name of the dish
to Database C. From here Database C looks into the DishData table to find the 
dish with that name and then grabs all of the ingredients (and amount of each 
ingredient) used in preparing that dish. It then decrements these ingredients, 
each by the proper amount, in the inventory table of Database C. Upon 
decrementing it checks if the low inventory threshold is met for any of the 
ingredients. If one of the ingredients threshold is met then it will send a 
message to the message controller to alert the appropriate employees of this 
low inventory.

Records of Receipts/Total Revenue
The record of receipts is a data collection that holds all of the Tickets of 
a restaurant for one day. The collection is updated everytime a table pays their
bill. When the waiter presses the paid button, the waiter interface sends a 
message to DataBase C. From here Database C adds the ticket to the list of paid
tickets (aka receipts)for that day. At the end of the day, Database C will save
this list to a file labeled with the date and start a new one for the next 
day’s collection.

Please note there is no scripts to run yet since we have not implemented the SQL 
part of our project yet.
