import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

class LogAnalyzer
{
public static void main(String args[]) throws Exception
{
String files[]={"log1.txt","log2.txt","log3.txt"};
String data[]={
"User logged in\nError found\nSystem started",
"Error in connection\nUser logged out",
"System running\nError occurred"
};

    for(int i=0;i<files.length;i++)
    {
        Files.write(Paths.get(files[i]),data[i].getBytes());
    }

    String key="Error";
    int total=0;
    int count=0;

    for(int i=0;i<files.length;i++)
    {
        Path p=Paths.get(files[i]);
        List<String> lines=Files.readAllLines(p);

        total=total+lines.size();

        for(String line:lines)
        {
            if(line.contains(key))
                count++;
        }

        BasicFileAttributes a=Files.readAttributes(p,BasicFileAttributes.class);

        System.out.println("File: "+files[i]);
        System.out.println("Size: "+a.size()+" bytes");
    }

    System.out.println("Total lines: "+total);
    System.out.println("Lines containing "+key+": "+count);
}

}
