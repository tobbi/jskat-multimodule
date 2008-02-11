/*

@ShortLicense@

Authors: @JS@
         @MJL@

Released: @ReleaseDate@

*/

package jskat.player;

import jskat.share.Card;
import jskat.share.CardVector;
import jskat.data.GameAnnouncement;

/** 
 * Human Player
 * 
 * @author Jan Schäfer <jan.schaefer@b0n541.net>
 */
public class HumanPlayer extends AbstractJSkatPlayer {
    
    /** Creates a new instance of SkatPlayer */
    public HumanPlayer(int playerID) {
        
        super();
        setPlayerID(playerID);
        setPlayerName("Nobody");
    }
    
    /** Creates a new instance of SkatPlayer */
    public HumanPlayer(int playerID, String playerName) {
        
        super();
        setPlayerID(playerID);
        setPlayerName(playerName);
    }
    
    public boolean bidMore(int currBidValue) {
        
        return false;
    }
    
    public GameAnnouncement announceGame() {
        
        return new GameAnnouncement();
    }
    
    public void takeRamschSkat(CardVector skat, boolean jacksAllowed) {
        
    }
    
    public boolean lookIntoSkat(boolean isRamsch) {
        return true;
    }
    
    public final boolean isHumanPlayer() {
        return true;
    }
    
    public final boolean isAIPlayer() {
        return false;
    }

	public void showTrick(CardVector trick, int trickWinner) {
	}

	public Card playCard(CardVector trick) {
		// TODO Auto-generated method stub
		return null;
	}
}