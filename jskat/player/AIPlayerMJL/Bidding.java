/*

@ShortLicense@

Author: @MJL@

Released: @ReleaseDate@

*/

package jskat.player.AIPlayerMJL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jskat.share.CardVector;
import jskat.share.SkatConstants;

/**
 * @author Markus J. Luzius <markus@luzius.de>
 *
 */
public class Bidding {

	private Log log = LogFactory.getLog(Bidding.class);

    /** default constructor
	 * @param cards hand of the player
	 */
	public Bidding(CardVector cards) {
	    log.debug("Checking out what to bid with ["+cards+"]");

		SkatConstants.Suits mostFrequentSuitColor;
		int mostFrequentSuitColorValue = 0;
		int multiplier = Helper.getMultiplier(cards);
        
		mostFrequentSuitColor = cards.getMostFrequentSuit();
		int noOfTrumps = cards.getSuitCount(SkatConstants.GameTypes.SUIT, mostFrequentSuitColor);
		int noOfJacks = Helper.countJacks(cards);
        
		if (mostFrequentSuitColor == SkatConstants.Suits.CLUBS) {
			mostFrequentSuitColorValue = SkatConstants.CLUBS_VAL;
		}
		else if (mostFrequentSuitColor == SkatConstants.Suits.SPADES) {
			mostFrequentSuitColorValue = SkatConstants.SPADES_VAL;
		}
		else if (mostFrequentSuitColor == SkatConstants.Suits.HEARTS) {
			mostFrequentSuitColorValue = SkatConstants.HEARTS_VAL;
		}
		else if (mostFrequentSuitColor == SkatConstants.Suits.DIAMONDS) {
			mostFrequentSuitColorValue = SkatConstants.DIAMONDS_VAL;
		}
		maxBid = mostFrequentSuitColorValue * multiplier;

		// but I will only play, if I have at least 1 jack and 4 color cards or 2 jacks and 3 color cards
		if (noOfJacks < 3 && noOfTrumps < 4) maxBid = 0;
		else if (noOfJacks < 2 && noOfTrumps < 5) maxBid = 0;
		else if (noOfJacks < 1 && noOfTrumps < 6) maxBid = 0;
		else if ((Helper.getJacks(cards)&12)==0 && noOfTrumps < 5) maxBid = 0;
        
		log.debug("I will bid until " + maxBid +" - I have "+noOfJacks+" Jacks and "+noOfTrumps+" Trumps in suit "+mostFrequentSuitColor);
	}

	/** Gets the maximum bid value of the player
	 * @return value
	 */
	public int getMaxBid() {
		return maxBid;
	}

	/**
	 * Maximum value that the player will bid
	 */
	private int maxBid = -1;
}
