package com.krishagni.catissueplus.core.common.domain;

import org.springframework.beans.factory.annotation.Autowired;

import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;

public abstract class AbstractLetterSequenceToken<T> extends AbstractLabelTmplToken  {
	protected String name;

	@Autowired
	protected DaoFactory daoFactory;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getReplacement(Object object) {
		String alphabet = getAlphabet();
		Integer seq = getSequence((T) object);
		if (seq == null) {
			return null;
		}

		StringBuilder result = new StringBuilder();
		int num = seq;
		while (num > 0) {
			int letterIdx = (num - 1) % alphabet.length();
			result.insert(0, alphabet.charAt(letterIdx));
			num = (num - (letterIdx + 1)) / alphabet.length();
		}

		return result.toString();
	}

	public int validate(Object object, String input, int startIdx, String ... args) {
		while (startIdx < input.length() && getAlphabet().indexOf(input.charAt(startIdx)) != -1) {
			++startIdx;
		}

		return startIdx;
	}

	protected Integer getUniqueId(String id) {
		if (id == null) {
			return null;
		}

		Long count = daoFactory.getUniqueIdGenerator().getUniqueId(getName(), id);
		return count.intValue();
	}

	protected abstract Integer getSequence(T object);

	protected abstract String getAlphabet();
}