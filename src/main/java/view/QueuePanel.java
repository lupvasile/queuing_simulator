package view;

import model.Customer;
import model.Queue;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * JPanel used for a Queue.
 *
 * @author Vasile Lup
 * @see Queue
 */
public class QueuePanel extends JPanel {
    private Queue queue;
    private JLabel queueNameLabel;

    public QueuePanel(Queue queue) {
        this.queue = queue;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEtchedBorder());
        setBackground(Color.WHITE);

        queueNameLabel = new JLabel(queue.getName());

        updatePanel();
    }

    public void updatePanel() {
        List<Customer> customers = queue.getCustomers();

        removeAll();
        add(queueNameLabel);
        customers.forEach(c -> add(new JLabel(c.toString())));

        revalidate();
        repaint();
    }
}
