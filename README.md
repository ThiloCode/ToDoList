A command line based to do list application working locally and in the cloud.

Modifying the code to change the to do list however you want is easy. The commands are implemented according to a rule engine pattern. To add a new command, just create a new class in the commands package which has an execute() method. Then modify the constructor of the relevant handler to add a new rule.

Modifying the screen runs through the ScreenManager class. Don't try to print new information to the screen from random classes! Use the ScreenManager to keep everything consistent.

The server runs on sockets and operates on a MongoDB. To set it up, simply put a connection string from your MongoDB in a file called DatabaseConfiguration.txt in the main directory of the application. Your MongoDB must contain a database called "ToDoLists" with collections called "Sessions" and "Users".
