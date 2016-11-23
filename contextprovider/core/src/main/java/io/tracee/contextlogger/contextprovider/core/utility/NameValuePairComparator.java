package io.tracee.contextlogger.contextprovider.core.utility;

import java.util.Comparator;

/**
 * Comparator for {@link NameValuePair}.
 */
public final class NameValuePairComparator implements Comparator<NameValuePair> {

	@Override
	public int compare(NameValuePair instance1, NameValuePair instance2) {

		// primary sort criteria is the order value of the annotation
		if (instance1 == null && instance2 == null) {
			return 0;
		} else if (instance1 != null && instance2 == null) {
			return -1;
		} else if (instance1 == null) {
			return 1;
		} else {

			return compareNames(instance1.getName(), instance2.getName());

		}

	}

	public int compareNames(final String name1, final String name2) {
		if (name1 == null && name2 == null) {
			return 0;
		} else if (name1 != null && name2 == null) {
			return -1;
		} else if (name1 == null) {
			return 1;
		} else {
			return name1.compareTo(name2);
		}
	}

}
