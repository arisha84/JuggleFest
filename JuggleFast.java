package juggleFast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;

import juggleFast.Circuit.AddResult;
import juggleFast.Circuit.AddResult.Result;
import juggleFast.Juggler.PrefernceEntry;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class JuggleFast {

	private List<Circuit> circuits = new ArrayList<Circuit>();
	private LinkedList<Juggler> unassignedJugglers = new LinkedList<Juggler>();
	private LinkedList<Juggler> weakJugglers = new LinkedList<Juggler>(); // This least will hold the jugglers that didn't get any of their
																		  // preferences. Not a good place to be :)
	private int circuitCapacity;
	
	
    /**
     * Constructor 
     * 
     * @param cirIn
     * @param juglist
     */
	public JuggleFast(List<Circuit> cirIn, LinkedList<Juggler> juglist) {
		circuits = cirIn;
		unassignedJugglers = juglist;
		circuitCapacity = juglist.size() / cirIn.size();
		for (Juggler j : unassignedJugglers) {
			j.buildPreferenceList(circuits);
		}

		for (Circuit c : circuits) {
			c.setCapacity(circuitCapacity);
		}

	}

	/**
	 * Main logic happens here. Assign Jugglers to Circuits according to their priorities
	 * @return
	 */
	public boolean AssignAllJugglers() {

		// First step - continue to extract Juggler from the list until its empty
		while (!unassignedJugglers.isEmpty()) {
			Juggler curJuggler = unassignedJugglers.peek();
			PrefernceEntry nextPreference = curJuggler
					.GetNextCircuitPrefrenceId();

			// If the nextPreference is null it means this is a weak juggler
			// that didn't get any preference. The weak jugglers will be added later to Random circuses.
			if (nextPreference == null) {
				weakJugglers.add(curJuggler);
				unassignedJugglers.remove();
				continue;
			}
			AddResult res = circuits.get(nextPreference.CircuitId).TryAdd(
					curJuggler, nextPreference.TotalSkill);

			switch (res.getRes()) {

			case ADDED:
				// if Added Successfully, remove from queue.
				unassignedJugglers.remove();
				break;

			case REPLACED:
				unassignedJugglers.remove();
				// If the juggler was replaced, we need to put the Juggler that
				// was kicked out back in the heap
				unassignedJugglers.add(res.j);
				break;

			case FAILED:
				// Nothing to do, next iteration of the loop will try the next
				// priority.
				break;

			}

		}

		// Second step: Randomly assign the weak Jugglers, who weren't assigned to any
		// of their preferences.
		int circuitindex = 0;
		boolean foundFreeSlot = false;
		while (!weakJugglers.isEmpty()) {
			while (!foundFreeSlot) {
				if (!circuits.get(circuitindex).isFull())
					foundFreeSlot = true;
				else
					circuitindex++;
			}
			Juggler j = weakJugglers.poll();
			Circuit c = circuits.get(circuitindex);
			c.TryAdd(j, j.CalcTotalSkillForCircuit(c));
			foundFreeSlot = false;
		}

		return true;
	}

	/**
	 * 
	 * @return String of Results in the desired format
	 */
	public String formattedResults() {

		StringBuilder sb = new StringBuilder();
		int index = 0;
		for (Circuit c : circuits) {
			sb.append(("\nC" + index));
			sb.append(c.printJugglers());
			index++;
		}

		return sb.toString();
	}

	@Override
	public String toString() {
		return "JuggleFast [circuits=\n" + circuits
				+ "\n, unassignedJugglers=\n" + unassignedJugglers + "]";
	}

}
