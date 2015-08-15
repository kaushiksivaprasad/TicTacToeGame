package com.tictactoe.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tictactoe.service.TicTacToeService;

@RestController
@RequestMapping("/GameService")

/**
 * This serves as the Rest controller for the game.
 * @author Kaushik
 *
 */
public class TicTacToeController {

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	/**
	 * This method will be called when a get request is made whose url matches the above pattern.
	 * This method returns {"result" : <VAL>}. 
	 * The possible responses for <VAL> are :
	 * '-1' - indicates that the game continues
	 * '0' - indicates that the game has been drawn
	 * '1' - indicates that user1 has won the game
	 * '2' - indicates that user2 has won the game 
	 * 
	 * @param request - HttpServletRequest object
	 * @param userId - Integer - Can be either '1' (user1) or '2' (user2)
	 * @param x - Integer - 'x' Co-Ordinates as in a Multi-Dimensional array.(Max : '3')
	 * @param y - Integer - 'x' Co-Ordinates as in a Multi-Dimensional array.(Max : '3')
	 * @param killSession - boolean - invalidates the session and clears the data
	 * 						associates with the game.
	 * @return
	 */
	public String playGame(HttpServletRequest request,
			@RequestParam("userId") int userId, @RequestParam("x") int x,
			@RequestParam("y") int y,
			@RequestParam("killSession") boolean killSession) {
		HttpSession session = request.getSession(false);
		if (session != null && killSession) {
			session.invalidate();
			session = request.getSession();
		} else {
			session = request.getSession(true);
		}
		TicTacToeService service = (TicTacToeService) session
				.getAttribute("service");
		String response = "";
		int result = -1;
		if (service == null) {
			service = new TicTacToeService();
		}
		try {
			result = service.putUsersChoiceInToMtrx(userId, x, y);
			response = "{\"result\" :" + result + "}";
		} catch (Exception e) {
			response = "{\"error\" : '" + e.getMessage() + "'}";
		}
		session.setAttribute("service", service);
		return response;
	}
}
