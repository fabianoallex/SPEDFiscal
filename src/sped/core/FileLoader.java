package sped.core;

import java.io.InputStream;

public interface FileLoader {
    InputStream getInputStream(String fromFile);
}
