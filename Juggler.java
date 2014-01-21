package juggleFast;

import java.util.ArrayList;
import java.util.List;

/**
 * Representation of a Juggler
 * @author AriSharon
 *
 */
public class Juggler {
	private int id;
	private int H;
	private int E;
	private int P;
	private List<Integer> circuitPrefrencesIds;
	private List<PrefernceEntry> preferenceList = new ArrayList<Juggler.PrefernceEntry>();
	private int currentPreference;
	
	/**
	 * Constructor
	 * 
	 * 
	 */
	public Juggler(int id, int h, int e, int p, List<Integer> cirList )
	{
		this.id = id;
		H = h;
		E = e;
		P = p;
		currentPreference = 0;
		circuitPrefrencesIds = cirList;
	}
	
	/**
	 * 
	 * @return If there are preferences left for this Juggler, return PrefernceEntry
	 * 		   else return null 
	 */
	public PrefernceEntry GetNextCircuitPrefrenceId ()
	{
		
		if (currentPreference < preferenceList.size())
		{
			return  preferenceList.get(currentPreference++);
		}
		
		
		return null;
	}
	
	/**
	 * Receives the list of all circuits and builds the Preferences list according 
	 * to the circuitPrefrencesIds of the Juggler.  
	 * 
	 * @param circuits
	 */
	public void buildPreferenceList (List<Circuit> circuits)
	{
		
		for (int circuitId : circuitPrefrencesIds)
		{
			Circuit c = circuits.get(circuitId);
			preferenceList.add(new PrefernceEntry(circuitId, CalcTotalSkillForCircuit(c)));
		}
	}
	
	
	public int CalcTotalSkillForCircuit (Circuit c)
	{
		return c.getE()*this.E + c.getH()*this.H + c.getP()*this.P; 
	}
	
	@Override
	public String toString() {
		return "Juggler [id=" + id + ", H=" + H + ", E=" + E + ", P=" + P
				+ ", preferenceList=" + preferenceList + ", currentPreference="
				+ currentPreference + "]";
	}
	
	/**
	 * 
	 * @return Juggler String in the desired Format
	 */
	public String printJuggler()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(" J"+ id + " ");
		for (PrefernceEntry entry : preferenceList)
		{
			sb.append("C"+ entry.CircuitId +":"+ entry.TotalSkill + " ");
		}
		sb.replace(sb.length() -1, sb.length(), "");
		
		return sb.toString();
	}

	/**
	 * Representation of PreferenceEntry, contains CircuitId and TotalSkill
	 * @author AriSharon
	 *
	 */
	public static class PrefernceEntry
	{
		int CircuitId;
		int TotalSkill;
		public PrefernceEntry(int circuitId, int totalSkill) {
			super();
			CircuitId = circuitId;
			TotalSkill = totalSkill;
		}
		@Override
		public String toString() {
			return "PrefernceEntry [CircuitId=" + CircuitId + ", TotalSkill="
					+ TotalSkill + "]";
		}
		public int getCircuitId() {
			return CircuitId;
		}
		public int getTotalSkill() {
			return TotalSkill;
		}
		
	    
	}
}
