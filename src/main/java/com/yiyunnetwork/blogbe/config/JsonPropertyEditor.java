package com.yiyunnetwork.blogbe.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import java.beans.PropertyEditorSupport;

@RequiredArgsConstructor
public class JsonPropertyEditor<T> extends PropertyEditorSupport {
    private final ObjectMapper objectMapper;
    private final Object typeReference;

    public JsonPropertyEditor(ObjectMapper objectMapper, Class<T> targetType) {
        this.objectMapper = objectMapper;
        this.typeReference = targetType;
    }

    public JsonPropertyEditor(ObjectMapper objectMapper, TypeReference<T> typeReference) {
        this.objectMapper = objectMapper;
        this.typeReference = typeReference;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        try {
            Object value;
            if (typeReference instanceof Class) {
                value = objectMapper.readValue(text, (Class<?>) typeReference);
            } else {
                value = objectMapper.readValue(text, (TypeReference<?>) typeReference);
            }
            setValue(value);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to convert JSON string to object: " + e.getMessage(), e);
        }
    }
} 