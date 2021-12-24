import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MainFrameForEnglishQuestionEdit extends JFrame {
    private JTextArea englishStatementText = new JTextArea();
    private JTextArea englishHintText = new JTextArea();
    private TextField englishTagsText = new TextField();
    private Question currentQuestion;
    private String formattedQuestionTable = "A_all_que_table";
    private JTextField qnoText = new JTextField();

    private JButton nextButton = new JButton("Next");
    private JButton prevButton = new JButton("Prev");
    private JButton loadButton = new JButton("Load");
    private JButton checkButton = new JButton("Check");
    private JButton clearButton = new JButton("Clear");
    private JButton translateButton = new JButton("Translate");
    private JButton updateButton = new JButton("Update Firebase");

    public MainFrameForEnglishQuestionEdit() {
        setTitle("UPSC Wars");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int) screenSize.getWidth(), (int) screenSize.getHeight());
        setLayout(new GridLayout(1, 1));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panelWithQuestionPanelAndButtonsPanel = new JPanel();
        panelWithQuestionPanelAndButtonsPanel.setLayout(new BorderLayout());
        add(panelWithQuestionPanelAndButtonsPanel);

        JPanel englishQuestionPanel = new JPanel();
        panelWithQuestionPanelAndButtonsPanel.add(englishQuestionPanel, BorderLayout.CENTER);
        englishQuestionPanel.setLayout(new GridLayout(1, 2));
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));

        JScrollPane scrollPane = new JScrollPane(englishStatementText);
        JScrollPane scrollPane1 = new JScrollPane(englishHintText);
        englishQuestionPanel.add(scrollPane);
        englishStatementText.setLineWrap(true);
        englishStatementText.setWrapStyleWord(true);
        englishHintText.setLineWrap(true);
        englishHintText.setWrapStyleWord(true);
        englishHintText.setFont(new Font("Calibri", Font.PLAIN, 20));
        englishStatementText.setFont(new Font("Calibri", Font.PLAIN, 20));
        englishQuestionPanel.add(scrollPane1);


        JPanel panelContainingQnoAndText = new JPanel();
        panelContainingQnoAndText.setLayout(new GridLayout(1, 2));
        panelContainingQnoAndText.add(qnoText);
        panelContainingQnoAndText.add(new JLabel("Qno"));

        buttonsPanel.add(panelContainingQnoAndText);
        buttonsPanel.add(loadButton);
        buttonsPanel.add(nextButton);
        buttonsPanel.add(prevButton);
        buttonsPanel.add(checkButton);
        buttonsPanel.add(clearButton);
        buttonsPanel.add(translateButton);
        buttonsPanel.add(updateButton);
        setVisible(true);

        panelWithQuestionPanelAndButtonsPanel.add(buttonsPanel, BorderLayout.SOUTH);

        loadButton.addActionListener(e -> loadQuestion(loadButton, allQueTableKey));

        updateButton.addActionListener(e -> updateQuestion(updateButton));
        checkButton.addActionListener(e -> loadQuestion(checkButton, formattedQuestionTable));

    }

    private void updateQuestion(JButton updateButton) {
        if (englishStatementText.getText() == null || englishStatementText.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(updateButton, "Statement is blank");
            return;
        }
        if (englishHintText.getText() == null || englishHintText.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(updateButton, "hint is blank");
            return;
        }
        currentQuestion.statement = englishStatementText.getText();
        currentQuestion.hint = englishHintText.getText();
        firestore.collection(formattedQuestionTable).document("Q" + currentQuestion.getQuestionNumber()).set(currentQuestion);
        JOptionPane.showMessageDialog(updateButton, "Updated!!");
    }

    private void loadQuestion(Component c, String collectionName) {
        JFrame window = new JFrame();
        window.setLayout(new FlowLayout());
        window.setTitle("Loading.. Please wait...");
        window.add(new JLabel("Loading..", SwingConstants.CENTER));
        window.setBounds(500, 500, 300, 20);
        window.setVisible(true);

        try {
            String qnoStr = qnoText.getText();
            if (qnoStr == null || qnoStr.isEmpty() || qnoStr.isBlank()) {
                JOptionPane.showMessageDialog(c, "Please enter valid Question Number.");
                return;
            }
            firestore = FirestoreClient.getFirestore();
            int qno = Integer.parseInt(qnoStr);
            DocumentSnapshot documentSnapshot = firestore.collection(collectionName).document("Q" + qno).get().get();
            if (!documentSnapshot.exists()) {
                JOptionPane.showMessageDialog(loadButton, "Question not found");
                return;
            }
            Map<String, Object> dataMap = documentSnapshot.getData();
            currentQuestion = Question.fromQuestionDataMap(dataMap);
            englishStatementText.setText(currentQuestion.statement);
            englishHintText.setText(currentQuestion.hint);

            return;
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (ExecutionException ex) {
            ex.printStackTrace();
        } finally {
            window.setVisible(false);
            window.dispose();
        }
    }

    private Firestore firestore;
    private String allQueTableKey = "all_que_table";
    private String stmtFieldKey = "statement";
    private String hintFieldKey = "hint";
    private String tagsFieldKey = "tags";
    private String qnoFieldKey = "Qno";
    private String bookmarkedFieldKey = "bookmarked";
    private String correctFieldKey = "correct";
    private String subjectFieldKey = "subject";
    private String userResponseFieldKey = "userResponse";

    public static void main(String[] args) {
        FirebaseOptions options = null;
        try {
            options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.getApplicationDefault())
                    .setDatabaseUrl("https://questionmaker-a821f.firebaseio.com")
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FirebaseApp.initializeApp(options);
        new MainFrameForEnglishQuestionEdit();
    }
}
