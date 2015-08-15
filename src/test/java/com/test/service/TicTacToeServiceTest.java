package com.test.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.tictactoe.service.TicTacToeService;

public class TicTacToeServiceTest {
	private TicTacToeService service = null;

	@Before
	public void doSetup() {
		service = new TicTacToeService();
	}

	@After
	public void doTearDown() {
		service = null;
	}

	public String readStringFromFile(File file) throws IOException {
		BufferedReader reader = null;
		StringBuilder builder = new StringBuilder();
		try {
			reader = new BufferedReader(new FileReader(file));
			String content = null;
			while ((content = reader.readLine()) != null) {
				builder.append(content);
			}
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		return builder.toString();
	}

	private int doInteractionWithTheMethodUnderTest(int testDataPosition,
			boolean checkForPremptiveness) throws NumberFormatException,
			Exception {
		File file = new File(this.getClass().getResource( "/TestResource.txt" ).toURI());
		String content[] = readStringFromFile(file).split("\\*");
		String processedString = content[testDataPosition].trim();
		String inputArry[] = processedString.split(",");
		int firstLoopEndVal = inputArry.length / 3;
		int secLoopEndVal = 3;
		int k = 0;
		int i = 0;
		int j = 0;
		int result = -1;
		for (i = 0; i < firstLoopEndVal; i++) {
			for (j = 0; j < secLoopEndVal; j++) {
				result = service.putUsersChoiceInToMtrx(
						Integer.parseInt(inputArry[k++]), i, j);
				if (result != -1) {
					break;
				}
			}
		}
		if (checkForPremptiveness
				&& (i == firstLoopEndVal && j == secLoopEndVal)) {
			throw new Exception("Pre-Emptiveness Failed..");
		}
		return result;
	}

	@Test
	public void testPutUsersChoiceInToMtrx_ReturnZeroWithPreEmption()
			throws NumberFormatException, Exception {
		int result = doInteractionWithTheMethodUnderTest(0, true);
		assertTrue(result == 0);

	}

	@Test
	public void testPutUsersChoiceInToMtrx_WinInStLine()
			throws NumberFormatException, Exception {
		int result = doInteractionWithTheMethodUnderTest(1, false);
		assertTrue(result == 1);

	}

	@Test
	public void testPutUsersChoiceInToMtrx_WinAlongDiagnol()
			throws NumberFormatException, Exception {
		int result = doInteractionWithTheMethodUnderTest(2, false);
		assertTrue(result == 1);
	}

	@Test
	public void testPutUsersChoiceInToMtrx_RaisesInvalidMove()
			throws NumberFormatException, Exception {
		try {
			service.putUsersChoiceInToMtrx(1, 0, 0);
			service.putUsersChoiceInToMtrx(1, 0, 0);
		} catch (Exception e) {
			assertEquals(e.getMessage(), "Invalid move..");
		}
	}

	@Test
	public void testPutUsersChoiceInToMtrx_RaisesInvalidUser()
			throws NumberFormatException, Exception {
		try {
			service.putUsersChoiceInToMtrx(4, 0, 0);
		} catch (Exception e) {
			assertEquals(e.getMessage(), "Invalid User value exception..");
		}
	}

	@Test
	public void testPutUsersChoiceInToMtrx_RaisesInvalidCoOrds()
			throws NumberFormatException, Exception {
		try {
			service.putUsersChoiceInToMtrx(1, -1, 4);
		} catch (Exception e) {
			assertEquals(e.getMessage(), "Invalid co-ordinate values..");
		}
	}

}
