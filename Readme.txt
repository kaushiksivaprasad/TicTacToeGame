This project uses Spring, Maven, BootStrap (UI) and JUnit for UT.
This project requires Tomcat web server to run properly.

Salient Features:
	*The system will be intelligent enough to intimate the user when there are no
	more moves possible, without making the user to fill in all the slots. 

	For example:
	1	2	1
	1	2	2
	2	1	x

	Assuming '1' and '2' be User1 and User2, the slot 'x' need not be 
	filled as, there is no way anybody is going to win by filling the slot.
	The system, will be intelligent enough to understand it and would declare
	that the game has been drawn.
	
	*The website can be scaled to any device resolution. In other words the game is responsive.

Procedure to run:
1. Compile the project using mvn clean install in your console. You can also see the results of the test cases when you compile.
2.	After the compilation is done, pls copy the 'TicTacToe-1.war' to webapps folder of
tomcat.
3.	Start the server. Once the server is started, pls  verify whether folder TicTacToe-1 is created in webapps folder of tomcat.
4.	Visit http://localhost:8080/TicTacToe-1/ to play the Game. Enjoy:p


