/**
 * JSkat - A skat program written in Java
 * by Jan Schäfer, Markus J. Luzius and Daniel Loreck
 *
 * Version 0.13.0-SNAPSHOT
 * Copyright (C) 2013-05-10
 *
 * Licensed under the Apache License, Version 2.0. You may
 * obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jskat.ai.algorithmic;

import org.apache.log4j.Logger;
import org.jskat.data.GameAnnouncement;
import org.jskat.data.GameAnnouncement.GameAnnouncementFactory;
import org.jskat.player.AbstractJSkatPlayer;
import org.jskat.player.PlayerKnowledge;
import org.jskat.util.Card;
import org.jskat.util.CardList;
import org.jskat.util.GameType;

/**
 * @author Markus J. Luzius <br>
 *         created: 13.05.2011 17:33:05
 * 
 */
public class AlgorithmicAIPlayer extends AbstractJSkatPlayer {
	private static final Logger log = Logger.getLogger(AlgorithmicAIPlayer.class);

	private IAlgorithmicAIPlayer aiPlayer = null;
	BidEvaluator bidEvaluator = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jskat.ai.IJSkatPlayer#preparateForNewGame()
	 */
	@Override
	public void preparateForNewGame() {
		log.debug("New game preparation for player <" + playerName + ">");
		aiPlayer = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jskat.ai.IJSkatPlayer#finalizeGame()
	 */
	@Override
	public void finalizeGame() {
		bidEvaluator = null; // not necessry any more
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jskat.ai.IJSkatPlayer#bidMore(int)
	 */
	@Override
	public int bidMore(final int nextBidValue) {
		if (bidEvaluator == null) {
			bidEvaluator = new BidEvaluator(knowledge.getOwnCards());
		}
		if (bidEvaluator.getMaxBid() >= nextBidValue) {
			return nextBidValue;
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jskat.ai.IJSkatPlayer#holdBid(int)
	 */
	@Override
	public boolean holdBid(final int currBidValue) {
		if (bidEvaluator == null) {
			bidEvaluator = new BidEvaluator(knowledge.getOwnCards());
		}
		return (bidEvaluator.getMaxBid() >= currBidValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jskat.ai.IJSkatPlayer#pickUpSkat()
	 */
	@Override
	public boolean pickUpSkat() {
		if (bidEvaluator == null) {
			bidEvaluator = new BidEvaluator(knowledge.getOwnCards());
		}
		return bidEvaluator.pickUpSkat();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jskat.ai.IJSkatPlayer#announceGame()
	 */
	@Override
	public GameAnnouncement announceGame() {
		if (bidEvaluator == null) {
			bidEvaluator = new BidEvaluator(knowledge.getOwnCards());
		}
		GameAnnouncementFactory factory = GameAnnouncement.getFactory();
		factory.setGameType(bidEvaluator.getSuggestedGameType());
		aiPlayer = new AlgorithmicSinglePlayer(this);
		return factory.getAnnouncement();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jskat.ai.IJSkatPlayer#playCard()
	 */
	@Override
	public Card playCard() {
		if (aiPlayer == null) {
			if (knowledge.getGameType() == GameType.RAMSCH) {
				log.debug("aiPlayer is still null - setting to ramsch player");
				aiPlayer = new AlgorithmicRamschPlayer(this);
			} else {
				throw new IllegalStateException("AIPlayer has not been correctly set");
			}
		}
		log.debug("-+-+-+-+-+-+-+-+-+- Trick #" + knowledge.getNoOfTricks() + " - " + playerName
				+ " is playing a card of " + knowledge.getOwnCards() + " (" + aiPlayer.getClass()
				+ ") -+-+-+-+-+-+-+-+-+-");
		Card c = aiPlayer.playCard();
		if (c != null) {
			return c;
		}
		log.warn("no card returned from AI player!");
		if (knowledge.getTrickCards().size() < 1) {
			return knowledge.getOwnCards().get(0);
		}
		for (Card c2 : knowledge.getOwnCards()) {
			if (c2.isAllowed(knowledge.getGameType(), knowledge.getTrickCards().get(0), knowledge.getOwnCards())) {
				return c2;
			}
		}
		log.warn("no valid card found!");
		return knowledge.getOwnCards().get(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jskat.ai.IJSkatPlayer#isAIPlayer()
	 */
	@Override
	public final boolean isAIPlayer() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jskat.ai.IJSkatPlayer#discardSkat()
	 */
	@Override
	public CardList getCardsToDiscard() {
		if (aiPlayer == null || !(aiPlayer instanceof AlgorithmicSinglePlayer)) {
			if (knowledge.getGameType() == null || knowledge.getGameType() == GameType.RAMSCH) {
				log.debug("GameType = " + knowledge.getGameType()
						+ (knowledge.getGameType() == null ? " - assuming Ramsch" : ""));
				aiPlayer = new AlgorithmicRamschPlayer(this);
				return aiPlayer.discardSkat(null);
			}
			log.warn("aiPlayer for " + knowledge.getGameType() + " game is not a single player instance: " + aiPlayer);
			aiPlayer = new AlgorithmicSinglePlayer(this);
		}
		return aiPlayer.discardSkat(bidEvaluator);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jskat.ai.AbstractJSkatPlayer#startGame()
	 */
	@Override
	public void startGame() {
		if (aiPlayer == null) {
			if (knowledge.getGameType() == GameType.RAMSCH) {
				log.debug("GameType = " + knowledge.getGameType());
				aiPlayer = new AlgorithmicRamschPlayer(this);
			} else {
				log.debug("GameType = " + knowledge.getGameType());
				aiPlayer = new AlgorithmicOpponentPlayer(this);
			}
			log.debug("aiPlayer set to " + aiPlayer);
		} else {
			if (aiPlayer instanceof AlgorithmicRamschPlayer && knowledge.getGameType() != GameType.RAMSCH) {
				log.debug("Game is grand hand - switching from RamschPlayer to OpponentPlayer");
				aiPlayer = new AlgorithmicOpponentPlayer(this);
			}
			log.debug("game started for aiPlayer " + aiPlayer);
		}
	}

	protected PlayerKnowledge getKnowledge() {
		return knowledge;
	}

}