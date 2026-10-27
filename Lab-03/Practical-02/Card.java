//Practical - 03  /practice program - 2
import java.util.Scanner;
class Card
{
    private String rank;
    private String suit;
   public Card(String rank , String suit)
    {
        this.rank = rank;
        this.suit = suit;

    }
    @Override
    public String toString()
    {
        return rank +" of " + suit ;
    }
    @Override
    public boolean equals(Object o)
    {
        
        if(!(o instanceof Card))
        {
            return false;
        }
        Card c = (Card)o;
        return this.rank.equals(c.rank) && this.suit.equals(c.suit);
    }
    

}
class Driver
{
    public static void main(String[] args)
    {
        int n = 0;
        Scanner sc = new Scanner(System.in);
        System.out.println("How many cards do you want to enter?");
        n = sc.nextInt();
        Card[] card = new Card[n];
        for(int i = 0 ; i < card.length ; i++ )
        {
            System.out.println("Enter the rank and suit:");
            card[i] = new Card(sc.next(),sc.next());
                for (int j = 0; j < i; j++)
                {
                if (card[i].equals(card[j]))
                {
                    System.out.println("duplicate found: " + card[i]);
                    break;
                }
                }
            }
            sc.close();
        }
        


}
