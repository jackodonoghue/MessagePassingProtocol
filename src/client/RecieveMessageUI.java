package client;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class RecieveMessageUI extends JPanel {
    RecieveMessageUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        setPreferredSize(new Dimension(100, 500));

        JPanel allMessagesPanel = new JPanel();
        allMessagesPanel.setLayout(new BoxLayout(allMessagesPanel, BoxLayout.Y_AXIS));

        //Scrollbar for messages
        JScrollPane messageScroller = new JScrollPane(allMessagesPanel);
        messageScroller.setPreferredSize(new Dimension(100, 700));

        //get all messages button
        constraints.weightx = .8;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 1;
        constraints.gridy = 0;
        add(messageScroller, constraints);

        //all messages
        for (int i = 0; i <= 10; i++) {
            allMessagesPanel.add(getMessagePanel());
        }

        constraints.weighty = 0;
        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.VERTICAL;
        add(new JButton("Get all messages"), constraints);
    }

    private JPanel getMessagePanel() {
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        Border border = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.DARK_GRAY),
                new EmptyBorder(10, 10, 10, 10));
        messagePanel.setBorder(border);

        messagePanel.add(new JLabel("Client"));
        messagePanel.add(new JLabel("message"));

        return messagePanel;
    }
}
