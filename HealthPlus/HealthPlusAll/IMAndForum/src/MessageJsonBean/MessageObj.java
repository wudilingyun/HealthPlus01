package MessageJsonBean;

import java.io.Serializable;

public class MessageObj implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8264004883100910270L;

	public String time;
	public String from;
	public String text;
	public String id;
	public int unread;
	
	public MessageObj() {
		
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof MessageObj)) {
			return false;
		}
		if (((MessageObj) o).id.equals(this.id)) {
			return true;
		}

		return false;
	}

	@Override
	public String toString() {
		return "MessageObj [time=" + time + ", from=" + from + ", text=" + text + ", id=" + id + ", unread=" + unread + "]";
	}
}
