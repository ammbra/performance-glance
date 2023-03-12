package org.acme.example.jfr;

import java.util.Set;
import java.util.regex.Pattern;

import jdk.jfr.SettingControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PathProcessorControl extends SettingControl {

    Logger logger = LoggerFactory.getLogger(CustomMetricsListener.class);
    private Pattern pattern = Pattern.compile(".*");

    @Override
    public void setValue(String value) {
        this.pattern = Pattern.compile(value);
    }

    @Override
    public String combine(Set<String> values) {
        return String.join("|", values);
    }

    @Override
    public String getValue() {
        return pattern.toString();
    }

    public boolean matches(String s) {
        return pattern.matcher(s).matches();
    }
}
