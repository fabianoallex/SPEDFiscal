package sped.core;

import java.util.*;

public class Definitions {
    public static final String REGISTER_FIELD_SEPARATOR_DEFAULT = "|";
    public static final String REGISTER_FIELD_BEGIN_END_SEPARATOR_DEFAULT = REGISTER_FIELD_SEPARATOR_DEFAULT;
    public static final String DEF_TAG_DEFINITIONS = "definitions";
    public static final String DEF_TAG_REGISTER = "register";
    public static final String DEF_TAG_FIELD = "field";
    public static final String DEF_TAG_FIELDS = "fields";
    public static final String DEF_TAG_REGEXS = "regexs";
    public static final String DEF_TAG_REGEX = "regex";
    public static final String DEF_TAG_SCRIPT = "script";
    public static final String FIELDS_REG_TYPE_STRING = "string";
    public static final String FIELDS_REG_TYPE_NUMBER = "number";
    public static final String FIELDS_REG_TYPE_DATE = "date";

    private final HashMap<String, FieldFormat> fieldsFormat = new HashMap<>();
    private final HashMap<String, Validation> validations = new HashMap<>();
    private volatile HashMap<String, FieldDefinitions[]> fieldsDefinitions = null;
    private volatile HashMap<String, RegisterDefinitions> registersDefinitions = null;
    private final String xmlFile;
    private FileLoader fileLoader;
    private ValidationHelper validationHelper;
    private String fieldsSeparator = REGISTER_FIELD_SEPARATOR_DEFAULT;
    private String beginEndSeparator = REGISTER_FIELD_BEGIN_END_SEPARATOR_DEFAULT;

    public Definitions(String xmlFile) {
        this.xmlFile = xmlFile;
    }
    public void setValidationHelper(ValidationHelper validationHelper) {
        this.validationHelper = validationHelper;
    }

    public void setBeginEndSeparator(String registerBeginEndSeparator) {
        this.beginEndSeparator = registerBeginEndSeparator;
    }

    public void setFieldsSeparator(String fieldsSeparator) {
        this.fieldsSeparator = fieldsSeparator;
    }

    public void setFileLoader(FileLoader fileLoader) {
        this.fileLoader = fileLoader;
    }

    public FileLoader getDefinitionsFileLoader() {
        return this.fileLoader;
    }

    public String getFieldsSeparator() {
        return fieldsSeparator;
    }

    public String getBeginEndSeparator() {
        return beginEndSeparator;
    }

    public ValidationHelper getValidationHelper() {
        return validationHelper;
    }

    protected void addValidation(Validation validation) {
        validations.put(validation.getName(), validation);
    }

    public List<Validation> getValidationsForField(String registerName, String fieldName) {
        List<Validation> validationList = new ArrayList<>();

        String validationNames = Arrays.stream(fieldsDefinitions.get(registerName))
                .filter(fieldDefinitions -> fieldDefinitions.name.equals(fieldName))
                .map(fieldDefinitions ->
                        fieldDefinitions.validationNames == null ? "" : fieldDefinitions.validationNames)
                .findAny()
                .orElse("");

        if (validationNames.trim().equals(""))
            return validationList;

        String[] validationNamesArray = validationNames.split(",");

        Arrays.stream(validationNamesArray)
                .map(String::trim)
                .forEach(validationName -> {
                    Validation validation = validations.get(validationName);

                    if (validation == null) {
                        validation = new ReflectionValidation(getValidationHelper(), validationName);
                        addValidation(validation);
                    }

                    validationList.add(validation);
                });

        return validationList;
    }

    public String getRequired(String registerName, String fieldName) {
        return Arrays.stream(fieldsDefinitions.get(registerName))
                .filter(fieldDefinitions -> fieldDefinitions.name.equals(fieldName))
                .map(definitions -> definitions.required)
                .findAny()
                .orElse("");
    }

    public void addFieldFormat(String name, FieldFormat fieldFormat) {
        fieldsFormat.put(name, fieldFormat);
    }

    public void addFieldDefinitions(String name, FieldDefinitions[] fieldDefinitions) {
        if (fieldsDefinitions == null) fieldsDefinitions = new HashMap<>();
        fieldsDefinitions.put(name, fieldDefinitions);
    }

    public void addRegisterDefinitions(String name, RegisterDefinitions registerDefinitions) {
        if (registersDefinitions == null) registersDefinitions = new HashMap<>();
        registersDefinitions.put(name, registerDefinitions);
    }

    public FieldDefinitions[] getFieldsDefinitions(String name) {
        if (fieldsDefinitions == null) {
            synchronized (Definitions.class) {
                if (fieldsDefinitions == null) {
                    fieldsDefinitions = new HashMap<>();
                    DefinitionsLoader.load(this);
                }
            }
        }

        return fieldsDefinitions.get(name);
    }

    public FieldFormat getFieldFormatByFieldName(String fieldName) {
        return fieldsFormat.get(fieldName);
    }

    public String getXmlFile() {
        return xmlFile;
    }

    RegisterDefinitions getRegisterDefinitions(String registerName) {
        if (registersDefinitions == null) {
            synchronized(this) {
                if (registersDefinitions == null) {
                    registersDefinitions = new HashMap<>();
                    DefinitionsLoader.load(this);
                }
            }
        }

        return registersDefinitions.get(registerName);
    }

    public Factory newFactory() {
        return new Factory(this);
    }

    public static class Builder {
        private final String xmlFile;
        private String fieldsSeparator = REGISTER_FIELD_SEPARATOR_DEFAULT;
        private String beginEndSeparator = REGISTER_FIELD_BEGIN_END_SEPARATOR_DEFAULT;
        private FileLoader fileLoader;
        private ValidationHelper validationHelper;

        Builder(String xmlFile) {
            this.xmlFile = xmlFile;
        }

        public Builder setBeginEndSeparator(String registerBeginEndSeparator) {
            this.beginEndSeparator = registerBeginEndSeparator;
            return this;
        }

        public Builder setFieldsSeparator(String fieldsSeparator) {
            this.fieldsSeparator = fieldsSeparator;
            return this;
        }

        public Builder setFileLoader(FileLoader fileLoader) {
            this.fileLoader = fileLoader;
            return this;
        }

        public Builder setValidationHelper(ValidationHelper validationHelper) {
            this.validationHelper = validationHelper;
            return this;
        }

        public Definitions build(){
            var definitions = new Definitions(this.xmlFile);

            definitions.setBeginEndSeparator(this.beginEndSeparator);
            definitions.setFieldsSeparator(this.fieldsSeparator);
            definitions.setFileLoader(this.fileLoader);
            definitions.setValidationHelper(this.validationHelper);

            return definitions;
        }
    }

    public static Builder newBuilder(String xmlFile) {
        return new Builder(xmlFile);
    }
}