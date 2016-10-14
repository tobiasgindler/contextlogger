package io.tracee.contextlogger.agent.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;


public class SimpleJsonParserTest {


	@Test
	public void shouldReturnNullForNull() {

		MatcherAssert.assertThat(SimpleJsonParser.getEnclosedElementsSubstring(null), Matchers.nullValue());
	}

	@Test
	public void shouldReturnNullForStringThatsStartingWithoutOpeningChar() {

		MatcherAssert.assertThat(SimpleJsonParser.getEnclosedElementsSubstring("a'abc'"), Matchers.nullValue());
	}

	@Test
	public void shouldFindTextSubstringWithLeadingSpaces() {

		MatcherAssert.assertThat(SimpleJsonParser.getEnclosedElementsSubstring("  'abc'def"), Matchers.is("'abc'"));
		MatcherAssert.assertThat(SimpleJsonParser.getEnclosedElementsSubstring("  [abc]def"), Matchers.is("[abc]"));
		MatcherAssert.assertThat(SimpleJsonParser.getEnclosedElementsSubstring("  {abc}def"), Matchers.is("{abc}"));

	}


	@Test
	public void shouldFindTextSubstring() {

		MatcherAssert.assertThat(SimpleJsonParser.getEnclosedElementsSubstring("'abc'def"), Matchers.is("'abc'"));
		MatcherAssert.assertThat(SimpleJsonParser.getEnclosedElementsSubstring("'abc'def'ghi"), Matchers.is("'abc'"));
		MatcherAssert.assertThat(SimpleJsonParser.getEnclosedElementsSubstring("'abc"), Matchers.nullValue());
	}

	@Test
	public void shouldFindArraySubstring() {

		MatcherAssert.assertThat(SimpleJsonParser.getEnclosedElementsSubstring("[abc]def"), Matchers.is("[abc]"));
		MatcherAssert.assertThat(SimpleJsonParser.getEnclosedElementsSubstring("[[abc][def]]ghi"), Matchers.is("[[abc][def]]"));
		MatcherAssert.assertThat(SimpleJsonParser.getEnclosedElementsSubstring("[[abc][def]][ghi]"), Matchers.is("[[abc][def]]"));
		MatcherAssert.assertThat(SimpleJsonParser.getEnclosedElementsSubstring("[[abc][def]"), Matchers.nullValue());


	}

	@Test
	public void shouldFindStructureSubstring() {

		MatcherAssert.assertThat(SimpleJsonParser.getEnclosedElementsSubstring("{abc}def"), Matchers.is("{abc}"));
		MatcherAssert.assertThat(SimpleJsonParser.getEnclosedElementsSubstring("{{abc}{def}}ghi"), Matchers.is("{{abc}{def}}"));
		MatcherAssert.assertThat(SimpleJsonParser.getEnclosedElementsSubstring("{{abc}{def}}{ghi}"), Matchers.is("{{abc}{def}}"));
		MatcherAssert.assertThat(SimpleJsonParser.getEnclosedElementsSubstring("{{abc}{def}"), Matchers.nullValue());

	}

	@Test
	public void shouldParseTextCorrectly() {
		MatcherAssert.assertThat(((SimpleJsonParser.Text) SimpleJsonParser.parse("'abc'")).getValue(), Matchers.is("abc"));
	}

	@Test
	public void shouldParseArraysCorrectly() {
		checkMatchingArray((SimpleJsonParser.Array) SimpleJsonParser.parse("['abc']"), "abc");
		checkMatchingArray((SimpleJsonParser.Array) SimpleJsonParser.parse("['abc','def','ghi']"), "abc", "def", "ghi");
		checkMatchingArray((SimpleJsonParser.Array) SimpleJsonParser.parse("['abc',   'def' \n   ,  'ghi'  ]"), "abc", "def", "ghi");
	}


	@Test
	public void shouldParseObjectCorrectly() {

		checkMatchingObject((SimpleJsonParser.Obj) SimpleJsonParser.parse("{'abc':'def'}"), new KVPair("abc", "def"));
		checkMatchingObject((SimpleJsonParser.Obj) SimpleJsonParser.parse("{'abc':'def','ghi':'jkl','mno':'pqr'}"), new KVPair("abc", "def"), new KVPair("ghi", "jkl"), new KVPair("mno", "pqr"));
		checkMatchingObject((SimpleJsonParser.Obj) SimpleJsonParser.parse("{'abc':'def'   ,    'ghi'  :  \n 'jkl','mno':\n'pqr'  }   "), new KVPair("abc", "def"), new KVPair("ghi", "jkl"), new KVPair("mno", "pqr"));

	}

	@Test
	public void shouldParsedMixedObjectCorrectly2() {

		String json = "  {\n" +
				"\t'ignoredPackages':[\n" +
				"\t\t'abc',\n" +
				"\t\t'def'\n" +
				"\t],\n" +
				"\t'xxx':'yyy'\n" +
				"}  ";

		SimpleJsonParser.Obj result = (SimpleJsonParser.Obj) SimpleJsonParser.parse(json);

		MatcherAssert.assertThat(((SimpleJsonParser.Text) result.getValueByKey("xxx")).getValue(), Matchers.is("yyy"));

		SimpleJsonParser.Array array = (SimpleJsonParser.Array) result.getValueByKey("ignoredPackages");
		checkMatchingArray(array, "abc", "def");


	}

	@Test
	public void shouldParsedMixedObjectCorrectly() {

		String json = "{'abc' : ['def', 'ghi', 'jkl'],'xxx':'yyy'}";

		SimpleJsonParser.Obj result = (SimpleJsonParser.Obj) SimpleJsonParser.parse(json);

		MatcherAssert.assertThat(((SimpleJsonParser.Text) result.getValueByKey("xxx")).getValue(), Matchers.is("yyy"));

		SimpleJsonParser.Array array = (SimpleJsonParser.Array) result.getValueByKey("abc");
		checkMatchingArray(array, "def", "ghi", "jkl");


	}

	@Test
	public void shouldBeAbleToGetKeyValuePaitFromObjectByKey() {

		SimpleJsonParser.Obj object = new SimpleJsonParser.Obj();
		object.addNameValuePair(new SimpleJsonParser.Text("abc"), new SimpleJsonParser.Text("def"));
		object.addNameValuePair(new SimpleJsonParser.Text("hij"), new SimpleJsonParser.Text("klm"));
		object.addNameValuePair(new SimpleJsonParser.Text("nop"), new SimpleJsonParser.Text("qrs"));

		MatcherAssert.assertThat(((SimpleJsonParser.Text) object.getValueByKey("hij")).getValue(), Matchers.is("klm"));
		MatcherAssert.assertThat(((SimpleJsonParser.Text) object.getValueByKey("abc")).getValue(), Matchers.is("def"));
		MatcherAssert.assertThat(((SimpleJsonParser.Text) object.getValueByKey("nop")).getValue(), Matchers.is("qrs"));


	}


	private void checkMatchingArray(SimpleJsonParser.Array array, String... exepectedElements) {

		MatcherAssert.assertThat(array.getSize(), Matchers.is(exepectedElements.length));

		for (int i = 0; i < array.getSize(); i++) {
			MatcherAssert.assertThat(((SimpleJsonParser.Text) array.getValue(i)).getValue(), Matchers.is(exepectedElements[i]));
		}

	}


	@Getter
	@AllArgsConstructor
	public static class KVPair {
		public final String key;
		public final String value;
	}

	private void checkMatchingObject(SimpleJsonParser.Obj obj, KVPair... kvpairs) {

		MatcherAssert.assertThat(obj, Matchers.notNullValue());
		MatcherAssert.assertThat(obj.getSize(), Matchers.is(kvpairs.length));


		for (int i = 0; i < obj.getSize(); i++) {
			SimpleJsonParser.KeyValuePair kvpair = obj.getValue(i);
			MatcherAssert.assertThat(String.format("[%d] : Key doesn't match", i), kvpair.getKey().getValue(), Matchers.is(kvpairs[i].getKey()));
			MatcherAssert.assertThat(String.format("[%d] : Value doesn't match", i), ((SimpleJsonParser.Text) kvpair.getValue()).getValue(), Matchers.is(kvpairs[i].getValue()));
		}


	}

	@Test
	public void shouldParseStructuresCorrectly() {
		MatcherAssert.assertThat(((SimpleJsonParser.Text) SimpleJsonParser.parse("'abc'")).getValue(), Matchers.is("abc"));
	}

}
