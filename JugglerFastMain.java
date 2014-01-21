package juggleFast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * Juggle Fast puzzle solution. 
 * See instructions here - http://www.yodlecareers.com/puzzles/jugglefest.html
 * 
 * @author AriSharon
 *
 */
public class JugglerFastMain {
	public static final String InputFilePath = "/Users/AriSharon/InterviewPrep/InterviewPrep/src/juggleFast/jugglefest.txt";
	public static final String OutputFilePath = "/Users/AriSharon/InterviewPrep/InterviewPrep/src/juggleFast/output.txt";
	
	
	public static void main(String[] args) {

		JuggleFast juggleFast = extactJuggle(InputFilePath);
		if (juggleFast == null) {
			System.out.println("failed to read file");
			return;
		}

		boolean res = juggleFast.AssignAllJugglers();
		if (res) {
			try {
				writeToOutputFile(juggleFast.formattedResults(), OutputFilePath);
				System.out.println("Jugglers assigned successfully!\n"
									+ "see output at: " + OutputFilePath);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else {
			System.out.println("Failed to assign all Juglers in juggleFast");
		}

	}
	
	/**
	 * Read Jugglers and Circuit from File
	 * @param path
	 * @return
	 */
	public static JuggleFast extactJuggle(String path) {
		try {

			BufferedReader br = new BufferedReader(new FileReader(path));
			String line;
			int JugglerNum = 0;
			
			List<Circuit> circuitsIn = new ArrayList<Circuit>();
			LinkedList<Juggler> unassignedJugglersIn = new LinkedList<Juggler>();

			while ((line = br.readLine()) != null) {

				String[] words = line.split(" ");
				if (words.length == 0)
					continue;
				else {
					if (words[0].equals("C")) {
						int H = Integer.decode(words[2].replaceAll("\\D+", ""));
						int E = Integer.decode(words[3].replaceAll("\\D+", ""));
						int P = Integer.decode(words[4].replaceAll("\\D+", ""));
						circuitsIn.add(new Circuit(H, E, P));

					}

					if (words[0].equals("J")) {
						int H = Integer.decode(words[2].replaceAll("\\D+", ""));
						int E = Integer.decode(words[3].replaceAll("\\D+", ""));
						int P = Integer.decode(words[4].replaceAll("\\D+", ""));

						String[] circuitsString = words[5].split(",");
						List<Integer> circuitIds = new LinkedList<Integer>();

						for (int i = 0; i < circuitsString.length; i++) {
							int circuitId = Integer.decode(circuitsString[i]
									.replaceAll("\\D+", ""));
							circuitIds.add(circuitId);

						}

						Juggler jug = new Juggler(JugglerNum++, H, E, P,
								circuitIds);
						unassignedJugglersIn.add(jug);

					}
				}

			}
			br.close();

			JuggleFast juggleFast = new JuggleFast(circuitsIn,
					unassignedJugglersIn);
			System.out.println("number of Circuits: " + circuitsIn.size());
			System.out.println("number of jugglers: " + unassignedJugglersIn.size());
			
			return juggleFast;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void writeToOutputFile(String output, String filePath) {
		try {
			OutputStreamWriter writer = new OutputStreamWriter(
					new FileOutputStream(filePath));
			writer.write(output);
			writer.flush();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
