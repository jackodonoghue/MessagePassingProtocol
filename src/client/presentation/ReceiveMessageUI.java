package client.presentation;

import common.Message;
import common.MessageType;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ReceiveMessageUI extends JPanel {
    private List<Message> allMessages = new ArrayList<>();
    private JPanel allMessagesPanel;

    ReceiveMessageUI(ActionListener listener) {
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        setPreferredSize(new Dimension(100, 500));

        allMessagesPanel = new JPanel();
        allMessagesPanel.setLayout(new BoxLayout(allMessagesPanel, BoxLayout.Y_AXIS));

        //Scrollbar for messages
        JScrollPane messageScroller = new JScrollPane(allMessagesPanel);
        messageScroller.setPreferredSize(new Dimension(100, 700));

        constraints.weightx = .8;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 1;
        constraints.gridy = 0;
        add(messageScroller, constraints);

        //get all messages button
        JButton allMessagesButton = new JButton("Get all messages");
        allMessagesButton.addActionListener(listener);
        constraints.weighty = 0;
        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.VERTICAL;
        add(allMessagesButton, constraints);
    }

    private JPanel getMessagePanel() {
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        Border border = BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(),
                new EmptyBorder(10, 10, 10, 10));
        messagePanel.setBorder(border);

        if (allMessages.size() != 0) {
            for (int i = allMessages.size() - 1; i >= 0; i--) {
                messagePanel.add(getMessageUI(allMessages.get(i)));
            }
        }

        return messagePanel;
    }

    private JPanel getMessageUI(Message message) {
        JPanel oneMessagePanel = new JPanel();
        oneMessagePanel.setLayout(new GridLayout(2, 1));
        Border border = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.DARK_GRAY),
                new EmptyBorder(10, 10, 10, 10));
        oneMessagePanel.setBorder(border);
        oneMessagePanel.add(new JLabel((String)message.getPayload().get(0)));
        oneMessagePanel.add(new JLabel((String)message.getPayload().get(1)));

        return oneMessagePanel;
    }

    public void setAllMessages(Message allMessages) {
        this.allMessages = (List<Message>) allMessages.getPayload();

        if(this.allMessages.size() == 0) {
            JOptionPane.showMessageDialog(null, "No Messages to Get");
            return;
        }

        if(this.allMessages.get(0).getType() == MessageType.GETERR) {
            JOptionPane.showMessageDialog(null, "Error getting messages");
            return;
        }

        for(Message m : this.allMessages){
            System.out.println("Receive msg UI: " + m.getPayload().get(1));
        }

        allMessagesPanel.removeAll();
        allMessagesPanel.add(getMessagePanel());
        allMessagesPanel.revalidate();
        allMessagesPanel.repaint();
    }
}
