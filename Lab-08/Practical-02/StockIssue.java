import java.util.*;

class OutOfStockException extends Exception
{
    int shortfall;

    OutOfStockException(int s)
    {
        super("Not enough stock");
        shortfall=s;
    }
}

class InvalidQuantityException extends Exception
{
    InvalidQuantityException(String s)
    {
        super(s);
    }
}

class Warehouse
{
    HashMap<String,Integer> stock=new HashMap<>();

    Warehouse()
    {
        stock.put("Pen",10);
        stock.put("Book",5);
        stock.put("Pencil",8);
    }

    void issue(String item,int qty) throws OutOfStockException,InvalidQuantityException
    {
        if(qty<=0)
            throw new InvalidQuantityException("Quantity must be positive");

        int have=stock.getOrDefault(item,0);

        if(qty>have)
            throw new OutOfStockException(qty-have);

        stock.put(item,have-qty);
        System.out.println("Issued "+qty+" "+item);
    }
}

class StockIssue
{
    public static void main(String[] args)
    {
        Warehouse w=new Warehouse();

        String[] items={"Pen","Book","Pencil","Pen","Book"};
        int[] qty={3,8,2,-1,2};

        for(int i=0;i<items.length;i++)
        {
            try
            {
                w.issue(items[i],qty[i]);
            }
            catch(OutOfStockException e)
            {
                System.out.println(items[i]+" short by "+e.shortfall);
            }
            catch(InvalidQuantityException e)
            {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("Stock issue completed");
    }
}