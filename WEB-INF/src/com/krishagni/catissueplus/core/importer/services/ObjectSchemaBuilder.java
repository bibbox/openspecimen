package com.krishagni.catissueplus.core.importer.services;

import java.util.Map;

import com.krishagni.catissueplus.core.importer.domain.ObjectSchema;

public interface ObjectSchemaBuilder {
	ObjectSchema getObjectSchema(Map<String, String> params);

	String insertAdditionalFields(String path);
}
