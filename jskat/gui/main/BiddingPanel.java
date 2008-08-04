/*

@ShortLicense@

Authors: @JS@
         @MJL@

Released: @ReleaseDate@

*/

package jskat.gui.main;

import java.util.Observer;
import java.util.Observable;
import java.awt.GridBagConstraints;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.GridBagLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import jskat.control.SkatGame;
import jskat.data.JSkatDataModel;
import jskat.data.BidStatus;
import jskat.share.SkatConstants;

/**
 * Panel that shows the progress of the bidding
 */
public class BiddingPanel extends JPanel implements Observer {

	private Log log = LogFactory.getLog(BiddingPanel.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -609028697594935772L;

	/**
	 * Creates new form BiddingDialog
	 * 
	 * @param dataModel
	 *            The JSkatDatamodel that holds all data
	 */
	public BiddingPanel(JSkatDataModel dataModel) {

		this.dataModel = dataModel;
		initComponents();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	private void initComponents() {

		setOpaque(false);

		GridBagConstraints gridBagConstraints;

		jPanel = new JPanel();
		leftAIPlayerPanel = new JPanel();
		leftAIPlayerBidField = new JTextField();
		rightAIPlayerPanel = new JPanel();
		rightAIPlayerBidField = new JTextField();
		humanPlayerBidPanel = new JPanel();
		humanPlayerBidField = new JTextField();
		buttonPanel = new JPanel();
		nextBidButton = new JButton();
		quitButton = new JButton();

		jPanel.setLayout(new GridBagLayout());
		jPanel.setPreferredSize(new Dimension(200, 150));

		leftAIPlayerPanel.setLayout(new GridBagLayout());

		leftAIPlayerBidField.setHorizontalAlignment(JTextField.CENTER);
		leftAIPlayerBidField.setMaximumSize(new Dimension(40, 19));
		leftAIPlayerBidField.setMinimumSize(new Dimension(40, 19));
		leftAIPlayerBidField.setPreferredSize(new Dimension(40, 19));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new Insets(10, 10, 10, 10);
		gridBagConstraints.weightx = 1.0;
		leftAIPlayerPanel.add(leftAIPlayerBidField, gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		jPanel.add(leftAIPlayerPanel, gridBagConstraints);

		rightAIPlayerPanel.setLayout(new GridBagLayout());

		rightAIPlayerBidField.setHorizontalAlignment(JTextField.CENTER);
		rightAIPlayerBidField.setMaximumSize(new Dimension(40, 19));
		rightAIPlayerBidField.setMinimumSize(new Dimension(40, 19));
		rightAIPlayerBidField.setPreferredSize(new Dimension(40, 19));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new Insets(10, 10, 10, 10);
		rightAIPlayerPanel.add(rightAIPlayerBidField, gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		jPanel.add(rightAIPlayerPanel, gridBagConstraints);

		humanPlayerBidPanel.setLayout(new GridBagLayout());

		humanPlayerBidField.setHorizontalAlignment(JTextField.CENTER);
		humanPlayerBidField.setMaximumSize(new Dimension(40, 19));
		humanPlayerBidField.setMinimumSize(new Dimension(40, 19));
		humanPlayerBidField.setPreferredSize(new Dimension(40, 19));
		humanPlayerBidPanel.add(humanPlayerBidField, new GridBagConstraints());

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(10, 10, 10, 10);
		jPanel.add(humanPlayerBidPanel, gridBagConstraints);

		nextBidButton.setText("   ...   ");
		nextBidButton.validate();
		nextBidButton.setPreferredSize(new Dimension(75, 25));
		nextBidButton.setEnabled(false);
		nextBidButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				nextBidButtonActionPerformed(evt);
			}
		});

		buttonPanel.add(nextBidButton);

		quitButton.setText(dataModel.getResourceBundle().getString("pass"));
		quitButton.setEnabled(false);
		quitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				quitButtonActionPerformed(evt);
			}
		});

		buttonPanel.add(quitButton);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(10, 10, 10, 10);
		jPanel.add(buttonPanel, gridBagConstraints);

		add(jPanel);
	}

	private void resetBiddingPanel() {

		log.debug("Resetting bidding panel...");
		
		humanPlayerBidField.setText(null);
		leftAIPlayerBidField.setText(null);
		rightAIPlayerBidField.setText(null);
		humanPlayerBidField.setEnabled(true);
		leftAIPlayerBidField.setEnabled(true);
		rightAIPlayerBidField.setEnabled(true);
		nextBidButton.setText("   ...   ");
		disableButtons();
	}

	private void quitButtonActionPerformed(ActionEvent evt) {

		skatGame.notifyBiddingThread(false);
	}

	private void nextBidButtonActionPerformed(ActionEvent evt) {

		skatGame.notifyBiddingThread(true);
	}

	private void disableButtons() {

		nextBidButton.setEnabled(false);
		quitButton.setEnabled(false);
	}

	private void enableButtons() {

		nextBidButton.setEnabled(true);
		quitButton.setEnabled(true);
	}

	/**
	 * Implementation of Observer pattern
	 * 
	 * @param observ
	 *            Observable that is observed
	 * @param obj
	 *            The object that has changed in the Observable
	 */
	public void update(Observable observ, Object obj) {

		// log.debug("UPDATE " + observ + ": " + obj + " has changed...");
		
		if (observ instanceof BidStatus) {

			BidStatus currBidStatus = (BidStatus) observ;

			int currentForeHandPlayer = currBidStatus.getForeHandPlayer();
			SkatConstants.Player currentHumanPlayerPosition = null;

			JTextField[] textFields = new JTextField[3];

			switch (currentForeHandPlayer) {

			case (0):

				textFields[0] = leftAIPlayerBidField;
				textFields[1] = rightAIPlayerBidField;
				textFields[2] = humanPlayerBidField;
				currentHumanPlayerPosition = SkatConstants.Player.BACK_HAND;

				break;
			case (1):

				textFields[0] = rightAIPlayerBidField;
				textFields[1] = humanPlayerBidField;
				textFields[2] = leftAIPlayerBidField;
				currentHumanPlayerPosition = SkatConstants.Player.MIDDLE_HAND;

				break;
			case (2):

				textFields[0] = humanPlayerBidField;
				textFields[1] = leftAIPlayerBidField;
				textFields[2] = rightAIPlayerBidField;
				currentHumanPlayerPosition = SkatConstants.Player.FORE_HAND;

				break;
			}

			if (obj instanceof SkatConstants.Player) {

				SkatConstants.Player currentPlayer = ((SkatConstants.Player) obj);

				// Value for player changed
				textFields[currentPlayer.getOrder()]
						.setText(Integer.toString(currBidStatus
								.getBidValue(currentPlayer)));
				textFields[currentPlayer.getOrder()].setEnabled(currBidStatus
						.getHandDoesBid(currentPlayer));

				if (skatGame.getPlayerOrder()[currentPlayer.getOrder()] == 2
						&& currBidStatus
								.getWasAsked(currentHumanPlayerPosition)) {

					if (Integer.parseInt(textFields[currentPlayer.getOrder()]
							.getText()) == currBidStatus
							.getHighestBidValue()
							|| Integer.parseInt(textFields[currentPlayer.getOrder()]
									.getText()) == 0
							&& currentHumanPlayerPosition == SkatConstants.Player.BACK_HAND) {

						nextBidButton.setText(currBidStatus
								.getNextBidValue()
								+ " ?");

					} else {

						nextBidButton.setText(currBidStatus
								.getHighestBidValue()
								+ " !");
					}
				}
			}

			if (currBidStatus.getButtonsEnabled()) {

				enableButtons();

			} else {

				disableButtons();
			}
		}
	}

	/**
	 * @see javax.swing.JComponent#setVisible(boolean)
	 */
	public void setVisible(boolean b) {
		
		log.debug("Setting BiddingPanel visibility to "+b);
		if(!this.isVisible() && b) {
			resetBiddingPanel();
		}
		
		super.setVisible(b);
	}
	
	/**
	 * Sets the current skat game
	 * 
	 * @param newSkatGame Current skat game
	 */
	public void setSkatGame(SkatGame newSkatGame) {
		
		this.skatGame = newSkatGame;
	}
	
	private JSkatDataModel dataModel;
	
	private SkatGame skatGame;

	private JPanel buttonPanel;

	private JTextField humanPlayerBidField;

	private JPanel humanPlayerBidPanel;

	private JPanel jPanel;

	private JTextField leftAIPlayerBidField;

	private JPanel leftAIPlayerPanel;

	private JButton nextBidButton;

	private JButton quitButton;

	private JTextField rightAIPlayerBidField;

	private JPanel rightAIPlayerPanel;

}
