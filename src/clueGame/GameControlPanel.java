package clueGame;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class GameControlPanel extends JPanel {
	public GameControlPanel(Board board) {
		GridBagLayout gridBag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();

		setLayout(gridBag);

		JPanel turnPanel = new JPanel(new GridLayout(1, 2));
		JLabel label = new JLabel("Whose turn? ");
		turnPanel.add(label);

		whoseTurnLabel = new JLabel("No one yet");
		turnPanel.add(whoseTurnLabel);

		c.insets = new Insets(5, 5, 5, 5);
		c.weightx = 0;
		c.weighty = 1;
		gridBag.setConstraints(turnPanel, c);
		add(turnPanel);

		JButton nextPlayerButton = new JButton("Next player");
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		gridBag.setConstraints(nextPlayerButton, c);
		add(nextPlayerButton);

		JButton makeAccusationButton = new JButton("Make an accusation");
		c.gridwidth = GridBagConstraints.REMAINDER;
		gridBag.setConstraints(makeAccusationButton, c);
		add(makeAccusationButton);

		JPanel diePanel = new JPanel(new GridLayout(1, 2));
		diePanel.setBorder(new TitledBorder(new EtchedBorder(), "Die"));
		label = new JLabel("Roll: ");
		diePanel.add(label);

		dieRollLabel = new JLabel("0");
		diePanel.add(dieRollLabel);

		c.weightx = 0;
		c.gridwidth = 1;
		gridBag.setConstraints(diePanel, c);
		add(diePanel);

		JPanel guessPanel = new JPanel(new GridLayout(1, 1));
		guessPanel.setBorder(new TitledBorder(new EtchedBorder(), "Guess"));

		guessLabel = new JLabel("None yet");
		guessPanel.add(guessLabel);

		c.weightx = 1;
		gridBag.setConstraints(guessPanel, c);
		add(guessPanel);
		
		JPanel resultPanel = new JPanel(new GridLayout(1, 2));
		resultPanel.setBorder(new TitledBorder(new EtchedBorder(), "Guess Result"));
		
		label = new JLabel("Response: ");
		resultPanel.add(label);
		
		responseLabel = new JLabel("None");
		resultPanel.add(responseLabel);
		
		c.gridwidth = GridBagConstraints.REMAINDER;
		gridBag.setConstraints(resultPanel, c);
		add(resultPanel);
	}

	private JLabel whoseTurnLabel;
	private JLabel dieRollLabel;
	private JLabel guessLabel;
	private JLabel responseLabel;
}
