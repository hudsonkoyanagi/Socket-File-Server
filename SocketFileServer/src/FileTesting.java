import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileTesting {

    static final String MAIN_DIR = "/Users/hudsonkoyanagi";

    public static void main(String[] args) throws IOException {
        Node node = new Node(MAIN_DIR);
        for(Node n : node.getNodes()){
            System.out.println(n.getFilePath());
        }

    }
}
