package dart.blackcat.agent;

import java.io.Serializable;

public class Agent implements Serializable {
	
	private static final long serialVersionUID = -6612680786727096976L;
	private Behavior behavior;
	
	public Behavior getBehavior() {
		return behavior;
	}
	
	public void setBehavior(Behavior behavior) {
		this.behavior = behavior;
	}
}
