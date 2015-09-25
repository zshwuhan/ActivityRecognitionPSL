package PSLHAR;//edu.umd.cs.example;

import java.util.ArrayList;

import edu.umd.cs.psl.database.ReadOnlyDatabase;
import edu.umd.cs.psl.model.argument.ArgumentType;
import edu.umd.cs.psl.model.argument.GroundTerm;
import edu.umd.cs.psl.model.argument.IntegerAttribute;
import edu.umd.cs.psl.model.function.ExternalFunction;

class FollowsForSameObjectTypeFunction implements ExternalFunction {

	private ArgumentType[] argTypes = new ArgumentType[]{ArgumentType.UniqueID, ArgumentType.UniqueID, ArgumentType.UniqueID, ArgumentType.UniqueID, ArgumentType.UniqueID, ArgumentType.UniqueID};
	private static final ArrayList<Integer> values = new ArrayList<Integer>();
	private static final ArrayList<Integer> timestamps = new ArrayList<Integer>();
	private int maxIncrement;
	
	public FollowsForSameObjectTypeFunction(int maxIncrement) {
		this.maxIncrement = maxIncrement;		
	}
	
	@Override
	public double getValue(ReadOnlyDatabase db, GroundTerm... args) {
		boolean sameType = true; //		argTypes = new ArgumentType[args.length];
		/* Get args */
		try{
			values.clear();
			timestamps.clear();
			for (int j = 0; j < args.length/2; j++) { // FIRST HALF IS TIMESTAMPS
				String value = args[j].toString();
				if (! value.equals("")){
					timestamps.add(Integer.parseInt(value));//((IntegerAttribute) args[j]).getValue().intValue());
				}				
			}
			for (int j = args.length/2; j < args.length; j++) { // SECOND HALF IS OBJECT TYPES
				String value = args[j].toString();
				if (! value.equals("")){
					values.add(Integer.parseInt(value));//((IntegerAttribute) args[j]).getValue().intValue());
				}				
			}
			if(values.size()<2 || timestamps.size()<2){
				System.err.println("Error in FollowsForSameObjectTypeFunction function: missing extra input variable ");
				System.exit(-1);
			}
			else{
				int i =1;			
				while (i<values.size()){
					if((values.get(i)) != (values.get(i-1)))
						return 0.0;  // False, values are not equal
					i++;
				}	
				i =1;
				while (i<timestamps.size()){
					if((timestamps.get(i)) != timestamps.get(i-1)+this.maxIncrement) // False, timestamps are not consecutive!
						return 0.0;
					i++;
				}
				//System.out.println(sameType);
			}
		}
		catch(Exception e){//Catch exception if any
            System.err.println("Error in SameObjectTypeFunction3: " + e.getMessage());
        }
		//System.out.println("Precedes evaluates to: "+follows);
		return sameType ? 1.0 : 0.0;  // 0 is False; 1 is True 
	}

	@Override
	public int getArity() {
		return argTypes.length;//argumentTypes.size();
	}

	@Override
	public ArgumentType[] getArgumentTypes() {
		return argTypes;// (ArgumentType[]) argumentTypes.toArray();
	}

}