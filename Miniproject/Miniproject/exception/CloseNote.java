package exception;
 public class CloseNote implements AutoCloseable {
    @Override
    public void close() {
        System.out.println("closed resource");
    }
}
