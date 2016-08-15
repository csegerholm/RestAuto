Run - ReadMe
No special run files were used in the application.

Setting up the IDE: NOTE: Must be using jdk 1.8 or greater.
Step 1) Open Java IDE (such as eclipse).
Step 2) Start a new project.
Step 3) Copy all folders from code->java-> into yourNewProjectFolder->src
Step 4) Import Gson jar into your working project. (Gson jar can be found in code->gson-2.3.1.jar)

Setting up the databases:
If you are running the database, message controller, and application processes all on the same machine skip to step 4.
Step 1) Open domainNames.txt and portNumbers.txt (they can be found in the configuration folder)
Step 2) Change the domainNames file so that instead of localhost it now has your computer's IP Address.
Step 3) **OPTIONAL** Change the portNumbers file if you would like to select the port numbers the processes will use.

Step 4) Run databaseAController.java, databaseBController.java, databaseCController.java
Step 5) Run messageController.java
Step 6) Run TabletApp.java for each employee you would like to log in.
	Running TabletApp.java should open up the login screen. **See Notes

Step 7) Logging in as an employee:
Authentication is done by a unique employee ID.
In order to demo our application at this time, we have hardcoded some login IDs for your convenience. 
0->waiter -> has predefined tickets (1 with a hot food notification and food rang in already, and one empty ticket)
1->manager -> has no list of messages
2->chef -> has one predefined ticket
3->host -> all tables are set to ready
4->waiter -> has no tickets listed

Notes:
-Certain Features require multiple employee's to be logged in.
To use the notifyWaiter button on the Waiter's screen, a host must be logged in and running.

-Changing the menu, list of employees, or table list can all be found in code->data->ReadMeData.txt