import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class LibraryBookIssueSystem extends JFrame implements ActionListener {

    JTextField txtName, txtRoll, txtBookTitle, txtIssueDate, txtReturnDate;
    JComboBox<String> cmbCategory;
    JRadioButton rbNew, rbOld;
    ButtonGroup bgEdition;
    JTextArea txtRemarks;
    JButton btnIssue, btnReset, btnExit;

    public LibraryBookIssueSystem() {

        setTitle("Library Book Issue System");
        setSize(480, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(11, 2, 8, 8));

        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Student Name
        add(new JLabel("Student Name:"));
        txtName = new JTextField();
        add(txtName);

        // Roll Number
        add(new JLabel("Roll Number:"));
        txtRoll = new JTextField();
        add(txtRoll);

        // Book Title
        add(new JLabel("Book Title:"));
        txtBookTitle = new JTextField();
        add(txtBookTitle);

        // Book Category
        add(new JLabel("Book Category:"));
        String[] categories = { "Select", "Programming", "AI", "Databases", "Networking" };
        cmbCategory = new JComboBox<>(categories);
        add(cmbCategory);

        // Book Edition (Radio Buttons)
        add(new JLabel("Book Edition:"));
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        rbNew = new JRadioButton("New", true);
        rbOld = new JRadioButton("Old");
        bgEdition = new ButtonGroup();
        bgEdition.add(rbNew);
        bgEdition.add(rbOld);
        radioPanel.add(rbNew);
        radioPanel.add(rbOld);
        add(radioPanel);

        // Issue Date
        add(new JLabel("Issue Date:"));
        txtIssueDate = new JTextField();
        add(txtIssueDate);

        // Return Date
        add(new JLabel("Return Date:"));
        txtReturnDate = new JTextField();
        add(txtReturnDate);

        // Remarks
        add(new JLabel("Remarks:"));
        txtRemarks = new JTextArea(2, 20);
        add(new JScrollPane(txtRemarks));

        // Buttons
        btnIssue = new JButton("Issue Book");
        btnReset = new JButton("Reset");
        btnExit  = new JButton("Exit");

        btnIssue.addActionListener(this);
        btnReset.addActionListener(this);
        btnExit.addActionListener(this);

        add(btnIssue);
        add(btnReset);
        add(new JLabel(""));
        add(btnExit);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btnIssue) {

            String name      = txtName.getText().trim();
            String roll      = txtRoll.getText().trim();
            String title     = txtBookTitle.getText().trim();
            String category  = (String) cmbCategory.getSelectedItem();
            String edition   = rbNew.isSelected() ? "New Edition" : "Old Edition";
            String issueDate = txtIssueDate.getText().trim();
            String retDate   = txtReturnDate.getText().trim();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter student name.");
                return;
            }
            if (roll.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter roll number.");
                return;
            }
            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter book title.");
                return;
            }
            if (category.equals("Select")) {
                JOptionPane.showMessageDialog(this, "Please select a book category.");
                return;
            }
            if (issueDate.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter issue date.");
                return;
            }
            if (retDate.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter return date.");
                return;
            }

            String summary =
                    "Name     : " + name      + "\n" +
                            "Roll No  : " + roll      + "\n" +
                            "Book     : " + title     + "\n" +
                            "Category : " + category  + "\n" +
                            "Edition  : " + edition   + "\n" +
                            "Issued   : " + issueDate + "\n" +
                            "Return   : " + retDate;

            JOptionPane.showMessageDialog(this, summary, "Book Issued Successfully", JOptionPane.INFORMATION_MESSAGE);

        } else if (e.getSource() == btnReset) {

            txtName.setText("");
            txtRoll.setText("");
            txtBookTitle.setText("");
            cmbCategory.setSelectedIndex(0);
            rbNew.setSelected(true);
            txtIssueDate.setText("");
            txtReturnDate.setText("");
            txtRemarks.setText("");

        } else if (e.getSource() == btnExit) {

            int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?");
            if (choice == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        }
    }

    public static void main(String[] args) {
        new LibraryBookIssueSystem();
    }
}