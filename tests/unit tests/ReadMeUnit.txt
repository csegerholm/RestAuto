Unit Tests
This ReadMe describes the scope of our unit tests and how to run them. 

We made two individual unit tests that are run on their own.
They are not a part of any integration tests.

MessageTesting.java
	Tests the Message fromString method in Message class. 
	Ensures that message can be decoded from String.
	Uses the message "h3*w0*1" as test.
		Outputs:
		-On success you will see: Message From String Unit Test: PASSED
		-On failure you will see: Sender Position: Expected: h    Got:<ActualSenderPosition>
				**Where actual position will be the position read from the String
			It will list this for all the parameters of the message object.
			Then you will see: Message From String Unit Test: FAILED



TicketToMessageConversion.java
	Tests Ticket class's addDishToTicket(), toStringForChef(), and fromString() methods
	Ensures that ticket can be decoded and encoded correctly so it can be used in a message.
	Outputs:
	-On success you will see: TicketToMessage Unit Test: Passed
	-On failure you will see the expected ticket followed by the actual ticket
		Then you will see: TicketToMessage Unit Test: Failed

How to run:
Step 1) Make sure your set up is correct (See ReadMe.txt in base folder).
Step 2) Open Eclipse (or IDE)
Step 3) Open up MessageTesting.java or TicketToMessageConversion.java
Step 4) Click Run