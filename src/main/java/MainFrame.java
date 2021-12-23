import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MainFrame extends JFrame {
    private JTextArea englishStatementText = new JTextArea();
    private JTextArea englishHintText = new JTextArea();
    private TextField englishTagsText = new TextField();
    private DefaultListModel listModel = new DefaultListModel();
    private JTextArea hindiStatementText = new JTextArea();
    private JTextArea hindiHintText = new JTextArea();
    private TextField hindiTagsText = new TextField();
    private JList list = new JList(listModel);
    private JButton nextButton = new JButton("Next");
    private JButton prevButton = new JButton("Prev");
    private JButton loadButton = new JButton("Load");
    private JButton checkButton = new JButton("Check");
    private JButton clearButton = new JButton("Clear");
    private JButton translateButton = new JButton("Translate");
    private JButton updateButton = new JButton("Update Firebase");

    public MainFrame() {
        setTitle("UPSC Wars");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int) screenSize.getWidth(), (int) screenSize.getHeight());
        setLayout(new GridLayout(1, 1));

        JPanel panelContainingTwoPanelsOneContainingTwoPanelsContainingEnglishAndHindiQuestionAndOtherPanelContainingButtons = new JPanel();
        panelContainingTwoPanelsOneContainingTwoPanelsContainingEnglishAndHindiQuestionAndOtherPanelContainingButtons.setBorder(new TitledBorder("UPSC Wars"));
        panelContainingTwoPanelsOneContainingTwoPanelsContainingEnglishAndHindiQuestionAndOtherPanelContainingButtons.setLayout(new BoxLayout(panelContainingTwoPanelsOneContainingTwoPanelsContainingEnglishAndHindiQuestionAndOtherPanelContainingButtons, BoxLayout.Y_AXIS));


        JPanel englishQuestionPanel = new JPanel();
        englishQuestionPanel.setBorder(new TitledBorder("English"));
        englishQuestionPanel.setLayout(new BoxLayout(englishQuestionPanel, BoxLayout.Y_AXIS));


        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));

        JPanel hindiPanel = new JPanel();
        hindiPanel.setLayout(new BoxLayout(hindiPanel, BoxLayout.Y_AXIS));

        JPanel panelContainingListOfTagsAndListOfSubjects = new JPanel();
        panelContainingListOfTagsAndListOfSubjects.setLayout(new BoxLayout(panelContainingListOfTagsAndListOfSubjects, BoxLayout.Y_AXIS));

        listModel.addElement("Tag1");
        listModel.addElement("Tag2");
        listModel.addElement("Tag333333333");
        listModel.addElement("Tag4");
        listModel.addElement("Tag5");
        listModel.addElement("Tag6");
        listModel.addElement("Tag7");
        listModel.addElement("Tag8");


        list.setLayoutOrientation(JList.HORIZONTAL_WRAP);

        panelContainingListOfTagsAndListOfSubjects.add(list);
        panelContainingListOfTagsAndListOfSubjects.add(new JButton("Add Tag"));

//        panelContainingListOfTagsAndListOfSubjects.add()

        JPanel panelContainingEnglishAndHindiQuestions = new JPanel();
        panelContainingEnglishAndHindiQuestions.setLayout(new BoxLayout(panelContainingEnglishAndHindiQuestions, BoxLayout.X_AXIS));


        panelContainingEnglishAndHindiQuestions.add(englishQuestionPanel);
        panelContainingEnglishAndHindiQuestions.add(hindiPanel);

        englishStatementText.setLineWrap(true);
        englishHintText.setLineWrap(true);
        englishQuestionPanel.add(englishStatementText);
        englishQuestionPanel.add(new JToolBar.Separator());
        englishQuestionPanel.add(englishHintText);
        englishQuestionPanel.add(new JToolBar.Separator());
        englishQuestionPanel.add(englishTagsText);

        TextArea stmt = new TextArea("Statement");

        JPanel hindiQuestionPanel = new JPanel();
        hindiQuestionPanel.setLayout(new BoxLayout(hindiQuestionPanel, BoxLayout.Y_AXIS));

        hindiQuestionPanel.add(hindiStatementText);
        hindiQuestionPanel.add(new JToolBar.Separator());
        hindiQuestionPanel.add(hindiHintText);
        hindiQuestionPanel.add(new JToolBar.Separator());
        hindiQuestionPanel.add(hindiTagsText);
        hindiPanel.add(panelContainingListOfTagsAndListOfSubjects);

        hindiPanel.add(hindiQuestionPanel);


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

        panelContainingListOfTagsAndListOfSubjects.add(buttonsPanel);

        panelContainingTwoPanelsOneContainingTwoPanelsContainingEnglishAndHindiQuestionAndOtherPanelContainingButtons.add(panelContainingEnglishAndHindiQuestions);
        panelContainingEnglishAndHindiQuestions.add(hindiQuestionPanel);

        add(panelContainingTwoPanelsOneContainingTwoPanelsContainingEnglishAndHindiQuestionAndOtherPanelContainingButtons);
        setVisible(true);


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
