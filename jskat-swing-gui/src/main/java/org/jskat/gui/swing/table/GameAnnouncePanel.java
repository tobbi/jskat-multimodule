/**
 * This file is part of JSkat.
 *
 * JSkat is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JSkat is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with JSkat.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jskat.gui.swing.table;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ActionMap;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.jskat.control.JSkatEventBus;
import org.jskat.control.event.skatgame.InvalidNumberOfCardsInDiscardedSkatEvent;
import org.jskat.data.GameAnnouncement;
import org.jskat.data.GameAnnouncement.GameAnnouncementFactory;
import org.jskat.data.JSkatOptions;
import org.jskat.gui.action.JSkatAction;
import org.jskat.gui.swing.AbstractI18NComboBoxRenderer;
import org.jskat.gui.swing.LayoutFactory;
import org.jskat.util.CardList;
import org.jskat.util.GameType;
import org.jskat.util.JSkatResourceBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Holds widgets for announcing a game
 */
class GameAnnouncePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory
			.getLogger(GameAnnouncePanel.class);

	JSkatResourceBundle strings;
	JSkatOptions options;

	JComboBox gameTypeList = null;
	JCheckBox handBox = null;
	JCheckBox ouvertBox = null;
	JCheckBox schneiderBox = null;
	JCheckBox schwarzBox = null;

	DiscardPanel discardPanel;
	JSkatUserPanel userPanel;

	boolean userPickedUpSkat = false;

	/**
	 * Constructor
	 * 
	 * @param actions
	 *            Action map
	 * @param userPanel
	 *            User panel
	 * @param discardPanel
	 *            Discard panel
	 */
	GameAnnouncePanel(final ActionMap actions, final JSkatUserPanel userPanel,
			final DiscardPanel discardPanel) {

		this.strings = JSkatResourceBundle.INSTANCE;
		this.userPanel = userPanel;
		this.discardPanel = discardPanel;

		initPanel(actions);
	}

	private void initPanel(final ActionMap actions) {

		this.setLayout(LayoutFactory.getMigLayout("fill")); //$NON-NLS-1$

		JPanel panel = new JPanel(LayoutFactory.getMigLayout("fill")); //$NON-NLS-1$

		this.gameTypeList = new JComboBox();
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		model.addElement(GameType.GRAND);
		model.addElement(GameType.CLUBS);
		model.addElement(GameType.SPADES);
		model.addElement(GameType.HEARTS);
		model.addElement(GameType.DIAMONDS);
		model.addElement(GameType.NULL);
		this.gameTypeList.setModel(model);
		this.gameTypeList.setRenderer(new GameTypeComboBoxRenderer());
		this.gameTypeList.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				// FIXME (jan 28.11.2010) send sorting game type to JSkatMaster
				// --> more view components can benefit from this
				GameType gameType = getSelectedGameType();

				if (gameType != null) {
					GameAnnouncePanel.this.userPanel.setSortGameType(gameType);

					if (GameAnnouncePanel.this.userPickedUpSkat) {
						if (gameType == GameType.NULL) {
							GameAnnouncePanel.this.ouvertBox.setEnabled(true);
						} else {
							GameAnnouncePanel.this.ouvertBox.setEnabled(false);
						}
					} else {
						GameAnnouncePanel.this.ouvertBox.setEnabled(true);
						if (gameType != GameType.NULL) {
							GameAnnouncePanel.this.schneiderBox.setEnabled(true);
							GameAnnouncePanel.this.schwarzBox.setEnabled(true);
							if (GameAnnouncePanel.this.ouvertBox.isSelected()) {
								GameAnnouncePanel.this.schneiderBox.setSelected(true);
								GameAnnouncePanel.this.schwarzBox.setSelected(true);
							}
						} else {
							GameAnnouncePanel.this.schneiderBox.setEnabled(false);
							GameAnnouncePanel.this.schneiderBox.setSelected(false);
							GameAnnouncePanel.this.schwarzBox.setEnabled(false);
							GameAnnouncePanel.this.schwarzBox.setSelected(false);
						}
					}
				}
			}
		});
		this.gameTypeList.setSelectedIndex(-1);

		this.handBox = new JCheckBox(this.strings.getString("hand")); //$NON-NLS-1$
		this.handBox.setEnabled(false);
		this.ouvertBox = createOuvertBox();
		this.schneiderBox = new JCheckBox(this.strings.getString("schneider")); //$NON-NLS-1$
		this.schwarzBox = createSchwarzBox();

		panel.add(this.gameTypeList, "grow, wrap"); //$NON-NLS-1$
		panel.add(this.handBox, "wrap"); //$NON-NLS-1$
		panel.add(this.ouvertBox, "wrap"); //$NON-NLS-1$
		panel.add(this.schneiderBox, "wrap"); //$NON-NLS-1$
		panel.add(this.schwarzBox, "wrap"); //$NON-NLS-1$

		final JButton announceButton = new JButton(
				actions.get(JSkatAction.ANNOUNCE_GAME));
		announceButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {

				if (getSelectedGameType() != null) {

					try {
						GameAnnouncement ann = getGameAnnouncement();
						if (ann == null) {
							return;
						}
						e.setSource(ann);
					} catch (IllegalArgumentException except) {
						log.error(except.getMessage());
					}
				} else {
					e.setSource(null);
				}

				// fire event again
				announceButton.dispatchEvent(e);
			}

			private GameAnnouncement getGameAnnouncement() {
				GameAnnouncementFactory factory = GameAnnouncement.getFactory();
				GameType gameType = getSelectedGameType();
				factory.setGameType(gameType);

				if (GameAnnouncePanel.this.discardPanel.isUserLookedIntoSkat()) {

					CardList discardedCards = GameAnnouncePanel.this.discardPanel.getDiscardedCards();
					if (discardedCards.size() != 2) {

						JSkatEventBus.INSTANCE
								.post(new InvalidNumberOfCardsInDiscardedSkatEvent());
						return null;
					}
					factory.setDiscardedCards(discardedCards);
					if (GameType.NULL.equals(gameType)
							&& GameAnnouncePanel.this.ouvertBox.isSelected()) {
						factory.setOuvert(true);
					}
				} else {

					if (GameAnnouncePanel.this.handBox.isSelected()) {
						factory.setHand(Boolean.TRUE);
					}
					if (GameAnnouncePanel.this.ouvertBox.isSelected()) {
						factory.setOuvert(Boolean.TRUE);
					}
					if (GameAnnouncePanel.this.schneiderBox.isSelected()) {
						factory.setSchneider(Boolean.TRUE);
					}
					if (GameAnnouncePanel.this.schwarzBox.isSelected()) {
						factory.setSchneider(Boolean.TRUE);
					}
				}
				return factory.getAnnouncement();
			}

		});
		panel.add(announceButton);

		add(panel, "center"); //$NON-NLS-1$

		setOpaque(false);

		resetPanel();
	}

	GameType getSelectedGameType() {
		Object selectedItem = this.gameTypeList.getSelectedItem();
		return (GameType) selectedItem;
	}

	private JCheckBox createOuvertBox() {
		final JCheckBox result = new JCheckBox(this.strings.getString("ouvert")); //$NON-NLS-1$

		result.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				if (result.isSelected() && GameAnnouncePanel.this.handBox.isSelected()
						&& getSelectedGameType() != null) {
					// hand ouvert
					if (GameType.NULL != getSelectedGameType()) {
						GameAnnouncePanel.this.schneiderBox.setSelected(true);
						GameAnnouncePanel.this.schwarzBox.setSelected(true);
					}
				}
			}
		});

		return result;
	}

	private JCheckBox createSchwarzBox() {
		final JCheckBox result = new JCheckBox(this.strings.getString("schwarz")); //$NON-NLS-1$

		result.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (result.isSelected()) {
					GameAnnouncePanel.this.schneiderBox.setSelected(true);
				}
			}
		});

		return result;
	}

	void resetPanel() {

		this.gameTypeList.setSelectedIndex(-1);
		this.handBox.setSelected(true);
		this.ouvertBox.setSelected(false);
		this.schneiderBox.setSelected(false);
		this.schwarzBox.setSelected(false);
	}

	private class GameTypeComboBoxRenderer extends AbstractI18NComboBoxRenderer {

		private static final long serialVersionUID = 1L;

		GameTypeComboBoxRenderer() {
			super();
		}

		@Override
		public String getValueText(final Object value) {

			String result = " "; //$NON-NLS-1$

			GameType gameType = (GameType) value;

			if (gameType != null) {
				result = GameAnnouncePanel.this.strings.getGameType(gameType);
			}

			return result;
		}
	}

	void setUserPickedUpSkat(final boolean isUserPickedUpSkat) {

		this.userPickedUpSkat = isUserPickedUpSkat;

		if (isUserPickedUpSkat) {
			this.handBox.setSelected(false);
			if (GameType.NULL.equals(getSelectedGameType())) {
				this.ouvertBox.setEnabled(true);
			} else {
				this.ouvertBox.setEnabled(false);
			}
			this.schneiderBox.setEnabled(false);
			this.schwarzBox.setEnabled(false);
		} else {
			this.handBox.setSelected(true);
			this.ouvertBox.setEnabled(true);
			this.schneiderBox.setEnabled(true);
			this.schwarzBox.setEnabled(true);
		}
	}
}
