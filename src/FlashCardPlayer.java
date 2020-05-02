import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

public class FlashCardPlayer {

    private JTextArea display;
    private JTextArea answer;
    private ArrayList<FlashCard> cardList;
    private Iterator<FlashCard> cardIterator;
    private FlashCard currentCard;
    private int currentCardIndex;
    private JFrame frame;
    private boolean isShowAnswer;
    private JButton showAnwser;

    public FlashCardPlayer(){
        //build UI
        frame = new JFrame("FLASH CARD PLAYER");
        JPanel mainPanel = new JPanel();
        Font greeatFont = new Font("Helvetica Neue",Font.BOLD,22);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        display = new JTextArea(10,20);

        JScrollPane qScrollPane = new JScrollPane(display);
        qScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        showAnwser= new JButton("Show Answer");

        mainPanel.add(qScrollPane);
        mainPanel.add(showAnwser);

        showAnwser.addActionListener(new NextCardListener());

        //add menu
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem loadMenuItem = new JMenuItem("Load Card Set");
        loadMenuItem.addActionListener(new OpenMenuListner());
        fileMenu.add(loadMenuItem);
         menuBar.add(fileMenu);
         frame.setJMenuBar(menuBar);
        //add to frame
        frame.getContentPane().add(BorderLayout.CENTER,mainPanel);
        frame.setSize(640,500);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FlashCardPlayer();
            }
        });
    }
    class NextCardListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(isShowAnswer){
                display.setText(currentCard.getAnswer());
                showAnwser.setText("Next Card");
                isShowAnswer = false;
            }
            else{
                //show the next question
                if(cardIterator.hasNext()){
                    showNextCard();
                }
                else{
                    //no more card
                    display.setText("That was the last card");
                    showAnwser.setEnabled(false);
                }
            }

        }
    }

    private class OpenMenuListner implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileOpen = new JFileChooser();
            fileOpen.showOpenDialog(frame);
            loadFile(fileOpen.getSelectedFile());
        }
    }

    private void loadFile(File selectedFile) {
        cardList = new ArrayList<FlashCard>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
            String line = null;
            while((line = reader.readLine()) != null){
                makeCard(line);

            }
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't read card.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //show the first card
        cardIterator = cardList.iterator();
        showNextCard();
    }

    private void showNextCard() {
        currentCard = (FlashCard)cardIterator.next();
        display.setText(currentCard.getQuestion());
        showAnwser.setText("Show Answer");
        isShowAnswer = true;

    }

    private void makeCard(String lineToParse) {
        //using the string tokenizer
        StringTokenizer result = new StringTokenizer(lineToParse, "/");
        if(result.hasMoreElements()){
            FlashCard card = new FlashCard(result.nextToken(),result.nextToken());
            cardList.add(card);
            System.out.println("Made a card");
        }
//        String[] result = lineToParse.split("/"); //[question, answer]
//        FlashCard card = new FlashCard(result[0],result[1]);
//        cardList.add(card);
//        System.out.println("Made a card");

    }
}
