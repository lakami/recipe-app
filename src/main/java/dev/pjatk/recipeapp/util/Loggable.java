package dev.pjatk.recipeapp.util;

import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

public interface Loggable {
    default Logger log() {
        return getLogger(getClass());
    }
}
