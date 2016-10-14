package io.tracee.contextlogger.agent.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


public class SimpleJsonParser {

	public static enum ValueType {


		TEXT('\'', '\''), ARRAY('[', ']'), OBJECT('{', '}');

		private final char openingChar;
		private final char closingChar;

		ValueType(char openingChar, char closingChar) {
			this.openingChar = openingChar;
			this.closingChar = closingChar;
		}

		public static ValueType getValueTypeForString(char firstChar) {
			for (ValueType valueType : values()) {
				if (valueType.openingChar == firstChar) {
					return valueType;
				}
			}

			return null;
		}

		public char getOpeningChar() {
			return openingChar;
		}

		public char getClosingChar() {
			return closingChar;
		}

	}

	@AllArgsConstructor
	@Getter
	public static class Value {
		private final ValueType valueType;

	}

	public static class Obj extends Value {

		List<KeyValuePair> values = new ArrayList<KeyValuePair>();

		public Obj() {
			super(ValueType.OBJECT);
		}

		public void addNameValuePair(Text key, Value value) {
			values.add(new KeyValuePair(key, value));
		}

		public int getSize() {
			return values.size();
		}

		public KeyValuePair getValue(int index) {
			return index < 0 || index > values.size() ? null : values.get(index);
		}

		public Value getValueByKey(String key) {

			if (key != null) {
				for (KeyValuePair kvpair : values) {
					if (key.equals(kvpair.getKey().getValue())) {
						return kvpair.getValue();
					}
				}
			}
			return null;
		}


	}

	@AllArgsConstructor
	@Getter
	public static class KeyValuePair {
		private final Text key;
		private final Value value;
	}


	public static class Array extends Value {


		public List<Value> values = new ArrayList<Value>();

		public Array() {
			super(ValueType.ARRAY);
		}

		public void addValue(Value valueToAdd) {
			values.add(valueToAdd);
		}

		public int getSize() {
			return values.size();
		}

		public Value getValue(int index) {
			return index < 0 || index > values.size() ? null : values.get(index);
		}


	}

	@Getter
	public static class Text extends Value {

		private final String value;

		public Text(String text) {
			super(ValueType.TEXT);
			this.value = text;
		}


	}


	protected static String getEnclosedElementsSubstring(String strToParse) {
		if (strToParse == null) {
			return null;
		}

		// check if it is empty
		String tmpString = strToParse.trim();
		if (tmpString.isEmpty()) {
			return null;
		}

		// check if it has a valid opening char
		ValueType valueType = ValueType.getValueTypeForString(tmpString.charAt(0));
		if (valueType == null) {
			return null;
		}

		int index = 0;
		boolean found = false;
		int level = 0;

		do {
			char currentChar = tmpString.charAt(index);


			if (valueType.getOpeningChar() == currentChar) {
				if (valueType == ValueType.TEXT && level == 1) {
					level--;
				} else {
					level++;
				}
			} else if (valueType.getClosingChar() == currentChar) {
				level--;
			}

			if (level == 0) {
				found = true;
			}

			//must be done last
			index++;
		} while (level > 0 && index < tmpString.length());

		return found ? tmpString.substring(0, index) : null;

	}


	protected static Text parseText(String value) {
		return new Text(value == null || value.length() <= 2 ? null : value.substring(1, value.length() - 1));
	}

	protected static Obj parseObject(String value) {

		if (value == null || value.length() <= 2) {
			return null;
		}

		Obj structure = new Obj();

		String restString = value.substring(1, value.length() - 1).trim();
		do {

			String currentElement = getEnclosedElementsSubstring(restString);

			if (currentElement != null) {
				Value currentElementKey = parse(currentElement);
				if (!(currentElementKey instanceof Text)) {
					return null;
				}

				restString = restString.substring(currentElement.length()).trim();

				if (restString.isEmpty() || !restString.startsWith(":")) {
					return null;
				}
				restString = restString.substring(1).trim();

				currentElement = getEnclosedElementsSubstring(restString);

				Value currentElementValue = parse(currentElement);
				structure.addNameValuePair((Text) currentElementKey, currentElementValue);

				restString = restString.substring(currentElement.length()).trim();


				if (restString.isEmpty()) {
					break;
				} else if (!restString.startsWith(",")) {
					return null;
				}
				restString = restString.substring(1).trim();

			} else {
				break;
			}


		} while (restString != null);

		return structure;

	}

	protected static Array parseArray(String value) {

		if (value == null || value.length() <= 2) {
			return null;
		}

		Array array = new Array();

		String restString = value.substring(1, value.length() - 1).trim();
		do {

			String currentElement = getEnclosedElementsSubstring(restString);

			if (currentElement != null) {
				array.addValue(parse(currentElement));

				restString = restString.substring(currentElement.length()).trim();

				if (restString.isEmpty()) {
					break;
				} else if (!restString.startsWith(",")) {
					return null;
				}
				restString = restString.substring(1).trim();

			} else {
				break;
			}


		} while (restString != null);


		return array;
	}


	public static Value parse(String json) {

		if (json == null) {
			return null;
		}

		String trimmedJson = json.trim();
		if (trimmedJson.isEmpty()) {
			return null;
		}

		ValueType valueType = ValueType.getValueTypeForString(trimmedJson.charAt(0));

		switch (valueType) {
			case TEXT:
				return parseText(trimmedJson);
			case ARRAY:
				return parseArray(trimmedJson);
			case OBJECT:
				return parseObject(trimmedJson);
		}

		return null;
	}


}
