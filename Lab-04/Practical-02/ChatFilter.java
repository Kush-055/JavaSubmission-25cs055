import java.util.Scanner;

class ChatFilter
{
    public static String[] filter(String line)
    {
        String[] parts=line.split(" ",3);

        if(parts.length<3)
        {
            return null;
        }

        return parts;
    }
}

class Driver
{
    public static void main(String[] args)
    {
        String[] log={
            "10:05 alice Hello there",
            "11:23 meow How are you",
            "12:43 angel Good morning",
            "13:15 riddhi Hello everyone"
        };

        Scanner sc=new Scanner(System.in);

        System.out.print("Enter keyword: ");
        String keyword=sc.nextLine();

        int count=0;
        StringBuilder report=new StringBuilder();

        for(String line:log)
        {
            String[] parts=ChatFilter.filter(line);

            if(parts==null)
            {
                continue;
            }

            String time=parts[0];
            String user=parts[1];
            String message=parts[2];

            if(message.toLowerCase().contains(keyword.toLowerCase()))
            {
                count++;
                report.append(time+" "+user+": "+message);
            }
        }

        System.out.println("Matches: "+count);
        System.out.print(report);

        sc.close();
    }
}