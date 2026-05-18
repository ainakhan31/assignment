import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

class EmptyFieldException extends Exception {
    public EmptyFieldException(String fieldName) {
        super("Field cannot be empty: " + fieldName);
    }
}

class InvalidRollNumberException extends Exception {
    public InvalidRollNumberException(String roll) {
        super("Invalid Roll Number format: \"" + roll + "\"\nExpected format: BSCS-F22-001");
    }
}

class InvalidDateException extends Exception {
    public InvalidDateException(String msg) {
        super(msg);
    }
}

class NullSelectionException extends Exception {
    public NullSelectionException(String fieldName) {
        super("Please make a selection for: " + fieldName);
    }
}

// ─────────────────────────────────────────────
//  MAIN CLASS
// ─────────────────────────────────────────────

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
        add(new JLabel("Issue Date (dd-MM-yyyy):"));
        txtIssueDate = new JTextField();
        add(txtIssueDate);

        // Return Date
        add(new JLabel("Return Date (dd-MM-yyyy):"));
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

    // ─────────────────────────────────────────────
    //  VALIDATION METHODS  (each throws its exception)
    // ─────────────────────────────────────────────

    // Throws EmptyFieldException if value is blank
    private void checkEmpty(String value, String fieldName) throws EmptyFieldException {
        if (value == null || value.trim().isEmpty()) {
            throw new EmptyFieldException(fieldName);
        }
    }

    // Throws InvalidRollNumberException if format is wrong
    // Expected format: letters-letters+digits-digits  e.g. BSCS-F22-001
    private void validateRollNumber(String roll) throws InvalidRollNumberException {
        if (!roll.matches("[A-Za-z]+-[A-Za-z0-9]+-[0-9]+")) {
            throw new InvalidRollNumberException(roll);
        }
    }

    // Throws NullSelectionException if default "Select" is still chosen
    private void checkCategorySelection(String category) throws NullSelectionException {
        if (category == null || category.equals("Select")) {
            throw new NullSelectionException("Book Category");
        }
    }

    // Throws NullSelectionException if no radio button is selected
    private void checkEditionSelection() throws NullSelectionException {
        if (!rbNew.isSelected() && !rbOld.isSelected()) {
            throw new NullSelectionException("Book Edition");
        }
    }

    // Throws InvalidDateException if dates are malformed or return < issue
    private void validateDates(String issueDateStr, String returnDateStr)
            throws InvalidDateException {

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.setLenient(false); // strict parsing — rejects invalid dates like 31-02-2025

        Date issueDate;
        Date returnDate;

        try {
            issueDate = sdf.parse(issueDateStr);
        } catch (ParseException ex) {
            throw new InvalidDateException(
                    "Issue Date format is invalid: \"" + issueDateStr + "\"\nUse dd-MM-yyyy (e.g. 12-05-2025)");
        }

        try {
            returnDate = sdf.parse(returnDateStr);
        } catch (ParseException ex) {
            throw new InvalidDateException(
                    "Return Date format is invalid: \"" + returnDateStr + "\"\nUse dd-MM-yyyy (e.g. 26-05-2025)");
        }

        if (!returnDate.after(issueDate)) {
            throw new InvalidDateException(
                    "Return Date must be later than Issue Date.\n" +
                            "Issue Date  : " + issueDateStr + "\n" +
                            "Return Date : " + returnDateStr);
        }
    }

    // ─────────────────────────────────────────────
    //  ACTION LISTENER
    // ─────────────────────────────────────────────

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btnIssue) {
            handleIssueBook();

        } else if (e.getSource() == btnReset) {
            handleReset();

        } else if (e.getSource() == btnExit) {
            handleExit();
        }
    }

    // ─────────────────────────────────────────────
    //  ISSUE BOOK  —  try / catch / throw / finally
    // ─────────────────────────────────────────────

    private void handleIssueBook() {

        boolean success = false;

        try {

            // Read all values
            String name      = txtName.getText().trim();
            String roll      = txtRoll.getText().trim();
            String title     = txtBookTitle.getText().trim();
            String category  = (String) cmbCategory.getSelectedItem();
            String issueDate = txtIssueDate.getText().trim();
            String retDate   = txtReturnDate.getText().trim();

            // 1. Empty field checks  →  throws EmptyFieldException
            checkEmpty(name,      "Student Name");
            checkEmpty(roll,      "Roll Number");
            checkEmpty(title,     "Book Title");
            checkEmpty(issueDate, "Issue Date");
            checkEmpty(retDate,   "Return Date");

            // 2. Roll number format  →  throws InvalidRollNumberException
            validateRollNumber(roll);

            // 3. Category selection  →  throws NullSelectionException
            checkCategorySelection(category);

            // 4. Edition selection   →  throws NullSelectionException
            checkEditionSelection();

            // 5. Date validation     →  throws InvalidDateException
            validateDates(issueDate, retDate);

            // 6. NumberFormatException example:
            //    If we ever parse a numeric field (e.g. book ID), wrap it here.
            //    Kept as a demonstration block.
            try {
                // txtBookTitle is text, but imagine a numeric ID field:
                // int id = Integer.parseInt(someNumericField);
                // For demonstration, this won't throw in normal usage.
                int dummy = Integer.parseInt("0"); // safe value
            } catch (NumberFormatException nfe) {
                throw new NumberFormatException("Book ID must be a number. " + nfe.getMessage());
            }

            // All validations passed — build summary
            String edition = rbNew.isSelected() ? "New Edition" : "Old Edition";
            String summary =
                    "Name     : " + name      + "\n" +
                            "Roll No  : " + roll      + "\n" +
                            "Book     : " + title     + "\n" +
                            "Category : " + category  + "\n" +
                            "Edition  : " + edition   + "\n" +
                            "Issued   : " + issueDate + "\n" +
                            "Return   : " + retDate;

            JOptionPane.showMessageDialog(this, summary,
                    "Book Issued Successfully", JOptionPane.INFORMATION_MESSAGE);

            success = true;

        } catch (EmptyFieldException ex) {
            JOptionPane.showMessageDialog(this,
                    "Empty Field Error:\n" + ex.getMessage(),
                    "Validation Error", JOptionPane.WARNING_MESSAGE);

        } catch (InvalidRollNumberException ex) {
            JOptionPane.showMessageDialog(this,
                    "Roll Number Error:\n" + ex.getMessage(),
                    "Validation Error", JOptionPane.WARNING_MESSAGE);

        } catch (NullSelectionException ex) {
            JOptionPane.showMessageDialog(this,
                    "Selection Error:\n" + ex.getMessage(),
                    "Validation Error", JOptionPane.WARNING_MESSAGE);

        } catch (InvalidDateException ex) {
            JOptionPane.showMessageDialog(this,
                    "Date Error:\n" + ex.getMessage(),
                    "Validation Error", JOptionPane.WARNING_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Number Format Error:\n" + ex.getMessage(),
                    "Validation Error", JOptionPane.WARNING_MESSAGE);

        } catch (Exception ex) {
            // Catch-all for any unexpected exception
            JOptionPane.showMessageDialog(this,
                    "Unexpected Error:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);

        } finally {
            // finally block always runs — whether exception occurred or not
            String status = success ? "Book issue operation completed successfully."
                    : "Operation completed with errors. Please review.";
            JOptionPane.showMessageDialog(this, status,
                    "Operation Completed", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // ─────────────────────────────────────────────
    //  RESET
    // ─────────────────────────────────────────────

    private void handleReset() {
        try {
            txtName.setText("");
            txtRoll.setText("");
            txtBookTitle.setText("");
            cmbCategory.setSelectedIndex(0);
            rbNew.setSelected(true);
            txtIssueDate.setText("");
            txtReturnDate.setText("");
            txtRemarks.setText("");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error while resetting form:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);

        } finally {
            JOptionPane.showMessageDialog(this,
                    "Form has been reset.", "Operation Completed",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // ─────────────────────────────────────────────
    //  EXIT
    // ─────────────────────────────────────────────

    private void handleExit() {
        try {
            int choice = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to exit?");
            if (choice == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error while exiting:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            // Only reaches here if user said No (exit was cancelled)
            System.out.println("Exit operation handled.");
        }
    }

    public static void main(String[] args) {
        new LibraryBookIssueSystem();
    }
}