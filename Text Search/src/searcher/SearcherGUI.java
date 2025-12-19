package searcher;
import helpers.*;
import printer.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class SearcherGUI extends JFrame {
    private JTextField inputFilePathField;
    private JTextField keywordField;
    private JComboBox<String> matchModeComboBox;
    private JCheckBox caseSensitiveCheckBox;
    private JTextField replaceTargetField;
    private JTextField outputFilePathField;
    private JTextArea resultTextArea;
    private final JFileChooser fileChooser = new JFileChooser();
    private StringSearcher stringSearcher;
    private int queryNumber = 1;
    public SearcherGUI() {
        super("Text Search & Replace Tool");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Fallback to default
        }

        Color bgColor = new Color(236, 229, 229);
        Color fieldColor = new Color(245, 235, 235);
        Color textColor = Color.BLACK;

        UIManager.put("Panel.background", bgColor);
        UIManager.put("TitledBorder.titleColor", textColor);
        UIManager.put("Label.foreground", textColor);
        UIManager.put("CheckBox.background", bgColor);
        UIManager.put("CheckBox.foreground", textColor);

        UIManager.put("TextField.background", fieldColor);
        UIManager.put("TextField.foreground", textColor);
        UIManager.put("TextField.caretColor", textColor);
        UIManager.put("TextArea.background", new Color(30, 30, 30));
        UIManager.put("TextArea.foreground", new Color(150, 255, 150));
        UIManager.put("ComboBox.background", fieldColor);
        UIManager.put("ComboBox.foreground", textColor);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));


        // Top Panel: Input File Selection
        add(createInputPanel(), BorderLayout.NORTH);

        // Middle Panel: Controls and Results
        JPanel mainContent = new JPanel(new BorderLayout(5, 10));
        mainContent.add(createControlPanel(), BorderLayout.NORTH);
        mainContent.add(createResultPanel(), BorderLayout.CENTER);

        add(mainContent, BorderLayout.CENTER);

        pack();
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Input Configuration"));

        inputFilePathField = new JTextField("input.txt");
        inputFilePathField.setEditable(false); // Disables typing
        inputFilePathField.setBackground(Color.WHITE); // Keeps it looking clean

        JButton browseInputButton = new JButton("Browse");
        browseInputButton.addActionListener(e -> {
            queryNumber = 1;
            openFileChooser(inputFilePathField);
            String fileContent = FileHelper.readFromFile(inputFilePathField.getText());
            double buildTime = CodeTimer.measureTimeNano(() -> {
                stringSearcher = new StringSearcher(fileContent);
            }) / 1000000.0;
            resultTextArea.setText("PreCompute time = " + buildTime + " ms\n");
        });

        panel.add(new JLabel(" Input File: "), BorderLayout.WEST);
        panel.add(inputFilePathField, BorderLayout.CENTER);
        panel.add(browseInputButton, BorderLayout.EAST);
        return panel;
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Search & Replace Controls"));

        panel.add(new JLabel("Keyword to Find:"));
        keywordField = new JTextField();
        panel.add(keywordField);

        panel.add(new JLabel("Match Mode:"));
        String[] modes = {MatchMode.WHOLE, MatchMode.PREFIX, MatchMode.SUBSTRING};
        matchModeComboBox = new JComboBox<>(modes);
        panel.add(matchModeComboBox);

        panel.add(new JLabel("Case Sensitive:"));
        caseSensitiveCheckBox = new JCheckBox();
        panel.add(caseSensitiveCheckBox);

        panel.add(new JLabel("Replace with (leave empty if no replacement is needed):"));
        replaceTargetField = new JTextField("");
        panel.add(replaceTargetField);

        panel.add(new JLabel("Output Destination:"));

        JPanel outputContainer = new JPanel(new BorderLayout(5, 0));
        outputFilePathField = new JTextField("output.txt");
        outputFilePathField.setEditable(false); // Disables typing
        outputFilePathField.setBackground(Color.WHITE);

        JButton outputBrowseButton = new JButton("Browse");
        outputBrowseButton.addActionListener(e -> openFileChooser(outputFilePathField));

        outputContainer.add(outputFilePathField, BorderLayout.CENTER);
        outputContainer.add(outputBrowseButton, BorderLayout.EAST);
        panel.add(outputContainer);

        JButton executeButton = new JButton("Run Search & Replace");
        executeButton.setFont(new Font("Arial", Font.BOLD, 12));

        executeButton.addActionListener(e -> {
            char caseFlag = caseSensitiveCheckBox.isSelected() ? 's' : 'i';
            String keyword = keywordField.getText();
            String replace = replaceTargetField.getText();
            String mode = (String) matchModeComboBox.getSelectedItem();
            String outPath = outputFilePathField.getText();

            // Create the object
            SearchQuery query = new SearchQuery(caseFlag, keyword, replace, mode, outPath);

            // Display data in output section
            resultTextArea.setText(getAndWriteResultMessage(query, queryNumber++));
        });
        panel.add(new JLabel(""));
        panel.add(executeButton);
        return panel;
    }
    private JPanel createResultPanel() {
        resultTextArea = new JTextArea();
        resultTextArea.setEditable(false);
        resultTextArea.setText("Ready. Results and logs will appear here...");

        JScrollPane resultScrollPane = new JScrollPane(resultTextArea);
        resultScrollPane.setBorder(BorderFactory.createTitledBorder("Search Results & Console Output"));

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(resultScrollPane, BorderLayout.CENTER);
        return panel;
    }
    private void openFileChooser(JTextField fileField) {
        fileChooser.setPreferredSize(new Dimension(1000, 800));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if(FileHelper.exists(file.getAbsolutePath())) {
                fileField.setText(file.getAbsolutePath());
            }
        }
    }
    private String getAndWriteResultMessage(SearchQuery sq, int queryNum){
        if(stringSearcher == null){
            stringSearcher = new StringSearcher(FileHelper.readFromFile(inputFilePathField.getText()));
        }
        StringBuilder result = new StringBuilder();
        ArrayList<SearchMatch> matches;
        double time = CodeTimer.measureTimeNano(() -> {stringSearcher.search(sq);}) / 1000000.0;
        matches = stringSearcher.search(sq);

        result.append("Query ").append(queryNum).append(" Result\n").append("Query: ").append(sq);
        result.append("\nSearch time: ").append(time).append(" ms\nCount: ");
        result.append(matches.size()).append("\nMatches: ").append(matches.toString()).append("\n");
        final String[] replace = new String[1];
        if(sq.getReplaceTarget() != null && !sq.getReplaceTarget().isEmpty()){
            double replaceTime = CodeTimer.measureTimeNano(() -> {
                replace[0] = stringSearcher.getReplacedText(matches, sq.getReplaceTarget());
            }) / 1000000.0;
            result.append("Replace time: ").append(replaceTime).append(" ms\n\n").append(replace[0]);
        } else {
            result.append(stringSearcher.getHighlightedText(matches));
        }
        result.append(Printer.separator).append("\n");
        String ans = result.toString();
        FileHelper.writeOnFile(sq.getOutputPath(), ans, queryNum > 1);
        return ans;
    }
}