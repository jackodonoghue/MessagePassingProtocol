package client;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionListener;

public class SendMessageUI extends JPanel implements DocumentListener {
    private int counter;
    private JLabel counterLabel = new JLabel(this.counter + " Characters used");
    private JTextArea messageArea;
    SendMessageUI(ActionListener listener) {
        this.counter = 0;
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        setPreferredSize(new Dimension(200, 250));
        //add jpanel for text
        constraints.weighty = .5;
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 1;
        constraints.gridy = 0;
        add(setTextPanel(), constraints);
        //panel for label and send button
        constraints.weighty = .01;
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 1;
        constraints.gridy = 1;
        add(setSendPanel(listener), constraints);
    }

    private JPanel setSendPanel(ActionListener listener) {
        JPanel sendPanel = new JPanel((new GridBagLayout()));
        GridBagConstraints constraints = new GridBagConstraints();

        //character counter
        constraints.weightx = 0.5;
        constraints.gridx = -2;
        constraints.gridy = 0;
        sendPanel.add(this.counterLabel, constraints);

        //send button
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(listener);
        constraints.weightx = 0.5;
        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.insets = new Insets(0, 0, 0, 0);
        sendPanel.add(sendButton, constraints);

        return sendPanel;
    }

    private JScrollPane setTextPanel() {
        //text box
        messageArea = new JTextArea(2,30);
        messageArea.getDocument().addDocumentListener(this);
        messageArea.setWrapStyleWord(false);
        messageArea.setLineWrap(true);

        return new JScrollPane(messageArea);
    }

    public String getMessageFromTextArea() {
        return messageArea.getText();
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        this.counter++;
        this.counterLabel.setText(this.counter + " Characters used");
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        this.counter--;
        this.counterLabel.setText(this.counter + " Characters used");
    }

    @Override
    public void changedUpdate(DocumentEvent e) {

    }
}
