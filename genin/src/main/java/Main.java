import java.io.IOException;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class Main {

	public static void main(String[] args) {
		try {
			AntlrParser parser = new AntlrParser(
					new CommonTokenStream(
						new AntlrLexer(
							new ANTLRFileStream(args[0]))));

			parser.program();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
