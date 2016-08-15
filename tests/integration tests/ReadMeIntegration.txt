Integration Tests
This readMe describes our integration tests and how to run them.

For all of our interfaces: chef, host, manager and waiter, most of the testing
was done visually. We tested each interface step by step and determined that 
each function worked properly. Using print statements, we were able to determine
exactly where the code failed and could pinpoint the locations of the errors. 
We used unit testing to check to make sure each function gave the correct 
results and handled any errors. Once all functions were working individually, 
we incorporated integration testing. With integration testing we tested 
scenarios such as a host changing a table status from ready to seated or a 
waiter adding a dish to the ticket. Through integration testing we were able to
confirm that all of our functions worked sequentially with other functions and 
delivered the correct results when tested.


To run the Database File Reading integration test:
Run DataLoadFromFileTesting.java
This test will test all three text files that hold the data needed for the database processes to run. These are the text files in the Configuration folder: 
employeeInfo.txt --- Loaded into DataBaseA
tableNumbers.txt ---  Loaded into DataBaseB
menu.txt   --- Loaded into DataBaseC

Each of these is tested with a unit test and all three of these unit tests are run by this Database File Reading Integration test.

The three unit tests are:
testEmployee - which tests that employeeInfo file is being read correctly
testTables - which tests that the tableNumbers file is being read correctly
testMenu - which tests that the menu file is being read correctly

To run this integration test (host changing a table status from ready to 
seated), first run DatabaseAController.java, DatabaseBController.java and 
MessageController.java. Then run IntegrationTestHost.java.

The first integration tested host interface with changing the status of a table 
from ready to seated. In this integration test, two functions were sequentially 
called. The first function is testLoadTables() which initially loads all tables and 
statuses and the second function is testSeat() which takes the table that is ready 
and changes its status to seated. We first tested these two functions 
individually to ensure that they worked and then tested them sequentially. 
We individually tested them as unit tests in the following way:

The function testLoadTables() returns true on success and false on failure to load the tables to the screen. The function testSeat() returns 0 on success of changing the status of a table from ready (‘r’) to seated (‘s’) and redrawing the screen to account for this update. It returns -1 if the host attempts to change the status to seated when a table is not ready. 

If both functions passed then the integration test passed and the output would be “Integration Test 1 PASSED”. If one or both functions failed, the output would show which test or tests failed. 

To run this integration test (a waiter adding a dish to the Ticket), first run DatabaseAController.java, DatabaseCController.java and MessageController.java. Then run IntegrationTestWaiter.java.

The second integration tested waiter interface to add a dish to the ticket.  In this integration test, two functions were sequentially called. The first function is testLoadMenu() which initially loads all the menu items (known to be stored in Database C). The second function is testaddDishtoTicket() which adds a new dish to the ticket. We first tested these functions individually to ensure that they worked and then we tested them sequentially. We individually tested them as unit tests in the following way:

The function testLoadMenu() returns true on success and false on failure to load the menu items from database C. The function testaddDishtoTicket() returns true if the dish is successfully added to the waiter’s ticket and false if the dish couldn’t be added to the ticket. 

If both the functions passed then the integration test passed and the overall output would be “Integration Test 2 PASSED”. If one or both functions failed, the output would show which test or tests failed.

Additional testing was done visually and sequentially through testing our GUIs.
Some examples are illustrated below:

Testing for Waiter

If we login with employee ID =0. This shows the waiter interface.

Test Case 1: Select Table number 1->Select Appetizer->Select Breadsticks->
Select Assorted apps->Press paid

Test Case 2: Select Table number 14->Select Drink->Select water->Click water 
on left screen->Select remove->Press Send to Chef

In test case 1, once a table number is selected, the GUI correctly switches to 
the menu screen. A type of part of a meal can be selected and once pressed the
menu items of that type are shown on the screen. After selecting a menu item 
it is added and total price is computed. Additional items are then added and 
the total price changes accordingly. When pressing paid and “are you sure” 
screen ensures that this ticket can be discarded. Afterwards when switching 
back to the main waiter screen, table 1 is no longer shown.

Test case 2 additionally tests the delete feature which allows a dish to be 
taken off the menu if it was added by mistake.


Testing for Manager

If we login with employee ID=1. This shows the manager interface. 
The current testing for this interface is limited as we are still developing 
additional functionalities to be shown in demo 2. 

Test case 3: Select message->press delete->press logout

This test case tests to make sure that messages are deleted properly from 
manager screen. Once a message is selected and delete is pressed, the message 
no longer appears on the screen. For clicking logout our “are you sure” feature
is again utilized. If the user presses delete without selecting a message, no 
action is taken. 

Testing for Host

If we login with employee ID=3, the Host interface is shown.

Test case 4: Select table1-> select waiter christina segerholm->press seat
Test case 5: Select table 2->press seat
Test case 6: Select waiter christina segerholm->press seat
Test case 7: Select table 7->select waiter emma roussos->press seat

Test case 4 is the ideal case, the user selects a table that is ready 
(shown in green) then selects a waiter (christina segerholm) and then presses 
seat. This sequence correctly moves a table from the ready list to the 
paid/seated section and changes the table color to red. 

Test case 5 shows what would happen if a waiter is not selected. In this case 
an error message shows on the screen to “please select a waiter”.
Test case 6 shows what happens when a table is not selected. An error message 
shows to “please select a table”. 
Once the table/waiter is correctly selected, the change is made to the screen 
as in test case 4.
Test case 7 shows what happens when the user tries to seat a table that is not 
ready. An error message of “table 7 can’t be seated” is shown.

Through integration as well as visual testing we are able to ensure that our 
application functions well and without bugs!
