package com.checkers.model;

import java.io.IOException;

public class FileFormatException extends IOException {
    public FileFormatException(String message) {
        super(message);
    }

    public FileFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
