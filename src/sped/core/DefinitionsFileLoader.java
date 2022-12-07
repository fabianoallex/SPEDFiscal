package sped.core;

import java.io.InputStream;

public interface DefinitionsFileLoader {
    InputStream getInputStream(String fromFile);
}
