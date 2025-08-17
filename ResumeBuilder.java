import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileOutputStream;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;

public class ResumeBuilder {

    private static JPanel cards; // A panel that uses CardLayout
    private static CardLayout cardLayout;
    private static JFrame frame;
    private static JTextField nameField, emailField, phoneField;
    private static JTextArea summaryArea, educationArea, achievementsArea, certificationsArea, projectsArea;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        frame = new JFrame("Professional Resume Builder");
        frame.setSize(800, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        // Create individual panels for each section
        JPanel personalInfoPanel = createPersonalInfoPanel();
        JPanel educationPanel = createEducationPanel();
        JPanel projectsPanel = createProjectsPanel();
        JPanel achievementsPanel = createAchievementsPanel();

        // Add panels to the CardLayout
        cards.add(personalInfoPanel, "Personal Info");
        cards.add(educationPanel, "Education");
        cards.add(projectsPanel, "Projects");
        cards.add(achievementsPanel, "Achievements");

        // Create navigation buttons
        JPanel navigationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton backButton = new JButton("Back");
        JButton nextButton = new JButton("Next");
        JButton submitButton = new JButton("Submit");

        backButton.setEnabled(false); // Disable "Back" on first panel

        // Style the buttons
        backButton.setBackground(Color.decode("#FF5722")); // Orange
        backButton.setForeground(Color.BLACK);
        backButton.setFocusPainted(false);
        nextButton.setBackground(Color.decode("#4CAF50")); // Green
        nextButton.setForeground(Color.BLACK);
        nextButton.setFocusPainted(false);
        submitButton.setBackground(Color.decode("#2196F3")); // Blue
        submitButton.setForeground(Color.BLACK);
        submitButton.setFocusPainted(false);

        // Add action listeners for navigation
        backButton.addActionListener(e -> {
            cardLayout.previous(cards);
            updateNavigationButtons(backButton, nextButton, submitButton);
        });

        nextButton.addActionListener(e -> {
            cardLayout.next(cards);
            updateNavigationButtons(backButton, nextButton, submitButton);
        });

        submitButton.addActionListener(e -> {
            generatePdf();
        });

        navigationPanel.add(backButton);
        navigationPanel.add(nextButton);
        navigationPanel.add(submitButton);

        frame.add(cards, BorderLayout.CENTER);
        frame.add(navigationPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private static void updateNavigationButtons(JButton backButton, JButton nextButton, JButton submitButton) {
        String currentCard = getCurrentCardName();
        backButton.setEnabled(!currentCard.equals("Personal Info"));
        nextButton.setEnabled(!currentCard.equals("Achievements"));
        submitButton.setVisible(currentCard.equals("Achievements"));
    }

    private static String getCurrentCardName() {
        for (Component comp : cards.getComponents()) {
            if (comp.isVisible()) {
                if (comp instanceof JPanel) {
                    return ((JPanel) comp).getName();
                }
            }
        }
        return "";
    }

    private static JPanel createPanel(String name, JComponent... components) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setName(name);
        panel.setBackground(Color.decode("#F5F5F5"));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        for (int i = 0; i < components.length; i++) {
            gbc.gridy = i;
            if (components[i] instanceof JLabel) {
                gbc.gridx = 0;
                gbc.anchor = GridBagConstraints.EAST;
            } else {
                gbc.gridx = 1;
                gbc.anchor = GridBagConstraints.WEST;
                gbc.weightx = 1.0;
            }
            panel.add(components[i], gbc);
        }
        return panel;
    }

    private static JPanel createPersonalInfoPanel() {
        nameField = new JTextField(30);
        emailField = new JTextField(30);
        phoneField = new JTextField(30);
        summaryArea = new JTextArea(10, 30);
        summaryArea.setLineWrap(true);
        summaryArea.setWrapStyleWord(true);
        JScrollPane summaryScrollPane = new JScrollPane(summaryArea);
        summaryScrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JLabel nameLabel = new JLabel("Name:");
        JLabel emailLabel = new JLabel("Email:");
        JLabel phoneLabel = new JLabel("Phone:");
        JLabel summaryLabel = new JLabel("Summary:");

        return createPanel("Personal Info", nameLabel, nameField, emailLabel, emailField, phoneLabel, phoneField, summaryLabel, summaryScrollPane);
    }

    private static JPanel createEducationPanel() {
        educationArea = new JTextArea(10, 40);
        educationArea.setLineWrap(true);
        educationArea.setWrapStyleWord(true);
        JScrollPane educationScrollPane = new JScrollPane(educationArea);
        educationScrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JLabel educationLabel = new JLabel("Education:");
        educationLabel.setVerticalAlignment(JLabel.TOP);

        return createPanel("Education", educationLabel, educationScrollPane);
    }

    private static JPanel createProjectsPanel() {
        projectsArea = new JTextArea(10, 40);
        projectsArea.setLineWrap(true);
        projectsArea.setWrapStyleWord(true);
        JScrollPane projectsScrollPane = new JScrollPane(projectsArea);
        projectsScrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JLabel projectsLabel = new JLabel("Projects:");
        projectsLabel.setVerticalAlignment(JLabel.TOP);

        return createPanel("Projects", projectsLabel, projectsScrollPane);
    }

    private static JPanel createAchievementsPanel() {
        achievementsArea = new JTextArea(10, 40);
        achievementsArea.setLineWrap(true);
        achievementsArea.setWrapStyleWord(true);
        JScrollPane achievementsScrollPane = new JScrollPane(achievementsArea);
        achievementsScrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JLabel achievementsLabel = new JLabel("Achievements:");
        achievementsLabel.setVerticalAlignment(JLabel.TOP);

        return createPanel("Achievements", achievementsLabel, achievementsScrollPane);
    }

    // Updated generatePdf() method for professional output
    private static void generatePdf() {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("Generated_Resume.pdf"));
            document.open();

            // Define Fonts
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD, BaseColor.BLACK);
            Font headingFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, new BaseColor(76, 175, 80)); // Green color
            Font contentFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);

            // --- Personal Information Section ---
            Paragraph name = new Paragraph(nameField.getText(), titleFont);
            name.setAlignment(Element.ALIGN_CENTER);
            document.add(name);

            Paragraph contactInfo = new Paragraph(emailField.getText() + " | " + phoneField.getText(), contentFont);
            contactInfo.setAlignment(Element.ALIGN_CENTER);
            document.add(contactInfo);
            document.add(new Paragraph("\n"));

            // --- Summary Section ---
            document.add(new Paragraph("Summary", headingFont));
            document.add(new Paragraph("----------------------------------------------------------------------------------------------------------------------------------"));
            document.add(new Paragraph(summaryArea.getText(), contentFont));
            document.add(new Paragraph("\n"));

            // --- Education Section ---
            document.add(new Paragraph("Education", headingFont));
            document.add(new Paragraph("----------------------------------------------------------------------------------------------------------------------------------"));
            Paragraph education = new Paragraph(educationArea.getText(), contentFont);
            document.add(education);
            document.add(new Paragraph("\n"));

            // --- Projects Section with bullet points ---
            document.add(new Paragraph("Projects", headingFont));
            document.add(new Paragraph("----------------------------------------------------------------------------------------------------------------------------------"));
            String[] projects = projectsArea.getText().split("\n");
            for (String project : projects) {
                if (!project.trim().isEmpty()) {
                    List list = new List(List.UNORDERED, 10);
                    list.add(new ListItem(project, contentFont));
                    document.add(list);
                }
            }
            document.add(new Paragraph("\n"));

            // --- Achievements & Certifications Section with bullet points ---
            document.add(new Paragraph("Achievements & Certifications", headingFont));
            document.add(new Paragraph("----------------------------------------------------------------------------------------------------------------------------------"));
            String[] achievements = achievementsArea.getText().split("\n");
            for (String achievement : achievements) {
                if (!achievement.trim().isEmpty()) {
                    List list = new List(List.UNORDERED, 10);
                    list.add(new ListItem(achievement, contentFont));
                    document.add(list);
                }
            }

            document.close();
            JOptionPane.showMessageDialog(frame, "Resume saved as Generated_Resume.pdf");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error generating PDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}