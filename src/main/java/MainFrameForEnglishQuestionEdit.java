import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
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
    private JButton undoStatementChange = new JButton("Undo Statement");
    private JButton undoHintChange = new JButton("Undo Hint");
    UndoManager statementUndoManager = new UndoManager();
    UndoManager hintUndoManager = new UndoManager();

    private JButton prevButton = new JButton("Load Prev");
    private JButton loadButton = new JButton("Load");
    private JButton nextButton = new JButton("Load Next");
    private JButton checkPreviousButton = new JButton("Check Previous");
    private JButton checkButton = new JButton("Check");
    private JButton checkNextButton = new JButton("Check Next");
    private JButton updateButton = new JButton("Update Firebase");
    private JButton pickFileButton = new JButton("Pick File");
    private static String jsonFilePath = "";

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
        englishStatementText.getDocument().addUndoableEditListener(new UndoableEditListener() {
            @Override
            public void undoableEditHappened(UndoableEditEvent e) {
                statementUndoManager.addEdit(e.getEdit());
            }
        });
        englishHintText.setLineWrap(true);
        englishHintText.getDocument().addUndoableEditListener(new UndoableEditListener() {
            @Override
            public void undoableEditHappened(UndoableEditEvent e) {
                hintUndoManager.addEdit(e.getEdit());
            }
        });
        englishHintText.setWrapStyleWord(true);
        englishHintText.setFont(new Font("Calibri", Font.PLAIN, 20));
        englishStatementText.setFont(new Font("Calibri", Font.PLAIN, 20));
        englishQuestionPanel.add(scrollPane1);


        JPanel panelContainingQnoAndText = new JPanel();
        panelContainingQnoAndText.setLayout(new GridLayout(1, 2));
        panelContainingQnoAndText.add(qnoText);
        panelContainingQnoAndText.add(new JLabel("Qno"));

        buttonsPanel.add(panelContainingQnoAndText);
        buttonsPanel.add(prevButton);
        buttonsPanel.add(loadButton);
        buttonsPanel.add(nextButton);
        buttonsPanel.add(checkPreviousButton);
        buttonsPanel.add(checkButton);
        buttonsPanel.add(checkNextButton);
        buttonsPanel.add(undoStatementChange);
        buttonsPanel.add(undoHintChange);
        buttonsPanel.add(updateButton);
        buttonsPanel.add(pickFileButton);
        setVisible(true);

        panelWithQuestionPanelAndButtonsPanel.add(buttonsPanel, BorderLayout.SOUTH);

        loadButton.addActionListener(e -> loadQuestion(loadButton, allQueTableKey));

        updateButton.addActionListener(e -> updateQuestion(updateButton));
        checkButton.addActionListener(e -> loadQuestion(checkButton, formattedQuestionTable));
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentQuestion == null) {
                    JOptionPane.showMessageDialog(nextButton, "Failed!!");
                    return;
                }
                qnoText.setText((currentQuestion.Qno + 1) + "");
                loadQuestion(checkButton, allQueTableKey);
            }
        });

        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentQuestion == null) {
                    JOptionPane.showMessageDialog(nextButton, "Failed!!");
                    return;
                }
                qnoText.setText((currentQuestion.Qno - 1) + "");
                loadQuestion(checkButton, allQueTableKey);
            }
        });
        checkNextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentQuestion == null) {
                    JOptionPane.showMessageDialog(nextButton, "Failed!!");
                    return;
                }
                qnoText.setText((currentQuestion.Qno + 1) + "");
                loadQuestion(checkButton, formattedQuestionTable);
            }
        });

        checkPreviousButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentQuestion == null) {
                    JOptionPane.showMessageDialog(nextButton, "Failed!!");
                    return;
                }
                qnoText.setText((currentQuestion.Qno - 1) + "");
                loadQuestion(checkButton, formattedQuestionTable);
            }
        });

        undoStatementChange.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (statementUndoManager.canUndo()) {
                    statementUndoManager.undo();
                }
            }
        });
        undoHintChange.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (hintUndoManager.canUndo()) {
                    hintUndoManager.undo();
                }
            }
        });

        pickFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser("d:");
                int returnVal = fc.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    try {
                        jsonFilePath = file.getCanonicalPath();
                        System.out.println("Path of selected path = " + jsonFilePath);
                        prevButton.setEnabled(true);
                        loadButton.setEnabled(true);
                        nextButton.setEnabled(true);
                        checkPreviousButton.setEnabled(true);
                        checkButton.setEnabled(true);
                        checkNextButton.setEnabled(true);
                        undoStatementChange.setEnabled(true);
                        undoHintChange.setEnabled(true);
                        updateButton.setEnabled(true);

                        FileInputStream serviceAccount =
                                new FileInputStream(jsonFilePath);
                        FirebaseOptions options = new FirebaseOptions.Builder()
                                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                                .setDatabaseUrl("https://upsc-wars.firebaseio.com")
                                .build();
                        FirebaseApp.initializeApp(options);

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        prevButton.setEnabled(false);
        loadButton.setEnabled(false);
        nextButton.setEnabled(false);
        checkPreviousButton.setEnabled(false);
        checkButton.setEnabled(false);
        checkNextButton.setEnabled(false);
        undoStatementChange.setEnabled(false);
        undoHintChange.setEnabled(false);
        updateButton.setEnabled(false);

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

        if (currentQuestion == null) {
            JOptionPane.showMessageDialog(updateButton, "Something not Right!");
            return;
        }

        int n = JOptionPane.showConfirmDialog(
                updateButton, "Are you sure?",
                "Double check to make sure you don't lose your edits",
                JOptionPane.YES_NO_OPTION);
        if (n == JOptionPane.YES_OPTION) {
            currentQuestion.statement = englishStatementText.getText();
            currentQuestion.hint = englishHintText.getText();
            firestore.collection(formattedQuestionTable).document("Q" + currentQuestion.getQno()).set(currentQuestion.toDataMap());
            JOptionPane.showMessageDialog(updateButton, "Updated!!");
        }

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

        new MainFrameForEnglishQuestionEdit();
    }
}
