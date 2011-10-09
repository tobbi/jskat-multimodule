/**
 * JSkat - A skat program written in Java
 * by Jan Schäfer and Markus J. Luzius
 *
 * Version: 0.10.0-SNAPSHOT
 * Build date: 2011-10-09
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jskat.util.rule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jskat.AbstractJSkatTest;
import org.jskat.data.GameAnnouncement;
import org.jskat.data.GameAnnouncement.GameAnnouncementFactory;
import org.jskat.data.SkatGameData;
import org.jskat.data.Trick;
import org.jskat.util.Card;
import org.jskat.util.GameType;
import org.jskat.util.Player;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for NullRules
 */
public class NullRuleTest extends AbstractJSkatTest {

	private SkatGameData data;
	private GameAnnouncementFactory factory;

	private static BasicSkatRules nullRules = SkatRuleFactory.getSkatRules(GameType.NULL);

	/**
	 * @see Before
	 */
	@Before
	public void initialize() {

		data = new SkatGameData();
		factory = GameAnnouncement.getFactory();
		factory.setGameType(GameType.NULL);
		data.setDeclarer(Player.FOREHAND);
		data.addTrick(new Trick(0, Player.FOREHAND));
		data.setTrickCard(Player.FOREHAND, Card.C7);
		data.setTrickCard(Player.MIDDLEHAND, Card.C8);
		data.setTrickCard(Player.REARHAND, Card.C9);
		data.setTrickWinner(0, Player.REARHAND);
		data.addTrick(new Trick(1, Player.REARHAND));
		data.setTrickCard(Player.REARHAND, Card.S8);
		data.setTrickCard(Player.FOREHAND, Card.S7);
		data.setTrickCard(Player.MIDDLEHAND, Card.S9);
		data.setTrickWinner(1, Player.MIDDLEHAND);
	}

	/**
	 * Checks @see NullRules#calcGameWon()
	 */
	@Test
	public void calcGameWon() {

		data.setAnnouncement(factory.getAnnouncement());
		data.calcResult();
		assertTrue(data.getResult().isWon());
	}

	/**
	 * Checks @see NullRules#calcGameWon()
	 */
	@Test
	public void calcGameLost() {

		data.setAnnouncement(factory.getAnnouncement());
		addLoosingTrick();
		data.calcResult();
		assertFalse(data.getResult().isWon());
	}

	/**
	 * Checks @see NullRules#calcGameResult()
	 */
	@Test
	public void calcGameResultGameWon() {

		data.setDeclarerPickedUpSkat(true);
		data.setAnnouncement(factory.getAnnouncement());
		data.calcResult();
		assertEquals(23, data.getResult().getGameValue());
	}

	/**
	 * Checks @see NullRules#calcGameResult()
	 */
	@Test
	public void calcGameResultGameWonHand() {

		data.setAnnouncement(factory.getAnnouncement());
		data.calcResult();
		assertEquals(35, data.getResult().getGameValue());
	}

	/**
	 * Checks @see NullRules#calcGameResult()
	 */
	@Test
	public void calcGameResultGameWonOuvert() {

		data.setDeclarerPickedUpSkat(true);
		factory.setOuvert(Boolean.TRUE);
		data.setAnnouncement(factory.getAnnouncement());
		data.calcResult();
		assertEquals(46, data.getResult().getGameValue());
	}

	/**
	 * Checks @see NullRules#calcGameResult()
	 */
	@Test
	public void calcGameResultGameWonHandOuvert() {

		factory.setOuvert(Boolean.TRUE);
		data.setAnnouncement(factory.getAnnouncement());
		data.calcResult();
		assertEquals(59, data.getResult().getGameValue());
	}

	/**
	 * Checks @see NullRules#calcGameResult()
	 */
	@Test
	public void calcGameResultGameLost() {

		data.setDeclarerPickedUpSkat(true);
		data.setAnnouncement(factory.getAnnouncement());
		addLoosingTrick();
		data.calcResult();
		assertEquals(-46, data.getResult().getGameValue());
	}

	private void addLoosingTrick() {
		data.addTrick(new Trick(2, Player.MIDDLEHAND));
		data.setTrickCard(Player.MIDDLEHAND, Card.H7);
		data.setTrickCard(Player.REARHAND, Card.H8);
		data.setTrickCard(Player.FOREHAND, Card.H9);
		data.setTrickWinner(2, Player.FOREHAND);
		data.getGameResult().setWon(nullRules.calcGameWon(data));
	}

	/**
	 * Checks @see NullRules#calcGameResult()
	 */
	@Test
	public void calcGameResultGameLostHand() {

		data.setAnnouncement(factory.getAnnouncement());
		addLoosingTrick();
		data.calcResult();
		assertEquals(-70, nullRules.calcGameResult(data));
	}

	/**
	 * Checks @see NullRules#calcGameResult()
	 */
	@Test
	public void calcGameResultGameLostOuvert() {

		data.setDeclarerPickedUpSkat(true);
		factory.setOuvert(Boolean.TRUE);
		data.setAnnouncement(factory.getAnnouncement());
		addLoosingTrick();
		data.calcResult();
		assertEquals(-92, nullRules.calcGameResult(data));
	}

	/**
	 * Checks @see NullRules#calcGameResult()
	 */
	@Test
	public void calcGameResultGameLostHandOuvert() {

		factory.setOuvert(Boolean.TRUE);
		data.setAnnouncement(factory.getAnnouncement());
		addLoosingTrick();
		data.calcResult();
		assertEquals(-118, nullRules.calcGameResult(data));
	}
}
