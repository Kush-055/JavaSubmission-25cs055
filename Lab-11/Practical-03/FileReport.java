import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.io.*;

class FileReport
{
public static void main(String args[]) throws Exception
{
Path p=Paths.get("sampleFolder");
Files.createDirectories(p.resolve("subfolder"));

    Files.write(p.resolve("a.txt"),"Hello World".getBytes());
    Files.write(p.resolve("b.txt"),"Java Program".getBytes());
    Files.write(p.resolve("subfolder/c.txt"),"NIO Example".getBytes());

    BufferedWriter w=new BufferedWriter(new FileWriter("report.txt"));

    Files.walkFileTree(p,new SimpleFileVisitor<Path>()
    {
        public FileVisitResult visitFile(Path f,BasicFileAttributes a) throws IOException
        {
            String s="Name: "+f.getFileName()+
                     ", Size: "+a.size()+
                     " bytes, Modified: "+a.lastModifiedTime();

            System.out.println(s);
            w.write(s);
            w.newLine();

            return FileVisitResult.CONTINUE;
        }
    });

    w.close();

    System.out.println("Report saved");
}

}
