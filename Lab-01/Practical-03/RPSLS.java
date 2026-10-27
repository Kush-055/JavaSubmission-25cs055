// Lab-01/Peactical - 1/Practice program - 3
//Rock-Paper-Scissors-Lizard-Spock 
import java.util.Random;
import java.util.Scanner;
class RPSLS
{
    enum Move{ROCK, PAPER, SCISSORS, LIZARD, SPOCK}
    static  int winner(Move a, Move b)
    {
        if(a == b)
        {
            return 0;
        }
        return switch(a)
        {
            case ROCK ->
            {
                if(b == Move.LIZARD || b == Move.SCISSORS )
                {
                    yield 1;
                }
                else
                    
                {

                    yield -1;

                }
                    
            }
            case PAPER -> 
            {
                if(b == Move.ROCK || b == Move.SPOCK)
                {
                    yield 1;
                }
                else
                {
                    yield -1;
                }
            }
            case SCISSORS ->
            {
                if(b == Move.PAPER || b == Move.LIZARD)
                {
                    yield 1;
                }
                else
                {
                    yield -1;
                }

            }
            case LIZARD ->
            {
                if(b == Move.PAPER || b == Move.SPOCK)
                {
                    yield 1;
                }
                else
                {
                    yield -1;
                }

            }
            case SPOCK->
            {
                if(b == Move.ROCK || b == Move.SCISSORS)
                {
                    yield 1;
                }
                else
                {
                    yield -1;
                }

            }



        };
    }
        public static void main(String[] args)
        {

            Scanner sc = new Scanner(System.in);
            Random r = new Random();
            Move[] moves= Move.values();
            int computer = 0;
            int player1 = 0;
            for(int i = 0 ; i < 5 ; i++ )
            {
                Move p1;
                System.out.println("Round:"+(i+1));
                while(true)
                {
                    try
                    {
                        System.out.println("Enter Your Move(Player 1):");
                        p1 = Move.valueOf(sc.next().toUpperCase());
                        break;

                    }
                    catch(Exception e)
                    {
                        System.out.println("Invalid Move !");
                    }
                }
                
                Move c = moves[r.nextInt(moves.length)];
                System.out.println("Computer's move:"+ c);
                System.out.println("Your move:"+ p1);
                
                 int result = winner(p1 , c);
                 if(result == 0)
                 {
                    System.out.println("Tie");
                 }
                 else if(result == 1)
                 {
                    System.out.println("You win the round " + (i+1) + "!");
                    player1++;
                 }
                  else if(result == -1)
                 {
                    System.out.println("Computer wins round" + (i+1) + "!");
                    computer++;
                 }
                 





            }
                 System.out.println("Player 1's Score:" + player1);
                 System.out.println("computer's Score:" + computer);
                 if(player1 > computer)
                 {
                    System.out.println("You Win " + player1 + "-" + computer);
                 }
                 else if(computer > player1)
                 {
                    System.out.println("computer Wins " + computer + "-" + player1);
                 }
                 else
                 {
                    System.out.println("Match is Draw!");


                 }
                 sc.close();

            
        }

 } 



    

