package io.tracee.contextlogger.outputgenerator;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import io.tracee.contextlogger.outputgenerator.outputelements.AtomicOutputElement;
import io.tracee.contextlogger.outputgenerator.outputelements.CollectionOutputElement;
import io.tracee.contextlogger.outputgenerator.outputelements.NullValueOutputElement;
import io.tracee.contextlogger.outputgenerator.outputelements.OutputElement;

/**
 * Test class for {@link io.tracee.contextlogger.outputgenerator.RootOutputElementTreeBuilder}.
 */
public class RootOutputElementTreeBuilderTest {

    private RootOutputElementTreeBuilder unit;
    private RecursiveOutputElementTreeBuilder recursiveOutputElementTreeBuilder;

    @Before
    public void init() {
        recursiveOutputElementTreeBuilder = Mockito.mock(RecursiveOutputElementTreeBuilder.class);
        unit = new RootOutputElementTreeBuilder(recursiveOutputElementTreeBuilder);
    }

    @Test
    public void should_handle_zero_instances_correctly() {

        OutputElement outputElement = unit.deserializeContextsMain();
        MatcherAssert.assertThat(outputElement, Matchers.is((OutputElement)NullValueOutputElement.INSTANCE));

        outputElement = unit.deserializeContextsMain(new Object[0]);
        MatcherAssert.assertThat(outputElement, Matchers.is((OutputElement)NullValueOutputElement.INSTANCE));
        Mockito.verify(recursiveOutputElementTreeBuilder, Mockito.never()).convertInstanceRecursively(
                Mockito.any(RecursiveOutputElementTreeBuilderState.class), Mockito.any(Object.class));

    }

    @Test
    public void should_handle_single_instance_correctly() {
        AtomicOutputElement atomicOutputElement = new AtomicOutputElement(String.class, "ABC");
        Mockito.when(
                recursiveOutputElementTreeBuilder.convertInstanceRecursively(Mockito.any(RecursiveOutputElementTreeBuilderState.class), Mockito.eq("ABC")))
                .thenReturn(atomicOutputElement);

        OutputElement outputElement = unit.deserializeContextsMain("ABC");
        MatcherAssert.assertThat(outputElement, Matchers.instanceOf(AtomicOutputElement.class));
        MatcherAssert.assertThat(outputElement, Matchers.is((OutputElement)atomicOutputElement));

        Mockito.verify(recursiveOutputElementTreeBuilder, Mockito.times(1)).convertInstanceRecursively(
                Mockito.any(RecursiveOutputElementTreeBuilderState.class), Mockito.eq("ABC"));
    }

    @Test
    public void should_handle_single_instance_correctly_if_recursive_output_tree_builder_returns_null() {
        AtomicOutputElement atomicOutputElement = null;
        Mockito.when(
                recursiveOutputElementTreeBuilder.convertInstanceRecursively(Mockito.any(RecursiveOutputElementTreeBuilderState.class), Mockito.eq("ABC")))
                .thenReturn(atomicOutputElement);

        OutputElement outputElement = unit.deserializeContextsMain("ABC");
        MatcherAssert.assertThat(outputElement, Matchers.is((OutputElement)NullValueOutputElement.INSTANCE));

        Mockito.verify(recursiveOutputElementTreeBuilder, Mockito.times(1)).convertInstanceRecursively(
                Mockito.any(RecursiveOutputElementTreeBuilderState.class), Mockito.eq("ABC"));
    }

    @Test
    public void should_handle_multiple_instances_correctly() {
        AtomicOutputElement atomicOutputElement1 = new AtomicOutputElement(String.class, "ABC");

        AtomicOutputElement atomicOutputElement2 = null;
        Mockito.when(
                recursiveOutputElementTreeBuilder.convertInstanceRecursively(Mockito.any(RecursiveOutputElementTreeBuilderState.class), Mockito.eq("ABC")))
                .thenReturn(atomicOutputElement1);
        Mockito.when(
                recursiveOutputElementTreeBuilder.convertInstanceRecursively(Mockito.any(RecursiveOutputElementTreeBuilderState.class), Mockito.eq("DEF")))
                .thenReturn(atomicOutputElement2);

        CollectionOutputElement outputElement = (CollectionOutputElement)unit.deserializeContextsMain("ABC", "DEF");
        MatcherAssert.assertThat(outputElement.getOutputElements(), Matchers.hasSize(1));
        MatcherAssert.assertThat(outputElement.getOutputElements().get(0), Matchers.is((OutputElement)atomicOutputElement1));

        Mockito.verify(recursiveOutputElementTreeBuilder, Mockito.times(1)).convertInstanceRecursively(
                Mockito.any(RecursiveOutputElementTreeBuilderState.class), Mockito.eq("ABC"));
        Mockito.verify(recursiveOutputElementTreeBuilder, Mockito.times(1)).convertInstanceRecursively(
                Mockito.any(RecursiveOutputElementTreeBuilderState.class), Mockito.eq("DEF"));
    }

}
