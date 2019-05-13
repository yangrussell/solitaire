import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/**
 * The Solitaire class defines a game of Solitaire using playing cards.
 * @author Russell Yang
 * @version 11/9/17
 */
public class Solitaire
{
    /**
     * Main method; creates a new Solitaire object.
     * @param args an argument
     * @postcondition a Solitaire object is created.
     */
    public static void main(String[] args)
    {
        new Solitaire();
    }
    /*
     * Instance variables
     */
    private Stack<Card> stock; //stores the stock Stack of Cards
    private Stack<Card> waste; //stores the waste Stack of Cards
    private Stack<Card>[] foundations; //stores the foundations array of Stacks of Cards
    private Stack<Card>[] piles; //stores the piles array of Stacks of Cards
    private SolitaireDisplay display; //stores the display mechanism

    /**
     * Default constructor; initializes instance variables.
     */
    public Solitaire()
    {
        stock = new Stack<Card>();
        waste = new Stack<Card>();
        foundations = new Stack[4];
        for(int i = 0; i<4; i++)
        {
            foundations[i] = new Stack<Card>();
        }
        piles = new Stack[7];
        for(int i = 0; i<7; i++)
        {
            piles[i] = new Stack<Card>();
        }
        display = new SolitaireDisplay(this);
        createStock();
        deal();
    }

    /**
     * Gets the card on top of the stack or null if there
     * isn't one.
     * @return the Card on top of the stack if there is one,
     *         null otherwise.
     */
    public Card getStockCard()
    {
        if(stock.isEmpty())
        {
            return null;
        }
        else
        {
            return stock.peek();
        }
    }

    /**
     * Gets the card on top of the waste or null if there
     * isn't one.
     * @return the Card on top of the waste if there is one,
     *         null otherwise.
     */
    public Card getWasteCard()
    {
        if(waste.isEmpty())
        {
            return null;
        }
        else
        {
            return waste.peek();
        }
    }

    /**
     * Gets the card on top of the foundation or null if there
     * isn't one.
     * @param index an index into the foundation array
     * @precondition index is valid (between 0 and 3 inclusive).
     * @return the Card on top of the foundation if there is one,
     *         null otherwise.
     */
    public Card getFoundationCard(int index)
    {
        Stack<Card> temp = foundations[index];
        if(temp.isEmpty())
        {
            return null;
        }
        else
        {
            return temp.peek();
        }
    }

    /**
     * Gets the pile at the given index.
     * @param index an index into the piles array
     * @precondition index is valid (between 0 and 6 inclusive).
     * @return the pile at the given index.
     */
    public Stack<Card> getPile(int index)
    {
        return piles[index];
    }
    
    /**
     * Creates a stock of cards.
     * @postcondition a stock of cards is created.
     */
    private void createStock()
    {
        ArrayList<Card> temp = new ArrayList<Card>(52);
        String suit;
        for(int rankIndex = 1; rankIndex<=13; rankIndex++)
        {
            for(int suitIndex = 1; suitIndex<=4; suitIndex++)
            {
                if(suitIndex==1)
                {
                    suit = "c";
                }
                else if(suitIndex==2)
                {
                    suit = "d";
                }
                else if(suitIndex==3)
                {
                    suit = "h";
                }
                else
                {
                    suit = "s";
                }
                temp.add(new Card(rankIndex, suit));
            }
        }
        int high = 52;
        while(temp.size()>0)
        {
            stock.add(temp.remove((int)(Math.random() * high)));
            high--;
        }
    }
    
    /**
     * Deals the cards onto the display.
     * @postcondition all 52 cards have been dealt onto the display.
     */
    private void deal()
    {
        for(int i = 0; i<7; i++)
        {
            for(int j = 0; j<=i; j++)
            {
                if(!stock.isEmpty())
                {
                    Card c = stock.remove(0);
                    if(j==i)
                    {
                        c.turnUp();
                    }
                    piles[i].add(c);
                }
            }
        }
    }

    /**
     * Deals three cards onto the waste.
     * @postcondition three cards have been dealt.
     */
    private void dealThreeCards()
    {
        for(int i = 0; i<3; i++)
        {
            if(!stock.isEmpty())
            {
                Card c = stock.pop();
                c.turnUp();
                waste.push(c);
            }
        }
    }
    
    /**
     * Resets the stock of cards.
     * @postcondition the cards from the waste have been
     *                popped back onto the stock.
     */
    private void resetStock()
    {
        while(!waste.isEmpty())
        {
            Card c = waste.pop();
            c.turnDown();
            stock.push(c);
        }
    }
    
    /**
     * Called when the stock is clicked, performs one of various
     * actions depending on state of game.
     * @postcondition nothing happens if the waste is selcted or a pile
     *                is selected, three cards are dealt if the stock isn't empty,
     *                otherwise the stock is reset.
     */
    public void stockClicked()
    {
        if(display.isWasteSelected() || display.isPileSelected())
        {
            return;
        }
        else if(!stock.isEmpty())
        {
            dealThreeCards();
        }
        else
        {
            resetStock();
        }
    }

    /**
     * Called when the waste is clicked, performs one of various
     * actions depending on state of game.
     * @postcondition the waste is selected if the waste isn't empty and the waste
     *                isn't selected and a pile isn't selected, otherwise if the waste
     *                is selected it is unselected.
     */
    public void wasteClicked()
    {
        if(!waste.isEmpty() && !display.isWasteSelected() && !display.isPileSelected())
        {
            display.selectWaste();
        }
        else if(display.isWasteSelected())
        {
            display.unselect();
        }
    }

    /**
     * Called when the foundation is clicked, performs one of various 
     * actions depending on state of game.
     * @param index an index into the foundations array
     * @precondition the index is valid (between 0 and 6 inclusive)
     * @postcondition a card popped from the waste is pushed to the foundations
     *                at the given index if the waste is selected and the card on top
     *                of the waste can be added at the given index, otherwise if
     *                a pile is selected and the top card of the selected pile is able
     *                to be added to the foundation at the given index, the pile at the
     *                selectedPile index is popped and that card is pushed to 
     *                foundations at the given index.
     */
    public void foundationClicked(int index)
    {
        if(display.isWasteSelected() && canAddToFoundation(waste.peek(), index))
        {
            foundations[index].push(waste.pop());
            display.unselect();
        }
        else if(display.isPileSelected()
                && canAddToFoundation(piles[display.selectedPile()].peek(), index))
        {
            foundations[index].push(piles[display.selectedPile()].pop());
            display.unselect();
        }
        
    }
    
    /**
     * Tells whether the game has been won or not - used to
     * implement the win animation.
     * @postcondition true is returned if the game has been won,
     *                false otherwise.
     * @return true if the game has been won, false otherwise.
     */
    public boolean celebrateTime()
    {
        for(int i = 0; i<4; i++)
        {
            Stack<Card> temp = foundations[i];
            if(temp.isEmpty())
            {
                return false;
            }
            else if(temp.peek().getRank()!=13)
            {
                return false;
            }
        }
        return true;
    }
  
    /**
     * Called when the pile is clicked, performs one of various 
     * actions depending on state of game.
     * @param index an index into the piles array
     * @precondition the index is valid (between 0 and 6 inclusive)
     * @postcondition if a pile is selected and the given index doesnt match
     *                the index of that pile, then the face up cards on the selected pile
     *                are extracted to a stack. If the top card on the stack can be added 
     *                safely to the pile at the given index, then it is added and the 
     *                old pile is unselected. Otherwise, if the waste is seleted, the top 
     *                card of the waste is extracted into a card. If the card can be 
     *                safely added to the pile with the given
     *                index, then it is added and the old pile 
     *                is unselected. Otherwise if the waste
     *                isn't selected and a pile isn't selected 
     *                and the pile at the given index is not empty
     *                and the card at the top of the pile with 
     *                the given index is face up, then the pile at the
     *                given index is selcted. Otherwise if the selectedPile 
     *                matches the given index, it is unselected.
     *                Otherwise, if the waste isn't selected and a pile 
     *                isn't selected and the pile at the
     *                given index is not empty and the top card of the 
     *                pile at the given index is face down,
     *                the top card of the pile with the given index is turned up.
     */
    public void pileClicked(int index)
    {
        if(display.isPileSelected() && index!=display.selectedPile())
        {
            Stack<Card> s = removeFaceUpCards(display.selectedPile());
            if(canAddToPile(s.peek(), index))
            {
                addToPile(s, index);
                display.unselect();
            }
            else
            {
                addToPile(s, display.selectedPile());
            }
        }
        else if(display.isWasteSelected())
        {
            Card c = waste.peek();
            if(canAddToPile(c, index))
            {
                piles[index].push(waste.pop());
                display.unselect();
            }
        }
        else if(!display.isWasteSelected() && !display.isPileSelected() && 
                !piles[index].isEmpty() && piles[index].peek().isFaceUp())
        {   
            display.selectPile(index);
        }    
        else if(display.selectedPile()==index)
        {
            display.unselect();
        }
        else if(!display.isWasteSelected()
                && !display.isPileSelected() && !piles[index].isEmpty() 
                && !piles[index].peek().isFaceUp())
        {
            piles[index].peek().turnUp();
        }
    }
    
    /**
     * Checks whether the given card can be added to the pile at the given index.
     * @param card a Card
     * @param index an index into the piles array
     * @postcondition true is returned if the card can be added (if the pile is empty,
     *                only a king can be added, otherwise, the suit must be opposite and the 
     *                given card must have a rank one less than the card on the pile), 
     *                false otherwise.
     * @return true if the card can be added, false otherwise.
     */
    private boolean canAddToPile(Card card, int index)
    {
        if(piles[index].isEmpty())
        {
            if(card.getRank()==13)
            {
                return true;
            }
            return false;
        }
        Card pileCard = piles[index].peek();
        if(pileCard.isFaceUp())
        {
            String cardSuit = card.getSuit();
            String pileCardSuit = pileCard.getSuit();
            if(card.getRank()+1==pileCard.getRank() && 
                 ((cardSuit.equals("h") || cardSuit.equals("d")) &&
                  (pileCardSuit.equals("c") || pileCardSuit.equals("s")) ||
                  (cardSuit.equals("c") || cardSuit.equals("s")) &&
                  (pileCardSuit.equals("h") || pileCardSuit.equals("d"))))
            {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Removes the face up cards at the pile with the given index.
     * @param index an index into the piles array.
     * @postcondition the face up cards at the pile with the given
     *                index are returned as a stack of cards.
     */
    private Stack<Card> removeFaceUpCards(int index)
    {
        Stack<Card> s = new Stack<Card>();
        while(!piles[index].isEmpty() && piles[index].peek().isFaceUp())
        {
            s.push(piles[index].pop());
        }
        return s;
    }
    
    /**
     * Checks if the stack of Cards can be added to the pile with 
     * the given index.
     * @param cards a Stack of Cards
     * @param index an index into the piles array
     * @postcondition true is returned if the stack of Cards
     *                can be added to the pile with the given
     *                index, false otherwise.
     */
    private void addToPile(Stack<Card> cards, int index)
    {
        while(!cards.isEmpty())
        {
            piles[index].push(cards.pop());
        }
    }
    
    /**
     * Checks if the given card can be added to the foundation at the
     * given index.
     * @param card a Card
     * @param index an index into the foundations array.
     * @postcondition returns true if the card can be added to the foundation
     *                with the given index(can be added if the foundation is empty
     *                and the card's rank is 1 or if the foundation's top card has a
     *                rank one lower than the given cards rank and they are the same suit.
     */
    private boolean canAddToFoundation(Card card, int index)
    {
        if(foundations[index].isEmpty())
        {
            if(card.getRank()==1)
            {
                return true;
            }
            return false;
        }
        else
        {
            return (getFoundationCard(index).getRank()==card.getRank()-1 
                    && getFoundationCard(index).getSuit().equals(card.getSuit()));
        }
    }
}