/*

@ShortLicense@

Authors: @JS@
         @MJL@

Released: @ReleaseDate@

 */

package de.jskat.gui.img;

import java.awt.Canvas;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.jskat.data.JSkatOptions;
import de.jskat.util.Rank;
import de.jskat.util.Suit;

/**
 * Repository that holds all images used in JSkat
 */
public class JSkatGraphicRepository {

	private static Log log = LogFactory.getLog(JSkatGraphicRepository.class);

	/**
	 * Creates a new instance of JSkatGraphicRepository
	 * 
	 * @param jskatOptions
	 *            Current JSkatOptions
	 */
	public JSkatGraphicRepository(JSkatOptions jskatOptions) {

		cardFace = jskatOptions.getCardFace();
		tracker = new MediaTracker(new Canvas());
		skatTable = Toolkit
				.getDefaultToolkit()
				.getImage(
						ClassLoader
								.getSystemResource("de/jskat/gui/img/gui/skat_table.png")); //$NON-NLS-1$
		tracker.addImage(skatTable, 0);
		jskatLogo = Toolkit
				.getDefaultToolkit()
				.getImage(
						ClassLoader
								.getSystemResource("de/jskat/gui/img/gui/jskatLogo.png")); //$NON-NLS-1$
		tracker.addImage(jskatLogo, 0);

		bidBubbles = new ArrayList<Image>();
		bidBubbles
				.add(Toolkit
						.getDefaultToolkit()
						.getImage(
								ClassLoader
										.getSystemResource("de/jskat/gui/img/gui/bid_left.png"))); //$NON-NLS-1$
		bidBubbles
				.add(Toolkit
						.getDefaultToolkit()
						.getImage(
								ClassLoader
										.getSystemResource("de/jskat/gui/img/gui/bid_right.png"))); //$NON-NLS-1$
		bidBubbles
				.add(Toolkit
						.getDefaultToolkit()
						.getImage(
								ClassLoader
										.getSystemResource("de/jskat/gui/img/gui/bid_user.png"))); //$NON-NLS-1$
		tracker.addImage(bidBubbles.get(0), 0);
		tracker.addImage(bidBubbles.get(1), 0);
		tracker.addImage(bidBubbles.get(2), 0);
		try {
			tracker.waitForID(0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		log.debug("Bitmaps for JSkat logo and skat table loaded..."); //$NON-NLS-1$

		icons = new ArrayList<List<Image>>();
		loadIcons();

		log.debug("Bitmaps for icons loaded..."); //$NON-NLS-1$

		cards = new ArrayList<List<Image>>();
		loadCards(cardFace);

		log.debug("Bitmaps for cards loaded..."); //$NON-NLS-1$
	}

	/**
	 * Loads all icons
	 */
	public void loadIcons() {

		// for all icons
		for (Icon icon : Icon.values()) {

			// new array list for all sizes
			icons.add(new ArrayList<Image>());

			// for all sizes
			for (IconSize size : IconSize.values()) {

				// add icon
				icons.get(icon.ordinal())
						.add(Toolkit
								.getDefaultToolkit()
								.getImage(
										ClassLoader
												.getSystemResource("de/jskat/gui/img/gui/" //$NON-NLS-1$
														+ icon.toString()
																.toLowerCase()
														+ '_'
														+ size.toString()
																.toLowerCase()
														+ ".png"))); //$NON-NLS-1$
				tracker.addImage(icons.get(icon.ordinal()).get(size.ordinal()),
						1);
			}
		}

		try {
			tracker.waitForID(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Load all card images
	 * 
	 * @param cardType
	 *            The directory name for the card set to be loaded
	 */
	public void loadCards(CardFace cardType) {

		for (Suit suit : Suit.values()) {

			cards.add(new ArrayList<Image>());

			for (Rank rank : Rank.values()) {

				cards.get(suit.ordinal())
						.add(Toolkit
								.getDefaultToolkit()
								.getImage(
										ClassLoader
												.getSystemResource("de/jskat/gui/img/cards/" //$NON-NLS-1$
														+ cardType.toString()
																.toLowerCase()
														+ "/gnome/" + suit.shortString() + '-' + rank.shortString() + ".gif"))); //$NON-NLS-1$//$NON-NLS-2$

				tracker.addImage(cards.get(suit.ordinal()).get(rank.ordinal()),
						2);
			}
		}

		cardBack = Toolkit
				.getDefaultToolkit()
				.getImage(
						ClassLoader
								.getSystemResource("de/jskat/gui/img/cards/" + cardType.toString().toLowerCase() //$NON-NLS-1$
										+ "/jskat/back.gif")); //$NON-NLS-1$
		tracker.addImage(cardBack, 2);

		try {
			tracker.waitForID(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets an icon image
	 * 
	 * @param icon
	 *            Icon name
	 * @param size
	 *            Icon size
	 * @return The icon image
	 */
	public Image getIconImage(Icon icon, IconSize size) {

		try {
			return icons.get(icon.ordinal()).get(size.ordinal());
		} catch (IndexOutOfBoundsException exc) {
			return null;
		}
	}

	/**
	 * Gets the card image
	 * 
	 * @param suit
	 *            The suit of the card
	 * @param value
	 *            The value of the card
	 * @return The card image
	 */
	public Image getCardImage(Suit suit, Rank value) {

		Image result = null;

		if (suit != null && value != null) {

			result = cards.get(suit.ordinal()).get(value.ordinal());
		} else {

			result = cardBack;
		}

		return result;
	}

	/**
	 * Gets the image for the skat table
	 * 
	 * @return The image for the skat table
	 */
	public Image getSkatTableImage() {

		return skatTable;
	}

	/**
	 * Gets the image for the JSkat logo
	 * 
	 * @return The image for the JSkat logo
	 */
	public Image getJSkatLogoImage() {

		return jskatLogo;
	}

	/**
	 * Gets the image for the left opponent bid bubble
	 * 
	 * @return Image for the left opponent bid bubble
	 */
	public Image getLeftBidBubbleImage() {
		return bidBubbles.get(0);
	}

	/**
	 * Gets the image for the right opponent bid bubble
	 * 
	 * @return Image for the right opponent bid bubble
	 */
	public Image getRightBidBubbleImage() {
		return bidBubbles.get(1);
	}

	/**
	 * Gets the image for the user bid bubble
	 * 
	 * @return Image for the user bid bubble
	 */
	public Image getUserBidBubbleImage() {
		return bidBubbles.get(2);
	}

	private CardFace cardFace;

	private MediaTracker tracker;

	private Image skatTable;

	private List<List<Image>> cards;

	private Image cardBack;

	private List<List<Image>> icons;

	private Image jskatLogo;

	private List<Image> bidBubbles;

	/**
	 * Holds all icon types
	 */
	public enum Icon {
		/**
		 * About
		 */
		ABOUT,
		/**
		 * Blank
		 */
		BLANK,
		/**
		 * Exit
		 */
		EXIT,
		/**
		 * Help
		 */
		HELP,
		/**
		 * New skat round
		 */
		NEW,
		/**
		 * Load skat round
		 */
		LOAD,
		/**
		 * Save
		 */
		SAVE,
		/**
		 * Save under new name
		 */
		SAVE_AS,
		/**
		 * First
		 */
		FIRST,
		/**
		 * Previous
		 */
		PREVIOUS,
		/**
		 * Next
		 */
		NEXT,
		/**
		 * Last
		 */
		LAST,
		/**
		 * Preferences
		 */
		PREFERENCES,
		/**
		 * Table
		 */
		TABLE,
		/**
		 * Start series / continue series etc.
		 */
		PLAY,
		/**
		 * Pause series
		 */
		PAUSE,
		/**
		 * Connect ISS
		 */
		CONNECT_ISS,
		/**
		 * Leave table / log out from ISS
		 */
		LOG_OUT,
		/**
		 * License
		 */
		LICENSE,
		/**
		 * Close icon for windows and tabs
		 */
		CLOSE,
		/**
		 * JSkat logo
		 */
		JSKAT,
		/**
		 * Train Neural Networks
		 */
		TRAIN_NN,
		/**
		 * OK / Bid / Hold bid
		 */
		OK,
		/**
		 * Cancel / Pass
		 */
		STOP,
		/**
		 * Chat
		 */
		CHAT,
		/**
		 * Chat disabled
		 */
		CHAT_DISABLED,
		/**
		 * User info
		 */
		USER_INFO,
		/**
		 * Web / Home page
		 */
		WEB,
		/**
		 * Clock
		 */
		CLOCK,
		/**
		 * Invite
		 */
		INVITE,
		/**
		 * Register
		 */
		REGISTER;
	}

	/**
	 * Holds all icon sizes
	 */
	public enum IconSize {
		/**
		 * Big
		 */
		BIG {
			@Override
			public String getSize() {
				return "48"; //$NON-NLS-1$
			}
		},
		/**
		 * Small
		 */
		SMALL {
			@Override
			public String getSize() {
				return "22"; //$NON-NLS-1$
			}
		};

		/**
		 * Gets a string representing the size of the icon
		 * 
		 * @return Size string
		 */
		public abstract String getSize();
	}
}
