//the start to our next generation pigLatinTranslator
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.ArrayList;

public class PigLatinTranslator extends JFrame
{
  //main() to run it
  public static void main(String[] args)
  {
    PigLatinTranslator translator = new PigLatinTranslator(500, 400);
  }
  
  //PIV
  private int width, height;
  
  private JLabel englishL, pigLatinL, directionsL;
  private JTextArea englishTA, pigLatinTA;
  private JButton translateB;
  
  private TranslateButtonHandler tbHandler;
  
  public PigLatinTranslator(int w, int h)
  {
    //we'll add text and customize for actual use later
    width = w;
    height = h;
    
    englishL = new JLabel("englishL");
    pigLatinL = new JLabel("pigLatinL");
    directionsL = new JLabel("directionsL");
    
    englishTA = new JTextArea("englishTA");
    pigLatinTA = new JTextArea("pigLatinTA");
    
    translateB = new JButton("Translate/Anslatetray");
    tbHandler = new TranslateButtonHandler();
    translateB.addActionListener(tbHandler);
    
    setTitle("Pig Latin Translator");
    
    Container pane = this.getContentPane();
    
    pane.setLayout(new GridLayout(3, 1));
    
    pane.add(directionsL);
    pane.add(englishL);
    pane.add(englishTA);
    pane.add(pigLatinL);
    pane.add(pigLatinTA);
    pane.add(translateB);
    
    //need to learn BufferedImage
    //setIconImage(new ImageIcon("https://img.purch.com/w/660/aHR0cDovL3d3dy5saXZlc2NpZW5jZS5jb20vaW1hZ2VzL2kvMDAwLzEwNC84MzAvb3JpZ2luYWwvc2h1dHRlcnN0b2NrXzExMTA1NzIxNTkuanBn"));
    setSize(width, height);
    setVisible(true);
    setDefaultCloseOperation( EXIT_ON_CLOSE );
  }
  
  //button handler nested class (anonymous methods are messy so we're doing it this way)
  private class TranslateButtonHandler implements ActionListener
  {
    public void actionPerformed(ActionEvent e)
    {
      //true signifies PigLatin to Enlish
      //false signifies English to PigLatin
      if(englishTA.getText().equals(""))
      {
        StringProcessor translate = new StringProcessor(pigLatinTA.getText(), true);
        englishTA.setText(translate.getEnglish());
      }
      else
      {
        StringProcessor translate = new StringProcessor(englishTA.getText(), false);
        pigLatinTA.setText(translate.getPigLatin());
      }
    }
  }
  
  private class StringProcessor
  {
    private String english;
    private String pigLatin;
    private ArrayList<String> words;
    private boolean toEnglish;
    //true - P > E
    //false - E > P
    
    public StringProcessor(String input, boolean b)////////////////////////////////////////
    {
      toEnglish = b;
      pigLatin = "";
      english = "";
      words = parseBySpaces(input);
      ArrayList<WordProcessor> translatedWords = new ArrayList<WordProcessor>();
      
      for(String s: words)
      {
        translatedWords.add(new WordProcessor(s, toEnglish));
      }
      
      if(toEnglish)
      {
        pigLatin = input;
        for(WordProcessor w: translatedWords)
        {
          english += w.reconstructEnglish() + " ";
        }
        english = english.substring(0, english.length() - 1);
      }
      else
      {
        english = input;
        for(WordProcessor w: translatedWords)
        {
          pigLatin += w.reconstructPigLatin() + " ";
        }
        pigLatin = pigLatin.substring(0, pigLatin.length() - 1);
      }
    }
    
    private ArrayList<String> parseBySpaces(String input)
    {
      ArrayList<String> words = new ArrayList<String>();
      int lastSpace = 0;
      
      for(int i = 0; i < input.length(); i++)
      {
        if(i == input.length() - 1 && input.charAt(i) != ' ' && input.charAt(i) != (char)(10))
        {
          words.add(input.substring(lastSpace, i+1));
        }
        else if(input.charAt(i) == ' ')
        {
          words.add(input.substring(lastSpace, i));
          lastSpace = i+1;
        }
        else if(input.charAt(i) == (char)(10))
        {
          words.add(input.substring(lastSpace,i+1));
          lastSpace = i+1;
        }
      }
      return words;
    }
    
    public String getEnglish(){return english;}
    public String getPigLatin(){return pigLatin;}
  }
  
  
  private class WordProcessor////////////////////////////////////////............................................
  {
    private String startPunct;
    private String endPunct;
    private String englishWord;
    private String pigLatinWord;
    
    public WordProcessor(String input, boolean toEnglish)
    {
      pigLatinWord = "";
      englishWord = "";
      startPunct = "";
      endPunct = "";
      
      if(toEnglish)
      {
        translateToEnglish(input);
      }
      else
      {
        translateToPigLatin(input);
      }
    }
    ///////////////////////////////////////////////......................................,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
    public String reconstructPigLatin()
    {
      return startPunct + pigLatinWord + endPunct;
    }
    
    public String reconstructEnglish()
    {
      return startPunct + englishWord + endPunct;
    }
    
    private void translateToEnglish(String input)
    {
      englishWord = handleEndPunctuation(input);
      englishWord = handleStartPunctuation(englishWord);
      pigLatinWord = translateWordToPigLatin(englishWord);
    }
    
    private void translateToPigLatin(String input)
    {
      pigLatinWord = handleEndPunctuation(input);
      pigLatinWord = handleStartPunctuation(pigLatinWord);
      englishWord = translateWordToEnglish(pigLatinWord);
    }
    
    private String handleStartPunctuation(String input)
    {
      boolean letters = false;
      String wordWithNoStartPunctuation = new String("");
      
      for(int i = input.length() - 1; i >= 0; i--)
      {
        letters = false;
        if((int)(input.toLowerCase().charAt(i)) < 97 || (int)(input.toLowerCase().charAt(i)) > 122)
        {
          for(int j = i-1; j >= 0; j--)
          {
            if((int)(input.toLowerCase().charAt(j)) >= 97 && (int)(input.toLowerCase().charAt(j)) <= 122)
            {
              letters = true;
            }
          }
          if(!letters)
          {
            wordWithNoStartPunctuation = input.substring(i+1, input.length());
            startPunct = input.substring(0, i+1);
            break;
          }
        }
        if(i == 0)
        {
          wordWithNoStartPunctuation = input;
          startPunct = "";
        }
      }
      
      return wordWithNoStartPunctuation;
    }
  
  private String handleEndPunctuation(String input)
    {
      boolean letters = false;
      String wordWithNoEndPunctuation = new String("");
      
      for(int i = 0; i < input.length(); i++)
      {
        letters = false;
        if((int)(input.toLowerCase().charAt(i)) < 97 || (int)(input.toLowerCase().charAt(i)) > 122)
        {
          for(int j = i+1; j < input.length(); j++)
          {
            if((int)(input.toLowerCase().charAt(j)) >= 97 && (int)(input.toLowerCase().charAt(j)) <= 122)
            {
              letters = true;
            }
          }
          if(!letters)
          {
            wordWithNoEndPunctuation = input.substring(0, i);
            endPunct = input.substring(i, input.length());
            break;
          }
        }
        if(i == input.length() - 1)
        {
          wordWithNoEndPunctuation = input;
          endPunct = "";
        }
      }
      
      return wordWithNoEndPunctuation;
  }
    
    public String translateWordToEnglish(String input)
    {
      return input;
    }
    
    public String translateWordToPigLatin(String input)
    {
      return input;
    }
  }
}
