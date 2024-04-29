import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class TypingSpeedTest extends JFrame {
    JTextArea textArea = new JTextArea();
    JButton startButton = new JButton("Start");
    JLabel wordLabel = new JLabel();
    JLabel timerLabel = new JLabel("Time: 60");
    JLabel correctWordsLabel = new JLabel("Correct words: 0");
    JLabel incorrectWordsLabel = new JLabel("Incorrect words: 0");
    ArrayList<String> words = new ArrayList<>();
    Random random = new Random();
    AtomicInteger timeLeft = new AtomicInteger(60);
    AtomicBoolean timeStarted = new AtomicBoolean(false);
    int correctWordsCount = 0;
    int incorrectWordsCount = 0;
    private Timer timer;

    public TypingSpeedTest() {
        loadWords();
        setupUI();
    }

    ////      CREATE A USER INTERFACE
    private void setupUI() {
        setTitle("Typing speed test");
        setSize(420, 350);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        startButton.setBounds(150, 20, 100, 30);
        wordLabel.setBounds(20, 60, 360, 30);
        textArea.setBounds(20, 100, 360, 100);
        timerLabel.setBounds(20, 210, 100, 30);
        correctWordsLabel.setBounds(20, 240, 200, 30);
        incorrectWordsLabel.setBounds(250, 240, 200, 30);


        startButton.addActionListener(e -> startTest());                    //START BUTTON OPERATION
        textArea.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    checkWordAndContinue();
                }
            }
        });

        add(startButton);
        add(wordLabel);
        add(textArea);
        add(timerLabel);
        add(correctWordsLabel);
        add(incorrectWordsLabel);

        setVisible(true);
    }
    ////


    ////        LOADING WORDS THAT ARE IN THE TXT FILE
    private void loadWords() {
        try {
            File file = new File("words.txt");
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                words.add(scanner.nextLine());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Words.txt file not found");
        }
    }
    ////


    ////        STARTING THE PROGRAM
    private void startTest() {
        textArea.setText("");
        textArea.setEnabled(true);
        textArea.requestFocus();
        wordLabel.setText(getRandomWord());
        correctWordsCount = 0;
        incorrectWordsCount = 0;
        updateLabels();
        timeLeft.set(60);                   //time setting to 60 seconds (can be changed)
        timerLabel.setText("Time: 60");
        timeStarted.set(false);
        timer = new Timer(1000, e -> updateTimer());
        timer.start();
    }
    ////

    ////        UPDATE ON REMAINING TIME
    private void updateTimer()
    {
        if (timeLeft.get() > 0)
        {
            timeLeft.decrementAndGet();
            timerLabel.setText("Time: " + timeLeft);
        }
        else
        {
            timer.stop();
            textArea.setEnabled(false);
            if (timeStarted.get())
            {
                JOptionPane.showMessageDialog(this, "Your typing speed is: " + correctWordsCount + " words per minutes");
            }
        }
    }
    ////

    ////        CHECKING THE CORRECTNESS OF THE ENTERED WORD
    private void checkWordAndContinue() {
        if (!timeStarted.get()) {
            timeStarted.set(true);
        }
        String typedWord = textArea.getText().trim();
        if (!typedWord.isEmpty()) {
            if (typedWord.equals(wordLabel.getText())) {
                correctWordsCount++;
            } else {
                incorrectWordsCount++;
            }
            updateLabels();
        }
        textArea.setText("");
        wordLabel.setText(getRandomWord());
    }
    ////

    //// VOID FUNCTION USED TO UPDATE CORRECTLY AND INCORRECTLY STORED WORDS, WHICH ARE CONTINUOUSLY UPDATED AND DISPLAYED
    private void updateLabels() {
        correctWordsLabel.setText("Correct words: " + correctWordsCount);
        incorrectWordsLabel.setText("Incorrect words: " + incorrectWordsCount);
    }
    ////


    ////A STRING FUNCTION RETURNING A VARIABLE THAT STORES THE DRAWN WORD FROM THE TXT FILE
    private String getRandomWord() {
        return words.get(random.nextInt(words.size()));
    }
    ////

    public static void main(String[] args) {
        new TypingSpeedTest();
    }
}