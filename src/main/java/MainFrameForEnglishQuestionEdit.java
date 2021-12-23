import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MainFrameForEnglishQuestionEdit extends JFrame {
    private JTextArea englishStatementText = new JTextArea();
    private JTextArea englishHintText = new JTextArea();
    private TextField englishTagsText = new TextField();

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


        JPanel englishQuestionPanel = new JPanel();
        add(englishQuestionPanel);
        englishQuestionPanel.setLayout(new BoxLayout(englishQuestionPanel, BoxLayout.Y_AXIS));
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));

        englishQuestionPanel.add(englishStatementText);
        englishQuestionPanel.add(new JToolBar.Separator());
        englishHintText.setLineWrap(true);
        englishQuestionPanel.add(englishHintText);
        englishQuestionPanel.add(new JToolBar.Separator());
        englishQuestionPanel.add(englishTagsText);


        JPanel panelContainingQnoAndText = new JPanel();
        panelContainingQnoAndText.setLayout(new FlowLayout());
        panelContainingQnoAndText.add(new JTextField("2000"));
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
        englishQuestionPanel.add(buttonsPanel);


        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                firestore = FirestoreClient.getFirestore();
                try {
                    DocumentSnapshot documentSnapshot = firestore.collection(allQueTableKey).document("Q0").get().get();
                    Map<String, Object> dataMap = documentSnapshot.getData();
                    englishStatementText.setText(dataMap.get(stmtFieldKey).toString());
                    englishHintText.setText(dataMap.get(hintFieldKey).toString());
                    englishTagsText.setText(dataMap.get(tagsFieldKey).toString());

                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                } catch (ExecutionException ex) {
                    ex.printStackTrace();
                }
            }
        });

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
