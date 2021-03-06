/*********************************************************************
*  Copyright (c) 2017 ModelSolv, Inc. and others.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
 *
 *  Contributors:
 *     ModelSolv, Inc. 
 *     - initial API and implementation and/or initial documentation
**********************************************************************/
package com.reprezen.jsonoverlay;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SerializationOptions {
	public enum Option {
		KEEP_EMPTY, KEEP_ONE_EMPTY, FOLLOW_REFS;
	}

	public final static SerializationOptions EMPTY = new SerializationOptions();
	private final Set<SerializationOptions.Option> options;

	public SerializationOptions(SerializationOptions.Option... options) {
		this.options = new HashSet<>(Arrays.asList(options));
	}

	public SerializationOptions(Collection<SerializationOptions.Option> options) {
		this.options = new HashSet<>(options);
	}

	public SerializationOptions plus(Collection<SerializationOptions.Option> addOptions) {
		Set<SerializationOptions.Option> newOptions = new HashSet<>(this.options);
		newOptions.addAll(addOptions);
		return new SerializationOptions(newOptions);
	}

	public SerializationOptions plus(SerializationOptions.Option... addOptions) {
		return plus(Arrays.asList(addOptions));
	}

	public SerializationOptions minus(Collection<SerializationOptions.Option> removeOptions) {
		Set<SerializationOptions.Option> newOptions = new HashSet<>(this.options);
		newOptions.removeAll(removeOptions);
		return new SerializationOptions(newOptions);
	}

	public SerializationOptions minus(SerializationOptions.Option... removeOptions) {
		return minus(Arrays.asList(removeOptions));
	}

	public boolean isKeepEmpty() {
		return options.contains(Option.KEEP_EMPTY);
	}

	public boolean isKeepOneEmpty() {
		return options.contains(Option.KEEP_ONE_EMPTY);
	}

	public boolean isKeepThisEmpty() {
		return isKeepEmpty() || isKeepOneEmpty();
	}

	public boolean isFollowRefs() {
		return options.contains(Option.FOLLOW_REFS);
	}
}