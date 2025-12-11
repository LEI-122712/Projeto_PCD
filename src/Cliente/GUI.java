package Cliente;

import Estrutura.*;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.util.Map;

public class GUI {

	private JFrame frame;
	private JLabel questionLabel;
	private JButton[] optionButtons;
	private JLabel timerLabel;
	private JLabel titleLable;
	private Client client;

	public GUI(Client client) {
		this.client = client;
		frame = new JFrame("Kahoot");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	public void addQuestionFrame(Question q) {
		frame.getContentPane().removeAll();
		frame.setLayout(new BorderLayout(10, 10));
		JPanel top = new JPanel();
		titleLable = new JLabel("IsKahoot", JLabel.CENTER);
		top.add(titleLable);
		frame.add(top, BorderLayout.NORTH);
		String qtext = q.getQuestion();
		JPanel center = new JPanel(new BorderLayout(10, 10));
		questionLabel = new JLabel(qtext, JLabel.CENTER);
		center.add(questionLabel, BorderLayout.NORTH);
		String[] options = q.getOptions();
		int gridsize = 0;
		if (options.length % 2 == 0) {
			gridsize = options.length / 2;
		} else {
			gridsize = (options.length + 1) / 2;
		}
		JPanel answers = new JPanel(new GridLayout(gridsize, 2, 10, 10));
		optionButtons = new JButton[4];
		Color[] colors = { new Color(171, 0, 11), new Color(0, 2, 154), new Color(139, 0, 139), new Color(4, 138, 0) };
		for (int i = 0; i < options.length; i++) {
			optionButtons[i] = new JButton(options[i]);
			answers.add(optionButtons[i]);
			optionButtons[i].setBackground(colors[i]);
			optionButtons[i].setOpaque(true);
			optionButtons[i].setBorderPainted(false);
			optionButtons[i].setForeground(Color.WHITE);
		}
		center.add(answers, BorderLayout.CENTER);
		frame.add(center, BorderLayout.CENTER);
		JPanel bottom = new JPanel();
		timerLabel = new JLabel("Tempo", JLabel.CENTER);
		bottom.add(timerLabel);
		frame.add(bottom, BorderLayout.SOUTH);
		for (int i = 0; i < options.length; i++) {
            int index = i; // Necessário para usar dentro da classe anónima
            optionButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // 3. ADICIONAR ESTA LÓGICA
                    // Desativar botões para impedir múltiplas respostas
                    for (JButton btn : optionButtons) btn.setEnabled(false);
                    
                    // Enviar a resposta ao servidor através do cliente
                    if (client != null) {
                        client.sendAnswer(index);
                    }
                }
            });
        }
		frame.pack();
		frame.revalidate();
		frame.repaint();

	}

	// METER AS STATS NA JANELA DA PERGUNTA
	public void addStatsFrame(Map<String, Integer> scoreboard) {
		// Limpa o conte�do anterior
		frame.getContentPane().removeAll();
		// vai buscar o scoreboard ao gamestate
		// Map<String, Integer> scoreboard=gamestate.getScoreboard();
		JPanel statsPanel = new JPanel();
		statsPanel.setLayout(new GridLayout(scoreboard.size() + 1, 2, 10, 10));

		statsPanel.add(new JLabel("Equipa", JLabel.CENTER));
		statsPanel.add(new JLabel("Pontua��o", JLabel.CENTER));

		for (String teamName : scoreboard.keySet()) {
			int score = scoreboard.get(teamName);
			statsPanel.add(new JLabel(teamName, JLabel.CENTER));
			statsPanel.add(new JLabel(String.valueOf(score), JLabel.CENTER));
		}

		frame.add(new JLabel("Fim da ronda", JLabel.CENTER), BorderLayout.NORTH);
		frame.add(statsPanel, BorderLayout.CENTER);

		JPanel ok = new JPanel();
		JButton b = new JButton("Ok");
		b.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});
		ok.add(b);
		frame.add(ok, BorderLayout.SOUTH);
		frame.pack();
		frame.revalidate();
		frame.repaint();
	}

	public void open() {
		// para abrir a janela (torna-la visivel)
		frame.setVisible(true);
	}

	public void endOfGame() {

		frame.getContentPane().removeAll();
		frame.setLayout(new BorderLayout(10, 10));

		JPanel end = new JPanel();
		titleLable = new JLabel("Fim do Jogo", JLabel.CENTER);
		end.add(titleLable);

		frame.add(end, BorderLayout.CENTER);

		frame.pack();
		frame.revalidate();
		frame.repaint();

	}

}
