import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class FlashCardBuilder {
    private JTextArea question;
    private JTextArea answer;
    private JFrame frame;
    private ArrayList<FlashCard> cardList;


    public FlashCardBuilder(){
        //build UI
        frame = new JFrame("Flash Card");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //ADD A JPANAL TO HOLD EVERYTHING TOGETHER
        JPanel mainPanel = new JPanel();
        //create font
        Font greatFont = new Font("Helvetica Neue",Font.BOLD,21);
        question = new JTextArea(6,20);
        question.setLineWrap(true);
        question.setWrapStyleWord(true);
        question.setFont(greatFont);
        //answer area
        answer = new JTextArea(6,20);
        answer.setLineWrap(true);
        answer.setWrapStyleWord(true);
        answer.setFont(greatFont);


        //question Jscrollpane
        JScrollPane qScrollPane = new JScrollPane(question);
        qScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);


        //answer jscrollpane
        JScrollPane aScrollPane = new JScrollPane(answer);
        aScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        aScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);




        JButton nextButton = new JButton("Next Card");

        //create label
        JLabel qlabel = new JLabel("Question");
        JLabel alabel = new JLabel("Answer");

        //add components to main panel
        mainPanel.add(qlabel);
        mainPanel.add(qScrollPane);
        mainPanel.add(alabel);
        mainPanel.add(aScrollPane);
        mainPanel.add(nextButton);

        //add an event listener to take action when the next button is clicked
        nextButton.addActionListener(new NextCardListener());

        //ADD A MENU BAR
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem newMenuItem = new JMenuItem("New");
        JMenuItem saveMenuItem = new JMenuItem("Save");

        //add items to the menu item
        fileMenu.add(newMenuItem);
        fileMenu.add(saveMenuItem);
        //add the file menu to the menu bar
        menuBar.add(fileMenu);
        //add menu bar to the frame
        frame.setJMenuBar(menuBar);

        //add action listeners for the menu items
        newMenuItem.addActionListener(new NewMenuActionListener());
        saveMenuItem.addActionListener(new SaveMenuItemListener());

        //add to frame
        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        frame.setSize(500,600);
        frame.setVisible(true);

        //add flash cards to the arraylist
        cardList = new ArrayList<FlashCard>();



    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FlashCardBuilder();
            }
        });

    }
    class NextCardListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            //create a flash card
            FlashCard card = new FlashCard(question.getText(),answer.getText());
            cardList.add(card);
            clearCard();
            //System.out.println("Size of array list: "+ cardList.size());

        }

    }

    private void clearCard() {
        question.setText("");
        answer.setText("");
        question.requestFocus();
    }

    class NewMenuActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("New menu clicked");
        }
    }
    class SaveMenuItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            FlashCard card = new FlashCard(question.getText(),answer.getText());
            cardList.add(card);
            //create file dialog with file chooser
            JFileChooser fileSave = new JFileChooser();
            fileSave.showSaveDialog(frame);
            saveFile(fileSave.getSelectedFile());


        }
    }

    private void saveFile(File selectedFile) {
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter((selectedFile)));
            Iterator<FlashCard> cardIterator = cardList.iterator();
            while(cardIterator.hasNext()){
                FlashCard card = (FlashCard)cardIterator.next();
                writer.write(card.getQuestion() + "/");
                writer.write(card.getAnswer() +"\n");
            }
            writer.close();

        } catch (Exception e) {
            System.out.println("Couldn't write to file");
            e.printStackTrace();
        }
    }
}
