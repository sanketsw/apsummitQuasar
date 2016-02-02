package au.com.ibm.webapp.web.forms;

public enum Mode {

	EDIT("Edit"), NEW("New");

	private final String value;

	Mode(String v) {
		value = v;
	}

	public String value() {
		return value;
	}

	public static Mode fromValue(String v) {
		for (Mode c : Mode.values()) {
			if (c.value.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}
}
