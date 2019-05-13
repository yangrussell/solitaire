
/**
 * The Card class defines a playing card and the methods an object
 * of the Card class can use.
 * @author Russell Yang
 * @version 11/9/17
 */
public class Card
{
    /**
     * Instance variables
     */
    private int rank; //stores the rank of a card
    private String suit; //stores the suit of a card
    private boolean isFaceUp; //stores whether the card is face up

    /**
     * Constructor with parameters; initializes instance variables.
     * @param initRank an initial rank of a card
     * @param initSuit an initial suit of a card
     */
    public Card(int initRank, String initSuit)
    {
        rank = initRank;
        suit = initSuit;
        isFaceUp = false;
    }

    /**
     * Gets the rank of the card.
     * @postcondition the rank of the card is returned.
     * @return the rank of the card.
     */
    public int getRank()
    {
        return rank;
    }
    
    /**
     * Gets the suit of the card.
     * @postcondition the suit of the card is returned.
     * @return the suit of the card.
     */
    public String getSuit()
    {
        return suit;
    }
    
    /**
     * Checks whether the card is red.
     * @postcondition true is returned if the card is a diamond
     *                or heart (red cards), false otherwise
     * @return true if the card is red, false otherwise.
     */
    public boolean isRed()
    {
        return(suit.equals("d") || suit.equals("h"));
    }
    
    /**
     * Checks if the card is face up.
     * @postcondition true is returned if the card is face up, false otherwise.
     * @return true if the card is face up, false otherwise.
     */
    public boolean isFaceUp()
    {
        return isFaceUp;
    }
    
    /**
     * Turns the card to face up.
     * @postcondition isFaceUp is set to true.
     */
    public void turnUp()
    {
        isFaceUp = true;
    }
    
    /**
     * Turns the card to face down.
     * @postcondition isFaceUp is set to false.
     */
    public void turnDown()
    {
        isFaceUp = false;
    }
    
    /**
     * Gets the file name of a card.
     * @postcondition the file name of a card is returned.
     * @return the file name of the card.
     */
    public String getFileName()
    {
        String firstPart = "cards\\";
        String lastPart = ".gif";
        if(!isFaceUp)
        {
            return firstPart + "back" + lastPart;
        }
        else
        {
            String middlePart = getSuit();
            if(rank==1)
            {
                middlePart = "a" + middlePart;
            }
            else if(rank==10)
            {
                middlePart = "t" + middlePart;
            }
            else if(rank==11)
            {
                middlePart = "j" + middlePart;
            }
            else if(rank==12)
            {
                middlePart = "q" + middlePart;
            }
            else if(rank==13)
            {
                middlePart = "k" + middlePart;
            }
            else
            {
                middlePart = getRank() + middlePart;
            }
            return firstPart + middlePart + lastPart;
        }
    }
}
