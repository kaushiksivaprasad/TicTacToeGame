package com.tictactoe.service;

import java.util.HashSet;
import java.util.Set;

/**
 * This class serves as the service for the game. This has the 
 * business logic for the game.
 * @author Kaushik
 *
 */
public class TicTacToeService {

	private Integer[][] ticTacToeMtrx = new Integer[3][3];
	private int addedElements = 0;
	private Set<String> decreasingDiagnol = null;
	private Set<String> increasingDiagnol = null;
	private boolean futureMovesExistForStLineWithXConstant = true;
	private boolean futureMovesExistForStLineWithYConstant = true;
	private boolean futureMovesExistForDecreasingDiagnol = true;
	private boolean futureMovesExistForIncreasingDiagnol = true;
	private Set<String> nullPositions = null;

	public TicTacToeService() {
		loadNullPositions();
		loadDiagnolCoOrds();
	}
	
	/**
	 * This method is responsible to load all the positions where, the null values of  
	 * {@link #ticTacToeMtrx} are present.
	 */
	private void loadNullPositions(){
		nullPositions = new HashSet<String>();
		for(int i = 0; i < ticTacToeMtrx.length; i++){
			for(int j = 0; j < ticTacToeMtrx.length; j++){
				nullPositions.add(convertXYToString(i, j));
			}
		}
	}
	
	/**
	 * This method is responsible for controlling the actual game play. 
	 * This holds all the business logic to see whether a user has won
	 * along either Straight lines or also along Diagnols.
	 * 
	 * @param userVal - Integer - Can be either '1' (user1) or '2' (user2)
	 * @param xCoOrd - Integer - 'x' Co-Ordinates as in a Multi-Dimensional array.(Max : '3')
	 * @param yCoOrd - Integer - 'x' Co-Ordinates as in a Multi-Dimensional array.(Max : '3')
	 * @return int 	This method returns {"result" : <VAL>}. 
	 * 				The possible responses for <VAL> are :
	 * 				'-1' - indicates that the game continues
	 * 				'0' - indicates that the game has been drawn
	 * 				'1' - indicates that user1 has won the game
	 * 				'2' - indicates that user2 has won the game 
	 */
	public int putUsersChoiceInToMtrx(int userVal, int xCoOrd,
			int yCoOrd) throws Exception {
		if (userVal != 1 && userVal != 2) {
			throw new Exception("Invalid User value exception..");
		}
		if ((xCoOrd < 0 || xCoOrd > ticTacToeMtrx.length - 1)
				|| (yCoOrd < 0 || yCoOrd > ticTacToeMtrx.length - 1)) {
			throw new Exception("Invalid co-ordinate values..");
		}

		if (ticTacToeMtrx[xCoOrd][yCoOrd] != null) {
			throw new Exception("Invalid move..");
		}

		// checking the straight line by iterating just the y values and keeping
		// the x val constant
		boolean hasUserWon = checkStraightLines(xCoOrd, yCoOrd, true, userVal);

		// checking by iterating the x co-ords and keeping the y co-ords
		// constant
		hasUserWon = (!hasUserWon) ? checkStraightLines(xCoOrd, yCoOrd, false,
				userVal) : hasUserWon;

		// checking the diagnols
		if (!hasUserWon
				&& decreasingDiagnol
						.contains(convertXYToString(xCoOrd, yCoOrd))) {
			hasUserWon = checkDiagnols(decreasingDiagnol, userVal, xCoOrd,
					yCoOrd, false);
		}

		if (!hasUserWon
				&& increasingDiagnol
						.contains(convertXYToString(xCoOrd, yCoOrd))) {
			hasUserWon = checkDiagnols(increasingDiagnol, userVal, xCoOrd,
					yCoOrd, true);
		}

		if (hasUserWon) {
			resetData();
			return userVal;
		} else {
			ticTacToeMtrx[xCoOrd][yCoOrd] = userVal;
			addedElements++;
			nullPositions.remove(convertXYToString(xCoOrd, yCoOrd));
			if (addedElements == ticTacToeMtrx.length * ticTacToeMtrx.length) {
				resetData();
				return 0;
			} else {
				if (futureMovesExistForDecreasingDiagnol
						|| futureMovesExistForIncreasingDiagnol
						|| futureMovesExistForStLineWithXConstant
						|| futureMovesExistForStLineWithYConstant)
					return -1;
				else{
					for(String position : nullPositions){
						String[] coOrds = extractCoOrdsFromString(position);
						int x = Integer.parseInt(coOrds[0]);
						int y = Integer.parseInt(coOrds[1]);
						boolean result = checkPossibleMovesInStraightLines(x,y,true);
						if(!result){
							result = checkPossibleMovesInStraightLines(x,y,false);
						}
						if(!result && increasingDiagnol.contains(position)){
							result = checkPossibleMovesInDiagnols(increasingDiagnol, x, y);
						}
						if(!result && decreasingDiagnol.contains(position)){
							result = checkPossibleMovesInDiagnols(decreasingDiagnol, x, y);
						}
						if(result){
							return -1;
						}
					}
					resetData();
					return 0;
				}
			}
		}
	}

	/**
	 * Loads the diagnol co-ordinates for both {@link #decreasingDiagnol} and 
	 * {@link #increasingDiagnol}
	 */
	private void loadDiagnolCoOrds() {
		decreasingDiagnol = new HashSet<String>();
		for (int i = 0; i < ticTacToeMtrx.length; i++) {
			decreasingDiagnol.add(convertXYToString(i, i));
		}
		increasingDiagnol = new HashSet<String>();
		int yVal = ticTacToeMtrx.length - 1;
		for (int i = 0; i < ticTacToeMtrx.length; i++) {
			increasingDiagnol.add(convertXYToString(i, yVal - i));
		}
	}
	
	private String convertXYToString(int xCoOrd, int yCoOrd) {
		return xCoOrd + "_" + yCoOrd;
	}

	private String[] extractCoOrdsFromString(String coOrdStr) {
		return coOrdStr.split("_");
	}
	
	/**
	 * Checks whether the user has won along the specified diagnols 
	 *  
	 * @param diagnol
	 * @param userVal
	 * @param xCoOrd
	 * @param yCoOrd
	 * @param isIncreasingDiagnol
	 * @return
	 */
	private boolean checkDiagnols(Set<String> diagnol, int userVal, int xCoOrd,
			int yCoOrd, boolean isIncreasingDiagnol) {
		boolean userWon = true;
		boolean newFutureMoves = true;
		for (String element : diagnol) {
			String[] coOrds = extractCoOrdsFromString(element);
			int x = Integer.parseInt(coOrds[0]);
			int y = Integer.parseInt(coOrds[1]);

			if (!(x == xCoOrd && y == yCoOrd) && ticTacToeMtrx[x][y] == null) {
				userWon = false;
			} else if (!(x == xCoOrd && y == yCoOrd) &&  ticTacToeMtrx[x][y] != userVal) {
				newFutureMoves = false;
				userWon = false;
			} else if (x == xCoOrd && y == yCoOrd) {
				continue;
			}
		}
		if (isIncreasingDiagnol)
			futureMovesExistForIncreasingDiagnol = newFutureMoves;
		else
			futureMovesExistForDecreasingDiagnol = newFutureMoves;
		return userWon;
	}
	
	/**
	 * Checks whether future moves are possible along the Straight paths
	 * 
	 * @param xCoOrd
	 * @param yCoOrd
	 * @param keepXConstant
	 * @return
	 */
	private boolean checkPossibleMovesInStraightLines(int xCoOrd, int yCoOrd,
			boolean keepXConstant){
		Set<Integer> uniqueElements = new HashSet<Integer>();
		for (int iter = 0; iter < ticTacToeMtrx.length; iter++) {
			int xVal = (keepXConstant) ? xCoOrd : iter;
			int yVal = (keepXConstant) ? iter : yCoOrd;
			if(ticTacToeMtrx[xVal][yVal] != null){
				uniqueElements.add(ticTacToeMtrx[xVal][yVal]);
			}
		}
		return (uniqueElements.size() < 2)?true:false;
	}
	
	/**
	 * Checks whether future moves are possible along the Diagnols.
	 * 
	 * @param diagnol
	 * @param xCoOrd
	 * @param yCoOrd
	 * @return
	 */
	private boolean checkPossibleMovesInDiagnols(Set<String> diagnol, int xCoOrd,
			int yCoOrd) {
		Set<Integer> uniqueElements = new HashSet<Integer>();
		for (String element : diagnol) {
			String[] coOrds = extractCoOrdsFromString(element);
			int x = Integer.parseInt(coOrds[0]);
			int y = Integer.parseInt(coOrds[1]);
			if(ticTacToeMtrx[x][y] != null){
				uniqueElements.add(ticTacToeMtrx[x][y]);
			}
		}
		return (uniqueElements.size() < 2)?true:false;
	}
	
	/**
	 * Checks whether the user has won along the Straight lines.
	 * @param xCoOrd
	 * @param yCoOrd
	 * @param keepXConstant
	 * @param userVal
	 * @return
	 */
	private boolean checkStraightLines(int xCoOrd, int yCoOrd,
			boolean keepXConstant, int userVal) {
		boolean userWon = true;
		boolean newFutureMoves = true;
		for (int iter = 0; iter < ticTacToeMtrx.length; iter++) {
			int xVal = (keepXConstant) ? xCoOrd : iter;
			int yVal = (keepXConstant) ? iter : yCoOrd;
			if (!(xVal == xCoOrd && yVal == yCoOrd) && ticTacToeMtrx[xVal][yVal] == null) {
				userWon = false;
			} else if (!(xVal == xCoOrd && yVal == yCoOrd) && ticTacToeMtrx[xVal][yVal] != userVal) {
				newFutureMoves = false;
				userWon = false;
			}
		}
		if (keepXConstant)
			futureMovesExistForStLineWithXConstant = newFutureMoves;
		else
			futureMovesExistForStLineWithYConstant = newFutureMoves;
		return userWon;
	}
	
	/**
	 * Used to clear the data after the result is known.
	 */
	private void resetData() {
		ticTacToeMtrx = new Integer[3][3];
		addedElements = 0;
		loadNullPositions();
	}
}
