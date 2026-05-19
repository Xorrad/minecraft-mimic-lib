package me.mimic.lib.commands;

import java.util.function.Function;

public class CommandParameter {

    private String label;
    private Type type;
    private Object defaultValue;

    public CommandParameter(String label, Type type) {
        this(label, type, null);
    }
    public CommandParameter(String label, Type type, Object defaultValue) {
        this.label = label;
        this.type = type;
        this.defaultValue = defaultValue;
    }

    public String getLabel() {
        return label;
    }

    public Type getType() {
        return this.type;
    }

    public boolean hasDefault() {
        return this.defaultValue != null;
    }

    public Object getDefault() {
        return this.defaultValue;
    }

    public enum Type {

        STRING(str -> str, str -> true),
        INT(Integer::parseInt, str -> {
            try {
                Integer.parseInt(str);
                return true;
            } catch (Exception e) {
                return false;
            }
        }),
        FLOAT(Float::parseFloat, str -> {
            try {
                Float.parseFloat(str);
                return true;
            } catch (Exception e) {
                return false;
            }
        });;

        public Function<String, Object> parse;
        public Function<String, Boolean> valid;

        Type(Function<String, Object> parse, Function<String, Boolean> valid) {
            this.parse = parse;
            this.valid = valid;
        }

        public boolean isValid(String str) {
            return this.valid.apply(str);
        }

        public Object parse(String str) {
            return this.parse.apply(str);
        }
    }
}
